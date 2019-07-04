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

import de.iabg.mode.plaf.ModalAssuranceCriterionUI;
import de.iabg.mode.plaf.OrthogonalityCheckUI;

import de.iabg.j3d.Renderable;

import de.iabg.j3d.event.RenderableSceneListener;
import de.iabg.j3d.event.SceneEvent;
import de.iabg.j3d.event.SceneListener;

import de.iabg.mesh.JMeshConnectionPanel;
import de.iabg.mesh.JMeshPanel;

import de.iabg.swing.Loggable;

import de.iabg.swing.event.LoggableLogListener;
import de.iabg.swing.event.LogEvent;
import de.iabg.swing.event.LogListener;

import java.awt.event.KeyEvent;

import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;

import javax.swing.JTabbedPane;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;

import javax.swing.event.EventListenerList;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;

/*******************************************************************************
 * This {@link javax.swing.JTabbedPane} assembles all the necessary
 * {@link javax.swing.JPanel} components to create a fully integrated mode
 * correlation component.  This includes a {@code MeshConnectionPanel}, a
 * {@code JModeMirrorPanel}, a {@code JModeCorrelationPanel} for a Modal
 * Assurance Criterion calculation, and a {@code JModeCorrelationPanel} for a
 * normalized Orthogonality Check calculation.  This is simply a convenience
 * class to instantiate all the components and provides no other additional
 * functionality.
 * 
 * This component is also {@link Renderable} and simply relays or chains any
 * {@code SceneEvents} from the {@code MeshConnectionPanel},
 * {@code JModeMirrorPanel}, Modal Assurance Criterion panel, and normalized
 * Orthogonality Check panel.
 * 
 * This component is also {@link Loggable} and simply relays or chains any
 * {@code LogEvents} from the {@code MeshConnectionPanel},
 * {@code JModeMirrorPanel}, Modal Assurance Criterion panel, and normalized
 * Orthogonality Check panel.
 * 
 * @author  Adam C. Dick, BSE
 * @version November 1, 2008
 ******************************************************************************/
