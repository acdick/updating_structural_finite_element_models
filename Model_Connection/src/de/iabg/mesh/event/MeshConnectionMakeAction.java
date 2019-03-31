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
 * This {@code Action} checks if the {@code JMeshConnectionPanel} has stored
 * nodes, if the nodes and node key groups have the same names, and if a node
 * key group is selected.  If all of these are {@code true}, it retrieves
 * the node connection distance tolerance and connects the nodes.  If the check
 * is {@code false}, it displays an error to the user.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public class MeshConnectionMakeAction extends AbstractAction {
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
    public MeshConnectionMakeAction(JMeshConnectionPanel connectionPanel,
            DefaultMeshConnectionUI connectionUI) {
        super("Connect");
        
        connectionPanel_    = connectionPanel;
        connectionUI_       = connectionUI;
        
        this.putValue(SHORT_DESCRIPTION, "Correlate nodes");
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener performs an action.
     * 
     * @param   actionEvent the {@link ActionEvent} from the action source
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        StringBuilder   result          = new StringBuilder();
        boolean         isConnectable   = true;
        double          tolerance;
        
        if (!connectionPanel_.hasNodes()) {
            isConnectable = false;
            result.append(String.format("ERROR: Open node files"));
        }
        else if (!connectionPanel_.isConsistent()) {
            isConnectable = false;
            result.append(String.format("ERROR: Inconsistent node files%n" +
                    "Check the IDs of the nodes and sets"));
        }
        else if (!connectionPanel_.hasNodeKeys()) {
            isConnectable = false;
            result.append(String.format("ERROR: Select node groups"));
        }
        
        if (isConnectable) {
            tolerance = connectionUI_.getConnectionTolerance();
            connectionPanel_.connectNodes(tolerance);
        }
        else {
            connectionPanel_.fireLogChanged(result.toString());
            connectionUI_.showMessageDialog(result.toString(), "Error",
                    DefaultMeshConnectionUI.ERROR_MESSAGE);
        }
    } // eom
} // eoc