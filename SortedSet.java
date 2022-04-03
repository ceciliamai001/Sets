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
 *  UTEID: cm64429
 *  email address: mai.cecilia@utexas.edu  
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
        myCon = new ArrayList<E>();
        for (E val : other) {
            myCon.add(val); //add items to arrayList to sort
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
            if (size() == 0 || item.compareTo(myCon.get(size() - 1)) > 0) {
                myCon.add(item);
            } else if (item.compareTo(myCon.get(0)) < 0) {
                myCon.add(0, item);
            } else{
                myCon.add(addIndex(item), item); //binary search to find position of insertion
            }
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
     * O(N) if other set is also a SortedSet, O(NlogN) otherwise
     */
    public boolean addAll(ISet<E> otherSet) {
        if (otherSet == null) {
            throw new IllegalArgumentException("Violation of precondition: otherSet != null");
        }
        SortedSet<E> otherSortedSet;
        if (!(otherSet instanceof SortedSet<?>)) {
            otherSortedSet = new SortedSet<>(otherSet); // should be O(NlogN)
        } else {
            otherSortedSet = (SortedSet<E>) otherSet;
        }
        int oldSize = size();
        mergeSet(this, otherSortedSet); // O(N) operation
        return oldSize == size();
    }
    
    /**
     * Determine if item is in this set. 
     * <br>pre: item != null
     * @param item element whose presence is being tested. 
     * Item may not equal null.
     * @return true if this set contains the specified item, false otherwise.
     * credits: code for binary search from class slides
     * O(logN)
     */
    public boolean contains(E item) {
        if (item == null) {
            throw new IllegalArgumentException("Violation of precondition: item != null");
        }
        //use binary search to look for item
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

    /**
     * Determine if all of the elements of otherSet are in this set.
     * <br> pre: otherSet != null
     * @param otherSet != null
     * @return true if this set contains all of the elements in otherSet, 
     * false otherwise.
     */
    public boolean containsAll(ISet<E> otherSet) {
        if (otherSet == null) {
            throw new IllegalArgumentException("Violation of precondition: otherSet != null");
        }
        if (this.size() < otherSet.size()) { // early return if otherSet has more elements
            return false;
        }
        return contAllHelp(0, 0, getOtherSorted(otherSet));
    }

    // Iterates through both sorted sets and returns true if 
    // this set contains all of the elements in otherSet
    private boolean contAllHelp(int indexThis, int indexOther, SortedSet<E> otherSortedSet) {
        while (indexThis < this.size() && indexOther < otherSortedSet.size()) {
            E currThis = myCon.get(indexThis);
            E currOther = otherSortedSet.myCon.get(indexOther);
            if (currThis.equals(currOther)) {
                indexThis++;
                indexOther++;
            } else if (currThis.compareTo(currOther) < 0) {
                indexThis++;
            }
            else {
                return false;
            }
        }
        return true;
    }
    
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
        if (otherSet == null) {
            throw new IllegalArgumentException("Violation of precondition: otherSet != null");
        }
        SortedSet<E> result = new SortedSet<E>();
        result.myCon = getDifference(0, 0, getOtherSorted(otherSet), new ArrayList<>());
        return result;
    }

    // Iterates through both sorted sets and returns an array of elements
    // in this set that are not in other set
    private ArrayList<E> getDifference(int indexThis, int indexOther, 
        SortedSet<E> otherSortedSet, ArrayList<E> temp) {
        while (indexThis < this.size() && indexOther < otherSortedSet.size()) {
            E currThis = myCon.get(indexThis);
            E currOther = otherSortedSet.myCon.get(indexOther);
            if (currThis.equals(currOther)) {
                indexThis++;
                indexOther++;
                //if currThis > currOther, move index of other to check for equivalence
            } else if (currThis.compareTo(currOther) > 0) {
                indexOther++;
            } else { 
                //if currThis < currOther, currOther "passed" currThis, meaning currThis
                //will never find an equal so add to list of differences
                temp.add(currThis);
                indexThis++;
            }
        }
        // add remaining elements from this set, if any
        while (indexThis < this.size()) { 
            temp.add(myCon.get(indexThis));
            indexThis++;
        }
        return temp;
    }
    
    
    /**
     * Determine if this set is equal to other.
     * Two sets are equal if they have exactly the same elements.
     * The order of the elements does not matter.
     * <br>pre: none
     * @param other the object to compare to this set 
     * @return true if other is a Set and has the same elements as this set
     * O(N)
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
        while (thisIt.hasNext()) { //both are same size if reached this point
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
     * O(N) if other set is a SortedSet, O(NlogN) else
     */ 
    public ISet<E> intersection(ISet<E> otherSet) {
        if (otherSet == null) {
            throw new IllegalArgumentException("Violation of precondition: otherSet != null");
        }
        SortedSet<E> result = new SortedSet<E>();
        result.myCon = getIntersection(0, 0, getOtherSorted(otherSet), new ArrayList<>());
        return result;
    }
    
    // Iterates through both sorted sets and returns an array of elements
    // in this set that are also in other set
    private ArrayList<E> getIntersection(int indexThis, int indexOther, 
        SortedSet<E> otherSortedSet, ArrayList<E> temp) {
        while (indexThis < this.size() && indexOther < otherSortedSet.size()) {
            E currThis = myCon.get(indexThis);
            E currOther = otherSortedSet.myCon.get(indexOther);

            //add equivalent vals or else move indices until found equivalent vals
            if (currThis.equals(currOther)) {
                temp.add(currThis);
                indexThis++;
                indexOther++;
            } else if (currThis.compareTo(currOther) > 0) {
                indexOther++;
            }
            else {
                indexThis++;
            }
        }
        return temp;
    }
    
    /**
     * Create a new set that is the union of this set and otherSet.
     * <br>pre: otherSet != null
     * <br>post: returns a set that is the union of this set and otherSet.
     * Neither this set or otherSet are altered as a result of this operation.
     * <br> pre: otherSet != null
     * @param otherSet != null
     * @return a set that is the union of this set and otherSet
     * O(N) if other set is a SortedSet, O(NlogN) otherwise
     */
    public ISet<E> union(ISet<E> otherSet) {
        if (otherSet == null) {
            throw new IllegalArgumentException("Violation of precondition: otherSet != null");
        }
        SortedSet<E> result = new SortedSet<E>();
        SortedSet<E> otherSortedSet = getOtherSorted(otherSet);
        mergeSet(result, otherSortedSet); // O(N)
        return result;
    }

    // checks if the set sent in as parameter is a sorted set. 
    // if yes, cast it to SortedSet, else sort it. returns a sorted set.
    private SortedSet<E> getOtherSorted(ISet<E> otherSet) {
        SortedSet<E> otherSortedSet;
        if (!(otherSet instanceof SortedSet<?>)) {
            otherSortedSet = new SortedSet<>(otherSet); // should be O(NlogN)
        } else {
            otherSortedSet = (SortedSet<E>) otherSet;
        }
        return otherSortedSet;
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
        return 0;
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
    private void mergeSet(SortedSet<E> result, SortedSet<E> other) {
        ArrayList<E> temp = new ArrayList<>();
        int indexThis = 0;
        int indexOther = 0;
        while (indexThis < this.size() && indexOther < other.size()) {
            E currThis = myCon.get(indexThis);
            E currOther = other.myCon.get(indexOther);
            if (currThis.equals(currOther)) {
                temp.add(currThis);
                indexThis++;
                indexOther++;
            } else if (currThis.compareTo(currOther) < 0) {
                temp.add(currThis);
                indexThis++;
            } else {
                temp.add(currOther);
                indexOther ++;
            }
        }
        result.myCon = temp;
    }
}
