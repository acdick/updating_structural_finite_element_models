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

import de.iabg.swing.event.LogEvent;
import de.iabg.swing.event.LogListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.event.EventListenerList;

import javax.swing.table.TableModel;

/*******************************************************************************
 * This implementation of {@link ModeModel} attempts to create all the
 * functionality described in the {@code ModeModel} class description.  Refer
 * to the class API for more details.
 * 
 * Because the aim of this model is to represent the mode shapes in a tabular
 * form, the mode data has been wrapped in another object, called
 * {@link ModeTableModel}, which implements {@link javax.swing.table.TableModel}
 * and is ready to be added to a Swing component.  Therefore most of the mehtods
 * in this class simply wrap methods from {@code ModeTableModel}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 12, 2008
 ******************************************************************************/
public class DefaultModeModel
        implements ModeModel {
    /** The {@link JModePanel} that this model is designed for */
    protected JModePanel modePanel_;
    
    /** A list for event listeners */
    protected EventListenerList listenerList_;
    
    /** The file of the currently stored modes */
    protected File modeFile_;
    
    /** A {@link ModeTableModel} for the mode shapes */
    protected ModeTableModel modeTableModel_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code DefaultModeModel} with no stored data.  This
     * constructor also initializes all the storage objects, which should not
     * be destroyed during the life of this object.  This is because listeners
     * may register themselves to these objects, and will not receive
     * notifications if the references to these objects are changed.  This
     * constructor also stores a reference to the {@link JModePanel}, so that
     * this model has access to all the methods of that component.
     * 
     * @param   modePanel   the {@code JModePanel} that this model is designed
     *                      for
     **************************************************************************/
    public DefaultModeModel(JModePanel modePanel) {
        modePanel_      = modePanel;
        listenerList_   = new EventListenerList();
        modeTableModel_ = new ModeTableModel();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void addLogListener(LogListener listener) {
        listenerList_.add(LogListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void deselectAllModeKeys() {
        modeTableModel_.deselectAllModeKeys();
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
     **************************************************************************/
    public File getModeFile() {
        return modeFile_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public ModeMatrix getModes() {
        return modeTableModel_.getModes();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public TableModel getModeTableModel() {
        return modeTableModel_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public KeyList getSelectedModeKeys() {
        return modeTableModel_.getSelectedModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasModeKeys() {
        return modeTableModel_.hasModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasModes() {
        return modeTableModel_.hasModes();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importModes(String fileName)
            throws  FileNotFoundException,
                    IOException {
        long time;
        
        modeFile_ = new File(fileName.trim());
        
        this.fireLogChanged("Opening source file: " + fileName);
        time = System.currentTimeMillis();
        modeTableModel_.importModes(modeFile_);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Read: " + modeTableModel_.getModeCount() +
                " modes (total time: " + time + " seconds)");
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void removeLogListener(LogListener listener) {
        listenerList_.remove(LogListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void selectAllModeKeys() {
        modeTableModel_.selectAllModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setDefaultKeys() {
        this.selectAllModeKeys();
        this.fireLogChanged("Set: " + this.getSelectedModeKeys().size() +
                " default mode keys");
    } // eom
    
    
    
    /***************************************************************************
     * Returns a formatted string containing the number of currently stored
     * modes and nodes.
     * 
     * @return  the string representation of this {@code ModeModel}
     **************************************************************************/
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        
        result.append(String.format("%16s", "MODE SHAPES"));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Modes:"));
        result.append(String.format(" %7d", modeTableModel_.getModeCount()));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Nodes:"));
        result.append(String.format(" %7d", modeTableModel_.getNodeCount()));
        result.append(String.format("%n"));
        
        return result.toString();
    } // eom
} // eoc