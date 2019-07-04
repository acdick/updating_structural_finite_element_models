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
 * This {@code Matrix} is designed as a standard fully populated matrix. This
 * matrix stores all values in a single row-major array, where each subsequent
 * row is appended to the previous.  This structure is used to improve
 * performance over the implementation of standard 2D arrays.  This single array
 * structure can be useful for copying or assigning values, for example, in
 * Java3D.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class RectangularMatrix extends AbstractMatrix {
    /** A single array containing all values */
    protected double[] values_;
    
    
    
    /***************************************************************************
     * Constructs a rectangular matrix of the given size.
     * 
     * @param   nRows       the rows of this matrix
     * @param   nColumns    the columns of this matrix
     **************************************************************************/
    public RectangularMatrix(int nRows, int nColumns) {
        super();
        
        rowNames_       = new String[nRows];
        columnNames_    = new String[nColumns];
        nRows_          = nRows;
        nColumns_       = nColumns;
        values_         = new double[nRows_ * nColumns_];
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void clear() {
        nRows_          = 0;
        nColumns_       = 0;
        rowNames_       = new String[nRows_];
        columnNames_    = new String[nColumns_];
        values_         = new double[nRows_ * nColumns_];
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= nRows_) {
            throw new ArrayIndexOutOfBoundsException(rowIndex);
        }
        
        if (columnIndex < 0 || columnIndex >= nColumns_) {
            throw new ArrayIndexOutOfBoundsException(columnIndex);
        }
        
        return values_[(rowIndex * nColumns_) + columnIndex];
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
                result.append(String.format(" % 11.8e",
                        values_[(i * nColumns_) + j]));
            }
            
            result.append(String.format("%n"));
        }
        
        return result.toString();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setColumnCount(int nColumns) {
        columnNames_    = new String[nColumns];
        nColumns_       = nColumns;
        values_         = new double[nRows_ * nColumns];
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setRowCount(int nRows) {
        rowNames_   = new String[nRows];
        nRows_      = nRows;
        values_     = new double[nRows * nColumns_];
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setValueAt(double value, int rowIndex, int columnIndex) {
        if (rowIndex < 0 || rowIndex >= nRows_) {
            throw new ArrayIndexOutOfBoundsException(rowIndex);
        }
        
        if (columnIndex < 0 || columnIndex >= nColumns_) {
            throw new ArrayIndexOutOfBoundsException(columnIndex);
        }
        
        values_[(rowIndex * nColumns_) + columnIndex] = value;
    } // eom
    
    
    
    /***************************************************************************
     * Sorts and resizes the number of columns in this matrix according to the
     * order of the given column names.  The number of given column names may be
     * smaller than the number of column names in this matrix, thus reducing the
     * number of columns.  The corresponding cells of the given columns are also
     * sorted in the same way.  The data of any column which is omitted will be
     * lost.
     * 
     * @param   columnKeys  the new order of the column names
     **************************************************************************/
    public void sortColumns(KeyList columnKeys) {
        String[]    columnNames;
        double[]    values;
        int         nColumns        = 0;
        int         nValues         = 0;
        
        for (String column : columnNames_) {
            if (columnKeys.contains(column)) {
                nColumns++;
            }
        }
        
        columnNames = new String[nColumns];
        values      = new double[nRows_ * nColumns];
        nColumns    = 0;
        
        for (String columnKey : columnKeys.values()) {
            for (int i = 0; i < nColumns_; i++) {
                if (columnKey.equals(columnNames_[i])) {
                    columnNames[nColumns] = columnNames_[i];
                    nColumns++;
                }
            }
        }
        
        for (int i = 0; i < nRows_; i++) {
            for (String columnKey : columnKeys.values()) {
                for (int j = 0; j < nColumns_; j++) {
                    if (columnKey.equals(columnNames_[j])) {
                        values[nValues] = values_[(i * nColumns_) + j];
                        nValues++;
                    }
                }
            }
        }
        
        columnNames_    = columnNames;
        nColumns_       = nColumns;
        values_         = values;
    } // eom
    
    
    
    /***************************************************************************
     * Sorts and resizes the number of rows in this matrix according to the
     * order of the given row names.  The number of given row names may be
     * smaller than the number of row names in this matrix, thus reducing the
     * number of rows.  The number of given row names may also contain
     * duplicate values, thus increasing the number of rows.  The corresponding
     * cells of the given rows are also sorted in the same way.  The data of any
     * row which is omitted will be lost.
     * 
     * @param   rowKeys  the new order of the row names
     **************************************************************************/
    public void sortRows(KeyList rowKeys) {
        String[]    rowNames;
        double[]    values;
        int         nRows       = 0;
        int         iRow;
        int         nRow;
        
        for (String row : rowKeys.values()) {
            if (Arrays.asList(rowNames_).contains(row)) {
                nRows++;
            }
        }
        
        rowNames    = new String[nRows];
        values      = new double[nRows * nColumns_];
        nRows       = 0;
        
        for (String rowKey : rowKeys.values()) {
            for (int i = 0; i < nRows_; i++) {
                if (rowKey.equals(rowNames_[i])) {
                    rowNames[nRows] = rowNames_[i];
                    iRow            = i * nColumns_;
                    nRow            = nRows * nColumns_;
                    System.arraycopy(values_, iRow, values, nRow, nColumns_);
                    nRows++;
                }
            }
        }
        
        rowNames_   = rowNames;
        nRows_      = nRows;
        values_     = values;
    } // eom
    
    
    
    /***************************************************************************
     * Swaps the data and names of the columns at the given indices.
     * 
     * @param   firstIndex  the first column index
     * @param   lastIndex   the last column index
     **************************************************************************/
    public void swapColumns(int firstIndex, int lastIndex) {
        String  columnName;
        int     rowIndex;
        double  columnValue;
        
        columnName                  = columnNames_[firstIndex];
        columnNames_[firstIndex]    = columnNames_[lastIndex];
        columnNames_[lastIndex]     = columnName;
        
        for (int i = 0; i < nRows_; i++) {
            rowIndex                        = i * nColumns_;
            columnValue                     = values_[rowIndex + firstIndex];
            values_[rowIndex + firstIndex]  = values_[rowIndex + lastIndex];
            values_[rowIndex + lastIndex]   = columnValue;
        }
    } // eom
    
    
    
    /***************************************************************************
     * Swaps the data and names of the rows at the given indices.
     * 
     * @param   firstIndex  the first row index
     * @param   lastIndex   the last row index
     **************************************************************************/
    public void swapRows(int firstIndex, int lastIndex) {
        double[]    row         = new double[nColumns_];
        String      rowName;
        
        rowName                 = rowNames_[firstIndex];
        rowNames_[firstIndex]   = rowNames_[lastIndex];
        rowNames_[lastIndex]    = rowName;
        
        firstIndex  *= nColumns_;
        lastIndex   *= nColumns_;
        
        System.arraycopy(values_, firstIndex, row, 0, nColumns_);
        System.arraycopy(values_, lastIndex, values_, firstIndex, nColumns_);
        System.arraycopy(row, 0, values_, lastIndex, nColumns_);
    } // eom
    
    
    
    /***************************************************************************
     * Returns the values of the cells of this matrix as a single array.
     * 
     * @return  the values of the cells of this matrix
     **************************************************************************/
    public double[] values() {
        return values_;
    } // eom
} // eoc