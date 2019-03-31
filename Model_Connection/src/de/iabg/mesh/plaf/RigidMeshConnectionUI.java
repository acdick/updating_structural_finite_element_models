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

import de.iabg.mesh.MeshConnectionUI;

import de.iabg.mesh.event.ConnectionColorItemListener;
import de.iabg.mesh.event.ConnectionDOFItemListener;
import de.iabg.mesh.event.ConnectionIdentifierChangeListener;
import de.iabg.mesh.event.MeshConnectionMakeAction;
import de.iabg.mesh.event.MeshConnectionOpenAction;
import de.iabg.mesh.event.MeshConnectionReadAction;
import de.iabg.mesh.event.MeshConnectionSaveAction;
import de.iabg.mesh.event.MeshConnectionSaveAsAction;
import de.iabg.mesh.event.MeshConnectionShowAction;

import de.iabg.swing.event.LoggableLogListener;

import java.awt.GridBagConstraints;

import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import javax.swing.event.ChangeListener;

/*******************************************************************************
 * This implementation of {@link MeshConnectionUI} extends
 * {@code DefaultMeshConnectionUI} to create a rigid connection.
 * 
 * @author  Adam C. Dick, BSE
 * @version November 1, 2008
 ******************************************************************************/
public class RigidMeshConnectionUI extends DefaultMeshConnectionUI {
    /** A label for the connection displacements */
    protected JLabel connectionDisplacementLabel_;
    
    /** A check box for the X displacements */
    protected JCheckBox connectionDisplacementXCheckBox_;
    
    /** A check box for the Y displacements */
    protected JCheckBox connectionDisplacementYCheckBox_;
    
    /** A check box for the Z displacements */
    protected JCheckBox connectionDisplacementZCheckBox_;
    
    /** A listener to handle connected degree of freedom changes */
    protected ItemListener connectionDOFItemListener_;
    
    /** A listener to handle connection identifier changes */
    protected ChangeListener connectionIdentifierChangeListener_;
    
    /** A label for the connection identifier */
    protected JLabel connectionIdentifierLabel_;
    
    /** A spinner for the connection identifier */
    protected JSpinner connectionIdentifierSpinner_;
    
    /** A combo box to select the independent mesh */
    protected JComboBox connectionIndependentComboBox_;
    
    /** A label for the independent mesh */
    protected JLabel connectionIndependentLabel_;
    
    /** A label for the connection rotations */
    protected JLabel connectionRotationLabel_;
    
    /** A check box for the X rotations */
    protected JCheckBox connectionRotationXCheckBox_;
    
    /** A check box for the Y rotations */
    protected JCheckBox connectionRotationYCheckBox_;
    
    /** A check box for the Z rotations */
    protected JCheckBox connectionRotationZCheckBox_;
    
    /** A combo box to select the connection type */
    protected JComboBox connectionTypeComboBox_;
    
    /** A label for the connection type */
    protected JLabel connectionTypeLabel_;
    
    
    
    /***************************************************************************
     * Constructs a {@code RigidMeshConnectionUI}.
     **************************************************************************/
    public RigidMeshConnectionUI() {
        super();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the string representation of the connected degrees of freedom.
     * 
     * @return  the connected degrees of freedom
     **************************************************************************/
    public String getConnectionDegreesOfFreedom() {
        StringBuilder connectionDOF = new StringBuilder();
        
        if (connectionDisplacementXCheckBox_.isSelected()) {
            connectionDOF.append("1");
        }
        
        if (connectionDisplacementYCheckBox_.isSelected()) {
            connectionDOF.append("2");
        }
        
        if (connectionDisplacementZCheckBox_.isSelected()) {
            connectionDOF.append("3");
        }
        
        if (connectionRotationXCheckBox_.isSelected()) {
            connectionDOF.append("4");
        }
        
        if (connectionRotationYCheckBox_.isSelected()) {
            connectionDOF.append("5");
        }
        
        if (connectionRotationZCheckBox_.isSelected()) {
            connectionDOF.append("6");
        }
        
        return connectionDOF.toString();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the current connection identifier.
     * 
     * @return  the current connection identifier
     **************************************************************************/
    public int getConnectionIdentifier() {
        return (Integer) connectionIdentifierSpinner_.getValue();
    } // eom
    
    
    
    /***************************************************************************
     * Initializes all fields or default settings after all components have been
     * added.
     **************************************************************************/
    @Override
    protected void initialize() {
        double tolerance = 0.0000;
        nodeToleranceSpinner_.setValue(tolerance);
        
        connectionIdentifierSpinner_.setValue(1);
        
        connectionDisplacementXCheckBox_.setSelected(true);
        connectionDisplacementYCheckBox_.setSelected(true);
        connectionDisplacementZCheckBox_.setSelected(true);
        
        connectionRotationXCheckBox_.setSelected(true);
        connectionRotationYCheckBox_.setSelected(true);
        connectionRotationZCheckBox_.setSelected(true);
    } // eom
    
    
    
    /***************************************************************************
     * Installs and lays out all components on the container.
     **************************************************************************/
    @Override
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
        gridBagConstraints.gridwidth    = 7;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(0, 0, 0, 0);
        connectionPanel_.add(firstMeshPanel_, gridBagConstraints);
        
        lastMeshPanel_                  = connectionPanel_.getLastMesh();
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 7;
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
        gridBagConstraints.gridwidth    = 6;
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
        gridBagConstraints.gridwidth    = 3;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionInputTextField_, gridBagConstraints);
        
        connectionOpenButton_           = new JButton("Open...");
        gridBagConstraints.gridx        = 5;
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
        gridBagConstraints.gridwidth    = 3;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(nodeToleranceSpinner_, gridBagConstraints);
        
        connectionMakeButton_           = new JButton("Connect");
        gridBagConstraints.gridx        = 5;
        gridBagConstraints.gridy        = 4;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        connectionPanel_.add(connectionMakeButton_, gridBagConstraints);
        
        connectionTypeLabel_      = new JLabel("Type:");
        connectionTypeLabel_.setLabelFor(connectionTypeComboBox_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionTypeLabel_, gridBagConstraints);
        
        connectionTypeComboBox_         = new JComboBox();
        connectionTypeComboBox_.setModel(
                connectionPanel_.getConnectionTypeModel());
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 5;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 3;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionTypeComboBox_, gridBagConstraints);
        
