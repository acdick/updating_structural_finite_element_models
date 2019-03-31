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
package de.iabg.mode.plaf;

import de.iabg.swing.filechooser.AbstractFilter;

/*******************************************************************************
 * This implementation of {@link javax.swing.filechooser.FileFilter} provides
 * the supported extensions for mode files.  In general, the user should create
 * one {@code ModeFilter} object of each supported type and attach them to the
 * {@code JFileChooser}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class ModeFilter extends AbstractFilter {
    /** An extension for Excel *.csv files */
    public static final String CSV = "CSV";
    
    /** An extension for Nastran *.nas files */
    public static final String NAS = "NAS";
    
    /** An extension for Nastran *.pch files */
    public static final String PCH = "PCH";
    
    /** An extension for Universal *.unv files */
    public static final String UNV = "UNV";
    
    
    
    /***************************************************************************
     * Constructs a {@code ModeFilter} with the given extension.
     * 
     * @param   extension   the extension of the accepted file type
     **************************************************************************/
    public ModeFilter(String extension) {
        super(extension.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Returns the description of this {@code ModeFilter}.
     * 
     * @return  the description of this filter
     **************************************************************************/
    public String getDescription() {
        String description = null;
        
        if (extension_.equals(CSV)) {
            description = String.format("Excel Mode Correlation Files (*.csv)");
        }
        else if (extension_.equals(NAS)) {
            description = String.format("Nastran Optimization Files (*.nas)");
        }
        else if (extension_.equals(PCH)) {
            description = String.format("Nastran Punch Files (*.pch)");
        }
        else if (extension_.equals(UNV)) {
            description = String.format("Universal Mode Files (*.unv)");
        }
        
        return description;
    } // eom
} // eoc