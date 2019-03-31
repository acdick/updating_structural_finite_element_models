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
package de.iabg.mode.io;

import de.iabg.mode.ModeCorrelationWriter;
import de.iabg.mode.ModeCorrelationMatrix;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*******************************************************************************
 * This implementation of {@link ModeCorrelationWriter} writes an Excel file
 * containing mode correlation data.  Refer to {@code ModeCorrelationWriter}
 * class API for more details.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class ExcelModeCorrelationWriter extends FileWriter
        implements ModeCorrelationWriter {
    /** The file of the {@code First Mesh} */
    protected File firstMeshFile_;
    
    /** The file of the {@code First Mode} */
    protected File firstModeFile_;
    
    /** The name of the {@code First Mesh} */
    protected String firstName_;
    
    /** The file of the {@code Last Mesh} */
    protected File lastMeshFile_;
    
    /** The file of the {@code Last Mode} */
    protected File lastModeFile_;
    
    /** The name of the {@code Last Mesh} */
    protected String lastName_;
    
    /** Storage for the mode correlations */
    protected ModeCorrelationMatrix modeCorrelations_;
    
    
    
    /***************************************************************************
     * Constructs an {@code ExcelModeCorrelationWriter} from the given file.
     * 
     * @param   file                the file to be exported
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public ExcelModeCorrelationWriter(File file)
            throws IOException {
        super(file);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void exportModeCorrelations(ModeCorrelationMatrix modeCorrelations)
            throws IOException {
        modeCorrelations_                   = modeCorrelations;
        BufferedWriter      bufferedWriter;
        PrintWriter         printWriter     = null;
        
        try {
            bufferedWriter  = new BufferedWriter(this);
            printWriter     = new PrintWriter(bufferedWriter);
            
            this.writeModeCorrelations(printWriter);
        }
        finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setFirstMeshFile(File firstMeshFile) {
        firstMeshFile_ = firstMeshFile;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setFirstModeFile(File firstModeFile) {
        firstModeFile_ = firstModeFile;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setFirstName(String firstName) {
        firstName_ = firstName;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setLastMeshFile(File lastMeshFile) {
        lastMeshFile_ = lastMeshFile;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setLastModeFile(File lastModeFile) {
        lastModeFile_ = lastModeFile;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setLastName(String lastName) {
        lastName_ = lastName;
    } // eom
    
    
    
    /***************************************************************************
     * Writes the stored mode correlations to the file using the given file
     * writer.  This method writes the names and frequencies of the
     * {@code First Mode} and {@code Last Mode}.  The values of the correlation
     * are written in semicolon-separated rows and columns.  Finally, the files
     * of the {@code First Mesh}, {@code First Mode}, {@code Last Mesh}, and
     * {@code Last Mode} and the names of the meshes are written.
     * 
     * @param   printWriter the file writer of the output file
     **************************************************************************/
    protected void writeModeCorrelations(PrintWriter printWriter) {
        String[] firstNames;
        double[] firstFrequencies;
        String[] lastNames;
        double[] lastFrequencies;
        
        firstNames          = modeCorrelations_.getFirstModeNames();
        firstFrequencies    = modeCorrelations_.getFirstModeFrequencies();
        lastNames           = modeCorrelations_.getLastModeNames();
        lastFrequencies     = modeCorrelations_.getLastModeFrequencies();
        
        printWriter.format(" %14s", firstName_);
        printWriter.format(";");
        printWriter.format(" %15s", ";");
        printWriter.format(" %14s", lastName_);
        printWriter.format(";");
        printWriter.format(" %14s", lastMeshFile_.getName());
        printWriter.format(";");
        printWriter.format(" %14s", lastModeFile_.getName());
        printWriter.format(";");
        printWriter.format("%n");
        
        printWriter.format(" %14s", firstMeshFile_.getName());
        printWriter.format(";");
        printWriter.format(" %15s", ";");
        printWriter.format(" %15s", "Moden-nummer;");
        
        for (String lastName : lastNames) {
            printWriter.format(" %14s", lastName);
            printWriter.format(";");
        }
        
        printWriter.format("%n");
        printWriter.format(" %14s", firstModeFile_.getName());
        printWriter.format(";");
        printWriter.format(" %15s", "Moden-nummer;");
        printWriter.format(" %15s", "Frequenz [Hz];");
        
        for (double frequency : lastFrequencies) {
            printWriter.format(" % 14f", frequency);
            printWriter.format(";");
        }
        
        printWriter.format("%n");
        
        for (int i = 0; i < modeCorrelations_.getFirstModeCount(); i++) {
            printWriter.format(" %15s", ";");
            printWriter.format(" %14s", firstNames[i]);
            printWriter.format(";");
            printWriter.format(" % 14f", firstFrequencies[i]);
            printWriter.format(";");
            
            for (int j = 0; j < modeCorrelations_.getLastModeCount(); j++) {
                printWriter.format(" % 14f",
                        modeCorrelations_.getValueAt(i, j));
                printWriter.format(";");
            }
            
            printWriter.format("%n");
        }
    } // eom
} // eoc