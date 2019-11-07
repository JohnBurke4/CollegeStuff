import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//-------------------------------------------------------------------------
/**
 *  Test class for Doubly Linked List
 *
 *  @version 3.1 09/11/15 11:32:15
 *
 *  @author  John Burke 18326420
 */

@RunWith(JUnit4.class)
public class BSTTest
{
    /** <p>Test {@link BST#prettyPrintKeys()}.</p> */

    @Test
    public void testPrettyPrint() {
        BST<Integer, Integer> bst = new BST<Integer, Integer>();
        assertEquals("Checking pretty printing of empty tree",
                "-null\n", bst.prettyPrintKeys());

        //  -7
        //   |-3
        //   | |-1
        //   | | |-null
        bst.put(7, 7);       //   | |  -2
        bst.put(8, 8);       //   | |   |-null
        bst.put(3, 3);       //   | |    -null
        bst.put(1, 1);       //   |  -6
        bst.put(2, 2);       //   |   |-4
        bst.put(6, 6);       //   |   | |-null
        bst.put(4, 4);       //   |   |  -5
        bst.put(5, 5);       //   |   |   |-null
        //   |   |    -null
        //   |    -null
        //    -8
        //     |-null
        //      -null

        String result =
                "-7\n" +
                        " |-3\n" +
                        " | |-1\n" +
                        " | | |-null\n" +
                        " | |  -2\n" +
                        " | |   |-null\n" +
                        " | |    -null\n" +
                        " |  -6\n" +
                        " |   |-4\n" +
                        " |   | |-null\n" +
                        " |   |  -5\n" +
                        " |   |   |-null\n" +
                        " |   |    -null\n" +
                        " |    -null\n" +
                        "  -8\n" +
                        "   |-null\n" +
                        "    -null\n";
        assertEquals("Checking pretty printing of non-empty tree", result, bst.prettyPrintKeys());
    }


    /** <p>Test {@link BST#delete(Comparable)}.</p> */
    @Test
    public void testDelete() {
        BST<Integer, Integer> bst = new BST<Integer, Integer>();
        bst.delete(1);
        assertEquals("Deleting from empty tree", "()", bst.printKeysInOrder());

        bst.put(7, 7);   //        _7_
        bst.put(8, 8);   //      /     \
        bst.put(3, 3);   //    _3_      8
        bst.put(1, 1);   //  /     \
        bst.put(2, 2);   // 1       6
        bst.put(6, 6);   //  \     /
        bst.put(4, 4);   //   2   4
        bst.put(5, 5);   //        \
        //         5

        assertEquals("Checking order of constructed tree",
                "(((()1(()2()))3((()4(()5()))6()))7(()8()))", bst.printKeysInOrder());

        bst.delete(9);
        assertEquals("Deleting non-existent key",
                "(((()1(()2()))3((()4(()5()))6()))7(()8()))", bst.printKeysInOrder());

        bst.delete(8);
        assertEquals("Deleting leaf", "(((()1(()2()))3((()4(()5()))6()))7())", bst.printKeysInOrder());

        bst.delete(6);
        assertEquals("Deleting node with single child",
                "(((()1(()2()))3(()4(()5())))7())", bst.printKeysInOrder());

        bst.delete(3);
        assertEquals("Deleting node with two children",
                "(((()1())2(()4(()5())))7())", bst.printKeysInOrder());

    }

    @Test
    public void testHeight(){
        BST<Integer, Integer> bst = new BST<Integer, Integer>();
        assertEquals("Checking height of null BST",-1, bst.height());
        bst.put(5, 1);
        assertEquals("Height of single node BST", 0, bst.height());
        bst.put(2, 1);
        assertEquals("Height of BST",1, bst.height());
        bst.put(8, 1);
        assertEquals("Height of BST", 1, bst.height());
        bst.put(1, 1);
        assertEquals("Height of BST", 2, bst.height());
        bst.put(7, 1);
        assertEquals("Height of BST", 2, bst.height());
        bst.put(6, 1);
        assertEquals("Height of BST", 3, bst.height());
        bst.put(9, 1);
        assertEquals("Height of BST", 3, bst.height());
    }

    @Test
    public void testMedian(){
        BST<Integer, Integer> bst = new BST<Integer, Integer>();
        assertNull("Median of empty BST", bst.median());
        bst.put(5, 1);
        assertEquals("Median of single node bst", Integer.valueOf(5), bst.median());
        bst.put(2, 1);
        assertEquals("Median of single node bst", Integer.valueOf(2), bst.median());
        bst.put(8, 1);
        assertEquals("Median of single node bst", Integer.valueOf(5), bst.median());
        bst.put(1, 1);
        assertEquals("Median of single node bst", Integer.valueOf(2), bst.median());
        bst.put(7, 1);
        assertEquals("Median of single node bst", Integer.valueOf(5), bst.median());
        bst.put(6, 1);
        assertEquals("Median of single node bst", Integer.valueOf(5), bst.median());
        bst.put(9, 1);
        assertEquals("Median of single node bst", Integer.valueOf(6), bst.median());
    }

    @Test
    public void testContains(){
        BST<Integer, Integer> bst = new BST<Integer, Integer>();
        bst.put(5, 1);
        assertTrue("Contains: ", bst.contains(5));
        bst.put(2, 5);
        assertTrue("Contains: ", bst.contains(2));
        bst.put(1, 4);
        assertTrue("Contains: ", bst.contains(1));
        bst.put(8, 1);
        assertTrue("Contains: ", bst.contains(8));
        bst.put(7, 5);
        assertTrue("Contains: ", bst.contains(7));
        bst.put(6, 4);
        assertTrue("Contains: ", bst.contains(6));
        bst.put(9, 1);
        assertTrue("Contains: ", bst.contains(9));
        bst.put(3, 2);
        assertTrue("Contains: ", bst.contains(3));
        bst.put(4, 1);
        assertTrue("Contains: ", bst.contains(4));
        assertFalse("Should not contain",bst.contains(30));
    }

    @Test
    public void testReplace(){
        BST<Integer, Integer> bst = new BST<Integer, Integer>();
        bst.put(5, 1);
        bst.put(5, 2);
        assertEquals("Replaced", Integer.valueOf(2), bst.get(5));
        bst.put(2, 4);
        bst.put(3, 5);
        bst.put(2, null);
        assertFalse("Replaced with null", bst.contains(2));
    }

}

