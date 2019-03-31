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
package de.iabg.mode;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;

/*******************************************************************************
 * This {@link javax.swing.ComboBoxModel} is backed by a
 * {@link java.util.LinkedHashMap} since there are several instances where it is
 * convenient to have this combination.  The type of elements stored in the map
 * are {@link MassMatrix} objects.  The methods in this class simply wrap those
 * in {@code LinkedHashMap} or they use the map to implement the methods
 * declared in {@code ComboBoxModel}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 12, 2008
 ******************************************************************************/
public class MassMatrixMap extends AbstractListModel
        implements ComboBoxModel {
    /** The map of {@code MassMatrix} objects */
    protected Map<String, MassMatrix> massMatrices_;
    
    /** The string key of the selected {@code MassMatrix} */
    protected String selectedItem_;
    
    
    
    /***************************************************************************
     * Constructs an empty map.
     **************************************************************************/
    public MassMatrixMap() {
        massMatrices_ = new LinkedHashMap<String, MassMatrix>();
    } // eom
    
    
    
    /***************************************************************************
     * Removes all mappings from this map and sets the selected item to
     * {@code null}.
     **************************************************************************/
    public void clear() {
        massMatrices_.clear();
        selectedItem_ = null;
        this.fireContentsChanged(this, 0, this.getSize());
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String getElementAt(int index) {
        String[]    keys    = new String[this.getSize()];
        String      key;
        
        massMatrices_.keySet().toArray(keys);
        key = keys[index];
        
        return key;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the string key of the selected {@link MassMatrix}.
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
     * Returns the selected {@link MassMatrix}.
     * 
     * @return  the selected {@code MassMatrix}
     **************************************************************************/
    public MassMatrix getSelectedMatrix() {
        return massMatrices_.get(selectedItem_);
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
        return massMatrices_.isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     * Associates the specified value with the specified key in this map.
     * 
     * @param   key     key with which the specified value is to be associated
     * @param   value   value to be associated with the specified key
     * @return  previous value associated with specified key, or {@code null} if
     *          there was no mapping for key
     **************************************************************************/
    public MassMatrix put(String key, MassMatrix value) {
        MassMatrix previousValue = massMatrices_.put(key.trim(), value);
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
        return massMatrices_.size();
    } // eom
    
    
    
    /***************************************************************************
     * Returns a collection view of the values contained in this map.
     * 
     * @return  a collection view of the values contained in this map
     **************************************************************************/
    public Collection<MassMatrix> values() {
        return massMatrices_.values();
    } // eom
} // eoc