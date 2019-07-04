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

import java.io.IOException;

import javax.swing.AbstractAction;

/*******************************************************************************
 * This {@code Action} checks if the {@code JModeCorrelationPanel} has stored
 * mode correlations.  If this is {@code true}, it automatically retrieves the
 * mode correlation and exports it to the current mode correlation output file.
 * If the check is {@code false}, it displays an error to the user.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class ModeCorrelationSaveAction extends AbstractAction {
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
    public ModeCorrelationSaveAction(JModeCorrelationPanel correlationPanel,
            ModeCorrelationUI correlationUI) {
        super("Save...");
        
        correlationPanel_   = correlationPanel;
        correlationUI_      = correlationUI;
        
        this.putValue(SHORT_DESCRIPTION, "Save correlation");
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener performs an action.
     * 
     * @param   actionEvent the {@link ActionEvent} from the action source
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        String fileName;
        
        try {
            if (correlationPanel_.isModeCorrelated()) {
                fileName = correlationUI_.getCorrelationOutput();
                if (fileName != null) {
                    correlationPanel_.exportModeCorrelation(fileName);
                }
            }
            else {
                correlationPanel_.fireLogChanged(
                        "ERROR: Correlation does not exist");
                correlationUI_.showMessageDialog(
                        "ERROR: Correlation does not exist", "Error",
                        ModeCorrelationUI.ERROR_MESSAGE);
            }
        }
        catch (IOException exception) {
            correlationPanel_.fireLogChanged(
                    "ERROR: " + exception.getMessage());
            correlationUI_.showMessageDialog(exception.getMessage(), "Error",
                    ModeCorrelationUI.ERROR_MESSAGE);
        }
    } // eom
} // eoc