public class JModeCorrelationTabbedPane extends JTabbedPane
        implements  Loggable,
                    Renderable {
    /** A list for event listeners */
    protected EventListenerList listenerList_;
    
    /** The listener that handles {@code LogEvents} */
    protected LogListener logListener_;
    
    /** The tab index for the {@code JMeshConnectionPanel} */
    protected static final int MESH_CONNECTION_INDEX = 0;
    
    /** The {@code JMeshConnectionPanel} */
    protected JMeshConnectionPanel meshConnectionPanel_;
    
    /** The tab index for the Modal Assurance Criterion panel */
    protected static final int MODAL_ASSURANCE_CRITERION_INDEX = 2;
    
    /** The {@code JModeCorrelationPanel} for the Modal Assurance Criterion */
    protected JModeCorrelationPanel modalAssuranceCriterionPanel_;
    
    /** The tab index for the {@code JModeMirrorPanel} */
    protected static final int MODE_CORRELATION_INDEX = 1;
    
    /** The {@code JModeMirrorPanel} */
    protected JModeMirrorPanel modeMirrorPanel_;
    
    /** The tab index for the normalized Orthogonality Check panel */
    protected static final int ORTHOGONALITY_CHECK_INDEX = 3;
    
    /** The {@code JModeCorrelationPanel} for the Orthogonality Check */
    protected JModeCorrelationPanel orthogonalityCheckPanel_;
    
    /** The listener that handles {@code SceneEvents} */
    protected SceneListener sceneListener_;
    
    
    
    /***************************************************************************
     * Constructs a default {@code JModeCorrelationTabbedPane} with the given
     * name and installs the user interface.
     * 
     * @param   name    the name of this component
     **************************************************************************/
    public JModeCorrelationTabbedPane(String name) {
        super();
        
        listenerList_ = new EventListenerList();
        
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
    public void addSceneListener(SceneListener listener) {
        listenerList_.add(SceneListener.class, listener);
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
     **************************************************************************/
    public void fireScene2DChanged(Object source) {
        SceneListener[] listeners;
        listeners = listenerList_.getListeners(SceneListener.class);
        
        for (SceneListener listener : listeners) {
            listener.scene2DChanged(new SceneEvent(source));
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void fireScene3DChanged(Object source) {
        SceneListener[] listeners;
        listeners = listenerList_.getListeners(SceneListener.class);
        
        for (SceneListener listener : listeners) {
            listener.scene3DChanged(new SceneEvent(source));
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getBoundingRadius2D() {
        double boundingRadius = 1.0;
        
        return boundingRadius;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getBoundingRadius3D() {
        double boundingRadius = 1.0;
        
        return boundingRadius;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene2D(double scale, Color3f backgroundColor) {
        Background  background;
        Bounds      bounds;
        BranchGroup branchGroup = new BranchGroup();
        
        bounds      = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        background  = new Background(backgroundColor);
        background.setApplicationBounds(bounds);
        branchGroup.addChild(background);
        
        return branchGroup;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene3D(double scale, Color3f backgroundColor) {
        Background  background;
        Bounds      bounds;
        BranchGroup branchGroup = new BranchGroup();
        
        bounds      = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        background  = new Background(backgroundColor);
        background.setApplicationBounds(bounds);
        branchGroup.addChild(background);
        
        return branchGroup;
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
        meshConnectionPanel_ = new JMeshConnectionPanel("Mesh Connection");
        this.addTab("Grids", meshConnectionPanel_);
        
        JModePanel firstModePanel   = new JModePanel("FE");
        JModePanel lastModePanel    = new JModePanel("Test");
        
        modeMirrorPanel_ = new JModeMirrorPanel("Solution Correlation",
                firstModePanel, lastModePanel);
        this.addTab("Modes", modeMirrorPanel_);
        
        ModeCorrelationModel correlationModel;
        correlationModel = new ModalAssuranceCriterionModel(modeMirrorPanel_,
                meshConnectionPanel_);
        
        ModeCorrelationUI correlationUI;
        correlationUI = new ModalAssuranceCriterionUI();
        
        modalAssuranceCriterionPanel_ = new JModeCorrelationPanel(
                "Modal Assurance Criterion", correlationModel, correlationUI);
        this.addTab("Modal Assurance Criterion", modalAssuranceCriterionPanel_);
        
        correlationModel = new OrthogonalityCheckModel(modeMirrorPanel_,
                meshConnectionPanel_);
        correlationUI = new OrthogonalityCheckUI();
        
        orthogonalityCheckPanel_ = new JModeCorrelationPanel(
                "Orthogonality Check", correlationModel, correlationUI);
        this.addTab("Orthogonality Check", orthogonalityCheckPanel_);
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
        this.setMnemonicAt(MESH_CONNECTION_INDEX, KeyEvent.VK_G);
        this.setToolTipTextAt(MESH_CONNECTION_INDEX, "Mesh correlation files");
        
        this.setMnemonicAt(MODE_CORRELATION_INDEX, KeyEvent.VK_M);
        this.setToolTipTextAt(MODE_CORRELATION_INDEX, "Mode files");
        
        this.setMnemonicAt(MODAL_ASSURANCE_CRITERION_INDEX, KeyEvent.VK_D);
        this.setToolTipTextAt(MODAL_ASSURANCE_CRITERION_INDEX,
                "Modal Assurance Criterion");
        
        this.setMnemonicAt(ORTHOGONALITY_CHECK_INDEX, KeyEvent.VK_O);
        this.setToolTipTextAt(ORTHOGONALITY_CHECK_INDEX,
                "Orthogonality Check");
    } // eom
    
    
    
    /***************************************************************************
     * Installs the layout manager of this container.
     **************************************************************************/
    protected void installLayout() {
        
    } // eom
    
    
    
    /***************************************************************************
     * Installs all listeners for all components in this container.
     **************************************************************************/
    protected void installListeners() {
        logListener_ = new LoggableLogListener(this);
        meshConnectionPanel_.addLogListener(logListener_);
        modeMirrorPanel_.addLogListener(logListener_);
        modalAssuranceCriterionPanel_.addLogListener(logListener_);
        orthogonalityCheckPanel_.addLogListener(logListener_);
        
        sceneListener_ = new RenderableSceneListener(this);
        meshConnectionPanel_.addSceneListener(sceneListener_);
        modalAssuranceCriterionPanel_.addSceneListener(sceneListener_);
        orthogonalityCheckPanel_.addSceneListener(sceneListener_);
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
     **************************************************************************/
    public void removeSceneListener(SceneListener listener) {
        listenerList_.remove(SceneListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     * Returns a formatted string containing the data of the
     * {@code JMeshConnectionPanel}, {@code JModeMirrorPanel}, the Modal
     * Assurance Criterion panel, and the normalized Orthogonality Check panel.
     * 
     * @return  the string representation of this {@code JModeCorrelationPanel}
     **************************************************************************/
    @Override
    public String toString() {
        JMeshPanel      firstMeshPanel  = meshConnectionPanel_.getFirstMesh();
        JMeshPanel      lastMeshPanel   = meshConnectionPanel_.getLastMesh();
        StringBuilder   result          = new StringBuilder();
        int             setSize         = 0;
        
        result.append(String.format("%16s%n", firstMeshPanel.getName()));
        result.append(String.format("%s", firstMeshPanel.toString()));
        
        result.append(String.format("%16s", "MATERIAL"));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Masses:"));
        result.append(String.format(" %7d", orthogonalityCheckPanel_.
                getMassMatrices().getSize()));
        result.append(String.format("%n"));
        
        if (orthogonalityCheckPanel_.getMassMatrices().getSelectedMatrix() !=
                null) {
            setSize = orthogonalityCheckPanel_.getMassMatrices().
                    getSelectedMatrix().getNodeCount();
        }
        
        result.append(String.format("%-9s", "MassSize:"));
        result.append(String.format(" %6d", setSize));
        result.append(String.format("%n"));
        
        result.append(String.format("%s", modeMirrorPanel_.getFirstMode().
                toString()));
        result.append(String.format("%n"));
        
        result.append(String.format("%16s%n", lastMeshPanel.getName()));
        result.append(String.format("%s", lastMeshPanel.toString()));
        result.append(String.format("%s", modeMirrorPanel_.getLastMode().
                toString()));
        result.append(String.format("%n"));
        
        result.append(String.format("%16s", "CONNECTIONS"));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Nodes:"));
        result.append(String.format(" %7d", meshConnectionPanel_.
                getNodeConnections().getConnectionCount()));
        result.append(String.format("%n"));
        result.append(String.format("%n"));
        
        result.append(String.format("%16s", "CORRELATIONS"));
        result.append(String.format("%n"));
        
        result.append(String.format("%16s", "MAC"));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Modes:"));
        result.append(String.format(" %3d%s%3d",
                modalAssuranceCriterionPanel_.getModeCorrelation().
                getFirstModeCount(), "x",
                modalAssuranceCriterionPanel_.getModeCorrelation().
                getLastModeCount()));
        result.append(String.format("%n"));
        
        result.append(String.format("%16s", "ORTHO"));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Modes:"));
        result.append(String.format(" %3d%s%3d",
                orthogonalityCheckPanel_.getModeCorrelation().
                getFirstModeCount(), "x",
                orthogonalityCheckPanel_.getModeCorrelation().
                getLastModeCount()));
        result.append(String.format("%n"));
        
        return result.toString();
    } // eom
} // eoc