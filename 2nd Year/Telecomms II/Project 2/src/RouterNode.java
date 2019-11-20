import java.util.ArrayList;

public class RouterNode {
    int socket;
    boolean unvisited;
    ArrayList<Link> links;
    int tentativeDistance;
    RouterNode prev;

    public RouterNode(int socket){
        tentativeDistance = 10000;
        unvisited = true;
        this.socket = socket;
        links = new ArrayList<>();
        prev = null;
    }


    public void addLink(RouterNode dest, int distance){
        links.add(new Link(dest, this, distance));
    }

    public static class Link{
        RouterNode dest;
        RouterNode begin;
        int distance;

        public Link(RouterNode dest, RouterNode begin, int distance){
            this.dest = dest;
            this.begin = begin;
            this.distance = distance;
        }
    }
}
