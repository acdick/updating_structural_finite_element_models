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

import de.iabg.j3d.ColorConstants;

import de.iabg.j3d.event.SceneEvent;
import de.iabg.j3d.event.SceneListener;

import de.iabg.mesh.io.ASCIIMeshConnectionReader;
import de.iabg.mesh.io.ASCIIMeshConnectionWriter;

import de.iabg.swing.KeyList;

import de.iabg.swing.event.LogEvent;
import de.iabg.swing.event.LogListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.Shape3D;

import javax.swing.ComboBoxModel;

import javax.swing.event.EventListenerList;

import javax.vecmath.Color3f;

/*******************************************************************************
 * This implementation of {@link MeshConnectionModel} attempts to create all the
 * functionality described in the {@code MeshConnectionModel} class description.
 * Refer to the class API for more details.
 * 
 * Much of the implementation of this class should be straight-forward, however,
 * a more in-depth summary of the connection algorithm may clarify some
 * conceptual issues.  There exist two {@link NodeMatrix} objects, one for the
 * {@code First Nodes} and one for the {@code Last Nodes}.  A
 * {@link NodeCorrelationMatrix} is created by calling
 * {@link NodeMatrix#getReducedDistanceCorrelation(de.iabg.mesh.NodeMatrix,
 * de.iabg.swing.KeyList, de.iabg.swing.KeyList)} and using the
 * {@code First Nodes}, {@code Last Nodes}, and their corresponding node keys
 * as arguments.  Once that has been completed, a {@link NodeConnectionMatrix}
 * with a maximum distance tolerance is created by calling
 * {@link NodeCorrelationMatrix#getPreferredConnection(double)}.  In these two
 * steps, the two {@code NodeMatrix} objects are connected.
 * 
 * To render the connected nodes in Java3D, a similar procedure is done as in
 * {@link DefaultMeshModel} for edges, triangles and quadrilaterals.  The scene
 * of the {@code First Mesh} and the scene of the {@code Last Mesh} are rendered
 * first.  After that, copies of the {@code First Nodes} and {@code Last Nodes}
 * are sorted according to the corresponding connected nodes in the stored
 * {@code NodeConnectionMatrix}.  The two resulting {@code NodeMatrix} objects
 * can then be added to the Java3D scene.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 26, 2008
 ******************************************************************************/
