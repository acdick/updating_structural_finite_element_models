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

import de.iabg.mode.JModePanel;

import de.iabg.mode.plaf.DefaultModeUI;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/*******************************************************************************
 * This {@code Action} deselects all currently stored modes in the
 * {@code JModePanel}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class ModeKeyDeselectAllAction extends AbstractAction {
    /** The {@code JModePanel} that this listener was designed for */
    protected JModePanel modePanel_;
    
    /** The {@code ModeUI} that this listener was designed for */
    protected DefaultModeUI modeUI_;
    
    
    
    /***************************************************************************
     * Constructs an {@code Action} from the given {@code JModePanel} and
     * {@code ModeUI}.
     * 
     * @param   modePanel   the {@code JModePanel} that this listener is
     *                      designed for
     * @param   modeUI      the {@code ModeUI} that this listener is designed
     *                      for
     **************************************************************************/
    public ModeKeyDeselectAllAction(JModePanel modePanel,
            DefaultModeUI modeUI) {
        super("Deselect All");
        
        modePanel_  = modePanel;
        modeUI_     = modeUI;
        
        this.putValue(SHORT_DESCRIPTION, "Deselect all modes");
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener performs an action.
     * 
     * @param   actionEvent the {@link ActionEvent} from the action source
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        modePanel_.deselectAllModeKeys();
    } // eom
} // eoc