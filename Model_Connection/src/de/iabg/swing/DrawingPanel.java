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

import de.iabg.j3d.DrawingCanvas3D;
import de.iabg.j3d.Renderable;
import de.iabg.j3d.Renderer;

import de.iabg.swing.event.BackgroundAction;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import javax.vecmath.Color3f;

/*******************************************************************************
 * This implementation of {@link Renderer} merges Swing and Java3D into one
 * standard component.  This class does not deal directly with Java3D, but wraps
 * most of the methods from {@link DrawingCanvas3D}.  Therefore, this component
 * can focus on the Swing side of this integrated {@link JPanel} and simply add
 * the {@code DrawingCanvas3D} as a component like any other.  This allows the
 * user to simply work with this {@code DrawingPanel} since it completely
 * wraps {@code DrawingCanvas3D}.
 * 
 * This {@code DrawingPanel} itself has another capability aside from those
 * declared in {@code Renderer}.  It has the possibility to change its
 * background color determined by a listener and if all {@link Renderable}
 * scenes correctly render the background color.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class DrawingPanel extends JPanel
        implements Renderer {
    /** An action that listens for background events */
    protected Action backgroundAction_;
    
    /** A button to change the background color */
    protected JButton backgroundButton_;
    
    /** A {@code Renderer} to display the Java3D scene */
    protected DrawingCanvas3D drawingCanvas3D_;
    
    
    
    /***************************************************************************
     * Constructs a {@code DrawingPanel} and installs the user interface.
     **************************************************************************/
    public DrawingPanel() {
        super();
        
        this.installUI();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void addRenderable(Renderable renderable) {
        drawingCanvas3D_.addRenderable(renderable);
        renderable.fireScene3DChanged(renderable);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public Color3f getBackgroundColor() {
        return drawingCanvas3D_.getBackgroundColor();
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
        JSeparator          separator;
        
        gridBagConstraints          = new GridBagConstraints();
        gridBagConstraints.fill     = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor   = GridBagConstraints.CENTER;
        
        label                           = new JLabel("Visualization");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 0;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 12, 0, 0);
        this.add(label, gridBagConstraints);
        
        separator                       = new JSeparator();
        gridBagConstraints.gridx        = 1;
        gridBagConstraints.gridy        = 0;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 1;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(12, 5, 0, 12);
        this.add(separator, gridBagConstraints);
        
        drawingCanvas3D_                = new DrawingCanvas3D();
        gridBagConstraints.fill         = GridBagConstraints.BOTH;
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 1;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 1;
        gridBagConstraints.weighty      = 1;
        gridBagConstraints.insets.set(5, 12, 0, 12);
        this.add(drawingCanvas3D_, gridBagConstraints);
        
        backgroundButton_               = new JButton("Toggle background");
        gridBagConstraints.gridx        = 0;
        gridBagConstraints.gridy        = 2;
        gridBagConstraints.gridheight   = 1;
        gridBagConstraints.gridwidth    = 2;
        gridBagConstraints.weightx      = 0;
        gridBagConstraints.weighty      = 0;
        gridBagConstraints.insets.set(5, 12, 12, 12);
        this.add(backgroundButton_, gridBagConstraints);
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
        backgroundAction_ = new BackgroundAction(this);
        backgroundButton_.setAction(backgroundAction_);
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
    public void removeRenderable(Renderable renderable) {
        drawingCanvas3D_.removeRenderable(renderable);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void render2D(Renderable renderable) {
        drawingCanvas3D_.render2D(renderable);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void render3D(Renderable renderable) {
        drawingCanvas3D_.render3D(renderable);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setBackgroundColor(Color3f backgroundColor) {
        drawingCanvas3D_.setBackgroundColor(backgroundColor);
    } // eom
} // eoc