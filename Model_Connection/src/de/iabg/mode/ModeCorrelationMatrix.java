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

import de.iabg.math.RectangularMatrix;

import de.iabg.swing.KeyList;

import java.util.Arrays;

import javax.media.j3d.BranchGroup;

import javax.vecmath.Color3f;

/*******************************************************************************
 * This {@code Matrix} is designed to store the values of a correlation between
 * all modes of two objects of {@link ModeMatrix}.  The naming conventions used
 * in this implementation are {@code First} or {@code First Modes} for the rows
 * and {@code Last} or {@code Last Modes} for the columns.
 * 
 * This class also provides the method {@link #getPreferredConnection(double)},
 * which returns a {@link ModeConnectionMatrix}.  The returned matrix is a
 * mapping between the {@code First Nodes} and {@code Last Nodes} based on the
 * maximum correlation.  This mapping is determined by searching this matrix
 * for the maximum correlation between any two modes and swapping the
 * corresponding row and column with the first row and column.  The first row
 * and column are omitted from the next search, which searches the remaining
 * nodes for the maximum correlation.  The corresponding row and column are
 * swapped with the second row and column.  This procedure is done iteratively
 * until the given minimum correlation tolerance is reached.  Thus, a mapping
 * based on the maximum correlation is formed along the diagonal, which is
 * ultimately returned to the user.  This procedure is robust in that it will
 * find the best mapping for any ordering of the stored modes.  The algorithm,
 * however, requires significant memory storage and may not properly execute
 * for large systems.
 * 
 * The 2D matrix that this correlation represents can also be rendered in Java3D
 * by calling {@link #getScene2D(double, javax.vecmath.Color3f)} or
 * {@link #getScene3D(double, javax.vecmath.Color3f)}.  The color of a cell is
 * green if it is less than the lower tolerance, yellow if it between the lower
 * and upper tolerances, and red if it is greater than the upper tolerance.
 * These tolerance values can be set to customize the rendering.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public class ModeCorrelationMatrix extends RectangularMatrix {
    /** The {@code First Mode} frequencies of this mode correlation matrix */
    protected double[] firstFrequencies_;
    
    /** The {@code First Mode} name */
    protected String firstName_;
    
    /** The {@code Last Mode} frequencies of this mode correlation matrix */
    protected double[] lastFrequencies_;
    
    /** The {@code Last Mode} name */
    protected String lastName_;
    
    /** The lower tolerance of the cell values */
    protected double lowerTolerance_;
    
    /** The upper tolerance of teh cell values */
    protected double upperTolerance_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code ModeMatrix} with no modes or nodes.
     **************************************************************************/
    public ModeCorrelationMatrix() {
        this(0, 0);
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a {@code ModeCorrelationMatrix} containing the given number of
     * {@code First Modes} as the rows and {@code Last Modes} as the columns.
     * 
     * @param   nFirstModes the number of {@code First Modes} in this matrix
     * @param   nLastModes  the number of {@code Last Modes} in this matrix
     **************************************************************************/
    public ModeCorrelationMatrix(int nFirstModes, int nLastModes) {
        super(nFirstModes, nLastModes);
        
        firstFrequencies_   = new double[nRows_];
        lastFrequencies_    = new double[nColumns_];
        lowerTolerance_     = 0.05;
        upperTolerance_     = 0.95;
        firstName_          = "Reference";
        lastName_           = "Update";
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a copy of the given {@code ModeCorrelationMatrix}.
     * 
     * @param   correlation the {@code ModeCorrelationMatrix} to be copied
     **************************************************************************/
    public ModeCorrelationMatrix(ModeCorrelationMatrix correlation) {
        this(correlation.nRows_, correlation.nColumns_);
        
        System.arraycopy(correlation.rowNames_, 0, rowNames_, 0, nRows_);
        System.arraycopy(correlation.columnNames_, 0, columnNames_, 0,
                nColumns_);
        System.arraycopy(correlation.values_, 0, values_, 0,
                nRows_ * nColumns_);
        System.arraycopy(correlation.firstFrequencies_, 0, firstFrequencies_, 0,
                nRows_);
        System.arraycopy(correlation.lastFrequencies_, 0, lastFrequencies_, 0,
                nColumns_);
        
        lowerTolerance_ = correlation.lowerTolerance_;
        upperTolerance_ = correlation.upperTolerance_;
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
        values_             = new double[nRows_ * nColumns_];
        firstFrequencies_   = new double[nRows_];
        lastFrequencies_    = new double[nColumns_];
        lowerTolerance_     = 0.0;
        upperTolerance_     = 1.0;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the 2D distance of the furthest cell from the origin.  The 2D
     * distance is calculated from only the x and y coordinates.
     * 
     * @return  the 2D distance of the furthest cell
     **************************************************************************/
    public double getBoundingRadius2D() {
        double boundingRadius   = 0.0;
        double radius;
        double dx;
        double dy;
        
        for (int i = 0; i < nRows_; i++) {
            for (int j = 0; j < nColumns_; j++) {
                dx = i + 1;
                dy = j + 1;
                
                radius          = Math.sqrt((dx * dx) + (dy * dy));
                boundingRadius  = Math.max(boundingRadius, radius);
            }
        }
        
        return boundingRadius;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the 3D distance of the furthest cell from the origin.
     * 
     * @return  the 3D distance of the furthest cell
     **************************************************************************/
    public double getBoundingRadius3D() {
        double boundingRadius   = 0.0;
        double radius;
        double dx;
        double dy;
        double dz;
        
        for (int i = 0; i < nRows_; i++) {
            for (int j = 0; j < nColumns_; j++) {
                dx = i + 1;
                dy = j + 1;
                dz = values_[(i * nColumns_) + j];
                
                radius          = Math.sqrt((dx * dx) + (dy * dy) + (dz * dz));
                boundingRadius  = Math.max(boundingRadius, radius);
            }
        }
        
        return boundingRadius;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the diagonal values of this matrix as a new
     * {@code ModeConnectionMatrix}.  This method simply copies the names and
     * frequencies of the {@code First Modes} and {@code Last Modes} and the
     * correlation along the diagonal into the {@code ModeConnectionMatrix}.
     * Therefore, this method does not guarantee anything about the quality of
     * the mapping.
     * 
     * @return the connection of the modes along the diagonal
     **************************************************************************/
    public ModeConnectionMatrix getConnection() {
        ModeConnectionMatrix    connection;
        double                  value;
        int                     dimension   = Math.min(nRows_, nColumns_);
        
        connection = new ModeConnectionMatrix(dimension);
        
        for (int i = 0; i < dimension; i++) {
            if (rowNames_[i] != null) {
                connection.setFirstModeNameAt(rowNames_[i], i);
            }
            
            if (columnNames_[i] != null) {
                connection.setLastModeNameAt(columnNames_[i], i);
            }
            
            value = values_[(i * nColumns_) + i];
            connection.setCorrelationAt(value, i);
            connection.setFirstModeFrequencyAt(firstFrequencies_[i], i);
            connection.setLastModeFrequencyAt(lastFrequencies_[i], i);
        }
        
        return connection;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getValueAt(int, int)}.
     * 
     * @param   firstIndex  the {@code First Mode} index
     * @param   lastIndex   the {@code Last Mode} index
     * @return  the value of the correlation at the given indices
     **************************************************************************/
    public double getCorrelationAt(int firstIndex, int lastIndex) {
        return this.getValueAt(firstIndex, lastIndex);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getRowCount()}.
     * 
     * @return  the number of {@code First Modes}
     **************************************************************************/
    public int getFirstModeCount() {
        return this.getRowCount();
    } // eom
    
    
    
    /***************************************************************************
     * Returns an array of all {@code First Mode} frequencies.
     * 
     * @return  an array containing all {@code First Mode} frequencies
     **************************************************************************/
    public double[] getFirstModeFrequencies() {
        return firstFrequencies_;
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
     * A convenience method which wraps {@link #getRowNames()}.
     * 
     * @return  a string array containing all {@code First Mode} names
     **************************************************************************/
    public String[] getFirstModeNames() {
        return this.getRowNames();
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getColumnCount()}.
     * 
     * @return  the number of {@code Last Modes}
     **************************************************************************/
    public int getLastModeCount() {
        return this.getColumnCount();
    } // eom
    
    
    
    /***************************************************************************
     * Returns an array of all {@code Last Mode} frequencies.
     * 
     * @return  an array containing all {@code Last Mode} frequencies
     **************************************************************************/
    public double[] getLastModeFrequencies() {
        return lastFrequencies_;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the frequency of the {@code Last Mode} at the given index.
     * 
     * @param   modeIndex   the mode index
     * @return  the frequency of the {@code Last Mode} at the given index
     **************************************************************************/
    public double getLastModeFrequencyAt(int modeIndex) {
        return lastFrequencies_[modeIndex];
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #getColumnIndexOf(java.lang.String)}.
     * 
     * @param   modeName    a mode name
     * @return  the index of the first occurance of the given mode name; -1 if
     *          the mode name is not found
     **************************************************************************/
    public int getLastModeIndexOf(String modeName) {
        return this.getColumnIndexOf(modeName);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getColumnNames()}.
     * 
     * @return  a string array containing all {@code Last Mode} names
     **************************************************************************/
    public String[] getLastModeNames() {
        return this.getColumnNames();
    } // eom
    
    
    
    /***************************************************************************
     * Returns a mapping between the {@code First Modes} and {@code Last Modes}
     * based on the maximum correlation.  This method makes a copy of this class
     * and calls {@link #sortDiagonalDescending()} with the given tolerance.  A
     * {@code ModeConnectionMatrix} is then created from the sorted correlation
     * and reduced to the given tolerance.
     * 
     * @param   tolerance   the minimum correlation tolerance
     * @return  the connection of the modes based on a maximum correlation
     **************************************************************************/
    public ModeConnectionMatrix getPreferredConnection(double tolerance) {
        ModeConnectionMatrix    connection;
        ModeCorrelationMatrix   correlation;
        
        correlation = new ModeCorrelationMatrix(this);
        correlation.sortDiagonalDescending();
        
        connection = correlation.getConnection();
        connection.reduce(tolerance);
        
        return connection;
    } // eom
    
    
    
    /***************************************************************************
     * Returns a new {@link ModeCorrelationScene2D} with the given scale and
     * background color.
     * 
     * @param   scale           the scale of the scene
     * @param   backgroundColor the background color of the scene
     * @return  a branch group containing the background and 2D matrix that
     *          this correlation represents
     **************************************************************************/
    public BranchGroup getScene2D(double scale, Color3f backgroundColor) {
        return new ModeCorrelationScene2D(this, scale, backgroundColor,
                lowerTolerance_, upperTolerance_, firstName_, lastName_);
    } // eom
    
    
    
    /***************************************************************************
     * Returns a new {@link ModeCorrelationScene3D} with the given scale and
     * background color.
     * 
     * @param   scale           the scale of the scene
     * @param   backgroundColor the background color of the scene
     * @return  a branch group containing the background and 3D matrix that
     *          this correlation represents
     **************************************************************************/
    public BranchGroup getScene3D(double scale, Color3f backgroundColor) {
        return new ModeCorrelationScene3D(this, scale, backgroundColor,
                lowerTolerance_, upperTolerance_, firstName_, lastName_);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public String paramString() {
        StringBuilder result = new StringBuilder();
        
        result.append(String.format(" %15s", ""));
        result.append(String.format(" %15s", "Mode"));
        
        for (String columnName : columnNames_) {
            result.append(String.format(" %15s", columnName));
        }
        
        result.append(String.format("%n"));
        result.append(String.format(" %15s", "Mode"));
        result.append(String.format(" %15s", "Frequency"));
        
        for (double frequency : lastFrequencies_) {
            result.append(String.format(" % 11.8e", frequency));
        }
        
        result.append(String.format("%n"));
        
        for (int i = 0; i < nRows_; i++) {
            result.append(String.format(" %15s", rowNames_[i]));
            result.append(String.format(" % 11.8e", firstFrequencies_[i]));
            
            for (int j = 0; j < nColumns_; j++) {
                result.append(String.format(" % 11.8e",
                        values_[(i * nColumns_) + j]));
            }
            
            result.append(String.format("%n"));
        }
        
        return result.toString();
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
        columnNames_        = new String[nColumns];
        nColumns_           = nColumns;
        values_             = new double[nRows_ * nColumns];
        lastFrequencies_    = new double[nColumns];
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #setValueAt(double, int, int)}.
     * 
     * @param   value       the new value of the correlation
     * @param   firstIndex  the index of the {@code First Mode} to be changed
     * @param   lastIndex   the index of the {@code Last Mode} to be changed
     **************************************************************************/
    public void setCorrelationAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, firstIndex, lastIndex);
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
     * Replaces the name of the {@code First Mode} with the given one.
     * 
     * @param   firstName   the new name of the {@code First Mode}
     **************************************************************************/
    public void setFirstName(String firstName) {
        firstName_ = firstName.trim();
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
     * Replaces the name of the {@code Last Mode} with the given one.
     * 
     * @param   lastName   the new name of the {@code Last Mode}
     **************************************************************************/
    public void setLastName(String lastName) {
        lastName_ = lastName.trim();
    } // eom
    
    
    
    /***************************************************************************
     * Sets the value of the lower tolerance to the given value.  The lower
     * tolerance cannot be greater than the upper tolerance.
     * 
     * @param   lowerTolerance  the new lower tolerance
     **************************************************************************/
    public void setLowerTolerance(double lowerTolerance) {
        if (lowerTolerance > upperTolerance_) {
            throw new IllegalArgumentException();
        }
        
        lowerTolerance_ = lowerTolerance;
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
        nRows_              = nRows;
        values_             = new double[nRows * nColumns_];
        firstFrequencies_   = new double[nRows];
    } // eom
    
    
    
    /***************************************************************************
     * Sets the value of the upper tolerance to the given value.  The upper
     * tolerance cannot be less than the lower tolerance.
     * 
     * @param   upperTolerance  the new upper tolerance
     **************************************************************************/
    public void setUpperTolerance(double upperTolerance) {
        if (upperTolerance < lowerTolerance_) {
            throw new IllegalArgumentException();
        }
        
        upperTolerance_ = upperTolerance;
    } // eom
    
    
    
    /***************************************************************************
     * Sorts and resizes the number of columns in this matrix according to the
     * order of the given column names.  This method overrides the previous
     * implementation to include the mode frequencies.  The number of given
     * column names may be smaller than the number of column names in this
     * matrix, thus reducing the number of columns.  The corresponding cells of
     * the given columns are also sorted in the same way.  The data of any
     * column which is omitted will be lost.
     * 
     * @param   columnKeys  the new order of the column names
     **************************************************************************/
    @Override
    public void sortColumns(KeyList columnKeys) {
        String[]    columnNames;
        double[]    values;
        double[]    lastFrequencies;
        int         nColumns            = 0;
        int         nValues             = 0;
        
        for (String column : columnNames_) {
            if (columnKeys.contains(column)) {
                nColumns++;
            }
        }
        
        columnNames     = new String[nColumns];
        values          = new double[nRows_ * nColumns];
        lastFrequencies = new double[nColumns];
        nColumns        = 0;
        
        for (String columnKey : columnKeys.values()) {
            for (int i = 0; i < nColumns_; i++) {
                if (columnKey.equals(columnNames_[i])) {
                    columnNames[nColumns]       = columnNames_[i];
                    lastFrequencies[nColumns]   = lastFrequencies_[i];
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
        
        columnNames_        = columnNames;
        nColumns_           = nColumns;
        values_             = values;
        lastFrequencies_    = lastFrequencies;
    } // eom
    
    
    
    /***************************************************************************
     * This method iteratively searches this matrix for the maximum correlation
     * mapping and places them along the diagonal.  The algorithm begins by
     * searching the entire matrix for the maximum value.  The corresponding row
     * and column are swapped into the first row and first column.  The next
     * search omits the row and column and searches the matrix for the next
     * largest value.  The corresponding row and column are swapped into the
     * second row and second column.  The next search proceeds by omitting the
     * first two rows and first two columns.  Thus, the maximum mapping between
     * the {@code First Modes} and {@code Last Modes} is built along the
     * diagonal.
     **************************************************************************/
    protected void sortDiagonalDescending() {
        double  maximumValue;
        double  value;
        int     rowIndex;
        int     columnIndex;
        int     dimension       = Math.min(nRows_, nColumns_);
        
        for (int i = 0; i < dimension; i++) {
            maximumValue    = values_[(i * nColumns_) + i];
            rowIndex        = i;
            columnIndex     = i;
            
            for (int j = i; j < nRows_; j++) {
                for (int k = i; k < nColumns_; k++) {
                    value = values_[(j * nColumns_) + k];
                    
                    if (value > maximumValue) {
                        maximumValue    = value;
                        rowIndex        = j;
                        columnIndex     = k;
                    }
                }
            }
            
            this.swapRows(i, rowIndex);
            this.swapColumns(i, columnIndex);
        }
    } // eom
    
    
    
    /***************************************************************************
     * Sorts and resizes the number of rows in this matrix according to the
     * order of the given row names.  This method overrides the previous
     * implementation to include the mode frequencies.  The number of given row
     * names may be smaller than the number of row names in this matrix, thus
     * reducing the number of rows.  The number of given row names may also
     * contain duplicate values, thus increasing the number of rows.  The
     * corresponding cells of the given rows are also sorted in the same way.
     * The data of any row which is omitted will be lost.
     * 
     * @param   rowKeys  the new order of the row names
     **************************************************************************/
    @Override
    public void sortRows(KeyList rowKeys) {
        String[]    rowNames;
        double[]    values;
        double[]    firstFrequencies;
        int         nRows               = 0;
        int         iRow;
        int         nRow;
        
        for (String row : rowKeys.values()) {
            if (Arrays.asList(rowNames_).contains(row)) {
                nRows++;
            }
        }
        
        rowNames            = new String[nRows];
        values              = new double[nRows * nColumns_];
        firstFrequencies    = new double[nRows];
        nRows               = 0;
        
        for (String rowKey : rowKeys.values()) {
            for (int i = 0; i < nRows_; i++) {
                if (rowKey.equals(rowNames_[i])) {
                    rowNames[nRows]         = rowNames_[i];
                    firstFrequencies[nRows] = firstFrequencies_[i];
                    iRow                    = i * nColumns_;
                    nRow                    = nRows * nColumns_;
                    System.arraycopy(values_, iRow, values, nRow, nColumns_);
                    nRows++;
                }
            }
        }
        
        rowNames_           = rowNames;
        nRows_              = nRows;
        values_             = values;
        firstFrequencies_   = firstFrequencies;
    } // eom
    
    
    
    /***************************************************************************
     * Swaps the data and names of the columns at the given indices.  This
     * method overrides the previous implementation to include the mode
     * frequencies.
     * 
     * @param   firstIndex  the first column index
     * @param   lastIndex   the last column index
     **************************************************************************/
    @Override
    public void swapColumns(int firstIndex, int lastIndex) {
        String  columnName;
        int     rowIndex;
        double  columnValue;
        double  lastFrequency;
        
        columnName                  = columnNames_[firstIndex];
        columnNames_[firstIndex]    = columnNames_[lastIndex];
        columnNames_[lastIndex]     = columnName;
        
        lastFrequency                   = lastFrequencies_[firstIndex];
        lastFrequencies_[firstIndex]    = lastFrequencies_[lastIndex];
        lastFrequencies_[lastIndex]     = lastFrequency;
        
        for (int i = 0; i < nRows_; i++) {
            rowIndex                        = i * nColumns_;
            columnValue                     = values_[rowIndex + firstIndex];
            values_[rowIndex + firstIndex]  = values_[rowIndex + lastIndex];
            values_[rowIndex + lastIndex]   = columnValue;
        }
    } // eom
    
    
    
    /***************************************************************************
     * Swaps the data and names of the rows at the given indices.  This method
     * overrides the previous implementation to include the mode frequencies.
     * 
     * @param   firstIndex  the first row index
     * @param   lastIndex   the last row index
     **************************************************************************/
    @Override
    public void swapRows(int firstIndex, int lastIndex) {
        double[]    row             = new double[nColumns_];
        String      rowName;
        double      firstFrequency;
        
        rowName                 = rowNames_[firstIndex];
        rowNames_[firstIndex]   = rowNames_[lastIndex];
        rowNames_[lastIndex]    = rowName;
        
        firstFrequency                  = firstFrequencies_[firstIndex];
        firstFrequencies_[firstIndex]   = firstFrequencies_[lastIndex];
        firstFrequencies_[lastIndex]    = firstFrequency;
        
        firstIndex  *= nColumns_;
        lastIndex   *= nColumns_;
        
        System.arraycopy(values_, firstIndex, row, 0, nColumns_);
        System.arraycopy(values_, lastIndex, values_, firstIndex, nColumns_);
        System.arraycopy(row, 0, values_, lastIndex, nColumns_);
    } // eom
} // eoc