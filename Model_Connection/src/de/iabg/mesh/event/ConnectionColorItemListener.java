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

import de.iabg.mesh.plaf.DefaultMeshConnectionUI;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/*******************************************************************************
 * This {@link ItemListener} updates the 3D scene of the
 * {@link JMeshConnectionPanel} whenever the node connection color changes.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class ConnectionColorItemListener
        implements ItemListener {
    /** The {@code JMeshConnectionPanel} that this listener was designed for */
    protected JMeshConnectionPanel connectionPanel_;
    
    /** The {@code MeshConnectionUI} that this listener was designed for */
    protected DefaultMeshConnectionUI connectionUI_;
    
    
    
    /***************************************************************************
     * Constructs an {@link ItemListener} from the given
     * {@code JMeshConnectionPanel} and {@code MeshConnectionUI}.
     * 
     * @param   connectionPanel the {@code JMeshConnectionPanel} that this
     *                          listener is designed for
     * @param   connectionUI    the {@code MeshConnectionUI} that this listener
     *                          is designed for
     **************************************************************************/
    public ConnectionColorItemListener(JMeshConnectionPanel connectionPanel,
            DefaultMeshConnectionUI connectionUI) {
        connectionPanel_    = connectionPanel;
        connectionUI_       = connectionUI;
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener has changed its item.
     * 
     * @param   itemEvent  the {@link ItemEvent} from the item source
     **************************************************************************/
    public void itemStateChanged(ItemEvent itemEvent) {
        connectionPanel_.fireScene3DChanged(connectionPanel_);
    } // eom
} // eoc