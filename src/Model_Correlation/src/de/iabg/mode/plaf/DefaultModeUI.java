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
package de.iabg.mode.plaf;

import de.iabg.mode.ModeUI;
import de.iabg.mode.JModePanel;

import de.iabg.mode.event.ModeKeyDeselectAllAction;
import de.iabg.mode.event.ModeKeySelectAllAction;
import de.iabg.mode.event.ModeOpenAction;
import de.iabg.mode.event.ModeReadAction;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.ScrollPaneConstants;

import javax.swing.table.TableModel;

/*******************************************************************************
 * This implementation of {@link ModeUI} creates a pluggable Look and Feel user
 * interface according to the {@code ModeUI} class description.  Refer to the
 * class API for more details.
 * 
 * Much of the implementation of this class should be straight-forward because
 * its design is modeled after Swing.  Swing follows a Model-View-Controller
 * (MVC) design pattern and uses a UI delegate, usually with inner classes, to
 * couple the view and controller.  This {@code ModeUI} separates the view and
 * controller and adheres to a slightly stricter MVC model by making controllers
 * their own outer classes.  This causes the view to declare its functionality
 * in methods, however, it allows for some reuse of listener classes and helps
 * maintain the scalability of the view.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 26, 2008
 ******************************************************************************/
public class DefaultModeUI
        implements ModeUI {
    /** A filler label for the bottom of the layout */
    protected JLabel bottomFillerLabel_;
    
    /** A constant for an error message */
    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    
    /** The file chooser of this interface */
    protected JFileChooser modeFileChooser_;
    
    /** A label for the mode input file */
    protected JLabel modeInputLabel_;
    
    /** A text field for the mode input file */
    protected JTextField modeInputTextField_;
    
    /** A listener to deselect all modes */
    protected Action modeKeyDeselectAllAction_;
    
    /** A button to deselect all modes */
    protected JButton modeKeyDeselectAllButton_;
    
    /** A label for the mode keys */
    protected JLabel modeKeyLabel_;
    
    /** A label for the mode key scroll pane */
    protected JScrollPane modeKeyScrollPane_;
    
    /** A listener to select all modes */
    protected Action modeKeySelectAllAction_;
    
    /** A button to select all modes */
    protected JButton modeKeySelectAllButton_;
    
    /** A label for the header of this interface */
    protected JLabel modeLabel_;
    
    /** A listener to open the mode */
    protected Action modeOpenAction_;
    
    /** A button to open the mode */
    protected JButton modeOpenButton_;
    
    /** The {@link JModePanel} that this interface is designed for */
    protected JModePanel modePanel_;
    
    /** A listener to open the connection */
    protected Action modeReadAction_;
    
    /** A separator for the header of this interface */
    protected JSeparator modeSeparator_;
    
    /** A constant for a warning message */
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    
    
    
    /***************************************************************************
     * Constructs a {@code DefaultModeUI}.
     **************************************************************************/
    public DefaultModeUI() {
        
    } // eom
    
    
    
    /***************************************************************************
     * Returns the mode input file name.
     * 
     * @return  the mode input file name
     **************************************************************************/
    public String getModeInput() {
        return modeInputTextField_.getText().trim();
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
        gridBagConstraints.fill     = GridBagConstraints.BOTH;
        gridBagConstraints.anchor   = GridBagConstraints.CENTER;
        
        modeFileChooser_ = new JFileChooser();
        modeFileChooser_.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        modeLabel_                      = new JLabel("Mode Shapes");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 0;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 12, 0, 0);
        modePanel_.add(modeLabel_, gridBagConstraints);
        
        modeSeparator_                  = new JSeparator();
        gridBagConstraints.fill         = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 0;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 4;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 5, 0, 12);
        modePanel_.add(modeSeparator_, gridBagConstraints);
        
        modeInputLabel_                 = new JLabel("PCH / UNV File:");
        modeInputLabel_.setLabelFor(modeInputTextField_);
        gridBagConstraints.fill         = GridBagConstraints.BOTH;
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        modePanel_.add(modeInputLabel_, gridBagConstraints);
        
        modeInputTextField_             = new JTextField(40);
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        modePanel_.add(modeInputTextField_, gridBagConstraints);
        
        modeOpenButton_                 = new JButton("Open...");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        modePanel_.add(modeOpenButton_, gridBagConstraints);
        
        modeKeyLabel_                   = new JLabel("Mode Selection:");
        modeKeyLabel_.setLabelFor(modeKeyScrollPane_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        modePanel_.add(modeKeyLabel_, gridBagConstraints);
        
        TableModel modeKeyModel = modePanel_.getModeTableModel();
        
        JTable modeKeyTable = new JTable();
        modeKeyTable.setModel(modeKeyModel);
        modeKeyTable.setPreferredScrollableViewportSize(new Dimension(32, 128));
        
        modeKeyScrollPane_              = new JScrollPane(modeKeyTable);
        modeKeyScrollPane_.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 3;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 12, 0);
        modePanel_.add(modeKeyScrollPane_, gridBagConstraints);
        
        modeKeySelectAllButton_         = new JButton("Select All");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        modePanel_.add(modeKeySelectAllButton_, gridBagConstraints);
        
        modeKeyDeselectAllButton_       = new JButton("Deselect All");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 3;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        modePanel_.add(modeKeyDeselectAllButton_, gridBagConstraints);
        
        bottomFillerLabel_              = new JLabel();
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 1;
        gridBagConstraints.insets.set(0, 0, 0, 0);
        modePanel_.add(bottomFillerLabel_, gridBagConstraints);
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
        modePanel_.setLayout(new GridBagLayout());
        modePanel_.setAlignmentX(JModePanel.LEFT_ALIGNMENT);
    } // eom
    
    
    
    /***************************************************************************
     * Installs all listeners for all components in this container.
     **************************************************************************/
    protected void installListeners() {
        modeReadAction_ = new ModeReadAction(modePanel_, this);
        modeInputTextField_.setAction(modeReadAction_);
        
        modeOpenAction_ = new ModeOpenAction(modePanel_, this);
        modeOpenButton_.setAction(modeOpenAction_);
        
        modeKeySelectAllAction_ = new ModeKeySelectAllAction(modePanel_, this);
        modeKeySelectAllButton_.setAction(modeKeySelectAllAction_);
        
        modeKeyDeselectAllAction_ = new ModeKeyDeselectAllAction(modePanel_,
                this);
        modeKeyDeselectAllButton_.setAction(modeKeyDeselectAllAction_);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void installUI(JModePanel modePanel) {
        modePanel_ = modePanel;
        
        this.installDefaults();
        this.installLayout();
        this.installComponents();
        this.installListeners();
        this.installKeyboardActions();
        this.initialize();
    } // eom
    
    
    
    /***************************************************************************
     * Replaces the mode input file with the given one.
     * 
     * @param   fileName    the new mode input file name
     **************************************************************************/
    public void setModeInput(String fileName) {
        modeInputTextField_.setText(fileName.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Shows a file chooser dialog for the supported mode input file types and
     * returns the selected file.
     * 
     * @return  the selected mode input file
     **************************************************************************/
    public String showModeOpenDialog() {
        String      fileName    = null;
        ModeFilter  pchFilter   = new ModeFilter(ModeFilter.PCH);
        ModeFilter  unvFilter   = new ModeFilter(ModeFilter.UNV);
        int         result;
        
        modeFileChooser_.addChoosableFileFilter(pchFilter);
        modeFileChooser_.addChoosableFileFilter(unvFilter);
        modeFileChooser_.setFileFilter(pchFilter);
        
        result = modeFileChooser_.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileName = modeFileChooser_.getSelectedFile().getAbsolutePath();
        }
        
        modeFileChooser_.removeChoosableFileFilter(pchFilter);
        modeFileChooser_.removeChoosableFileFilter(unvFilter);
        
        return fileName;
    } // eom
    
    
    
    /***************************************************************************
     * Shows a message dialog for error or warning messages.
     * 
     * @param   message     the message of the error or warning
     * @param   title       the title of the error or warning
     * @param   messageType an {@code ERROR_MESSAGE} or {@code WARNING_MESSAGE}
     **************************************************************************/
    public void showMessageDialog(Object message, String title,
            int messageType) {
        switch (messageType) {
            case ERROR_MESSAGE:
            case WARNING_MESSAGE:
                JOptionPane.showMessageDialog(null, message, title,
                        messageType);
                break;
            default:
                throw new IllegalArgumentException();
        }
    } // eom
    
    
    
    /***************************************************************************
     * Uninstalls all components on the container.
     **************************************************************************/
    protected void uninstallComponents() {
        modeFileChooser_ = null;
        
        modePanel_.remove(modeLabel_);
        modeLabel_ = null;
        
        modePanel_.remove(modeSeparator_);
        modeSeparator_ = null;
        
        modePanel_.remove(modeInputLabel_);
        modeInputLabel_ = null;
        
        modePanel_.remove(modeInputTextField_);
        modeInputTextField_ = null;
        
        modePanel_.remove(modeOpenButton_);
        modeOpenButton_ = null;
        
        modePanel_.remove(modeKeyLabel_);
        modeKeyLabel_ = null;
        
        modePanel_.remove(modeKeyScrollPane_);
        modeKeyScrollPane_ = null;
        
        modePanel_.remove(modeKeySelectAllButton_);
        modeKeySelectAllButton_ = null;
        
        modePanel_.remove(modeKeyDeselectAllButton_);
        modeKeyDeselectAllButton_ = null;
        
        modePanel_.remove(bottomFillerLabel_);
        bottomFillerLabel_ = null;
    } // eom
    
    
    
    /***************************************************************************
     * Uninstalls all global default settings of the container.
     **************************************************************************/
    protected void uninstallDefaults() {
        JPopupMenu.setDefaultLightWeightPopupEnabled(true);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(true);
    } // eom
    
    
    
    /***************************************************************************
     * Uninstalls all keyboard actions and mnemonics.
     **************************************************************************/
    protected void uninstallKeyboardActions() {
        
    } // eom
    
    
    
    /***************************************************************************
     * Uninstalls the layout manager of this container.
     **************************************************************************/
    protected void uninstallLayout() {
        modePanel_.setBorder(null);
        modePanel_.setLayout(null);
    } // eom
    
    
    
    /***************************************************************************
     * Uninstalls all listeners for all components in this container.
     **************************************************************************/
    protected void uninstallListeners() {
        modeInputTextField_.removeActionListener(modeReadAction_);
        modeReadAction_ = null;
        
        modeOpenButton_.removeActionListener(modeOpenAction_);
        modeOpenAction_ = null;
        
        modeKeySelectAllButton_.removeActionListener(modeKeySelectAllAction_);
        modeKeySelectAllAction_ = null;
        
        modeKeyDeselectAllButton_.removeActionListener(
                modeKeyDeselectAllAction_);
        modeKeyDeselectAllAction_ = null;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void uninstallUI(JModePanel modePanel) {
        modePanel_ = modePanel;
        
        this.uninstallKeyboardActions();
        this.uninstallListeners();
        this.uninstallComponents();
        this.uninstallLayout();
        this.uninstallDefaults();
    } // eom
} // eoc