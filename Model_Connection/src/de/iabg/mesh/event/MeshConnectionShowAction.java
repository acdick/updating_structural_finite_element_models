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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/*******************************************************************************
 * This {@code Action} updates the 3D scene of the {@link JMeshConnectionPanel}
 * whenever an action is performed.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public class MeshConnectionShowAction extends AbstractAction {
    /** The {@code JMeshConnectionPanel} that this listener was designed for */
    protected JMeshConnectionPanel connectionPanel_;
    
    /** The {@code MeshConnectionUI} that this listener was designed for */
    protected DefaultMeshConnectionUI connectionUI_;
    
    
    
    /***************************************************************************
     * Constructs an {@code Action} from the given {@code JMeshConnectionPanel}
     * and {@code MeshConnectionUI}.
     * 
     * @param   connectionPanel the {@code JMeshConnectionPanel} that this
     *                          listener is designed for
     * @param   connectionUI    the {@code MeshConnectionUI} that this listener
     *                          is designed for
     **************************************************************************/
    public MeshConnectionShowAction(JMeshConnectionPanel connectionPanel,
            DefaultMeshConnectionUI connectionUI) {
        super("Display");
        
        connectionPanel_    = connectionPanel;
        connectionUI_       = connectionUI;
        
        this.putValue(SHORT_DESCRIPTION, "Display mesh connection");
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener performs an action.
     * 
     * @param   actionEvent the {@link ActionEvent} from the action source
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        connectionPanel_.fireScene3DChanged(connectionPanel_);
    } // eom
} // eoc