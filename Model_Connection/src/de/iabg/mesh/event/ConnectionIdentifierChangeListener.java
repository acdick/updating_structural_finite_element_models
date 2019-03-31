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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*******************************************************************************
 * This {@link ChangeListener} updates the {@link JMeshConnectionPanel} whenever
 * the connection identifier changes.
 * 
 * @author  Adam C. Dick, BSE
 * @version November 1, 2008
 ******************************************************************************/
public class ConnectionIdentifierChangeListener
        implements ChangeListener {
    /** The {@code JMeshConnectionPanel} that this listener was designed for */
    protected JMeshConnectionPanel connectionPanel_;
    
    /** The {@code MeshConnectionUI} that this listener was designed for */
    protected RigidMeshConnectionUI connectionUI_;
    
    
    
    /***************************************************************************
     * Constructs a {@link ChangeListener} from the given
     * {@code JMeshConnectionPanel} and {@code MeshConnectionUI}.
     * 
     * @param   connectionPanel the {@code JMeshConnectionPanel} that this
     *                          listener is designed for
     * @param   connectionUI    the {@code MeshConnectionUI} that this listener
     *                          is designed for
     **************************************************************************/
    public ConnectionIdentifierChangeListener(
            JMeshConnectionPanel connectionPanel,
            RigidMeshConnectionUI connectionUI) {
        connectionPanel_    = connectionPanel;
        connectionUI_       = connectionUI;
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener has changed its item.
     * 
     * @param   changeEvent  the {@link ChangeEvent} from the change source
     **************************************************************************/
    public void stateChanged(ChangeEvent changeEvent) {
        int connectionIdentifier = connectionUI_.getConnectionIdentifier();
        
        connectionPanel_.setConnectionIdentifier(connectionIdentifier);
    } // eom
} // eoc