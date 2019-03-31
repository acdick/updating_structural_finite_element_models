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

import de.iabg.mesh.io.NastranBulkDataReader;
import de.iabg.mesh.io.NastranSetReader;

import de.iabg.swing.KeyList;
import de.iabg.swing.KeyMap;

import de.iabg.swing.event.LogEvent;
import de.iabg.swing.event.LogListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Arrays;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.LineArray;
import javax.media.j3d.LineAttributes;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TriangleArray;

import javax.swing.ComboBoxModel;

import javax.swing.event.EventListenerList;

import javax.vecmath.Color3f;

/*******************************************************************************
 * This implementation of {@link MeshModel} attempts to create all the
 * functionality described in the {@code MeshModel} class description.  Refer
 * to the class API for more details.
 * 
 * Much of the implementation of this class should be straight-forward, however,
 * a more in-depth summary of the data structure may clarify some conceptual
 * issues.  A {@link NodeConnectionMatrix} is a {@code DiagonalMatrix} which
 * stores the identifiers of the {@code First Nodes} as the rows and the
 * identifiers of the {@code Last Nodes} as the columns.  It can also store a
 * correlation value along the diagonal, but for the purposes of this class, it
 * is enough to consider that there is essentially a mapping between the
 * {@code First Nodes} and {@code Last Nodes}.
 * 
 * Node connection data, such as an edge, has a {@code First Node} and a
 * {@code Last Node}, which can be added to such {@code NodeConnectionMatrix}
 * for as many edges as required.  A triangle has three nodes and three edges,
 * and these three edges can be added in exactly the same way.  Similarly,
 * the four edges of a quadrilateral can be added.
 * 
 * Java3D can read in geometry as arrays of coordinates as can be read in its
 * API and documentation.  To render a {@code NodeConnectionMatrix}, we can
 * copy the {@code NodeMatrix} and sort its nodes using the ordering of the
 * {@code First Nodes} of the {@code NodeConnectionMatrix}.  The values of the
 * {@code NodeMatrix} can then assigned in Java3D.  This works well for nodes,
 * triangles, and quadrilaterals.  For edges, the ordering of the
 * {@code Last Nodes} is also required, so another copy of the
 * {@code NodeMatrix} using this ordering must be merged into that of the
 * {@code First Nodes}.  Triangles and quadrilaterals can then be visualized as
 * wire frame lines or filled solids by changing the appropriate polygon
 * attributes.
 * 
 * @author  Adam C. Dick, BSE
 * @version November 1, 2008
 ******************************************************************************/
