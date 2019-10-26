import java.util.Iterator;
import java.util.ListIterator;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An array based implementation of the IList interface, 
 * a simple list ADT. 
 * @author Matt T 
 *
 * @param <T> generic type of the objects in the list 
 */
public class IUArrayList<T> implements IndexedUnsortedList<T>
{
	private T[] list; // array holding objects in list 
	private int count; // number of objects in list / first open position in array 
	private int capacity; // capacity of the array 
	private static final int DEFAULT_CAPACITY = 10; 
	private int modCount; // number of modifications to list 
	
	/**
	 * Default constructor 
	 */
	@SuppressWarnings("unchecked")
	public IUArrayList()
	{
		capacity = DEFAULT_CAPACITY;	 
		list = (T[])(new Object[capacity]); 
		count = 0; 
		modCount = 0; 
	}

	@Override
	public void add(T item)
	{
		checkCapacity(); 
		list[count] = item;
		count++; 
		modCount++; 
	}

	public void insert(int pos, T item)
	{
		if(pos < 0 || pos > count)
		{
			throw new IndexOutOfBoundsException("Invalid index of insert method"); 
		}
		checkCapacity();	 
		for(int i = count; i > pos; i--)
		{
			list[i] = list[i - 1]; 
		}
		list[pos] = item; 
		count++; 
		modCount++; 
	}

	/**
	 * Checks whether capacity to add more objects, 
	 * if not, doubles capacity of array
	 */
	private void checkCapacity()
	{
		if(count == capacity)
		{
			capacity *= 2; 
			list = Arrays.copyOf(list, capacity);
		}
	}
	
	@Override
	public void set(int pos, T item)
	{
		if(pos < 0 || pos >= count)
		{
			throw new IndexOutOfBoundsException("Invalid index for set method"); 
		}
		T oldItem = list[pos];
		list[pos] = item;
		modCount++; 
	
	}

	@Override
	public T get(int pos)
	{
		if(pos < 0 || pos >= count)
		{
			throw new IndexOutOfBoundsException("Invalid index for get method"); 
		}

		return  list[pos];
	}

	@Override
	public T remove(int pos)
	{
		if(pos < 0 || pos >= count)
		{
			throw new IndexOutOfBoundsException("Invalid index for remove method"); 
		}		
		T oldItem = list[pos];

		for(int i = pos; i < count - 1; i++)
		{
			list[i] = list[i + 1]; 
		}
		list[count - 1] = null;
		count--; 
		modCount++; 
		return oldItem;
	}

	@Override
	public T remove(T item)
	{
		// get index of item, if in list 
		int index = indexOf(item); 
		// if item not in list
		if(index == -1)
		{
			throw new NoSuchElementException("Item not found - can't remove it."); 
		}
		return remove(index);
	}

	@Override
	public int size()
	{
		return count;
	}

	@Override
	public int indexOf(T item)
	{
		int index = 0;
		boolean found = false; 
		
		while(!found && index < count)
		{
			if(list[index] == item)
			{
				found = true; 
			}
			else
			{
				index++;
			}
		}
		if(!found)
		{
			index = -1; 
		}
		
		return index;
	}

	@SuppressWarnings("unchecked")
	public void makeEmpty()
	{
		capacity = DEFAULT_CAPACITY;	 
		list = (T[])(new Object[capacity]); 
		count = 0; 		
		modCount++; 
	}

	@Override
	public Iterator<T> iterator()
	{
		return new ListIterator();
	}

	/**
	 * Iterator for the List ADT 
	 * @author Matt T 
	 *
	 */
	private class ListIterator implements Iterator<T>
	{
		private int next; // index of next item to be returned 
		private int itrModCount; // number of modifications when initialize iterator 
		private boolean canRemove; // flag for precondition on remove method 
		
		/**
		 * Default constructor, iteration starts at first element
		 */
		public ListIterator()
		{
			next = 0;
			itrModCount = modCount; 
			canRemove = false; 
		}
		
		@Override
		public boolean hasNext()
		{
			return (next < count);
		}

		@Override
		public T next()
		{
			checkModifications(); 
			
			if(!hasNext())
			{
				throw new NoSuchElementException("Can't call next, no more items in list"); 
			}
			T item = list[next]; 
			next++; 
			canRemove = true; 
			return item;
		}
		
		@Override 
		public void remove()
		{
			
			canRemove = false; 
		}
		
		/**
		 * Check whether modifications done to list 
		 */
		private void checkModifications()
		{
			if(modCount != itrModCount)
			{
				throw new ConcurrentModificationException("Changes made to list"); 
			}
		}
	}

	@Override
	public void addToFront(T element) {
		T[] copy = Arrays.copyOf(list, capacity+1);
		copy[0] = element;
		for(int i = 1; i < capacity; i++) {
		copy[i]= list[i-1];
		
		
		
		}
		
		count+=1;
		list = copy;
		
		
		
		
		
	}

	@Override
	public void addToRear(T element) {
		checkCapacity(); 
		list[count] = element;
		count++; 
		modCount++; 
		
	}

	@Override
	public void addAfter(T element, T target) {
		//[A,B,C] addAfter(A,Z) --> [A,Z,B,C]
		//[A,B,C] addAfter(Q,Z) --> throw new NoSuchElementException because Q is not in list.
		
		
		
		
	}

	@Override
	public void add(int index, T element) {
		//[A,B] add(0,C) --> [C,B]
		//[A,B] add(1,F) --> [A,F]
		//[A,B] add(2,Z) --> [A,B,Z]
		//valid add indexes 0,1,2
	checkCapacity();
	if (index == count) {
		list[count] = element;
		count++;
		modCount++;
	}
	if((index < count) && (index > -1)) {
		list[index] = element;
	}
	else {
		throw new IndexOutOfBoundsException();
	}
	
	}

	@Override
	public T removeFirst() {
		//[A,B] removeFirst() [B]
		if (count==0) {
			throw new NoSuchElementException();
			
		}
		T firstElement = list[0];
		T[] copy = Arrays.copyOf(list, capacity-1);
		for(int i =1; i<capacity; i++) {
			copy[i-1]=list[i];
		}
		list=copy;
		count--;
		modCount++;
		return firstElement;
		
	}

	@Override
	public T removeLast() {
		// TODO Auto-generated method stub
		if (count == 0 ) {
			throw new NoSuchElementException();
		} 
		T lastIndex = list[count-1];
		T[] copy = Arrays.copyOf(list,capacity-1);
		for(int i=0; i<capacity-1; i++) {
			copy[i] = list[i];
			
		}
		list = copy;
		count--;
		return lastIndex;
	}

	@Override
	public T first() {
		
		if(list[0] == null) {
			throw new NoSuchElementException();
		}
		return list[0];
	}

	@Override
	public T last() {
		return list[count-1];
	}

	@Override
	public boolean contains(T target) {
		for(int x = 0; x < capacity; x++) {
			if(list[x] == target) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isEmpty() {
		if(count == 0) {
		return (true);
				}
		return false;
	}

	@Override
	public java.util.ListIterator<T> listIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.util.ListIterator<T> listIterator(int startingIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
