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

import de.iabg.mesh.MeshConnectionWriter;
import de.iabg.mesh.NodeConnectionMatrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Calendar;

/*******************************************************************************
 * This implementation of {@link MeshConnectionWriter} writes an ASCII file
 * containing rigid mesh connection data, namely the node connections.  Refer to
 * {@code MeshConnectionWriter} class API for more details.
 * 
 * @author  Adam C. Dick, BSE
 * @version November 1, 2008
 ******************************************************************************/
public class RigidMeshConnectionWriter extends FileWriter
        implements MeshConnectionWriter {
    /** The connected degrees of freedom */
    protected String connectionDOF_;
    
    /** The first connection identifier */
    protected int connectionIdentifier_;
    
    /** The connection type */
    protected String connectionType_;
    
    /** The file of the {@code First Mesh} */
    protected File firstMeshFile_;
    
    /** The independent mesh */
    protected String independentMesh_;
    
    /** The file of the {@code Last Mesh} */
    protected File lastMeshFile_;
    
    /** Storage for the node connections */
    protected NodeConnectionMatrix nodeConnections_;
    
    /** The output writer of this class */
    protected PrintWriter printWriter_;
    
    
    
    /***************************************************************************
     * Constructs a {@code RigidMeshConnectionWriter} from the given file.
     * 
     * @param   file                the file to be exported
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public RigidMeshConnectionWriter(File file)
            throws IOException {
        super(file);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void exportNodeConnections(NodeConnectionMatrix connections)
            throws IOException {
        nodeConnections_                    = connections;
        printWriter_                        = null;
        BufferedWriter      bufferedWriter;
        
        try {
            bufferedWriter  = new BufferedWriter(this);
            printWriter_    = new PrintWriter(bufferedWriter);
            
            this.writeHeader();
            
            for (int i = 0; i < nodeConnections_.getConnectionCount(); i++) {
                this.writeNodeConnection(i);
            }
            
            this.writeBreak();
        }
        finally {
            if (printWriter_ != null) {
                printWriter_.close();
            }
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setConnectionDegreesOfFreedom(String connectionDOF) {
        connectionDOF_ = connectionDOF;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setConnectionIdentifier(int connectionIdentifier) {
        connectionIdentifier_ = connectionIdentifier;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setConnectionType(String connectionType) {
        connectionType_ = connectionType;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setFirstMeshFile(File firstMeshFile) {
        firstMeshFile_ = firstMeshFile;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setIndependentMesh(String independentMesh) {
        independentMesh_ = independentMesh;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setLastMeshFile(File lastMeshFile) {
        lastMeshFile_ = lastMeshFile;
    } // eom
    
    
    
    /***************************************************************************
     * Writes a formatted line break to the file.
     **************************************************************************/
    protected void writeBreak() {
        printWriter_.print(  "$ ----------------------------------");
        printWriter_.println("------------------------------------");
    } // eom
    
    
    
    /***************************************************************************
     * Writes a formatted comment line to the file from the given line.
     * 
     * @param   line    the comment line
     **************************************************************************/
    protected void writeComment(String line) {
        StringBuilder   commentLine = new StringBuilder();
        int             lineLength  = 68;
        
        commentLine.append("$ --- ");
        commentLine.append(line);
        
        while (commentLine.length() < lineLength) {
            commentLine.append(" ");
        }
        
        commentLine.append(" ---");
        
        printWriter_.println(commentLine.toString());
    } // eom
    
    
    
    /***************************************************************************
     * Writes the standard header to the file.  This header contains information
     * for the time and date, the {@code First Mesh} file and the
     * {@code Last Mesh} file.
     **************************************************************************/
    protected void writeHeader() {
        String date = String.format("%td.%tm.%tY", Calendar.getInstance(),
                Calendar.getInstance(), Calendar.getInstance());
        String time = String.format("%tT", Calendar.getInstance());
        
        this.writeBreak();
        this.writeComment("Zuordnung Versuchs zu Simulationsknoten");
        this.writeComment("Copyright IABG 2008");
        this.writeComment("TA43; Einsteinstr. 20; 85521 Ottobrunn");
        this.writeComment("");
        this.writeComment("Author .................. : Adam C. Dick, BSE");
        this.writeComment("Version ................. : 0.01");
        this.writeComment("vom  .................... : 01.11.2008");
        this.writeComment("Erstellt am ............. : " + date);
        this.writeComment("Erstellt um ............. : " + time);
        this.writeComment("");
        this.writeComment("Versuchsmodell aus ...... : " +
                firstMeshFile_.getName());
        this.writeComment("Simulationsmodell aus ... : " +
                lastMeshFile_.getName());
        this.writeBreak();
    } // eom
    
    
    
    /***************************************************************************
     * Writes a node connection to the file from the given node connection
     * index.  This method accesses the data at that index from the stored
     * {@link NodeConnectionMatrix} and outputs the data to a formatted line.
     * 
     * @param   nodeConnectionIndex the index of the node connection
     **************************************************************************/
    protected void writeNodeConnection(int nodeConnectionIndex) {
        String  firstName;
        String  lastName;
        int     identifier;
        
        if (independentMesh_.equals("Model A")) {
            firstName   =
                    nodeConnections_.getFirstNodeNameAt(nodeConnectionIndex);
            lastName    =
                    nodeConnections_.getLastNodeNameAt(nodeConnectionIndex);
        }
        else {
            firstName   =
                    nodeConnections_.getLastNodeNameAt(nodeConnectionIndex);
            lastName    =
                    nodeConnections_.getFirstNodeNameAt(nodeConnectionIndex);
        }
        
        identifier  = connectionIdentifier_ + nodeConnectionIndex;
        
        printWriter_.format("%-8s", connectionType_);
        printWriter_.format("%-8d", identifier);
        printWriter_.format("%-8s", firstName);
        printWriter_.format("%-8s", connectionDOF_);
        printWriter_.format("%-8s", lastName);
        printWriter_.format("%n");
    } // eom
} // eoc