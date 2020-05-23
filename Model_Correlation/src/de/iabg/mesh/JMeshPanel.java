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

import de.iabg.mesh.plaf.DefaultMeshUI;

import de.iabg.swing.KeyList;
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
 * An implementation of a Finite Element Mesh panel.  This component can import
 * geometric data and store the data for later use.  Refer to {@link MeshModel}
 * class API for more details.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public class JMeshPanel extends JPanel
        implements  Loggable,
                    Renderable {
    /** A list for event listeners */
    protected EventListenerList listenerList_;
    
    /** The listener that handles {@code LogEvents} */
    protected LogListener logListener_;
    
    /** The {@code MeshModel} of this component */
    protected MeshModel meshModel_;
    
    /** The {@code MeshUI} of this component */
    protected MeshUI meshUI_;
    
    /** The listener that handles {@code SceneEvents} */
    protected SceneListener sceneListener_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code JMeshPanel} with the given name.
     * 
     * @param   name    the name of this component
     **************************************************************************/
    public JMeshPanel(String name) {
        super();
        
        listenerList_   = new EventListenerList();
        
        this.setName(name.trim());
        this.setMeshModel(new DefaultMeshModel(this));
        this.setMeshUI(new DefaultMeshUI());
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
        return meshModel_.getBoundingRadius2D();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getBoundingRadius3D() {
        return meshModel_.getBoundingRadius3D();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored edges.
     * 
     * @return  the {@link NodeConnectionMatrix} storing the edges
     **************************************************************************/
    public NodeConnectionMatrix getEdges() {
        return meshModel_.getEdges();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.ComboBoxModel} of the geometry colors that
     * can be rendered in Java3D.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} of the geometry colors
     **************************************************************************/
    public ComboBoxModel getGeometryColorModel() {
        return meshModel_.getGeometryColorModel();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the file where the geometric data was last imported.
     * 
     * @return  the last imported geometry file
     **************************************************************************/
    public File getGeometryFile() {
        return meshModel_.getGeometryFile();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.ComboBoxModel} of the groups of node
     * subsets.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} of the node key groups
     **************************************************************************/
    public ComboBoxModel getNodeKeyModel() {
        return meshModel_.getNodeKeyModel();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored nodes.
     * 
     * @return  the {@link NodeMatrix} storing the nodes
     **************************************************************************/
    public NodeMatrix getNodes() {
        return meshModel_.getNodes();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored quadrilaterals.
     * 
     * @return  the {@link NodeConnectionMatrix} storing the quadrilaterals
     **************************************************************************/
    public NodeConnectionMatrix getQuadrilaterals() {
        return meshModel_.getQuadrilaterals();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene2D(double scale, Color3f backgroundColor) {
        return meshModel_.getScene2D(scale, backgroundColor);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene3D(double scale, Color3f backgroundColor) {
        return meshModel_.getScene3D(scale, backgroundColor);
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link KeyList} of the currently selected node subset group.
     * 
     * @return  the {@link KeyList} of the currently selected node keys
     **************************************************************************/
    public KeyList getSelectedNodeKeys() {
        return meshModel_.getSelectedNodeKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored triangles.
     * 
     * @return  the {@link NodeConnectionMatrix} storing the triangles
     **************************************************************************/
    public NodeConnectionMatrix getTriangles() {
        return meshModel_.getTriangles();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code JMeshPanel} is storing any edges.
     * 
     * @return  {@code true} if this {@code JMeshPanel} stores any edges;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasEdges() {
        return meshModel_.hasEdges();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code JMeshPanel} is storing any node subsets.
     * 
     * @return  {@code true} if this {@code JMeshPanel} stores any node subsets;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasNodeKeys() {
        return meshModel_.hasNodeKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code JMeshPanel} is storing any nodes.
     * 
     * @return  {@code true} if this {@code JMeshPanel} stores any nodes;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasNodes() {
        return meshModel_.hasNodes();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code JMeshPanel} is storing any quadrilaterals.
     * 
     * @return  {@code true} if this {@code JMeshPanel} stores any
     *          quadrilaterals; {@code false} otherwise
     **************************************************************************/
    public boolean hasQuadrilaterals() {
        return meshModel_.hasQuadrilaterals();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code JMeshPanel} is storing any triangles.
     * 
     * @return  {@code true} if this {@code JMeshPanel} stores any triangles;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasTriangles() {
        return meshModel_.hasTriangles();
    } // eom
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link GeometryReader} for the given file
     * and imports all the available geometry contained in the file.  This
     * includes the nodes, edges, triangles, and quadrilaterals.
     * 
     * @param   fileName                        the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     * @throws  java.io.IOException             if the file could not be read
     **************************************************************************/
    public void importGeometry(String fileName)
            throws  FileNotFoundException,
                    IOException {
        meshModel_.importGeometry(fileName.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link KeyReader} for the given file and imports
     * all the available groups of node subsets contained in the file.
     * 
     * @param   fileName                        the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     * @throws  java.io.IOException             if the file could not be read
     **************************************************************************/
    public void importNodeKeyLists(String fileName)
            throws  FileNotFoundException,
                    IOException {
        meshModel_.importNodeKeyLists(fileName.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Tests if all the node keys of this {@code JMeshPanel} are contained in
     * the stored nodes.  If there are no stored nodes or no stored node keys,
     * this method should return {@code true}.
     * 
     * @return  {@code true} if all node keys are contains in the nodes, if
     *          there are no stored nodes, or if there are no stored node key;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean isConsistent() {
        return meshModel_.isConsistent();
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
     * Creates a default node subset group called {@code All Nodes}, which is
     * inserted to the node key model.  This group should contain all of the
     * currently stored nodes.
     **************************************************************************/
    public void setDefaultKeys() {
        meshModel_.setDefaultKeys();
    } // eom
    
    
    
    /***************************************************************************
     * Sets the {@link MeshModel} that this component represents.
     * 
     * @param   meshModel   the new {@code MeshModel}
     **************************************************************************/
    public void setMeshModel(MeshModel meshModel) {
        if (meshModel_ != null) {
            meshModel_.removeLogListener(logListener_);
            meshModel_.removeSceneListener(sceneListener_);
            
            logListener_    = null;
            sceneListener_  = null;
        }
        
        meshModel_ = meshModel;
        if (meshModel_ != null) {
            logListener_    = new LoggableLogListener(this);
            sceneListener_  = new RenderableSceneListener(this);
            
            meshModel_.addLogListener(logListener_);
            meshModel_.addSceneListener(sceneListener_);
        }
        
        this.invalidate();
    } // eom
    
    
    
    /***************************************************************************
     * Sets the {@link MeshUI} Look and Feel that renders this component.
     * 
     * @param   meshUI  the new {@code MeshUI} Look and Feel object
     **************************************************************************/
    public void setMeshUI(MeshUI meshUI) {
        if (meshUI_ != null) {
            meshUI_.uninstallUI(this);
        }
        
        meshUI_ = meshUI;
        if (meshUI_ != null) {
            meshUI_.installUI(this);
        }
        
        this.invalidate();
    } // eom
    
    
    
    /***************************************************************************
     * Returns a formatted string containing the number of currently stored
     * nodes, edges, triangles, quadrilaterals, and node set groups.
     * 
     * @return  the string representation of this {@code JMeshPanel}
     **************************************************************************/
    @Override
    public String toString() {
        return meshModel_.toString();
    } // eom
} // eoc