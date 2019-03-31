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
 * This implementation of {@link ModeReader} parses a Universal Dataset Number
 * 55 file containing mode shape data.  Refer to {@code ModeReader}
 * class API for more details.
 * 
 * The Universal file can contain an undefined number of mode shapes, with each
 * mode shape having a frequency and a name or identifier.  This parser is
 * designed to read in all of this data.  Refer to the latest version of the
 * guide for Universal File Formats for Modal Analysis Testing for more
 * information for the file format.
 * 
 * @author  Adam C. Dick, BSE
 * @version November 10, 2008
 ******************************************************************************/
public class UniversalModeReader extends FileReader
        implements ModeReader {
    /** A list for the data from the file */
    protected ArrayList<String> data_;
    
    /** A constant for dataset lines */
    protected static final int DATASET_FORMAT = 1000;
    
    /** A constant for mode lines */
    protected static final int MODE_FORMAT = 1001;
    
    /** Storage for the modes */
    protected ModeMatrix modes_;
    
    /** A constant for undefined lines */
    protected static final int UNDEFINED_FORMAT = 1099;
    
    
    
    /***************************************************************************
     * Constructs a {@code UniversalModeReader} from the given file.
     * 
     * @param   file                            the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     **************************************************************************/
    public UniversalModeReader(File file)
            throws FileNotFoundException {
        super(file);
    } // eom
    
    
    
    /***************************************************************************
     * Returns the format of the given line.
     * 
     * @param   line    the line to be parsed
     * @return  the line format
     **************************************************************************/
    protected int getFormat(String line) {
        if (line.startsWith("    -1")) {
            return DATASET_FORMAT;
        }
        else if (line.startsWith("    55")) {
            return MODE_FORMAT;
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
        String  line;
        int     nModes = 0;
        
        for (int i = 0; i < data_.size(); i++) {
            line = data_.get(i);
            
            switch (this.getFormat(line)) {
                case DATASET_FORMAT:
                    if (i < data_.size() - 1) {
                        line = data_.get(i + 1);
                    }
                    
                    switch (this.getFormat(line)) {
                        case MODE_FORMAT:
                            nModes++;
                            break;
                        default:
                    }
                    
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
        String  line;
        int     nNodes      = 0;
        int     nFieldLines = 8;
        
        nodeCount:
            for (int i = 0; i < data_.size(); i++) {
                line = data_.get(i);
                
                switch (this.getFormat(line)) {
                    case DATASET_FORMAT:
                        if (i < data_.size() - 1) {
                            line = data_.get(i + 1);
                        }
                        
                        switch (this.getFormat(line)) {
                            case MODE_FORMAT:
                                i += nFieldLines + 2;
                                line = data_.get(i);
                                
                                while (this.getFormat(line) != DATASET_FORMAT) {
                                    nNodes++;
                                    i       += 2;
                                    line    =  data_.get(i);
                                }
                                
                                break nodeCount;
                            default:
                        }
                        
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
        
        int     modeIndex   = -1;
        int     nodeIndex   = 0;
        int     nFieldLines = 8;
        String  line;
        
        for (int i = 0; i < data_.size(); i++) {
            line = data_.get(i);
            
            switch (this.getFormat(line)) {
                case DATASET_FORMAT:
                    if (i < data_.size() - 1) {
                        line = data_.get(i + 1);
                    }
                    
                    switch (this.getFormat(line)) {
                        case MODE_FORMAT:
                            modeIndex++;
                            nodeIndex   =  0;
                            i           += nFieldLines;
                            this.setModeAt(i, modeIndex);
                            
                            i       += 2;
                            line    =  data_.get(i);
                            
                            while (this.getFormat(line) != DATASET_FORMAT) {
                                this.setNodeAt(i, modeIndex, nodeIndex);
                                nodeIndex++;
                                i       += 2;
                                line    =  data_.get(i);
                            }
                            
                            break;
                        default:
                    }
                    
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
     * Parses the line at the given index and sets the mode name and frequency
     * at the given mode index.
     * 
     * @param   lineIndex   the index of the line to be parsed
     * @param   modeIndex   the index of the mode
     **************************************************************************/
    protected void setModeAt(int lineIndex, int modeIndex) {
        double frequency;
        String line;
        String name;
        
        line = data_.get(lineIndex);
        name = line.substring(30, 40).trim();
        
        line        = data_.get(++lineIndex);
        frequency   = Double.parseDouble(line.substring(1, 13).trim());
        
        modes_.setModeNameAt(name, modeIndex);
        modes_.setModeFrequencyAt(frequency, modeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Parses the line at the given index and sets the x, y, and z-translations
     * at the given mode and node indices.
     * 
     * @param   lineIndex   the index of the line to be parsed
     * @param   modeIndex   the index of the mode
     * @param   nodeIndex   the index of the node
     **************************************************************************/
    protected void setNodeAt(int lineIndex, int modeIndex, int nodeIndex) {
        String line;
        String name;
        double x;
        double y;
        double z;
        
        line = data_.get(lineIndex);
        name = line.substring(0, 10).trim();
        
        line    = data_.get(++lineIndex);
        x       = Double.parseDouble(line.substring(1, 13).trim());
        y       = Double.parseDouble(line.substring(14, 26).trim());
        z       = Double.parseDouble(line.substring(27, 39).trim());
        
        modes_.setNodeNameAt(name, nodeIndex);
        modes_.setXTranslationAt(x, modeIndex, nodeIndex);
        modes_.setYTranslationAt(y, modeIndex, nodeIndex);
        modes_.setZTranslationAt(z, modeIndex, nodeIndex);
    } // eom
} // eoc