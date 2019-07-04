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

import java.io.IOException;

/*******************************************************************************
 * This interface declares basic functionality for parsing a file containing
 * geometric data, namely nodes, edges, triangles, and quadrilaterals.  In
 * general, the user should call {@link #readFile()} so that the parser reads
 * and internally stores the data of the file.  The data can then be safely
 * parsed and extracted without having to stream file data multiple times.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public interface GeometryReader {
    
    
    
    /***************************************************************************
     * Clears the given edges and replaces them with the data parsed from the
     * internally stored file.  This method should be called after
     * {@link #readFile()}, otherwise no data will be saved.
     * 
     * @param   edges   the {@link NodeConnectionMatrix} where the data will be
     *                  stored
     **************************************************************************/
    public void importEdges(NodeConnectionMatrix edges);
    
    
    
    /***************************************************************************
     * Clears the given nodes and replaces them with the data parsed from the
     * internally stored file.  This method should be called after
     * {@link #readFile()}, otherwise no data will be saved.
     * 
     * @param   nodes   the {@link NodeMatrix} where the data will be stored
     **************************************************************************/
    public void importNodes(NodeMatrix nodes);
    
    
    
    /***************************************************************************
     * Clears the given quadrilaterals and replaces them with the data parsed
     * from the internally stored file.  This method should be called after
     * {@link #readFile()}, otherwise no data will be saved.
     * 
     * @param   quads   the {@link NodeConnectionMatrix} where the data will be
     *                  stored
     **************************************************************************/
    public void importQuadrilaterals(NodeConnectionMatrix quads);
    
    
    
    /***************************************************************************
     * Clears the given triangles and replaces them with the data parsed from
     * the internally stored file.  This method should be called after
     * {@link #readFile()}, otherwise no data will be saved.
     * 
     * @param   triangles   the {@link NodeConnectionMatrix} where the data will
     *                      be stored
     **************************************************************************/
    public void importTriangles(NodeConnectionMatrix triangles);
    
    
    
    /***************************************************************************
     * Internally saves the data from the file for later use.  The format of the
     * lines may be altered as required to more easily parse the file.
     * 
     * @throws  java.io.IOException if the file could not be read
     **************************************************************************/
    public void readFile()
            throws IOException;
} // eoi