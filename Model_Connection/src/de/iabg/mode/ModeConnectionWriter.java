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

import de.iabg.mesh.NodeConnectionMatrix;

import java.io.File;
import java.io.IOException;

/*******************************************************************************
 * This interface declares basic functionality for writing a file containing
 * mode connection data.  In general, the last method the user should call is
 * {@link #exportModeConnections(de.iabg.mode.ModeConnectionMatrix,
 * de.iabg.mesh.NodeConnectionMatrix)}.  All other methods declared in this
 * interface may be required to successfully write the data and therefore,
 * should be called beforehand.  If any data is omitted, the output data may
 * give unpredicatable results.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public interface ModeConnectionWriter {
    
    
    
    /***************************************************************************
     * Writes the given mode connections and node connections as an optimization
     * file.  This method should be called after all other methods declared in
     * this interface so that data can be successfully written.
     * 
     * @param   modeConnections     the {@link ModeConnectionMatrix} to be
     *                              written
     * @param   nodeConnections     the {@link NodeConnectionMatrix} to be
     *                              written
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public void exportModeConnections(ModeConnectionMatrix modeConnections,
            NodeConnectionMatrix nodeConnections)
            throws IOException;
    
    
    
    /***************************************************************************
     * Internally saves the correlation weights to be written with any exported
     * data.
     * 
     * @param   correlationWeights  the correlation weights
     **************************************************************************/
    public void setCorrelationWeights(double[] correlationWeights);
    
    
    
    /***************************************************************************
     * Internally saves the initial equation identifier to be written with any
     * exported data.
     * 
     * @param   equationIdentifier  the initial equation identifier
     **************************************************************************/
    public void setEquationIdentifier(int equationIdentifier);
    
    
    
    /***************************************************************************
     * Internally saves the file data to be written with any exported data.
     * 
     * @param   firstMeshFile   the file of the {@code First Mesh}
     **************************************************************************/
    public void setFirstMeshFile(File firstMeshFile);
    
    
    
    /***************************************************************************
     * Internally saves the file data to be written with any exported data.
     * 
     * @param   firstModeFile   the file of the {@code First Mode}
     **************************************************************************/
    public void setFirstModeFile(File firstModeFile);
    
    
    
    /***************************************************************************
     * Internally saves the {@code First Modes} to be written with any exported
     * data.
     * 
     * @param   firstModes  the {@code First Modes}
     **************************************************************************/
    public void setFirstModes(ModeMatrix firstModes);
    
    
    
    /***************************************************************************
     * Internally saves the frequency weights to be written with any exported
     * data.
     * 
     * @param   frequencyWeights  the frequency weights
     **************************************************************************/
    public void setFrequencyWeights(double[] frequencyWeights);
    
    
    
    /***************************************************************************
     * Internally saves the file data to be written with any exported data.
     * 
     * @param   lastMeshFile   the file of the {@code Last Mesh}
     **************************************************************************/
    public void setLastMeshFile(File lastMeshFile);
    
    
    
    /***************************************************************************
     * Internally saves the file data to be written with any exported data.
     * 
     * @param   lastModeFile    the file of the {@code Last Mode}
     **************************************************************************/
    public void setLastModeFile(File lastModeFile);
    
    
    
    /***************************************************************************
     * Internally saves the {@code Last Modes} to be written with any exported
     * data.
     * 
     * @param   lastModes   the {@code Last Modes}
     **************************************************************************/
    public void setLastModes(ModeMatrix lastModes);
    
    
    
    /***************************************************************************
     * Internally saves the selected mass matrix to be written with any exported
     * data.
     * 
     * @param   masses   the selected mass matrix
     **************************************************************************/
    public void setMasses(MassMatrix masses);
    
    
    
    /***************************************************************************
     * Internally saves the file data to be written with any exported data.
     * 
     * @param   massFile    the file of the selected mass matrix
     **************************************************************************/
    public void setMassFile(File massFile);
} // eoc