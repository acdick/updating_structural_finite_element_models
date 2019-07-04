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

import de.iabg.mode.JModeCorrelationPanel;
import de.iabg.mode.ModeCorrelationUI;

import de.iabg.mode.event.LowerToleranceChangeListener;
import de.iabg.mode.event.MassKeyItemListener;
import de.iabg.mode.event.MassOpenAction;
import de.iabg.mode.event.MassReadAction;
import de.iabg.mode.event.ModeConnectionSaveAction;
import de.iabg.mode.event.ModeConnectionSaveAsAction;
import de.iabg.mode.event.ModeCorrelationSaveAction;
import de.iabg.mode.event.ModeCorrelationSaveAsAction;
import de.iabg.mode.event.ModeCorrelationShow2DAction;
import de.iabg.mode.event.ModeCorrelationShow3DAction;
import de.iabg.mode.event.OrthogonalityCheckMakeAction;
import de.iabg.mode.event.UpdateKeyDefaultAction;
import de.iabg.mode.event.UpdateKeyDeselectAllAction;
import de.iabg.mode.event.UpdateKeySelectAllAction;
import de.iabg.mode.event.UpperToleranceChangeListener;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ItemListener;

import java.io.File;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.ToolTipManager;

import javax.swing.event.ChangeListener;

import javax.swing.table.TableModel;

/*******************************************************************************
 * This implementation of {@link ModeCorrelationUI} creates a pluggable Look and
 * Feel user interface according to the {@code ModeCorrelationUI} class
 * description.  The specifics of this implementation are tailored for the
 * Orthogonality Check, therefore importing mass matrices is included.
 * Refer to the class API for more details.
 * 
 * Much of the implementation of this class should be straight-forward because
 * its design is modeled after Swing.  Swing follows a Model-View-Controller
 * (MVC) design pattern and uses a UI delegate, usually with inner classes, to
 * couple the view and controller.  This {@code ModeCorrelationUI} separates the
 * view and controller and adheres to a slightly stricter MVC model by making
 * controllers their own outer classes.  This causes the view to declare its
 * functionality in methods, however, it allows for some reuse of listener
 * classes and helps maintain the scalability of the view.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 26, 2008
 ******************************************************************************/
