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

import de.iabg.j3d.Renderable;

import de.iabg.mesh.JMeshConnectionPanel;

import de.iabg.swing.KeyList;
import de.iabg.swing.Loggable;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.ComboBoxModel;
import javax.swing.JTable;

import javax.swing.table.TableModel;

/*******************************************************************************
 * This interface declares basic functionality for correlating two mode shape
 * matrices.  This includes the ability to compute the modal correlation between
 * two {@link JModePanel} objects and export the crrelation data to different
 * file types.  From the correlation data, implementations of this class should
 * also be able to compute the conneciton between the two {@code JModePanel}
 * objects and export the connection data as an optimization deck input file.
 * The connected modes can be selected, deselected, or altered during runtime.
 * Necessary information for generating the optimization file should be editable
 * at runtime in a table.  This information includes weighting for each mode
 * frequency and each mode correlation value, as well as the ability to switch
 * a mode mapping between the {@code First Modes} and {@code Last Modes} as
 * the user desires.
 * 
 * This {@code ModeConnectionModel} should also be {@link Renderable}, meaning
 * that a Java3D scene should be created from the stored data.  There should
 * be the functionality to display the matrix that this correlation represents
 * as a 2D or 3D scene, where a direct visualization of the matrix can be seen.
 * The colors of each cell of this matrix should be based on a lower tolerance
 * and upper tolerance, where values below the lower tolerance are green, those
 * between the lower and upper tolerances are yellow, and those greater than
 * the upper tolerance are red.
 * 
 * This {@code ModeConnectionModel} should also be {@link Loggable}, meaning
 * that for important user-controlled events, log messages should be fired so
 * that the user can have a command history and be able to view the current
 * status of the stored data.  There must be methods to access the data for
 * calculations or output.  Before output data can be made, queries must be
 * available to check if the data is already stored.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 12, 2008
 ******************************************************************************/
