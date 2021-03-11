import tkinter as tk
import threading
from proxy import Proxy


class GUI:

    config = {
        'HOST_NAME': '0.0.0.0',
        'BIND_PORT': 12345,
        'MAX_REQUEST_LENGTH': 8192,
        'CONNECTION_TIMEOUT': 15,
        'TTL': 10080
    }

    def __init__(self):

        self.proxyStarted = False
        self.m = tk.Tk()
        self.m.geometry('900x600')
        self.currentBlacklistUrl = tk.StringVar()

        self.timeSaved = tk.StringVar()
        self.timeSaved.set('Time saved by cache: 0s')
        self.bandwidthSaved = tk.StringVar()
        self.bandwidthSaved.set('Network traffic saved by cache:\n 0 KB')

        self.proxy = Proxy(self.config, self.timeSaved, self.bandwidthSaved)

        self.leftScreenSide = tk.Frame(self.m)

        self.startProxyButton = tk.Button(
            self.leftScreenSide, text='Start Proxy', width='25', command=lambda: self.startProxy(), state=tk.NORMAL)
        self.startProxyButton.pack(side='top', padx='5', pady='5')

        self.stopProxyButton = tk.Button(
            self.leftScreenSide, text='Stop Proxy', width='25', command=lambda: self.stopProxy(), state=tk.DISABLED)
        self.stopProxyButton.pack(side='top', padx='5')

        self.addToBlacklistButton = tk.Button(
            self.leftScreenSide, text='Add to blacklist', width='25', command=lambda: self.addToBlacklist())
        self.addToBlacklistButton.pack(side='top', padx='5', pady='5')

        self.removeFromBlacklistButton = tk.Button(
            self.leftScreenSide, text='Remove from blacklist', width='25', command=lambda: self.removeFromBlacklist())
        self.removeFromBlacklistButton.pack(side='top', padx='5')

        self.blacklistEntry = tk.Entry(
            self.leftScreenSide, width='30', textvariable=self.currentBlacklistUrl)
        self.blacklistEntry.pack(side='top', padx='5', pady='5')

        self.blacklistText = tk.Label(
            self.leftScreenSide, width='30', text='Blacklist')
        self.blacklistText.pack(side='top', padx='5')

        self.leftScreenSide.pack(side='left', fill=tk.Y)

        self.blacklistBox = tk.Listbox(
            self.leftScreenSide, width=10, selectmode=tk.SINGLE)
        self.blacklistBox.pack(fill='both', expand=1, side='top')

        self.cacheTimeSavedText = tk.Label(
            self.leftScreenSide, width='30', textvariable=self.timeSaved)
        self.cacheTimeSavedText.pack(side='top', padx='5', pady='5')

        self.cacheBandwidthSavedText = tk.Label(
            self.leftScreenSide, width='30', textvariable=self.bandwidthSaved)
        self.cacheBandwidthSavedText.pack(side='bottom', padx='5', pady='5')

        self.proxyResponseBox = tk.Listbox(self.m, width=10)
        self.proxyResponseBox.pack(fill='both', expand=2, side='right')

        self.m.mainloop()

    def startProxy(self):
        if (not self.proxyStarted):
            self.proxy.initialise()
            self.proxyStarted = True
            self.startProxyButton['state'] = tk.DISABLED
            self.stopProxyButton['state'] = tk.NORMAL
            self.proxyResponseBox.insert(tk.END, 'Starting proxy')
            process = threading.Thread(
                target=lambda: self.proxy.listenForClients(self.proxyResponseBox))
            process.start()

    def stopProxy(self):
        if (self.proxyStarted):
            self.proxyStarted = False
            self.startProxyButton['state'] = tk.NORMAL
            self.stopProxyButton['state'] = tk.DISABLED
            self.proxyResponseBox.insert(tk.END, 'Shutting down proxy')
            self.proxy.shutdown()

    def addToBlacklist(self):
        url = self.currentBlacklistUrl.get()
        self.proxy.addToBlacklist(url)
        self.blacklistBox.insert(tk.END, url)
        self.blacklistEntry.delete(0, 'end')

    def removeFromBlacklist(self):

        index = self.blacklistBox.curselection()
        if (index != ()):
            url = self.blacklistBox.get(index[0])
            self.proxy.removeFromBlacklist(url)
            self.blacklistBox.delete(index)


def main():
    GUI()


if __name__ == '__main__':
    main()
