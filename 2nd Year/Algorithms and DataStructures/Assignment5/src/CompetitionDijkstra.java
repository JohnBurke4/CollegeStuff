/*
 * A Contest to Meet (ACM) is a reality TV contest that sets three contestants at three random
 * city intersections. In order to win, the three contestants need all to meet at any intersection
 * of the city as fast as possible.
 * It should be clear that the contestants may arrive at the intersections at different times, in
 * which case, the first to arrive can wait until the others arrive.
 * From an estimated walking speed for each one of the three contestants, ACM wants to determine the
 * minimum time that a live TV broadcast should last to cover their journey regardless of the contestants’
 * initial positions and the intersection they finally meet. You are hired to help ACM answer this question.
 * You may assume the following:
 *     Each contestant walks at a given estimated speed.
 *     The city is a collection of intersections in which some pairs are connected by one-way
 * streets that the contestants can use to traverse the city.
 *
 * This class implements the competition using Dijkstra's algorithm
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class CompetitionDijkstra {

    private class Bag{
        ArrayList<Edge> bag;

        public Bag(){
            bag = new ArrayList<>();
        }

        public void add(int v2, double w){
            bag.add(new Edge(v2, w));
        }

        public void print(){
            for(Edge e: bag){
                System.out.print(" -> " + e.toString());
            }
            System.out.println();
        }
    }

    private class Edge{
        int v2;
        double w;

        public Edge(int v2, double w){
            this.v2 = v2;
            this.w = w;
        }

        public String toString(){
            return v2 + " " + w;
        }
    }


    private int V;
    private int E;
    private Bag[] adj;
    private boolean[] visited;
    /**
     * @param filename: A filename containing the details of the city road network
     * @param sA, sB, sC: speeds for 3 contestants
     */
    CompetitionDijkstra (String filename, int sA, int sB, int sC){
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            V = Integer.parseInt(myReader.nextLine());
            E = Integer.parseInt(myReader.nextLine());
            adj = new Bag[V];
            for (int i = 0; i < V; i++){
                adj[i] = new Bag();
            }
            visited = new boolean[V];
            for(int i = 0; i < E; i++){
                String[] line = myReader.nextLine().split(" ");
                int v1 = Integer.parseInt(line[0]);
                int v2 = Integer.parseInt(line[1]);
                double w = Double.parseDouble(line[2]);
                adj[v1].add(v2, w);
            }
            myReader.close();
        } catch (Exception e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        for (int i = 0; i < V; i++){
            System.out.print(i + ": ");
            adj[i].print();
        }
        //TODO
    }


    /**
     * @return int: minimum minutes that will pass before the three contestants can meet
     */
    public int timeRequiredforCompetition(){

        //TO DO
        return -1;
    }

    public static void main(String[] args){
        new CompetitionDijkstra("tinyEWD.txt", 1, 2, 3);
    }

}
