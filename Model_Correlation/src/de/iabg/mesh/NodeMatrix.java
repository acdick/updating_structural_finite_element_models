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

import de.iabg.math.RectangularMatrix;

import de.iabg.swing.KeyList;

import java.util.Arrays;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.PointArray;
import javax.media.j3d.PointAttributes;
import javax.media.j3d.Shape3D;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/*******************************************************************************
 * This {@code Matrix} is designed to store the nodal coordinates of a given
 * geometry.  This matrix has several convenience methods that wrap methods from
 * {@code RectangularMatrix}.  The nodes are stored as rows and the x, y, z
 * coordinates are stored as fixed columns.
 * 
 * This matrix also has the ability to correlate and connect itself with another
 * {@code NodeMatrix}.  A correlation compares all nodes of this matrix
 * with all nodes of another matrix, while a connection specifies a mapping
 * between nodes from this matrix and another matrix.  A connection can also be
 * defined between different nodes of the same matrix, such as edges, triangle
 * connections and quadrilateral connections.
 * 
 * The point cloud that this matrix represents can also be rendered in Java3D
 * by calling {@link #getScene2D(double, javax.vecmath.Color3f)} or
 * {@link #getScene3D(double, javax.vecmath.Color3f)}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class NodeMatrix extends RectangularMatrix {
    /** The number of components in 3D space */
    protected static final int N_COMPONENTS = 3;
    
    /** The color of the nodes */
    protected Color3f nodeColor_;
    
    /** The index of the x-coordinate */
    protected static final int X_INDEX = 0;
    
    /** The index of the y-coordinate */
    protected static final int Y_INDEX = 1;
    
    /** The index of the z-coordinate */
    protected static final int Z_INDEX = 2;
    
    
    
    /***************************************************************************
     * Constructs a default {@code NodeMatrix} with no nodes.
     **************************************************************************/
    public NodeMatrix() {
        this(0);
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a {@code NodeMatrix} containing the given number of nodes.  It
     * also sets the default node color to white.
     * 
     * @param   nNodes  the number of nodes in this matrix
     **************************************************************************/
    public NodeMatrix(int nNodes) {
        super(nNodes, N_COMPONENTS);
        
        this.setColumnNameAt("X-Coordinate", X_INDEX);
        this.setColumnNameAt("Y-Coordinate", Y_INDEX);
        this.setColumnNameAt("Z-Coordinate", Z_INDEX);
        this.setNodeColor(ColorConstants.WHITE_COLOR);
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a copy of the given {@code NodeMatrix}.
     * 
     * @param   nodes   the {@code NodeMatrix} to be copied
     **************************************************************************/
    public NodeMatrix(NodeMatrix nodes) {
        this(nodes.nRows_);
        
        System.arraycopy(nodes.rowNames_, 0, rowNames_, 0, nRows_);
        System.arraycopy(nodes.columnNames_, 0, columnNames_, 0, nColumns_);
        System.arraycopy(nodes.values_, 0, values_, 0, nRows_ * nColumns_);
    } // eom
    
    
    
    /***************************************************************************
     * Removes the data from this matrix.  This method wraps
     * {@link #setNodeCount(int)} and sets the number of nodes to zero.  This
     * method overrides the existing implementation so that the number of
     * columns (coordinates) remains constant.
     **************************************************************************/
    @Override
    public void clear() {
        this.setNodeCount(0);
    } // eom
    
    
    
    /***************************************************************************
     * Computes a matrix of the distances between each node in this
     * {@code NodeMatrix} and the given {@code NodeMatrix}.  The nodes of this
     * matrix are the rows of the returned {@link NodeCorrelationMatrix}, while
     * the nodes of the given matrix are the columns.
     * 
     * @param   nodes   the {@code NodeMatrix} that will be correlated to this
     *                  {@code NodeMatrix}
     * @return  the matrix of the distance correlation
     **************************************************************************/
    public NodeCorrelationMatrix computeDistanceCorrelation(NodeMatrix nodes) {
        NodeCorrelationMatrix   correlation;
        double                  distanceSquared;
        double                  dComponent;
        int                     nFirstNodes         = nRows_;
        int                     nLastNodes          = nodes.nRows_;
        
        correlation = new NodeCorrelationMatrix(nFirstNodes, nLastNodes);
        
        for (int i = 0; i < nFirstNodes; i++) {
            if (rowNames_[i] != null) {
                correlation.setFirstNodeNameAt(rowNames_[i], i);
            }
        }
        
        for (int i = 0; i < nLastNodes; i++) {
            if (nodes.rowNames_[i] != null) {
                correlation.setLastNodeNameAt(nodes.rowNames_[i], i);
            }
        }
        
        for (int i = 0; i < nFirstNodes; i++) {
            for (int j = 0; j < nLastNodes; j++) {
                distanceSquared = 0.0;
                
                for (int k = 0; k < N_COMPONENTS; k++) {
                    dComponent      =  values_[(i * nColumns_) + k];
                    dComponent      -= nodes.values_[(j * nColumns_) + k];
                    dComponent      *= dComponent;
                    distanceSquared += dComponent;
                }
                
                correlation.setCorrelationAt(Math.sqrt(distanceSquared), i, j);
            }
        }
        
        return correlation;
    } // eom
    
    
    
    /***************************************************************************
     * Returns {@code true} if this matrix contains all of the node names
     * specified in the given subset.
     * 
     * @param   nodeKeys    the node names to be checked
     * @return  {@code true} if this matrix contains all of the nodes names;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean containsAll(KeyList nodeKeys) {
        return Arrays.asList(rowNames_).containsAll(nodeKeys.values());
    } // eom
    
    
    
    /***************************************************************************
     * Returns the 2D distance of the furthest node from the origin.  The 2D
     * distance is calculated from only the x and y coordinates.
     * 
     * @return  the 2D distance of the furthest node
     **************************************************************************/
    public double getBoundingRadius2D() {
        double boundingRadius = 0.0;
        double radius;
        double dx;
        double dy;
        
        for (int i = 0; i < nRows_; i++) {
            dx = values_[(i * N_COMPONENTS) + X_INDEX];
            dy = values_[(i * N_COMPONENTS) + Y_INDEX];
            
            radius          = Math.sqrt((dx * dx) + (dy * dy));
            boundingRadius  = Math.max(boundingRadius, radius);
        }
        
        return boundingRadius;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the 3D distance of the furthest node from the origin.
     * 
     * @return  the 3D distance of the furthest node
     **************************************************************************/
    public double getBoundingRadius3D() {
        double boundingRadius = 0.0;
        double radius;
        double dx;
        double dy;
        double dz;
        
        for (int i = 0; i < nRows_; i++) {
            dx = values_[(i * N_COMPONENTS) + X_INDEX];
            dy = values_[(i * N_COMPONENTS) + Y_INDEX];
            dz = values_[(i * N_COMPONENTS) + Z_INDEX];
            
            radius          = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
            boundingRadius  = Math.max(boundingRadius, radius);
        }
        
        return boundingRadius;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getRowCount()}.
     * 
     * @return  the number of nodes
     **************************************************************************/
    public int getNodeCount() {
        return this.getRowCount();
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getRowNames()}.
     * 
     * @return  a string array containing all node names
     **************************************************************************/
    public String[] getNodeNames() {
        return this.getRowNames();
    } // eom
    
    
    
    /***************************************************************************
     * Makes a copy of this {@code NodeMatrix} and the given one, sorts each by
     * the corresponding given node names, and computes the resulting
     * {@link NodeCorrelationMatrix}.
     * 
     * @param   nodes           the {@code NodeMatrix} that will be correlated
     *                          to this {@code NodeMatrix}
     * @param   firstNodeKeys   the new order of the nodes of this matrix
     * @param   lastNodeKeys    the new order of the nodes of the given matrix 
     * @return  the matrix of the distance correlation
     **************************************************************************/
    public NodeCorrelationMatrix getReducedDistanceCorrelation(NodeMatrix nodes,
            KeyList firstNodeKeys, KeyList lastNodeKeys) {
        NodeMatrix firstNodes   = new NodeMatrix(this);
        NodeMatrix lastNodes    = new NodeMatrix(nodes);
        
        firstNodes.sortNodes(firstNodeKeys);
        lastNodes.sortNodes(lastNodeKeys);
        
        return firstNodes.computeDistanceCorrelation(lastNodes);
    } // eom
    
    
    
    /***************************************************************************
     * This method has not been implemented.
     * 
     * @param   scale           the scale of the scene
     * @param   backgroundColor the background color of the scene
     * @return  a new branch group
     **************************************************************************/
    public BranchGroup getScene2D(double scale, Color3f backgroundColor) {
        return new BranchGroup();
    } // eom
    
    
    
    /***************************************************************************
     * Creates a Java3D scene of the point cloud that this {@code NodeMatrix}
     * represents at the given scale and with the given background color.  If
     * there are no nodes present, only the background is created.  The nodes
     * are rendered with the current node color and can be changed beforehand by
     * calling {@link #setNodeColor(javax.vecmath.Color3f)}.
     * 
     * @param   scale           the scale of the scene
     * @param   backgroundColor the background color of the scene
     * @return  a branch group containing the background and point cloud that
     *          this matrix represents
     **************************************************************************/
    public BranchGroup getScene3D(double scale, Color3f backgroundColor) {
        double[]            coordinates         = new double[values_.length];
        Appearance          appearance          = new Appearance();
        Background          background;
        Bounds              bounds;
        BranchGroup         branchGroup         = new BranchGroup();
        ColoringAttributes  coloringAttributes;
        PointArray          pointArray;
        PointAttributes     pointAttributes;
        Shape3D             shape3D;
        
        bounds      = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        background  = new Background(backgroundColor);
        background.setApplicationBounds(bounds);
        branchGroup.addChild(background);
        
        if (!this.isEmpty()) {
            System.arraycopy(values_, 0, coordinates, 0, values_.length);
            
            for (int i = 0; i < coordinates.length; i++) {
                coordinates[i] *= scale;
            }
            
            pointArray = new PointArray(nRows_, PointArray.COORDINATES);
            pointArray.setCoordinates(0, coordinates);
            
            coloringAttributes = new ColoringAttributes();
            coloringAttributes.setColor(nodeColor_);
            coloringAttributes.setShadeModel(ColoringAttributes.FASTEST);
            appearance.setColoringAttributes(coloringAttributes);
            
            pointAttributes = new PointAttributes();
            pointAttributes.setPointSize(3);
            pointAttributes.setPointAntialiasingEnable(true);
            appearance.setPointAttributes(pointAttributes);
            
            shape3D = new Shape3D(pointArray, appearance);
            branchGroup.addChild(shape3D);
        }
        
        return branchGroup;
    } // eom
    
    
    
    /***************************************************************************
     * Tests to see if this {@code NodeMatrix} has no data.  This method
     * overrides the previous implementation by only checking that the are no
     * nodes.
     **************************************************************************/
    @Override
    public boolean isEmpty() {
        boolean isEmpty = false;
        
        if(nRows_ == 0) {
            isEmpty = true;
        }
        
        return isEmpty;
    } // eom
    
    
    
    /***************************************************************************
     * This method does nothing and overrides the previous implementation
     * because the number of columns (coordinates) should not change.
     **************************************************************************/
    @Override
    public void setColumnCount(int nColumns) {
        
    } // eom
    
    
    
    /***************************************************************************
     * Replaces the color with which the nodes will be rendered in
     * {@link #getScene2D(double, javax.vecmath.Color3f)} or
     * {@link #getScene3D(double, javax.vecmath.Color3f)}.
     * 
     * @param   nodeColor   the new node color
     **************************************************************************/
    public void setNodeColor(Color3f nodeColor) {
        nodeColor_ = nodeColor;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #setRowCount(int)}.
     * 
     * @param   nNodes  the new number of nodes
     **************************************************************************/
    public void setNodeCount(int nNodes) {
        this.setRowCount(nNodes);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #setRowNameAt(java.lang.String, int)}.
     * 
     * @param   nodeName    the new name of the node
     * @param   nodeIndex   the index of the node to be changed
     **************************************************************************/
    public void setNodeNameAt(String nodeName, int nodeIndex) {
        this.setRowNameAt(nodeName, nodeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given x-component value at the given
     * node index.
     * 
     * @param   value       the new value of the x-component
     * @param   nodeIndex   the index of the node to be changed
     **************************************************************************/
    public void setXCoordinateAt(double value, int nodeIndex) {
        this.setValueAt(value, nodeIndex, X_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given y-component value at the given
     * node index.
     * 
     * @param   value       the new value of the y-component
     * @param   nodeIndex   the index of the node to be changed
     **************************************************************************/
    public void setYCoordinateAt(double value, int nodeIndex) {
        this.setValueAt(value, nodeIndex, Y_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given z-component value at the given
     * node index.
     * 
     * @param   value       the new value of the z-component
     * @param   nodeIndex   the index of the node to be changed
     **************************************************************************/
    public void setZCoordinateAt(double value, int nodeIndex) {
        this.setValueAt(value, nodeIndex, Z_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * This method does nothing and overrides the previous implementation
     * because the columns (coordinates) should not sorted.
     **************************************************************************/
    @Override
    public void sortColumns(KeyList columnKeys) {
        
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #sortRows(de.iabg.swing.KeyList)}.
     * 
     * @param   nodeKeys    the new order of the nodes
     **************************************************************************/
    public void sortNodes(KeyList nodeKeys) {
        this.sortRows(nodeKeys);
    } // eom
} // eoc