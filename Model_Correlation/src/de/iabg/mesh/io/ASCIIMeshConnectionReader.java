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
package de.iabg.mesh.io;

import de.iabg.mesh.MeshConnectionReader;
import de.iabg.mesh.NodeConnectionMatrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

/*******************************************************************************
 * This implementation of {@link MeshConnectionReader} parses an ASCII file
 * containing mesh connection data, namely the node connections.  Refer to
 * {@code MeshConnectionReader} class API for more details.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public class ASCIIMeshConnectionReader extends FileReader
        implements MeshConnectionReader {
    /** A constant for comments */
    protected static final int COMMENT_FORMAT = 1000;
    
    /** A list for the data from the file */
    protected ArrayList<String> data_;
    
    /** A constant for node connections */
    protected static final int NODE_CONNECTION_FORMAT = 1001;
    
    /** Storage for the node connections */
    protected NodeConnectionMatrix nodeConnections_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code ASCIIMeshConnectionReader} from the given
     * file.
     * 
     * @param   file                            the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     **************************************************************************/
    public ASCIIMeshConnectionReader(File file)
            throws FileNotFoundException {
        super(file);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a node connection from the given line and adds it to the stored
     * node connections at the given index. 
     * 
     * @param   nodeConnectionIndex the index of the node connection
     * @param   line                the line to be parsed
     **************************************************************************/
    protected void addNodeConnection(int nodeConnectionIndex, String line) {
        String firstName;
        String lastName;
        double correlation;
        
        firstName   = line.substring(0, 8).trim();
        lastName    = line.substring(11, 19).trim();
        correlation = Double.parseDouble(line.substring(22, 30).trim());
        
        nodeConnections_.setFirstNodeNameAt(firstName, nodeConnectionIndex);
        nodeConnections_.setLastNodeNameAt(lastName, nodeConnectionIndex);
        nodeConnections_.setCorrelationAt(correlation, nodeConnectionIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Returns the format of the given line.
     * 
     * @param   line    the line to be parsed
     * @return  the line format
     **************************************************************************/
    protected int getFormat(String line) {
        if (line.startsWith("$")) {
            return COMMENT_FORMAT;
        }
        else {
            return NODE_CONNECTION_FORMAT;
        }
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of node connections in the data.
     * 
     * @return  the number of node connections
     **************************************************************************/
    protected int getNodeConnectionCount() {
        int nNodeConnections = 0;
        
        for (String line : data_) {
            if (!line.startsWith("$")) {
                nNodeConnections++;
            }
        }
        
        return nNodeConnections;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importNodeConnections(NodeConnectionMatrix connections) {
        nodeConnections_ = connections;
        nodeConnections_.setConnectionCount(this.getNodeConnectionCount());
        
        int nodeConnectionIndex = 0;
        
        for (String line : data_) {
            if (!line.startsWith("$")) {
                this.addNodeConnection(nodeConnectionIndex, line);
                nodeConnectionIndex++;
            }
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void readFile()
            throws IOException {
        data_                           = new ArrayList<String>();
        BufferedReader  bufferedReader  = null;
        String          line;
        
        try {
            bufferedReader = new BufferedReader(this);
            
            line = bufferedReader.readLine();
            while (line != null) {
                switch (this.getFormat(line)) {
                    case NODE_CONNECTION_FORMAT:
                    default:
                        data_.add(line);
                }
                
                line = bufferedReader.readLine();
            }
        }
        finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    } // eom
} // eoc