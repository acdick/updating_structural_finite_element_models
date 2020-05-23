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

import java.io.IOException;

import javax.swing.AbstractAction;

/*******************************************************************************
 * This {@code Action} checks if the {@code JMeshConnectionPanel} has stored
 * node connections.  If this is {@code true}, it opens a dialog for the user to
 * select the node connection output file and then exports it.  If the check is
 * {@code false}, it displays an error to the user.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class MeshConnectionSaveAsAction extends AbstractAction {
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
    public MeshConnectionSaveAsAction(JMeshConnectionPanel connectionPanel,
            DefaultMeshConnectionUI connectionUI) {
        super("Save...");
        
        connectionPanel_    = connectionPanel;
        connectionUI_       = connectionUI;
        
        this.putValue(SHORT_DESCRIPTION, "Save mesh connection file");
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener performs an action.
     * 
     * @param   actionEvent the {@link ActionEvent} from the action source
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        String fileName;
        
        try {
            if (connectionPanel_.isConnected()) {
                fileName = connectionUI_.showConnectionSaveDialog();
                if (fileName != null) {
                    connectionUI_.setConnectionOutput(fileName);
                    connectionPanel_.exportNodeConnections(fileName);
                }
            }
            else {
                connectionPanel_.fireLogChanged(
                        "ERROR: Connection does not exist");
                connectionUI_.showMessageDialog(
                        "ERROR: Connection does not exist", "Error",
                        DefaultMeshConnectionUI.ERROR_MESSAGE);
            }
        }
        catch (IOException exception) {
            connectionPanel_.fireLogChanged("ERROR: " + exception.getMessage());
            connectionUI_.showMessageDialog(exception.getMessage(), "Error",
                    DefaultMeshConnectionUI.ERROR_MESSAGE);
        }
    } // eom
} // eoc