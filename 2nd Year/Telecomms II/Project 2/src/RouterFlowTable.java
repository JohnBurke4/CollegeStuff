import java.util.ArrayList;

public class RouterFlowTable {

    private ArrayList<Flow> flows;

    public RouterFlowTable(){
        flows = new ArrayList<>();
    }

    public void addFlow(int dest, int out, int in){
        flows.add(new Flow(dest, out, in));
    }

    public int getNextHop(int dest){
        for(Flow flow: flows){
            if(flow.compareDest(dest)){
                return flow.getOut();
            }
        }
        return -1;
    }

    private class Flow{
        int dest;
        int out;
        int in;

        public Flow(int dest, int out, int in){
            this.dest = dest;
            this.out = out;
            this.in = in;
        }

        public int getOut(){
            return out;
        }

        public boolean compareDest(int dest){
            return this.dest == dest;
        }
    }
}
