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
 * This {@code Action} automatically imports the current node connection
 * input file.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class MeshConnectionReadAction extends AbstractAction {
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
    public MeshConnectionReadAction(JMeshConnectionPanel connectionPanel,
            DefaultMeshConnectionUI connectionUI) {
        super("Open...");
        
        connectionPanel_    = connectionPanel;
        connectionUI_       = connectionUI;
        
        this.putValue(SHORT_DESCRIPTION, "Open mesh connection file");
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener performs an action.
     * 
     * @param   actionEvent the {@link ActionEvent} from the action source
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        String fileName;
        
        try {
            fileName = connectionUI_.getConnectionInput();
            if (fileName != null) {
                connectionPanel_.importNodeConnections(fileName);
            }
        }
        catch (IOException exception) {
            connectionPanel_.fireLogChanged("ERROR: " + exception.getMessage());
            connectionUI_.showMessageDialog(exception.getMessage(), "Error",
                    DefaultMeshConnectionUI.ERROR_MESSAGE);
        }
    } // eom
} // eoc