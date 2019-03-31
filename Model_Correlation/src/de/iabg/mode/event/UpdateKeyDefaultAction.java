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

import de.iabg.mode.ModeCorrelationUI;
import de.iabg.mode.JModeCorrelationPanel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/*******************************************************************************
 * This {@code Action} sets the default mode selection and connection of the
 * {@code JModeCorrelationPanel}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class UpdateKeyDefaultAction extends AbstractAction {
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
    public UpdateKeyDefaultAction(JModeCorrelationPanel correlationPanel,
            ModeCorrelationUI correlationUI) {
        super("Default");
        
        correlationPanel_   = correlationPanel;
        correlationUI_      = correlationUI;
        
        this.putValue(SHORT_DESCRIPTION, "Set default mode keys");
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener performs an action.
     * 
     * @param   actionEvent the {@link ActionEvent} from the action source
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        correlationPanel_.setDefaultKeys();
    } // eom
} // eoc