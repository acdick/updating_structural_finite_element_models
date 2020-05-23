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

import java.io.IOException;

import javax.swing.AbstractAction;

/*******************************************************************************
 * This {@code Action} automatically imports the current node key group input
 * file.  It then checks if the nodes and node key groups that were just
 * imported have the same names.  If the check is {@code false}, it displays a
 * warning to the user.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class NodeKeyReadAction extends AbstractAction {
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
    public NodeKeyReadAction(JMeshPanel meshPanel, DefaultMeshUI meshUI) {
        super("Open...");
        
        meshPanel_  = meshPanel;
        meshUI_     = meshUI;
        
        this.putValue(SHORT_DESCRIPTION, "Open node key file");
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener performs an action.
     * 
     * @param   actionEvent the {@link ActionEvent} from the action source
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        String fileName;
        
        try {
            fileName = meshUI_.getNodeKeyInput();
            if (fileName != null) {
                meshPanel_.importNodeKeyLists(fileName);
                meshPanel_.setDefaultKeys();
                
                if (!meshPanel_.isConsistent()) {
                    meshPanel_.fireLogChanged(String.format(
                            "WARNING: Inconsistent mesh%n" +
                            "Check the IDs of the nodes and sets"));
                }
            }
        }
        catch (IOException exception) {
            meshPanel_.fireLogChanged("ERROR: " + exception.getMessage());
            meshUI_.showMessageDialog(exception.getMessage(), "Error",
                    DefaultMeshUI.ERROR_MESSAGE);
        }
    } // eom
} // eoc