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


// NOTE QUESTIONS:
// DO WE NEED TO CALL SUPER ON METHODS WE DID NOT NEED TO IMPLEMENT
// IS CLEAR NOT O(1)
//

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
            myCon.add(addIndex(item), item); //binary search to find position of insertion
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
     * Determine if item is in this set. 
     * <br>pre: item != null
     * @param item element whose presence is being tested. 
     * Item may not equal null.
     * @return true if this set contains the specified item, false otherwise.
     * credits: code for binary search from class slides
     */
    public boolean contains(E item) {
        if (item == null) {
            throw new IllegalArgumentException("Violation of precondition: item != null");
        }
        int low = 0;
        int high = size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int compareResult = item.compareTo(myCon.get(mid));
		    if (compareResult == 0) {
                return true;
            } else if (compareResult > 0) {
                low = mid + 1;
            } else {
                high = mid - 1; // compareResult is < 0
            }
        }
        return false;
    }

    // calls AbstractSet's containsAllmethod calls this method's contains on each elem of otherSet

    /**
     * Create a new set that is the difference of this set and otherSet. 
     * Return an ISet of elements that are in this Set but not in otherSet. 
     * Also called the relative complement. 
     * <br>Example: If ISet A contains [X, Y, Z] and ISet B contains [W, Z] 
     * then A.difference(B) would return an ISet with elements [X, Y] while
     * B.difference(A) would return an ISet with elements [W]. 
     * <br>pre: otherSet != null
     * <br>post: returns a set that is the difference of this set and otherSet.
     * Neither this set or otherSet are altered as a result of this operation.
     * <br> pre: otherSet != null
     * @param otherSet != null
     * @return a set that is the difference of this set and otherSet
     * O(N) if otherSet is a SortedSet, O(NlogN) otherwise
     */
    public ISet<E> difference(ISet<E> otherSet) {
        SortedSet<E> result = new SortedSet<E>();
        SortedSet<E> otherSortedSet;
        if (!(otherSet instanceof SortedSet<?>)) {
            otherSortedSet = new SortedSet<>(otherSet); // should be O(NlogN)
        } else {
            otherSortedSet = (SortedSet<E>) otherSet;
        }
        ArrayList<E> temp = new ArrayList<>();
        Iterator<E> thisIt = this.iterator();
        Iterator<E> otherIt = otherSortedSet.iterator();
        if (thisIt.hasNext() && otherIt.hasNext()) {
            E thisCurr = thisIt.next();
            E otherCurr = otherIt.next();
            while (thisIt.hasNext() && otherIt.hasNext()) {
                while (thisCurr.equals(otherCurr)) {
                    // while both are same, incr both iterators
                    thisCurr = thisIt.next();
                    otherCurr = otherIt.next();
                }
                if (thisCurr.compareTo(otherCurr) > 0) {
                    //if this set's currVal is greater than other set, incr other set's iterator to see if there is a match in other set's next val
                    otherCurr = otherIt.next();
                } else {
                    //if this set's curr val is less than other set, add this val to result and incr this set's iterator to see if next one in this set matches
                    temp.add(thisCurr);
                    thisCurr = thisIt.next();
                }
            }
        }
        if (thisIt.hasNext()) { // add remaining
            while (thisIt.hasNext()) {
                temp.add(thisIt.next());
            }
        }
        result.myCon = temp;
        return result;
    }

    /**
     * Determine if this set is equal to other.
     * Two sets are equal if they have exactly the same elements.
     * The order of the elements does not matter.
     * <br>pre: none
     * @param other the object to compare to this set 
     * @return true if other is a Set and has the same elements as this set
     */
    public boolean equals(Object other) {
        if (!(other instanceof ISet)) { //check if other is ISet before casting
            return false;
        }
        ISet<?> otherSet = (ISet<?>) other;
        if (otherSet.size() != this.size()) {
            return false; //quicker return before iterating thru all items
        }
        Iterator<E> thisIt = this.iterator();
        Iterator<?> otherIt = otherSet.iterator();
        while (thisIt.hasNext()) {
            if (!thisIt.next().equals(otherIt.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * create a new set that is the intersection of this set and otherSet.
     * <br>pre: otherSet != null<br>
     * <br>post: returns a set that is the intersection of this set 
     * and otherSet.
     * Neither this set or otherSet are altered as a result of this operation.
     * <br> pre: otherSet != null
     * @param otherSet != null
     * @return a set that is the intersection of this set and otherSet
     */
    public ISet<E> intersection(ISet<E> otherSet) {
        // TODO
    }

    /**
     * Return an Iterator object for the elements of this set.
     * pre: none
     * @return an Iterator object for the elements of this set
     * O(1)
     */
    public Iterator<E> iterator() {
        return myCon.iterator();
    }

    // REMOVE IS STILL O(N) SO DO I CALL SUPER OR LEAVE IT LOL

     /**
     * Return the number of elements of this set.
     * pre: none
     * @return the number of items in this set
     * O(1)
     */
    public int size() {
        return myCon.size();
    }

    /**
     * Return the smallest element in this SortedSet.
     * <br> pre: size() != 0
     * @return the smallest element in this SortedSet.
     * O(1)
     */
    public E min() {
        if (size() == 0) {
            throw new IllegalArgumentException("Violation of precondition: size() != 0");
        }
        return myCon.get(0);
    }
    
    /**
     * Return the largest element in this SortedSet.
     * <br> pre: size() != 0
     * @return the largest element in this SortedSet.
     */
    public E max() {
        if (size() == 0) {
            throw new IllegalArgumentException("Violation of precondition: size() != 0");
        }
        return myCon.get(size() - 1);
    }
    
    // implement a binary search method to find where we can put 
    // the new added element for add method; returns this index
    // credits: code for binary search from class slides
    private int addIndex(E tgt) {
        int result = -1;
        int low = 0;
        int high = size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int beforeIndex = tgt.compareTo(myCon.get(mid));
            int afterIndex = tgt.compareTo(myCon.get(mid + 1));
            if (beforeIndex > 0 && afterIndex < 0) {
                return mid + 1;
            } else if (beforeIndex > 0) {
                low = mid + 1;
            } else {
                high = mid - 1; // beforeIndex is < 0
            }
        }
        return result;
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
        }
        if (thisIt.hasNext() || otherIt.hasNext()) {
            Iterator<E> remaining = thisIt.hasNext() ? thisIt : otherIt;
            while (remaining.hasNext()) {
                temp.add(remaining.next());
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
