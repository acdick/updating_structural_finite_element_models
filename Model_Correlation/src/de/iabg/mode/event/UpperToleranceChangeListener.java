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

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/*******************************************************************************
 * This {@link ChangeListener} updates the upper color tolerance of the
 * {@link JModeCorrelationPanel} whenever the target changes.  If the upper
 * color tolerance is less than the lower color tolerance, the upper color
 * tolerance is set to the same value as the lower color tolerance.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class UpperToleranceChangeListener
        implements ChangeListener {
    /** The {@code JModeCorrelationPanel} that this listener was designed for */
    protected JModeCorrelationPanel correlationPanel_;
    
    /** The {@code ModeCorrelationUI} that this listener was designed for */
    protected ModeCorrelationUI correlationUI_;
    
    
    
    /***************************************************************************
     * Constructs a {@link ChangeListener} from the given
     * {@code JModeCorrelationPanel} and {@code ModeCorrelationUI}.
     * 
     * @param   correlationPanel    the {@code JModeCorrelationPanel} that this
     *                              listener is designed for
     * @param   correlationUI       the {@code ModeCorrelationUI} that this
     *                              listener is designed for
     **************************************************************************/
    public UpperToleranceChangeListener(JModeCorrelationPanel correlationPanel,
            ModeCorrelationUI correlationUI) {
        correlationPanel_   = correlationPanel;
        correlationUI_      = correlationUI;
    } // eom
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener has changed its state.
     * 
     * @param   changeEvent the {@link ChangeEvent} from the change source
     **************************************************************************/
    public void stateChanged(ChangeEvent changeEvent) {
        double lowerTolerance;
        double upperTolerance;
        
        lowerTolerance = correlationUI_.getLowerTolerance();
        upperTolerance = correlationUI_.getUpperTolerance();
        
        if (upperTolerance < lowerTolerance) {
            correlationUI_.setUpperTolerance(lowerTolerance);
        }
        else {
            correlationPanel_.setUpperTolerance(upperTolerance);
        }
    } // eom
} // eoc