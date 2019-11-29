
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;

public class Control extends Node {

    Terminal terminal;
    CommandFlowTable flowTable;
    ArrayList<Integer> routerAddresses;
    ArrayList<RouterNode> unvisitedNodes;
    ArrayList<RouterNode> allNodes;

    public Control(int address, Terminal terminal) {
        try {
            this.terminal = terminal;
            this.socket = new DatagramSocket(address);
            this.listener.go();
            routerAddresses = new ArrayList<>();
            allNodes = new ArrayList<>();
            flowTable = new CommandFlowTable();
            unvisitedNodes = new ArrayList<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printRoutes() {
        System.out.println("Routes\n" + flowTable.toString());
    }

    public void createShortestPath(int source, int dest) {
        RouterNode initial = null;
        RouterNode destination = null;
        for (RouterNode node : allNodes) {
            if (node.socket == source) {
                initial = node;
                initial.tentativeDistance = 0;
            } else {
                node.tentativeDistance = 1000000;
            }

            if (node.socket == dest) {
                destination = node;
            }
            node.prev = null;
            node.unvisited = true;
            unvisitedNodes.add(node);
        }

        RouterNode current = initial;
        while (!unvisitedNodes.isEmpty()) {
            for (RouterNode.Link link : current.links) {
                if (link.dest.unvisited) {
                    int tentDist = current.tentativeDistance + link.distance;
                    if (tentDist < link.dest.tentativeDistance) {
                        link.dest.tentativeDistance = tentDist;
                        link.dest.prev = current;
                    }
                }
            }
            current.unvisited = false;
            unvisitedNodes.remove(current);
            if (current.equals(destination)) {
                if (destination.tentativeDistance == 1000000){
                    terminal.println("No route is found, please restart the program");
                }
                savePathToTable(getPath(destination, ""));
                return;
            }
            double highest = Double.POSITIVE_INFINITY;
            RouterNode nextNode = null;
            for (RouterNode node : unvisitedNodes) {
                if (highest > node.tentativeDistance) {
                    highest = node.tentativeDistance;
                    nextNode = node;
                }
            }

            current = nextNode;
        }
    }

    public String getPath(RouterNode node, String path) {
        if (node.prev != null) {
            return getPath(node.prev, "|" + node.socket + path);
        }
        return node.socket + path;
    }

    public void savePathToTable(String path) {
        String[] hops = path.split("\\|");
        int start = Integer.parseInt(hops[0]);
        int end = Integer.parseInt(hops[hops.length - 1]);
        flowTable.addRoute(end, start);
        for (int i = 1; i < hops.length - 1; i++) {
            int prev = Integer.parseInt(hops[i - 1]);
            int current = Integer.parseInt(hops[i]);
            int next = Integer.parseInt(hops[i + 1]);

            flowTable.addFlowToRoute(end, start, current, next, prev);
        }
    }

    @Override
    public synchronized void sendMessage(SocketAddress address, String message, byte type) {
        try {
            socket.send(makePacket(address, message.getBytes(), type));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sendAck(SocketAddress returnAddress) throws Exception {

    }

    @Override
    public void onReceipt(DatagramPacket packet) {
        try {
            byte[] data = packet.getData();
            switch (data[TYPE_POS]) {
                case TYPE_HELLO:
                    terminal.println("Router Connected");
                    addRouterToMap(packet.getPort());
                    socket.send(makePacket(packet.getSocketAddress(), null, TYPE_HELLO));
                    break;
                case TYPE_FEATURE_REQUEST:
                    terminal.println("Feature Request Recieved");
                    sendRouteToAllRouters(getDest(data), getSrc(data));
                    break;
                case TYPE_USER_HELLO:
                    terminal.println("User connected");
                    int portToSend = addUserToMap(packet.getPort());
                    socket.send(makePacket(packet.getSocketAddress(), String.valueOf(portToSend).getBytes(), TYPE_FEATURE_RESPONSE));
                    break;
                default:
                    terminal.println("Unknown packet type recieved");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addRouterToMap(int port) {
        if (routerAddresses.contains(port)) {
            terminal.println("Router has already connected");
        } else {
            routerAddresses.add(port);
            RouterNode node = new RouterNode(port);
            for (RouterNode rNode : allNodes) {
                int distance = (int) Math.random() * 500;
                if (Math.random() >= 0.3) {
                    node.addLink(rNode, distance);
                    rNode.addLink(node, distance);
                }

            }
            allNodes.add(node);
        }
    }

    public int addUserToMap(int port) {
        RouterNode node = new RouterNode(port);
        while (true){
            for (RouterNode rNode: allNodes){
                int distance = (int) Math.random() * 500;
                if (Math.random() > 0.9 && rNode.socket >= 40010) {
                    allNodes.add(node);
                    node.addLink(rNode, distance);
                    rNode.addLink(node, distance);
                    return rNode.socket;
                }
            }
        }
    }

    public int getDest(byte[] data) {
        byte[] message = new byte[data[LENGTH_POS]];
        System.arraycopy(data, HEADER_LENGTH, message, 0, data[LENGTH_POS]);
        String[] src = new String(message).split("\\|");
        return Integer.parseInt(src[0]);
    }

    public int getSrc(byte[] data) {
        byte[] message = new byte[data[LENGTH_POS]];
        System.arraycopy(data, HEADER_LENGTH, message, 0, data[LENGTH_POS]);
        String[] src = new String(message).split("\\|");
        return Integer.parseInt(src[1]);
    }

    public void sendRouteToAllRouters(int dest, int src) {
        if (!flowTable.hasRoute(dest, src)) {
            createShortestPath(src, dest);
        }
        printRoutes();
        for (Integer address : routerAddresses) {
            InetSocketAddress router = new InetSocketAddress("localhost", address);
            String message = flowTable.getRouterFlow(dest, address);
            if (message != null) {
                sendMessage(router, dest + "," + message, TYPE_FEATURE_RESPONSE);
            }
        }
    }


    @Override
    public void run() {

    }

    @Override
    public void connectToControl() throws Exception {

    }

    public static void main(String[] args) {
        Terminal terminal = new Terminal("Control");
        Control control = new Control(CONTROL_ADDRESS, terminal);
    }
}
