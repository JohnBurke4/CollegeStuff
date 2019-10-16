package main;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

//-------------------------------------------------------------------------
/**
 *  Test class for Doubly Linked List
 *
 *  @author  
 *  @version 13/10/16 18:15
 */
@RunWith(JUnit4.class)
public class DoublyLinkedListTest
{
    //~ Constructor ........................................................
    @Test
    public void testConstructor()
    {
      new DoublyLinkedList<Integer>();
    }

    //~ Public Methods ........................................................

    // ----------------------------------------------------------
    /**
     * Check if the insertBefore works
     */
    @Test
    public void testInsertBefore()
    {
        // test non-empty list
        DoublyLinkedList<Integer> testDLL = new DoublyLinkedList<Integer>();
        testDLL.insertBefore(0,1);
        testDLL.insertBefore(1,2);
        testDLL.insertBefore(2,3);

        testDLL.insertBefore(0,4);
        assertEquals( "Checking insertBefore to a list containing 3 elements at position 0", "4,1,2,3", testDLL.toString() );
        testDLL.insertBefore(1,5);
        assertEquals( "Checking insertBefore to a list containing 4 elements at position 1", "4,5,1,2,3", testDLL.toString() );
        testDLL.insertBefore(2,6);       
        assertEquals( "Checking insertBefore to a list containing 5 elements at position 2", "4,5,6,1,2,3", testDLL.toString() );
        testDLL.insertBefore(-1,7);        
        assertEquals( "Checking insertBefore to a list containing 6 elements at position -1 - expected the element at the head of the list", "7,4,5,6,1,2,3", testDLL.toString() );
        testDLL.insertBefore(7,8);        
        assertEquals( "Checking insertBefore to a list containing 7 elemenets at position 8 - expected the element at the tail of the list", "7,4,5,6,1,2,3,8", testDLL.toString() );
        testDLL.insertBefore(700,9);        
        assertEquals( "Checking insertBefore to a list containing 8 elements at position 700 - expected the element at the tail of the list", "7,4,5,6,1,2,3,8,9", testDLL.toString() );

        // test empty list
        testDLL = new DoublyLinkedList<Integer>();
        testDLL.insertBefore(0,1);        
        assertEquals( "Checking insertBefore to an empty list at position 0 - expected the element at the head of the list", "1", testDLL.toString() );
        testDLL = new DoublyLinkedList<Integer>();
        testDLL.insertBefore(10,1);        
        assertEquals( "Checking insertBefore to an empty list at position 10 - expected the element at the head of the list", "1", testDLL.toString() );
        testDLL = new DoublyLinkedList<Integer>();
        testDLL.insertBefore(-10,1);        
        assertEquals( "Checking insertBefore to an empty list at position -10 - expected the element at the head of the list", "1", testDLL.toString() );
     }

    // TODO: add more tests here. Each line of code in DoublyLinkedList.java should
    // be executed at least once from at least one test.

