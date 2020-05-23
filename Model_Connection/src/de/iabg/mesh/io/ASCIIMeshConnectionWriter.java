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
import java.util.Locale;

/*******************************************************************************
 * This implementation of {@link MeshConnectionWriter} writes an ASCII file
 * containing mesh connection data, namely the node connections.  Refer to
 * {@code MeshConnectionWriter} class API for more details.
 * 
 * @author  Adam C. Dick, BSE
 * @version November 1, 2008
 ******************************************************************************/
public class ASCIIMeshConnectionWriter extends FileWriter
        implements MeshConnectionWriter {
    /** The file of the {@code First Mesh} */
    protected File firstMeshFile_;
    
    /** The file of the {@code Last Mesh} */
    protected File lastMeshFile_;
    
    /** Storage for the node connections */
    protected NodeConnectionMatrix nodeConnections_;
    
    /** The output writer of this class */
    protected PrintWriter printWriter_;
    
    
    
    /***************************************************************************
     * Constructs a {@code ASCIIMeshConnectionWriter} from the given file.
     * 
     * @param   file                the file to be exported
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public ASCIIMeshConnectionWriter(File file)
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
        
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setConnectionIdentifier(int connectionIdentifier) {
        
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setConnectionType(String connectionType) {
        
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setFirstMeshFile(File firstMeshFile) {
        firstMeshFile_ = firstMeshFile;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setIndependentMesh(String independentMesh) {
        
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
        this.writeComment("Erstellt mit macwerte");
        this.writeComment("Author .................. : Adam C. Dick, BSE");
        this.writeComment("Version ................. : 0.01");
        this.writeComment("vom  .................... : 11.09.2008");
        this.writeComment("Erstellt am ............. : " + date);
        this.writeComment("Erstellt um ............. : " + time);
        this.writeComment("");
        this.writeComment("Versuchsmodell aus ...... : " +
                firstMeshFile_.getName());
        this.writeComment("Simulationsmodell aus ... : " +
                lastMeshFile_.getName());
        this.writeBreak();
        this.writeComment("Versuchsknoten - SimulationsKnoten - Abstand " +
                "(zur Information)");
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
        String firstName;
        String lastName;
        double correlation;
        
        firstName   = nodeConnections_.getFirstNodeNameAt(nodeConnectionIndex);
        lastName    = nodeConnections_.getLastNodeNameAt(nodeConnectionIndex);
        correlation = nodeConnections_.getCorrelationAt(nodeConnectionIndex);
        
        printWriter_.format(" %7s", firstName);
        printWriter_.format(" %10s", lastName);
        printWriter_.format(Locale.US, " %10.4f", correlation);
        printWriter_.format("%n");
    } // eom
} // eoc