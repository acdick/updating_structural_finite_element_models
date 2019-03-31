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
package de.iabg.mesh.event;

import de.iabg.mesh.JMeshConnectionPanel;

import de.iabg.mesh.plaf.RigidMeshConnectionUI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/*******************************************************************************
 * This {@link ItemListener} updates the {@link JMeshConnectionPanel} whenever
 * the connection degrees of freedom change.
 * 
 * @author  Adam C. Dick, BSE
 * @version November 1, 2008
 ******************************************************************************/
public class ConnectionDOFItemListener
        implements ItemListener {
    /** The {@code JMeshConnectionPanel} that this listener was designed for */
    protected JMeshConnectionPanel connectionPanel_;
    
    /** The {@code MeshConnectionUI} that this listener was designed for */
    protected RigidMeshConnectionUI connectionUI_;
    
    
    
    /***************************************************************************
     * Constructs an {@link ItemListener} from the given
     * {@code JMeshConnectionPanel} and {@code MeshConnectionUI}.
     * 
     * @param   connectionPanel the {@code JMeshConnectionPanel} that this
     *                          listener is designed for
     * @param   connectionUI    the {@code MeshConnectionUI} that this listener
     *                          is designed for
     **************************************************************************/
    public ConnectionDOFItemListener(JMeshConnectionPanel connectionPanel,
            RigidMeshConnectionUI connectionUI) {
        connectionPanel_    = connectionPanel;
        connectionUI_       = connectionUI;
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener has changed its item.
     * 
     * @param   itemEvent  the {@link ItemEvent} from the item source
     **************************************************************************/
    public void itemStateChanged(ItemEvent itemEvent) {
        String connectionDOF = connectionUI_.getConnectionDegreesOfFreedom();
        
        connectionPanel_.setConnectionDegreesOfFreedom(connectionDOF);
    } // eom
} // eoc