public interface ModeCorrelationModel
        extends Loggable,
                Renderable {
    
    
    
    /***************************************************************************
     * Computes and stores the modal correlation between the {@code First Mode}
     * and the {@code Last Mode}.
     **************************************************************************/
    public void correlateModes();
    
    
    
    /***************************************************************************
     * Deselects all currently stored {@code First Modes}.
     **************************************************************************/
    public void deselectAllFirstModeKeys();
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link ModeConnectionWriter} for the given file
     * and exports all stored mode connections.
     * 
     * @param   fileName            the file to be exported
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public void exportModeConnection(String fileName)
            throws IOException;
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link ModeCorrelationWriter} for the given file
     * and exports all stored mode correlations.
     * 
     * @param   fileName            the file to be exported
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public void exportModeCorrelation(String fileName)
            throws IOException;
    
    
    
    /***************************************************************************
     * Returns the currently stored {@code First Modes}.
     * 
     * @return  the {@link JModePanel} of the {@code First Modes}
     **************************************************************************/
    public JModePanel getFirstMode();
    
    
    
    /***************************************************************************
     * Returns the {@link KeyList} of the currently selected
     * {@code First Modes}.
     * 
     * @return  the {@link KeyList} of the currently selected
     *          {@code First Modes}
     **************************************************************************/
    public KeyList getFirstSelectedModeKeys();
    
    
    
    /***************************************************************************
     * Returns the currently stored {@code Last Modes}.
     * 
     * @return  the {@link JModePanel} of the {@code Last Modes}
     **************************************************************************/
    public JModePanel getLastMode();
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.ComboBoxModel} of the mass matrices.
     * 
     * @return  the {@link javax.swing.ComboBoxModel} of the mass matrices
     **************************************************************************/
    public ComboBoxModel getMassKeyModel();
    
    
    
    /***************************************************************************
     * Returns the currently stored mass matrices.
     * 
     * @return  the {@link MassMatrixMap} of the mass matrices
     **************************************************************************/
    public MassMatrixMap getMassMatrices();
    
    
    
    /***************************************************************************
     * Returns the currently stored {@link JMeshConnectionPanel} of the
     * {@code First Mesh} and {@code Last Mesh}.
     * 
     * @return  the {@code JMeshConnectionPanel} of the {@code First Mesh} and
     *          {@code Last Mesh}
     **************************************************************************/
    public JMeshConnectionPanel getMeshConnection();
    
    
    
    /***************************************************************************
     * Returns the currently stored mode correlations
     * 
     * @return  the {@link ModeCorrelationMatrix} storing the mode correlations
     **************************************************************************/
    public ModeCorrelationMatrix getModeCorrelation();
    
    
    
    /***************************************************************************
     * Returns the {@link javax.swing.table.TableModel} of the mode correlation.
     * 
     * @return  the {@link javax.swing.table.TableModel} of the mode correlation
     **************************************************************************/
    public TableModel getModeCorrelationTableModel();
    
    
    
    /***************************************************************************
     * Tests if there is a currently selected mass matrix.
     * 
     * @return  {@code true} if there is a currently selected mass matrix;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasMassKey();
    
    
    
    /***************************************************************************
     * Tests if there are any currently stored mass matrices.
     * 
     * @return  {@code true} if if there are any currently stored mass matrices;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasMassMatrices();
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mode} and the {@code Last Mode} are both
     * storing any currently selected mode selections.
     * 
     * @return  {@code true} if the {@code First Mode} and the {@code Last Mode}
     *          are both storing any currently selected mode selections;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean hasModeKeys();
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mode} and the {@code Last Mode} are both
     * storing any modes.
     * 
     * @return  {@code true} if the {@code First Mode} and the {@code Last Mode}
     *          both store any modes; {@code false} otherwise
     **************************************************************************/
    public boolean hasModes();
    
    
    
    /***************************************************************************
     * Selects the appropriate {@link MaterialReader} for the given file
     * and imports all the available material contained in the file.  This
     * includes the mass and stiffness matrices.
     * 
     * @param   fileName                        the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     * @throws  java.io.IOException             if the file could not be read
     **************************************************************************/
    public void importMassMatrices(String fileName)
            throws  FileNotFoundException,
                    IOException;
    
    
    
    /***************************************************************************
     * Tests if the {@code First Mode}, {@code Last Mode}, mass matrices and
     * {@link JMeshConnectionPanel} are consistent.
     * 
     * @return  {@code true} if the {@code First Mode}, {@code Last Mode},
     *          mass matrices and {@link JMeshConnectionPanel} are consistent;
     *          {@code false} otherwise
     **************************************************************************/
    public boolean isConsistent();
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeCorrelationModel} is storing any connections
     * between the {@code First Mode} and the {@code Last Mode}.
     * 
     * @return  {@code true} if this {@code ModeCorrelationModel} stores any
     *          mode connections; {@code false} otherwise
     **************************************************************************/
    public boolean isModeConnected();
    
    
    
    /***************************************************************************
     * Tests if this {@code ModeCorrelationModel} is storing any correlations
     * between the {@code First Mode} and the {@code Last Mode}.
     * 
     * @return  {@code true} if this {@code ModeCorrelationModel} stores any
     *          mode correlations; {@code false} otherwise
     **************************************************************************/
    public boolean isModeCorrelated();
    
    
    
    /***************************************************************************
     * Tests if the {@code JMeshConnectionPanel} is storing any connections
     * between the {@code First Mesh} and the {@code Last Mesh}.
     * 
     * @return  {@code true} if the {@code JMeshConnectionPanel} stores any
     *          node connections; {@code false} otherwise
     **************************************************************************/
    public boolean isNodeConnected();
    
    
    
    /***************************************************************************
     * Selects all currently stored {@code First Modes}.
     **************************************************************************/
    public void selectAllFirstModeKeys();
    
    
    
    /***************************************************************************
     * Sets all the column properties for the given {@link javax.swing.JTable}.
     * 
     * @param   correlationTable    the {@code JTable} whose columns properties
     *                              are set
     **************************************************************************/
    public void setColumnProperties(JTable correlationTable);
    
    
    
    /***************************************************************************
     * Sets the default mode connection between the {@code FirstMode} and
     * {@code Last Mode} by calling
     * {@link ModeCorrelationMatrix#getPreferredConnection(double)} of the
     * stored {@code ModeCorrelationMatrix}.
     **************************************************************************/
    public void setDefaultKeys();
    
    
    
    /***************************************************************************
     * Sets the value of the lower tolerance to the given value.  The lower
     * tolerance cannot be greater than the upper tolerance.
     * 
     * @param   lowerTolerance  the new lower tolerance
     **************************************************************************/
    public void setLowerTolerance(double lowerTolerance);
    
    
    
    /***************************************************************************
     * Sets the value of the upper tolerance to the given value.  The upper
     * tolerance cannot be less than the lower tolerance.
     * 
     * @param   upperTolerance  the new upper tolerance
     **************************************************************************/
    public void setUpperTolerance(double upperTolerance);
} // eoi