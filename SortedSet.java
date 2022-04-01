/*  Student information for assignment:
 *
 *  On OUR honor, Mia Tey and Cecilia Mai, 
 *  this programming assignment is OUR own work
 *  and WE have not provided this code to any other student.
 *
 *  Number of slip days used: 0
 *
 *  Student 1
 *  UTEID: MAT5693
 *  email address: mia_tey@aol.com
 *  TA name: Pranav
 *  
 *  Student 2 
 *  UTEID:
 *  email address:   
 */

import java.util.Iterator;
import java.util.ArrayList;

/**
 * In this implementation of the ISet interface the elements in the Set are 
 * maintained in ascending order.
 * 
 * The data type for E must be a type that implements Comparable.
 * 
 * Implement methods that were not implemented in AbstractSet 
 * and override methods that can be done more efficiently. An ArrayList must 
 * be used as the internal storage container. For methods involving two set, 
 * if that method can be done more efficiently if the other set is also a 
 * SortedSet, then do so.
 */
public class SortedSet<E extends Comparable<? super E>> extends AbstractSet<E> {

    private ArrayList<E> myCon;

    /**
     * create an empty SortedSet
     * O(1)
     */
    public SortedSet() {
        myCon = new ArrayList<E>();
    }

    /**
     * create a SortedSet out of an unsorted set. <br>
     * @param other != null
     * credits: code for Merge Sort from class slides, located at bottom of class
     * O(NlogN)
     */
    public SortedSet(ISet<E> other) {
        if (other == null) {
            throw new IllegalArgumentException("Violation of precondition: other != null");
        }
        // add items to arrayList to sort
        myCon = new ArrayList<E>();
        for (E val : other) {
            myCon.add(val);
        }
        ArrayList<E> temp = new ArrayList<>(); // temp container
	    sort(myCon, temp, 0, other.size() - 1);
    }

    /**
     * Add an item to this set.
     * <br> item != null
     * @param item the item to be added to this set. item may not equal null.
     * @return true if this set changed as a result of this operation, 
     * false otherwise.
     * O(N)
     */
    public boolean add(E item) {
        if (item == null) {
            throw new IllegalArgumentException("Violation of precondition: item != null");
        }
        if (!myCon.contains(item)) {
            myCon.add(item);
            return true;
        }
        return false;
    }

    /**
      * A union operation. Add all items of otherSet that 
      * are not already present in this set to this set.
      * @param otherSet != null
      * @return true if this set changed as a result of this operation, 
      * false otherwise.
      * O(N)
      */
      public boolean addAll(ISet<E> otherSet) {
        if (otherSet == null) {
            throw new IllegalArgumentException("Violation of precondition: otherSet != null");
        }
        int oldSize = size();
        mergeSet(otherSet);
        return oldSize == size();
    }

    /**
     * Make this set empty.
     * <br>pre: none
     * <br>post: size() = 0
     * O(1)
     * NOTE: WHAT DO THEY MEAN BY "You can do this with a single statement in 
     * SortedSet, but the Big O of that statement is O(N)." ?? cant we garbage collect
     */
    public void clear() {
        myCon = new ArrayList<>();
    }
    
    /**
     * Return the smallest element in this SortedSet.
     * <br> pre: size() != 0
     * @return the smallest element in this SortedSet.
     */
    public E min() {
        
    }
    
    /**
     * Return the largest element in this SortedSet.
     * <br> pre: size() != 0
     * @return the largest element in this SortedSet.
     */
    public E max() {
        
    }
    
    // Code to sort as part of Merge Sort algorithm
    // credits: code for Merge Sort from class slides
    private void sort(ArrayList<E> myCon, ArrayList<E> temp, int low, int high) {
        if (low < high) {
            int center = (low + high) / 2;
            sort(myCon, temp, low, center);
            sort(myCon, temp, center + 1, high);
            merge(myCon, temp, low, center + 1, high);
        }
    }
    
    // Code to merge as part of Merge Sort algorithm
    // credits: code for Merge Sort from class slides
    private void merge(ArrayList<E> myCon, ArrayList<E> temp, int leftPos, int rightPos, int rightEnd) {
        int leftEnd = rightPos - 1;
        int tempPos = leftPos;
        int numElements = rightEnd - leftPos + 1;
        // main loop
        while (leftPos <= leftEnd && rightPos <= rightEnd) {
            if (myCon.get(leftPos).compareTo(myCon.get(rightPos)) <= 0) {
                temp.set(tempPos, myCon.get(leftPos));
                leftPos++;
            } else {
                temp.set(tempPos, myCon.get(rightPos));
                rightPos++;
            }
            tempPos++;
        }
        // copy rest of left half
        while (leftPos <= leftEnd) {
            temp.set(tempPos, myCon.get(leftPos));
            tempPos++;
            leftPos++;
        }
        // copy rest of right half
        while (rightPos <= rightEnd) {
            temp.set(tempPos, myCon.get(rightPos));
            tempPos++;
            rightPos++;
        }
        // Copy temp back into data
        for (int i = 0; i < numElements; i++, rightEnd--) {
            myCon.set(rightEnd, temp.get(rightEnd));
        }
    }
    // merge algorithm that takes a set as a parameter and merges it with the current
    // sorted set by iterating through sets and comparing each element
    // credits: code for Merge Sort from class slides
    private void mergeSet(ISet<E> other) {
        ArrayList<E> temp = new ArrayList<>();
        Iterator<E> thisIt = this.iterator();
        Iterator<E> otherIt = other.iterator();
        if (thisIt.hasNext() && otherIt.hasNext()) {
            E thisCurr = thisIt.next();
            E otherCurr = otherIt.next();
            while (thisIt.hasNext() && otherIt.hasNext()) {
                compareVals(thisCurr, otherCurr, thisIt, otherIt, temp);
            }
            if (thisIt.hasNext() || otherIt.hasNext()) {
                Iterator<E> remaining = thisIt.hasNext() ? thisIt : otherIt;
                while (remaining.hasNext()) {
                    temp.add(remaining.next());
                }
            }
        }
        myCon = temp;
    }
    
    // compare items from both sets one by one place in sorted order in temp array
    private void compareVals(E thisCurr, E otherCurr, Iterator<E> thisIt, 
        Iterator<E> otherIt, ArrayList<E> temp) {
        E lesser = thisCurr.compareTo(otherCurr) < 0 ? thisCurr : otherCurr;
        temp.add(lesser);
        //to avoid duplicates move both sets' iterators if they equal the current "least" val
        if (thisCurr.equals(lesser) && otherCurr.equals(lesser)) {
            while (thisCurr.equals(lesser)) {
               thisCurr = thisIt.next(); 
            }
            while (thisCurr.equals(lesser)) {
                otherCurr = otherIt.next();
            }
        //if not dupes, only move the iterator from which the val that was placed came from
        } else if (thisCurr.equals(lesser)) {
            thisCurr = thisIt.next();
        } else {
            otherCurr = otherIt.next();
        }
    }
}
