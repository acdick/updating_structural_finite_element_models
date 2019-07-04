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
package de.iabg.mesh.plaf;

import de.iabg.swing.filechooser.AbstractFilter;

/*******************************************************************************
 * This implementation of {@link javax.swing.filechooser.FileFilter} provides
 * the supported extensions for mesh files.  In general, the user should create
 * one {@code MeshFilter} object of each supported type and attach them to the
 * {@code JFileChooser}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public class MeshFilter extends AbstractFilter {
    /** An extension for Nastran *.bdf files */
    public static final String BDF = "BDF";
    
    /** An extension for Nastran *.dat files */
    public static final String DAT = "DAT";
    
    /** An extension for Nastran *.nas files */
    public static final String NAS = "NAS";
    
    /** An extension for Nastran *.nset files */
    public static final String NSET = "NSET";
    
    /** An extension for Nastran *.set files */
    public static final String SET = "SET";
    
    /** An extension for ASCII *.txt files */
    public static final String TXT = "TXT";
    
    
    
    /***************************************************************************
     * Constructs a {@code MeshFilter} with the given extension.
     * 
     * @param   extension   the extension of the accepted file type
     **************************************************************************/
    public MeshFilter(String extension) {
        super(extension.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Returns the description of this {@code MeshFilter}.
     * 
     * @return  the description of this filter
     **************************************************************************/
    public String getDescription() {
        String description = null;
        
        if (extension_.equals(BDF)) {
            description = String.format("Nastran Bulk Data Files (*.bdf)");
        }
        else if (extension_.equals(DAT)) {
            description = String.format("Nastran Bulk Data Files (*.dat)");
        }
        else if (extension_.equals(NAS)) {
            description = String.format("Nastran Bulk Data Files (*.nas)");
        }
        else if (extension_.equals(NSET)) {
            description = String.format("Nastran Set Files (*.nset)");
        }
        else if (extension_.equals(SET)) {
            description = String.format("Nastran Set Files (*.set)");
        }
        else if (extension_.equals(TXT)) {
            description = String.format("ASCII Mesh Connection Files (*.txt)");
        }
        
        return description;
    } // eom
} // eoc