public class OrthogonalityCheckUI
        implements ModeCorrelationUI {
    /** A filler label for the bottom of the layout */
    protected JLabel bottomFillerLabel_;
    
    /** The file chooser of this interface */
    protected JFileChooser correlationFileChooser_;
    
    /** A label for the header of this interface */
    protected JLabel correlationLabel_;
    
    /** A listener to correlate the modes */
    protected Action correlationMakeAction_;
    
    /** A button to correlate the modes */
    protected JButton correlationMakeButton_;
    
    /** A label for the correlation output file */
    protected JLabel correlationOutputLabel_;
    
    /** A text field for the correlation output file */
    protected JTextField correlationOutputTextField_;
    
    /** The {@link JModeCorrelationPanel} that this interface is designed for */
    protected JModeCorrelationPanel correlationPanel_;
    
    /** A listener to save the correlation */
    protected Action correlationSaveAction_;
    
    /** A listener to save the correlation */
    protected Action correlationSaveAsAction_;
    
    /** A button to save the correlation */
    protected JButton correlationSaveButton_;
    
    /** A separator for the header of this interface */
    protected JSeparator correlationSeparator_;
    
    /** A listener to render the 2D correlation */
    protected Action correlationShow2DAction_;
    
    /** A button to render the 2D correlation */
    protected JButton correlationShow2DButton_;
    
    /** A listener to render the 3D correlation */
    protected Action correlationShow3DAction_;
    
    /** A button to render the 3D correlation */
    protected JButton correlationShow3DButton_;
    
    /** A label for the lower color tolerance */
    protected JLabel lowerToleranceLabel_;
    
    /** A listener for the lower color tolerance */
    protected ChangeListener lowerToleranceListener_;
    
    /** A spinner for the lower color tolerance */
    protected JSpinner lowerToleranceSpinner_;
    
    /** A label for the mass input file */
    protected JLabel massInputLabel_;
    
    /** A text field for the mass input file */
    protected JTextField massInputTextField_;
    
    /** A combo box for the mass matrices */
    protected JComboBox massKeyComboBox_;
    
    /** A listener to handle mass matrix changes */
    protected ItemListener massKeyItemListener_;
    
    /** A label for the selected mass matrix */
    protected JLabel massKeyLabel_;
    
    /** A listener to open the mass matrices */
    protected Action massOpenAction_;
    
    /** A button to open the mass matrices */
    protected JButton massOpenButton_;
    
    /** A listener to open the mass matrices */
    protected Action massReadAction_;
    
    /** A listener to set the default connections */
    protected Action updateKeyDefaultAction_;
    
    /** A button to set the default connections */
    protected JButton updateKeyDefaultButton_;
    
    /** A listener to deselect all connections */
    protected Action updateKeyDeselectAllAction_;
    
    /** A button to deselect all connections */
    protected JButton updateKeyDeselectAllButton_;
    
    /** A scroll pane for the connections */
    protected JScrollPane updateKeyScrollPane_;
    
    /** A listener to select all connections */
    protected Action updateKeySelectAllAction_;
    
    /** A button to select all connections */
    protected JButton updateKeySelectAllButton_;
    
    /** A label for the connections */
    protected JLabel updateLabel_;
    
    /** A label for the connection output file */
    protected JLabel updateOutputLabel_;
    
    /** A text field for the connection output file */
    protected JTextField updateOutputTextField_;
    
    /** A listener to save the connection */
    protected Action updateSaveAction_;
    
    /** A listener to save the connection */
    protected Action updateSaveAsAction_;
    
    /** A button to save the connection */
    protected JButton updateSaveButton_;
    
    /** A separator for the connection */
    protected JSeparator updateSeparator_;
    
    /** A label for the upper color tolerance */
    protected JLabel upperToleranceLabel_;
    
    /** A listener for the upper color tolerance */
    protected ChangeListener upperToleranceListener_;
    
    /** A spinner for the upper color tolerance */
    protected JSpinner upperToleranceSpinner_;
    
    
    
    /***************************************************************************
     * Constructs a {@code OrthogonalityCheckUI}.
     **************************************************************************/
    public OrthogonalityCheckUI() {
        
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String getConnectionOutput() {
        String fileName = updateOutputTextField_.getText().trim();
        
        if (!fileName.toUpperCase().endsWith(ModeFilter.NAS)) {
            fileName = new String(fileName + "." + ModeFilter.NAS);
            updateOutputTextField_.setText(fileName);
        }
        
        return fileName;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String getCorrelationOutput() {
        String fileName = correlationOutputTextField_.getText().trim();
        
        if (!fileName.toUpperCase().endsWith(ModeFilter.CSV)) {
            fileName = new String(fileName + "." + ModeFilter.CSV);
            correlationOutputTextField_.setText(fileName);
        }
        
        return fileName;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getLowerTolerance() {
        return (Double) lowerToleranceSpinner_.getValue();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String getMassInput() {
        return massInputTextField_.getText();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getUpperTolerance() {
        return (Double) upperToleranceSpinner_.getValue();
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
        
        correlationFileChooser_ = new JFileChooser();
        correlationFileChooser_.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        correlationLabel_               = new JLabel("Orthogonality Check");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 0;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 12, 0, 0);
        correlationPanel_.add(correlationLabel_, gridBagConstraints);
        
        correlationSeparator_           = new JSeparator();
        gridBagConstraints.fill         = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 0;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 3;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 5, 0, 12);
        correlationPanel_.add(correlationSeparator_, gridBagConstraints);
        
        massInputLabel_                 = new JLabel("DMIG Input File:");
        massInputLabel_.setLabelFor(massInputTextField_);
        gridBagConstraints.fill         = GridBagConstraints.BOTH;
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        correlationPanel_.add(massInputLabel_, gridBagConstraints);
        
        massInputTextField_             = new JTextField(40);
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        correlationPanel_.add(massInputTextField_, gridBagConstraints);
        
        massOpenButton_                 = new JButton("Open...");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        correlationPanel_.add(massOpenButton_, gridBagConstraints);
        
        massKeyLabel_                   = new JLabel("Mass Selection:");
        massKeyLabel_.setLabelFor(massKeyComboBox_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        correlationPanel_.add(massKeyLabel_, gridBagConstraints);
        
        massKeyComboBox_                = new JComboBox();
        massKeyComboBox_.setModel(correlationPanel_.getMassKeyModel());
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        correlationPanel_.add(massKeyComboBox_, gridBagConstraints);
        
        correlationMakeButton_          = new JButton("Correlate");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        correlationPanel_.add(correlationMakeButton_, gridBagConstraints);
        
        correlationOutputLabel_         = new JLabel("CSV Output File:");
        correlationOutputLabel_.setLabelFor(correlationOutputTextField_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 3;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        correlationPanel_.add(correlationOutputLabel_, gridBagConstraints);
        
        correlationOutputTextField_     = new JTextField(40);
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 3;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        correlationPanel_.add(correlationOutputTextField_, gridBagConstraints);
        
        correlationSaveButton_          = new JButton("Save...");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 3;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        correlationPanel_.add(correlationSaveButton_, gridBagConstraints);
        
        lowerToleranceLabel_            = new JLabel("Lower Tolerance:");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 4;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        correlationPanel_.add(lowerToleranceLabel_, gridBagConstraints);
        
        SpinnerModel lowerToleranceModel;
        lowerToleranceModel = new SpinnerNumberModel(0.05, 0.0, 1.0, 0.05);
        
        lowerToleranceSpinner_          = new JSpinner();
        lowerToleranceSpinner_.setModel(lowerToleranceModel);
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 4;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        correlationPanel_.add(lowerToleranceSpinner_, gridBagConstraints);
        
        correlationShow2DButton_        = new JButton("Display 2D");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 4;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        correlationPanel_.add(correlationShow2DButton_, gridBagConstraints);
        
        upperToleranceLabel_            = new JLabel("Upper Tolerance:");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        correlationPanel_.add(upperToleranceLabel_, gridBagConstraints);
        
        SpinnerModel upperToleranceModel;
        upperToleranceModel = new SpinnerNumberModel(0.95, 0.0, 1.0, 0.05);
        
        upperToleranceSpinner_          = new JSpinner();
        upperToleranceSpinner_.setModel(upperToleranceModel);
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        correlationPanel_.add(upperToleranceSpinner_, gridBagConstraints);
        
        correlationShow3DButton_        = new JButton("Display 3D");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        correlationPanel_.add(correlationShow3DButton_, gridBagConstraints);
        
        updateLabel_                    = new JLabel("Model Update");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 6;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 12, 0, 0);
        correlationPanel_.add(updateLabel_, gridBagConstraints);
        
        updateSeparator_                = new JSeparator();
        gridBagConstraints.fill         = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 6;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 4;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 5, 0, 12);
        correlationPanel_.add(updateSeparator_, gridBagConstraints);
        
        TableModel updateKeyModel;
        updateKeyModel = correlationPanel_.getModeCorrelationTableModel();
        
        JTable updateKeyTable = new JTable();
        updateKeyTable.setModel(updateKeyModel);
        updateKeyTable.setPreferredScrollableViewportSize(
                new Dimension(32, 256));
        correlationPanel_.setColumnProperties(updateKeyTable);
        
        updateKeyScrollPane_            = new JScrollPane(updateKeyTable);
        updateKeyScrollPane_.setVerticalScrollBarPolicy(
                ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        gridBagConstraints.fill         = GridBagConstraints.BOTH;
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 7;
        gridBagConstraints.gridheight   = 4;
        gridBagConstraints.gridwidth    = 3;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        correlationPanel_.add(updateKeyScrollPane_, gridBagConstraints);
        
        updateKeySelectAllButton_       = new JButton("Select All");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 7;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        correlationPanel_.add(updateKeySelectAllButton_, gridBagConstraints);
        
        updateKeyDeselectAllButton_     = new JButton("Deselect All");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 8;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        correlationPanel_.add(updateKeyDeselectAllButton_, gridBagConstraints);
        
        updateKeyDefaultButton_         = new JButton("Default");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 9;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        correlationPanel_.add(updateKeyDefaultButton_, gridBagConstraints);
        
        updateOutputLabel_              = new JLabel("NAS Output File:");
        updateOutputLabel_.setLabelFor(updateOutputTextField_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 11;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 12, 0);
        correlationPanel_.add(updateOutputLabel_, gridBagConstraints);
        
        updateOutputTextField_          = new JTextField(40);
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 11;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 12, 0);
        correlationPanel_.add(updateOutputTextField_, gridBagConstraints);
        
        updateSaveButton_               = new JButton("Save...");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 11;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 12, 12);
        correlationPanel_.add(updateSaveButton_, gridBagConstraints);
        
        bottomFillerLabel_              = new JLabel();
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 12;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 1;
        gridBagConstraints.insets.set(0, 0, 0, 0);
        correlationPanel_.add(bottomFillerLabel_, gridBagConstraints);
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
        correlationPanel_.setLayout(new GridBagLayout());
        correlationPanel_.setAlignmentX(JModeCorrelationPanel.LEFT_ALIGNMENT);
    } // eom
    
    
    
    /***************************************************************************
     * Installs all listeners for all components in this container.
     **************************************************************************/
    protected void installListeners() {
        massReadAction_ = new MassReadAction(correlationPanel_, this);
        massInputTextField_.setAction(massReadAction_);
        
        massOpenAction_ = new MassOpenAction(correlationPanel_, this);
        massOpenButton_.setAction(massOpenAction_);
        
        massKeyItemListener_ = new MassKeyItemListener(correlationPanel_, this);
        massKeyComboBox_.addItemListener(massKeyItemListener_);
        
        correlationMakeAction_ = new OrthogonalityCheckMakeAction(
                correlationPanel_, this);
        correlationMakeButton_.setAction(correlationMakeAction_);
        
        correlationSaveAction_ = new ModeCorrelationSaveAction(
                correlationPanel_, this);
        correlationOutputTextField_.setAction(correlationSaveAction_);
        
        correlationSaveAsAction_ = new ModeCorrelationSaveAsAction(
                correlationPanel_, this);
        correlationSaveButton_.setAction(correlationSaveAsAction_);
        
        lowerToleranceListener_ = new LowerToleranceChangeListener(
                correlationPanel_, this);
        lowerToleranceSpinner_.addChangeListener(lowerToleranceListener_);
        
        correlationShow2DAction_ = new ModeCorrelationShow2DAction(
                correlationPanel_, this);
        correlationShow2DButton_.setAction(correlationShow2DAction_);
        
        upperToleranceListener_ = new UpperToleranceChangeListener(
                correlationPanel_, this);
        upperToleranceSpinner_.addChangeListener(upperToleranceListener_);
        
        correlationShow3DAction_ = new ModeCorrelationShow3DAction(
                correlationPanel_, this);
        correlationShow3DButton_.setAction(correlationShow3DAction_);
        
        updateKeySelectAllAction_ = new UpdateKeySelectAllAction(
                correlationPanel_, this);
        updateKeySelectAllButton_.setAction(updateKeySelectAllAction_);
        
        updateKeyDeselectAllAction_ = new UpdateKeyDeselectAllAction(
                correlationPanel_, this);
        updateKeyDeselectAllButton_.setAction(updateKeyDeselectAllAction_);
        
        updateKeyDefaultAction_ = new UpdateKeyDefaultAction(correlationPanel_,
                this);
        updateKeyDefaultButton_.setAction(updateKeyDefaultAction_);
        
        updateSaveAction_ = new ModeConnectionSaveAction(correlationPanel_,
                this);
        updateOutputTextField_.setAction(updateSaveAction_);
        
        updateSaveAsAction_ = new ModeConnectionSaveAsAction(
                correlationPanel_, this);
        updateSaveButton_.setAction(updateSaveAsAction_);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void installUI(JModeCorrelationPanel correlationPanel) {
        correlationPanel_ = correlationPanel;
        
        this.installDefaults();
        this.installLayout();
        this.installComponents();
        this.installListeners();
        this.installKeyboardActions();
        this.initialize();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setConnectionOutput(String fileName) {
        updateOutputTextField_.setText(fileName.trim());
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setCorrelationOutput(String fileName) {
        correlationOutputTextField_.setText(fileName.trim());
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setLowerTolerance(double lowerTolerance) {
        lowerToleranceSpinner_.setValue(lowerTolerance);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setMassInput(String fileName) {
        massInputTextField_.setText(fileName.trim());
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setUpperTolerance(double upperTolerance) {
        upperToleranceSpinner_.setValue(upperTolerance);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String showConnectionSaveDialog() {
        String      fileName    = null;
        ModeFilter  nasFilter   = new ModeFilter(ModeFilter.NAS);
        File        file;
        int         result;
        
        file = new File("mode_connection." + ModeFilter.NAS);
        
        correlationFileChooser_.addChoosableFileFilter(nasFilter);
        correlationFileChooser_.setFileFilter(nasFilter);
        correlationFileChooser_.setSelectedFile(file);
        
        result = correlationFileChooser_.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileName = correlationFileChooser_.getSelectedFile().
                    getAbsolutePath();
            
            if (!fileName.toUpperCase().endsWith(ModeFilter.NAS)) {
                fileName = new String(fileName + "." + ModeFilter.NAS);
            }
        }
        
        correlationFileChooser_.removeChoosableFileFilter(nasFilter);
        correlationFileChooser_.setSelectedFile(null);
        
        return fileName;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public String showCorrelationSaveDialog() {
        String      fileName    = null;
        ModeFilter  csvFilter   = new ModeFilter(ModeFilter.CSV);
        File        file;
        int         result;
        
        file = new File("mode_correlation." + ModeFilter.CSV);
        
        correlationFileChooser_.addChoosableFileFilter(csvFilter);
        correlationFileChooser_.setFileFilter(csvFilter);
        correlationFileChooser_.setSelectedFile(file);
        
        result = correlationFileChooser_.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileName = correlationFileChooser_.getSelectedFile().
                    getAbsolutePath();
            
            if (!fileName.toUpperCase().endsWith(ModeFilter.CSV)) {
                fileName = new String(fileName + "." + ModeFilter.CSV);
            }
        }
        
        correlationFileChooser_.removeChoosableFileFilter(csvFilter);
        correlationFileChooser_.setSelectedFile(null);
        
        return fileName;
    } // eom
    
    
    
     /***************************************************************************
     **************************************************************************/
    public String showMassOpenDialog() {
        String      fileName    = null;
        ModeFilter  pchFilter   = new ModeFilter(ModeFilter.PCH);
        int         result;
        
        correlationFileChooser_.addChoosableFileFilter(pchFilter);
        correlationFileChooser_.setFileFilter(pchFilter);
        
        result = correlationFileChooser_.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileName = correlationFileChooser_.getSelectedFile().
                    getAbsolutePath();
        }
        
        correlationFileChooser_.removeChoosableFileFilter(pchFilter);
        
        return fileName;
    } // eom
    
    
    
    /***************************************************************************
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
        correlationFileChooser_ = null;
        
        correlationPanel_.remove(correlationLabel_);
        correlationLabel_ = null;
        
        correlationPanel_.remove(correlationSeparator_);
        correlationSeparator_ = null;
        
        correlationPanel_.remove(massInputLabel_);
        massInputLabel_ = null;
        
        correlationPanel_.remove(massInputTextField_);
        massInputTextField_ = null;
        
        correlationPanel_.remove(massOpenButton_);
        massOpenButton_ = null;
        
        correlationPanel_.remove(massKeyLabel_);
        massKeyLabel_ = null;
        
        correlationPanel_.remove(massKeyComboBox_);
        massKeyComboBox_ = null;
        
        correlationPanel_.remove(correlationMakeButton_);
        correlationMakeButton_ = null;
        
        correlationPanel_.remove(correlationOutputLabel_);
        correlationOutputLabel_ = null;
        
        correlationPanel_.remove(correlationOutputTextField_);
        correlationOutputTextField_ = null;
        
        correlationPanel_.remove(correlationSaveButton_);
        correlationSaveButton_ = null;
        
        correlationPanel_.remove(lowerToleranceLabel_);
        lowerToleranceLabel_ = null;
        
        correlationPanel_.remove(lowerToleranceSpinner_);
        lowerToleranceSpinner_ = null;
        
        correlationPanel_.remove(correlationShow2DButton_);
        correlationShow2DButton_ = null;
        
        correlationPanel_.remove(upperToleranceLabel_);
        upperToleranceLabel_ = null;
        
        correlationPanel_.remove(upperToleranceSpinner_);
        upperToleranceSpinner_ = null;
        
        correlationPanel_.remove(correlationShow3DButton_);
        correlationShow3DButton_ = null;
        
        correlationPanel_.remove(updateLabel_);
        updateLabel_ = null;
        
        correlationPanel_.remove(updateSeparator_);
        updateSeparator_ = null;
        
        correlationPanel_.remove(updateKeyScrollPane_);
        updateKeyScrollPane_ = null;
        
        correlationPanel_.remove(updateKeySelectAllButton_);
        updateKeySelectAllButton_ = null;
        
        correlationPanel_.remove(updateKeyDeselectAllButton_);
        updateKeyDeselectAllButton_ = null;
        
        correlationPanel_.remove(updateKeyDefaultButton_);
        updateKeyDefaultButton_ = null;
        
        correlationPanel_.remove(updateOutputLabel_);
        updateOutputLabel_ = null;
        
        correlationPanel_.remove(updateOutputTextField_);
        updateOutputTextField_ = null;
        
        correlationPanel_.remove(updateSaveButton_);
        updateSaveButton_ = null;
        
        correlationPanel_.remove(bottomFillerLabel_);
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
        correlationPanel_.setBorder(null);
        correlationPanel_.setLayout(null);
    } // eom
    
    
    
    /***************************************************************************
     * Uninstalls all listeners for all components in this container.
     **************************************************************************/
    protected void uninstallListeners() {
        massInputTextField_.removeActionListener(massReadAction_);
        massReadAction_ = null;
        
        massOpenButton_.removeActionListener(massOpenAction_);
        massOpenAction_ = null;
        
        massKeyComboBox_.removeItemListener(massKeyItemListener_);
        massKeyItemListener_ = null;
        
        correlationMakeButton_.removeActionListener(correlationMakeAction_);
        correlationMakeAction_ = null;
        
        correlationOutputTextField_.removeActionListener(
                correlationSaveAction_);
        correlationSaveAction_ = null;
        
        correlationSaveButton_.removeActionListener(correlationSaveAsAction_);
        correlationSaveAsAction_ = null;
        
        lowerToleranceSpinner_.removeChangeListener(lowerToleranceListener_);
        lowerToleranceListener_ = null;
        
        correlationShow2DButton_.removeActionListener(correlationShow2DAction_);
        correlationShow2DAction_ = null;
        
        upperToleranceSpinner_.removeChangeListener(upperToleranceListener_);
        upperToleranceListener_ = null;
        
        correlationShow3DButton_.removeActionListener(correlationShow3DAction_);
        correlationShow3DAction_ = null;
        
        updateKeySelectAllButton_.removeActionListener(
                updateKeySelectAllAction_);
        updateKeySelectAllAction_ = null;
        
        updateKeyDeselectAllButton_.removeActionListener(
                updateKeyDeselectAllAction_);
        updateKeyDeselectAllAction_ = null;
        
        updateKeyDefaultButton_.removeActionListener(updateKeyDefaultAction_);
        updateKeyDefaultAction_ = null;
        
        updateOutputTextField_.removeActionListener(updateSaveAction_);
        updateSaveAction_ = null;
        
        updateSaveButton_.removeActionListener(updateSaveAsAction_);
        updateSaveAsAction_ = null;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void uninstallUI(JModeCorrelationPanel correlationPanel) {
        correlationPanel_ = correlationPanel;
        
        this.uninstallKeyboardActions();
        this.uninstallListeners();
        this.uninstallComponents();
        this.uninstallLayout();
        this.uninstallDefaults();
    } // eom
} // eoc