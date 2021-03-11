import signal
import socket
import threading
import sys
import tkinter as tk
import ssl
import time


class Proxy:

    timeSaved = 0.0
    bandwidthSaved = 0

    blockedHTML = """
    <html>
        <head>
        </head>

        <body>
            <h1>This website is blocked by the proxy</h1>
        </body>

    </html>

    """

    def __init__(self, config, timeText, bandwidthText):
        self.timeText = timeText
        self.bandwidthText = bandwidthText
        self.config = config
        self.blacklist = set()
        self.cache = {}

    def initialise(self):

        # Shutdown on Ctrl+C
        signal.signal(signal.SIGINT, self.shutdown)

        # Create a TCP socket
        self.serverSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

        # Re-use the socket
        self.serverSocket.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

        # Bind socket to a public host, and a port
        self.serverSocket.bind(
            (self.config['HOST_NAME'], self.config['BIND_PORT']))

        self.serverSocket.listen(100)  # Server Socket
        self.__clients = {}

    def listenForClients(self, textBox):
        self.textBox = textBox
        while True:
            # Establish the connection
            (clientSocket, clientAddress) = self.serverSocket.accept()

            d = threading.Thread(name=self.getClientName(clientAddress),
                                 target=self.proxyThread, args=(clientSocket, clientAddress))

            d.setDaemon(True)
            d.start()
        self.shutdown(0, 0)

    def proxyThread(self, clientSocket, clientAddress):
        # Get the request from the browser
        request = clientSocket.recv(self.config['MAX_REQUEST_LENGTH'])
        startTime = time.time()

        try:
            # Parse the first line
            firstLine = request.decode().split('\n')[0]
            if (firstLine != ''):
                self.textBox.insert(tk.END, request.decode())
                url = firstLine.split(' ')[1]

                # Find pos of ://
                httpsPos = url.find('://')

                if (httpsPos == -1):
                    temp = url
                else:
                    temp = url[(httpsPos+3):]

                # Find port position
                portPos = temp.find(':')

                # Find end of web server
                webserverPos = temp.find('/')

                if (webserverPos == -1):
                    webserverPos = len(temp)

                webserver = ''

                port = -1
                if (portPos == -1 or webserverPos < portPos):
                    # Default port
                    port = 80
                    webserver = temp[:webserverPos]

                else:
                    # Specific port
                    port = int((temp[(portPos+1):])[:webserverPos-portPos-1])
                    webserver = temp[:portPos]

                if (webserver in self.blacklist):
                    self.textBox.insert(tk.END, "This web address is blocked")
                    clientSocket.sendall(self.blockedHTML.encode())

                    clientSocket.close()
                    return

                cache = self.getFromCache(firstLine)
                if (cache is not None):
                    bandwidth = 0
                    # Sending all the packets from the cache
                    for data in cache[1]:
                        bandwidth += len(data)
                        clientSocket.sendall(data)

                    endTime = time.time()
                    timeElapsed = endTime-startTime
                    currentTimeSaved = cache[2] - timeElapsed

                    self.timeSaved += currentTimeSaved
                    self.bandwidthSaved += bandwidth

                    self.textBox.insert(
                        tk.END, 'Time saved by fetching from cache: {:.5f}'.format(currentTimeSaved))
                    self.textBox.insert(
                        tk.END, 'Bandwidth saved by fetching from cache: {:.5f} KB/s'.format(
                            bandwidth / (cache[2] * 1024)))

                    self.timeText.set(
                        'Time saved by caching: {:.5f}s'.format(self.timeSaved))
                    self.bandwidthText.set(
                        'Network traffic saved by caching:\n {:.3f} KB'.format(self.bandwidthSaved / 1024))
                    clientSocket.close()
                    return

                try:

                    # Connect to destination server
                    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
                    s.settimeout(self.config['CONNECTION_TIMEOUT'])

                    if ("CONNECT" in firstLine):

                        s.connect((webserver, port))
                        # Setting up http tunneling reply
                        reply = "HTTP/1.0 200 Connection established\r\n"
                        reply += "Proxy-agent: JohnsProxy\r\n"
                        reply += "\r\n"

                        clientSocket.sendall(reply.encode())

                        # Setting up websocket connection
                        clientSocket.setblocking(0)
                        s.setblocking(0)

                        while True:
                            try:
                                # Recieving new request
                                newRequest = clientSocket.recv(
                                    self.config['MAX_REQUEST_LENGTH'])
                                # Sending request to the server
                                s.sendall(newRequest)
                            except socket.error as err:
                                pass
                            try:
                                # Recieving reply from the server
                                reply = s.recv(
                                    self.config['MAX_REQUEST_LENGTH'])

                                # Sending reply to the client
                                clientSocket.sendall(reply)
                            except socket.error as err:
                                pass

                    else:
                        s.connect((webserver, port))

                        clientSocket.setblocking(0)
                        s.setblocking(0)

                        s.sendall(request)

                        cache = []

                        while True:
                            try:
                                # Recieve data from web serer
                                data = s.recv(
                                    self.config['MAX_REQUEST_LENGTH'])
                                # Checking if there is more data to come
                                if (len(data) == self.config['MAX_REQUEST_LENGTH']):
                                    cache.append(data)
                                    clientSocket.send(data)
                                # Otherwise cache the response
                                elif (len(data) > 0):
                                    cache.append(data)
                                    clientSocket.send(data)
                                    endTime = time.time()
                                    # Cache the response along with the time it took to fetch and the url
                                    self.addToCache(
                                        firstLine, cache, (endTime - startTime))
                                else:

                                    break
                            except:
                                pass

                    s.close()
                    clientSocket.close()
                except socket.error as errorMsg:
                    print('ERROR: ' + str(errorMsg))
                    if s:
                        s.close()
                    if clientSocket:
                        clientSocket.close()
            else:
                clientSocket.close()
        except:
            pass

    def shutdown(self):
        # Close existing server
        self.serverSocket.close()

    def getClientName(self, clientAddress):
        return str(clientAddress)

    def addToBlacklist(self, url):
        self.blacklist.add(url)

    def removeFromBlacklist(self, url):
        self.blacklist.remove(url)

    def getFromCache(self, request):
        # If the request is HTTPS, there is no cached response
        if ('CONNECT' in request):
            return None

        # Check if the URL is already cached
        cached = self.cache.get(request)
        #
        if (cached is not None):
            # Check if the cached response has expired
            if (cached[0] + self.config['TTL'] > int(time.time())):
                return cached
        return None

    def addToCache(self, request, data, requestTime):
        try:
            self.cache[request] = (
                int(time.time()),
                data,
                requestTime
            )
        except:
            print('something went wrong')
