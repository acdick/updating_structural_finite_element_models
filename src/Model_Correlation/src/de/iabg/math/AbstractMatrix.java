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
 * This default implementation of a {@code Matrix} provides basic methods and
 * storage for row and column names.  Storage of the cells is left to subclasses
 * to determine optimal behavior.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public abstract class AbstractMatrix
        implements Matrix {
    /** An array of column names */
    protected String[] columnNames_;
    
    /** The number of columns */
    protected int nColumns_;
    
    /** The number of rows */
    protected int nRows_;
    
    /** An array of row names */
    protected String[] rowNames_;
    
    
    
    /***************************************************************************
     **************************************************************************/
    public int getColumnCount() {
        return nColumns_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public int getColumnIndexOf(String columnName) {
        int index = -1;
        
        for (int i = 0; i < columnNames_.length; i++) {
            if (columnNames_[i].equals(columnName)) {
                index = i;
                break;
            }
        }
        
        return index;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String getColumnNameAt(int columnIndex) {
        return columnNames_[columnIndex];
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String[] getColumnNames() {
        return columnNames_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public int getRowCount() {
        return nRows_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public int getRowIndexOf(String rowName) {
        int index = -1;
        
        for (int i = 0; i < rowNames_.length; i++) {
            if (rowNames_[i].equals(rowName)) {
                index = i;
                break;
            }
        }
        
        return index;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String getRowNameAt(int rowIndex) {
        return rowNames_[rowIndex];
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String[] getRowNames() {
        return rowNames_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean isEmpty() {
        boolean isEmpty = false;
        
        if (nColumns_ == 0) {
            if(nRows_ == 0) {
                isEmpty = true;
            }
        }
        
        return isEmpty;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setColumnNameAt(String columnName, int columnIndex) {
        columnNames_[columnIndex] = columnName.trim();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setRowNameAt(String rowName, int rowIndex) {
        rowNames_[rowIndex] = rowName.trim();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of rows and columns in this matrix.
     * 
     * @return  the string representation of this matrix
     **************************************************************************/
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        
        result.append(String.format("%s", "["));
        result.append(String.format("%d", nRows_));
        result.append(String.format("%s", ", "));
        result.append(String.format("%d", nColumns_));
        result.append(String.format("%s", "]"));
        
        return result.toString();
    } // eom
} // eoc