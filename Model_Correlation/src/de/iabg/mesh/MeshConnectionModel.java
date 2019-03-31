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

import de.iabg.swing.Loggable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ComboBoxModel;

/*******************************************************************************
 * This interface declares basic functionality for geometrically connecting two
 * Finite Element Model meshes.  This includes the ability to compute the nodal
 * geometric connection between two {@link JMeshPanel} objects, import the
 * connection data from different file types, and export the connection data
 * to different file types.  The only criterion that must be given is the
 * maximum tolerance between any two connected nodes.
 * 
 * This {@code MeshConnectionModel} should also be {@link Renderable}, meaning
 * that a Java3D scene should be created from the stored data.  Ideally, the
 * {@code First Mesh} and the {@code Last Mesh} should be rendered in the same
 * scene.  Then the connected nodal geometry should overlap both of the models,
 * essentially, highlighting them.
 * 
 * This {@code MeshConnectionModel} should also be {@link Loggable}, meaning
 * that for important user-controlled events, log messages should be fired so
 * that the user can have a command history and be able to view the current
 * status of the stored data.  There must be methods to access the data for
 * calculations or output.  Before output data can be made, queries must be
 * available to check if the data is already stored.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 12, 2008
 ******************************************************************************/
public interface MeshConnectionModel
        extends Loggable,
                Renderable {
    
    
    
    /***************************************************************************
     * Computes and stores the nodal connection between the {@code First Mesh}
     * and the {@code Last Mesh}.
     * 
     * @param   tolerance   the maximum tolerance between any two connected
     *                      nodes
     **************************************************************************/
    public void connectNodes(double tolerance);
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link MeshConnectionWriter} for the given file
     * and exports all stored node connections.
     * 
     * @param   fileName            the file to be exported
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public void exportNodeConnections(String fileName)
            throws IOException;
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.ComboBoxModel} of the connection colors
     * that can be rendered in Java3D.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} of the connection colors
     **************************************************************************/
    public ComboBoxModel getConnectionColorModel();
    
    
    
    /***************************************************************************
     * Returns the file where the connection data was last imported.
     * 
     * @return  the last imported connection file
     **************************************************************************/
    public File getConnectionFile();
    
    
    
    /***************************************************************************
     * Returns the {@code First Mesh}.
     * 
     * @return  the {@link JMeshPanel} of the {@code First Mesh}
     **************************************************************************/
    public JMeshPanel getFirstMesh();
    
    
    
    /***************************************************************************
     * Returns the {@code Last Mesh}.
     * 
     * @return  the {@link JMeshPanel} of the {@code Last Mesh}
     **************************************************************************/
    public JMeshPanel getLastMesh();
    
    
    
    /***************************************************************************
     * Returns the currently stored node connections.
     * 
     * @return  the {@link NodeConnectionMatrix} storing the node connections
     **************************************************************************/
    public NodeConnectionMatrix getNodeConnections();
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mesh} and the {@code Last Mesh} are both
     * storing any groups of node subsets.
     * 
     * @return  {@code true} if the {@code First Mesh} and the {@code Last Mesh}
     *          both store any groups of node subsets; {@code false} otherwise
     **************************************************************************/
    public boolean hasNodeKeys();
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mesh} and the {@code Last Mesh} are both
     * storing any nodes.
     * 
     * @return  {@code true} if the {@code First Mesh} and the {@code Last Mesh}
     *          both store any nodes; {@code false} otherwise
     **************************************************************************/
    public boolean hasNodes();
    
    
    
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
                    IOException;
    
    
    
    /***************************************************************************
     * Tests if this {@code MeshConnectionModel} is storing any connections
     * between the {@code First Mesh} and the {@code Last Mesh}.
     * 
     * @return  {@code true} if this {@code MeshConnectionModel} stores any node
     *          connections; {@code false} otherwise
     **************************************************************************/
    public boolean isConnected();
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mesh} and {@code Last Mesh} are both
     * consistent.
     * 
     * @return  {@code true} if the {@code First Mesh} and {@code Last Mesh} are
     *          both consistent; {@code false} otherwise
     **************************************************************************/
    public boolean isConsistent();
} // eoi