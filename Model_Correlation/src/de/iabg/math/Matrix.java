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
 * This interface declares basic functionality that mathematical matrices should
 * have.  It allows for subclasses to have optimized storage schemes and
 * algorithms.  The common way to implement this class is to extend
 * {@link AbstractMatrix}, which provides default implementation for most
 * methods.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 14, 2008
 * @see     AbstractMatrix
 ******************************************************************************/
public interface Matrix {
    
    
    
    /***************************************************************************
     * Removes all data from this matrix.
     **************************************************************************/
    public void clear();
    
    
    
    /***************************************************************************
     * Returns the number of columns in this matrix.
     * 
     * @return  the number of columns
     **************************************************************************/
    public int getColumnCount();
    
    
    
    /***************************************************************************
     * Searches for the first occurance of the given column name.
     * 
     * @param   columnName  a column name
     * @return  the index of the first occurance of the given column name; -1 if
     *          the column name is not found
     **************************************************************************/
    public int getColumnIndexOf(String columnName);
    
    
    
    /***************************************************************************
     * Returns the name of the column at the given index.
     * 
     * @param   columnIndex the column index
     * @return  the name of the column at the given index
     **************************************************************************/
    public String getColumnNameAt(int columnIndex);
    
    
    
    /***************************************************************************
     * Returns an array of all column names.
     * 
     * @return  a string array containing all column names
     **************************************************************************/
    public String[] getColumnNames();
    
    
    
    /***************************************************************************
     * Returns the number of rows in this matrix.
     * 
     * @return  the number of rows
     **************************************************************************/
    public int getRowCount();
    
    
    
    /***************************************************************************
     * Searches for the first occurance of the given row name.
     * 
     * @param   rowName     a row name
     * @return  the index of the first occurance of the given row name; -1 if
     *          the row name is not found
     **************************************************************************/
    public int getRowIndexOf(String rowName);
    
    
    
    /***************************************************************************
     * Returns the name of the row at the given index.
     * 
     * @param   rowIndex    the row index
     * @return  the name of the row at the given index
     **************************************************************************/
    public String getRowNameAt(int rowIndex);
    
    
    
    /***************************************************************************
     * Returns an array of all row names.
     * 
     * @return  a string array containing all row names
     **************************************************************************/
    public String[] getRowNames();
    
    
    
    /***************************************************************************
     * Returns the value at the given row and column indices.
     * 
     * @param   rowIndex    the row index
     * @param   columnIndex the column index
     * @return  the value at the given row and column indices
     **************************************************************************/
    public double getValueAt(int rowIndex, int columnIndex);
    
    
    
    /***************************************************************************
     * Tests if this matrix has no data.
     * 
     * @return  {@code true} if this matrix has no data; {@code false} otherwise
     **************************************************************************/
    public boolean isEmpty();
    
    
    
    /***************************************************************************
     * Returns a string representation of this matrix. This method is intended
     * to be used only for debugging purposes, and the content and format of the
     * returned string may vary between implementations.
     * 
     * @return  a string representation of this matrix
     **************************************************************************/
    public String paramString();
    
    
    
    /***************************************************************************
     * Sets the number of columns to the specified amount.  The data of all
     * cells may be lost due to resizing of the matrix.
     * 
     * @param   nColumns    the new number of columns
     **************************************************************************/
    public void setColumnCount(int nColumns);
    
    
    
    /***************************************************************************
     * Sets the name of the column at the given index.
     * 
     * @param   columnName  the new name of the column
     * @param   columnIndex the index of the column to be changed
     **************************************************************************/
    public void setColumnNameAt(String columnName, int columnIndex);
    
    
    
    /***************************************************************************
     * Sets the number of rows to the specified amount.  The data of all
     * cells may be lost due to resizing of the matrix.
     * 
     * @param   nRows    the new number of rows
     **************************************************************************/
    public void setRowCount(int nRows);
    
    
    
    /***************************************************************************
     * Sets the name of the row at the given index.
     * 
     * @param   rowName     the new name of the row
     * @param   rowIndex    the index of the row to be changed
     **************************************************************************/
    public void setRowNameAt(String rowName, int rowIndex);
    
    
    
    /***************************************************************************
     * Sets the value of the cell at the given row and column indices.
     * 
     * @param   value       the new value of the cell
     * @param   rowIndex    the index of the row to be changed
     * @param   columnIndex the index of the column to be changed
     **************************************************************************/
    public void setValueAt(double value, int rowIndex, int columnIndex);
} // eoi