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

import de.iabg.swing.Loggable;

/*******************************************************************************
 * This implementation of a {@link LogListener} links one {@link Loggable}
 * object with another.  This is usually necessary when an object aggregates
 * {@code Loggable} objects and needs to be notified when each log is changed.
 * The notification of the log change is chained through each subsequent
 * {@code Loggable} object until it reaches a {@code Logger}, which finally
 * handles the log.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class LoggableLogListener
        implements LogListener {
    /** The {@code Loggable} object that is notified of log changes */
    protected Loggable loggable_;
    
    
    
    /***************************************************************************
     * Constructs a {@code LogListener} with a reference to the given
     * {@code Loggable}.
     * 
     * @param   loggable    the object to be notified of a log change
     **************************************************************************/
    public LoggableLogListener(Loggable loggable) {
        loggable_ = loggable;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void logChanged(LogEvent logEvent) {
        loggable_.fireLogChanged(logEvent.getLog());
    } // eom
} // eoc