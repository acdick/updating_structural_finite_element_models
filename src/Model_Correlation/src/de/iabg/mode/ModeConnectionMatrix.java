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
package de.iabg.mode;

import de.iabg.math.DiagonalMatrix;

import de.iabg.swing.KeyList;

import java.util.Arrays;

/*******************************************************************************
 * This {@code Matrix} is designed to store a connection or mapping between
 * two objects of {@code ModeMatrix}.  The naming conventions used in this
 * implementation are {@code First} or {@code First Modes} for the rows and
 * {@code Last} or {@code Last Modes} for the columns.  This implementation
 * takes advantage of the storage scheme of {@link DiagonalMatrix}, which has
 * an efficient method of storing such a mapping.  This class is created instead
 * of using an actual map because it uses primitive values, which can be
 * beneficial for large systems.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public class ModeConnectionMatrix extends DiagonalMatrix {
    /** The {@code First Mode} frequencies of this mode correlation matrix */
    protected double[] firstFrequencies_;
    
    /** The {@code Last Mode} frequencies of this mode correlation matrix */
    protected double[] lastFrequencies_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code ModeConnectionMatrix} with no connections.
     **************************************************************************/
    public ModeConnectionMatrix() {
        this(0);
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a {@code ModeConnectionMatrix} containing the given number of
     * connections.
     * 
     * @param   nConnections  the number of connections in this matrix
     **************************************************************************/
    public ModeConnectionMatrix(int nConnections) {
        super(nConnections);
        
        firstFrequencies_   = new double[nRows_];
        lastFrequencies_    = new double[nColumns_];
    } // eom
    
    
    
    /***************************************************************************
     * Removes the data from this matrix.  This method overrides the existing
     * implementation to also clear the mode frequencies.
     **************************************************************************/
    @Override
    public void clear() {
        nRows_              = 0;
        nColumns_           = 0;
        rowNames_           = new String[nRows_];
        columnNames_        = new String[nColumns_];
        values_             = new double[nRows_];
        firstFrequencies_   = new double[nRows_];
        lastFrequencies_    = new double[nColumns_];
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getRowCount()}.
     * 
     * @return  the number of connections
     **************************************************************************/
    public int getConnectionCount() {
        return this.getRowCount();
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getValueAt(int)}.
     * 
     * @param   connectionIndex the index of the connection
     * @return  the connection at the given index
     **************************************************************************/
    public double getCorrelationAt(int connectionIndex) {
        return this.getValueAt(connectionIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Returns the frequency of the {@code First Mode} at the given index.
     * 
     * @param   modeIndex   the mode index
     * @return  the frequency of the {@code First Mode} at the given index
     **************************************************************************/
    public double getFirstModeFrequencyAt(int modeIndex) {
        return firstFrequencies_[modeIndex];
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getRowNameAt(int)}.
     * 
     * @param   modeIndex   the mode index
     * @return  the name of the {@code First Mode} at the given index
     **************************************************************************/
    public String getFirstModeNameAt(int modeIndex) {
        return this.getRowNameAt(modeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@code First Mode} names as a {@link KeyList}.
     * 
     * @return  a {@code KeyList} containing all {@code First Mode} names
     **************************************************************************/
    public KeyList getFirstModeNames() {
        KeyList firstNames = new KeyList();
        firstNames.addAll(Arrays.asList(rowNames_));
        
        return firstNames;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getColumnNameAt(int)}.
     * 
     * @param   modeIndex   the mode index
     * @return  the name of the {@code Last Mode} at the given index
     **************************************************************************/
    public String getLastModeNameAt(int modeIndex) {
        return this.getColumnNameAt(modeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@code Last Mode} names as a {@link KeyList}.
     * 
     * @return  a {@code KeyList} containing all {@code Lastt Mode} names
     **************************************************************************/
    public KeyList getLastModeNames() {
        KeyList lastNames = new KeyList();
        lastNames.addAll(Arrays.asList(columnNames_));
        
        return lastNames;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public String paramString() {
        StringBuilder result = new StringBuilder();
        
        result.append(String.format(" %-15s", "First Mode"));
        result.append(String.format(" %-15s", "First Frequency"));
        result.append(String.format(" %-15s", "Last Mode"));
        result.append(String.format(" %-15s", "Last Frequency"));
        result.append(String.format(" %-15s", "Correlation"));
        result.append(String.format("%n"));
        
        for (int i = 0; i < nRows_; i++) {
            result.append(String.format(" %15s", rowNames_[i]));
            result.append(String.format(" % 11.8e", firstFrequencies_[i]));
            result.append(String.format(" %15s", columnNames_[i]));
            result.append(String.format(" % 11.8e", lastFrequencies_[i]));
            result.append(String.format(" % 11.8e", values_[i]));
            result.append(String.format("%n"));
        }
        
        return result.toString();
    } // eom
    
    
    
    /***************************************************************************
     * Reduces the size of this matrix by removing the corresponding
     * {@code First Mode}, {@code Last Mode}, and value of any connection which
     * is less than the given tolerance.
     * 
     * @param   tolerance   the minimum connection tolerance
     **************************************************************************/
    public void reduce(double tolerance) {
        String[]    rowNames;
        String[]    columnNames;
        double[]    values;
        double[]    firstFrequencies;
        double[]    lastFrequencies;
        int         dimension       = 0;
        
        for (double value : values_) {
            if (value >= tolerance) {
                dimension++;
            }
        }
        
        rowNames            = new String[dimension];
        columnNames         = new String[dimension];
        values              = new double[dimension];
        firstFrequencies    = new double[dimension];
        lastFrequencies     = new double[dimension];
        dimension           = 0;
        
        for (int i = 0; i < values_.length; i++) {
            if (values_[i] >= tolerance) {
                rowNames[dimension]         = rowNames_[i];
                columnNames[dimension]      = columnNames_[i];
                values[dimension]           = values_[i];
                firstFrequencies[dimension] = firstFrequencies_[i];
                lastFrequencies[dimension]  = lastFrequencies_[i];
                dimension++;
            }
        }
        
        rowNames_           = rowNames;
        columnNames_        = columnNames;
        nRows_              = dimension;
        nColumns_           = dimension;
        values_             = values;
        firstFrequencies_   = firstFrequencies;
        lastFrequencies_    = lastFrequencies;
    } // eom
    
    
    
    /***************************************************************************
     * Sets the number of columns to the specified amount.  This method
     * overrides the existing implementation to also clear the mode frequencies.
     * The data of all cells may be lost due to resizing of the matrix.
     * 
     * @param   nColumns    the new number of columns
     **************************************************************************/
    @Override
    public void setColumnCount(int nColumns) {
        rowNames_           = new String[nColumns];
        columnNames_        = new String[nColumns];
        nRows_              = nColumns;
        nColumns_           = nColumns;
        values_             = new double[nColumns];
        firstFrequencies_   = new double[nColumns];
        lastFrequencies_    = new double[nColumns];
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #setValueAt(double, int)}.
     * 
     * @param   value           the new value of the correlation
     * @param   connectionIndex the index of the connection to be changed
     **************************************************************************/
    public void setCorrelationAt(double value, int connectionIndex) {
        this.setValueAt(value, connectionIndex);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which is equivalent to {@link #setColumnCount(int)}
     * and {@link #setRowCount(int)}.  This method overrides the existing
     * implementation to also clear the mode frequencies.
     * 
     * @param   dimension   the new dimension
     **************************************************************************/
    @Override
    public void setDimensionCount(int dimension) {
        rowNames_           = new String[dimension];
        columnNames_        = new String[dimension];
        nRows_              = dimension;
        nColumns_           = dimension;
        values_             = new double[dimension];
        firstFrequencies_   = new double[dimension];
        lastFrequencies_    = new double[dimension];
    } // eom
    
    
    
    /***************************************************************************
     * Sets the frequency of the {@code First Mode} at the given index.
     * 
     * @param   modeFrequency   the new frequency of the {@code First Mode}
     * @param   modeIndex       the index of the mode to be changed
     **************************************************************************/
    public void setFirstModeFrequencyAt(double modeFrequency, int modeIndex) {
        firstFrequencies_[modeIndex] = modeFrequency;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #setRowNameAt(java.lang.String, int)}.
     * 
     * @param   modeName    the new name of the {@code First Mode}
     * @param   modeIndex   the index of the {@code First Mode} to be changed
     **************************************************************************/
    public void setFirstModeNameAt(String modeName, int modeIndex) {
        this.setRowNameAt(modeName, modeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Sets the frequency of the {@code Last Mode} at the given index.
     * 
     * @param   modeFrequency   the new frequency of the {@code Last Mode}
     * @param   modeIndex       the index of the mode to be changed
     **************************************************************************/
    public void setLastModeFrequencyAt(double modeFrequency, int modeIndex) {
        lastFrequencies_[modeIndex] = modeFrequency;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #setColumnNameAt(java.lang.String, int)}.
     * 
     * @param   modeName    the new name of the {@code Last Mode}
     * @param   modeIndex   the index of the {@code Lasst Mode} to be changed
     **************************************************************************/
    public void setLastModeNameAt(String modeName, int modeIndex) {
        this.setColumnNameAt(modeName, modeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Sets the number of rows to the specified amount.  This method overrides
     * the previous implementation to include the mode frequencies.  The data of
     * all cells may be lost due to resizing of the matrix.
     * 
     * @param   nRows    the new number of rows
     **************************************************************************/
    @Override
    public void setRowCount(int nRows) {
        rowNames_           = new String[nRows];
        columnNames_        = new String[nRows];
        nRows_              = nRows;
        nColumns_           = nRows;
        values_             = new double[nRows];
        firstFrequencies_   = new double[nRows];
        lastFrequencies_    = new double[nRows];
    } // eom
} // eoc