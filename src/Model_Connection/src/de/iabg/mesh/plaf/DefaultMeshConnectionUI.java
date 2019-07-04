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
package de.iabg.mesh.plaf;

import de.iabg.j3d.event.RenderableSceneListener;
import de.iabg.j3d.event.SceneListener;

import de.iabg.mesh.JMeshConnectionPanel;
import de.iabg.mesh.JMeshPanel;
import de.iabg.mesh.MeshConnectionUI;

import de.iabg.mesh.event.ConnectionColorItemListener;
import de.iabg.mesh.event.MeshConnectionMakeAction;
import de.iabg.mesh.event.MeshConnectionOpenAction;
import de.iabg.mesh.event.MeshConnectionReadAction;
import de.iabg.mesh.event.MeshConnectionSaveAction;
import de.iabg.mesh.event.MeshConnectionSaveAsAction;
import de.iabg.mesh.event.MeshConnectionShowAction;

import de.iabg.swing.event.LoggableLogListener;
import de.iabg.swing.event.LogListener;

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
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.ToolTipManager;

/*******************************************************************************
 * This implementation of {@link MeshConnectionUI} creates a pluggable Look and
 * Feel user interface according to the {@code MeshConnectionUI} class
 * description.  Refer to the class API for more details.
 * 
 * Much of the implementation of this class should be straight-forward because
 * its design is modeled after Swing.  Swing follows a Model-View-Controller
 * (MVC) design pattern and uses a UI delegate, usually with inner classes, to
 * couple the view and controller.  This {@code MeshConnectionUI} separates the
 * view and controller and adheres to a slightly stricter MVC model by making
 * controllers their own outer classes.  This causes the view to declare its
 * functionality in methods, however, it allows for some reuse of listener
 * classes and helps maintain the scalability of the view.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public class DefaultMeshConnectionUI
        implements MeshConnectionUI {
    /** A filler label for the bottom of the layout */
    protected JLabel bottomFillerLabel_;
    
    /** A combo box to select the connection color */
    protected JComboBox colorComboBox_;
    
    /** A listener to handle connection color changes */
    protected ItemListener colorItemListener_;
    
    /** A label for the connection color */
    protected JLabel colorLabel_;
    
    /** The file chooser of this interface */
    protected JFileChooser connectionFileChooser_;
    
    /** A label for the connection input file */
    protected JLabel connectionInputLabel_;
    
    /** A text field for the connection input file */
    protected JTextField connectionInputTextField_;
    
    /** A label for the header of this interface */
    protected JLabel connectionLabel_;
    
    /** A listener to connect the meshes */
    protected Action connectionMakeAction_;
    
    /** A button to connect the meshes */
    protected JButton connectionMakeButton_;
    
    /** A listener to open the connection */
    protected Action connectionOpenAction_;
    
    /** A button to open the connection */
    protected JButton connectionOpenButton_;
    
    /** A label for the connection output file */
    protected JLabel connectionOutputLabel_;
    
    /** A text field for the connection output file */
    protected JTextField connectionOutputTextField_;
    
    /** The {@link JMeshConnectionPanel} that this interface is designed for */
    protected JMeshConnectionPanel connectionPanel_;
    
    /** A listener to open the connection */
    protected Action connectionReadAction_;
    
    /** A listener to save the connection */
    protected Action connectionSaveAction_;
    
    /** A listener to save the connection */
    protected Action connectionSaveAsAction_;
    
    /** A button to save the connection */
    protected JButton connectionSaveButton_;
    
    /** A separator for the header of this interface */
    protected JSeparator connectionSeparator_;
    
    /** A listener to render the connection */
    protected Action connectionShowAction_;
    
    /** A button to render the connection */
    protected JButton connectionShowButton_;
    
    /** A constant for an error message */
    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    
    /** The {@code First Mesh} */
    protected JMeshPanel firstMeshPanel_;
    
    /** The {@code Last Mesh} */
    protected JMeshPanel lastMeshPanel_;
    
    /** A listener to handle {@code LogEvents} */
    protected LogListener logListener_;
    
    /** A label for the node tolerance */
    protected JLabel nodeToleranceLabel_;
    
    /** A spinner for the node tolerance */
    protected JSpinner nodeToleranceSpinner_;
    
    /** A listener to handle {@code SceneEvents} */
    protected SceneListener sceneListener_;
    
    /** A constant for a warning message */
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    
    
    
    /***************************************************************************
     * Constructs a {@code DefaultMeshConnectionUI}.
     **************************************************************************/
    public DefaultMeshConnectionUI() {
        
    } // eom
    
    
    
    /***************************************************************************
     * Returns the node connection input file name.
     * 
     * @return  the node connection input file name
     **************************************************************************/
    public String getConnectionInput() {
        return connectionInputTextField_.getText().trim();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the node connection output file name.
     * 
     * @return  the node connection output file name
     **************************************************************************/
    public String getConnectionOutput() {
        String fileName = connectionOutputTextField_.getText().trim();
        
        if (!fileName.toUpperCase().endsWith(MeshFilter.TXT)) {
            fileName = new String(fileName + "." + MeshFilter.TXT);
            connectionOutputTextField_.setText(fileName);
        }
        
        return fileName;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the current node connection tolerance.
     * 
     * @return  the current node connection tolerance
     **************************************************************************/
    public double getConnectionTolerance() {
        return (Double) nodeToleranceSpinner_.getValue();
    } // eom
    
    
    
    /***************************************************************************
     * Initializes all fields or default settings after all components have been
     * added.
     **************************************************************************/
    protected void initialize() {
        double tolerance = 0.0000;
        nodeToleranceSpinner_.setValue(tolerance);
    } // eom
    
    
    
    /***************************************************************************
     * Installs and lays out all components on the container.
     **************************************************************************/
    protected void installComponents() {
        GridBagConstraints gridBagConstraints;
        gridBagConstraints          = new GridBagConstraints();
        gridBagConstraints.fill     = GridBagConstraints.BOTH;
        gridBagConstraints.anchor   = GridBagConstraints.CENTER;
        
        connectionFileChooser_ = new JFileChooser();
        connectionFileChooser_.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        firstMeshPanel_                 = connectionPanel_.getFirstMesh();
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 0;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 5;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(0, 0, 0, 0);
        connectionPanel_.add(firstMeshPanel_, gridBagConstraints);
        
        lastMeshPanel_                  = connectionPanel_.getLastMesh();
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 5;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(0, 0, 0, 0);
        connectionPanel_.add(lastMeshPanel_, gridBagConstraints);
        
        connectionLabel_                = new JLabel("Node Connection");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 12, 0, 0);
        connectionPanel_.add(connectionLabel_, gridBagConstraints);
        
        connectionSeparator_            = new JSeparator();
        gridBagConstraints.fill         = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 4;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 5, 0, 12);
        connectionPanel_.add(connectionSeparator_, gridBagConstraints);
        
        connectionInputLabel_           = new JLabel("TXT Input File:");
        connectionInputLabel_.setLabelFor(connectionInputTextField_);
        gridBagConstraints.fill         = GridBagConstraints.BOTH;
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 3;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionInputLabel_, gridBagConstraints);
        
        connectionInputTextField_       = new JTextField(40);
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 3;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionInputTextField_, gridBagConstraints);
        
        connectionOpenButton_           = new JButton("Open...");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 3;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        connectionPanel_.add(connectionOpenButton_, gridBagConstraints);
        
        nodeToleranceLabel_             = new JLabel("Max. Tolerance:");
        nodeToleranceLabel_.setLabelFor(nodeToleranceSpinner_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 4;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(nodeToleranceLabel_, gridBagConstraints);
        
        SpinnerModel nodeToleranceModel;
        nodeToleranceModel = new SpinnerNumberModel(0.0, 0.0, null, 1.0);
        
        nodeToleranceSpinner_           = new JSpinner();
        nodeToleranceSpinner_.setModel(nodeToleranceModel);
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 4;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(nodeToleranceSpinner_, gridBagConstraints);
        
        connectionMakeButton_           = new JButton("Connect");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 4;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        connectionPanel_.add(connectionMakeButton_, gridBagConstraints);
        
        connectionOutputLabel_          = new JLabel("TXT Output File:");
        connectionOutputLabel_.setLabelFor(connectionOutputTextField_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionOutputLabel_, gridBagConstraints);
        
        connectionOutputTextField_      = new JTextField(40);
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionOutputTextField_, gridBagConstraints);
        
        connectionSaveButton_           = new JButton("Save...");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        connectionPanel_.add(connectionSaveButton_, gridBagConstraints);
        
        colorLabel_                     = new JLabel("Color:");
        colorLabel_.setLabelFor(colorComboBox_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 6;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 12, 0);
        connectionPanel_.add(colorLabel_, gridBagConstraints);
        
        colorComboBox_                  = new JComboBox();
        colorComboBox_.setModel(connectionPanel_.getConnectionColorModel());
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 6;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 12, 0);
        connectionPanel_.add(colorComboBox_, gridBagConstraints);
        
        connectionShowButton_           = new JButton("Display");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 6;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 12, 12);
        connectionPanel_.add(connectionShowButton_, gridBagConstraints);
        
        bottomFillerLabel_              = new JLabel();
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 7;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 1;
        gridBagConstraints.insets.set(0, 0, 0, 0);
        connectionPanel_.add(bottomFillerLabel_, gridBagConstraints);
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
        connectionPanel_.setLayout(new GridBagLayout());
        connectionPanel_.setAlignmentX(JMeshConnectionPanel.LEFT_ALIGNMENT);
    } // eom
    
    
    
    /***************************************************************************
     * Installs all listeners for all components in this container.
     **************************************************************************/
    protected void installListeners() {
        logListener_ = new LoggableLogListener(connectionPanel_);
        firstMeshPanel_.addLogListener(logListener_);
        lastMeshPanel_.addLogListener(logListener_);
        
        sceneListener_ = new RenderableSceneListener(connectionPanel_);
        firstMeshPanel_.addSceneListener(sceneListener_);
        lastMeshPanel_.addSceneListener(sceneListener_);
        
        connectionReadAction_ = new MeshConnectionReadAction(connectionPanel_,
                this);
        connectionInputTextField_.setAction(connectionReadAction_);
        
        connectionOpenAction_ = new MeshConnectionOpenAction(connectionPanel_,
                this);
        connectionOpenButton_.setAction(connectionOpenAction_);
        
        connectionMakeAction_ = new MeshConnectionMakeAction(connectionPanel_,
                this);
        connectionMakeButton_.setAction(connectionMakeAction_);
        
        connectionSaveAction_ = new MeshConnectionSaveAction(connectionPanel_,
                this);
        connectionOutputTextField_.setAction(connectionSaveAction_);
        
        connectionSaveAsAction_ = new MeshConnectionSaveAsAction(
                connectionPanel_, this);
        connectionSaveButton_.setAction(connectionSaveAsAction_);
        
        colorItemListener_ = new ConnectionColorItemListener(connectionPanel_,
                this);
        colorComboBox_.addItemListener(colorItemListener_);
        
        connectionShowAction_ = new MeshConnectionShowAction(connectionPanel_,
                this);
        connectionShowButton_.setAction(connectionShowAction_);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void installUI(JMeshConnectionPanel connectionPanel) {
        connectionPanel_ = connectionPanel;
        
        this.installDefaults();
        this.installLayout();
        this.installComponents();
        this.installListeners();
        this.installKeyboardActions();
        this.initialize();
    } // eom
    
    
    
    /***************************************************************************
     * Replaces the node connection input file with the given one.
     * 
     * @param   fileName    the new node connection input file name
     **************************************************************************/
    public void setConnectionInput(String fileName) {
        connectionInputTextField_.setText(fileName.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Replaces the node connection output file with the given one.
     * 
     * @param   fileName    the new node connection output file name
     **************************************************************************/
    public void setConnectionOutput(String fileName) {
        connectionOutputTextField_.setText(fileName.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Shows a file chooser dialog for the supported node connection input file
     * types and returns the selected file.
     * 
     * @return  the selected node connection input file
     **************************************************************************/
    public String showConnectionOpenDialog() {
        String      fileName    = null;
        MeshFilter  txtFilter   = new MeshFilter(MeshFilter.TXT);
        int         result;
        
        connectionFileChooser_.addChoosableFileFilter(txtFilter);
        connectionFileChooser_.setFileFilter(txtFilter);
        
        result = connectionFileChooser_.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileName = connectionFileChooser_.getSelectedFile().
                    getAbsolutePath();
        }
        
        connectionFileChooser_.removeChoosableFileFilter(txtFilter);
        
        return fileName;
    } // eom
    
    
    
    /***************************************************************************
     * Shows a file chooser dialog for the supported node connection output file
     * types and returns the selected file.
     * 
     * @return  the selected node connection output file
     **************************************************************************/
    public String showConnectionSaveDialog() {
        String      fileName    = null;
        MeshFilter  txtFilter   = new MeshFilter(MeshFilter.TXT);
        File        file;
        int         result;
        
        file = new File("node_connection." + MeshFilter.TXT);
        
        connectionFileChooser_.addChoosableFileFilter(txtFilter);
        connectionFileChooser_.setFileFilter(txtFilter);
        connectionFileChooser_.setSelectedFile(file);
        
        result = connectionFileChooser_.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileName = connectionFileChooser_.getSelectedFile().
                    getAbsolutePath();
            
            if (!fileName.toUpperCase().endsWith(MeshFilter.TXT)) {
                fileName = new String(fileName + "." + MeshFilter.TXT);
            }
        }
        
        connectionFileChooser_.removeChoosableFileFilter(txtFilter);
        connectionFileChooser_.setSelectedFile(null);
        
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
        connectionFileChooser_ = null;
        
        connectionPanel_.remove(firstMeshPanel_);
        
        connectionPanel_.remove(lastMeshPanel_);
        
        connectionPanel_.remove(connectionLabel_);
        connectionLabel_ = null;
        
        connectionPanel_.remove(connectionSeparator_);
        connectionSeparator_ = null;
        
        connectionPanel_.remove(connectionInputLabel_);
        connectionInputLabel_ = null;
        
        connectionPanel_.remove(connectionInputTextField_);
        connectionInputTextField_ = null;
        
        connectionPanel_.remove(connectionOpenButton_);
        connectionOpenButton_ = null;
        
        connectionPanel_.remove(nodeToleranceLabel_);
        nodeToleranceLabel_ = null;
        
        connectionPanel_.remove(nodeToleranceSpinner_);
        nodeToleranceSpinner_ = null;
        
        connectionPanel_.remove(connectionMakeButton_);
        connectionMakeButton_ = null;
        
        connectionPanel_.remove(connectionOutputLabel_);
        connectionOutputLabel_ = null;
        
        connectionPanel_.remove(connectionOutputTextField_);
        connectionOutputTextField_ = null;
        
        connectionPanel_.remove(connectionSaveButton_);
        connectionSaveButton_ = null;
        
        connectionPanel_.remove(colorLabel_);
        colorLabel_ = null;
        
        connectionPanel_.remove(colorComboBox_);
        colorComboBox_ = null;
        
        connectionPanel_.remove(connectionShowButton_);
        connectionShowButton_ = null;
        
        connectionPanel_.remove(bottomFillerLabel_);
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
        connectionPanel_.setBorder(null);
        connectionPanel_.setLayout(null);
    } // eom
    
    
    
    /***************************************************************************
     * Uninstalls all listeners for all components in this container.
     **************************************************************************/
    protected void uninstallListeners() {
        firstMeshPanel_.removeLogListener(logListener_);
        lastMeshPanel_.removeLogListener(logListener_);
        logListener_ = null;
        
        firstMeshPanel_.removeSceneListener(sceneListener_);
        lastMeshPanel_.removeSceneListener(sceneListener_);
        sceneListener_ = null;
        
        connectionInputTextField_.removeActionListener(connectionReadAction_);
        connectionReadAction_ = null;
        
        connectionOpenButton_.removeActionListener(connectionOpenAction_);
        connectionOpenAction_ = null;
        
        connectionMakeButton_.removeActionListener(connectionMakeAction_);
        connectionMakeAction_ = null;
        
        connectionOutputTextField_.removeActionListener(connectionSaveAction_);
        connectionSaveAction_ = null;
        
        connectionSaveButton_.removeActionListener(connectionSaveAsAction_);
        connectionSaveAsAction_ = null;
        
        colorComboBox_.removeItemListener(colorItemListener_);
        colorItemListener_ = null;
        
        connectionShowButton_.removeActionListener(connectionShowAction_);
        connectionShowAction_ = null;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void uninstallUI(JMeshConnectionPanel connectionPanel) {
        connectionPanel_ = connectionPanel;
        
        this.uninstallKeyboardActions();
        this.uninstallListeners();
        this.uninstallComponents();
        this.uninstallLayout();
        this.uninstallDefaults();
    } // eom
} // eoc