/*
 * A Contest to Meet (ACM) is a reality TV contest that sets three contestants at three random
 * city intersections. In order to win, the three contestants need all to meet at any intersection
 * of the city as fast as possible.
 * It should be clear that the contestants may arrive at the intersections at different times, in
 * which case, the first to arrive can wait until the others arrive.
 * From an estimated walking speed for each one of the three contestants, ACM wants to determine the
 * minimum time that a live TV broadcast should last to cover their journey regardless of the contestantsâ€™
 * initial positions and the intersection they finally meet. You are hired to help ACM answer this question.
 * You may assume the following:
 *    ï‚· Each contestant walks at a given estimated speed.
 *    ï‚· The city is a collection of intersections in which some pairs are connected by one-way
 * streets that the contestants can use to traverse the city.
 *
 * This class implements the competition using Floyd-Warshall algorithm
 */

import java.io.File;
import java.util.Scanner;

public class CompetitionFloydWarshall {

    /**
     * @param filename: A filename containing the details of the city road network
     * @param sA, sB, sC: speeds for 3 contestants
     */

    private double[][] adj;
    private int V;
    private int E;

    public double maxDistance;
    private int sA;
    private int sB;
    private int sC;

    public boolean allGood = false;

    CompetitionFloydWarshall (String filename, int sA, int sB, int sC){
        fillGraph(filename);
        for(int k = 0; k < V; k++){
            for(int i = 0; i < V; i++){
                for(int j = 0; j < V; j++){
                    if(adj[i][k] + adj[k][j] < adj[i][j]){
                        adj[i][j] = adj[i][k] + adj[k][j];
                    }
                }
            }
        }

        maxDistance = -1;
        for(int i = 0; i < V; i++){
            for(int j = 0; j < V; j++){
                maxDistance = Math.max(adj[i][j], maxDistance);
            }
        }
        this.sA = sA;
        this.sB = sB;
        this.sC = sC;

        if (V == 0 || maxDistance == Double.POSITIVE_INFINITY || sA < 50 || sA > 100 || sB < 50 || sB > 100 || sC < 50 || sC > 100)
            allGood = false;
    }

    private void fillGraph(String filename) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);
            V = Integer.parseInt(myReader.nextLine());
            E = Integer.parseInt(myReader.nextLine());
            adj = new double[V][V];
            for (int i = 0; i < V; i++){
                for(int j = 0; j < V; j++){
                    adj[i][j] = Double.POSITIVE_INFINITY;
                }
            }
            for (int i = 0; i < E; i++) {
                String[] line = myReader.nextLine().split(" ");
                int v1 = Integer.parseInt(line[0]);
                int v2 = Integer.parseInt(line[1]);
                double w = Double.parseDouble(line[2]);
                adj[v1][v2] = w;
            }

            for(int i = 0; i < V; i++){
                adj[i][i] = 0;
            }
            allGood = true;
            myReader.close();
        } catch (Exception e) {
//            System.out.println("An error occurred.");
//            e.printStackTrace();
        }
    }


    /**
     * @return int: minimum minutes that will pass before the three contestants can meet
     */
    public int timeRequiredforCompetition(){
        if (!allGood)
            return -1;

        return (int) Math.ceil((maxDistance * 1000) / Math.min(Math.min(sA, sB), sC));


    }

}