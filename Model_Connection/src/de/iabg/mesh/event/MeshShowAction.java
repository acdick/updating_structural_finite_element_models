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

import de.iabg.mesh.JMeshPanel;

import de.iabg.mesh.plaf.DefaultMeshUI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/*******************************************************************************
 * This {@code Action} updates the 3D scene of the {@link JMeshPanel} whenever
 * an action is performed.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public class MeshShowAction extends AbstractAction {
    /** The {@code JMeshPanel} that this listener was designed for */
    protected JMeshPanel meshPanel_;
    
    /** The {@code MeshUI} that this listener was designed for */
    protected DefaultMeshUI meshUI_;
    
    
    
    /***************************************************************************
     * Constructs an {@code Action} from the given {@code JMeshPanel} and
     * {@code MeshUI}.
     * 
     * @param   meshPanel   the {@code JMeshPanel} that this listener is
     *                      designed for
     * @param   meshUI      the {@code MeshUI} that this listener is designed
     *                      for
     **************************************************************************/
    public MeshShowAction(JMeshPanel meshPanel, DefaultMeshUI meshUI) {
        super("Display");
        
        meshPanel_  = meshPanel;
        meshUI_     = meshUI;
        
        this.putValue(SHORT_DESCRIPTION, "Display mesh model");
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener performs an action.
     * 
     * @param   actionEvent the {@link ActionEvent} from the action source
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        meshPanel_.fireScene3DChanged(meshPanel_);
    } // eom
} // eoc