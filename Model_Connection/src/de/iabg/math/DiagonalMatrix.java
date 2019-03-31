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

import de.iabg.swing.KeyList;

import java.util.Arrays;

/*******************************************************************************
 * This {@code Matrix} is designed as a strictly orthogonal matrix.  This matrix
 * assumes all off-diagonal values are zero and therefore does not store them.
 * The diagonal values are stored in an array, whose size is the dimension of
 * this matrix.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 14, 2008
 ******************************************************************************/
public class DiagonalMatrix extends AbstractMatrix {
    /** An array of diagonal values */
    protected double[] values_;
    
    
    
    /***************************************************************************
     * Constructs a diagonal matrix with the given dimension.
     * 
     * @param   dimension   the dimension of this matrix
     **************************************************************************/
    public DiagonalMatrix(int dimension) {
        super();
        
        rowNames_       = new String[dimension];
        columnNames_    = new String[dimension];
        nRows_          = dimension;
        nColumns_       = dimension;
        values_         = new double[dimension];
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void clear() {
        nRows_          = 0;
        nColumns_       = 0;
        rowNames_       = new String[nRows_];
        columnNames_    = new String[nColumns_];
        values_         = new double[nRows_];
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which calls {@link #getValueAt(int, int)} with the
     * given dimension as the row and column indices.
     * 
     * @param   dimensionIndex  the dimension index
     * @return  the value at the given dimension index
     **************************************************************************/
    public double getValueAt(int dimensionIndex) {
        return this.getValueAt(dimensionIndex, dimensionIndex);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == columnIndex) {
            return values_[rowIndex];
        }
        else {
            throw new ArrayIndexOutOfBoundsException();
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
            
            for (int j = 0; j < nColumns_; j++) {
                if (i == j) {
                    result.append(String.format(" % 11.8e", values_[i]));
                }
                else {
                    result.append(String.format(" % 11.8e", 0.0));
                }
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
        values_         = new double[nColumns];
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which is equivalent to {@link #setColumnCount(int)}
     * and {@link #setRowCount(int)}.
     * 
     * @param   dimension   the new dimension
     **************************************************************************/
    public void setDimensionCount(int dimension) {
        rowNames_       = new String[dimension];
        columnNames_    = new String[dimension];
        nRows_          = dimension;
        nColumns_       = dimension;
        values_         = new double[dimension];
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setRowCount(int nRows) {
        rowNames_       = new String[nRows];
        columnNames_    = new String[nRows];
        nRows_          = nRows;
        nColumns_       = nRows;
        values_         = new double[nRows];
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which calls {@link #setValueAt(double, int)} with
     * the given dimension as the row and column indices.
     * 
     * @param   value           the new value of the cell
     * @param   dimensionIndex  the index of the dimension to be changed
     **************************************************************************/
    public void setValueAt(double value, int dimensionIndex) {
        this.setValueAt(value, dimensionIndex, dimensionIndex);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setValueAt(double value, int rowIndex, int columnIndex) {
        if (rowIndex == columnIndex) {
            values_[rowIndex] = value;
        }
        else {
            throw new ArrayIndexOutOfBoundsException();
        }
    } // eom
    
    
    
    /***************************************************************************
     * Sorts and resizes the number of rows in this matrix according to the
     * order of the given row names.  The number of given row names may be
     * smaller than the number of row names in this matrix, thus reducing the
     * number of rows.  The corresponding cells and columns of the given rows
     * are also sorted in the same way.  The data of any row which is omitted
     * will be lost.
     * 
     * @param   rowKeys  the new order of the row names
     **************************************************************************/
    public void sortRows(KeyList rowKeys) {
        String[]    rowNames;
        String[]    columnNames;
        double[]    values;
        int         nRows           = 0;
        
        for (String row : rowNames_) {
            if (rowKeys.contains(row)) {
                nRows++;
            }
        }
        
        rowNames    = new String[nRows];
        columnNames = new String[nRows];
        values      = new double[nRows];
        nRows       = 0;
        
        for (String rowKey : rowKeys.values()) {
            if (!Arrays.asList(rowNames).contains(rowKey)) {
                for (int i = 0; i < nRows_; i++) {
                    if (rowKey.equals(rowNames_[i])) {
                        rowNames[nRows]     = rowNames_[i];
                        columnNames[nRows]  = columnNames_[i];
                        values[nRows]       = values_[i];
                        nRows++;
                        break;
                    }
                }
            }
        }
        
        rowNames_       = rowNames;
        columnNames_    = columnNames;
        nRows_          = nRows;
        nColumns_       = nRows;
        values_         = values;
    } // eom
} // eoc