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
package de.iabg.swing;

/*******************************************************************************
 * This interface declares basic functionality for an object that can log
 * events.  The ability to add {@link Loggable} objects allows the
 * {@code Logger} to update the log from multiple sources.
 * 
 * Typically, a {@code Logger} will register listeners on a {@code Loggable}
 * object and simply listen for string messages.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public interface Logger {
    
    
    
    /***************************************************************************
     * Adds a {@link Loggable} object to this {@code Logger}.
     * 
     * @param   loggable    the {@code Loggable} object to add
     **************************************************************************/
    public void addLoggable(Loggable loggable);
    
    
    
    /***************************************************************************
     * Removes a {@link Loggable} object from this {@code Logger}.
     * 
     * @param   loggable    the {@code Loggable} object to remove
     **************************************************************************/
    public void removeLoggable(Loggable loggable);
    
    
    
    /***************************************************************************
     * Updates the log of the given {@link Loggable} object
     * 
     * @param   log the string of the log
     **************************************************************************/
    public void updateLog(String log);
} // eoi