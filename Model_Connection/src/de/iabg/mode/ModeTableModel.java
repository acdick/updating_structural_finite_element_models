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

import de.iabg.mode.io.NastranPunchReader;
import de.iabg.mode.io.UniversalModeReader;

import de.iabg.mode.plaf.ModeFilter;

import de.iabg.swing.KeyList;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

/*******************************************************************************
 * This implementation of a {@link javax.swing.table.TableModel} provides a
 * table representation of a {@link ModeMatrix}.  This class bridges the
 * connection between a {@code ModeMatrix} and a {@code ModeModel} by simply
 * adding a table representation.  Therefore, some of the methods of this class
 * wrap those in {@code ModeMatrix}, while other methods are wrapped in
 * {@code ModeModel}.  Most of the descriptions of the methods in this class
 * can be found by refering to the API of either of those two classes.  The
 * remaining methods generally implement or override those specified in
 * {@code TableModel}.  Refer to the API or tutorial for using
 * {@code Table Models} for more details.
 * 
 * This {@code TableModel} provides three columns to represent a
 * {@code ModeMatrix}:
 * 
 * The first is called {@code Selected} or {@code On}, which selects the
 * corresponding mode shape.  Selecting or deselecting this option can allow
 * later calculations, such as the Modal Assurance Criterion to only calculate
 * specified modes.
 * 
 * The second column is called {@code Mode ID}, which displays the identifier of
 * the mode.
 * 
 * The third column is called {@code Frequency [Hz]}, which displays the
 * frequency of the mode.
 * 
 * The rows of this table reflect the total number of modes that are currently
 * stored.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class ModeTableModel extends AbstractTableModel
        implements TableModel {
    /** The column index of the mode frequency */
    protected static final int MODE_FREQUENCY_INDEX = 2;
    
    /** The column index of the mode name */
    protected static final int MODE_NAME_INDEX = 1;
    
    /** The column index of the mode selection */
    protected static final int MODE_SELECTED_INDEX = 0;
    
    /** The {@code ModeMatrix} that this {@code TableModel} represents */
    protected ModeMatrix modes_;
    
    /** The number of table columns */
    protected static final int N_COLUMNS = 3;
    
    /** An array of the selected mode keys */
    protected boolean[] selectedModeKeys_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code ModeTableModel} with no modes.
     **************************************************************************/
    public ModeTableModel() {
        super();
        
        modes_ = new ModeMatrix();
    } // eom
    
    
    
    /***************************************************************************
     * Deselects all currently stored modes.
     **************************************************************************/
    public void deselectAllModeKeys() {
        if (!modes_.isEmpty()) {
            for (int i = 0; i < selectedModeKeys_.length; i++) {
                selectedModeKeys_[i] = false;
            }
            
            this.fireTableDataChanged();
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public Class getColumnClass(int columnIndex) {
        return this.getValueAt(0, columnIndex).getClass();
    } // eom
    
    
    
    /***************************************************************************
     ***************************************************************************/
    public int getColumnCount() {
        return N_COLUMNS;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public String getColumnName(int columnIndex) {
        String columnName;
        
        switch (columnIndex) {
            case MODE_SELECTED_INDEX:
                columnName = new String("On");
                break;
            case MODE_NAME_INDEX:
                columnName = new String("Mode ID");
                break;
            case MODE_FREQUENCY_INDEX:
                columnName = new String("Frequency [Hz]");
                break;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
        
        return columnName;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link ModeMatrix#getModeCount()}.
     * 
     * @return  the number of modes
     **************************************************************************/
    public int getModeCount() {
        return modes_.getModeCount();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored {@link ModeMatrix}.
     * 
     * @return  the currently stored {@code ModeMatrix}
     **************************************************************************/
    public ModeMatrix getModes() {
        return modes_;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link ModeMatrix#getNodeCount()}.
     * 
     * @return  the number of nodes
     **************************************************************************/
    public int getNodeCount() {
        return modes_.getNodeCount();
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link ModeMatrix#getRowCount()}.
     * 
     * @return  the number of rows
     **************************************************************************/
    public int getRowCount() {
        return modes_.getRowCount();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link KeyList} of the currently selected modes.
     * 
     * @return  the {@link KeyList} of the currently selected modes
     **************************************************************************/
    public KeyList getSelectedModeKeys() {
        KeyList selectedModeKeys = new KeyList();
        
        for (int i = 0; i < selectedModeKeys_.length; i++) {
            if (selectedModeKeys_[i]) {
                selectedModeKeys.add(modes_.getModeNameAt(i));
            }
        }
        
        return selectedModeKeys;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value;
        
        switch (columnIndex) {
            case MODE_SELECTED_INDEX:
                value = selectedModeKeys_[rowIndex];
                break;
            case MODE_NAME_INDEX:
                value = modes_.getModeNameAt(rowIndex);
                break;
            case MODE_FREQUENCY_INDEX:
                value = modes_.getModeFrequencyAt(rowIndex);
                break;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
        
        return value;
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeTableModel} has selected any modes.
     * 
     * @return  {@code true} if this {@code ModeTableModel} has selected any
     *          modes; {@code false} otherwise
     **************************************************************************/
    public boolean hasModeKeys() {
        boolean hasModeKeys = false;
        
        for (boolean modeKey : selectedModeKeys_) {
            if (modeKey == true) {
                hasModeKeys = true;
                break;
            }
        }
        
        return hasModeKeys;
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeTableModel} is storing any modes.
     * 
     * @return  {@code true} if this {@code ModeTableModel} stores any modes;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasModes() {
        return !modes_.isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link ModeReader} for the given file and imports
     * all modes contained in the file.
     * 
     * @param   fileName                        the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     * @throws  java.io.IOException             if the file could not be read
     **************************************************************************/
    public void importModes(File fileName)
            throws  FileNotFoundException,
                    IOException {
        ModeReader modeReader;
        
        modes_.clear();
        
        if (fileName.getPath().toUpperCase().endsWith(ModeFilter.UNV)) {
            modeReader = new UniversalModeReader(fileName);
        }
        else {
            modeReader = new NastranPunchReader(fileName);
        }
        
        modeReader.readFile();
        modeReader.importModes(modes_);
        
        selectedModeKeys_ = new boolean[modes_.getModeCount()];
        this.fireTableDataChanged();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean isCellEditable;
        
        switch (columnIndex) {
            case MODE_SELECTED_INDEX:
                isCellEditable = true;
                break;
            case MODE_NAME_INDEX:
            case MODE_FREQUENCY_INDEX:
                isCellEditable = false;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
        
        return isCellEditable;
    } // eom
    
    
    
    /***************************************************************************
     * Selects all currently stored modes.
     **************************************************************************/
    public void selectAllModeKeys() {
        if (!modes_.isEmpty()) {
            for (int i = 0; i < selectedModeKeys_.length; i++) {
                selectedModeKeys_[i] = true;
            }
            
            this.fireTableDataChanged();
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case MODE_SELECTED_INDEX:
                selectedModeKeys_[rowIndex] = (Boolean) aValue;
                this.fireTableDataChanged();
                break;
            case MODE_NAME_INDEX:
            case MODE_FREQUENCY_INDEX:
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    } // eom
} // eoc