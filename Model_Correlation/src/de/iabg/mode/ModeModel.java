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

import de.iabg.swing.KeyList;
import de.iabg.swing.Loggable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.table.TableModel;

/*******************************************************************************
 * This interface declares basic functionality for storing data for a dynamic
 * mode shape matrix.  This includes the ability to read in the mode shape data
 * from different file types and obtain the modes.  The user should be able to
 * select subsets of modes for later calculations.  Thus, there should
 * also be methods to set default mode sets when mode shape data is read in.
 * 
 * This {@code ModeModel} should also be {@link Loggable}, meaning that for
 * important user-controlled events, log messages should be fired so that the
 * user can have a command history and be able to view the current status of the
 * stored data.  There must be methods to access the data for calculations or
 * output.  Before output data can be made, queries must be available to check
 * if the data is already stored.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 12, 2008
 ******************************************************************************/
public interface ModeModel
        extends Loggable {
    
    
    
    /***************************************************************************
     * Deselects all currently stored modes.
     **************************************************************************/
    public void deselectAllModeKeys();
    
    
    
    /***************************************************************************
     * Returns the file where the mode shape data was last imported.
     * 
     * @return  the last imported mode shape file
     **************************************************************************/
    public File getModeFile();
    
    
    
    /***************************************************************************
     * Returns the currently stored modes.
     * 
     * @return  the {@link ModeMatrix} storing the modes
     **************************************************************************/
    public ModeMatrix getModes();
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.table.TableModel} of the modes.
     * 
     * @return  the {@link javax.swing.table.TableModel} of the modes
     **************************************************************************/
    public TableModel getModeTableModel();
    
    
    
    /***************************************************************************
     * Returns the {@link KeyList} of the currently selected modes.
     * 
     * @return  the {@link KeyList} of the currently selected modes
     **************************************************************************/
    public KeyList getSelectedModeKeys();
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeModel} has selected any modes.
     * 
     * @return  {@code true} if this {@code ModeModel} has selected any modes;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasModeKeys();
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeModel} is storing any modes.
     * 
     * @return  {@code true} if this {@code ModeModel} stores any modes;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasModes();
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link ModeReader} for the given file and imports
     * all modes contained in the file.
     * 
     * @param   fileName                        the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     * @throws  java.io.IOException             if the file could not be read
     **************************************************************************/
    public void importModes(String fileName)
            throws  FileNotFoundException,
                    IOException;
    
    
    
    /***************************************************************************
     * Selects all currently stored modes.
     **************************************************************************/
    public void selectAllModeKeys();
    
    
    
    /***************************************************************************
     * Selects all currently stored modes and is equivalent to calling
     * {@link #selectAllModeKeys()}.
     **************************************************************************/
    public void setDefaultKeys();
} // eoi