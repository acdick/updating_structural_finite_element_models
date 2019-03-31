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

import java.io.File;
import java.io.IOException;

/*******************************************************************************
 * This interface declares basic functionality for writing a file containing
 * mode correlation data.  In general, the last method the user should call is
 * {@link #exportModeCorrelations(de.iabg.mode.ModeCorrelationMatrix)}.  All
 * other methods declared in this interface may be required to successfully
 * write the data and therefore, should be called beforehand.  If any data is
 * omitted, the output data may give unpredicatable results.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public interface ModeCorrelationWriter {
    
    
    
    /***************************************************************************
     * Writes the given mode correlations and to an output file.  This method
     * should be called after all other methods declared in this interface so
     * that data can be successfully written.
     * 
     * @param   modeCorrelations    the {@link ModeCorrelationMatrix} to be
     *                              written
     * @throws  java.io.IOException if the file could not be written
     **************************************************************************/
    public void exportModeCorrelations(ModeCorrelationMatrix modeCorrelations)
            throws IOException;
    
    
    
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
     * Internally saves the name to be written with any exported data.
     * 
     * @param   firstName   the name of the {@code First Mesh}
     **************************************************************************/
    public void setFirstName(String firstName);
    
    
    
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
     * Internally saves the name to be written with any exported data.
     * 
     * @param   lastName   the name of the {@code Last Mesh}
     **************************************************************************/
    public void setLastName(String lastName);
} // eoi