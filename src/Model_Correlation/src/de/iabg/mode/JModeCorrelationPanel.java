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

import de.iabg.j3d.Renderable;

import de.iabg.j3d.event.RenderableSceneListener;
import de.iabg.j3d.event.SceneEvent;
import de.iabg.j3d.event.SceneListener;

import de.iabg.mesh.JMeshConnectionPanel;

import de.iabg.swing.KeyList;
import de.iabg.swing.Loggable;

import de.iabg.swing.event.LoggableLogListener;
import de.iabg.swing.event.LogEvent;
import de.iabg.swing.event.LogListener;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.media.j3d.BranchGroup;

import javax.swing.ComboBoxModel;
import javax.swing.JPanel;
import javax.swing.JTable;

import javax.swing.event.EventListenerList;

import javax.swing.table.TableModel;

import javax.vecmath.Color3f;

/*******************************************************************************
 * An implementation for for correlating two mode shape matrices.  This
 * component can import geometric data, mode shape data, and mass matrix data.
 * With this data, this component can compute modal correlations and modal
 * connections, and export those correlations and connections for later use.
 * Refer to {@link ModeCorrelationModel} class API for more details.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 12, 2008
 ******************************************************************************/
public class JModeCorrelationPanel extends JPanel
        implements  Loggable,
                    Renderable {
    /** The {@code ModeCorrelationModel} of this component */
    protected ModeCorrelationModel correlationModel_;
    
    /** The {@code ModeCorrelationUI} of this component */
    protected ModeCorrelationUI correlationUI_;
    
    /** A list for event listeners */
    protected EventListenerList listenerList_;
    
    /** The listener that handles {@code LogEvents} */
    protected LogListener logListener_;
    
    /** The listener that handles {@code SceneEvents} */
    protected SceneListener sceneListener_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code JModeCorrelationPanel} with the given name,
     * {@code ModeCorrelationModel}, and {@code ModeCorrelationUI}.
     * 
     * @param   name                the name of this component
     * @param   correlationModel    the {@code ModeCorrelationModel} of this
     *                              component
     * @param   correlationUI       the {@code ModeCorrelationUI} of this
     *                              component
     **************************************************************************/
    public JModeCorrelationPanel(String name,
            ModeCorrelationModel correlationModel,
            ModeCorrelationUI correlationUI) {
        super();
        
        listenerList_   = new EventListenerList();
        
        this.setName(name.trim());
        this.setCorrelationModel(correlationModel);
        this.setCorrelationUI(correlationUI);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void addLogListener(LogListener listener) {
        listenerList_.add(LogListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void addSceneListener(SceneListener listener) {
        listenerList_.add(SceneListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     * Computes and stores the modal correlation between the {@code First Mode}
     * and the {@code Last Mode}.
     **************************************************************************/
    public void correlateModes() {
        correlationModel_.correlateModes();
    } // eom
    
    
    
    /***************************************************************************
     * Deselects all currently stored {@code First Modes}.
     **************************************************************************/
    public void deselectAllFirstModeKeys() {
        correlationModel_.deselectAllFirstModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link ModeConnectionWriter} for the given file
     * and exports all stored mode connections.
     * 
     * @param   fileName            the file to be exported
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public void exportModeConnection(String fileName)
            throws IOException {
        correlationModel_.exportModeConnection(fileName);
    } // eom
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link ModeCorrelationWriter} for the given file
     * and exports all stored mode correlations.
     * 
     * @param   fileName            the file to be exported
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public void exportModeCorrelation(String fileName)
            throws IOException {
        correlationModel_.exportModeCorrelation(fileName);
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
    public void fireScene2DChanged(Object source) {
        SceneListener[] listeners;
        listeners = listenerList_.getListeners(SceneListener.class);
        
        for (SceneListener listener : listeners) {
            listener.scene2DChanged(new SceneEvent(source));
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void fireScene3DChanged(Object source) {
        SceneListener[] listeners;
        listeners = listenerList_.getListeners(SceneListener.class);
        
        for (SceneListener listener : listeners) {
            listener.scene3DChanged(new SceneEvent(source));
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getBoundingRadius2D() {
        return correlationModel_.getBoundingRadius2D();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getBoundingRadius3D() {
        return correlationModel_.getBoundingRadius3D();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored {@code First Modes}.
     * 
     * @return  the {@link JModePanel} of the {@code First Modes}
     **************************************************************************/
    public JModePanel getFirstMode() {
        return correlationModel_.getFirstMode();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link KeyList} of the currently selected
     * {@code First Modes}.
     * 
     * @return  the {@link KeyList} of the currently selected
     *          {@code First Modes}
     **************************************************************************/
    public KeyList getFirstSelectedModeKeys() {
        return correlationModel_.getFirstSelectedModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored {@code Last Modes}.
     * 
     * @return  the {@link JModePanel} of the {@code Last Modes}
     **************************************************************************/
    public JModePanel getLastMode() {
        return correlationModel_.getLastMode();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.ComboBoxModel} of the mass matrices.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} of the mass matrices
     **************************************************************************/
    public ComboBoxModel getMassKeyModel() {
        return correlationModel_.getMassKeyModel();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored mass matrices.
     * 
     * @return  the {@link MassMatrixMap} of the mass matrices
     **************************************************************************/
    public MassMatrixMap getMassMatrices() {
        return correlationModel_.getMassMatrices();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored {@link JMeshConnectionPanel} of the
     * {@code First Mesh} and {@code Last Mesh}.
     * 
     * @return  the {@code JMeshConnectionPanel} of the {@code First Mesh} and
     *          {@code Last Mesh}
     **************************************************************************/
    public JMeshConnectionPanel getMeshConnection() {
        return correlationModel_.getMeshConnection();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored mode correlations
     * 
     * @return  the {@link ModeCorrelationMatrix} storing the mode correlations
     **************************************************************************/
    public ModeCorrelationMatrix getModeCorrelation() {
        return correlationModel_.getModeCorrelation();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.table.TableModel} of the mode correlation.
     * 
     * @return  the {@link javax.swing.table.TableModel} of the mode correlation
     **************************************************************************/
    public TableModel getModeCorrelationTableModel() {
        return correlationModel_.getModeCorrelationTableModel();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene2D(double scale, Color3f backgroundColor) {
        return correlationModel_.getScene2D(scale, backgroundColor);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene3D(double scale, Color3f backgroundColor) {
        return correlationModel_.getScene3D(scale, backgroundColor);
    } // eom
    
    
    
    /***************************************************************************
     * Tests if there is a currently selected mass matrix.
     * 
     * @return  {@code true} if there is a currently selected mass matrix;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasMassKey() {
        return correlationModel_.hasMassKey();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if there are any currently stored mass matrices.
     * 
     * @return  {@code true} if if there are any currently stored mass matrices;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasMassMatrices() {
        return correlationModel_.hasMassMatrices();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mode} and the {@code Last Mode} are both
     * storing any currently selected mode selections.
     * 
     * @return  {@code true} if the {@code First Mode} and the {@code Last Mode}
     *          are both storing any currently selected mode selections;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasModeKeys() {
        return correlationModel_.hasModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mode} and the {@code Last Mode} are both
     * storing any modes.
     * 
     * @return  {@code true} if the {@code First Mode} and the {@code Last Mode}
     *          both store any modes; {@code false} otherwise
     **************************************************************************/
    public boolean hasModes() {
        return correlationModel_.hasModes();
    } // eom
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link MaterialReader} for the given file
     * and imports all the available material contained in the file.  This
     * includes the mass and stiffness matrices.
     * 
     * @param   fileName                        the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     * @throws  java.io.IOException             if the file could not be read
     **************************************************************************/
    public void importMassMatrices(String fileName)
            throws  FileNotFoundException,
                    IOException {
        correlationModel_.importMassMatrices(fileName);
    } // eom
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mode}, {@code Last Mode}, mass matrices and
     * {@link JMeshConnectionPanel} are consistent.
     * 
     * @return  {@code true} if the {@code First Mode}, {@code Last Mode},
     *          mass matrices and {@link JMeshConnectionPanel} are consistent;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean isConsistent() {
        return correlationModel_.isConsistent();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeCorrelationModel} is storing any connections
     * between the {@code First Mode} and the {@code Last Mode}.
     * 
     * @return  {@code true} if this {@code ModeCorrelationModel} stores any
     *          mode connections; {@code false} otherwise
     **************************************************************************/
    public boolean isModeConnected() {
        return correlationModel_.isModeConnected();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeCorrelationModel} is storing any correlations
     * between the {@code First Mode} and the {@code Last Mode}.
     * 
     * @return  {@code true} if this {@code ModeCorrelationModel} stores any
     *          mode correlations; {@code false} otherwise
     **************************************************************************/
    public boolean isModeCorrelated() {
        return correlationModel_.isModeCorrelated();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if the {@code JMeshConnectionPanel} is storing any connections
     * between the {@code First Mesh} and the {@code Last Mesh}.
     * 
     * @return  {@code true} if the {@code JMeshConnectionPanel} stores any
     *          node connections; {@code false} otherwise
     **************************************************************************/
    public boolean isNodeConnected() {
        return correlationModel_.isNodeConnected();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void removeLogListener(LogListener listener) {
        listenerList_.remove(LogListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void removeSceneListener(SceneListener listener) {
        listenerList_.remove(SceneListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     * Selects all currently stored {@code First Modes}.
     **************************************************************************/
    public void selectAllFirstModeKeys() {
        correlationModel_.selectAllFirstModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Sets all the column properties for the given {@link javax.swing.JTable}.
     * 
     * @param   correlationTable    the {@code JTable} whose columns properties
     *                              are set
     **************************************************************************/
    public void setColumnProperties(JTable correlationTable) {
        correlationModel_.setColumnProperties(correlationTable);
    } // eom
    
    
    
    /***************************************************************************
     * Sets the {@link ModeCorrelationModel} that this component represents.
     * 
     * @param   correlationModel the new {@code ModeCorrelationModel}
     **************************************************************************/
    public void setCorrelationModel(ModeCorrelationModel correlationModel) {
        if (correlationModel_ != null) {
            correlationModel_.removeLogListener(logListener_);
            correlationModel_.removeSceneListener(sceneListener_);
            
            logListener_    = null;
            sceneListener_  = null;
        }
        
        correlationModel_ = correlationModel;
        if (correlationModel_ != null) {
            logListener_    = new LoggableLogListener(this);
            sceneListener_  = new RenderableSceneListener(this);
            
            correlationModel_.addLogListener(logListener_);
            correlationModel_.addSceneListener(sceneListener_);
        }
        
        this.invalidate();
    } // eom
    
    
    
    /***************************************************************************
     * Sets the {@link ModeCorrelationUI} Look and Feel that renders this
     * component.
     * 
     * @param   correlationUI   the new {@code ModeCorrelationUI} Look and Feel
     *                          object
     **************************************************************************/
    public void setCorrelationUI(ModeCorrelationUI correlationUI) {
        if (correlationUI_ != null) {
            correlationUI_.uninstallUI(this);
        }
        
        correlationUI_ = correlationUI;
        if (correlationUI_ != null) {
            correlationUI_.installUI(this);
        }
        
        this.invalidate();
    } // eom
    
    
    
    /***************************************************************************
     * Sets the default mode connection between the {@code FirstMode} and
     * {@code Last Mode} by calling
     * {@link ModeCorrelationMatrix#getPreferredConnection(double)} of the
     * stored {@code ModeCorrelationMatrix}.
     **************************************************************************/
    public void setDefaultKeys() {
        correlationModel_.setDefaultKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Sets the value of the lower tolerance to the given value.  The lower
     * tolerance cannot be greater than the upper tolerance.
     * 
     * @param   lowerTolerance  the new lower tolerance
     **************************************************************************/
    public void setLowerTolerance(double lowerTolerance) {
        correlationModel_.setLowerTolerance(lowerTolerance);
    } // eom
    
    
    
    /***************************************************************************
     * Sets the value of the upper tolerance to the given value.  The upper
     * tolerance cannot be less than the lower tolerance.
     * 
     * @param   upperTolerance  the new upper tolerance
     **************************************************************************/
    public void setUpperTolerance(double upperTolerance) {
        correlationModel_.setUpperTolerance(upperTolerance);
    } // eom
    
    
    
    /***************************************************************************
     * Returns a formatted string containing the data of the {@code First Mesh},
     * {@code Last Mesh}, {@code First Mode}, {@code Last Mode}, mass matrices,
     * the number of currently stored node connections, and the number of
     * currently stored mode correlations.
     * 
     * @return  the string representation of this {@code JModeCorrelationPanel}
     **************************************************************************/
    @Override
    public String toString() {
        return correlationModel_.toString();
    } // eom
} // eoc