        connectionIdentifierLabel_      = new JLabel("Start ID:");
        connectionIdentifierLabel_.setLabelFor(connectionIdentifierSpinner_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 6;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionIdentifierLabel_, gridBagConstraints);
        
        SpinnerModel connectionIdentifierModel;
        connectionIdentifierModel = new SpinnerNumberModel(0, 0, null, 1);
        
        connectionIdentifierSpinner_    = new JSpinner();
        connectionIdentifierSpinner_.setModel(connectionIdentifierModel);
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 6;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 3;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionIdentifierSpinner_, gridBagConstraints);
        
        connectionDisplacementLabel_    = new JLabel("Displacements:");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 7;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionDisplacementLabel_, gridBagConstraints);
        
        connectionDisplacementXCheckBox_    = new JCheckBox("X");
        gridBagConstraints.gridx            = 2;
        gridBagConstraints.gridy            = 7;
        gridBagConstraints.gridheight       = 1;
        gridBagConstraints.gridwidth        = 1;
        gridBagConstraints.weightx          = 0;
        gridBagConstraints.weighty          = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(
                connectionDisplacementXCheckBox_, gridBagConstraints);
        
        connectionDisplacementYCheckBox_    = new JCheckBox("Y");
        gridBagConstraints.gridx            = 3;
        gridBagConstraints.gridy            = 7;
        gridBagConstraints.gridheight       = 1;
        gridBagConstraints.gridwidth        = 1;
        gridBagConstraints.weightx          = 0;
        gridBagConstraints.weighty          = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(
                connectionDisplacementYCheckBox_, gridBagConstraints);
        
        connectionDisplacementZCheckBox_    = new JCheckBox("Z");
        gridBagConstraints.gridx            = 4;
        gridBagConstraints.gridy            = 7;
        gridBagConstraints.gridheight       = 1;
        gridBagConstraints.gridwidth        = 1;
        gridBagConstraints.weightx          = 0;
        gridBagConstraints.weighty          = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(
                connectionDisplacementZCheckBox_, gridBagConstraints);
        
        connectionRotationLabel_        = new JLabel("Rotations:");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 8;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionRotationLabel_, gridBagConstraints);
        
        connectionRotationXCheckBox_    = new JCheckBox("X");
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 8;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionRotationXCheckBox_, gridBagConstraints);
        
        connectionRotationYCheckBox_    = new JCheckBox("Y");
        gridBagConstraints.gridx        = 3;
        gridBagConstraints.gridy        = 8;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionRotationYCheckBox_, gridBagConstraints);
        
        connectionRotationZCheckBox_    = new JCheckBox("Z");
        gridBagConstraints.gridx        = 4;
        gridBagConstraints.gridy        = 8;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionRotationZCheckBox_, gridBagConstraints);
        
        connectionIndependentLabel_     = new JLabel("Independent Mesh:");
        connectionIndependentLabel_.setLabelFor(connectionIndependentComboBox_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 9;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionIndependentLabel_, gridBagConstraints);
        
        connectionIndependentComboBox_  = new JComboBox();
        connectionIndependentComboBox_.setModel(
                connectionPanel_.getIndependentMeshModel());
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 9;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 3;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(
                connectionIndependentComboBox_, gridBagConstraints);
        
        connectionOutputLabel_          = new JLabel("TXT Output File:");
        connectionOutputLabel_.setLabelFor(connectionOutputTextField_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 10;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionOutputLabel_, gridBagConstraints);
        
        connectionOutputTextField_      = new JTextField(40);
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 10;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 3;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 0, 0);
        connectionPanel_.add(connectionOutputTextField_, gridBagConstraints);
        
        connectionSaveButton_           = new JButton("Save...");
        gridBagConstraints.gridx        = 5;
        gridBagConstraints.gridy        = 10;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 0, 12);
        connectionPanel_.add(connectionSaveButton_, gridBagConstraints);
        
        colorLabel_                     = new JLabel("Color:");
        colorLabel_.setLabelFor(colorComboBox_);
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 11;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 12, 0);
        connectionPanel_.add(colorLabel_, gridBagConstraints);
        
        colorComboBox_                  = new JComboBox();
        colorComboBox_.setModel(connectionPanel_.getConnectionColorModel());
        gridBagConstraints.gridx        = 2;
        gridBagConstraints.gridy        = 11;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 3;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 12, 0);
        connectionPanel_.add(colorComboBox_, gridBagConstraints);
        
        connectionShowButton_           = new JButton("Display");
        gridBagConstraints.gridx        = 5;
        gridBagConstraints.gridy        = 11;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 5, 12, 12);
        connectionPanel_.add(connectionShowButton_, gridBagConstraints);
        
        bottomFillerLabel_              = new JLabel();
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 12;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 1;
        gridBagConstraints.insets.set(0, 0, 0, 0);
        connectionPanel_.add(bottomFillerLabel_, gridBagConstraints);
    } // eom
    
    
    
    /***************************************************************************
     * Installs all listeners for all components in this container.
     **************************************************************************/
    @Override
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
        
        connectionIdentifierChangeListener_ =
                new ConnectionIdentifierChangeListener(connectionPanel_, this);
        connectionIdentifierSpinner_.addChangeListener(
                connectionIdentifierChangeListener_);
        
        connectionDOFItemListener_ = new ConnectionDOFItemListener(
                connectionPanel_, this);
        connectionDisplacementXCheckBox_.addItemListener(
                connectionDOFItemListener_);
        connectionDisplacementYCheckBox_.addItemListener(
                connectionDOFItemListener_);
        connectionDisplacementZCheckBox_.addItemListener(
                connectionDOFItemListener_);
        connectionRotationXCheckBox_.addItemListener(
                connectionDOFItemListener_);
        connectionRotationYCheckBox_.addItemListener(
                connectionDOFItemListener_);
        connectionRotationZCheckBox_.addItemListener(
                connectionDOFItemListener_);
        
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
     * Uninstalls all components on the container.
     **************************************************************************/
    @Override
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
        
        connectionPanel_.remove(connectionTypeLabel_);
        connectionTypeLabel_ = null;
        
        connectionPanel_.remove(connectionTypeComboBox_);
        connectionTypeComboBox_ = null;
        
        connectionPanel_.remove(connectionIdentifierLabel_);
        connectionIdentifierLabel_ = null;
        
        connectionPanel_.remove(connectionIdentifierSpinner_);
        connectionIdentifierSpinner_ = null;
        
        connectionPanel_.remove(connectionDisplacementLabel_);
        connectionDisplacementLabel_ = null;
        
        connectionPanel_.remove(connectionDisplacementXCheckBox_);
        connectionDisplacementXCheckBox_ = null;
        
        connectionPanel_.remove(connectionDisplacementYCheckBox_);
        connectionDisplacementYCheckBox_ = null;
        
        connectionPanel_.remove(connectionDisplacementZCheckBox_);
        connectionDisplacementZCheckBox_ = null;
        
        connectionPanel_.remove(connectionRotationLabel_);
        connectionRotationLabel_ = null;
        
        connectionPanel_.remove(connectionRotationXCheckBox_);
        connectionRotationXCheckBox_ = null;
        
        connectionPanel_.remove(connectionRotationYCheckBox_);
        connectionRotationYCheckBox_ = null;
        
        connectionPanel_.remove(connectionRotationZCheckBox_);
        connectionRotationZCheckBox_ = null;
        
        connectionPanel_.remove(connectionIndependentLabel_);
        connectionIndependentLabel_ = null;
        
        connectionPanel_.remove(connectionIndependentComboBox_);
        connectionIndependentComboBox_ = null;
        
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
     * Uninstalls all listeners for all components in this container.
     **************************************************************************/
    @Override
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
        
        connectionIdentifierSpinner_.removeChangeListener(
                connectionIdentifierChangeListener_);
        connectionIdentifierChangeListener_ = null;
        
        connectionDisplacementXCheckBox_.removeItemListener(
                connectionDOFItemListener_);
        connectionDisplacementYCheckBox_.removeItemListener(
                connectionDOFItemListener_);
        connectionDisplacementZCheckBox_.removeItemListener(
                connectionDOFItemListener_);
        connectionRotationXCheckBox_.removeItemListener(
                connectionDOFItemListener_);
        connectionRotationYCheckBox_.removeItemListener(
                connectionDOFItemListener_);
        connectionRotationZCheckBox_.removeItemListener(
                connectionDOFItemListener_);
        connectionDOFItemListener_ = null;
        
        connectionOutputTextField_.removeActionListener(connectionSaveAction_);
        connectionSaveAction_ = null;
        
        connectionSaveButton_.removeActionListener(connectionSaveAsAction_);
        connectionSaveAsAction_ = null;
        
        colorComboBox_.removeItemListener(colorItemListener_);
        colorItemListener_ = null;
        
        connectionShowButton_.removeActionListener(connectionShowAction_);
        connectionShowAction_ = null;
    } // eom
} // eoc