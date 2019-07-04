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
package de.iabg.mode.event;

import de.iabg.mode.JModeCorrelationPanel;
import de.iabg.mode.ModeCorrelationUI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/*******************************************************************************
 * This {@code Action} checks if the {@code JModeCorrelationPanel} has stored
 * node connections, if it has stored modes, if the modes and nodes have the
 * same names, and if any modes are selected.  If all of these are {@code true},
 * it correlates the modes and sets the default mode connection.  If the check
 * is {@code false}, it displays an error to the user.  This class is tailored
 * for the Modal Assurance Criterion since it does not perform any mass matrix
 * checks.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class ModalAssuranceCriterionMakeAction extends AbstractAction {
    /** The {@code JModeCorrelationPanel} that this listener was designed for */
    protected JModeCorrelationPanel correlationPanel_;
    
    /** The {@code ModeCorrelationUI} that this listener was designed for */
    protected ModeCorrelationUI correlationUI_;
    
    
    
    /***************************************************************************
     * Constructs an {@code Action} from the given {@code JModeCorrelationPanel}
     * and {@code ModeCorrelationUI}.
     * 
     * @param   correlationPanel    the {@code JModeCorrelationPanel} that this
     *                              listener is designed for
     * @param   correlationUI       the {@code ModeCorrelationUI} that this
     *                              listener is designed for
     **************************************************************************/
    public ModalAssuranceCriterionMakeAction(
            JModeCorrelationPanel correlationPanel,
            ModeCorrelationUI correlationUI) {
        super("Correlate");
        
        correlationPanel_   = correlationPanel;
        correlationUI_      = correlationUI;
        
        this.putValue(SHORT_DESCRIPTION, "Correlate modes");
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener performs an action.
     * 
     * @param   actionEvent the {@link ActionEvent} from the action source
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        StringBuilder   result          = new StringBuilder();
        boolean         isCorrelatable  = true;
        
        if (!correlationPanel_.isNodeConnected()) {
            isCorrelatable = false;
            result.append(String.format("ERROR: Connect nodes"));
        }
        else if (!correlationPanel_.hasModes()) {
            isCorrelatable = false;
            result.append(String.format("ERROR: Open mode files"));
        }
        else if (!correlationPanel_.isConsistent()) {
            isCorrelatable = false;
            result.append(String.format("ERROR: Inconsistent mesh%n" +
                    "Check the IDs of the modes, nodes, and sets"));
        }
        else if (!correlationPanel_.hasModeKeys()) {
            isCorrelatable = false;
            result.append(String.format("ERROR: Select modes"));
        }
        
        if (isCorrelatable) {
            correlationPanel_.correlateModes();
            correlationPanel_.setDefaultKeys();
        }
        else {
            correlationPanel_.fireLogChanged(result.toString());
            correlationUI_.showMessageDialog(result.toString(), "Error",
                    ModeCorrelationUI.ERROR_MESSAGE);
        }
    } // eom
} // eoc