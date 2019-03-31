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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/*******************************************************************************
 * This {@link ItemListener} updates the log of the {@link JMeshPanel} whenever
 * a set is selected.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 26, 2008
 ******************************************************************************/
public class NodeKeyItemListener
        implements ItemListener {
    /** The {@code JMeshPanel} that this listener was designed for */
    protected JMeshPanel meshPanel_;
    
    /** The {@code MeshUI} that this listener was designed for */
    protected DefaultMeshUI meshUI_;
    
    
    
    /***************************************************************************
     * Constructs an {@link ItemListener} from the given {@code JMeshPanel} and
     * {@code MeshUI}.
     * 
     * @param   meshPanel   the {@code JMeshPanel} that this listener is
     *                      designed for
     * @param   meshUI      the {@code MeshUI} that this listener is designed
     *                      for
     **************************************************************************/
    public NodeKeyItemListener(JMeshPanel meshPanel, DefaultMeshUI meshUI) {
        meshPanel_  = meshPanel;
        meshUI_     = meshUI;
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener has changed its item.
     * 
     * @param   itemEvent  the {@link ItemEvent} from the item source
     **************************************************************************/
    public void itemStateChanged(ItemEvent itemEvent) {
        meshPanel_.fireLogChanged("Updated set selection:");
    } // eom
} // eoc