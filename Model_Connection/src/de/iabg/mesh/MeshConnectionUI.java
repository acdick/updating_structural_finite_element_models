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
package de.iabg.mesh;

/*******************************************************************************
 * This interface declares basic functionality for the user interface of a
 * {@link JMeshConnectionPanel}.  This interface tries to follow the structure
 * described in Swing for a Model-View-Controller.  Generally, the
 * {@code JMeshConnectionPanel} is instantiated and then a
 * {@code MeshConnectionUI} is installed onto it.  Any implementing classes of
 * this interface must be able to add and remove their own layout, components,
 * listeners, keyboard actions, and defaults.  If the user interface is not
 * completely uninstalled when called, it can lead to unpredictable results in
 * the {@code JMeshConnectionPanel}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public interface MeshConnectionUI {
    
    
    
    /***************************************************************************
     * Installs layout, components, listeners, keyboard actions, defaults, and
     * initializes any values or fields.
     * 
     * @param   connectionPanel the {@link JMeshConnectionPanel} where the user
     *                          interface will be installed
     **************************************************************************/
    public void installUI(JMeshConnectionPanel connectionPanel);
    
    
    
    /***************************************************************************
     * Uninstalls layout, components, listeners, keyboard actions, and defaults.
     * 
     * @param   connectionPanel the {@link JMeshConnectionPanel} where the user
     *                          interface will be uninstalled
     **************************************************************************/
    public void uninstallUI(JMeshConnectionPanel connectionPanel);
} // eoi