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
package de.iabg.mode;

import de.iabg.swing.Loggable;

import de.iabg.swing.event.LoggableLogListener;
import de.iabg.swing.event.LogEvent;
import de.iabg.swing.event.LogListener;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;

import javax.swing.event.EventListenerList;

/*******************************************************************************
 * An implementation for a {@code JPanel} containing two {@link JModePanel}
 * objects with the {@code First Mode} panel placed vertically above the
 * {@code Last Model} panel.  This is simply a convenience class to instantiate
 * the two {@code JModePanel} components and provides no other additional
 * functionality.
 * 
 * This component is also {@link Loggable} and simply relays or chains any
 * {@code LogEvents} from the {@code First Mode} panel or the {@code Last Mode}
 * panel.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 12, 2008
 ******************************************************************************/
public class JModeMirrorPanel extends JPanel
        implements Loggable {
    /** The {@code First Mode} panel */
    protected JModePanel firstModePanel_;
    
    /** The {@code Last Mode} panel */
    protected JModePanel lastModePanel_;
    
    /** A list for event listeners */
    protected EventListenerList listenerList_;
    
    /** The listener that handles {@code LogEvents} */
    protected LogListener logListener_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code JModeMirrorPanel} with the given name,
     * {@code First Mode} panel, and {@code Last Mode} panel.
     * 
     * @param   name            the name of this component
     * @param   firstModePanel  the {@code First Mode} panel
     * @param   lastModePanel   the {@code Last Mode} panel
     **************************************************************************/
    public JModeMirrorPanel(String name, JModePanel firstModePanel,
            JModePanel lastModePanel) {
        super();
        
        firstModePanel_ = firstModePanel;
        lastModePanel_  = lastModePanel;
        listenerList_   = new EventListenerList();
        
        this.setName(name.trim());
        this.installUI();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void addLogListener(LogListener listener) {
        listenerList_.add(LogListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void fireLogChanged(String log) {
        LogListener[] listeners;
        listeners = listenerList_.getListeners(LogListener.class);
        
        for (LogListener listener : listeners) {
            listener.logChanged(new LogEvent(this, log));
        }
    } // eom
    
    
    
    /***************************************************************************
     * Returns a reference to the {@code First Mode} panel.
     * 
     * @return  the {@code First Mode} panel
     **************************************************************************/
    public JModePanel getFirstMode() {
        return firstModePanel_;
    } // eom
    
    
    
    /***************************************************************************
     * Returns a reference to the {@code Last Mode} panel.
     * 
     * @return  the {@code Last Mode} panel
     **************************************************************************/
    public JModePanel getLastMode() {
        return lastModePanel_;
    } // eom
    
    
    
    /***************************************************************************
     * Initializes all fields or default settings after all components have been
     * added.
     **************************************************************************/
    protected void initialize() {
        
    } // eom
    
    
    
    /***************************************************************************
     * Installs and lays out all components on the container.
     **************************************************************************/
    protected void installComponents() {
        GridBagConstraints gridBagConstraints;
        gridBagConstraints          = new GridBagConstraints();
        gridBagConstraints.fill     = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor   = GridBagConstraints.FIRST_LINE_START;
        
        gridBagConstraints.gridx            = 0;
        gridBagConstraints.gridy            = 0;
        gridBagConstraints.gridheight       = 1;
        gridBagConstraints.gridwidth        = 1;
        gridBagConstraints.weightx          = 1;
        gridBagConstraints.weighty          = 0;
        gridBagConstraints.insets.set(0, 0, 0, 0);
        this.add(firstModePanel_, gridBagConstraints);
        
        gridBagConstraints.gridx            = 0;
        gridBagConstraints.gridy            = 1;
        gridBagConstraints.gridheight       = 1;
        gridBagConstraints.gridwidth        = 1;
        gridBagConstraints.weightx          = 1;
            gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(0, 0, 0, 0);
        this.add(lastModePanel_, gridBagConstraints);
        
        JLabel bottomFillerLabel        = new JLabel();
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 1;
        gridBagConstraints.insets.set(0, 0, 0, 0);
        this.add(bottomFillerLabel, gridBagConstraints);
    } // eom
    
    
    
    /***************************************************************************
     * Installs all global default settings of the container.
     **************************************************************************/
    protected void installDefaults() {
        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
    } // eom
    
    
    
    /***************************************************************************
     * Installs all keyboard actions and mnemonics.
     **************************************************************************/
    protected void installKeyboardActions() {
        
    } // eom
    
    
    
    /***************************************************************************
     * Installs the layout manager of this container.
     **************************************************************************/
    protected void installLayout() {
        this.setLayout(new GridBagLayout());
        this.setAlignmentX(LEFT_ALIGNMENT);
    } // eom
    
    
    
    /***************************************************************************
     * Installs all listeners for all components in this container.
     **************************************************************************/
    protected void installListeners() {
        logListener_ = new LoggableLogListener(this);
        firstModePanel_.addLogListener(logListener_);
        lastModePanel_.addLogListener(logListener_);
    } // eom
    
    
    
    /***************************************************************************
     * Installs layout, components, listeners, keyboard actions, defaults, and
     * initializes any values or fields.
     **************************************************************************/
    protected void installUI() {
        this.installDefaults();
        this.installLayout();
        this.installComponents();
        this.installListeners();
        this.installKeyboardActions();
        this.initialize();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void removeLogListener(LogListener listener) {
        listenerList_.remove(LogListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     * Returns a formatted string containing the number of currently stored
     * modes and nodes in both the {@code First Mode} panel and
     * {@code Last Mode} panel.
     * 
     * @return  the string representation of this {@code JModeMirrorPanel}
     **************************************************************************/
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        
        result.append(String.format("%s%n", firstModePanel_.toString()));
        result.append(String.format("%s%n", lastModePanel_.toString()));
        
        return result.toString();
    } // eom
} // eoc