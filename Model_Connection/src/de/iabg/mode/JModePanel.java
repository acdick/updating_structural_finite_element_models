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

import de.iabg.mode.plaf.DefaultModeUI;

import de.iabg.swing.KeyList;
import de.iabg.swing.Loggable;

import de.iabg.swing.event.LoggableLogListener;
import de.iabg.swing.event.LogEvent;
import de.iabg.swing.event.LogListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JPanel;

import javax.swing.event.EventListenerList;

import javax.swing.table.TableModel;

/*******************************************************************************
 * An implementation for storing data for a dynamic mode shape matrix.  This
 * component can import mode shape data and store the data for later use.  Refer
 * to {@link ModeModel} class API for more details.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 12, 2008
 ******************************************************************************/
public class JModePanel extends JPanel
        implements Loggable {
    /** A list for event listeners */
    protected EventListenerList listenerList_;
    
    /** The listener that handles {@code LogEvents} */
    protected LogListener logListener_;
    
    /** The {@code ModeModel} of this component */
    protected ModeModel modeModel_;
    
    /** The {@code ModeUI} of this component */
    protected ModeUI modeUI_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code JModePanel} with the given name.
     * 
     * @param   name    the name of this component
     **************************************************************************/
    public JModePanel(String name) {
        super();
        
        listenerList_   = new EventListenerList();
        
        this.setName(name.trim());
        this.setModeModel(new DefaultModeModel(this));
        this.setModeUI(new DefaultModeUI());
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void addLogListener(LogListener listener) {
        listenerList_.add(LogListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     * Deselects all currently stored modes.
     **************************************************************************/
    public void deselectAllModeKeys() {
        modeModel_.deselectAllModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void fireLogChanged(String log) {
        LogListener[] listeners;
        listeners = listenerList_.getListeners(LogListener.class);
        
        for (LogListener listener : listeners) {
            listener.logChanged(new LogEvent(this, log));
        }
    } // eom
    
    
    
    /***************************************************************************
     * Returns the file where the mode shape data was last imported.
     * 
     * @return  the last imported mode shape file
     **************************************************************************/
    public File getModeFile() {
        return modeModel_.getModeFile();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored modes.
     * 
     * @return  the {@link ModeMatrix} storing the modes
     **************************************************************************/
    public ModeMatrix getModes() {
        return modeModel_.getModes();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.table.TableModel} of the modes.
     * 
     * @return  the {@link javax.swing.table.TableModel} of the modes
     **************************************************************************/
    public TableModel getModeTableModel() {
        return modeModel_.getModeTableModel();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link KeyList} of the currently selected modes.
     * 
     * @return  the {@link KeyList} of the currently selected modes
     **************************************************************************/
    public KeyList getSelectedModeKeys() {
        return modeModel_.getSelectedModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeModel} has selected any modes.
     * 
     * @return  {@code true} if this {@code ModeModel} has selected any modes;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasModeKeys() {
        return modeModel_.hasModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeModel} is storing any modes.
     * 
     * @return  {@code true} if this {@code ModeModel} stores any modes;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasModes() {
        return modeModel_.hasModes();
    } // eom
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link ModeReader} for the given file
     * and imports all modes contained in the file.
     * 
     * @param   fileName                        the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     * @throws  java.io.IOException             if the file could not be read
     **************************************************************************/
    public void importModes(String fileName)
            throws  FileNotFoundException,
                    IOException {
        modeModel_.importModes(fileName);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void removeLogListener(LogListener listener) {
        listenerList_.remove(LogListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     * Selects all currently stored modes.
     **************************************************************************/
    public void selectAllModeKeys() {
        modeModel_.selectAllModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Selects all currently stored modes and is equivalent to calling
     * {@link #selectAllModeKeys()}.
     **************************************************************************/
    public void setDefaultKeys() {
        modeModel_.setDefaultKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Sets the {@link ModeModel} that this component represents.
     * 
     * @param   modeModel   the new {@code ModeModel}
     **************************************************************************/
    public void setModeModel(ModeModel modeModel) {
        if (modeModel_ != null) {
            modeModel_.removeLogListener(logListener_);
            
            logListener_    = null;
        }
        
        modeModel_ = modeModel;
        if (modeModel_ != null) {
            logListener_    = new LoggableLogListener(this);
            
            modeModel_.addLogListener(logListener_);
        }
        
        this.invalidate();
    } // eom
    
    
    
    /***************************************************************************
     * Sets the {@link ModeUI} Look and Feel that renders this component.
     * 
     * @param   modeUI  the new {@code ModeUI} Look and Feel object
     **************************************************************************/
    public void setModeUI(ModeUI modeUI) {
        if (modeUI_ != null) {
            modeUI_.uninstallUI(this);
        }
        
        modeUI_ = modeUI;
        if (modeUI_ != null) {
            modeUI_.installUI(this);
        }
        
        this.invalidate();
    } // eom
    
    
    
    /***************************************************************************
     * Returns a formatted string containing the number of currently stored
     * modes and nodes.
     * 
     * @return  the string representation of this {@code JModePanel}
     **************************************************************************/
    @Override
    public String toString() {
        return modeModel_.toString();
    } // eom
} // eoc