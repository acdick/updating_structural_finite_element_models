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
package de.iabg.swing;

import de.iabg.swing.event.LoggerLogListener;
import de.iabg.swing.event.LogListener;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

/*******************************************************************************
 * This {@link Logger} creates a Swing component that displays a running command
 * history of all logged actions.  The display area for the log appends the new
 * log message after every {@code LogEvent} is fired.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class HistoryPanel extends JPanel
        implements Logger {
    /** A list of all referenced {@code Loggable} objects */
    protected ArrayList<Loggable> loggables_;
    
    /** The listener that handles {@code LogEvents} */
    protected LogListener logListener_;
    
    /** The display area for the log */
    protected JTextArea logTextArea_;
    
    
    
    /***************************************************************************
     * Constructs a {@code HistoryPanel} and installs the user interface.
     **************************************************************************/
    public HistoryPanel() {
        super();
        
        loggables_ = new ArrayList<Loggable>();
        
        this.installUI();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void addLoggable(Loggable loggable) {
        loggable.addLogListener(logListener_);
        loggables_.add(loggable);
    } // eom
    
    
    
    /***************************************************************************
     * Initializes all fields or default settings after all components have been
     * added.
     **************************************************************************/
    protected void initialize() {
        this.setVisible(true);
    } // eom
    
    
    
    /***************************************************************************
     * Installs and lays out all components on the container.
     **************************************************************************/
    protected void installComponents() {
        GridBagConstraints  gridBagConstraints;
        JLabel              label;
        JScrollPane         scrollPane;
        JSeparator          separator;
        
        gridBagConstraints          = new GridBagConstraints();
        gridBagConstraints.fill     = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor   = GridBagConstraints.CENTER;
        
        label                           = new JLabel("History");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 0;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(0, 12, 0, 0);
        this.add(label, gridBagConstraints);
        
        separator                       = new JSeparator();
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 0;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(0, 5, 0, 12);
        this.add(separator, gridBagConstraints);
        
        logTextArea_ = new JTextArea(5, 20);
        logTextArea_.setEditable(false);
        logTextArea_.setFont(new Font("Monospaced", Font.PLAIN, 11));
        logTextArea_.setLineWrap(true);
        logTextArea_.setWrapStyleWord(true);
        
        scrollPane                      = new JScrollPane(logTextArea_);
        scrollPane.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 1;
        gridBagConstraints.insets.set(5, 12, 12, 12);
        this.add(scrollPane, gridBagConstraints);
    } // eom
    
    
    
    /***************************************************************************
     * Installs all global default settings of the container.
     **************************************************************************/
    protected void installDefaults() {
        
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
        logListener_ = new LoggerLogListener(this);
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
    public void removeLoggable(Loggable loggable) {
        loggable.removeLogListener(logListener_);
        loggables_.remove(loggable);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void updateLog(String log) {
        logTextArea_.append(String.format("%s%n", log));
    } // eom
} // eoc