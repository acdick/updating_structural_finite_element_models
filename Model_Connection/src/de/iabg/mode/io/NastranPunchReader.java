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

import de.iabg.mode.ModeMatrix;
import de.iabg.mode.ModeReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;

/*******************************************************************************
 * This implementation of {@link ModeReader} parses a Nastran file containing
 * mode shape data.  Refer to {@code ModeReader} class API for more details.
 * 
 * The Nastran file can contain an undefined number of mode shapes, with each
 * mode shape having a frequency and a name or identifier.  This parser is
 * designed to read in all of this data.  Refer to the latest version of the
 * MSC.Nastran Quick Reference Guide for more information for the file format.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class NastranPunchReader extends FileReader
        implements ModeReader {
    /** A constant for comments */
    protected static final int COMMENT_FORMAT = 1000;
    
    /** A list for the data from the file */
    protected ArrayList<String> data_;
    
    /** A constant for mode lines */
    protected static final int MODE_FORMAT = 1001;
    
    /** Storage for the modes */
    protected ModeMatrix modes_;
    
    /** A constant for node continuation lines */
    protected static final int NODE_CONTINUOUS_FORMAT = 1002;
    
    /** A constant for node lines */
    protected static final int NODE_FORMAT = 1003;
    
    /** A constant for undefined lines */
    protected static final int UNDEFINED_FORMAT = 1099;
    
    
    
    /***************************************************************************
     * Constructs a {@code NastranPunchReader} from the given file.
     * 
     * @param   file                            the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     **************************************************************************/
    public NastranPunchReader(File file)
            throws FileNotFoundException {
        super(file);
    } // eom
    
    
    
    /***************************************************************************
     * If the length of the given line is less than 72 characters, white space
     * characters are appended to the line until it reaches that size.
     * 
     * @param   line    the line to be filled
     * @return  the new line that has been filled
     **************************************************************************/
    protected String fillFields(String line) {
        StringBuilder   filledLine = new StringBuilder(line);
        int             lineLength = 72;
        
        while (filledLine.length() < lineLength) {
            filledLine.append(" ");
        }
        
        return filledLine.toString();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the format of the given line.
     * 
     * @param   line    the line to be parsed
     * @return  the line format
     **************************************************************************/
    protected int getFormat(String line) {
        if (line.toUpperCase().startsWith("$EIGENVALUE")) {
            return MODE_FORMAT;
        }
        else if (line.startsWith("$")) {
            return COMMENT_FORMAT;
        }
        else if (line.toUpperCase().startsWith("-CONT-")) {
            return NODE_CONTINUOUS_FORMAT;
        }
        else if (line.startsWith(" ")) {
            return NODE_FORMAT;
        }
        else {
            return UNDEFINED_FORMAT;
        }
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of modes in the data.
     * 
     * @return  the number of modes
     **************************************************************************/
    protected int getModeCount() {
        int nModes = 0;
        
        for (String line : data_) {
            switch (this.getFormat(line)) {
                case MODE_FORMAT:
                    nModes++;
                    break;
                default:
            }
        }
        
        return nModes;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of nodes in the data.
     * 
     * @return  the number of nodes
     **************************************************************************/
    protected int getNodeCount() {
        int iMode   = 0;
        int nNodes  = 0;
        
        nodeCount:
            for (String line : data_) {
                switch (this.getFormat(line)) {
                    case COMMENT_FORMAT:
                    case NODE_CONTINUOUS_FORMAT:
                        break;
                    case MODE_FORMAT:
                        iMode++;
                        if (iMode > 1) {
                            break nodeCount;
                        }
                        
                        break;
                    case NODE_FORMAT:
                        nNodes++;
                        break;
                    default:
                }
            }
        
        return nNodes;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importModes(ModeMatrix modes) {
        modes_ = modes;
        modes_.setModeCount(this.getModeCount());
        modes_.setNodeCount(this.getNodeCount());
        
        int modeIndex = -1;
        int nodeIndex = 0;
        
        for (String line : data_) {
            switch (this.getFormat(line)) {
                case MODE_FORMAT:
                    modeIndex++;
                    this.setModeAt(line, modeIndex);
                    nodeIndex = 0;
                    break;
                case NODE_FORMAT:
                    this.setNodeAt(line, modeIndex, nodeIndex);
                    nodeIndex++;
                    break;
                default:
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
                line = this.fillFields(line);
                data_.add(line);
                
                line = bufferedReader.readLine();
            }
        }
        finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    } // eom
    
    
    
    /***************************************************************************
     * Parses the given line and sets the mode name and frequency at the given
     * mode index.
     * 
     * @param   line        the line to be parsed
     * @param   modeIndex   the index of the mode
     **************************************************************************/
    protected void setModeAt(String line, int modeIndex) {
        double frequency;
        String name;
        
        frequency   = Double.parseDouble(line.substring(14, 29).trim());
        frequency   = Math.sqrt(frequency) / (2 * Math.PI);
        name        = line.substring(37, 43).trim();
        
        modes_.setModeFrequencyAt(frequency, modeIndex);
        modes_.setModeNameAt(name, modeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Parses the given line and sets the x, y, and z-translations at the given
     * mode and node indices.
     * 
     * @param   line        the line to be parsed
     * @param   modeIndex   the index of the mode
     * @param   nodeIndex   the index of the node
     **************************************************************************/
    protected void setNodeAt(String line, int modeIndex, int nodeIndex) {
        String name;
        double x;
        double y;
        double z;
        
        name    = line.substring(0, 10).trim();
        x       = Double.parseDouble(line.substring(18, 36).trim());
        y       = Double.parseDouble(line.substring(36, 54).trim());
        z       = Double.parseDouble(line.substring(54, 72).trim());
        
        modes_.setNodeNameAt(name, nodeIndex);
        modes_.setXTranslationAt(x, modeIndex, nodeIndex);
        modes_.setYTranslationAt(y, modeIndex, nodeIndex);
        modes_.setZTranslationAt(z, modeIndex, nodeIndex);
    } // eom
} // eoc