public class DefaultMeshConnectionModel
        implements MeshConnectionModel {
    /** A {@link javax.swing.ComboBoxModel} for the connection color */
    protected KeyList connectionColors_;
    
    /** The {@link JMeshConnectionPanel} that this model is designed for */
    protected JMeshConnectionPanel connectionPanel_;
    
    /** The {@code First Mesh} */
    protected JMeshPanel firstMeshPanel_;
    
    /** The {@code Last Mesh} */
    protected JMeshPanel lastMeshPanel_;
    
    /** A list for event listeners */
    protected EventListenerList listenerList_;
    
    /** The file of the currently stored node connections */
    protected File nodeConnectionFile_;
    
    /** Storage for the node connections */
    protected NodeConnectionMatrix nodeConnections_;
    
    
    
    /***************************************************************************
     * Constructs a {@code DefaultMeshConnectionModel} with no stored data.
     * This constructor also initializes all the storage objects, which should
     * not be destroyed during the life of this object.  This is because
     * listeners may register themselves to these objects, and will not receive
     * notifications if the references to these objects are changed.  This
     * constructor also stores a reference to the {@link JMeshConnectionPanel},
     * so that this model has access to all the methods of that component.
     * 
     * @param   connectionPanel the {@code JMeshConnectionPanel} that this model
     *                          is designed for
     **************************************************************************/
    public DefaultMeshConnectionModel(JMeshConnectionPanel connectionPanel) {
        connectionPanel_    = connectionPanel;
        listenerList_       = new EventListenerList();
        firstMeshPanel_     = new JMeshPanel("Reference");
        lastMeshPanel_      = new JMeshPanel("Update");
        nodeConnections_    = new NodeConnectionMatrix();
        connectionColors_   = new KeyList();
        
        firstMeshPanel_.getGeometryColorModel().setSelectedItem(
                ColorConstants.GREEN);
        lastMeshPanel_.getGeometryColorModel().setSelectedItem(
                ColorConstants.YELLOW);
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
    public void connectNodes(double tolerance) {
        NodeCorrelationMatrix   nodeCorrelations;
        NodeMatrix              firstNodes;
        KeyList                 firstNodeKeys;
        NodeMatrix              lastNodes;
        KeyList                 lastNodeKeys;
        long                    time;
        
        firstNodes      = firstMeshPanel_.getNodes();
        firstNodeKeys   = firstMeshPanel_.getSelectedNodeKeys();
        lastNodes       = lastMeshPanel_.getNodes();
        lastNodeKeys    = lastMeshPanel_.getSelectedNodeKeys();
        
        this.fireLogChanged("Connecting nodes:");
        time = System.currentTimeMillis();
        nodeCorrelations = firstNodes.getReducedDistanceCorrelation(lastNodes,
                firstNodeKeys, lastNodeKeys);
        nodeConnections_ = nodeCorrelations.getPreferredConnection(tolerance);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Computed: " +
                nodeConnections_.getConnectionCount() +
                " node connections (total time: " + time + " seconds)");
        this.fireScene3DChanged(this);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void exportNodeConnections(String fileName)
            throws IOException {
        MeshConnectionWriter    connectionWriter;
        long                    time;
        
        nodeConnectionFile_ = new File(fileName.trim());
        
        connectionPanel_.fireLogChanged("Creating source file: " + fileName);
        connectionWriter = new ASCIIMeshConnectionWriter(nodeConnectionFile_);
        connectionWriter.setFirstMeshFile(firstMeshPanel_.getGeometryFile());
        connectionWriter.setLastMeshFile(lastMeshPanel_.getGeometryFile());
        
        time = System.currentTimeMillis();
        connectionWriter.exportNodeConnections(nodeConnections_);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Wrote: " + nodeConnections_.getConnectionCount() +
                " node connections (total time: " + time + " seconds)");
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
        double firstBoundingRadius  = firstMeshPanel_.getBoundingRadius2D();
        double lastBoundingRadius   = lastMeshPanel_.getBoundingRadius2D();
        
        return Math.max(firstBoundingRadius, lastBoundingRadius);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getBoundingRadius3D() {
        double firstBoundingRadius  = firstMeshPanel_.getBoundingRadius3D();
        double lastBoundingRadius   = lastMeshPanel_.getBoundingRadius3D();
        
        return Math.max(firstBoundingRadius, lastBoundingRadius);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public ComboBoxModel getConnectionColorModel() {
        connectionColors_.clear();
        connectionColors_.add(ColorConstants.BLACK);
        connectionColors_.add(ColorConstants.BLUE);
        connectionColors_.add(ColorConstants.CYAN);
        connectionColors_.add(ColorConstants.GREEN);
        connectionColors_.add(ColorConstants.MAGENTA);
        connectionColors_.add(ColorConstants.RED);
        connectionColors_.add(ColorConstants.WHITE);
        connectionColors_.add(ColorConstants.YELLOW);
        connectionColors_.setSelectedItem(ColorConstants.RED);
        
        return connectionColors_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public File getConnectionFile() {
        return nodeConnectionFile_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public JMeshPanel getFirstMesh() {
        return firstMeshPanel_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public JMeshPanel getLastMesh() {
        return lastMeshPanel_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public NodeConnectionMatrix getNodeConnections() {
        return nodeConnections_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene2D(double scale, Color3f backgroundColor) {
        BranchGroup branchGroup = new BranchGroup();
        BranchGroup firstGroup  =
                firstMeshPanel_.getScene2D(scale, backgroundColor);
        BranchGroup lastGroup   =
                lastMeshPanel_.getScene2D(scale, backgroundColor);
        
        branchGroup.addChild(firstGroup);
        branchGroup.addChild(lastGroup);
        
        return branchGroup;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene3D(double scale, Color3f backgroundColor) {
        int                 nComponents         = 3;
        double[]            coordinates;
        Appearance          appearance          = new Appearance();
        BranchGroup         branchGroup         = new BranchGroup();
        BranchGroup         firstGroup          =
                firstMeshPanel_.getScene3D(scale, backgroundColor);
        BranchGroup         lastGroup           =
                lastMeshPanel_.getScene3D(scale, backgroundColor);
        String              selectedColor       =
                (String) connectionColors_.getSelectedItem();
        ColoringAttributes  coloringAttributes;
        PointArray          pointArray;
        PointAttributes     pointAttributes;
        Shape3D             shape3D;
        Color3f             connectionColor;
        NodeMatrix          firstSelectedNodes;
        NodeMatrix          lastSelectedNodes;
        
        if (selectedColor.equals(ColorConstants.BLACK)) {
            connectionColor = ColorConstants.BLACK_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.BLUE)) {
            connectionColor = ColorConstants.BLUE_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.CYAN)) {
            connectionColor = ColorConstants.CYAN_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.GREEN)) {
            connectionColor = ColorConstants.GREEN_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.MAGENTA)) {
            connectionColor = ColorConstants.MAGENTA_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.RED)) {
            connectionColor = ColorConstants.RED_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.WHITE)) {
            connectionColor = ColorConstants.WHITE_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.YELLOW)) {
            connectionColor = ColorConstants.YELLOW_COLOR;
        }
        else {
            connectionColor = ColorConstants.WHITE_COLOR;
        }
        
        branchGroup.addChild(firstGroup);
        branchGroup.addChild(lastGroup);
        
        if (this.isConnected()) {
            coordinates         = new double[2 * nComponents *
                    nodeConnections_.getConnectionCount()];
            firstSelectedNodes  = new NodeMatrix(firstMeshPanel_.getNodes());
            lastSelectedNodes   = new NodeMatrix(lastMeshPanel_.getNodes());
            
            firstSelectedNodes.sortNodes(nodeConnections_.getFirstNodeKeys());
            lastSelectedNodes.sortNodes(nodeConnections_.getLastNodeKeys());
            
            for (int i = 0; i < nodeConnections_.getConnectionCount(); i++) {
                System.arraycopy(firstSelectedNodes.values(), i * nComponents,
                        coordinates, i * 2 * nComponents, nComponents);
                System.arraycopy(lastSelectedNodes.values(), i * nComponents,
                        coordinates, (i * 2 + 1) * nComponents, nComponents);
            }
            
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[i] *= scale;
            }
            
            pointArray = new PointArray(
                    2 * nodeConnections_.getConnectionCount(),
                    PointArray.COORDINATES);
            pointArray.setCoordinates(0, coordinates);
            
            coloringAttributes = new ColoringAttributes();
            coloringAttributes.setColor(connectionColor);
            coloringAttributes.setShadeModel(ColoringAttributes.FASTEST);
            appearance.setColoringAttributes(coloringAttributes);
            
            pointAttributes = new PointAttributes();
            pointAttributes.setPointSize(6);
            pointAttributes.setPointAntialiasingEnable(true);
            appearance.setPointAttributes(pointAttributes);
            
            shape3D = new Shape3D(pointArray, appearance);
            branchGroup.addChild(shape3D);
        }
        
        return branchGroup;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasNodeKeys() {
        boolean hasNodeKeys = false;
        
        if (firstMeshPanel_.hasNodeKeys()) {
            if (lastMeshPanel_.hasNodeKeys()) {
                hasNodeKeys = true;
            }
        }
        
        return hasNodeKeys;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasNodes() {
        boolean hasNodes = false;
        
        if (firstMeshPanel_.hasNodes()) {
            if (lastMeshPanel_.hasNodes()) {
                hasNodes = true;
            }
        }
        
        return hasNodes;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importNodeConnections(String fileName)
            throws  FileNotFoundException,
                    IOException {
        MeshConnectionReader    connectionReader;
        long                    time;
        
        nodeConnectionFile_ = new File(fileName.trim());
        nodeConnections_.clear();
        
        this.fireLogChanged("Opening source file: " + fileName);
        connectionReader = new ASCIIMeshConnectionReader(nodeConnectionFile_);
        connectionReader.readFile();
        
        time = System.currentTimeMillis();
        connectionReader.importNodeConnections(nodeConnections_);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Read: " + nodeConnections_.getConnectionCount() +
                " node connections (total time: " + time + " seconds)");
        
        this.fireScene3DChanged(this);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean isConnected() {
        return !nodeConnections_.isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean isConsistent() {
        boolean isConsistent = false;
        
        if (firstMeshPanel_.isConsistent()) {
            if (lastMeshPanel_.isConsistent()) {
                isConsistent = true;
            }
        }
        
        return isConsistent;
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
     * Returns a formatted string containing the data of the {@code First Mesh},
     * the {@code Last Mesh}, and the number of currently stored node
     * connections.
     * 
     * @return  the string representation of this {@code MeshConnectionModel}
     **************************************************************************/
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        
        result.append(String.format("%16s%n", firstMeshPanel_.getName()));
        result.append(String.format("%s%n", firstMeshPanel_.toString()));
        
        result.append(String.format("%16s%n", lastMeshPanel_.getName()));
        result.append(String.format("%s%n", lastMeshPanel_.toString()));
        
        result.append(String.format("%16s%n", "CONNECTIONS"));
        result.append(String.format("%-8s", "Nodes:"));
        result.append(String.format(" %7d",
                nodeConnections_.getConnectionCount()));
        result.append(String.format("%n"));
        
        return result.toString();
    } // eom
} // eoc