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
package de.iabg.swing.filechooser;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/*******************************************************************************
 * This abstract implementation of {@link javax.swing.filechooser.FileFilter}
 * provides subclasses with the ability to be a filter for a single given
 * extension.  The {@link #accept(java.io.File)} method checks the extension of
 * the file and accepts a file only if it matches the stored extension.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public abstract class AbstractFilter extends FileFilter {
    /** The extension of the files that this filter allows */
    protected String extension_;
    
    
    
    /***************************************************************************
     * Constructs a filter with the given extension.
     * 
     * @param   extension   the extension of the accepted file type
     **************************************************************************/
    public AbstractFilter(String extension) {
        super();
        
        extension_ = extension.trim();
    } // eom
    
    
    
    /***************************************************************************
     * Tests whether or not the specified abstract pathname should be included
     * in a pathname list.  Directories and files with the given extension are
     * accepted.
     * 
     * @param   file    The file to be tested
     * @return  {@code true} if the file should be accepted; {@code false}
     *          otherwise
     **************************************************************************/
    public boolean accept(File file) {
        boolean accept      = false;
        String  extension   = this.getExtension(file);
        
        if (file.isDirectory()) {
            accept = true;
        }
        else if (extension != null) {
            if (extension_.equals(extension)) {
                accept = true;
            }
        }
        
        return accept;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the extension of the given file as an upper case string.
     * 
     * @param   file    the file whose extension is checked
     * @return  the string extension of the given file
     **************************************************************************/
    protected String getExtension(File file) {
        String  extension   = null;
        String  fileName    = file.getName();
        int     index;
        
        index = fileName.lastIndexOf(".");
        if (index > 0 &&  index < fileName.length() - 1) {
            extension = fileName.substring(index + 1).toUpperCase().trim();
        }
        
        return extension;
    } // eom
} // eoc