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

/*******************************************************************************
 * This {@code Matrix} is designed to store the mode shape coordinates of a
 * given mode shape matrix.  This matrix has several convenience methods that
 * wrap methods from {@code RectangularMatrix}.  The modes are stored as rows
 * and the x, y, z coordinates of each node are stored as fixed columns.
 * 
 * This matrix also has the ability to correlate and connect itself with another
 * {@code ModeMatrix}.  A correlation compares all modes of this matrix
 * with all modes of another matrix, while a connection specifies a mapping
 * between modes from this matrix and another matrix.  There are a few types of
 * correlations implemented in this class, a dot product correlation, a Modal
 * Assurance Criterion correlation, and an Orthogonality Check correlation.
 * 
 * This class also adds storage for the frequencies of each mode.  This means
 * that some of the methods defined in {@link RectangularMatrix} must be
 * overridden to include changes to this data.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class ModeMatrix extends RectangularMatrix {
    /** The mode frequencies of this mode shape matrix */
    protected double[] modeFrequencies_;
    
    /** The number of components in 3D space */
    protected static final int N_COMPONENTS = 3;
    
    /** The index of the x-coordinate */
    protected static final int X_INDEX = 0;
    
    /** The index of the y-coordinate */
    protected static final int Y_INDEX = 1;
    
    /** The index of the z-coordinate */
    protected static final int Z_INDEX = 2;
    
    
    
    /***************************************************************************
     * Constructs a default {@code ModeMatrix} with no modes or nodes.
     **************************************************************************/
    public ModeMatrix() {
        this(0, 0);
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a {@code ModeMatrix} containing the given number of modes and
     * nodes.
     * 
     * @param   nModes  the number of modes in this matrix
     * @param   nNodes  the number of nodes in this matrix
     **************************************************************************/
    public ModeMatrix(int nModes, int nNodes) {
        super(nModes, nNodes * N_COMPONENTS);
        
        modeFrequencies_ = new double[nRows_];
    } // eom
    
    
    
    /***************************************************************************
     * Constructs a copy of the given {@code ModeMatrix}.
     * 
     * @param   modes   the {@code ModeMatrix} to be copied
     **************************************************************************/
    public ModeMatrix(ModeMatrix modes) {
        this(modes.nRows_, modes.nColumns_ / N_COMPONENTS);
        
        System.arraycopy(modes.rowNames_, 0, rowNames_, 0, nRows_);
        System.arraycopy(modes.columnNames_, 0, columnNames_, 0, nColumns_);
        System.arraycopy(modes.values_, 0, values_, 0, nRows_ * nColumns_);
        System.arraycopy(modes.modeFrequencies_, 0, modeFrequencies_, 0,
                nRows_);
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
        modeFrequencies_    = new double[nRows_];
    } // eom
    
    
    
    /***************************************************************************
     * Computes a matrix of the dot products of each mode in this
     * {@code ModeMatrix} and the given {@code ModeMatrix}.  The modes of this
     * matrix are the rows of the returned {@link ModeCorrelationMatrix}, while
     * the modes of the given matrix are the columns.
     * 
     * @param   modes   the {@code ModeMatrix} that will be correlated to this
     *                  {@code ModeMatrix}
     * @return  the matrix of the dot product correlation
     **************************************************************************/
    public ModeCorrelationMatrix computeDotProduct(ModeMatrix modes) {
        ModeCorrelationMatrix   correlation;
        double                  mixedProduct;
        double                  firstComponent;
        double                  lastComponent;
        int                     nFirstModes     = nRows_;
        int                     nLastModes      = modes.nRows_;
        
        correlation = new ModeCorrelationMatrix(nFirstModes, nLastModes);
        
        for (int i = 0; i < nFirstModes; i++) {
            if (rowNames_[i] != null) {
                correlation.setFirstModeNameAt(rowNames_[i], i);
            }
            
            correlation.setFirstModeFrequencyAt(modeFrequencies_[i], i);
        }
        
        for (int i = 0; i < nLastModes; i++) {
            if (modes.rowNames_[i] != null) {
                correlation.setLastModeNameAt(modes.rowNames_[i], i);
            }
            
            correlation.setLastModeFrequencyAt(modes.modeFrequencies_[i], i);
        }
        
        for (int i = 0; i < nFirstModes; i++) {
            for (int j = 0; j < nLastModes; j++) {
                mixedProduct = 0.0;
                
                for (int k = 0; k < nColumns_; k++) {
                    firstComponent  =  values_[(i * nColumns_) + k];
                    lastComponent   =  modes.values_[(j * nColumns_) + k];
                    mixedProduct    += firstComponent * lastComponent;
                }
                
                correlation.setCorrelationAt(mixedProduct, i, j);
            }
        }
        
        return correlation;
    } // eom
    
    
    
    /***************************************************************************
     * Computes a matrix of the generalized mass of each mode in this
     * {@code ModeMatrix} and the given {@code ModeMatrix}.  The required
     * {@link MassMatrix} is for the {@code First Mode} and should have the same
     * node names for the calculation to successfully compute.  The modes of
     * this matrix are the rows of the returned {@link ModeCorrelationMatrix},
     * while the modes of the given matrix are the columns.
     * 
     * @param   masses  the {@code MassMatrix} for the {@code First Mode}
     * @param   modes   the {@code ModeMatrix} that will be correlated to this
     *                  {@code ModeMatrix}
     * @return  the matrix of the generalized mass correlation
     **************************************************************************/
    public ModeCorrelationMatrix computeGeneralizedMass(MassMatrix masses,
            ModeMatrix modes) {
        ModeCorrelationMatrix   correlation;
        double[][]              massValues;
        double                  mixedProduct;
        double                  firstComponent;
        double                  lastComponent;
        double                  massComponent;
        double                  firstMassProduct;
        int                     nFirstModes         = nRows_;
        int                     nLastModes          = modes.nRows_;
        
        correlation = new ModeCorrelationMatrix(nFirstModes, nLastModes);
        massValues  = masses.values();
        
        for (int i = 0; i < nFirstModes; i++) {
            if (rowNames_[i] != null) {
                correlation.setFirstModeNameAt(rowNames_[i], i);
            }
            
            correlation.setFirstModeFrequencyAt(modeFrequencies_[i], i);
        }
        
        for (int i = 0; i < nLastModes; i++) {
            if (modes.rowNames_[i] != null) {
                correlation.setLastModeNameAt(modes.rowNames_[i], i);
            }
            
            correlation.setLastModeFrequencyAt(modes.modeFrequencies_[i], i);
        }
        
        for (int i = 0; i < nFirstModes; i++) {
            for (int j = 0; j < nLastModes; j++) {
                mixedProduct = 0.0;
                
                for (int k = 0; k < nColumns_; k++) {
                    firstMassProduct = 0.0;
                    
                    for (int l = 0; l < k; l++) {
                        firstComponent      =  values_[(i * nColumns_) + l];
                        lastComponent       =
                                modes.values_[(j * nColumns_) + l];
                        massComponent       =  massValues[k][l];
                        firstMassProduct    += firstComponent * massComponent;
                    }
                    
                    for (int l = k; l < nColumns_; l++) {
                        firstComponent      =  values_[(i * nColumns_) + l];
                        lastComponent       =
                                modes.values_[(j * nColumns_) + l];
                        massComponent       =  massValues[l][k];
                        firstMassProduct    += firstComponent * massComponent;
                    }
                    
                    firstComponent  =  values_[(i * nColumns_) + k];
                    lastComponent   =  modes.values_[(j * nColumns_) + k];
                    mixedProduct    += firstMassProduct * lastComponent;
                }
                
                correlation.setCorrelationAt(mixedProduct, i, j);
            }
        }
        
        return correlation;
    } // eom
    
    
    
    /***************************************************************************
     * Computes a matrix of the Modal Assurance Criterion of each mode in this
     * {@code ModeMatrix} and the given {@code ModeMatrix}.  The modes of this
     * matrix are the rows of the returned {@link ModeCorrelationMatrix}, while
     * the modes of the given matrix are the columns.
     * 
     * @param   modes   the {@code ModeMatrix} that will be correlated to this
     *                  {@code ModeMatrix}
     * @return  the matrix of the Modal Assurance Criterion correlation
     **************************************************************************/
    public ModeCorrelationMatrix computeModalAssuranceCriterion(
            ModeMatrix modes) {
        ModeCorrelationMatrix   correlation;
        double                  firstProduct;
        double                  lastProduct;
        double                  mixedProduct;
        double                  normalProduct;
        double                  firstComponent;
        double                  lastComponent;
        int                     nFirstModes     = nRows_;
        int                     nLastModes      = modes.nRows_;
        
        correlation = new ModeCorrelationMatrix(nFirstModes, nLastModes);
        
        for (int i = 0; i < nFirstModes; i++) {
            if (rowNames_[i] != null) {
                correlation.setFirstModeNameAt(rowNames_[i], i);
            }
            
            correlation.setFirstModeFrequencyAt(modeFrequencies_[i], i);
        }
        
        for (int i = 0; i < nLastModes; i++) {
            if (modes.rowNames_[i] != null) {
                correlation.setLastModeNameAt(modes.rowNames_[i], i);
            }
            
            correlation.setLastModeFrequencyAt(modes.modeFrequencies_[i], i);
        }
        
        for (int i = 0; i < nFirstModes; i++) {
            for (int j = 0; j < nLastModes; j++) {
                firstProduct    = 0.0;
                lastProduct     = 0.0;
                mixedProduct    = 0.0;
                normalProduct   = 0.0;
                
                for (int k = 0; k < nColumns_; k++) {
                    firstComponent  =  values_[(i * nColumns_) + k];
                    lastComponent   =  modes.values_[(j * nColumns_) + k];
                    firstProduct    += firstComponent * firstComponent;
                    lastProduct     += lastComponent * lastComponent;
                    mixedProduct    += firstComponent * lastComponent;
                }
                
                normalProduct += mixedProduct * mixedProduct;
                normalProduct /= firstProduct;
                normalProduct /= lastProduct;
                correlation.setCorrelationAt(normalProduct, i, j);
            }
        }
        
        return correlation;
    } // eom
    
    
    
    /***************************************************************************
     * Computes a matrix of the normalized Orthogonality Check of each mode in
     * this {@code ModeMatrix} and the given {@code ModeMatrix}.  The required
     * {@link MassMatrix} is for the {@code First Mode} and should have the same
     * node names for the calculation to successfully compute.  The modes of
     * this matrix are the rows of the returned {@link ModeCorrelationMatrix},
     * while the modes of the given matrix are the columns.
     * 
     * @param   masses  the {@code MassMatrix} for the {@code First Mode}
     * @param   modes   the {@code ModeMatrix} that will be correlated to this
     *                  {@code ModeMatrix}
     * @return  the matrix of the normalized Orthogonality Check correlation
     **************************************************************************/
    public ModeCorrelationMatrix computeOrthogonalityCheck(MassMatrix masses,
            ModeMatrix modes) {
        ModeCorrelationMatrix   correlation;
        double[][]              massValues;
        double                  firstProduct;
        double                  lastProduct;
        double                  mixedProduct;
        double                  normalProduct;
        double                  firstComponent;
        double                  lastComponent;
        double                  massComponent;
        double                  firstMassProduct;
        double                  lastMassProduct;
        int                     nFirstModes         = nRows_;
        int                     nLastModes          = modes.nRows_;
        
        correlation = new ModeCorrelationMatrix(nFirstModes, nLastModes);
        massValues  = masses.values();
        
        for (int i = 0; i < nFirstModes; i++) {
            if (rowNames_[i] != null) {
                correlation.setFirstModeNameAt(rowNames_[i], i);
            }
            
            correlation.setFirstModeFrequencyAt(modeFrequencies_[i], i);
        }
        
        for (int i = 0; i < nLastModes; i++) {
            if (modes.rowNames_[i] != null) {
                correlation.setLastModeNameAt(modes.rowNames_[i], i);
            }
            
            correlation.setLastModeFrequencyAt(modes.modeFrequencies_[i], i);
        }
        
        for (int i = 0; i < nFirstModes; i++) {
            for (int j = 0; j < nLastModes; j++) {
                firstProduct    = 0.0;
                lastProduct     = 0.0;
                mixedProduct    = 0.0;
                normalProduct   = 0.0;
                
                for (int k = 0; k < nColumns_; k++) {
                    firstMassProduct    = 0.0;
                    lastMassProduct     = 0.0;
                    
                    for (int l = 0; l < k; l++) {
                        firstComponent      =  values_[(i * nColumns_) + l];
                        lastComponent       =
                                modes.values_[(j * nColumns_) + l];
                        massComponent       =  massValues[k][l];
                        firstMassProduct    += firstComponent * massComponent;
                        lastMassProduct     += lastComponent * massComponent;
                    }
                    
                    for (int l = k; l < nColumns_; l++) {
                        firstComponent      =  values_[(i * nColumns_) + l];
                        lastComponent       =
                                modes.values_[(j * nColumns_) + l];
                        massComponent       =  massValues[l][k];
                        firstMassProduct    += firstComponent * massComponent;
                        lastMassProduct     += lastComponent * massComponent;
                    }
                    
                    firstComponent  =  values_[(i * nColumns_) + k];
                    lastComponent   =  modes.values_[(j * nColumns_) + k];
                    firstProduct    += firstMassProduct * firstComponent;
                    lastProduct     += lastMassProduct * lastComponent;
                    mixedProduct    += firstMassProduct * lastComponent;
                }
                
                normalProduct += mixedProduct * mixedProduct;
                normalProduct /= firstProduct;
                normalProduct /= lastProduct;
                correlation.setCorrelationAt(normalProduct, i, j);
            }
        }
        
        return correlation;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getRowCount()}.
     * 
     * @return  the number of modes
     **************************************************************************/
    public int getModeCount() {
        return this.getRowCount();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the frequency of the mode at the given index.
     * 
     * @param   modeIndex   the mode index
     * @return  the frequency of the mode at the given index
     **************************************************************************/
    public double getModeFrequencyAt(int modeIndex) {
        return modeFrequencies_[modeIndex];
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getRowNameAt(int)}.
     * 
     * @param   modeIndex   the mode index
     * @return  the name of the mode at the given index
     **************************************************************************/
    public String getModeNameAt(int modeIndex) {
        return this.getRowNameAt(modeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getRowNames()}.
     * 
     * @return  a string array containing all mode names
     **************************************************************************/
    public String[] getModeNames() {
        return this.getRowNames();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of nodes in this matrix.
     * 
     * @return  the number of nodes
     **************************************************************************/
    public int getNodeCount() {
        return this.getColumnCount() / N_COMPONENTS;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #getColumnNames()}.
     * 
     * @return  a string array containing all node names
     **************************************************************************/
    public String[] getNodeNames() {
        return this.getColumnNames();
    } // eom
    
    
    
    /***************************************************************************
     * Makes a copy of this {@code ModeMatrix} and the given one, sorts each by
     * the corresponding given mode names and node names, and computes the
     * resulting {@link ModeCorrelationMatrix} from
     * {@link #computeDotProduct(de.iabg.mode.ModeMatrix)}.
     * 
     * @param   modes           the {@code ModeMatrix} that will be correlated
     *                          to this {@code ModeMatrix}
     * @param   firstModeKeys   the new order of the modes of this matrix
     * @param   firstNodeKeys   the new order of the nodes of this matrix
     * @param   lastModeKeys    the new order of the modes of the given matrix 
     * @param   lastNodeKeys    the new order of the nodes of the given matrix 
     * @return  the matrix of the dot product correlation
     **************************************************************************/
    public ModeCorrelationMatrix getReducedDotProduct(ModeMatrix modes,
            KeyList firstModeKeys, KeyList firstNodeKeys, KeyList lastModeKeys,
            KeyList lastNodeKeys) {
        ModeMatrix firstModes   = new ModeMatrix(this);
        ModeMatrix lastModes    = new ModeMatrix(modes);
        
        firstModes.sortModes(firstModeKeys);
        firstModes.sortNodes(firstNodeKeys);
        lastModes.sortModes(lastModeKeys);
        lastModes.sortNodes(lastNodeKeys);
        
        return firstModes.computeDotProduct(lastModes);
    } // eom
    
    
    
    /***************************************************************************
     * Makes a copy of this {@code ModeMatrix} and the given one, sorts each by
     * the corresponding given mode names and node names, and computes the
     * resulting {@link ModeCorrelationMatrix} from
     * {@link #computeGeneralizedMass(de.iabg.mode.MassMatrix,
     * de.iabg.mode.ModeMatrix)}.
     * 
     * @param   masses          the {@code MassMatrix} for the
     *                          {@code First Mode}
     * @param   modes           the {@code ModeMatrix} that will be correlated
     *                          to this {@code ModeMatrix}
     * @param   firstModeKeys   the new order of the modes of this matrix
     * @param   firstNodeKeys   the new order of the nodes of this matrix
     * @param   lastModeKeys    the new order of the modes of the given matrix 
     * @param   lastNodeKeys    the new order of the nodes of the given matrix 
     * @return  the matrix of the generalized mass correlation
     **************************************************************************/
    public ModeCorrelationMatrix getReducedGeneralizedMass(MassMatrix masses,
            ModeMatrix modes, KeyList firstModeKeys, KeyList firstNodeKeys,
            KeyList lastModeKeys, KeyList lastNodeKeys) {
        ModeMatrix firstModes   = new ModeMatrix(this);
        ModeMatrix lastModes    = new ModeMatrix(modes);
        
        firstModes.sortModes(firstModeKeys);
        firstModes.sortNodes(firstNodeKeys);
        lastModes.sortModes(lastModeKeys);
        lastModes.sortNodes(lastNodeKeys);
        
        return firstModes.computeGeneralizedMass(masses, lastModes);
    } // eom
    
    
    
    /***************************************************************************
     * Makes a copy of this {@code ModeMatrix} and the given one, sorts each by
     * the corresponding given mode names and node names, and computes the
     * resulting {@link ModeCorrelationMatrix} from
     * {@link #computeModalAssuranceCriterion(de.iabg.mode.ModeMatrix)}.
     * 
     * @param   modes           the {@code ModeMatrix} that will be correlated
     *                          to this {@code ModeMatrix}
     * @param   firstModeKeys   the new order of the modes of this matrix
     * @param   firstNodeKeys   the new order of the nodes of this matrix
     * @param   lastModeKeys    the new order of the modes of the given matrix 
     * @param   lastNodeKeys    the new order of the nodes of the given matrix 
     * @return  the matrix of the Modal Assurance Criterion correlation
     **************************************************************************/
    public ModeCorrelationMatrix getReducedModalAssuranceCriterion(
            ModeMatrix modes, KeyList firstModeKeys, KeyList firstNodeKeys,
            KeyList lastModeKeys, KeyList lastNodeKeys) {
        ModeMatrix firstModes   = new ModeMatrix(this);
        ModeMatrix lastModes    = new ModeMatrix(modes);
        
        firstModes.sortModes(firstModeKeys);
        firstModes.sortNodes(firstNodeKeys);
        lastModes.sortModes(lastModeKeys);
        lastModes.sortNodes(lastNodeKeys);
        
        return firstModes.computeModalAssuranceCriterion(lastModes);
    } // eom
    
    
    
    /***************************************************************************
     * Makes a copy of this {@code ModeMatrix} and the given one, sorts each by
     * the corresponding given mode names and node names, and computes the
     * resulting {@link ModeCorrelationMatrix} from
     * {@link #computeOrthogonalityCheck(de.iabg.mode.MassMatrix,
     * de.iabg.mode.ModeMatrix)}.
     * 
     * @param   masses          the {@code MassMatrix} for the
     *                          {@code First Mode}
     * @param   modes           the {@code ModeMatrix} that will be correlated
     *                          to this {@code ModeMatrix}
     * @param   firstModeKeys   the new order of the modes of this matrix
     * @param   firstNodeKeys   the new order of the nodes of this matrix
     * @param   lastModeKeys    the new order of the modes of the given matrix 
     * @param   lastNodeKeys    the new order of the nodes of the given matrix 
     * @return  the matrix of the normalized Orthogonality Check correlation
     **************************************************************************/
    public ModeCorrelationMatrix getReducedOrthogonalityCheck(MassMatrix masses,
            ModeMatrix modes, KeyList firstModeKeys, KeyList firstNodeKeys,
            KeyList lastModeKeys, KeyList lastNodeKeys) {
        ModeMatrix firstModes   = new ModeMatrix(this);
        ModeMatrix lastModes    = new ModeMatrix(modes);
        
        firstModes.sortModes(firstModeKeys);
        firstModes.sortNodes(firstNodeKeys);
        lastModes.sortModes(lastModeKeys);
        lastModes.sortNodes(lastNodeKeys);
        
        return firstModes.computeOrthogonalityCheck(masses, lastModes);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the given x-translation value at the
     * given mode and node indices.
     * 
     * @param   modeIndex   the index of the mode
     * @param   nodeIndex   the index of the node
     * @return  the x-translation at the given mode and node indices
     **************************************************************************/
    public double getXTranslationAt(int modeIndex, int nodeIndex) {
        return this.getValueAt(modeIndex, (nodeIndex * N_COMPONENTS) + X_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the given y-translation value at the
     * given mode and node indices.
     * 
     * @param   modeIndex   the index of the mode
     * @param   nodeIndex   the index of the node
     * @return  the y-translation at the given mode and node indices
     **************************************************************************/
    public double getYTranslationAt(int modeIndex, int nodeIndex) {
        return this.getValueAt(modeIndex, (nodeIndex * N_COMPONENTS) + Y_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which gets the given z-translation value at the
     * given mode and node indices.
     * 
     * @param   modeIndex   the index of the mode
     * @param   nodeIndex   the index of the node
     * @return  the z-translation at the given mode and node indices
     **************************************************************************/
    public double getZTranslationAt(int modeIndex, int nodeIndex) {
        return this.getValueAt(modeIndex, (nodeIndex * N_COMPONENTS) + Z_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public String paramString() {
        StringBuilder result = new StringBuilder();
        
        result.append(String.format(" %15s", ""));
        result.append(String.format(" %15s", "Node"));
        
        for (String columnName : columnNames_) {
            result.append(String.format(" %15s", columnName));
        }
        
        result.append(String.format("%n"));
        result.append(String.format(" %15s", "Mode"));
        result.append(String.format(" %15s", "Frequency"));
        
        for (int i = 0; i < nColumns_ / N_COMPONENTS; i++) {
            result.append(String.format(" %15s", "X-Translation"));
            result.append(String.format(" %15s", "Y-Translation"));
            result.append(String.format(" %15s", "Z-Translation"));
        }
        
        result.append(String.format("%n"));
        
        for (int i = 0; i < nRows_; i++) {
            result.append(String.format(" %15s", rowNames_[i]));
            result.append(String.format(" % 11.8e", modeFrequencies_[i]));
            
            for (int j = 0; j < nColumns_; j++) {
                result.append(String.format(" % 11.8e",
                        values_[(i * nColumns_) + j]));
            }
            
            result.append(String.format("%n"));
        }
        
        return result.toString();
    } // eom
    
    
    
    /***************************************************************************
     * This method does nothing and overrides the previous implementation
     * because the number of columns (coordinates) should not be directly
     * changed.
     **************************************************************************/
    @Override
    public void setColumnCount(int nColumns) {
        
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps {@link #setRowCount(int)}.
     * 
     * @param   nModes  the new number of modes
     **************************************************************************/
    public void setModeCount(int nModes) {
        this.setRowCount(nModes);
    } // eom
    
    
    
    /***************************************************************************
     * Sets the frequency of the mode at the given index to the specified value.
     * 
     * @param   modeFrequency   the new value of the frequency
     * @param   modeIndex       the index of the mode to be changed
     **************************************************************************/
    public void setModeFrequencyAt(double modeFrequency, int modeIndex) {
        modeFrequencies_[modeIndex] = modeFrequency;
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #setRowNameAt(java.lang.String, int)}.
     * 
     * @param   modeName    the new name of the mode
     * @param   modeIndex   the index of the mode to be changed
     **************************************************************************/
    public void setModeNameAt(String modeName, int modeIndex) {
        this.setRowNameAt(modeName, modeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Sets the number of nodes to the specified amount.  The data of all cells
     * may be lost due to resizing of the matrix.
     * 
     * @param   nNodes  the new number of nodes
     **************************************************************************/
    public void setNodeCount(int nNodes) {
        columnNames_    = new String[nNodes * N_COMPONENTS];
        nColumns_       = nNodes * N_COMPONENTS;
        values_         = new double[nRows_ * nNodes * N_COMPONENTS];
    } // eom
    
    
    
    /***************************************************************************
     * Sets the name of the node at the x, y, and z translations at the given
     * index.
     * 
     * @param   nodeName    the new name of the node
     * @param   nodeIndex   the index of the node to be changed
     **************************************************************************/
    public void setNodeNameAt(String nodeName, int nodeIndex) {
        this.setColumnNameAt(nodeName, (nodeIndex * N_COMPONENTS) + X_INDEX);
        this.setColumnNameAt(nodeName, (nodeIndex * N_COMPONENTS) + Y_INDEX);
        this.setColumnNameAt(nodeName, (nodeIndex * N_COMPONENTS) + Z_INDEX);
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
        modeFrequencies_    = new double[nRows];
        nRows_              = nRows;
        values_             = new double[nRows_ * nColumns_];
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given x-translation value at the
     * given mode and node indices.
     * 
     * @param   value       the new value of the x-translation
     * @param   modeIndex   the index of the mode to be changed
     * @param   nodeIndex   the index of the node to be changed
     **************************************************************************/
    public void setXTranslationAt(double value, int modeIndex, int nodeIndex) {
        this.setValueAt(value, modeIndex, (nodeIndex * N_COMPONENTS) + X_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given y-translation value at the
     * given mode and node indices.
     * 
     * @param   value       the new value of the y-translation
     * @param   modeIndex   the index of the mode to be changed
     * @param   nodeIndex   the index of the node to be changed
     **************************************************************************/
    public void setYTranslationAt(double value, int modeIndex, int nodeIndex) {
        this.setValueAt(value, modeIndex, (nodeIndex * N_COMPONENTS) + Y_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which sets the given z-translation value at the
     * given mode and node indices.
     * 
     * @param   value       the new value of the z-translation
     * @param   modeIndex   the index of the mode to be changed
     * @param   nodeIndex   the index of the node to be changed
     **************************************************************************/
    public void setZTranslationAt(double value, int modeIndex, int nodeIndex) {
        this.setValueAt(value, modeIndex, (nodeIndex * N_COMPONENTS) + Z_INDEX);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #sortRows(de.iabg.swing.KeyList)}.
     * 
     * @param   modeKeys    the new order of the modes
     **************************************************************************/
    public void sortModes(KeyList modeKeys) {
        this.sortRows(modeKeys);
    } // eom
    
    
    
    /***************************************************************************
     * A convenience method which wraps
     * {@link #sortColumns(de.iabg.swing.KeyList)}.
     * 
     * @param   nodeKeys    the new order of the nodes
     **************************************************************************/
    public void sortNodes(KeyList nodeKeys) {
        this.sortColumns(nodeKeys);
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
        double[]    modeFrequencies;
        double[]    values;
        int         nRows               = 0;
        int         iRow;
        int         nRow;
        
        for (String row : rowKeys.values()) {
            if (Arrays.asList(rowNames_).contains(row)) {
                nRows++;
            }
        }
        
        rowNames        = new String[nRows];
        modeFrequencies = new double[nRows];
        values          = new double[nRows * nColumns_];
        nRows           = 0;
        
        for (String rowKey : rowKeys.values()) {
            for (int i = 0; i < nRows_; i++) {
                if (rowKey.equals(rowNames_[i])) {
                    rowNames[nRows]         = rowNames_[i];
                    modeFrequencies[nRows]  = modeFrequencies_[i];
                    iRow                    = i * nColumns_;
                    nRow                    = nRows * nColumns_;
                    System.arraycopy(values_, iRow, values, nRow, nColumns_);
                    nRows++;
                }
            }
        }
        
        rowNames_           = rowNames;
        modeFrequencies_    = modeFrequencies;
        nRows_              = nRows;
        values_             = values;
    } // eom
    
    
    
    /***************************************************************************
     * This method does nothing and overrides the previous implementation
     * because the columns (coordinates) should not swapped.
     **************************************************************************/
    @Override
    public void swapColumns(int firstIndex, int lastIndex) {
        
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
        double      modeFrequency;
        
        rowName                 = rowNames_[firstIndex];
        rowNames_[firstIndex]   = rowNames_[lastIndex];
        rowNames_[lastIndex]    = rowName;
        
        modeFrequency                   = modeFrequencies_[firstIndex];
        modeFrequencies_[firstIndex]    = modeFrequencies_[lastIndex];
        modeFrequencies_[lastIndex]     = modeFrequency;
        
        firstIndex  *= nColumns_;
        lastIndex   *= nColumns_;
        
        System.arraycopy(values_, firstIndex, row, 0, nColumns_);
        System.arraycopy(values_, lastIndex, values_, firstIndex, nColumns_);
        System.arraycopy(row, 0, values_, lastIndex, nColumns_);
    } // eom
} // eoc