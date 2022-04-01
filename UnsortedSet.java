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
 * A simple implementation of an ISet. 
 * Elements are not in any particular order.
 * Students are to implement methods that 
 * were not implemented in AbstractSet and override
 * methods that can be done more efficiently. 
 * An ArrayList must be used as the internal storage container.
 *
 */
public class UnsortedSet<E> extends AbstractSet<E> {

    private ArrayList<E> myCon;

    /**
     * Constructor to build new unsorted set
     */
	public UnsortedSet() {
	    myCon = new ArrayList<>();
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
        ArrayList<E> prev = new ArrayList<E>(myCon);
        if (!myCon.contains(item)) {
            myCon.add(item); 
        }
        return !myCon.equals(prev);
    }

    /**
     * A union operation. Add all items of otherSet that 
     * are not already present in this set to this set.
     * @param otherSet != null
     * @return true if this set changed as a result of this operation, 
     * false otherwise.
     */
    public boolean addAll(ISet<E> otherSet) {
    if (otherSet == null) {
        throw new IllegalArgumentException("Violation of precondition: otherSet != null");
    }
    ArrayList<E> prev = new ArrayList<E>(myCon);
    for (E val : otherSet) {
        myCon.add(val);
    }
        return !myCon.equals(prev);
    }

    /**
     * Make this set empty.
     * <br>pre: none
     * <br>post: size() = 0
     */
    public void clear() {
        myCon = new ArrayList<>();
    }



}
