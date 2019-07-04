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
package de.iabg.math;

/*******************************************************************************
 * This {@code Matrix} is designed as a mathematically symmetric matrix.  This
 * matrix stores only half of the values of this matrix in a lower triangular
 * array of arrays.  The values of the upper triangle can be accessed by
 * transposing their indices.  This structure is used to reduce storage
 * requirements for large symmetric arrays.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class SymmetricMatrix extends AbstractMatrix {
    /** A triangular array of arrays containing all values */
    protected double[][] values_;
    
    
    
    /***************************************************************************
     * Constructs a symmetric matrix of the given size.
     * 
     * @param   dimension   the dimension of this matrix
     **************************************************************************/
    public SymmetricMatrix(int dimension) {
        super();
        
        rowNames_       = new String[dimension];
        columnNames_    = new String[dimension];
        nRows_          = dimension;
        nColumns_       = dimension;
        values_         = new double[nRows_][];
        
        for (int i = 0; i < nRows_; i++) {
            values_[i] = new double[i + 1];
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void clear() {
        nRows_          = 0;
        nColumns_       = 0;
        rowNames_       = new String[nRows_];
        columnNames_    = new String[nColumns_];
        values_         = new double[nRows_][];
        
        for (int i = 0; i < nRows_; i++) {
            values_[i] = new double[i + 1];
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex > columnIndex) {
            return values_[rowIndex][columnIndex];
        }
        else {
            return values_[columnIndex][rowIndex];
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String paramString() {
        StringBuilder result = new StringBuilder();
        
        result.append(String.format(" %15s", ""));
        
        for (String columnName : columnNames_) {
            result.append(String.format(" %15s", columnName));
        }
        
        result.append(String.format("%n"));
        
        for (int i = 0; i < nRows_; i++) {
            result.append(String.format(" %15s", rowNames_[i]));
            
            for (int j = 0; j < i; j++) {
                result.append(String.format(" % 11.8e", values_[i][j]));
            }
            
            for (int j = i; j < nColumns_; j++) {
                result.append(String.format(" % 11.8e", values_[j][i]));
            }
            
            result.append(String.format("%n"));
        }
        
        return result.toString();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setColumnCount(int nColumns) {
        rowNames_       = new String[nColumns];
        columnNames_    = new String[nColumns];
        nRows_          = nColumns;
        nColumns_       = nColumns;
        values_         = new double[nRows_][];
        
        for (int i = 0; i < nRows_; i++) {
            values_[i] = new double[i + 1];
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setRowCount(int nRows) {
        rowNames_       = new String[nRows];
        columnNames_    = new String[nRows];
        nRows_          = nRows;
        nColumns_       = nRows;
        values_         = new double[nRows_][];
        
        for (int i = 0; i < nRows_; i++) {
            values_[i] = new double[i + 1];
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setValueAt(double value, int rowIndex, int columnIndex) {
        if (rowIndex > columnIndex) {
            values_[rowIndex][columnIndex] = value;
        }
        else {
            values_[columnIndex][rowIndex] = value;
        }
    } // eom
    
    
    
    /***************************************************************************
     * Returns the values of the cells of this matrix as a triangular array.
     * 
     * @return  the values of the cells of this matrix
     **************************************************************************/
    public double[][] values() {
        return values_;
    } // eom
} // eoc