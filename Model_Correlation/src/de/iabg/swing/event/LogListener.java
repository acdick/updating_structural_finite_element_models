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
package de.iabg.swing.event;

import java.util.EventListener;

/*******************************************************************************
 * Defines an object which listens for {@code LogEvents}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public interface LogListener
        extends EventListener {
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener has changed its log.
     * 
     * @param   logEvent    the {@link LogEvent} from the log source
     **************************************************************************/
    public void logChanged(LogEvent logEvent);
} // eoi