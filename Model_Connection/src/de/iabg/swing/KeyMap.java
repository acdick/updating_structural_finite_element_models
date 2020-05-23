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

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/*******************************************************************************
 * This {@link javax.swing.ComboBoxModel} is backed by a
 * {@link java.util.LinkedHashMap} since there are several instances where it is
 * convenient to have this combination.  The type of elements stored in the map
 * are {@link KeyList} objects, thus the data stored is similar to an array of
 * arrays.  The methods in this class simply wrap those in {@code LinkedHashMap}
 * or they use the map to implement the methods declared in
 * {@code ComboBoxModel}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class KeyMap extends AbstractListModel
        implements ComboBoxModel {
    /** The map of {@code KeyList} objects */
    protected Map<String, KeyList> keyLists_;
    
    /** The string key of the selected {@code KeyList} */
    protected String selectedItem_;
    
    
    
    /***************************************************************************
     * Constructs an empty map.
     **************************************************************************/
    public KeyMap() {
        keyLists_ = new LinkedHashMap<String, KeyList>();
    } // eom
    
    
    
    /***************************************************************************
     * Removes all mappings from this map and sets the selected item to
     * {@code null}.
     **************************************************************************/
    public void clear() {
        keyLists_.clear();
        selectedItem_ = null;
        this.fireContentsChanged(this, 0, this.getSize());
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String getElementAt(int index) {
        String[]    keys    = new String[this.getSize()];
        String      key;
        
        keyLists_.keySet().toArray(keys);
        key = keys[index];
        
        return key;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the string key of the selected {@link KeyList}.
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
     * Returns the selected {@link KeyList}.
     * 
     * @return  the selected {@code KeyList}
     **************************************************************************/
    public KeyList getSelectedList() {
        return keyLists_.get(selectedItem_);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public int getSize() {
        return this.size();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this map has no elements.
     * 
     * @return  {@code true} if this map has no elements; {@code false}
     *          otherwise.
     **************************************************************************/
    public boolean isEmpty() {
        return keyLists_.isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     * Associates the specified value with the specified key in this map.
     * 
     * @param   key     key with which the specified value is to be associated
     * @param   value   value to be associated with the specified key
     * @return  previous value associated with specified key, or {@code null} if
     *          there was no mapping for key
     **************************************************************************/
    public KeyList put(String key, KeyList value) {
        KeyList previousValue = keyLists_.put(key.trim(), value);
        this.fireContentsChanged(this, 0, this.getSize());
        
        return previousValue;
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
     * Returns the number of key-value mappings in this map.
     * 
     * @return  the number of key-value mappings in this map
     **************************************************************************/
    public int size() {
        return keyLists_.size();
    } // eom
    
    
    
    /***************************************************************************
     * Returns a collection view of the values contained in this map.
     * 
     * @return  a collection view of the values contained in this map
     **************************************************************************/
    public Collection<KeyList> values() {
        return keyLists_.values();
    } // eom
} // eoc