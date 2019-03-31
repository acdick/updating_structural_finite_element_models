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

import de.iabg.swing.KeyList;

import java.util.Arrays;

import javax.media.j3d.BranchGroup;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import javax.vecmath.Color3f;

/*******************************************************************************
 * This implementation of a {@link javax.swing.table.TableModel} provides a
 * table representation of a {@link ModeCorrelationMatrix}.  This class bridges
 * the connection between a {@code ModeCorrelationMatrix} and a
 * {@code ModeCorrelationModel} by simply adding a table representation.
 * Therefore, some of the methods of this class wrap those in
 * {@code ModeCorrelationMatrix}, while other methods are wrapped in
 * {@code ModeCorrelationModel}.  Most of the descriptions of the methods in
 * this class can be found by refering to the API of either of those two
 * classes.  The remaining methods generally implement or override those
 * specified in {@code TableModel}.  Refer to the API or tutorial for using
 * {@code Table Models} for more details.
 * 
 * This {@code TableModel} provides nine columns to represent a
 * {@code ModeCorrelationMatrix}:
 * 
 * The first is called {@code Selected} or {@code On}, which selects the
 * corresponding mode connection.  Selecting or deselecting this option can
 * allow specified mode connections to be output to a file.
 * 
 * The second column is called {@code Mode 1} or {@code First Mode}, which
 * displays the identifier of the {@code First Mode}.
 * 
 * The third column is called {@code Mode Weighting} or {@code WFM}, which
 * displays the correlation weighting factor of the corresponding mode
 * connection.  This value can be edited by the user.
 * 
 * The fourth column is called {@code Freq 1} or {@code First Frequency}, which
 * displays the frequency of the {@code First Mode}.
 * 
 * The fifth column is called {@code Frequency Weighting} or {@code WFF}, which
 * displays the frequency weighting factor of the corresponding mode connection.
 * 
 * The sixth column is called {@code Mode 2} or {@code Last Mode}, which
 * displays the identifier of the connected {@code Last Mode}.  The default
 * value for this comes from the preferred connection, however, the user may
 * change this to any other mode.
 * 
 * The seventh column is called {@code Freq 2} or {@code Last Frequency}, which
 * displays the frequency of the {@code Last Mode}.  This value is automatically
 * updated when the user changes the {@code Last Mode}.
 * 
 * The eighth column is called {@code Correlation} or simply {@code Value},
 * which displays the corresponding correlation value between the selected
 * {@code First Mode} and {@code Last Mode}.  This value is automatically
 * updated when the user changes the {@code Last Mode}.
 * 
 * The ninth column is called {@code Frequency Difference} or {@code dFreq},
 * which displays the frequency difference between the selected
 * {@code First Mode} and {@code Last Mode}.  This value is automatically
 * updated when the user changes the {@code Last Mode}.
 * 
 * The rows of this table reflect the total number of mode connections that are
 * currently stored.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 14, 2008
 ******************************************************************************/
