/*  Student information for assignment:
 *
 *  On OUR honor, Mia Tey and Cecilia Mai, 
 *  this programming assignment is OUR own work
 *  and WE have not provided this code to any other student.
 *
 *  Number of slip days used: 0
 *
 *  Student 1
 *  UTEID: mat5693
 *  email address: mia_tey@aol.com
 *  TA name: Pranav
 *  
 *  Student 2 
 *  UTEID: cm64429
 *  email address: mai.cecilia@utexas.edu  
 */

import java.util.Iterator;

public abstract class AbstractSet<E> implements ISet<E> {

    /**
      * A union operation. Add all items of otherSet that 
      * are not already present in this set to this set.
      * @param otherSet != null
      * @return true if this set changed as a result of this operation, 
      * false otherwise.
      */
    public boolean addAll(ISet<E> otherSet) {
        if (otherSet == null)
            throw new IllegalArgumentException("Violation of precondition: otherSet != null");
        
        boolean mod = false;
        for (E val : otherSet) {
            if (add(val)) { //add method in sorted & unsorted set handles duplicates
                mod = true;
            }
        }
        return mod;
    }


    /**
     * Make this set empty.
     * <br>pre: none
     * <br>post: size() = 0
     */
    public void clear() {
        Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
    }


    /**
     * Determine if item is in this set. 
     * <br>pre: item != null
     * @param item element whose presence is being tested. 
     * Item may not equal null.
     * @return true if this set contains the specified item, false otherwise.
     */
    public boolean contains(E item) {
        if (item == null) {
            throw new IllegalArgumentException("Violation of precondition: item != null");
        }
        for (E val : this) {
            if (val.equals(item)) {
                return true;
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
        //if otherSet has more elements than this set, this set does not contain all elements
        if (this.size() < otherSet.size()) { 
            return false;
        }
        Iterator<E> otherIt = otherSet.iterator();
        while (otherIt.hasNext()) {
            if (!this.contains(otherIt.next())) {
                return false;
            }
        }
        return true;
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
        while (thisIt.hasNext()) {
            Iterator<?> otherIt = otherSet.iterator();
            boolean found = false;
            E thisVal = thisIt.next();
            while (otherIt.hasNext() && !found) {
                found = otherIt.next().equals(thisVal); 
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }


    /**
     * Remove the specified item from this set if it is present.
     * pre: item != null
     * @param item the item to remove from the set. item may not equal null.
     * @return true if this set changed as a result of this operation, 
     * false otherwise
     */
    public boolean remove(E item) {
        if (item == null) {
            throw new IllegalArgumentException("Violation of precondition: item != null");
        }
        Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            if (it.next().equals(item)) {
                it.remove();
                return true;
            }
        }
        return false;
    }


    /**
     * Return the number of elements of this set.
     * pre: none
     * @return the number of items in this set
     */
    public int size() {
        int count = 0;
        Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            it.next();
            count ++;
        }
        return count;
    }
     
      
    /**
     * Return a String version of this set. 
     * Format is (e1, e2, ... en)
     * @return A String version of this set.
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        String seperator = ", ";
        result.append("(");

        Iterator<E> it = this.iterator();
        while (it.hasNext()) {
            result.append(it.next());
            result.append(seperator);
        }
        // get rid of extra separator
        if (this.size() > 0) {
            result.setLength(result.length() - seperator.length());
        }

        result.append(")");
        return result.toString();
    }


    /**
     * Create a new set that is the union of this set and otherSet.
     * <br>pre: otherSet != null
     * <br>post: returns a set that is the union of this set and otherSet.
     * Neither this set or otherSet are altered as a result of this operation.
     * <br> pre: otherSet != null
     * @param otherSet != null
     * @return a set that is the union of this set and otherSet
     */
    public ISet<E> union(ISet<E> otherSet) {
        if (otherSet == null) {
            throw new IllegalArgumentException("Violation of precondition: otherSet != null");
        }
        ISet<E> result = this.difference(otherSet); //retrieve unique elements of calling set
        result.addAll(otherSet); //add the elements of otherSet
        return result;
    }
}
