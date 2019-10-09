package main;

import java.awt.event.ItemEvent;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

import sun.invoke.empty.Empty;

// -------------------------------------------------------------------------
/**
 *  This class contains the methods of Doubly Linked List.
 *
 *  @author  
 *  @version 09/10/18 11:13:22
 */


/**
 * Class DoublyLinkedList: implements a *generic* Doubly Linked List.
 * @param <T> This is a type parameter. T is used as a class name in the
 * definition of this class.
 *
 * When creating a new DoublyLinkedList, T should be instantiated with an
 * actual class name that extends the class Comparable.
 * Such classes include String and Integer.
 *
 * For example to create a new DoublyLinkedList class containing String data: 
 *    DoublyLinkedList<String> myStringList = new DoublyLinkedList<String>();
 *
 * The class offers a toString() method which returns a comma-separated sting of
 * all elements in the data structure.
 * 
 * This is a bare minimum class you would need to completely implement.
 * You can add additional methods to support your code. Each method will need
 * to be tested by your jUnit tests -- for simplicity in jUnit testing
 * introduce only public methods.
 */
class DoublyLinkedList<T extends Comparable<T>>
{

    /**
     * private class DLLNode: implements a *generic* Doubly Linked List node.
     */
    private class DLLNode
    {
        public final T data; // this field should never be updated. It gets its
                             // value once from the constructor DLLNode.
        public DLLNode next;
        public DLLNode prev;
    
        /**
         * Constructor
         * @param theData : data of type T, to be stored in the node
         * @param prevNode : the previous Node in the Doubly Linked List
         * @param nextNode : the next Node in the Doubly Linked List
         * @return DLLNode
         */
        public DLLNode(T theData, DLLNode prevNode, DLLNode nextNode) 
        {
          data = theData;
          prev = prevNode;
          next = nextNode;
        }
    }

    // Fields head and tail point to the first and last nodes of the list.
    private DLLNode head, tail;

    /**
     * Constructor of an empty DLL
     * @return DoublyLinkedList
     */
    public DoublyLinkedList() 
    {
      head = null;
      tail = null;
    }

    /**
     * Tests if the doubly linked list is empty
     * @return true if list is empty, and false otherwise
     *
     * Worst-case asymptotic running time cost: TODO
     *
     * Justification:
     *  TODO
     */
    public boolean isEmpty()
    {
      return (head == null && tail == null);
    }

    /**
     * Inserts an element in the doubly linked list
     * @param pos : The integer location at which the new data should be
     *      inserted in the list. We assume that the first position in the list
     *      is 0 (zero). If pos is less than 0 then add to the head of the list.
     *      If pos is greater or equal to the size of the list then add the
     *      element at the end of the list.
     * @param data : The new data of class T that needs to be added to the list
     * @return none
     *
     * Worst-case asymptotic running time cost: TODO
     *
     * Justification:
     *  TODO
     */
    public void insertBefore( int pos, T data ) 
    {
    	
    	if (isEmpty()) {
    		insertFirstNode(data);
    	}
    	else if (pos < 1 ) {
    		insertAtStart(data);
    	} 
    	else 
    	{
    		DLLNode currentNode = head;
    		int index = 0;
    		while (currentNode != null) {
    			if (index == pos) {
    				DLLNode newNode = new DLLNode(data, currentNode.prev, currentNode);
    				currentNode.prev.next = newNode;
    				currentNode.prev = newNode;
    				return;
    			}
    			index++;
    			currentNode = currentNode.next;
    		}
    		insertAtEnd(data);
    	}
      return;
    }
    
    public void insertFirstNode(T data) {
    	DLLNode newNode = new DLLNode(data, null, null);
    	head = newNode;
    	tail = newNode;
    }
    
    public void insertAtStart(T data) {
    	DLLNode newNode = new DLLNode(data, null, head);
    	head.prev = newNode;
    	head = newNode;
    }
    
    public void insertAtEnd(T data) {
    	DLLNode newNode = new DLLNode(data, tail, null);
    	tail.next = newNode;
    	tail = newNode;
    }
    

    /**
     * Returns the data stored at a particular position
     * @param pos : the position
     * @return the data at pos, if pos is within the bounds of the list, and null otherwise.
     *
     * Worst-case asymptotic running time cost: TODO
     *
     * Justification:
     *  TODO
     *
     */
    public T get(int pos) 
    {
    	DLLNode currentNode = head;
		int index = 0;
		while (currentNode != null && index <= pos) {
			if (index == pos) {
				return currentNode.data;
			}
			index++;
			currentNode = currentNode.next;
		}
      return null;
    }

    /**
     * Deletes the element of the list at position pos.
     * First element in the list has position 0. If pos points outside the
     * elements of the list then no modification happens to the list.
     * @param pos : the position to delete in the list.
     * @return true : on successful deletion, false : list has not been modified. 
     *
     * Worst-case asymptotic running time cost: TODO
     *
     * Justification:
     *  TODO
     */
    public boolean deleteAt(int pos) 
    {
    	DLLNode currentNode = head;
		int index = 0;
		while (currentNode != null && index <= pos) {
			if (pos == 0 && currentNode.next == null && currentNode.prev == null) {
				deleteOnlyNode();
				return true;
			}
			else if (pos == 0) {
				deleteFirstNode();
				return true;
			}
			else if (pos == index) {
				if (currentNode.next != null) {
					currentNode.prev = currentNode.prev.next;
				}
				currentNode.prev.next = currentNode.next;
			}
			index++;
			currentNode = currentNode.next;
		}
      return false;
    }
    
