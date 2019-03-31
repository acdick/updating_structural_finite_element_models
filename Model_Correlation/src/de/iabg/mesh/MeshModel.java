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

import de.iabg.swing.KeyList;
import de.iabg.swing.Loggable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ComboBoxModel;

/*******************************************************************************
 * This interface declares basic functionality for storing data for a Finite
 * Element Model mesh.  This includes the ability to read in the geometric data
 * from different file types and obtain the nodes and their connections.  These
 * connections are generally edges, triangles, and quadrilaterals.  They differ
 * only in the amount of connections per element, where edges have one
 * connection, triangles have three and quadrilaterals have four.  This means
 * that they can all be stored in the same basic structure, such as a
 * {@link NodeConnectionMatrix} and will only differ in size.  For instance, one
 * quadrilateral will be four times as large as one edge.  The user should be
 * able to select subsets of nodes for later calculations.  Thus, there should
 * also be methods to set default node sets when geometry is read in.
 * 
 * This {@code MeshModel} should also be {@link Renderable}, meaning that a
 * Java3D scene should be created from the stored data.  Inherently, node
 * connections do not contain coordinate data, which is generally only stored
 * in the nodes.  Therefore, the {@code MeshModel} must be able to create the
 * correct line and polygon coordinate geometry for the Java3D renderer.  In
 * order to assure that the nodes, node connections, and node subsets contain
 * data for the same geometry, a consistency check must be available.  There
 * should also be methods to set the color properties of the geometry during
 * runtime.
 * 
 * This {@code MeshModel} should also be {@link Loggable}, meaning that for
 * important user-controlled events, log messages should be fired so that the
 * user can have a command history and be able to view the current status of the
 * stored data.  There must be methods to access the data for calculations or
 * output.  Before output data can be made, queries must be available to check
 * if the data is already stored.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public interface MeshModel
        extends Loggable,
                Renderable {
    
    
    
    /***************************************************************************
     * Returns the currently stored edges.
     * 
     * @return  the {@link NodeConnectionMatrix} storing the edges
     **************************************************************************/
    public NodeConnectionMatrix getEdges();
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.ComboBoxModel} of the geometry colors that
     * can be rendered in Java3D.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} of the geometry colors
     **************************************************************************/
    public ComboBoxModel getGeometryColorModel();
    
    
    
    /***************************************************************************
     * Returns the file where the geometric data was last imported.
     * 
     * @return  the last imported geometry file
     **************************************************************************/
    public File getGeometryFile();
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.ComboBoxModel} of the groups of node
     * subsets.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} of the node key groups
     **************************************************************************/
    public ComboBoxModel getNodeKeyModel();
    
    
    
    /***************************************************************************
     * Returns the currently stored nodes.
     * 
     * @return  the {@link NodeMatrix} storing the nodes
     **************************************************************************/
    public NodeMatrix getNodes();
    
    
    
    /***************************************************************************
     * Returns the currently stored quadrilaterals.
     * 
     * @return  the {@link NodeConnectionMatrix} storing the quadrilaterals
     **************************************************************************/
    public NodeConnectionMatrix getQuadrilaterals();
    
    
    
    /***************************************************************************
     * Returns the {@link KeyList} of the currently selected node subset group.
     * 
     * @return  the {@link KeyList} of the currently selected node keys
     **************************************************************************/
    public KeyList getSelectedNodeKeys();
    
    
    
    /***************************************************************************
     * Returns the currently stored triangles.
     * 
     * @return  the {@link NodeConnectionMatrix} storing the triangles
     **************************************************************************/
    public NodeConnectionMatrix getTriangles();
    
    
    
    /***************************************************************************
     * Tests if this {@code MeshModel} is storing any edges.
     * 
     * @return  {@code true} if this {@code MeshModel} stores any edges;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasEdges();
    
    
    
    /***************************************************************************
     * Tests if this {@code MeshModel} is storing any node subsets.
     * 
     * @return  {@code true} if this {@code MeshModel} stores any node subsets;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasNodeKeys();
    
    
    
    /***************************************************************************
     * Tests if this {@code MeshModel} is storing any nodes.
     * 
     * @return  {@code true} if this {@code MeshModel} stores any nodes;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasNodes();
    
    
    
    /***************************************************************************
     * Tests if this {@code MeshModel} is storing any quadrilaterals.
     * 
     * @return  {@code true} if this {@code MeshModel} stores any
     *          quadrilaterals; {@code false} otherwise
     **************************************************************************/
    public boolean hasQuadrilaterals();
    
    
    
    /***************************************************************************
     * Tests if this {@code MeshModel} is storing any triangles.
     * 
     * @return  {@code true} if this {@code MeshModel} stores any triangles;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasTriangles();
    
    
    
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
                    IOException;
    
    
    
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
                    IOException;
    
    
    
    /***************************************************************************
     * Tests if all the node keys of this {@code MeshModel} are contained in the
     * stored nodes.  If there are no stored nodes or no stored node keys, this
     * method should return {@code true}.
     * 
     * @return  {@code true} if all node keys are contains in the nodes, if
     *          there are no stored nodes, or if there are no stored node key;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean isConsistent();
    
    
    
    /***************************************************************************
     * Creates a default node subset group called {@code All Nodes}, which is
     * inserted to the node key model.  This group should contain all of the
     * currently stored nodes.
     **************************************************************************/
    public void setDefaultKeys();
} // eoi