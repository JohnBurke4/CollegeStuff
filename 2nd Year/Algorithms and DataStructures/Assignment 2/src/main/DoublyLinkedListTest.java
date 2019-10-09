package main;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class DoublyLinkedListTest {

	@Test
	void testEmpty() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		assertTrue(dll.isEmpty(), "Empty dll checked as not empty");
	}
	
	@Test
	void testAddNodeToEmpty() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.insertBefore(100, 3);
		assertEquals("3", dll.toString(), "Data not inserted correctly to empty dll");
	}
	
	@Test
	void testInsertBefore() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.insertBefore(0, 1);
		dll.insertBefore(1, 2);
		dll.insertBefore(0, 3);
		dll.insertBefore(5, 4);
		assertEquals("3,1,2,4", dll.toString(), "Data not inserted correctly");
		
		
	}
	
	@Test
	void testInsertAtEnd() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.insertBefore(0, 1);
		dll.insertBefore(0, 2);
		dll.insertBefore(0, 3);
		dll.insertBefore(0, 4);
		dll.insertBefore(100, 10);
		assertEquals("4,3,2,1,10", dll.toString(), "Data not inserted correctly at end of dll");
	}
	
	@Test
	void testInsertAtBeginning() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.insertBefore(0, 1);
		dll.insertBefore(1, 2);
		dll.insertBefore(2, 3);
		dll.insertBefore(0, 4);
		assertEquals("4,1,2,3", dll.toString(), "Incorrect data at beginning of dll");
	}
	
	@Test
	void testGetValueNotStored() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.insertBefore(0, 1);
		dll.insertBefore(0, 2);
		dll.insertBefore(0, 3);
		dll.insertBefore(0, 4);
		assertNull(dll.get(100), "Value found in dll that shouldn't be there");
	}
	
	@Test
	void testValueDeleted() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		int unexpectedData = 4;
		int dataPos = 2;
		dll.insertBefore(0, 1);
		dll.insertBefore(0, 2);
		dll.insertBefore(0, 3);
		dll.insertBefore(dataPos, unexpectedData);
		
		assertTrue(dll.deleteAt(dataPos), "Node not found in dll");
		assertEquals("3,2,1", dll.toString());
	}
	
	@Test
	void testDeleteWrongIndex() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		assertFalse(dll.deleteAt(0), "Deleted node from empty dll");
		dll.insertBefore(0, 1);
		dll.insertBefore(0, 2);
		dll.insertBefore(0, 3);
		
		assertFalse(dll.deleteAt(-100), "Deleted data from non existent index ( i < 0 )");
		assertFalse(dll.deleteAt(100), "Deleted data from non existent index ( i > dll.length) ");
	}
	
	@Test
	void testReverse() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.reverse();
		dll.insertBefore(0, 1);
		dll.reverse();
		assertEquals("1", dll.toString(), "Data not reversed with only one node");
		dll.insertBefore(1, 2);
		dll.insertBefore(2, 3);
		assertEquals("1,2,3", dll.toString(), "Data not inserted properly");
		dll.reverse();
		assertEquals("3,2,1", dll.toString(), "Data not reversed");
	}
	
	@Test
	void testMakeUnique() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.makeUnique();
		dll.insertBefore(0, 1);
		dll.makeUnique();
		assertEquals("1", dll.toString(), "Make unique failed with only 1 node");
		dll.insertBefore(1, 1);
		dll.insertBefore(2, 3);
		dll.insertBefore(3, 1);
		dll.insertBefore(4, 2);
		dll.insertBefore(5, 3);
		dll.makeUnique();
		assertEquals("1,3,2", dll.toString(), "Make unique failed with multiple nodes");
	}
	
	@Test
	void testPush() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		dll.push(10);
		assertEquals("10", dll.toString(), "Push failed pushing onto an empty dll");
		dll.push(20);
		assertEquals("20,10", dll.toString(), "Push failed pushing onto dll");
	}
	
	@Test
	void testPop() {
		DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
		assertNull(dll.pop(), "Pop not null for empty dll");
		dll.push(10);
		assertEquals(Integer.valueOf(10), dll.pop(), "Value not popped from dll");
		assertTrue(dll.isEmpty(), "Pop did not empty dll");
	}

}
