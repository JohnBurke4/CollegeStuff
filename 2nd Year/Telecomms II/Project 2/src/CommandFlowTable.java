import java.util.ArrayList;

public class CommandFlowTable {

    private ArrayList<Route> flowTable;

    public CommandFlowTable(){
        flowTable = new ArrayList<>();
    }

    public void addRoute(int dest, int src){
        flowTable.add(new Route(dest, src));
    }

    public void addFlowToRoute(int dest, int src, int router, int out, int in){
        for (Route route: flowTable){
            if (route.compareRoute(dest)){
                route.addFlow(router, out, in);
            }
        }
    }

    public boolean hasRoute(int dest, int src){
        for (Route route: flowTable){
            if (route.deepCompareRoute(dest, src)){
                return true;
            }
        }
        return false;
    }

    public String getRouterFlow(int dest, int router){
        for (Route route: flowTable){
            if (route.compareRoute(dest)){
                return route.getFlow(router);
            }
        }
        return null;
    }



    @Override
    public String toString(){
        String result = "";
        for (Route route: flowTable){
            result += route.toString() + "\n";
        }
        return result;
    }

    private class Route{
        int dest;
        int src;
        ArrayList<Flow> flows;

        public Route(int dest, int src){
            this.dest = dest;
            this.src = src;
            flows = new ArrayList<>();
        }

        public void addFlow(int router, int out, int in){
            flows.add(new Flow(router, out, in));
        }

        public String getFlow(int router){
            for(Flow flow: flows){
                if (flow.compareRouter(router)){
                    return flow.flowToString();
                }
            }
            return null;
        }

        public boolean deepCompareRoute(int dest, int src){
            return this.dest == dest && this.src == src;
        }

        @Override
        public String toString(){
            String result = "";
            result += "Destination: " + dest + " Source: " + src + "\n";

            for (Flow flow: flows){
                result += flow.toString() + "\n";
            }
            return result;
        }

        public boolean compareRoute(int dest){
            return this.dest==dest;
        }


    }

    private class Flow{
        int router;
        int out;
        int in;

        public Flow(int router, int out, int in){
            this.router = router;
            this.out = out;
            this.in = in;
        }

        public boolean compareRouter(int router){
            return this.router==router;
        }

        public String flowToString(){
            return out + "," + in;
        }

        @Override
        public String toString(){
            return "In: " + in + " Router: " + router + " Out: " + out;
        }

    }
}