public class ModeCorrelationTableModel extends AbstractTableModel
        implements TableModel {
    /** The {@code ModeCorrelationMatrix} of this {@code TableModel} */
    protected ModeCorrelationMatrix correlation_;
    
    /** The column index of the correlation between the corresponding modes */
    protected static final int CORRELATION_INDEX = 7;
    
    /** The column index of the {@code First Mode} frequency */
    protected static final int FIRST_MODE_FREQUENCY_INDEX = 3;
    
    /** The column index of the {@code First Mode} name */
    protected static final int FIRST_MODE_NAME_INDEX = 1;
    
    /** The column index of the {@code First Mode} selection */
    protected static final int FIRST_MODE_SELECTED_INDEX = 0;
    
    /** An array of the selected {@code First Mode} keys */
    protected boolean[] firstSelectedModeKeys_;
    
    /** The column index of the frequency between the corresponding modes */
    protected static final int FREQUENCY_DIFFERENCE_INDEX = 8;
    
    /** The column index of the frequency weightings */
    protected static final int FREQUENCY_WEIGHTING_INDEX = 4;
    
    /** An array of the selected mode frequency weightings */
    protected double[] frequencyWeightings_;
    
    /** The column index of the {@code Last Mode} frequency */
    protected static final int LAST_MODE_FREQUENCY_INDEX = 6;
    
    /** The column index of the {@code Last Mode} name */
    protected static final int LAST_MODE_NAME_INDEX = 5;
    
    /** A {@code KeyList} of all stored {@code Last Mode} names */
    protected KeyList lastModeNames_;
    
    /** An array of the selected {@code Last Mode} keys */
    protected String[] lastSelectedModeKeys_;
    
    /** The column index of the mode correlation weightings */
    protected static final int MODE_WEIGHTING_INDEX = 2;
    
    /** An array of the selected mode correlation weightings */
    protected double[] modeWeightings_;
    
    /** The number of table columns */
    protected static final int N_COLUMNS = 9;
    
    
    
    /***************************************************************************
     * Constructs a default {@code ModeCorrelationTableModel} with no mode
     * correlations.
     **************************************************************************/
    public ModeCorrelationTableModel() {
        super();
        
        correlation_    = new ModeCorrelationMatrix();
        lastModeNames_  = new KeyList();
    } // eom
    
    
    
    /***************************************************************************
     * Deselects all currently stored {@code First Modes}.
     **************************************************************************/
    public void deselectAllFirstModeKeys() {
        if (!correlation_.isEmpty()) {
            for (int i = 0; i < firstSelectedModeKeys_.length; i++) {
                firstSelectedModeKeys_[i] = false;
            }
            
            this.fireTableDataChanged();
        }
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link ModeCorrelationMatrix#getBoundingRadius2D()}.
     * 
     * @return  the 2D distance of the furthest cell
     **************************************************************************/
    public double getBoundingRadius2D() {
        return correlation_.getBoundingRadius2D();
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link ModeCorrelationMatrix#getBoundingRadius3D()}.
     * 
     * @return  the 3D distance of the furthest cell
     **************************************************************************/
    public double getBoundingRadius3D() {
        return correlation_.getBoundingRadius3D();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public Class getColumnClass(int columnIndex) {
        return this.getValueAt(0, columnIndex).getClass();
    } // eom
    
    
    
    /***************************************************************************
     ***************************************************************************/
    public int getColumnCount() {
        return N_COLUMNS;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public String getColumnName(int columnIndex) {
        String columnName;
        
        switch (columnIndex) {
            case FIRST_MODE_SELECTED_INDEX:
                columnName = new String("On");
                break;
            case FIRST_MODE_NAME_INDEX:
                columnName = new String("Mode 1");
                break;
            case MODE_WEIGHTING_INDEX:
                columnName = new String("WFM");
                break;
            case FIRST_MODE_FREQUENCY_INDEX:
                columnName = new String("Freq 1");
                break;
            case FREQUENCY_WEIGHTING_INDEX:
                columnName = new String("WFF");
                break;
            case LAST_MODE_NAME_INDEX:
                columnName = new String("Mode 2");
                break;
            case LAST_MODE_FREQUENCY_INDEX:
                columnName = new String("Freq 2");
                break;
            case CORRELATION_INDEX:
                columnName = new String("Value");
                break;
            case FREQUENCY_DIFFERENCE_INDEX:
                columnName = new String("dFreq");
                break;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
        
        return columnName;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link ModeCorrelationMatrix#getFirstModeCount()}.
     * 
     * @return  the number of {@code First Modes}
     **************************************************************************/
    public int getFirstModeCount() {
        return correlation_.getFirstModeCount();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@link KeyList} of the currently selected
     * {@code First Modes}.
     * 
     * @return  the {@link KeyList} of the currently selected
     *          {@code First Modes}
     **************************************************************************/
    public KeyList getFirstSelectedModeKeys() {
        KeyList firstSelectedModeKeys = new KeyList();
        
        if (firstSelectedModeKeys_ == null) {
            return firstSelectedModeKeys;
        }
        
        for (int i = 0; i < firstSelectedModeKeys_.length; i++) {
            if (firstSelectedModeKeys_[i]) {
                firstSelectedModeKeys.add(correlation_.getFirstModeNameAt(i));
            }
        }
        
        return firstSelectedModeKeys;
    } // eom
    
    
    
    /***************************************************************************
     * Returns an array of the weightings of the frequencies.
     * 
     * @return  an array of the weightings of the frequencies
     **************************************************************************/
    public double[] getFrequencyWeightings() {
        return frequencyWeightings_;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link ModeCorrelationMatrix#getLastModeCount()}.
     * 
     * @return  the number of {@code Last Modes}
     **************************************************************************/
    public int getLastModeCount() {
        return correlation_.getLastModeCount();
    } // eom
    
    
    
    /***************************************************************************
     * Creates and returns a {@link ModeConnectionMatrix} from the currently
     * selected {@code First Modes} and{@code Last Modes} in this
     * {@code TableModel}.  The corresponding names and frequencies are also set
     * in the {@code ModeConnectionMatrixs}.
     * 
     * @return  a {@link ModeConnectionMatrix} from the currently selected
     *          {@code First Modes} and{@code Last Modes}
     **************************************************************************/
    public ModeConnectionMatrix getModeConnection() {
        ModeConnectionMatrix    modeConnections;
        int                     connectionIndex     = 0;
        
        modeConnections = new ModeConnectionMatrix(
                this.getFirstSelectedModeKeys().size());
        
        if (firstSelectedModeKeys_ == null) {
            return modeConnections;
        }
        
        for (int i = 0; i < firstSelectedModeKeys_.length; i++) {
            if (firstSelectedModeKeys_[i]) {
                modeConnections.setFirstModeNameAt((String) this.getValueAt(
                        i, FIRST_MODE_NAME_INDEX), connectionIndex);
                modeConnections.setFirstModeFrequencyAt((Double)
                        this.getValueAt(
                        i, FIRST_MODE_FREQUENCY_INDEX), connectionIndex);
                modeConnections.setLastModeNameAt((String) this.getValueAt(
                        i, LAST_MODE_NAME_INDEX), connectionIndex);
                modeConnections.setLastModeFrequencyAt((Double) this.getValueAt(
                        i, LAST_MODE_FREQUENCY_INDEX), connectionIndex);
                modeConnections.setCorrelationAt((Double) this.getValueAt(
                        i, CORRELATION_INDEX), connectionIndex);
                connectionIndex++;
            }
        }
        
        return modeConnections;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the currently stored {@code ModeCorrelationMatrix}.
     * 
     * @return  the currently stored {@code ModeCorrelationMatrix}
     **************************************************************************/
    public ModeCorrelationMatrix getModeCorrelation() {
        return correlation_;
    } // eom
    
    
    
    /***************************************************************************
     * Returns an array of the weightings of the mode correlations.
     * 
     * @return  an array of the weightings of the mode correlations
     **************************************************************************/
    public double[] getModeWeightings() {
        return modeWeightings_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public int getRowCount() {
        return this.getFirstModeCount();
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link ModeCorrelationMatrix#getScene2D(double, javax.vecmath.Color3f)}.
     * 
     * @param   scale           the scale of the scene
     * @param   backgroundColor the background color of the scene
     * @return  a branch group containing the background and 2D matrix that
     *          this correlation represents
     **************************************************************************/
    public BranchGroup getScene2D(double scale, Color3f backgroundColor) {
        return correlation_.getScene2D(scale, backgroundColor);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link ModeCorrelationMatrix#getScene3D(double, javax.vecmath.Color3f)}.
     * 
     * @param   scale           the scale of the scene
     * @param   backgroundColor the background color of the scene
     * @return  a branch group containing the background and 3D matrix that
     *          this correlation represents
     **************************************************************************/
    public BranchGroup getScene3D(double scale, Color3f backgroundColor) {
        return correlation_.getScene3D(scale, backgroundColor);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object value;
        
        switch (columnIndex) {
            case FIRST_MODE_SELECTED_INDEX:
                value = firstSelectedModeKeys_[rowIndex];
                break;
            case FIRST_MODE_NAME_INDEX:
                value = correlation_.getFirstModeNameAt(rowIndex);
                break;
            case MODE_WEIGHTING_INDEX:
                value = modeWeightings_[rowIndex];
                break;
            case FIRST_MODE_FREQUENCY_INDEX:
                value = correlation_.getFirstModeFrequencyAt(rowIndex);
                break;
            case FREQUENCY_WEIGHTING_INDEX:
                value = frequencyWeightings_[rowIndex];
                break;
            case LAST_MODE_NAME_INDEX:
                if (lastSelectedModeKeys_[rowIndex] == null) {
                    value = String.format(" %s", "Select...");
                }
                else {
                    value = lastSelectedModeKeys_[rowIndex];
                }
                
                break;
            case LAST_MODE_FREQUENCY_INDEX:
                if (lastSelectedModeKeys_[rowIndex] == null) {
                    value = 0.0;
                }
                else {
                    value       = lastSelectedModeKeys_[rowIndex];
                    columnIndex = correlation_.getLastModeIndexOf(
                            (String) value);
                    value       = correlation_.getLastModeFrequencyAt(
                            columnIndex);
                }
                
                break;
            case CORRELATION_INDEX:
                if (lastSelectedModeKeys_[rowIndex] == null) {
                    value = 0.0;
                }
                else {
                    value       = lastSelectedModeKeys_[rowIndex];
                    columnIndex = correlation_.getLastModeIndexOf(
                            (String) value);
                    value       = correlation_.getCorrelationAt(rowIndex,
                            columnIndex);
                }
                
                break;
            case FREQUENCY_DIFFERENCE_INDEX:
                if (lastSelectedModeKeys_[rowIndex] == null) {
                    value = 0.0;
                }
                else {
                    value       = lastSelectedModeKeys_[rowIndex];
                    columnIndex = correlation_.getLastModeIndexOf(
                            (String) value);
                    
                    value = correlation_.getLastModeFrequencyAt(columnIndex) -
                            correlation_.getFirstModeFrequencyAt(rowIndex);
                }
                
                break;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
        
        return value;
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeTableModel} has selected any
     * {@code First Modes}.
     * 
     * @return  {@code true} if this {@code ModeTableModel} has selected any
     *          {@code First Modes}; {@code false} otherwise
     **************************************************************************/
    public boolean hasFirstModeKeys() {
        boolean hasFirstModeKeys = false;
        
        for (boolean firstModeKey : firstSelectedModeKeys_) {
            if (firstModeKey == true) {
                hasFirstModeKeys = true;
                break;
            }
        }
        
        return hasFirstModeKeys;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        boolean isCellEditable;
        
        switch (columnIndex) {
            case FIRST_MODE_SELECTED_INDEX:
            case MODE_WEIGHTING_INDEX:
            case FREQUENCY_WEIGHTING_INDEX:
            case LAST_MODE_NAME_INDEX:
                isCellEditable = true;
                break;
            case FIRST_MODE_NAME_INDEX:
            case FIRST_MODE_FREQUENCY_INDEX:
            case LAST_MODE_FREQUENCY_INDEX:
            case CORRELATION_INDEX:
            case FREQUENCY_DIFFERENCE_INDEX:
                isCellEditable = false;
                break;
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
        
        return isCellEditable;
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeCorrelationTableModel} is storing any
     * connections between the {@code First Mode} and the {@code Last Mode}.
     * 
     * @return  {@code true} if this {@code ModeCorrelationTableModel} stores
     *          any mode connections; {@code false} otherwise
     **************************************************************************/
    public boolean isConnected() {
        return !this.getModeConnection().isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeCorrelationTableModel} is storing any
     * correlations between the {@code First Mode} and the {@code Last Mode}.
     * 
     * @return  {@code true} if this {@code ModeCorrelationTableModel} stores
     *          any mode correlations; {@code false} otherwise
     **************************************************************************/
    public boolean isCorrelated() {
        return !correlation_.isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     * Selects all currently stored {@code First Modes}.
     **************************************************************************/
    public void selectAllFirstModeKeys() {
        if (!correlation_.isEmpty()) {
            for (int i = 0; i < firstSelectedModeKeys_.length; i++) {
                firstSelectedModeKeys_[i] = true;
            }
            
            this.fireTableDataChanged();
        }
    } // eom
    
    
    
    /***************************************************************************
     * Sets all the column properties for the given {@link javax.swing.JTable}.
     * 
     * @param   correlationTable    the {@code JTable} whose columns properties
     *                              are set
     **************************************************************************/
    public void setColumnProperties(JTable correlationTable) {
        TableColumn         column;
        JComboBox           correlationComboBox;
        DefaultCellEditor   correlationCellEditor;
        
        correlationComboBox     = new JComboBox(lastModeNames_);
        correlationCellEditor   = new DefaultCellEditor(correlationComboBox);
        
        for (int i = 0; i < N_COLUMNS; i++) {
            column = correlationTable.getColumnModel().getColumn(i);
            
            switch (i) {
                case FIRST_MODE_SELECTED_INDEX:
                    column.setPreferredWidth(40);
                    break;
                case MODE_WEIGHTING_INDEX:
                case FREQUENCY_WEIGHTING_INDEX:
                    column.setPreferredWidth(50);
                    break;
                case LAST_MODE_NAME_INDEX:
                    column.setCellEditor(correlationCellEditor);
                    break;
                case FIRST_MODE_NAME_INDEX:
                case FIRST_MODE_FREQUENCY_INDEX:
                case LAST_MODE_FREQUENCY_INDEX:
                case CORRELATION_INDEX:
                case FREQUENCY_DIFFERENCE_INDEX:
                    break;
                default:
                    throw new ArrayIndexOutOfBoundsException();
            }
        }
    } // eom
    
    
    
    /***************************************************************************
     * Sets the default mode connection between the {@code FirstMode} and
     * {@code Last Mode} by calling
     * {@link ModeCorrelationMatrix#getPreferredConnection(double)} of the
     * stored {@code ModeCorrelationMatrix}.
     **************************************************************************/
    public void setDefaultKeys() {
        ModeConnectionMatrix    connection;
        KeyList                 firstModeNames;
        KeyList                 lastModeNames;
        String                  firstModeName;
        String                  lastModeName;
        int                     index;
        double                  tolerance           = 0.0;
        
        connection      = correlation_.getPreferredConnection(tolerance);
        firstModeNames  = connection.getFirstModeNames();
        lastModeNames   = connection.getLastModeNames();
        
        for (int i = 0; i < this.getFirstModeCount(); i++) {
            firstModeName = (String) this.getValueAt(i, FIRST_MODE_NAME_INDEX);
            
            if (firstModeNames.contains(firstModeName)) {
                index           = firstModeNames.indexOf(firstModeName);
                lastModeName    = lastModeNames.get(index);
                
                firstSelectedModeKeys_[i]   = true;
                lastSelectedModeKeys_[i]    = lastModeName;
            }
            else {
                firstSelectedModeKeys_[i]   = false;
                lastSelectedModeKeys_[i]    = null;
            }
        }
        
        this.fireTableDataChanged();
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link ModeCorrelationMatrix#setFirstName(java.lang.String)}.
     * 
     * @param   firstName   the new name of the {@code First Mode}
     **************************************************************************/
    public void setFirstName(String firstName) {
        correlation_.setFirstName(firstName);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link ModeCorrelationMatrix#setLastName(java.lang.String)}.
     * 
     * @param   lastName   the new name of the {@code Last Mode}
     **************************************************************************/
    public void setLastName(String lastName) {
        correlation_.setLastName(lastName);
    } // eom
    
    
    
    /***************************************************************************
     * Sets the value of the lower tolerance to the given value.  The lower
     * tolerance cannot be greater than the upper tolerance.
     * 
     * @param   lowerTolerance  the new lower tolerance
     **************************************************************************/
    public void setLowerTolerance(double lowerTolerance) {
        correlation_.setLowerTolerance(lowerTolerance);
    } // eom
    
    
    
    /***************************************************************************
     * Replaces the current {@code ModeCorrelationMatrix} with the given one.
     * 
     * @param   correlation the new {@code ModeCorrelationMatrix}
     **************************************************************************/
    public void setModeCorrelation(ModeCorrelationMatrix correlation) {
        int correlationCount = correlation.getFirstModeCount();
        
        correlation_            = correlation;
        firstSelectedModeKeys_  = new boolean[correlationCount];
        lastSelectedModeKeys_   = new String[correlationCount];
        modeWeightings_         = new double[correlationCount];
        frequencyWeightings_    = new double[correlationCount];
        
        for (int i = 0; i < correlationCount; i++) {
            modeWeightings_[i]      = 1.0;
            frequencyWeightings_[i] = 1.0;
        }
        
        lastModeNames_.clear();
        lastModeNames_.addAll(Arrays.asList(correlation_.getLastModeNames()));
        this.fireTableDataChanged();
    } // eom
    
    
    
    /***************************************************************************
     * Sets the value of the upper tolerance to the given value.  The upper
     * tolerance cannot be less than the lower tolerance.
     * 
     * @param   upperTolerance  the new upper tolerance
     **************************************************************************/
    public void setUpperTolerance(double upperTolerance) {
        correlation_.setUpperTolerance(upperTolerance);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case FIRST_MODE_SELECTED_INDEX:
                firstSelectedModeKeys_[rowIndex] = (Boolean) aValue;
                this.fireTableDataChanged();
                break;
            case MODE_WEIGHTING_INDEX:
                modeWeightings_[rowIndex] = (Double) aValue;
                this.fireTableDataChanged();
                break;
            case FREQUENCY_WEIGHTING_INDEX:
                frequencyWeightings_[rowIndex] = (Double) aValue;
                this.fireTableDataChanged();
                break;
            case LAST_MODE_NAME_INDEX:
                if (aValue == null) {
                    lastSelectedModeKeys_[rowIndex] = null;
                }
                else {
                    lastSelectedModeKeys_[rowIndex] = (String) aValue;
                }
                
                this.fireTableDataChanged();
                break;
            case FIRST_MODE_NAME_INDEX:
            case FIRST_MODE_FREQUENCY_INDEX:
            case LAST_MODE_FREQUENCY_INDEX:
            case CORRELATION_INDEX:
            case FREQUENCY_DIFFERENCE_INDEX:
            default:
                throw new ArrayIndexOutOfBoundsException();
        }
    } // eom
} // eoc