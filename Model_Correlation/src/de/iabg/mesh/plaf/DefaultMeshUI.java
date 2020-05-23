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

import de.iabg.mesh.JMeshPanel;
import de.iabg.mesh.MeshUI;

import de.iabg.mesh.event.GeometryColorItemListener;
import de.iabg.mesh.event.MeshNameAction;
import de.iabg.mesh.event.MeshOpenAction;
import de.iabg.mesh.event.MeshReadAction;
import de.iabg.mesh.event.MeshShowAction;
import de.iabg.mesh.event.NodeKeyItemListener;
import de.iabg.mesh.event.NodeKeyOpenAction;
import de.iabg.mesh.event.NodeKeyReadAction;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import java.awt.event.ItemListener;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;

/*******************************************************************************
 * This implementation of {@link MeshUI} creates a pluggable Look and Feel user
 * interface according to the {@code MeshUI} class description.  Refer to the
 * class API for more details.
 * 
 * Much of the implementation of this class should be straight-forward because
 * its design is modeled after Swing.  Swing follows a Model-View-Controller
 * (MVC) design pattern and uses a UI delegate, usually with inner classes, to
 * couple the view and controller.  This {@code MeshUI} separates the view and
 * controller and adheres to a slightly stricter MVC model by making controllers
 * their own outer classes.  This causes the view to declare its functionality
 * in methods, however, it allows for some reuse of listener classes and helps
 * maintain the scalability of the view.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 26, 2008
 ******************************************************************************/
