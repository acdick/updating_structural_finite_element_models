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

import de.iabg.mode.plaf.OrthogonalityCheckUI;

import java.awt.event.ActionEvent;

import java.io.IOException;

import javax.swing.AbstractAction;

/*******************************************************************************
 * This {@code Action} automatically imports the current mass matrix input file.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class MassReadAction extends AbstractAction {
    /** The {@code JModeCorrelationPanel} that this listener was designed for */
    protected JModeCorrelationPanel correlationPanel_;
    
    /** The {@code ModeCorrelationUI} that this listener was designed for */
    protected OrthogonalityCheckUI correlationUI_;
    
    
    
    /***************************************************************************
     * Constructs an {@code Action} from the given {@code JModeCorrelationPanel}
     * and {@code ModeCorrelationUI}.
     * 
     * @param   correlationPanel    the {@code JModeCorrelationPanel} that this
     *                              listener is designed for
     * @param   correlationUI       the {@code ModeUI} that this listener is
     *                              designed for
     **************************************************************************/
    public MassReadAction(JModeCorrelationPanel correlationPanel,
            OrthogonalityCheckUI correlationUI) {
        super("Open...");
        
        correlationPanel_   = correlationPanel;
        correlationUI_      = correlationUI;
        
        this.putValue(SHORT_DESCRIPTION, "Open mass file");
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener performs an action.
     * 
     * @param   actionEvent the {@link ActionEvent} from the action source
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        String fileName;
        
        try {
            fileName = correlationUI_.getMassInput();
            if (fileName != null) {
                correlationPanel_.importMassMatrices(fileName);
            }
        }
        catch (IOException exception) {
            correlationPanel_.fireLogChanged(
                    "ERROR: " + exception.getMessage());
            correlationUI_.showMessageDialog(exception.getMessage(), "Error",
                    OrthogonalityCheckUI.ERROR_MESSAGE);
        }
    } // eom
} // eoc