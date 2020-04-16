import org.junit.Assert;
import org.junit.Test;

public class CompetitionTests {

    @Test
    public void testDijkstraConstructor() {
        CompetitionDijkstra cd = new CompetitionDijkstra("tinyEWD.txt", 50, 60, 70);
        Assert.assertEquals("Correct distance not found",1.86, cd.longestTime, 0.0005);

        cd = new CompetitionDijkstra("input-A.txt", 50, 60, 70);
        Assert.assertEquals("Invalid distance not found", Double.POSITIVE_INFINITY, cd.longestTime, 0.0005);
    }

    @Test
    public void testDijkstraConstructorEdgeCases() {
        CompetitionDijkstra cd = new CompetitionDijkstra("tinyEWD.txt", 50, 60, 70);
        Assert.assertTrue("Graph not built properly", cd.allGood);

        cd = new CompetitionDijkstra("input-J.txt", 50, 60, 70);
        Assert.assertFalse("Did not detect graph with 0 vertices", cd.allGood);

        cd = new CompetitionDijkstra("tinyEWD.txt", 40, 60, 70);
        Assert.assertFalse("Did not detect sA less than 50", cd.allGood);

        cd = new CompetitionDijkstra("tinyEWD.txt", 110, 60, 70);
        Assert.assertFalse("Did not detect sA greater than 110", cd.allGood);

        cd = new CompetitionDijkstra("tinyEWD.txt", 50, 40, 70);
        Assert.assertFalse("Did not detect sB less than 50", cd.allGood);

        cd = new CompetitionDijkstra("tinyEWD.txt", 50, 1100, 70);
        Assert.assertFalse("Did not detect sB greater than 110", cd.allGood);

        cd = new CompetitionDijkstra("tinyEWD.txt", 50, 60, 40);
        Assert.assertFalse("Did not detect sC less than 50", cd.allGood);

        cd = new CompetitionDijkstra("tinyEWD.txt", 50, 60, 110);
        Assert.assertFalse("Did not detect sC greater than 110", cd.allGood);

        cd = new CompetitionDijkstra(null, 50, 60, 70);
        Assert.assertFalse("Did not detect null file", cd.allGood);

        cd = new CompetitionDijkstra("invalidFile", 50, 60, 70);
        Assert.assertFalse("Did not detect invalid file", cd.allGood);

    }

    @Test
    public void testDijkstraTime(){
        CompetitionDijkstra cd = new CompetitionDijkstra("tinyEWD.txt", 50, 60, 70);
        Assert.assertEquals("Correct time not found",38, cd.timeRequiredforCompetition());

        cd = new CompetitionDijkstra("input-A.txt", 50, 60, 70);
        Assert.assertEquals("Invalid time not found", -1, cd.timeRequiredforCompetition());
    }

    @Test
    public void testFWConstructor() {
        CompetitionFloydWarshall cfw = new CompetitionFloydWarshall("tinyEWD.txt", 50, 60, 70);
        Assert.assertEquals("Correct distance not found",1.86, cfw.maxDistance, 0.0005);

        cfw = new CompetitionFloydWarshall("input-A.txt", 50, 60, 70);
        Assert.assertEquals("Invalid distance not found", Double.POSITIVE_INFINITY, cfw.maxDistance, 0.0005);
    }

    @Test
    public void testFWEdgeCases() {
        CompetitionFloydWarshall cfw = new CompetitionFloydWarshall("tinyEWD.txt", 50, 60, 70);
        Assert.assertTrue("Graph not built properly", cfw.allGood);

        cfw = new CompetitionFloydWarshall("input-J.txt", 50, 60, 70);
        Assert.assertFalse("Did not detect graph with 0 vertices", cfw.allGood);

        cfw = new CompetitionFloydWarshall("tinyEWD.txt", 40, 60, 70);
        Assert.assertFalse("Did not detect sA less than 50", cfw.allGood);

        cfw = new CompetitionFloydWarshall("tinyEWD.txt", 110, 60, 70);
        Assert.assertFalse("Did not detect sA greater than 110", cfw.allGood);

        cfw = new CompetitionFloydWarshall("tinyEWD.txt", 50, 40, 70);
        Assert.assertFalse("Did not detect sB less than 50", cfw.allGood);

        cfw = new CompetitionFloydWarshall("tinyEWD.txt", 50, 1100, 70);
        Assert.assertFalse("Did not detect sB greater than 110", cfw.allGood);

        cfw = new CompetitionFloydWarshall("tinyEWD.txt", 50, 60, 40);
        Assert.assertFalse("Did not detect sC less than 50", cfw.allGood);

        cfw = new CompetitionFloydWarshall("tinyEWD.txt", 50, 60, 110);
        Assert.assertFalse("Did not detect sC greater than 110", cfw.allGood);

        cfw = new CompetitionFloydWarshall(null, 50, 60, 70);
        Assert.assertFalse("Did not detect null file", cfw.allGood);

        cfw = new CompetitionFloydWarshall("invalidFile", 50, 60, 70);
        Assert.assertFalse("Did not detect invalid file", cfw.allGood);

    }

    @Test
    public void testFWTime(){
        CompetitionFloydWarshall cfw = new CompetitionFloydWarshall("tinyEWD.txt", 50, 60, 70);
        Assert.assertEquals("Correct time not found",38, cfw.timeRequiredforCompetition());

        cfw = new CompetitionFloydWarshall("input-A.txt", 50, 60, 70);
        Assert.assertEquals("Invalid time not found", -1, cfw.timeRequiredforCompetition());
    }

}