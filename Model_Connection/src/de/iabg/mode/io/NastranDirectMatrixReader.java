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
import de.iabg.mode.MassMatrixMap;
import de.iabg.mode.MaterialReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

/*******************************************************************************
 * This implementation of {@link MaterialReader} parses a Nastran file
 * containing symmetric matrices representing the mass matrix, stiffness matrix,
 * or other data.  Refer to {@code MaterialReader} class API for more details.
 * 
 * The Nastran file can contain an undefined number of mass or stiffness
 * matrices, with each matrix having a name or identifier.  This parser is
 * designed to read in all of this data, however, non-symmetric matrices such as
 * force vectors, are omitted.  Refer to the latest version of the MSC.Nastran
 * Quick Reference Guide for more information for the file format.
 * 
 * @author  Adam C. Dick, BSE
 * @version Septmeber 13, 2008
 ******************************************************************************/
public class NastranDirectMatrixReader extends FileReader
        implements MaterialReader {
    /** A list for the data from the file */
    protected ArrayList<String> data_;
    
    /** A constant for mass column lines */
    protected static final int MASS_COLUMN_FORMAT = 1001;
    
    /** A constant for mass matrix lines */
    protected static final int MASS_MATRIX_FORMAT = 1000;
    
    /** A constant for mass row lines */
    protected static final int MASS_ROW_FORMAT = 1002;
    
    /** Storage for a mass matrix */
    protected MassMatrix masses_;
    
    /** Storage for the mass matrix map */
    protected MassMatrixMap massMatrices_;
    
    /** The number of components in 3D space */
    protected static final int N_COMPONENTS = 3;
    
    /** A list of the node names */
    protected List<String> nodeNames_;
    
    /** A constant for real double precision matrices */
    protected static final int REAL_DOUBLE_PRECISION = 2;
    
    /** A constant for real single precision matrices */
    protected static final int REAL_SINGLE_PRECISION = 1;
    
    /** A constant for symmetric matrices */
    protected static final int SYMMETRIC_FORMAT = 6;
    
    /** A constant for undefined lines */
    protected static final int UNDEFINED_FORMAT = 1099;
    
    
    
    /***************************************************************************
     * Constructs a {@code NastranDirectMatrixReader} from the given file.
     * 
     * @param   file                            the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     **************************************************************************/
    public NastranDirectMatrixReader(File file)
            throws FileNotFoundException {
        super(file);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a mass column from the line at the given index and adds it to the
     * currently stored mass matrix.
     * 
     * @param   lineIndex   the index of the line to be parsed
     **************************************************************************/
    protected void addMassColumn(int lineIndex) {
        String  line;
        String  columnName;
        String  rowName;
        int     columnIndex;
        int     rowIndex;
        double  massValue;
        
        line        = data_.get(lineIndex);
        columnName  = line.substring(24, 40).trim();
        columnIndex = nodeNames_.indexOf(columnName);
        masses_.setNodeNameAt(columnName, columnIndex);
        
        columnIndex *= N_COMPONENTS;
        columnIndex += Integer.parseInt(line.substring(40, 56).trim()) - 1;
        
        line = line.substring(56);
        while (line.length() > 0) {
            rowName     =  line.substring(0, 16).trim();
            rowIndex    =  nodeNames_.indexOf(rowName) * N_COMPONENTS;
            rowIndex    += Integer.parseInt(line.substring(16, 32).trim()) - 1;
            massValue   =  Double.parseDouble(line.substring(32, 48).trim());
            line        =  line.substring(48);
            masses_.setMassAt(massValue, rowIndex, columnIndex);
        }
    } // eom
    
    
    
    /***************************************************************************
     * Parses a mass matrix from the line at the given index and adds it to the
     * map of mass matrices.
     * 
     * @param   lineIndex   the index of the line to be parsed
     **************************************************************************/
    protected void addMassMatrix(int lineIndex) {
        String  line;
        String  name;
        int     format;
        int     type;
        
        line    = data_.get(lineIndex);
        name    = line.substring(8, 16).trim();
        format  = Integer.parseInt(line.substring(24, 32).trim());
        type    = Integer.parseInt(line.substring(32, 40).trim());
        masses_ = new MassMatrix(this.getNodeCount(++lineIndex));
        
        switch (format) {
            case SYMMETRIC_FORMAT:
                switch (type) {
                    case REAL_SINGLE_PRECISION:
                    case REAL_DOUBLE_PRECISION:
                        for (int i = lineIndex; i < data_.size(); i++) {
                            line = data_.get(i);
                            
                            if (this.getFormat(line) == MASS_COLUMN_FORMAT) {
                                this.addMassColumn(i);
                            }
                            else {
                                break;
                            }
                        }
                        
                        massMatrices_.put(name, masses_);
                        break;
                    default:
                }
                
                break;
            default:
        }
    } // eom
    
    
    
    /***************************************************************************
     * Returns the format of the given line.
     * 
     * @param   line    the line to be parsed
     * @return  the line format
     **************************************************************************/
    protected int getFormat(String line) {
        if (line.toUpperCase().startsWith("DMIG ")) {
            return MASS_MATRIX_FORMAT;
        }
        else if (line.toUpperCase().startsWith("DMIG*")) {
            return MASS_COLUMN_FORMAT;
        }
        else if (line.startsWith("*")) {
            return MASS_ROW_FORMAT;
        }
        else {
            return UNDEFINED_FORMAT;
        }
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of nodes in the matrix, beginning at the index of the
     * give line.
     * 
     * @param   lineIndex   the first index of the line to be parsed
     * @return  the number of nodes
     **************************************************************************/
    protected int getNodeCount(int lineIndex) {
        nodeNames_ = new ArrayList<String>();
        
        String line;
        String columnName;
        
        for (int i = lineIndex; i < data_.size(); i++) {
            line = data_.get(i);
            
            if (this.getFormat(line) == MASS_COLUMN_FORMAT) {
                columnName = line.substring(24, 40).trim();
                if (!nodeNames_.contains(columnName)) {
                    nodeNames_.add(columnName);
                }
            }
            else {
                break;
            }
        }
        
        return nodeNames_.size();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importMassMatrices(MassMatrixMap massMatrices) {
        massMatrices_ = massMatrices;
        massMatrices_.clear();
        
        String line;
        
        for (int i = 0; i < data_.size(); i++) {
            line = data_.get(i);
            
            switch (this.getFormat(line)) {
                case MASS_MATRIX_FORMAT:
                    this.addMassMatrix(i);
                    break;
                case MASS_COLUMN_FORMAT:
                case MASS_ROW_FORMAT:
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
        StringBuilder   commandLine;
        
        try {
            bufferedReader = new BufferedReader(this);
            
            line = bufferedReader.readLine();
            while (line != null) {
                switch (this.getFormat(line)) {
                    case MASS_ROW_FORMAT:
                        commandLine = new StringBuilder();
                        commandLine.append(data_.get(data_.size() - 1));
                        commandLine.append(line.replace('D', 'E').substring(8));
                        data_.set(data_.size() - 1, commandLine.toString());
                        break;
                    case MASS_COLUMN_FORMAT:
                    case MASS_MATRIX_FORMAT:
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