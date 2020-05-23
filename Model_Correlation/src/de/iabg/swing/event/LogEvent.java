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

import java.util.EventObject;

/*******************************************************************************
 * {@code LogEvent} is used to notify interested parties that the log has
 * changed from the event source.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class LogEvent extends EventObject {
    /** */
    protected String log_;
    
    
    
    /***************************************************************************
     * Constructs a {@code LogEvent} object.
     * 
     * @param   source  the source of the event (typically {@code this}),
     *                  however, it may be another source if log sources are
     *                  aggregated
     * @param   log     the string of the log
     **************************************************************************/
    public LogEvent(Object source, String log) {
        super(source);
        
        log_ = log;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the log of this {@code LogEvent}.
     * 
     * @return  the log of this {@code LogEvent}
     **************************************************************************/
    public String getLog() {
        return log_;
    } // eom
} // eoc