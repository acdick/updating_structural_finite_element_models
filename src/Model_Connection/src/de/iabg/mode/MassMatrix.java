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

import de.iabg.math.SymmetricMatrix;

/*******************************************************************************
 * This {@code Matrix} is designed to store the influence values of a given mass
 * matrix.  This matrix has several convenience methods that wrap methods from
 * {@code SymmetricMatrix}.  The mass influence values are stored as a lower
 * triangular matrix with the x, y, z mass influences of each node are stored as
 * fixed rows and columns.
 * 
 * The main idea of this class is to have a convenient and efficient storage
 * object for a symmetrix mass matrix.  Therefore, any calculations including
 * this mass matrix, such as the Orthogonality Check, should use the ordering of
 * this mass matrix as a reference.  The corresponding {@link ModeMatrix}
 * objects should instead be sorted to match the ordering of the nodes of this
 * {@code Matrix}. The values of this {@code Matrix} can therefore also be
 * accessed by calling the {@link #values()} method.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class MassMatrix extends SymmetricMatrix {
    /** The number of components in 3D space */
    protected static final int N_COMPONENTS = 3;
    
    /** The index of the x-coordinate */
    protected static final int X_INDEX = 0;
    
    /** The index of the y-coordinate */
    protected static final int Y_INDEX = 1;
    
    /** The index of the z-coordinate */
    protected static final int Z_INDEX = 2;
    
    
    
    /***************************************************************************
     * Constructs a default {@code MassMatrix} with no nodes.
     **************************************************************************/
    public MassMatrix() {
        this(0);
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a {@code MassMatrix} containing the given number of nodes.
     * 
     * @param   nNodes  the number of nodes in this matrix
     **************************************************************************/
    public MassMatrix(int nNodes) {
        super(nNodes * N_COMPONENTS);
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a copy of the given {@code MassMatrix}.
     * 
     * @param   masses  the {@code MassMatrix} to be copied
     **************************************************************************/
    public MassMatrix(MassMatrix masses) {
        this(masses.nRows_ / N_COMPONENTS);
        
        System.arraycopy(masses.rowNames_, 0, rowNames_, 0, nRows_);
        System.arraycopy(masses.columnNames_, 0, columnNames_, 0, nColumns_);
        
        for (int i = 0; i < nRows_; i++) {
            System.arraycopy(masses.values_[i], 0, values_[i], 0, i + 1);
        }
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of nodes in this matrix.
     * 
     * @return  the number of nodes
     **************************************************************************/
    public int getNodeCount() {
        return this.getRowCount() / N_COMPONENTS;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getRowNames()}.
     * 
     * @return  a string array containing all node names
     **************************************************************************/
    public String[] getNodeNames() {
        return this.getRowNames();
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the xx-mass influence value at the given
     * {@code First Node} and {@code Last Node} indices.
     * 
     * @param   firstIndex  the index of the {@code First Node}
     * @param   lastIndex   the index of the {@code Last Node}
     * @return  the value of the xx-mass influence at the given
     *          {@code First Node} and {@code Last Node} indices
     **************************************************************************/
    public double getXXMassAt(int firstIndex, int lastIndex) {
        return this.getValueAt((firstIndex * N_COMPONENTS) + X_INDEX,
                (lastIndex * N_COMPONENTS) + X_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the xy-mass influence value at the given
     * {@code First Node} and {@code Last Node} indices.
     * 
     * @param   firstIndex  the index of the {@code First Node}
     * @param   lastIndex   the index of the {@code Last Node}
     * @return  the value of the xy-mass influence
     **************************************************************************/
    public double getXYMassAt(int firstIndex, int lastIndex) {
        return this.getValueAt((firstIndex * N_COMPONENTS) + X_INDEX,
                (lastIndex * N_COMPONENTS) + Y_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the xz-mass influence value at the given
     * {@code First Node} and {@code Last Node} indices.
     * 
     * @param   firstIndex  the index of the {@code First Node}
     * @param   lastIndex   the index of the {@code Last Node}
     * @return  the value of the xz-mass influence
     **************************************************************************/
    public double getXZMassAt(int firstIndex, int lastIndex) {
        return this.getValueAt((firstIndex * N_COMPONENTS) + X_INDEX,
                (lastIndex * N_COMPONENTS) + Z_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the yx-mass influence value at the given
     * {@code First Node} and {@code Last Node} indices.
     * 
     * @param   firstIndex  the index of the {@code First Node}
     * @param   lastIndex   the index of the {@code Last Node}
     * @return  the value of the yx-mass influence
     **************************************************************************/
    public double getYXMassAt(int firstIndex, int lastIndex) {
        return this.getValueAt((firstIndex * N_COMPONENTS) + Y_INDEX,
                (lastIndex * N_COMPONENTS) + X_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the yy-mass influence value at the given
     * {@code First Node} and {@code Last Node} indices.
     * 
     * @param   firstIndex  the index of the {@code First Node}
     * @param   lastIndex   the index of the {@code Last Node}
     * @return  the value of the yy-mass influence
     **************************************************************************/
    public double getYYMassAt(int firstIndex, int lastIndex) {
        return this.getValueAt((firstIndex * N_COMPONENTS) + Y_INDEX,
                (lastIndex * N_COMPONENTS) + Y_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the yz-mass influence value at the given
     * {@code First Node} and {@code Last Node} indices.
     * 
     * @param   firstIndex  the index of the {@code First Node}
     * @param   lastIndex   the index of the {@code Last Node}
     * @return  the value of the yz-mass influence
     **************************************************************************/
    public double getYZMassAt(int firstIndex, int lastIndex) {
        return this.getValueAt((firstIndex * N_COMPONENTS) + Y_INDEX,
                (lastIndex * N_COMPONENTS) + Z_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the zx-mass influence value at the given
     * {@code First Node} and {@code Last Node} indices.
     * 
     * @param   firstIndex  the index of the {@code First Node}
     * @param   lastIndex   the index of the {@code Last Node}
     * @return  the value of the zx-mass influence
     **************************************************************************/
    public double getZXMassAt(int firstIndex, int lastIndex) {
        return this.getValueAt((firstIndex * N_COMPONENTS) + Z_INDEX,
                (lastIndex * N_COMPONENTS) + X_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the zy-mass influence value at the given
     * {@code First Node} and {@code Last Node} indices.
     * 
     * @param   firstIndex  the index of the {@code First Node}
     * @param   lastIndex   the index of the {@code Last Node}
     * @return  the value of the zy-mass influence
     **************************************************************************/
    public double getZYMassAt(int firstIndex, int lastIndex) {
        return this.getValueAt((firstIndex * N_COMPONENTS) + Z_INDEX,
                (lastIndex * N_COMPONENTS) + Y_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the zz-mass influence value at the given
     * {@code First Node} and {@code Last Node} indices.
     * 
     * @param   firstIndex  the index of the {@code First Node}
     * @param   lastIndex   the index of the {@code Last Node}
     * @return  the value of the zz-mass influence
     **************************************************************************/
    public double getZZMassAt(int firstIndex, int lastIndex) {
        return this.getValueAt((firstIndex * N_COMPONENTS) + Z_INDEX,
                (lastIndex * N_COMPONENTS) + Z_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public String paramString() {
        StringBuilder   result      = new StringBuilder();
        int             iComponent;
        
        result.append(String.format(" %15s", ""));
        result.append(String.format(" %15s", "Mass"));
        
        for (String columnName : columnNames_) {
            result.append(String.format(" %15s", columnName));
        }
        
        result.append(String.format("%n"));
        result.append(String.format(" %15s", "Mass"));
        result.append(String.format(" %15s", "Component"));
        
        for (int i = 0; i < nColumns_ / N_COMPONENTS; i++) {
            result.append(String.format(" %15s", "X-Component"));
            result.append(String.format(" %15s", "Y-Component"));
            result.append(String.format(" %15s", "Z-Component"));
        }
        
        result.append(String.format("%n"));
        
        for (int i = 0; i < nRows_; i++) {
            result.append(String.format(" %15s", rowNames_[i]));
            
            iComponent = i % N_COMPONENTS;
            if (iComponent == X_INDEX) {
                result.append(String.format(" %15s", "X-Component"));
            }
            else if (iComponent == Y_INDEX) {
                result.append(String.format(" %15s", "Y-Component"));
            }
            else {
                result.append(String.format(" %15s", "Z-Component"));
            }
            
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
     * This method does nothing and overrides the previous implementation
     * because the number of columns (mass influences) should not be directly
     * changed.
     **************************************************************************/
    @Override
    public void setColumnCount(int nColumns) {
        
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #setValueAt(double, int, int)}.
     * 
     * @param   value       the new value of the mass influence
     * @param   firstIndex  the index of the {@code First Node} to be changed
     * @param   lastIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setMassAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, firstIndex, lastIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Sets the number of nodes to the specified amount.  The data of all cells
     * may be lost due to resizing of the matrix.
     * 
     * @param   nNodes  the new number of nodes
     **************************************************************************/
    public void setNodeCount(int nNodes) {
        rowNames_       = new String[nNodes * N_COMPONENTS];
        columnNames_    = new String[nNodes * N_COMPONENTS];
        nRows_          = nNodes * N_COMPONENTS;
        nColumns_       = nNodes * N_COMPONENTS;
        values_         = new double[nRows_][];
        
        for (int i = 0; i < nRows_; i++) {
            values_[i] = new double[i + 1];
        }
    } // eom
    
    
    
    /***************************************************************************
     * Sets the name of the node at the x, y, and z mass influences at the given
     * index.
     * 
     * @param   nodeName    the new name of the node
     * @param   nodeIndex   the index of the node to be changed
     **************************************************************************/
    public void setNodeNameAt(String nodeName, int nodeIndex) {
        this.setRowNameAt(nodeName, (nodeIndex * N_COMPONENTS) + X_INDEX);
        this.setRowNameAt(nodeName, (nodeIndex * N_COMPONENTS) + Y_INDEX);
        this.setRowNameAt(nodeName, (nodeIndex * N_COMPONENTS) + Z_INDEX);
        this.setColumnNameAt(nodeName, (nodeIndex * N_COMPONENTS) + X_INDEX);
        this.setColumnNameAt(nodeName, (nodeIndex * N_COMPONENTS) + Y_INDEX);
        this.setColumnNameAt(nodeName, (nodeIndex * N_COMPONENTS) + Z_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * This method does nothing and overrides the previous implementation
     * because the number of rows (mass influences) should not be directly
     * changed.
     **************************************************************************/
    @Override
    public void setRowCount(int nRows) {
        
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given xx-mass influence value at the
     * given {@code First Node} and {@code Last Node} indices.
     * 
     * @param   value       the new value of the xx-mass influence
     * @param   firstIndex  the index of the {@code First Node} to be changed
     * @param   lastIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setXXMassAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, (firstIndex * N_COMPONENTS) + X_INDEX,
                (lastIndex * N_COMPONENTS) + X_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given xy-mass influence value at the
     * given {@code First Node} and {@code Last Node} indices.
     * 
     * @param   value       the new value of the xy-mass influence
     * @param   firstIndex  the index of the {@code First Node} to be changed
     * @param   lastIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setXYMassAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, (firstIndex * N_COMPONENTS) + X_INDEX,
                (lastIndex * N_COMPONENTS) + Y_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given xz-mass influence value at the
     * given {@code First Node} and {@code Last Node} indices.
     * 
     * @param   value       the new value of the xz-mass influence
     * @param   firstIndex  the index of the {@code First Node} to be changed
     * @param   lastIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setXZMassAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, (firstIndex * N_COMPONENTS) + X_INDEX,
                (lastIndex * N_COMPONENTS) + Z_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given yx-mass influence value at the
     * given {@code First Node} and {@code Last Node} indices.
     * 
     * @param   value       the new value of the yx-mass influence
     * @param   firstIndex  the index of the {@code First Node} to be changed
     * @param   lastIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setYXMassAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, (firstIndex * N_COMPONENTS) + Y_INDEX,
                (lastIndex * N_COMPONENTS) + X_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given yy-mass influence value at the
     * given {@code First Node} and {@code Last Node} indices.
     * 
     * @param   value       the new value of the yy-mass influence
     * @param   firstIndex  the index of the {@code First Node} to be changed
     * @param   lastIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setYYMassAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, (firstIndex * N_COMPONENTS) + Y_INDEX,
                (lastIndex * N_COMPONENTS) + Y_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given yz-mass influence value at the
     * given {@code First Node} and {@code Last Node} indices.
     * 
     * @param   value       the new value of the yz-mass influence
     * @param   firstIndex  the index of the {@code First Node} to be changed
     * @param   lastIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setYZMassAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, (firstIndex * N_COMPONENTS) + Y_INDEX,
                (lastIndex * N_COMPONENTS) + Z_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given zx-mass influence value at the
     * given {@code First Node} and {@code Last Node} indices.
     * 
     * @param   value       the new value of the zx-mass influence
     * @param   firstIndex  the index of the {@code First Node} to be changed
     * @param   lastIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setZXMassAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, (firstIndex * N_COMPONENTS) + Z_INDEX,
                (lastIndex * N_COMPONENTS) + X_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given zy-mass influence value at the
     * given {@code First Node} and {@code Last Node} indices.
     * 
     * @param   value       the new value of the zy-mass influence
     * @param   firstIndex  the index of the {@code First Node} to be changed
     * @param   lastIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setZYMassAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, (firstIndex * N_COMPONENTS) + Z_INDEX,
                (lastIndex * N_COMPONENTS) + Y_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given zz-mass influence value at the
     * given {@code First Node} and {@code Last Node} indices.
     * 
     * @param   value       the new value of the zz-mass influence
     * @param   firstIndex  the index of the {@code First Node} to be changed
     * @param   lastIndex   the index of the {@code Last Node} to be changed
     **************************************************************************/
    public void setZZMassAt(double value, int firstIndex, int lastIndex) {
        this.setValueAt(value, (firstIndex * N_COMPONENTS) + Z_INDEX,
                (lastIndex * N_COMPONENTS) + Z_INDEX);
    } // eom
} // eoc