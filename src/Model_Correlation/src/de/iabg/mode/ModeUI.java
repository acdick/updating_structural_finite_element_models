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

/*******************************************************************************
 * This interface declares basic functionality for the user interface of a
 * {@link JModePanel}.  This interface tries to follow the structure described
 * in Swing for a Model-View-Controller.  Generally, the {@code JModePanel} is
 * instantiated and then a {@code ModeUI} is installed onto it.  Any
 * implementing classes of this interface must be able to add and remove their
 * own layout, components, listeners, keyboard actions, and defaults.  If the
 * user interface is not completely uninstalled when called, it can lead to
 * unpredictable results in the {@code JModePanel}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public interface ModeUI {
    
    
    
    /***************************************************************************
     * Installs layout, components, listeners, keyboard actions, defaults, and
     * initializes any values or fields.
     * 
     * @param   modePanel   the {@link JModePanel} where the user interface will
     *                      be installed
     **************************************************************************/
    public void installUI(JModePanel modePanel);
    
    
    
    /***************************************************************************
     * Uninstalls layout, components, listeners, keyboard actions, and defaults.
     * 
     * @param   modePanel   the {@link JModePanel} where the user interface will
     *                      be uninstalled
     **************************************************************************/
    public void uninstallUI(JModePanel modePanel);
} // eoi