    public void deleteOnlyNode() {
    	head = null;
    	tail = null;
    }
    
    public void deleteFirstNode() {
    	head = head.next;
    	head.prev = null;
    }

    /**
     * Reverses the list.
     * If the list contains "A", "B", "C", "D" before the method is called
     * Then it should contain "D", "C", "B", "A" after it returns.
     *
     * Worst-case asymptotic running time cost: TODO
     *
     * Justification:
     *  TODO
     */
    public void reverse()
    {
      DLLNode currentNode = head;
      DLLNode temp;
      while (currentNode != null) {
    	  temp = currentNode.next;
    	  currentNode.next = currentNode.prev;
    	  currentNode.prev = temp;
    	  currentNode = temp;
      }
      temp = head;
      head = tail;
      tail = temp;
    }

    /**
     * Removes all duplicate elements from the list.
     * The method should remove the _least_number_ of elements to make all elements uniqueue.
     * If the list contains "A", "B", "C", "B", "D", "A" before the method is called
     * Then it should contain "A", "B", "C", "D" after it returns.
     * The relative order of elements in the resulting list should be the same as the starting list.
     *
     * Worst-case asymptotic running time cost: TODO
     *
     * Justification:
     *  TODO
     */
     public void makeUnique()
    {
      //TODO
    	 int index1 = 0;
    	 int index2 = 0;
    	 DLLNode node1 = head;
    	 DLLNode node2;
    	 while (node1 != null) {
    		 node2 = node1.next;
    		 index2 = index1+1;
    		 while (node2 != null) {
    			 if (node1.data.compareTo(node2.data) == 0) {
    				 deleteAt(index2);
    				 node2 = node1;
    				 index2 = index1;
    			 }
    			 node2 = node2.next;
    			 index2++;
    		 }
    		 index1++;
    		 node1 = node1.next;
    	 }
    }


    /*----------------------- STACK API 
     * If only the push and pop methods are called the data structure should behave like a stack.
     */

    /**
     * This method adds an element to the data structure.
     * How exactly this will be represented in the Doubly Linked List is up to the programmer.
     * @param item : the item to push on the stack
     *
     * Worst-case asymptotic running time cost: TODO
     *
     * Justification:
     *  TODO
     */
    public void push(T item) 
    {
      //TODO
    	if (isEmpty())
    		insertFirstNode(item);
    	else
    		insertAtStart(item);
    }

    /**
     * This method returns and removes the element that was most recently added by the push method.
     * @return the last item inserted with a push; or null when the list is empty.
     *
     * Worst-case asymptotic running time cost: TODO
     *
     * Justification:
     *  TODO
     */
    public T pop() 
    {
      //TODO
    	if (isEmpty())
    		return null;
    	else {
    		T item = get(0);
    		deleteAt(0);
    		return item;
    	}
    }

    /*----------------------- QUEUE API
     * If only the enqueue and dequeue methods are called the data structure should behave like a FIFO queue.
     */
 
    /**
     * This method adds an element to the data structure.
     * How exactly this will be represented in the Doubly Linked List is up to the programmer.
     * @param item : the item to be enqueued to the stack
     *
     * Worst-case asymptotic running time cost: TODO
     *
     * Justification:
     *  TODO
     */
    public void enqueue(T item) 
    {
      //TODO
    }

     /**
     * This method returns and removes the element that was least recently added by the enqueue method.
     * @return the earliest item inserted with an equeue; or null when the list is empty.
     *
     * Worst-case asymptotic running time cost: TODO
     *
     * Justification:
     *  TODO
     */
    public T dequeue() 
    {
      //TODO
      return null;
    }
 

    /**
     * @return a string with the elements of the list as a comma-separated
     * list, from beginning to end
     *
     * Worst-case asymptotic running time cost:   Theta(n)
     *
     * Justification:
     *  We know from the Java documentation that StringBuilder's append() method runs in Theta(1) asymptotic time.
     *  We assume all other method calls here (e.g., the iterator methods above, and the toString method) will execute in Theta(1) time.
     *  Thus, every one iteration of the for-loop will have cost Theta(1).
     *  Suppose the doubly-linked list has 'n' elements.
     *  The for-loop will always iterate over all n elements of the list, and therefore the total cost of this method will be n*Theta(1) = Theta(n).
     */
    public String toString() 
    {
      StringBuilder s = new StringBuilder();
      boolean isFirst = true; 

      // iterate over the list, starting from the head
      for (DLLNode iter = head; iter != null; iter = iter.next)
      {
        if (!isFirst)
        {
          s.append(",");
        } else {
          isFirst = false;
        }
        s.append(iter.data.toString());
      }

      return s.toString();
    }
    
    public static void main(String[] args) {
    	DoublyLinkedList<Integer> dll = new DoublyLinkedList<Integer>();
    	dll.insertBefore(0, 1);
		dll.insertBefore(1, 1);
		dll.insertBefore(2, 3);
		dll.insertBefore(3, 1);
		dll.insertBefore(4, 2);
		dll.insertBefore(5, 3);
		dll.makeUnique();
	}

}



