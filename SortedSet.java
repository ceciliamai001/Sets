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
     */
    public SortedSet() {

    }

    /**
     * create a SortedSet out of an unsorted set. <br>
     * @param other != null
     */
    public SortedSet(ISet<E> other) {

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



}
