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
 * geometric connection data between two geometric models, namely node
 * connections.  In general, the user should call {@link #readFile()} so that
 * the parser reads and internally stores the data of the file.  The data can
 * then be safely parsed and extracted without having to stream file data
 * multiple times.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public interface MeshConnectionReader {
    
    
    
    /***************************************************************************
     * Clears the given node connections and replaces them with the data parsed
     * from the internally stored file.  This method should be called after
     * {@link #readFile()}, otherwise no data will be saved.
     * 
     * @param   connections the {@link NodeConnectionMatrix} where the data will
     *                      be stored
     **************************************************************************/
    public void importNodeConnections(NodeConnectionMatrix connections);
    
    
    
    /***************************************************************************
     * Internally saves the data from the file for later use.  The format of the
     * lines may be altered as required to more easily parse the file.
     * 
     * @throws  java.io.IOException if the file could not be read
     **************************************************************************/
    public void readFile()
            throws IOException;
} // eoi