public class DefaultMeshModel
        implements MeshModel {
    /** Storage for the edges */
    protected NodeConnectionMatrix edges_;
    
    /** A {@link javax.swing.ComboBoxModel} for the geometry color */
    protected KeyList geometryColors_;
    
    /** The file of the currently stored geometry */
    protected File geometryFile_;
    
    /** A list for event listeners */
    protected EventListenerList listenerList_;
    
    /** The {@link JMeshPanel} that this model is designed for */
    protected JMeshPanel meshPanel_;
    
    /** The file of the currently stored node subset groups */
    protected File nodeKeyFile_;
    
    /** A {@link javax.swing.ComboBoxModel} for the node subset groups */
    protected KeyMap nodeKeyLists_;
    
    /** Storage for the nodes */
    protected NodeMatrix nodes_;
    
    /** Storage for the quadrilaterals */
    protected NodeConnectionMatrix quads_;
    
    /** Storage for the triangles */
    protected NodeConnectionMatrix triangles_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code DefaultMeshModel} with no stored data.  This
     * constructor also initializes all the storage objects, which should not
     * be destroyed during the life of this object.  This is because listeners
     * may register themselves to these objects, and will not receive
     * notifications if the references to these objects are changed.  This
     * constructor also stores a reference to the {@link JMeshPanel}, so that
     * this model has access to all the methods of that component.
     * 
     * @param   meshPanel   the {@code JMeshPanel} that this model is designed
     *                      for
     **************************************************************************/
    public DefaultMeshModel(JMeshPanel meshPanel) {
        meshPanel_      = meshPanel;
        listenerList_   = new EventListenerList();
        nodes_          = new NodeMatrix();
        edges_          = new NodeConnectionMatrix();
        triangles_      = new NodeConnectionMatrix();
        quads_          = new NodeConnectionMatrix();
        nodeKeyLists_   = new KeyMap();
        geometryColors_ = new KeyList();
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
        return nodes_.getBoundingRadius2D();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getBoundingRadius3D() {
        return nodes_.getBoundingRadius3D();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public NodeConnectionMatrix getEdges() {
        return edges_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public ComboBoxModel getGeometryColorModel() {
        geometryColors_.clear();
        geometryColors_.add(ColorConstants.BLACK);
        geometryColors_.add(ColorConstants.BLUE);
        geometryColors_.add(ColorConstants.CYAN);
        geometryColors_.add(ColorConstants.GREEN);
        geometryColors_.add(ColorConstants.MAGENTA);
        geometryColors_.add(ColorConstants.RED);
        geometryColors_.add(ColorConstants.WHITE);
        geometryColors_.add(ColorConstants.YELLOW);
        geometryColors_.setSelectedItem(ColorConstants.WHITE);
        
        return geometryColors_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public File getGeometryFile() {
        return geometryFile_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public ComboBoxModel getNodeKeyModel() {
        return nodeKeyLists_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public NodeMatrix getNodes() {
        return nodes_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public NodeConnectionMatrix getQuadrilaterals() {
        return quads_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene2D(double scale, Color3f backgroundColor) {
        String  selectedColor   = (String) geometryColors_.getSelectedItem();
        Color3f geometryColor;
        
        if (selectedColor.equals(ColorConstants.BLACK)) {
            geometryColor = ColorConstants.BLACK_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.BLUE)) {
            geometryColor = ColorConstants.BLUE_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.CYAN)) {
            geometryColor = ColorConstants.CYAN_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.GREEN)) {
            geometryColor = ColorConstants.GREEN_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.MAGENTA)) {
            geometryColor = ColorConstants.MAGENTA_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.RED)) {
            geometryColor = ColorConstants.RED_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.WHITE)) {
            geometryColor = ColorConstants.WHITE_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.YELLOW)) {
            geometryColor = ColorConstants.YELLOW_COLOR;
        }
        else {
            geometryColor = ColorConstants.WHITE_COLOR;
        }
        
        nodes_.setNodeColor(geometryColor);
        
        return nodes_.getScene2D(scale, backgroundColor);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene3D(double scale, Color3f backgroundColor) {
        int                 nComponents         = 3;
        double[]            coordinates;
        Appearance          appearance          = new Appearance();
        BranchGroup         branchGroup         = new BranchGroup();
        ColoringAttributes  coloringAttributes;
        LineArray           lineArray;
        LineAttributes      lineAttributes;
        PolygonAttributes   polygonAttributes;
        QuadArray           quadArray;
        Shape3D             shape3D;
        TriangleArray       triangleArray;
        String              selectedColor       =
                (String) geometryColors_.getSelectedItem();
        Color3f             geometryColor;
        NodeMatrix          firstNodes;
        NodeMatrix          lastNodes;
        
        if (selectedColor.equals(ColorConstants.BLACK)) {
            geometryColor = ColorConstants.BLACK_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.BLUE)) {
            geometryColor = ColorConstants.BLUE_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.CYAN)) {
            geometryColor = ColorConstants.CYAN_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.GREEN)) {
            geometryColor = ColorConstants.GREEN_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.MAGENTA)) {
            geometryColor = ColorConstants.MAGENTA_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.RED)) {
            geometryColor = ColorConstants.RED_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.WHITE)) {
            geometryColor = ColorConstants.WHITE_COLOR;
        }
        else if (selectedColor.equals(ColorConstants.YELLOW)) {
            geometryColor = ColorConstants.YELLOW_COLOR;
        }
        else {
            geometryColor = ColorConstants.WHITE_COLOR;
        }
        
        nodes_.setNodeColor(geometryColor);
        branchGroup.addChild(nodes_.getScene3D(scale, backgroundColor));
        
        if (this.hasEdges()) {
            coordinates =
                    new double[2 * nComponents * edges_.getConnectionCount()];
            firstNodes  = new NodeMatrix(nodes_);
            lastNodes   = new NodeMatrix(nodes_);
            
            firstNodes.sortNodes(edges_.getFirstNodeKeys());
            lastNodes.sortNodes(edges_.getLastNodeKeys());
            
            for (int i = 0; i < edges_.getConnectionCount(); i++) {
                System.arraycopy(firstNodes.values(), i * nComponents,
                        coordinates, i * 2 * nComponents, nComponents);
                System.arraycopy(lastNodes.values(), i * nComponents,
                        coordinates, (i * 2 + 1) * nComponents, nComponents);
            }
            
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[i] *= scale;
            }
            
            lineArray = new LineArray(2 * edges_.getConnectionCount(),
                    LineArray.COORDINATES);
            lineArray.setCoordinates(0, coordinates);
            
            coloringAttributes = new ColoringAttributes();
            coloringAttributes.setColor(geometryColor);
            coloringAttributes.setShadeModel(ColoringAttributes.FASTEST);
            appearance.setColoringAttributes(coloringAttributes);
            
            lineAttributes = new LineAttributes();
            lineAttributes.setLineWidth(1);
            lineAttributes.setLineAntialiasingEnable(true);
            appearance.setLineAttributes(lineAttributes);
            
            shape3D = new Shape3D(lineArray, appearance);
            branchGroup.addChild(shape3D);
        }
        
        if (this.hasTriangles()) {
            coordinates =
                    new double[nComponents * triangles_.getConnectionCount()];
            firstNodes  = new NodeMatrix(nodes_);
            firstNodes.sortNodes(triangles_.getFirstNodeKeys());
            System.arraycopy(firstNodes.values(), 0, coordinates, 0,
                    firstNodes.values().length);
            
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[i] *= scale;
            }
            
            triangleArray = new TriangleArray(triangles_.getConnectionCount(),
                    TriangleArray.COORDINATES);
            triangleArray.setCoordinates(0, coordinates);
            
            coloringAttributes = new ColoringAttributes();
            coloringAttributes.setColor(geometryColor);
            coloringAttributes.setShadeModel(ColoringAttributes.FASTEST);
            appearance.setColoringAttributes(coloringAttributes);
            
            lineAttributes = new LineAttributes();
            lineAttributes.setLineWidth(1);
            lineAttributes.setLineAntialiasingEnable(true);
            appearance.setLineAttributes(lineAttributes);
            
            polygonAttributes = new PolygonAttributes();
            polygonAttributes.setBackFaceNormalFlip(true);
            polygonAttributes.setCullFace(PolygonAttributes.CULL_NONE);
            polygonAttributes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
            appearance.setPolygonAttributes(polygonAttributes);
            
            shape3D = new Shape3D(triangleArray, appearance);
            branchGroup.addChild(shape3D);
        }
        
        if (this.hasQuadrilaterals()) {
            coordinates =
                    new double[nComponents * quads_.getConnectionCount()];
            firstNodes  = new NodeMatrix(nodes_);
            firstNodes.sortNodes(quads_.getFirstNodeKeys());
            System.arraycopy(firstNodes.values(), 0, coordinates, 0,
                    firstNodes.values().length);
            
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[i] *= scale;
            }
            
            quadArray = new QuadArray(quads_.getConnectionCount(),
                    QuadArray.COORDINATES);
            quadArray.setCoordinates(0, coordinates);
            
            coloringAttributes = new ColoringAttributes();
            coloringAttributes.setColor(geometryColor);
            coloringAttributes.setShadeModel(ColoringAttributes.FASTEST);
            appearance.setColoringAttributes(coloringAttributes);
            
            lineAttributes = new LineAttributes();
            lineAttributes.setLineWidth(1);
            lineAttributes.setLineAntialiasingEnable(true);
            appearance.setLineAttributes(lineAttributes);
            
            polygonAttributes = new PolygonAttributes();
            polygonAttributes.setBackFaceNormalFlip(true);
            polygonAttributes.setCullFace(PolygonAttributes.CULL_NONE);
            polygonAttributes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
            appearance.setPolygonAttributes(polygonAttributes);
            
            shape3D = new Shape3D(quadArray, appearance);
            branchGroup.addChild(shape3D);
        }
        
        return branchGroup;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public KeyList getSelectedNodeKeys() {
        return nodeKeyLists_.getSelectedList();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public NodeConnectionMatrix getTriangles() {
        return triangles_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasEdges() {
        return !edges_.isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasNodeKeys() {
        boolean hasSelectedNodes = true;
        
        if (nodeKeyLists_.getSelectedList() == null) {
            hasSelectedNodes = false;
        }
        
        return hasSelectedNodes;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasNodes() {
        return !nodes_.isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasQuadrilaterals() {
        return !quads_.isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasTriangles() {
        return !triangles_.isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importGeometry(String fileName)
            throws  FileNotFoundException,
                    IOException {
        GeometryReader  geometryReader;
        long            time;
        
        geometryFile_ = new File(fileName.trim());
        nodes_.clear();
        edges_.clear();
        triangles_.clear();
        quads_.clear();
        
        this.fireLogChanged("Opening source file: " + fileName);
        geometryReader = new NastranBulkDataReader(geometryFile_);
        geometryReader.readFile();
        
        time = System.currentTimeMillis();
        geometryReader.importNodes(nodes_);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Read: " + nodes_.getNodeCount() +
                " nodes (total time: " + time + " seconds)");
        
        time = System.currentTimeMillis();
        geometryReader.importEdges(edges_);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Read: " + edges_.getConnectionCount() +
                " edges (total time: " + time + " seconds)");
        
        time = System.currentTimeMillis();
        geometryReader.importTriangles(triangles_);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Read: " + triangles_.getConnectionCount() / 3 +
                " triangles (total time: " + time + " seconds)");
        
        time = System.currentTimeMillis();
        geometryReader.importQuadrilaterals(quads_);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Read: " + quads_.getConnectionCount() / 4 +
                " quadrilaterals (total time: " + time + " seconds)");
        
        this.fireScene3DChanged(this);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importNodeKeyLists(String fileName)
            throws  FileNotFoundException,
                    IOException {
        long time;
        
        nodeKeyFile_ = new File(fileName.trim());
        nodeKeyLists_.clear();
        
        this.fireLogChanged("Opening source file: " + fileName);
        KeyReader keyReader = new NastranSetReader(nodeKeyFile_);
        keyReader.readFile();
        
        time = System.currentTimeMillis();
        keyReader.importNodeKeyLists(nodeKeyLists_);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Read: " + nodeKeyLists_.getSize() +
                " node key lists (total time: " + time + " seconds)");
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean isConsistent() {
        boolean isConsistent = true;
        
        nodeCheck:
            if (!nodes_.isEmpty()) {
                for (KeyList nodeKeys : nodeKeyLists_.values()) {
                    if (!nodes_.containsAll(nodeKeys)) {
                        isConsistent = false;
                        break nodeCheck;
                    }
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
     **************************************************************************/
    public void setDefaultKeys() {
        KeyList nodeKeys;
        String  key;
        
        if (!nodes_.isEmpty()) {
            key = new String("All nodes");
            
            nodeKeys = new KeyList();
            nodeKeys.addAll(Arrays.asList(nodes_.getNodeNames()));
            
            nodeKeyLists_.put(key, nodeKeys);
            this.fireLogChanged("Set: " + nodes_.getNodeCount() +
                    " default node keys");
        }
    } // eom
    
    
    
    /***************************************************************************
     * Returns a formatted string containing the number of currently stored
     * nodes, edges, triangles, quadrilaterals, and node set groups.
     * 
     * @return  the string representation of this {@code MeshModel}
     **************************************************************************/
    @Override
    public String toString() {
        StringBuilder   result  = new StringBuilder();
        int             setSize = 0;
        
        result.append(String.format("%16s", "MESH"));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Nodes:"));
        result.append(String.format(" %7d", nodes_.getNodeCount()));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Edges:"));
        result.append(String.format(" %7d", edges_.getConnectionCount()));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Tris:"));
        result.append(String.format(" %7d",
                triangles_.getConnectionCount() / 3));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Quads:"));
        result.append(String.format(" %7d", quads_.getConnectionCount() / 4));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Sets:"));
        result.append(String.format(" %7d", nodeKeyLists_.getSize()));
        result.append(String.format("%n"));
        
        if (nodeKeyLists_.getSelectedList() != null) {
            setSize = nodeKeyLists_.getSelectedList().getSize();
        }
        
        result.append(String.format("%-8s", "SetSize:"));
        result.append(String.format(" %7d", setSize));
        result.append(String.format("%n"));
        
        return result.toString();
    } // eom
} // eoc