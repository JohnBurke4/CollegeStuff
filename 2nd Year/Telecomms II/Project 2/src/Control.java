import sun.security.krb5.internal.crypto.Aes128;

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
            flowTable = new CommandFlowTable();
            unvisitedNodes = new ArrayList<>();
            createTable();
            createGraph();
            //createShortestPath(40010, 40011);
            //printRoutes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printRoutes(){
        System.out.println(flowTable.toString());
    }

    private void createTable() {
        int e1 = 40010;
        int e2 = 40011;
        int r1 = 40012;
        int r2 = 40013;
        int r3 = 40014;
        int r4 = 40015;
        routerAddresses = new ArrayList<>();
        routerAddresses.add(r1);
        routerAddresses.add(r2);
        routerAddresses.add(r3);
        routerAddresses.add(r4);

//        flowTable.addRoute(e1, e2);
//        flowTable.addFlowToRoute(e1, e2, r1, r2, e1);
//        flowTable.addFlowToRoute(e1, e2, r2, r3, r1);
//        flowTable.addFlowToRoute(e1, e2, r3, e2, r2);
//
//        flowTable.addRoute(e2, e1);
//        flowTable.addFlowToRoute(e2, e1, r3, r2, e2);
//        flowTable.addFlowToRoute(e2, e1, r2, r1, r3);
//        flowTable.addFlowToRoute(e2, e1, r1, e1, r2);
    }

    public void createGraph() {
        RouterNode e1 = new RouterNode(40010);
        RouterNode e2 = new RouterNode(40011);
        RouterNode r1 = new RouterNode(40012);
        RouterNode r2 = new RouterNode(40013);
        RouterNode r3 = new RouterNode(40014);
        RouterNode r4 = new RouterNode(40015);

        allNodes = new ArrayList<>();
        allNodes.add(e1);
        allNodes.add(e2);
        allNodes.add(r1);
        allNodes.add(r2);
        allNodes.add(r3);
        allNodes.add(r4);

        e1.addLink(r1, 20);
        r1.addLink(e1, 20);
//        e1.addLink(r2, 30);
//        r2.addLink(e1, 30);

        r1.addLink(r3, 100);
        r3.addLink(r1, 100);
//        r1.addLink(r4, 110);
//        r4.addLink(r1, 110);

        r2.addLink(r3, 50);
        r3.addLink(r2, 50);

        r2.addLink(r4, 30);
        r4.addLink(r2, 30);

//        r3.addLink(r4, 60);
//        r4.addLink(r3, 60);
//        r3.addLink(e2, 20);
//        e2.addLink(r3, 20);


        r4.addLink(e2, 10);
        e2.addLink(r4, 10);
        r1.addLink(r4, 30);
        //r4.addLink(r1, 40);
    }

    public void createShortestPath(int source, int dest) {
        RouterNode initial = null;
        RouterNode destination = null;
        int distance = 0;
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
                savePathToTable(getPath(destination, ""));
                return;
            }
            double highest = Double.POSITIVE_INFINITY;
            RouterNode nextNode = null;
            for (RouterNode node : unvisitedNodes) {
                if (highest > node.tentativeDistance) {
                    highest = (double) node.tentativeDistance;
                    nextNode = node;
                }
            }

            current = nextNode;
        }
    }

    public String getPath(RouterNode node, String path){
        if (node.prev != null){
            return getPath(node.prev, "|" + node.socket + path);
        }
        return node.socket + path;
    }

    public void savePathToTable(String path){
        String[] hops = path.split("\\|");
        int start = Integer.parseInt(hops[0]);
        int end = Integer.parseInt(hops[hops.length-1]);
//        for (String n: hops){
//            System.out.println(n);
//        }
        //System.out.println(start);
        //System.out.println(end);
        flowTable.addRoute(end, start);
        for(int i = 1; i < hops.length-1; i++){
            int prev = Integer.parseInt(hops[i-1]);
            int current = Integer.parseInt(hops[i]);
            int next = Integer.parseInt(hops[i+1]);

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
                    socket.send(makePacket(packet.getSocketAddress(), null, TYPE_HELLO));
                    break;
                case TYPE_FEATURE_REQUEST:
                    terminal.println("Feature Request Recieved");
                    sendRouteToAllRouters(getDest(data), getSrc(data));
                    break;
                default:
                    terminal.println("Unknown packet type recieved");
            }
        } catch (Exception e) {
            e.printStackTrace();
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
        if (!flowTable.hasRoute(dest, src)){
            createShortestPath(src, dest);
        }
        for (Integer address : routerAddresses) {
            System.out.println("Doing stuff");
            InetSocketAddress router = new InetSocketAddress("localhost", address);
            String message = flowTable.getRouterFlow(dest, address);
            if (message != null){
                System.out.println("Create route");
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
