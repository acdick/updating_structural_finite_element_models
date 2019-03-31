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

import de.iabg.swing.event.LogListener;

/*******************************************************************************
 * This interface declares basic functionality for an object that can send log
 * strings to registered listeners.  The use of listeners allows a
 * {@link Logger} to update the log by sending messages.
 * 
 * Typically, a {@code Logger} will register listeners on a {@code Loggable}
 * object and simply listen for string messages.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public interface Loggable {
    
    
    
    /***************************************************************************
     * Adds a {@link LogListener} to this object.
     * 
     * @param   listener  the {@code LogListener} to add
     **************************************************************************/
    public void addLogListener(LogListener listener);
    
    
    
    /***************************************************************************
     * Sends a string of the log to each listener.  This method reports a string
     * that should be handled by a {@link Logger}.
     * 
     * @param   log the string of the log
     **************************************************************************/
    public void fireLogChanged(String log);
    
    
    
    /***************************************************************************
     * Removes a {@link LogListener} from this object.
     * 
     * @param   listener  the {@code LogListener} to remove
     **************************************************************************/
    public void removeLogListener(LogListener listener);
} // eoi