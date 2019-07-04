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

import de.iabg.swing.Logger;

/*******************************************************************************
 * This implementation of a {@link LogListener} links a {@code Logger} with a
 * {@code Loggable} object.  The {@code Logger} typically creates an instance of
 * this class and registers the instance onto one or more {@code Loggable}
 * objects.  The {@code Loggable} objects fire log strings to this listener,
 * which notifies the {@code Logger} to update the log.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class LoggerLogListener
        implements LogListener {
    /** The {@code Logger} that handles log events */
    protected Logger logger_;
    
    
    
    /***************************************************************************
     * Constructs a {@code LogListener} with a reference to the given
     * {@code Logger}.
     * 
     * @param   logger  the object that handles the log
     **************************************************************************/
    public LoggerLogListener(Logger logger) {
        logger_ = logger;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void logChanged(LogEvent logEvent) {
        logger_.updateLog(logEvent.getLog());
    } // eom
} // eoc