    @Test
	public void testEmpty() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		assertTrue("Empty dll checked as not empty", dll.isEmpty());
	}
	
	@Test
	public void testAddNodeToEmpty() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.insertBefore(100, 3);
		assertEquals("Data not inserted correctly to empty dll", "3", dll.toString());
	}
	
	
	@Test
	public void testInsertAtEnd() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.insertBefore(0, 1);
		dll.insertBefore(0, 2);
		dll.insertBefore(0, 3);
		dll.insertBefore(0, 4);
		dll.insertBefore(100, 10);
		assertEquals("Data not inserted correctly at end of dll", "4,3,2,1,10", dll.toString());
	}
	
	@Test
	public void testInsertAtBeginning() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.insertBefore(0, 1);
		dll.insertBefore(1, 2);
		dll.insertBefore(2, 3);
		dll.insertBefore(0, 4);
		assertEquals("Incorrect data at beginning of dll", "4,1,2,3", dll.toString());
	}
	
	@Test
	public void testGetValueNotStored() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.insertBefore(0, 1);
		dll.insertBefore(0, 2);
		dll.insertBefore(0, 3);
		dll.insertBefore(0, 4);
		assertNull("Value found in dll that shouldn't be there", dll.get(100));
	}
	
	@Test
	public void testOnlyValueDeleted() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.insertBefore(0, 1);
		
		assertTrue("Node not found in dll", dll.deleteAt(0));
		assertTrue("Only node not deleted", dll.isEmpty());
	}
	
	@Test
	public void testFirstValueDeleted() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.insertBefore(0, 1);
		dll.insertBefore(0, 2);
		
		assertTrue("Node not found in dll", dll.deleteAt(0));
		assertEquals("First node not deleted", "1", dll.toString());
	}
	
	
	@Test
	public void testValueDeleted() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		int unexpectedData = 4;
		int dataPos = 2;
		dll.insertBefore(0, 1);
		dll.insertBefore(0, 2);
		dll.insertBefore(0, 3);
		dll.insertBefore(dataPos, unexpectedData);
		assertTrue("Node not found in dll", dll.deleteAt(dataPos));
		assertEquals("3,2,1", dll.toString());
	}
	
	@Test
	public void testDeleteWrongIndex() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		assertFalse("Deleted node from empty dll", dll.deleteAt(0));
		dll.insertBefore(0, 1);
		dll.insertBefore(0, 2);
		dll.insertBefore(0, 3);
		
		assertFalse("Deleted data from non existent index ( i < 0 )", dll.deleteAt(-100));
		assertFalse("Deleted data from non existent index ( i > dll.length)", dll.deleteAt(100));
	}
	
	@Test
	public void testReverse() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.reverse();
		dll.insertBefore(0, 1);
		dll.reverse();
		assertEquals("Data not reversed with only one node", "1", dll.toString());
		dll.insertBefore(1, 2);
		dll.insertBefore(2, 3);
		assertEquals("Data not inserted properly", "1,2,3", dll.toString());
		dll.reverse();
		assertEquals("Data not reversed", "3,2,1", dll.toString());
	}
	
	@Test
	public void testMakeUnique() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.makeUnique();
		dll.insertBefore(0, 1);
		dll.makeUnique();
		assertEquals("Make unique failed with only 1 node", "1", dll.toString());
		dll.insertBefore(1, 1);
		dll.insertBefore(2, 3);
		dll.insertBefore(3, 1);
		dll.insertBefore(4, 2);
		dll.insertBefore(5, 3);
		dll.makeUnique();
		assertEquals("Make unique failed with multiple nodes", "1,3,2", dll.toString());
	}
	
	@Test
	public void testPush() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.push(10);
		assertEquals("Push failed pushing onto an empty dll", "10", dll.toString());
		dll.push(20);
		assertEquals("Push failed pushing onto dll", "20,10", dll.toString());
	}
	
	@Test
	public void testPop() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		assertNull("Pop not null for empty dll", dll.pop());
		dll.push(10);
		assertEquals("Value not popped from dll", Integer.valueOf(10), dll.pop());
		assertTrue("Pop did not empty dll", dll.isEmpty());
	}
	
	@Test
	public void testEnqueue() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.enqueue(10);
		assertEquals("Enqueue failed enqueueing onto an empty dll", "10", dll.toString());
		dll.enqueue(20);;
		assertEquals("Enqueue failed enqueueing onto dll", "20,10", dll.toString());
	}
	
	@Test
	public void testDequeue() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		assertNull("Dequeue not null for empty dll", dll.dequeue());
		dll.enqueue(10);
		assertEquals("Value not dequeued from dll", Integer.valueOf(10), dll.dequeue());
		assertTrue("Dequeue did not empty dll", dll.isEmpty());
		dll.enqueue(10);
		dll.enqueue(20);
		dll.enqueue(30);
		assertEquals("Value not dequeued from dll", Integer.valueOf(10), dll.dequeue());
		assertEquals("Dequeue failed deleting from dll", "30,20", dll.toString());
	}
}


