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
package de.iabg.mesh;

import de.iabg.math.DiagonalMatrix;

import de.iabg.swing.KeyList;

import java.util.Arrays;

/*******************************************************************************
 * This {@code Matrix} is designed to store a connection or mapping between
 * two objects of {@code NodeMatrix}.  The naming conventions used in this
 * implementation are {@code First} or {@code First Nodes} for the rows and
 * {@code Last} or {@code Last Nodes} for the columns.  This implementation
 * takes advantage of the storage scheme of {@link DiagonalMatrix}, which has
 * an efficient method of storing such a mapping.  This class is created instead
 * of using an actual map because it uses primitive values, which can be
 * beneficial for large systems.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 14, 2008
 ******************************************************************************/
public class NodeConnectionMatrix extends DiagonalMatrix {
    
    
    
    /***************************************************************************
     * Constructs a default {@code NodeConnectionMatrix} with no connections.
     **************************************************************************/
    public NodeConnectionMatrix() {
        this(0);
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a {@code NodeConnectionMatrix} containing the given number of
     * connections.
     * 
     * @param   nConnections  the number of connections in this matrix
     **************************************************************************/
    public NodeConnectionMatrix(int nConnections) {
        super(nConnections);
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a copy of the given {@code NodeConnectionMatrix}.
     * 
     * @param   connections the {@code NodeConnectionMatrix} to be copied
     **************************************************************************/
    public NodeConnectionMatrix(NodeConnectionMatrix connections) {
        this(connections.nRows_);
        
        System.arraycopy(connections.rowNames_, 0, rowNames_, 0, nRows_);
        System.arraycopy(
                connections.columnNames_, 0, columnNames_, 0, nColumns_);
        System.arraycopy(connections.values_, 0, values_, 0, nRows_);
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
     * Returns the {@code First Nodes} as a {@link KeyList}.  This wrapping is
     * done so that the {@code First Nodes} can be easily represented as a
     * {@link javax.swing.JComboBox}.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} containing the
     *          {@code First Nodes}
     **************************************************************************/
    public KeyList getFirstNodeKeys() {
        KeyList firstKeys = new KeyList();
        firstKeys.addAll(Arrays.asList(rowNames_));
        
        return firstKeys;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getRowNameAt(int)}.
     * 
     * @param   nodeIndex   the index of the {@code First Node}
     * @return  the name of the {@code First Node} at the given index.
     **************************************************************************/
    public String getFirstNodeNameAt(int nodeIndex) {
        return this.getRowNameAt(nodeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Returns the {@code Last Nodes} as a {@link KeyList}.  This wrapping is
     * done so that the {@code Last Nodes} can be easily represented as a
     * {@link javax.swing.JComboBox}.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} containing the
     *          {@code Last Nodes}
     **************************************************************************/
    public KeyList getLastNodeKeys() {
        KeyList lastKeys = new KeyList();
        lastKeys.addAll(Arrays.asList(columnNames_));
        
        return lastKeys;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getColumnNameAt(int)}.
     * 
     * @param   nodeIndex   the index of the {@code Last Node}
     * @return  the name of the {@code Last Node} at the given index.
     **************************************************************************/
    public String getLastNodeNameAt(int nodeIndex) {
        return this.getColumnNameAt(nodeIndex);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public String paramString() {
        StringBuilder result = new StringBuilder();
        
        result.append(String.format(" %-15s", "First Node"));
        result.append(String.format(" %-15s", "Last Node"));
        result.append(String.format(" %-15s", "Distance"));
        result.append(String.format("%n"));
        
        for (int i = 0; i < nRows_; i++) {
            result.append(String.format(" %15s", rowNames_[i]));
            result.append(String.format(" %15s", columnNames_[i]));
            result.append(String.format(" % 11.8e", values_[i]));
            result.append(String.format("%n"));
        }
        
        return result.toString();
    } // eom
    
    
    
    /***************************************************************************
     * Reduces the size of this matrix by removing the corresponding
     * {@code First Node}, {@code Last Node}, and value of any connection which
     * is more than the given tolerance.
     * 
     * @param   tolerance   the maximum connection tolerance
     **************************************************************************/
    public void reduce(double tolerance) {
        String[]    rowNames;
        String[]    columnNames;
        double[]    values;
        int         dimension       = 0;
        
        for (double value : values_) {
            if (value <= tolerance) {
                dimension++;
            }
        }
        
        rowNames    = new String[dimension];
        columnNames = new String[dimension];
        values      = new double[dimension];
        dimension   = 0;
        
        for (int i = 0; i < values_.length; i++) {
            if (values_[i] <= tolerance) {
                rowNames[dimension]     = rowNames_[i];
                columnNames[dimension]  = columnNames_[i];
                values[dimension]       = values_[i];
                dimension++;
            }
        }
        
        rowNames_       = rowNames;
        columnNames_    = columnNames;
        nRows_          = dimension;
        nColumns_       = dimension;
        values_         = values;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #setConnectionCount(int)}.
     * 
     * @param   nConnections    the new number of connections
     **************************************************************************/
    public void setConnectionCount(int nConnections) {
        this.setDimensionCount(nConnections);
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
     * A convenience method which wraps
     * {@link #setRowNameAt(java.lang.String, int)}.
     * 
     * @param   nodeName        the new name of the {@code First Node}
     * @param   connectionIndex the index of the connection
     **************************************************************************/
    public void setFirstNodeNameAt(String nodeName, int connectionIndex) {
        this.setRowNameAt(nodeName, connectionIndex);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #setColumnNameAt(java.lang.String, int)}.
     * 
     * @param   nodeName        the new name of the {@code Last Node}
     * @param   connectionIndex the index of the connection
     **************************************************************************/
    public void setLastNodeNameAt(String nodeName, int connectionIndex) {
        this.setColumnNameAt(nodeName, connectionIndex);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #sortRows(de.iabg.swing.KeyList)}.
     * 
     * @param   firstNodeKeys   the new order of the {@code First Node} names
     **************************************************************************/
    public void sortFirstNodes(KeyList firstNodeKeys) {
        this.sortRows(firstNodeKeys);
    } // eom
} // eoc