public class DefaultMeshUI
        implements MeshUI {
    /** A filler label for the bottom of the layout */
    protected JLabel bottomFillerLabel_;
    
    /** A combo box to select the mesh color */
    protected JComboBox colorComboBox_;
    
    /** A listener to handle mesh color changes */
    protected ItemListener colorItemListener_;
    
    /** A label for the mesh color */
    protected JLabel colorLabel_;
    
    /** A constant for an error message */
    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    
    /** The file chooser of this interface */
    protected JFileChooser meshFileChooser_;
    
    /** A label for the mesh input file */
    protected JLabel meshInputLabel_;
    
    /** A text field for the mesh input file */
    protected JTextField meshInputTextField_;
    
    /** A label for the header of this interface */
    protected JLabel meshLabel_;
    
    /** A listener to handle mesh name changes */
    protected Action meshNameAction_;
    
    /** A label for the mesh name */
    protected JLabel meshNameLabel_;
    
    /** A text field for the mesh name */
    protected JTextField meshNameTextField_;
    
    /** A listener to open the mesh */
    protected Action meshOpenAction_;
    
    /** A button to open the mesh */
    protected JButton meshOpenButton_;
    
    /** The {@link JMeshPanel} that this interface is designed for */
    protected JMeshPanel meshPanel_;
    
    /** A listener to open the mesh */
    protected Action meshReadAction_;
    
    /** A separator for the header of this interface */
    protected JSeparator meshSeparator_;
    
    /** A listener to render the mesh */
    protected Action meshShowAction_;
    
    /** A button to render the mesh */
    protected JButton meshShowButton_;
    
    /** A combo box to select the node key group */
    protected JComboBox nodeKeyComboBox_;
    
    /** A label for the node key group input file */
    protected JLabel nodeKeyInputLabel_;
    
    /** A text field for the node key group input file */
    protected JTextField nodeKeyInputTextField_;
    
    /** A listener to handle set changes */
    protected ItemListener nodeKeyItemListener_;
    
    /** A label for the node key group */
    protected JLabel nodeKeyLabel_;
    
    /** A listener to open the node key groups */
    protected Action nodeKeyOpenAction_;
    
    /** A button to open the node key groups */
    protected JButton nodeKeyOpenButton_;
    
    /** A listener to open the node key groups */
    protected Action nodeKeyReadAction_;
    
    /** A constant for a warning message */
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    
    
    
    /***************************************************************************
     * Constructs a {@code DefaultMeshUI}.
     **************************************************************************/
    public DefaultMeshUI() {
        
    } // eom
    
    
    
    /***************************************************************************
     * Returns the mesh input file name.
     * 
     * @return  the mesh input file name
     **************************************************************************/
    public String getMeshInput() {
        return meshInputTextField_.getText().trim();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the mesh name.
     * 
     * @return  the mesh name
     **************************************************************************/
    public String getMeshName() {
        return meshNameTextField_.getText().trim();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the node key group input file name.
     * 
     * @return  the node key group input file name
     **************************************************************************/
    public String getNodeKeyInput() {
        return nodeKeyInputTextField_.getText().trim();
    } // eom
    
    
    
    /***************************************************************************
     * Initializes all fields or default settings after all components have been
     * added.
     **************************************************************************/
    protected void initialize() {
        meshNameTextField_.setText(meshPanel_.getName());
    } // eom
    
    
    
    /***************************************************************************
     * Installs and lays out all components on the container.
     **************************************************************************/
    protected void installComponents() {
        GridBagConstraints gridBagConstraints;
        gridBagConstraints          = new GridBagConstraints();
        gridBagConstraints.fill     = GridBagConstraints.BOTH;
        gridBagConstraints.anchor   = GridBagConstraints.CENTER;
        
        meshFileChooser_ = new JFileChooser();
        meshFileChooser_.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        meshLabel_                      = new JLabel("Geometry");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 0;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 12, 0, 0);
        meshPanel_.add(meshLabel_, gridBagConstraints);
        
        meshSeparator_                  = new JSeparator();
        gridBagConstraints.fill         = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 0;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 4;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 5, 0, 12);
        meshPanel_.add(meshSeparator_, gridBagConstraints);
        
        meshNameLabel_                  = new JLabel("Name:");
        gridBagConstraints.fill         = GridBagConstraints.BOTH;
        meshNameLabel_.setLabelFor(meshNameTextField_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        meshPanel_.add(meshNameLabel_, gridBagConstraints);
        
        meshNameTextField_              = new JTextField(40);
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        meshPanel_.add(meshNameTextField_, gridBagConstraints);
        
        meshInputLabel_                 = new JLabel("BDF Input File:");
        meshInputLabel_.setLabelFor(meshInputTextField_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        meshPanel_.add(meshInputLabel_, gridBagConstraints);
        
        meshInputTextField_             = new JTextField(40);
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        meshPanel_.add(meshInputTextField_, gridBagConstraints);
        
        meshOpenButton_                 = new JButton("Open...");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        meshPanel_.add(meshOpenButton_, gridBagConstraints);
        
        nodeKeyInputLabel_              = new JLabel("SET Input File:");
        nodeKeyInputLabel_.setLabelFor(nodeKeyInputTextField_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 3;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        meshPanel_.add(nodeKeyInputLabel_, gridBagConstraints);
        
        nodeKeyInputTextField_          = new JTextField(40);
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 3;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        meshPanel_.add(nodeKeyInputTextField_, gridBagConstraints);
        
        nodeKeyOpenButton_              = new JButton("Open...");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 3;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        meshPanel_.add(nodeKeyOpenButton_, gridBagConstraints);
        
        nodeKeyLabel_                   = new JLabel("Node Group:");
        nodeKeyLabel_.setLabelFor(nodeKeyComboBox_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 4;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        meshPanel_.add(nodeKeyLabel_, gridBagConstraints);
        
        nodeKeyComboBox_                = new JComboBox();
        nodeKeyComboBox_.setModel(meshPanel_.getNodeKeyModel());
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 4;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        meshPanel_.add(nodeKeyComboBox_, gridBagConstraints);
        
        colorLabel_                     = new JLabel("Color:");
        colorLabel_.setLabelFor(colorComboBox_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 12, 0);
        meshPanel_.add(colorLabel_, gridBagConstraints);
        
        colorComboBox_                  = new JComboBox();
        colorComboBox_.setModel(meshPanel_.getGeometryColorModel());
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 12, 0);
        meshPanel_.add(colorComboBox_, gridBagConstraints);
        
        meshShowButton_                 = new JButton("Display");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 12, 12);
        meshPanel_.add(meshShowButton_, gridBagConstraints);
        
        bottomFillerLabel_              = new JLabel();
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 6;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 1;
        gridBagConstraints.insets.set(0, 0, 0, 0);
        meshPanel_.add(bottomFillerLabel_, gridBagConstraints);
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
        meshPanel_.setLayout(new GridBagLayout());
        meshPanel_.setAlignmentX(JMeshPanel.LEFT_ALIGNMENT);
    } // eom
    
    
    
    /***************************************************************************
     * Installs all listeners for all components in this container.
     **************************************************************************/
    protected void installListeners() {
        meshNameAction_ = new MeshNameAction(meshPanel_, this);
        meshNameTextField_.setAction(meshNameAction_);
        
        meshReadAction_ = new MeshReadAction(meshPanel_, this);
        meshInputTextField_.setAction(meshReadAction_);
        
        meshOpenAction_ = new MeshOpenAction(meshPanel_, this);
        meshOpenButton_.setAction(meshOpenAction_);
        
        nodeKeyReadAction_ = new NodeKeyReadAction(meshPanel_, this);
        nodeKeyInputTextField_.setAction(nodeKeyReadAction_);
        
        nodeKeyOpenAction_ = new NodeKeyOpenAction(meshPanel_, this);
        nodeKeyOpenButton_.setAction(nodeKeyOpenAction_);
        
        nodeKeyItemListener_ = new NodeKeyItemListener(meshPanel_, this);
        nodeKeyComboBox_.addItemListener(nodeKeyItemListener_);
        
        colorItemListener_ = new GeometryColorItemListener(meshPanel_, this);
        colorComboBox_.addItemListener(colorItemListener_);
        
        meshShowAction_ = new MeshShowAction(meshPanel_, this);
        meshShowButton_.setAction(meshShowAction_);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void installUI(JMeshPanel meshPanel) {
        meshPanel_ = meshPanel;
        
        this.installDefaults();
        this.installLayout();
        this.installComponents();
        this.installListeners();
        this.installKeyboardActions();
        this.initialize();
    } // eom
    
    
    
    /***************************************************************************
     * Replaces the mesh input file with the given one.
     * 
     * @param   fileName    the new mesh input file name
     **************************************************************************/
    public void setMeshInput(String fileName) {
        meshInputTextField_.setText(fileName.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Replaces the node key group input file with the given one.
     * 
     * @param   fileName    the new node key group input file name
     **************************************************************************/
    public void setNodeKeyInput(String fileName) {
        nodeKeyInputTextField_.setText(fileName.trim());
    } // eom
    
    
    
    /***************************************************************************
     * Shows a file chooser dialog for the supported mesh input file types and
     * returns the selected file.
     * 
     * @return  the selected mesh input file
     **************************************************************************/
    public String showMeshOpenDialog() {
        String      fileName    = null;
        MeshFilter  bdfFilter   = new MeshFilter(MeshFilter.BDF);
        MeshFilter  datFilter   = new MeshFilter(MeshFilter.DAT);
        MeshFilter  nasFilter   = new MeshFilter(MeshFilter.NAS);
        int         result;
        
        meshFileChooser_.addChoosableFileFilter(bdfFilter);
        meshFileChooser_.addChoosableFileFilter(datFilter);
        meshFileChooser_.addChoosableFileFilter(nasFilter);
        meshFileChooser_.setFileFilter(bdfFilter);
        
        result = meshFileChooser_.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileName = meshFileChooser_.getSelectedFile().getAbsolutePath();
        }
        
        meshFileChooser_.removeChoosableFileFilter(bdfFilter);
        meshFileChooser_.removeChoosableFileFilter(datFilter);
        meshFileChooser_.removeChoosableFileFilter(nasFilter);
        
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
     * Shows a file chooser dialog for the supported node key group input file
     * types and returns the selected file.
     * 
     * @return  the selected node key group input file
     **************************************************************************/
    public String showNodeKeyOpenDialog() {
        String      fileName    = null;
        MeshFilter  setFilter   = new MeshFilter(MeshFilter.SET);
        MeshFilter  nsetFilter  = new MeshFilter(MeshFilter.NSET);
        int         result;
        
        meshFileChooser_.addChoosableFileFilter(setFilter);
        meshFileChooser_.addChoosableFileFilter(nsetFilter);
        meshFileChooser_.setFileFilter(setFilter);
        
        result = meshFileChooser_.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            fileName = meshFileChooser_.getSelectedFile().getAbsolutePath();
        }
        
        meshFileChooser_.removeChoosableFileFilter(setFilter);
        meshFileChooser_.removeChoosableFileFilter(nsetFilter);
        
        return fileName;
    } // eom
    
    
    
    /***************************************************************************
     * Uninstalls all components on the container.
     **************************************************************************/
    protected void uninstallComponents() {
        meshFileChooser_ = null;
        
        meshPanel_.remove(meshLabel_);
        meshLabel_ = null;
        
        meshPanel_.remove(meshSeparator_);
        meshSeparator_ = null;
        
        meshPanel_.remove(meshNameLabel_);
        meshNameLabel_ = null;
        
        meshPanel_.remove(meshNameTextField_);
        meshNameTextField_ = null;
        
        meshPanel_.remove(meshInputLabel_);
        meshInputLabel_ = null;
        
        meshPanel_.remove(meshInputTextField_);
        meshInputTextField_ = null;
        
        meshPanel_.remove(meshOpenButton_);
        meshOpenButton_ = null;
        
        meshPanel_.remove(nodeKeyInputLabel_);
        nodeKeyInputLabel_ = null;
        
        meshPanel_.remove(nodeKeyInputTextField_);
        nodeKeyInputTextField_ = null;
        
        meshPanel_.remove(nodeKeyOpenButton_);
        nodeKeyOpenButton_ = null;
        
        meshPanel_.remove(nodeKeyLabel_);
        nodeKeyLabel_ = null;
        
        meshPanel_.remove(nodeKeyComboBox_);
        nodeKeyComboBox_ = null;
        
        meshPanel_.remove(colorLabel_);
        colorLabel_ = null;
        
        meshPanel_.remove(colorComboBox_);
        colorComboBox_ = null;
        
        meshPanel_.remove(meshShowButton_);
        meshShowButton_ = null;
        
        meshPanel_.remove(bottomFillerLabel_);
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
        meshPanel_.setBorder(null);
        meshPanel_.setLayout(null);
    } // eom
    
    
    
    /***************************************************************************
     * Uninstalls all listeners for all components in this container.
     **************************************************************************/
    protected void uninstallListeners() {
        meshNameTextField_.removeActionListener(meshNameAction_);
        meshNameAction_ = null;
        
        meshInputTextField_.removeActionListener(meshReadAction_);
        meshReadAction_ = null;
        
        meshOpenButton_.removeActionListener(meshOpenAction_);
        meshOpenAction_ = null;
        
        nodeKeyInputTextField_.removeActionListener(nodeKeyReadAction_);
        nodeKeyReadAction_ = null;
        
        nodeKeyOpenButton_.removeActionListener(nodeKeyOpenAction_);
        nodeKeyOpenAction_ = null;
        
        nodeKeyComboBox_.removeItemListener(nodeKeyItemListener_);
        nodeKeyItemListener_ = null;
        
        colorComboBox_.removeItemListener(colorItemListener_);
        colorItemListener_ = null;
        
        meshShowButton_.removeActionListener(meshShowAction_);
        meshShowAction_ = null;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void uninstallUI(JMeshPanel meshPanel) {
        meshPanel_ = meshPanel;
        
        this.uninstallKeyboardActions();
        this.uninstallListeners();
        this.uninstallComponents();
        this.uninstallLayout();
        this.uninstallDefaults();
    } // eom
} // eoc