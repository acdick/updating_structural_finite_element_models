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
package de.iabg.mesh;

import de.iabg.j3d.Renderable;

import de.iabg.j3d.event.RenderableSceneListener;
import de.iabg.j3d.event.SceneEvent;
import de.iabg.j3d.event.SceneListener;

import de.iabg.mesh.plaf.DefaultMeshConnectionUI;

import de.iabg.swing.Loggable;

import de.iabg.swing.event.LoggableLogListener;
import de.iabg.swing.event.LogEvent;
import de.iabg.swing.event.LogListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.media.j3d.BranchGroup;

import javax.swing.ComboBoxModel;
import javax.swing.JPanel;

import javax.swing.event.EventListenerList;

import javax.vecmath.Color3f;

/*******************************************************************************
 * An implementation for performing a geometrical connection of two Finite
 * Element Mesh panels.  This component can compute, import, and export
 * geometric data and store the data for later use.  Refer to
 * {@link MeshConnectionModel} class API for more details.
 * 
 * @author  Adam C. Dick, BSE
 * @version November 1, 2008
 ******************************************************************************/
public class JMeshConnectionPanel extends JPanel
        implements  Loggable,
                    Renderable {
    /** The {@code MeshConnectionModel} of this component */
    protected MeshConnectionModel connectionModel_;
    
    /** The {@code MeshConnectionUI} of this component */
    protected MeshConnectionUI connectionUI_;
    
    /** A list for event listeners */
    protected EventListenerList listenerList_;
    
    /** The listener that handles {@code LogEvents} */
    protected LogListener logListener_;
    
    /** The listener that handles {@code SceneEvents} */
    protected SceneListener sceneListener_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code JMeshConnectionPanel} with the given name.
     * 
     * @param   name    the name of this component
     **************************************************************************/
    public JMeshConnectionPanel(String name) {
        super();
        
        listenerList_   = new EventListenerList();
        
        this.setName(name.trim());
        this.setConnectionModel(new DefaultMeshConnectionModel(this));
        this.setConnectionUI(new DefaultMeshConnectionUI());
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
     * Computes and stores the nodal connection between the {@code First Mesh}
     * and the {@code Last Mesh}.
     * 
     * @param   tolerance   the maximum tolerance between any two connected
     *                      nodes
     **************************************************************************/
    public void connectNodes(double tolerance) {
        connectionModel_.connectNodes(tolerance);
    } // eom
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link MeshConnectionWriter} for the given file
     * and exports all stored node connections.
     * 
     * @param   fileName            the file to be exported
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public void exportNodeConnections(String fileName)
            throws IOException {
        connectionModel_.exportNodeConnections(fileName);
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
        return connectionModel_.getBoundingRadius2D();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getBoundingRadius3D() {
        return connectionModel_.getBoundingRadius3D();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.ComboBoxModel} of the connection colors
     * that can be rendered in Java3D.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} of the connection colors
     **************************************************************************/
    public ComboBoxModel getConnectionColorModel() {
        return connectionModel_.getConnectionColorModel();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the file where the connection data was last imported.
     * 
     * @return  the last imported connection file
     **************************************************************************/
    public File getConnectionFile() {
        return connectionModel_.getConnectionFile();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.ComboBoxModel} of the connection type.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} of the connection type
     **************************************************************************/
    public ComboBoxModel getConnectionTypeModel() {
        return connectionModel_.getConnectionTypeModel();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@code First Mesh}.
     * 
     * @return  the {@link JMeshPanel} of the {@code First Mesh}
     **************************************************************************/
    public JMeshPanel getFirstMesh() {
        return connectionModel_.getFirstMesh();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.ComboBoxModel} of the independent mesh.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} of the independent mesh
     **************************************************************************/
    public ComboBoxModel getIndependentMeshModel() {
        return connectionModel_.getIndependentMeshModel();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@code Last Mesh}.
     * 
     * @return  the {@link JMeshPanel} of the {@code Last Mesh}
     **************************************************************************/
    public JMeshPanel getLastMesh() {
        return connectionModel_.getLastMesh();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored node connections.
     * 
     * @return  the {@link NodeConnectionMatrix} storing the node connections
     **************************************************************************/
    public NodeConnectionMatrix getNodeConnections() {
        return connectionModel_.getNodeConnections();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene2D(double scale, Color3f backgroundColor) {
        return connectionModel_.getScene2D(scale, backgroundColor);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene3D(double scale, Color3f backgroundColor) {
        return connectionModel_.getScene3D(scale, backgroundColor);
    } // eom
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mesh} and the {@code Last Mesh} are both
     * storing any groups of node subsets.
     * 
     * @return  {@code true} if the {@code First Mesh} and the {@code Last Mesh}
     *          both store any groups of node subsets; {@code false} otherwise
     **************************************************************************/
    public boolean hasNodeKeys() {
        return connectionModel_.hasNodeKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mesh} and the {@code Last Mesh} are both
     * storing any nodes.
     * 
     * @return  {@code true} if the {@code First Mesh} and the {@code Last Mesh}
     *          both store any nodes; {@code false} otherwise
     **************************************************************************/
    public boolean hasNodes() {
        return connectionModel_.hasNodes();
    } // eom
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link MeshConnectionReader} for the given file
     * and imports all the available node connections contained in the file.
     * 
     * @param   fileName                        the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     * @throws  java.io.IOException             if the file could not be read
     **************************************************************************/
    public void importNodeConnections(String fileName)
            throws  FileNotFoundException,
                    IOException {
        connectionModel_.importNodeConnections(fileName.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code JMeshConnectionPanel} is storing any connections
     * between the {@code First Mesh} and the {@code Last Mesh}.
     * 
     * @return  {@code true} if this {@code JMeshConnectionPanel} stores any
     *          node connections; {@code false} otherwise
     **************************************************************************/
    public boolean isConnected() {
        return connectionModel_.isConnected();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mesh} and {@code Last Mesh} are both
     * consistent.
     * 
     * @return  {@code true} if the {@code First Mesh} and {@code Last Mesh} are
     *          both consistent; {@code false} otherwise
     **************************************************************************/
    public boolean isConsistent() {
        return connectionModel_.isConsistent();
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
     * Sets the connected degrees of freedom.
     * 
     * @param  connectionDOF the currently connected degrees of freedom
     **************************************************************************/
    public void setConnectionDegreesOfFreedom(String connectionDOF) {
        connectionModel_.setConnectionDegreesOfFreedom(connectionDOF);
    } // eom
    
    
    
    /***************************************************************************
     * Sets the connection identifier.
     * 
     * @param  connectionIdentifier the current connection identifier
     **************************************************************************/
    public void setConnectionIdentifier(int connectionIdentifier) {
        connectionModel_.setConnectionIdentifier(connectionIdentifier);
    } // eom
    
    
    
    /***************************************************************************
     * Sets the {@link MeshConnectionModel} that this component represents.
     * 
     * @param   connectionModel the new {@code MeshConnectionModel}
     **************************************************************************/
    public void setConnectionModel(MeshConnectionModel connectionModel) {
        if (connectionModel_ != null) {
            connectionModel_.removeLogListener(logListener_);
            connectionModel_.removeSceneListener(sceneListener_);
            
            logListener_    = null;
            sceneListener_  = null;
        }
        
        connectionModel_ = connectionModel;
        if (connectionModel_ != null) {
            logListener_    = new LoggableLogListener(this);
            sceneListener_  = new RenderableSceneListener(this);
            
            connectionModel_.addLogListener(logListener_);
            connectionModel_.addSceneListener(sceneListener_);
        }
        
        this.invalidate();
    } // eom
    
    
    
    /***************************************************************************
     * Sets the {@link MeshConnectionUI} Look and Feel that renders this
     * component.
     * 
     * @param   connectionUI    the new {@code MeshConnectionUI} Look and Feel
     *                          object
     **************************************************************************/
    public void setConnectionUI(MeshConnectionUI connectionUI) {
        if (connectionUI_ != null) {
            connectionUI_.uninstallUI(this);
        }
        
        connectionUI_ = connectionUI;
        if (connectionUI_ != null) {
            connectionUI_.installUI(this);
        }
        
        this.invalidate();
    } // eom
    
    
    
    /***************************************************************************
     * Returns a formatted string containing the data of the {@code First Mesh},
     * the {@code Last Mesh}, and the number of currently stored node
     * connections.
     * 
     * @return  the string representation of this {@code JMeshConnectionPanel}
     **************************************************************************/
    @Override
    public String toString() {
        return connectionModel_.toString();
    } // eom
} // eoc