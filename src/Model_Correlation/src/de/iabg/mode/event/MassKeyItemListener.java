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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/*******************************************************************************
 * This {@link ItemListener} updates the log of the
 * {@link JModeCorrelationPanel} whenever a mass matrix is selected.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 26, 2008
 ******************************************************************************/
public class MassKeyItemListener
        implements ItemListener {
    /** The {@code JModeCorrelationPanel} that this listener was designed for */
    protected JModeCorrelationPanel correlationPanel_;
    
    /** The {@code ModeCorrelationUI} that this listener was designed for */
    protected ModeCorrelationUI correlationUI_;
    
    
    
    /***************************************************************************
     * Constructs an {@link ItemListener} from the given
     * {@code JModeCorrelationPanel} and {@code ModeCorrelationUI}.
     * 
     * @param   correlationPanel    the {@code JModeCorrelationPanel} that this
     *                              listener is designed for
     * @param   correlationUI       the {@code ModeCorrelationUI} that this
     *                              listener is designed for
     **************************************************************************/
    public MassKeyItemListener(JModeCorrelationPanel correlationPanel,
            ModeCorrelationUI correlationUI) {
        correlationPanel_   = correlationPanel;
        correlationUI_      = correlationUI;
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener has changed its item.
     * 
     * @param   itemEvent  the {@link ItemEvent} from the item source
     **************************************************************************/
    public void itemStateChanged(ItemEvent itemEvent) {
        correlationPanel_.fireLogChanged("Updated mass matrix selection:");
    } // eom
} // eoc