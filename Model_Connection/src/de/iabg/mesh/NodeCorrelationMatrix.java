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

import de.iabg.math.RectangularMatrix;

/*******************************************************************************
 * This {@code Matrix} is designed to store the values of a correlation between
 * all nodes of two objects of {@link NodeMatrix}.  The naming conventions used
 * in this implementation are {@code First} or {@code First Nodes} for the rows
 * and {@code Last} or {@code Last Nodes} for the columns.
 * 
 * This class also provides the method {@link #getPreferredConnection(double)},
 * which returns a {@link NodeConnectionMatrix}.  The returned matrix is a
 * mapping between the {@code First Nodes} and {@code Last Nodes} based on the
 * minimum correlation.  This mapping is determined by searching this matrix
 * for the minimum correlation between any two nodes and swapping the
 * corresponding row and column with the first row and column.  The first row
 * and column are omitted from the next search, which searches the remaining
 * nodes for the minimum correlation.  The corresponding row and column are
 * swapped with the second row and column.  This procedure is done iteratively
 * until the given maximum correlation tolerance is reached.  Thus, a mapping
 * based on the minimum correlation is formed along the diagonal, which is
 * ultimately returned to the user.  This procedure is robust in that it will
 * find the best mapping for any ordering of the stored nodes.  The algorithm,
 * however, requires significant memory storage and may not properly execute
 * for large systems.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class NodeCorrelationMatrix extends RectangularMatrix {
    
    
    
    /***************************************************************************
     * Constructs a {@code NodeCorrelationMatrix} containing the given number of
     * {@code First Nodes} as the rows and {@code Last Nodes} as the columns.
     * 
     * @param   nFirstNodes the number of {@code First Nodes} in this matrix
     * @param   nLastNodes  the number of {@code Last Nodes} in this matrix
     **************************************************************************/
    public NodeCorrelationMatrix(int nFirstNodes, int nLastNodes) {
        super(nFirstNodes, nLastNodes);
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a copy of the given {@code NodeCorrelationMatrix}.
     * 
     * @param   correlation the {@code NodeCorrelationMatrix} to be copied
     **************************************************************************/
    public NodeCorrelationMatrix(NodeCorrelationMatrix correlation) {
        this(correlation.nRows_, correlation.nColumns_);
        
        System.arraycopy(correlation.rowNames_, 0, rowNames_, 0, nRows_);
        System.arraycopy(correlation.columnNames_, 0, columnNames_, 0,
                nColumns_);
        System.arraycopy(correlation.values_, 0, values_, 0,
                nRows_ * nColumns_);
    } // eom
    
    
    
    /***************************************************************************
     * Returns the diagonal values of this matrix as a new
     * {@code NodeConnectionMatrix}.  This method simply copies the names of the
     * {@code First Nodes} and {@code Last Nodes} and the correlation along
     * the diagonal into the {@code NodeConnectionMatrix}.  Therefore, this
     * method does not guarantee anything about the quality of the mapping.
     * 
     * @return the connection of the nodes along the diagonal
     **************************************************************************/
    protected NodeConnectionMatrix getConnection() {
        NodeConnectionMatrix    connection;
        double                  value;
        int                     dimension   = Math.min(nRows_, nColumns_);
        
        connection = new NodeConnectionMatrix(dimension);
        
        for (int i = 0; i < dimension; i++) {
            if (rowNames_[i] != null) {
                connection.setFirstNodeNameAt(rowNames_[i], i);
            }
            
            if (columnNames_[i] != null) {
                connection.setLastNodeNameAt(columnNames_[i], i);
            }
            
            value = values_[(i * nColumns_) + i];
            connection.setCorrelationAt(value, i);
        }
        
        return connection;
    } // eom
    
    
    
    /***************************************************************************
     * Returns a mapping between the {@code First Nodes} and {@code Last Nodes}
     * based on the minimum correlation.  This method makes a copy of this class
     * and calls {@link #sortDiagonalAscending()} with the given tolerance.  A
     * {@code NodeConnectionMatrix} is then created from the sorted correlation
     * and reduced to the given tolerance.
     * 
     * @param   tolerance   the maximum correlation tolerance
     * @return  the connection of the nodes based on a minimum distanace
     *          correlation
     **************************************************************************/
    public NodeConnectionMatrix getPreferredConnection(double tolerance) {
        NodeConnectionMatrix    connection;
        NodeCorrelationMatrix   correlation;
        
        correlation = new NodeCorrelationMatrix(this);
        correlation.sortDiagonalAscending();
        
        connection = correlation.getConnection();
        connection.reduce(tolerance);
        
        return connection;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #setValueAt(double, int, int)}.
     * 
     * @param   value       the new value of the correlation
     * @param   firstIndex  the index of the {@code First Node} to be changed
     * @param   lastIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setCorrelationAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, firstIndex, lastIndex);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #setRowNameAt(java.lang.String, int)}.
     * 
     * @param   nodeName    the new name of the {@code First Node}
     * @param   nodeIndex   the index of the {@code First Node} to be changed
     **************************************************************************/
    public void setFirstNodeNameAt(String nodeName, int nodeIndex) {
        this.setRowNameAt(nodeName, nodeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #setColumnNameAt(java.lang.String, int)}.
     * 
     * @param   nodeName    the new name of the {@code Last Node}
     * @param   nodeIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setLastNodeNameAt(String nodeName, int nodeIndex) {
        this.setColumnNameAt(nodeName, nodeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * This method iteratively searches this matrix for the minimum correlation
     * mapping and places them along the diagonal.  The algorithm begins by
     * searching the entire matrix for the minimum value.  The corresponding row
     * and column are swapped into the first row and first column.  The next
     * search omits the row and column and searches the matrix for the next
     * smallest value.  The corresponding row and column are swapped into the
     * second row and second column.  The next search proceeds by omitting the
     * first two rows and first two columns.  Thus, the minimum mapping between
     * the {@code First Nodes} and {@code Last Nodes} is built along the
     * diagonal.
     **************************************************************************/
    protected void sortDiagonalAscending() {
        double  minimumValue;
        double  value;
        int     rowIndex;
        int     columnIndex;
        int     dimension       = Math.min(nRows_, nColumns_);
        
        for (int i = 0; i < dimension; i++) {
            minimumValue    = values_[(i * nColumns_) + i];
            rowIndex        = i;
            columnIndex     = i;
            
            for (int j = i; j < nRows_; j++) {
                for (int k = i; k < nColumns_; k++) {
                    value = values_[(j * nColumns_) + k];
                    
                    if (value < minimumValue) {
                        minimumValue    = value;
                        rowIndex        = j;
                        columnIndex     = k;
                    }
                }
            }
            
            this.swapRows(i, rowIndex);
            this.swapColumns(i, columnIndex);
        }
    } // eom
} // eoc