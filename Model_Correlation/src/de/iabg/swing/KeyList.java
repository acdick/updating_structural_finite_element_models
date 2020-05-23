/*******************************************************************************
 * Student:             Adam C. Dick, BSE
 * Master's Thesis:     Validating and Updating Structural FE Models
 *                      for Dynamic Analysis
 * 
 * Industry Partner:    Industrieanlagen-Betriebsgesellschaft mbH in Ottobrunn
 * Supervisor:          Dr.-Ing. Manfred Kroiss
 * 
 * Academic Partner:    Technische Universitaet Muenchen
 * Supervisor:          Dr.-Ing. Martin Ruess
 ******************************************************************************/
package de.iabg.swing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/*******************************************************************************
 * This {@link javax.swing.ComboBoxModel} is backed by an
 * {@link java.util.ArrayList} since there are several instances where it is
 * convenient to have this combination.  The methods in this class simply wrap
 * those in {@code ArrayList} or they use the list to implement the methods
 * declared in {@code ComboBoxModel}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class KeyList extends AbstractListModel
        implements ComboBoxModel {
    /** The list of strings */
    protected List<String> keys_;
    
    /** The selected string item */
    protected String selectedItem_;
    
    
    
    /***************************************************************************
     * Constructs an empty list.
     **************************************************************************/
    public KeyList() {
        keys_ = new ArrayList<String>();
    } // eom
    
    
    
    /***************************************************************************
     * Appends the specified element to the end of this list.
     * 
     * @param   element the element to be added to this list
     * @return  {@code true} (as per the general contract of Collection.add)
     **************************************************************************/
    public boolean add(String element) {
        boolean isAdded = keys_.add(element.trim());
        this.fireContentsChanged(this, 0, this.getSize());
        
        return isAdded;
    } // eom
    
    
    
    /***************************************************************************
     * Appends all of the elements in the specified Collection to the end of
     * this list, in the order that they are returned by the specified
     * Collection's Iterator.
     * 
     * @param   collection  the elements to be inserted into this list
     * @return  {@code true} if this list changed as a result of the call
     **************************************************************************/
    public boolean addAll(Collection<String> collection) {
        boolean isAdded = keys_.addAll(collection);
        this.fireContentsChanged(this, 0, this.getSize());
        
        return isAdded;
    } // eom
    
    
    
    /***************************************************************************
     * Removes all of the elements from this list and sets the selected item to
     * {@code null}.
     **************************************************************************/
    public void clear() {
        keys_.clear();
        selectedItem_ = null;
    } // eom
    
    
    
    /***************************************************************************
     * Returns {@code true} if this list contains the specified element.
     * 
     * @param   element the element whose presence in this list is to be tested
     * @return  {@code true} if the specified element is present; {@code false}
     *          otherwise.
     **************************************************************************/
    public boolean contains(String element) {
        return keys_.contains(element.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Returns the element at the specified position in this list.
     * 
     * @param   index   the index of element to return
     * @return  the element at the specified position in this list
     **************************************************************************/
    public String get(int index) {
        return keys_.get(index);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String getElementAt(int index) {
        return this.get(index);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String getSelectedItem() {
        String selectedItem;
        
        if (selectedItem_ == null) {
            selectedItem = String.format(" %s", "Select...");
        }
        else {
            selectedItem = selectedItem_;
        }
        
        return selectedItem;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public int getSize() {
        return this.size();
    } // eom
    
    
    
    /***************************************************************************
     * Searches for the first occurence of the given argument, testing for
     * equality using the {@code equals} method.
     * 
     * @param   element the object to be found
     * @return  the index of the first occurrence of the argument in this list;
     *          returns -1 if the object is not found.
     **************************************************************************/
    public int indexOf(String element) {
        return keys_.indexOf(element.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this list has no elements.
     * 
     * @return  {@code true} if this list has no elements; {@code false}
     *          otherwise.
     **************************************************************************/
    public boolean isEmpty() {
        return keys_.isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     * Removes the object from this list.
     * 
     * @param   object  the object to be removed
     * @return  {@code true} if the object was removed; {@code false} otherwise
     **************************************************************************/
    public boolean remove(Object object) {
        boolean isRemoved = keys_.remove(object);
        this.fireContentsChanged(this, 0, this.getSize());
        
        return isRemoved;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setSelectedItem(Object anItem) {
        String selectedItem = (String) anItem;
        
        if (selectedItem != null) {
            selectedItem_ = selectedItem;
            this.fireContentsChanged(this, 0, this.getSize());
        }
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of elements in this list.
     * 
     * @return the number of elements in this list
     **************************************************************************/
    public int size() {
        return keys_.size();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the string array of the elements contained in this list.
     * 
     * @return the array of elements in this list
     **************************************************************************/
    public List<String> values() {
        return keys_;
    } // eom
} // eoc