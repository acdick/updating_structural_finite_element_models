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

import de.iabg.mode.MassMatrix;
import de.iabg.mode.ModeConnectionWriter;
import de.iabg.mode.ModeConnectionMatrix;
import de.iabg.mode.ModeCorrelationMatrix;
import de.iabg.mode.ModeMatrix;

import de.iabg.mesh.NodeConnectionMatrix;

import de.iabg.swing.KeyList;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import java.util.Calendar;
import java.util.Locale;

/*******************************************************************************
 * This implementation of {@link ModeConnectionWriter} writes an ASCII file
 * containing mode connection data, namely a Nastran optimization deck input
 * file for an Orthogonality Check update.  The mode connections make up
 * the objective function to be used in Nastran with other files for design
 * parameters.  In general, the last method the user should call is
 * {@link #exportModeConnections(de.iabg.mode.ModeConnectionMatrix,
 * de.iabg.mesh.NodeConnectionMatrix)}.  All other methods declared in this
 * interface may be required to successfully write the data and therefore,
 * should be called beforehand.  If any data is omitted, the output data may
 * give unpredicatable results.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 27, 2008
 ******************************************************************************/
public class OrthogonalityCheckUpdateWriter extends FileWriter
        implements ModeConnectionWriter {
    /** An array for the entry identifiers of the correlations */
    protected int[] correlationEntries_;
    
    /** An array of the weightings of the correlations */
    protected double[] correlationWeights_;
    
    /** The current entry identifier */
    protected int entryIdentifier_;
    
    /** The current equation identifier */
    protected int equationIdentifier_;
    
    /** The file of the {@code First Mesh} */
    protected File firstMeshFile_;
    
    /** The file of the {@code First Mode} */
    protected File firstModeFile_;
    
    /** Storage for the {@code First Mode} matrix */
    protected ModeMatrix firstModes_;
    
    /** An array for the entry identifiers of the frequencies */
    protected int[] frequencyEntries_;
    
    /** An array of the weightings of the frequencies */
    protected double[] frequencyWeights_;
    
    /** The file of the {@code Last Mesh} */
    protected File lastMeshFile_;
    
    /** The file of the {@code Last Mode} */
    protected File lastModeFile_;
    
    /** Storage for the {@code Last Mode} matrix */
    protected ModeMatrix lastModes_;
    
    /** Storage for the mass matrix */
    protected MassMatrix masses_;
    
    /** The file of the mass matrix */
    protected File massFile_;
    
    /** Storage for the mode connections */
    protected ModeConnectionMatrix modeConnections_;
    
    /** Storage for the node connections */
    protected NodeConnectionMatrix nodeConnections_;
    
    /** The identifier of the objective */
    protected int objectiveIdentifier_;
    
    /** The output writer of this class */
    protected PrintWriter printWriter_;
    
    
    
    /***************************************************************************
     * Constructs a {@code OrthogonalityCheckUpdateWriter} from the given
     * file.
     * 
     * @param   file                the file to be exported
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public OrthogonalityCheckUpdateWriter(File file)
            throws IOException {
        super(file);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void exportModeConnections(ModeConnectionMatrix modeConnections,
            NodeConnectionMatrix nodeConnections)
            throws IOException {
        modeConnections_                    = modeConnections;
        nodeConnections_                    = nodeConnections;
        printWriter_                        = null;
        BufferedWriter      bufferedWriter;
        int                 nModes;
        
        try {
            bufferedWriter      = new BufferedWriter(this);
            printWriter_        = new PrintWriter(bufferedWriter);
            nModes              = modeConnections_.getConnectionCount();
            correlationEntries_ = new int[nModes];
            frequencyEntries_   = new int[nModes];
            
            this.writeHeader();
            
            for (int i = 0; i < nModes; i++) {
                this.writeModeConnection(i);
            }
            
            this.writeSummary();
            this.writeObjectiveEquation();
            this.writeObjectiveFunction();
            this.writeObjectiveWeights();
        }
        finally {
            if (printWriter_ != null) {
                printWriter_.close();
            }
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setCorrelationWeights(double[] correlationWeights) {
        correlationWeights_ = correlationWeights;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setEquationIdentifier(int equationIdentifier) {
        equationIdentifier_     = equationIdentifier;
        entryIdentifier_        = equationIdentifier + 1;
        objectiveIdentifier_    = equationIdentifier;
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
    public void setFirstModes(ModeMatrix firstModes) {
        firstModes_ = firstModes;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setFrequencyWeights(double[] frequencyWeights) {
        frequencyWeights_ = frequencyWeights;
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
    public void setLastModes(ModeMatrix lastModes) {
        lastModes_ = lastModes;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setMasses(MassMatrix masses) {
        masses_ = masses;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setMassFile(File massFile) {
        massFile_ = massFile;
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
     * Writes the mode correlation function at the given mode index.
     * 
     * @param   modeIndex   the index of the mode correlation
     **************************************************************************/
    protected void writeCorrelationFunction(int modeIndex) {
        ModeConnectionMatrix    modeConnections;
        ModeCorrelationMatrix   modeCorrelations;
        ModeMatrix              firstModes;
        KeyList                 firstModeKeys;
        KeyList                 firstNodeKeys;
        int                     nNodes;
        
        firstModes          = new ModeMatrix(firstModes_);
        firstModeKeys       = modeConnections_.getFirstModeNames();
        firstNodeKeys       = nodeConnections_.getFirstNodeKeys();
        modeCorrelations    = firstModes.getReducedGeneralizedMass(masses_,
                firstModes, firstModeKeys, firstNodeKeys, firstModeKeys,
                firstNodeKeys);
        modeConnections     = modeCorrelations.getConnection();
        nNodes              = nodeConnections_.getConnectionCount();
        
        correlationEntries_[modeIndex] = entryIdentifier_;
        
        printWriter_.format("%-8s", "DRESP2");
        printWriter_.format("%8d", entryIdentifier_++);
        printWriter_.format(" ORT%02d  ", modeIndex + 1);
        printWriter_.format("%8s", equationIdentifier_);
        printWriter_.format("%n");
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "DRESP2");
        printWriter_.format("%8s", entryIdentifier_ - 3 - nNodes);
        printWriter_.format("%8s", entryIdentifier_ - 2);
        printWriter_.format("%n");
        
        printWriter_.format("%-8s", "DEQATN");
        printWriter_.format("%8d", equationIdentifier_++);
        printWriter_.format("f(VerSim, SimSim)=VerSim**2/(SimSim * %E)",
                modeConnections.getCorrelationAt(modeIndex));
        printWriter_.format("%n");
    } // eom
    
    
    
    /***************************************************************************
     * Writes a formatted double line break to the file.
     **************************************************************************/
    protected void writeDoubleBreak() {
        printWriter_.println("$");
        this.writeBreak();
    } // eom
    
    
    
    /***************************************************************************
     * Writes the frequency difference of the objective function at the given
     * mode index.
     * 
     * @param   modeIndex   the index of the mode
     **************************************************************************/
    protected void writeFrequencyFunction(int modeIndex) {
        double frequency = modeConnections_.getFirstModeFrequencyAt(modeIndex);
        
        frequencyEntries_[modeIndex] = entryIdentifier_;
        
        printWriter_.format("%-8s", "DEQATN");
        printWriter_.format("%8d", equationIdentifier_++);
        printWriter_.format(Locale.US, "f(Freq)=%8.3f/Freq", frequency);
        printWriter_.format("%n");
        
        printWriter_.format("%-8s", "DRESP2");
        printWriter_.format("%8d", entryIdentifier_++);
        printWriter_.format("VFreq%02d ", modeIndex + 1);
        printWriter_.format("%8d", equationIdentifier_ - 1);
        printWriter_.format("%n");
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "DRESP1");
        printWriter_.format("%8d", entryIdentifier_ - 2);
        printWriter_.format("%n");
    } // eom
    
    
    
    /***************************************************************************
     * Writes the standard header to the file.  This header contains information
     * for the time and date, the {@code First Mesh} file, the {@code Last Mesh}
     * file, the {@code First Mode} file, and the {@code Last Mode} file.
     **************************************************************************/
    protected void writeHeader() {
        String date = String.format("%td.%tm.%tY", Calendar.getInstance(),
                Calendar.getInstance(), Calendar.getInstance());
        String time = String.format("%tT", Calendar.getInstance());
        
        this.writeBreak();
        this.writeComment(
                "Optimierung der Eigenformen und Frequenzen mit MSC.Nastran");
        this.writeComment("Copyright IABG 2008");
        this.writeComment("TA43; Einsteinstr. 20; 85521 Ottobrunn");
        this.writeComment("");
        this.writeComment("Erstellt mit ortwerte");
        this.writeComment("Author .................. : Adam C. Dick, BSE");
        this.writeComment("Version ................. : 0.01");
        this.writeComment("vom  .................... : 27.09.2008");
        this.writeComment("Erstellt am ............. : " + date);
        this.writeComment("Erstellt um ............. : " + time);
        this.writeComment("");
        this.writeComment("Versuchsmoden aus ....... : " +
                firstModeFile_.getName());
        this.writeComment("Simulationsmoden aus .... : " +
                lastModeFile_.getName());
        this.writeComment("Versuchsmodell aus ...... : " +
                firstMeshFile_.getName());
        this.writeComment("Simulationsmodell aus ... : " +
                lastMeshFile_.getName());
        this.writeComment("Massenmatrix aus ........ : " +
                massFile_.getName());
        this.writeComment("");
        this.writeComment("              * * * * * * * * * * * * * * * *");
        this.writeComment(
                " Am Ende der Datei steht eine Zusammenfassung der Responses");
        this.writeComment(
                "    Die Zielfunktion ist ueber Faktoren zu beeinflussen");
        this.writeComment("              * * * * * * * * * * * * * * * *");
        this.writeComment("");
        this.writeBreak();
        this.writeDoubleBreak();
    } // eom
    
    
    
    /***************************************************************************
     * Writes the frequency of the {@code Last Mode} at the given mode index.
     * 
     * @param   modeIndex   the index of the mode
     **************************************************************************/
    protected void writeLastFrequency(int modeIndex) {
        String modeName = modeConnections_.getLastModeNameAt(modeIndex);
        
        printWriter_.format("%-8s", "DRESP1");
        printWriter_.format("%8d", entryIdentifier_++);
        printWriter_.format("FREQ%02d  ", modeIndex + 1);
        printWriter_.format("%-8s", "FREQ");
        printWriter_.format("%-8s", "");
        printWriter_.format("%-8s", "");
        printWriter_.format("%8s", modeName);
        printWriter_.format("%n");
    } // eom
    
    
    
    /***************************************************************************
     * Writes the names of the {@code Last Nodes} of the
     * {@code MeshConnectionPanel} at the given mode index.
     * 
     * @param   modeIndex   the index of the mode
     **************************************************************************/
    protected void writeLastNodes(int modeIndex) {
        String modeName = modeConnections_.getLastModeNameAt(modeIndex);
        String nodeName;
        
        for (int i = 0; i < nodeConnections_.getConnectionCount(); i++) {
            nodeName = nodeConnections_.getLastNodeNameAt(i);
            
            printWriter_.format("%-8s", "DRESP1");
            printWriter_.format("%8d", entryIdentifier_++);
            printWriter_.format("M%02dDx%03d", modeIndex + 1, i + 1);
            printWriter_.format("%-8s", "DISP");
            printWriter_.format("%-8s", "");
            printWriter_.format("%-8s", "");
            printWriter_.format("%8d", 1);
            printWriter_.format("%8s", modeName);
            printWriter_.format("%8s", nodeName);
            printWriter_.format("%n");
            
            printWriter_.format("%-8s", "DRESP1");
            printWriter_.format("%8d", entryIdentifier_++);
            printWriter_.format("M%02dDy%03d", modeIndex + 1, i + 1);
            printWriter_.format("%-8s", "DISP");
            printWriter_.format("%-8s", "");
            printWriter_.format("%-8s", "");
            printWriter_.format("%8d", 2);
            printWriter_.format("%8s", modeName);
            printWriter_.format("%8s", nodeName);
            printWriter_.format("%n");
            
            printWriter_.format("%-8s", "DRESP1");
            printWriter_.format("%8d", entryIdentifier_++);
            printWriter_.format("M%02dDz%03d", modeIndex + 1, i + 1);
            printWriter_.format("%-8s", "DISP");
            printWriter_.format("%-8s", "");
            printWriter_.format("%-8s", "");
            printWriter_.format("%8d", 3);
            printWriter_.format("%8s", modeName);
            printWriter_.format("%8s", nodeName);
            printWriter_.format("%n");
        }
    } // eom
    
    
    
    /***************************************************************************
     * Writes the equation of the {@code Last Mode} for the last product of
     * the objective function at the given mode index.
     * 
     * @param   modeIndex   the index of the mode
     **************************************************************************/
    protected void writeLastProductEquation(int modeIndex) {
        int     nNodes = nodeConnections_.getConnectionCount();
        double  xx;
        double  xy;
        double  xz;
        double  yx;
        double  yy;
        double  yz;
        double  zx;
        double  zy;
        double  zz;
        
        for (int i = 0; i < nNodes; i++) {
            printWriter_.format("%-8s", "DEQATN");
            printWriter_.format("%8d", equationIdentifier_++);
            printWriter_.format("f(");
            printWriter_.format("%n");
            
            for (int k = 0; k < nNodes - 1; k++) {
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("M%02dDx%03d,", modeIndex + 1, k + 1);
                printWriter_.format("M%02dDy%03d,", modeIndex + 1, k + 1);
                printWriter_.format("M%02dDz%03d,", modeIndex + 1, k + 1);
                printWriter_.format("%n");
            }
            
            printWriter_.format("%-8s", "+");
            printWriter_.format("%-8s", "");
            printWriter_.format("M%02dDx%03d,", modeIndex + 1, nNodes);
            printWriter_.format("M%02dDy%03d,", modeIndex + 1, nNodes);
            printWriter_.format("M%02dDz%03d)=", modeIndex + 1, nNodes);
            printWriter_.format("%n");
        
            for (int j = 0; j < nNodes; j++) {
                xx = masses_.getXXMassAt(i, j);
                xy = masses_.getXYMassAt(i, j);
                xz = masses_.getXZMassAt(i, j);
                yx = masses_.getYXMassAt(i, j);
                yy = masses_.getYYMassAt(i, j);
                yz = masses_.getYZMassAt(i, j);
                zx = masses_.getZXMassAt(i, j);
                zy = masses_.getZYMassAt(i, j);
                zz = masses_.getZZMassAt(i, j);
                
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("M%02dDx%03d*% E*M%02dDx%03d+",
                        modeIndex + 1, i + 1, xx, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("M%02dDy%03d*% E*M%02dDx%03d+",
                        modeIndex + 1, i + 1, xy, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("M%02dDz%03d*% E*M%02dDx%03d+",
                        modeIndex + 1, i + 1, xz, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("M%02dDx%03d*% E*M%02dDy%03d+",
                        modeIndex + 1, i + 1, yx, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("M%02dDy%03d*% E*M%02dDy%03d+",
                        modeIndex + 1, i + 1, yy, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("M%02dDz%03d*% E*M%02dDy%03d+",
                        modeIndex + 1, i + 1, yz, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("M%02dDx%03d*% E*M%02dDz%03d+",
                        modeIndex + 1, i + 1, zx, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("M%02dDy%03d*% E*M%02dDz%03d+",
                        modeIndex + 1, i + 1, zy, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                
                if (j == nNodes - 1) {
                    printWriter_.format("M%02dDz%03d*% E*M%02dDz%03d",
                            modeIndex + 1, i + 1, zz, modeIndex + 1, j + 1);
                }
                else {
                    printWriter_.format("M%02dDz%03d*% E*M%02dDz%03d+",
                            modeIndex + 1, i + 1, zz, modeIndex + 1, j + 1);
                }
                
                printWriter_.format("%n");
            }
            
            this.writeBreak();
        }
    } // eom
    
    
    
    /***************************************************************************
     * Writes the names of the {@code Last Mode} for the last product of the
     * objective function at the given mode index.
     * 
     * @param   modeIndex   the index of the mode
     **************************************************************************/
    protected void writeLastProductNames(int modeIndex) {
        int nNodes      = nodeConnections_.getConnectionCount();
        int nColumns    = 8;
        int iColumn     = 1;
        
        for (int j = 0; j < nNodes; j++) {
            printWriter_.format("%-8s", "DRESP2");
            printWriter_.format("%8d", entryIdentifier_++);
            printWriter_.format("SS%02dN%03d", modeIndex + 1, j + 1);
            printWriter_.format("%8s", equationIdentifier_ - nNodes + j);
            printWriter_.format("%n");
            
            printWriter_.format("%-8s", "+");
            printWriter_.format("%-8s", "DRESP1");
            
            for (int i = 0; i < 3 * nNodes; i++) {
                if (iColumn == nColumns) {
                    printWriter_.format("%n");
                    iColumn = 0;
                }
                
                if (iColumn == 0) {
                    printWriter_.format("%-8s", "+");
                    printWriter_.format("%-8s", "");
                    iColumn++;
                }
                
                printWriter_.format("%8d", entryIdentifier_ -
                        (3 * nNodes) - nNodes - 4 - j + i);
                iColumn++;
            }
            
            iColumn = 1;
            printWriter_.format("%n");
            this.writeBreak();
        }
        
        printWriter_.format("%-8s", "DRESP2");
        printWriter_.format("%8d", entryIdentifier_++);
        printWriter_.format("SimSim%02d", modeIndex + 1);
        printWriter_.format("%8s", equationIdentifier_++);
        printWriter_.format("%n");
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "DRESP2");
        
        for (int i = 0; i < nNodes; i++) {
            if (iColumn == nColumns) {
                printWriter_.format("%n");
                iColumn = 0;
            }
            
            if (iColumn == 0) {
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                iColumn++;
            }
            
            printWriter_.format("%8d",
                    entryIdentifier_ - nNodes - 1 + i);
            iColumn++;
        }
        
        printWriter_.format("%n");
        printWriter_.format("%-8s", "DEQATN");
        printWriter_.format("%8d", equationIdentifier_ - 1);
        printWriter_.format("f(");
        printWriter_.format("%n");
        
        for (int i = 0; i < nNodes - 1; i++) {
            printWriter_.format("%-8s", "+");
            printWriter_.format("%-8s", "");
            printWriter_.format("SS%02dN%03d,", modeIndex + 1, i + 1);
            printWriter_.format("%n");
        }
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "");
        printWriter_.format("SS%02dN%03d)=", modeIndex + 1, nNodes);
        printWriter_.format("%n");
        
        for (int i = 0; i < nNodes - 1; i++) {
            printWriter_.format("%-8s", "+");
            printWriter_.format("%-8s", "");
            printWriter_.format("SS%02dN%03d+", modeIndex + 1, i + 1);
            printWriter_.format("%n");
        }
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "");
        printWriter_.format("SS%02dN%03d", modeIndex + 1, nNodes);
        printWriter_.format("%n");
    } // eom
    
    
    
    /***************************************************************************
     * Writes the equation of the {@code First Mode}, {@code Last Mode}, and
     * mass matrix for the mixed product of the objective function at the given
     * mode index.
     * 
     * @param   modeIndex   the index of the mode
     **************************************************************************/
    protected void writeMixedProductEquation(int modeIndex) {
        ModeMatrix  firstModes  = new ModeMatrix(firstModes_);
        int         nNodes      = nodeConnections_.getConnectionCount();
        double      x;
        double      y;
        double      z;
        double      xx;
        double      xy;
        double      xz;
        double      yx;
        double      yy;
        double      yz;
        double      zx;
        double      zy;
        double      zz;
        
        firstModes.sortNodes(nodeConnections_.getFirstNodeKeys());
        
        for (int i = 0; i < nNodes; i++) {
            x = firstModes.getXTranslationAt(modeIndex, i);
            y = firstModes.getYTranslationAt(modeIndex, i);
            z = firstModes.getZTranslationAt(modeIndex, i);
            
            printWriter_.format("%-8s", "DEQATN");
            printWriter_.format("%8d", equationIdentifier_++);
            printWriter_.format("f(");
            printWriter_.format("%n");
            
            for (int k = 0; k < nNodes - 1; k++) {
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("M%02dDx%03d,", modeIndex + 1, k + 1);
                printWriter_.format("M%02dDy%03d,", modeIndex + 1, k + 1);
                printWriter_.format("M%02dDz%03d,", modeIndex + 1, k + 1);
                printWriter_.format("%n");
            }
            
            printWriter_.format("%-8s", "+");
            printWriter_.format("%-8s", "");
            printWriter_.format("M%02dDx%03d,", modeIndex + 1, nNodes);
            printWriter_.format("M%02dDy%03d,", modeIndex + 1, nNodes);
            printWriter_.format("M%02dDz%03d)=", modeIndex + 1, nNodes);
            printWriter_.format("%n");
                
            for (int j = 0; j < nNodes; j++) {
                xx = masses_.getXXMassAt(i, j);
                xy = masses_.getXYMassAt(i, j);
                xz = masses_.getXZMassAt(i, j);
                yx = masses_.getYXMassAt(i, j);
                yy = masses_.getYYMassAt(i, j);
                yz = masses_.getYZMassAt(i, j);
                zx = masses_.getZXMassAt(i, j);
                zy = masses_.getZYMassAt(i, j);
                zz = masses_.getZZMassAt(i, j);
                
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("% E*% E*M%02dDx%03d+",
                        x, xx, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("% E*% E*M%02dDx%03d+",
                        y, xy, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("% E*% E*M%02dDx%03d+",
                        z, xz, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("% E*% E*M%02dDy%03d+",
                        x, yx, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("% E*% E*M%02dDy%03d+",
                        y, yy, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("% E*% E*M%02dDy%03d+",
                        z, yz, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("% E*% E*M%02dDz%03d+",
                        x, zx, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                printWriter_.format("% E*% E*M%02dDz%03d+",
                        y, zy, modeIndex + 1, j + 1);
                printWriter_.format("%n");
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                
                if (j == nNodes - 1) {
                    printWriter_.format("% E*% E*M%02dDz%03d",
                            z, zz, modeIndex + 1, j + 1);
                }
                else {
                    printWriter_.format("% E*% E*M%02dDz%03d+",
                            z, zz, modeIndex + 1, j + 1);
                }
                
                printWriter_.format("%n");
            }
            
            this.writeBreak();
        }
    } // eom
    
    
    
    /***************************************************************************
     * Writes the names of the {@code First Mode} and {@code Last Mode} for the
     * mixed product of the objective function at the given mode index.
     * 
     * @param   modeIndex   the index of the mode
     **************************************************************************/
    protected void writeMixedProductNames(int modeIndex) {
        int nNodes      = nodeConnections_.getConnectionCount();
        int nColumns    = 8;
        int iColumn     = 1;
        
        for (int j = 0; j < nNodes; j++) {
            printWriter_.format("%-8s", "DRESP2");
            printWriter_.format("%8d", entryIdentifier_++);
            printWriter_.format("VS%02dN%03d", modeIndex + 1, j + 1);
            printWriter_.format("%8s", equationIdentifier_ - nNodes + j);
            printWriter_.format("%n");
            
            printWriter_.format("%-8s", "+");
            printWriter_.format("%-8s", "DRESP1");
            
            for (int i = 0; i < 3 * nNodes; i++) {
                if (iColumn == nColumns) {
                    printWriter_.format("%n");
                    iColumn = 0;
                }
                
                if (iColumn == 0) {
                    printWriter_.format("%-8s", "+");
                    printWriter_.format("%-8s", "");
                    iColumn++;
                }
                
                printWriter_.format("%8d",
                        entryIdentifier_ - (3 * nNodes) - 3 - j + i);
                iColumn++;
            }
            
            iColumn = 1;
            printWriter_.format("%n");
            this.writeBreak();
        }
        
        printWriter_.format("%-8s", "DRESP2");
        printWriter_.format("%8d", entryIdentifier_++);
        printWriter_.format("VerSim%02d", modeIndex + 1);
        printWriter_.format("%8s", equationIdentifier_++);
        printWriter_.format("%n");
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "DRESP2");
        
        for (int i = 0; i < nNodes; i++) {
            if (iColumn == nColumns) {
                printWriter_.format("%n");
                iColumn = 0;
            }
            
            if (iColumn == 0) {
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                iColumn++;
            }
            
            printWriter_.format("%8d",
                    entryIdentifier_ - nNodes - 1 + i);
            iColumn++;
        }
        
        printWriter_.format("%n");
        printWriter_.format("%-8s", "DEQATN");
        printWriter_.format("%8d", equationIdentifier_ - 1);
        printWriter_.format("f(");
        printWriter_.format("%n");
        
        for (int i = 0; i < nNodes - 1; i++) {
            printWriter_.format("%-8s", "+");
            printWriter_.format("%-8s", "");
            printWriter_.format("VS%02dN%03d,", modeIndex + 1, i + 1);
            printWriter_.format("%n");
        }
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "");
        printWriter_.format("VS%02dN%03d)=", modeIndex + 1, nNodes);
        printWriter_.format("%n");
        
        for (int i = 0; i < nNodes - 1; i++) {
            printWriter_.format("%-8s", "+");
            printWriter_.format("%-8s", "");
            printWriter_.format("VS%02dN%03d+", modeIndex + 1, i + 1);
            printWriter_.format("%n");
        }
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "");
        printWriter_.format("VS%02dN%03d", modeIndex + 1, nNodes);
        printWriter_.format("%n");
    } // eom
    
    
    
    /***************************************************************************
     * Writes a mode connection to the file from the given mode connection
     * index.
     * 
     * @param   modeConnectionIndex the index of the mode connection
     **************************************************************************/
    protected void writeModeConnection(int modeConnectionIndex) {
        String modeName;
        
        modeName = String.format("%2d", modeConnectionIndex + 1);
        
        this.writeComment(
                "                   *** Beginn Mode " + modeName + " ***");
        this.writeBreak();
        this.writeComment("DRESP1-Karten fuer die Eigenformoptimierung");
        this.writeComment("Mode: " + modeName);
        this.writeBreak();
        this.writeLastNodes(modeConnectionIndex);
        this.writeDoubleBreak();
        this.writeComment("DRESP1-Karten fuer die Eigenfrequenzoptimierung");
        this.writeComment("Mode: " + modeName);
        this.writeBreak();
        this.writeLastFrequency(modeConnectionIndex);
        this.writeBreak();
        this.writeDoubleBreak();
        this.writeComment("DRESP2-" +
                "Karte mit Verhaeltnis gerechnete zu Simulationsfrequenz");
        this.writeComment("Mode: " + modeName);
        this.writeBreak();
        this.writeBreak();
        this.writeComment(
                "Gleichung zur Bestimmung des Frequenzverhaeltnisses");
        this.writeComment("der gerechneten zu den gemessenen Eigenfrequenzen");
        this.writeBreak();
        this.writeFrequencyFunction(modeConnectionIndex);
        this.writeBreak();
        this.writeDoubleBreak();
        this.writeComment("Gleichung des Skalarprodukts");
        this.writeComment(
                "Phi(Versuch) * Massenmatrix(Versuch) * Phi(Simulation)");
        this.writeComment("Mode: " + modeName);
        this.writeBreak();
        this.writeMixedProductEquation(modeConnectionIndex);
        this.writeBreak();
        this.writeDoubleBreak();
        this.writeComment("DRESP2-Karte des Skalarprodukt");
        this.writeComment(
                "Phi(Versuch) * Massenmatrix(Versuch) * Phi(Simulation)");
        this.writeComment("Mode: " + modeName);
        this.writeBreak();
        this.writeMixedProductNames(modeConnectionIndex);
        this.writeBreak();
        this.writeDoubleBreak();
        this.writeComment("Gleichung des Skalarprodukts");
        this.writeComment(
                "Phi(Simulation) * Massenmatrix(Versuch) * Phi(Simulation)");
        this.writeComment("Mode: " + modeName);
        this.writeBreak();
        this.writeLastProductEquation(modeConnectionIndex);
        this.writeBreak();
        this.writeDoubleBreak();
        this.writeComment("DRESP2-Karte des Skalarprodukts");
        this.writeComment(
                "Phi(Simulation) * Massenmatrix(Versuch) * Phi(Simulation)");
        this.writeComment("Mode: " + modeName);
        this.writeBreak();
        this.writeLastProductNames(modeConnectionIndex);
        this.writeBreak();
        this.writeDoubleBreak();
        this.writeComment("ORT-Wert Versuch-Rechnung");
        this.writeComment("Mode: " + modeName);
        this.writeBreak();
        this.writeCorrelationFunction(modeConnectionIndex);
        this.writeBreak();
        this.writeComment("*** Ende Mode " + modeName + " ***");
        this.writeBreak();
        this.writeDoubleBreak();
    } // eom
    
    
    
    /***************************************************************************
     * Writes the equation of the objective function for the stored mode
     * connection.
     **************************************************************************/
    protected void writeObjectiveEquation() {
        int nModes = modeConnections_.getConnectionCount();
        
        this.writeComment("                 Gleichung der Zielfunktion");
        this.writeBreak();
        printWriter_.format("%-8s", "DEQATN");
        printWriter_.format("%8d", equationIdentifier_++);
        printWriter_.format("f(");
        printWriter_.format("%n");
        
        for (int i = 0; i < nModes; i++) {
            printWriter_.format("%-8s", "+");
            printWriter_.format("%-8s", "");
            printWriter_.format("xFreq%02d, ", i + 1);
            printWriter_.format("xOrt%02d,", i + 1);
            printWriter_.format("%n");
        }
        
        for (int i = 0; i < nModes - 1; i++) {
            printWriter_.format("%-8s", "+");
            printWriter_.format("%-8s", "");
            printWriter_.format("Freq%02d, ", i + 1);
            printWriter_.format("Ort%02d,", i + 1);
            printWriter_.format("%n");
        }
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "");
        printWriter_.format("Freq%02d, ", nModes);
        printWriter_.format("Ort%02d)=", nModes);
        printWriter_.format("%n");
        
        for (int i = 0; i < nModes - 1; i++) {
            printWriter_.format("%-8s", "+");
            printWriter_.format("%-8s", "");
            printWriter_.format("xFreq%02d*(Freq%02d-1.)**2+", i + 1, i + 1);
            printWriter_.format("xOrt%02d*(Ort%02d-1.)**2+", i + 1, i + 1);
            printWriter_.format("%n");
        }
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "");
        printWriter_.format("xFreq%02d*(Freq%02d-1.)**2+", nModes, nModes);
        printWriter_.format("xOrt%02d*(Ort%02d-1.)**2", nModes, nModes);
        printWriter_.format("%n");
        
        this.writeBreak();
        this.writeDoubleBreak();
    } // eom
    
    
    
    /***************************************************************************
     * Writes the objective function for the stored mode connection.
     **************************************************************************/
    protected void writeObjectiveFunction() {
        int         nModes              = modeConnections_.getConnectionCount();
        int[]       objectiveEntries    = new int[2 * nModes];
        String[]    objectiveNames      = new String[2 * nModes];
        int         nColumns            = 8;
        int         iColumn             = 1;
        
        for (int i = 0; i < nModes; i++) {
            objectiveNames[2 * i]       = String.format(" xFreq%02d", i + 1);
            objectiveNames[(2 * i) + 1] = String.format("  xOrt%02d", i + 1);
            
            objectiveEntries[2 * i]         = frequencyEntries_[i];
            objectiveEntries[(2 * i) + 1]   = correlationEntries_[i];
        }
        
        this.writeComment("                  Z i e l f u n k t i o n");
        this.writeBreak();
        
        printWriter_.format("%-8s", "DRESP2");
        printWriter_.format("%8d", objectiveIdentifier_);
        printWriter_.format("%8s", "Zielfkt");
        printWriter_.format("%8d", equationIdentifier_ - 1);
        printWriter_.format("%n");
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "DTABLE");
        
        for (int i = 0; i < 2 * nModes; i++) {
            if (iColumn == nColumns) {
                printWriter_.format("%n");
                iColumn = 0;
            }
            
            if (iColumn == 0) {
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                iColumn++;
            }
            
            printWriter_.format("%s", objectiveNames[i]);
            iColumn++;
        }
        
        printWriter_.format("%n");
        
        iColumn = 1;
        
        printWriter_.format("%-8s", "+");
        printWriter_.format("%-8s", "DRESP2");
        
        for (int i = 0; i < 2 * nModes; i++) {
            if (iColumn == nColumns) {
                printWriter_.format("%n");
                iColumn = 0;
            }
            
            if (iColumn == 0) {
                printWriter_.format("%-8s", "+");
                printWriter_.format("%-8s", "");
                iColumn++;
            }
            
            printWriter_.format("%8d", objectiveEntries[i]);
            iColumn++;
        }
        
        printWriter_.format("%n");
        
        this.writeBreak();
        this.writeDoubleBreak();
    } // eom
    
    
    
    /***************************************************************************
     * Writes the weightings of the objective function, namely the stored mode
     * correlation and frequency weightings.
     **************************************************************************/
    protected void writeObjectiveWeights() {
        int nModes = modeConnections_.getConnectionCount();
        
        this.writeComment("               Faktoren fuer die Zielfunktion");
        this.writeBreak();
        
        printWriter_.format("%-8s", "DTABLE");
        
        for (int i = 0; i < nModes - 1; i++) {
            printWriter_.format(" xFreq%02d", i + 1);
            printWriter_.format(Locale.US, " %7.2f", frequencyWeights_[i]);
            printWriter_.format("  xOrt%02d", i + 1);
            printWriter_.format(Locale.US, " %7.2f", correlationWeights_[i]);
            printWriter_.format("%n");
            printWriter_.format("%-8s", "+");
        }
        
        printWriter_.format(" xFreq%02d", nModes);
        printWriter_.format(Locale.US, " %7.2f", frequencyWeights_[nModes - 1]);
        printWriter_.format("  xOrt%02d", nModes);
        printWriter_.format(Locale.US, " %7.2f",
                correlationWeights_[nModes - 1]);
        printWriter_.format("%n");
        
        this.writeBreak();
        this.writeDoubleBreak();
    } // eom
    
    
    
    /***************************************************************************
     * Writes the summary of the data contained in this file.
     **************************************************************************/
    protected void writeSummary() {
        String  correlationLine;
        String  frequencyLine;
        int     nModes              = modeConnections_.getConnectionCount();
        
        this.writeComment("               Zusammenfassung der RESPONSES");
        
        for (int i = 0; i < nModes; i++) {
            correlationLine = String.format(
                    "ORT-Wert            Versuchsmode (%2d)= DRESP-Id: %8d",
                    i + 1, correlationEntries_[i]);
            
            this.writeComment(correlationLine);
            
            frequencyLine = String.format(
                    "Frequenzverhaeltnis Versuchsmode (%2d)= DRESP-Id: %8d",
                    i + 1, frequencyEntries_[i]);
            
            this.writeComment(frequencyLine);
        }
        
        this.writeBreak();
        this.writeDoubleBreak();
    } // eom
} // eoc