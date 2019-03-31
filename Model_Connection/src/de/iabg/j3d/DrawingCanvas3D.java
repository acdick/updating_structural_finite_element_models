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
package de.iabg.j3d;

import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.behaviors.vp.ViewPlatformBehavior;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

import de.iabg.j3d.event.RendererSceneListener;
import de.iabg.j3d.event.SceneListener;

import java.awt.Dimension;

import java.util.ArrayList;

import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;

import javax.vecmath.Point3d;
import javax.vecmath.Color3f;

/*******************************************************************************
 * This implementation of a {@link Renderer} provides default functionality to
 * displace a Java3D scene.  Typically, The creation of just one Java3D scene
 * requires many classes on both the scene branch and view branch sides.  This
 * class creates a reusable object that acts as the entire view branch.  Java3D
 * already has a simplified class, the
 * {@link com.sun.j3d.utils.universe.SimpleUniverse}, however, it still requires
 * some basic functionality such as mouse behaviors and is not itself designed
 * to handle user-defined events.  This implementation attempts to further
 * simplify the {@code SimpleUniverse} and allow a Swing component to drive
 * which objects are rendered and when they are rendered.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class DrawingCanvas3D extends Canvas3D
        implements Renderer {
    /** The background color of the scene */
    protected Color3f backgroundColor_;
    
    /** The current scene source */
    protected Renderable currentRenderable_;
    
    /** The list of scene sources */
    protected ArrayList<Renderable> renderables_;
    
    /** The parent branch group of all scene branch groups */
    protected BranchGroup sceneBranchGroup_;
    
    /** The listener attached to every {@link Renderable} */
    protected SceneListener sceneListener_;
    
    /** The default Java3D view branch group */
    protected SimpleUniverse simpleUniverse_;
    
    /** The {@code ViewingPlatform} of the {@code SimpleUniverse} */
    protected ViewingPlatform viewingPlatform_;
    
    
    
    /***************************************************************************
     * Constructs a {@code DrawingCanvas3D} and instantiates the
     * {@code SimpleUniverse} and list of {@code Renderable} objects.  It also
     * sets the default background color to black before calling
     * {@link #installScene()}, which installs all the necessary components to
     * create a legitimate Java3D scene with modular content.
     **************************************************************************/
    public DrawingCanvas3D() {
        super(SimpleUniverse.getPreferredConfiguration());
        
        renderables_        = new ArrayList<Renderable>();
        simpleUniverse_     = new SimpleUniverse(this);
        backgroundColor_    = ColorConstants.BLACK_COLOR;
        
        this.installScene();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void addRenderable(Renderable renderable) {
        renderable.addSceneListener(sceneListener_);
        renderables_.add(renderable);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public Color3f getBackgroundColor() {
        return backgroundColor_;
    } // eom
    
    
    
    /***************************************************************************
     * This method does nothing since there are no initialization tasks.
     **************************************************************************/
    protected void initialize() {
        
    } // eom
    
    
    
    /***************************************************************************
     * Installs a standard orbital behavior that allows paning, rotating, and
     * zooming.
     **************************************************************************/
    protected void installBehaviors() {
        ViewPlatformBehavior    behavior;
        Bounds                  bounds;
        
        behavior    = new OrbitBehavior(this, OrbitBehavior.REVERSE_ALL);
        bounds      = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        
        behavior.setSchedulingBounds(bounds);
        viewingPlatform_.setViewPlatformBehavior(behavior);
    } // eom
    
    
    
    /***************************************************************************
     * Installs an empty scene branch group to create a live scene.  A reference
     * to this scene branch group is stored and its capabilities are set such
     * that children can be added or removed.
     **************************************************************************/
    protected void installContentBranchGraph() {
        sceneBranchGroup_ = new BranchGroup();
        sceneBranchGroup_.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        sceneBranchGroup_.setCapability(BranchGroup.ALLOW_CHILDREN_READ);
        sceneBranchGroup_.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        sceneBranchGroup_.compile();
        simpleUniverse_.addBranchGraph(sceneBranchGroup_);
    } // eom
    
    
    
    /***************************************************************************
     * Installs a preferred canvas size of 512 by 512 pixels.
     **************************************************************************/
    protected void installLayout() {
        this.setPreferredSize(new Dimension(512, 512));
    } // eom
    
    
    
    /***************************************************************************
     * Instantiates a {@link SceneListener}, which can later be attached to
     * {@link Renderable} objects.
     **************************************************************************/
    protected void installListeners() {
        sceneListener_ = new RendererSceneListener(this);
    } // eom
    
    
    
    /***************************************************************************
     * Installs all the necessary components of a Java3D scene.  The concept and
     * structure of the method calls are conceived from a typical Swing user
     * interface.
     **************************************************************************/
    protected void installScene() {
        this.installLayout();
        this.installViewBranchGraph();
        this.installContentBranchGraph();
        this.installBehaviors();
        this.installListeners();
        this.initialize();
    } // eom
    
    
    
    /***************************************************************************
     * Creates a reference to the {@code ViewingPlatform} of the
     * {@code SimpleUniverse} and sets a nominal view.
     **************************************************************************/
    protected void installViewBranchGraph() {
        viewingPlatform_ = simpleUniverse_.getViewingPlatform();
        viewingPlatform_.setNominalViewingTransform();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void removeRenderable(Renderable renderable) {
        renderable.removeSceneListener(sceneListener_);
        renderables_.remove(renderable);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void render2D(Renderable renderable) {
        currentRenderable_                  = renderable;
        BranchGroup         branchGroup;
        double              nominalBounds   = 1.0;
        double              scale           = 1.0;
        
        sceneBranchGroup_.removeAllChildren();
        
        scale = nominalBounds / renderable.getBoundingRadius2D();
        
        branchGroup = renderable.getScene2D(scale, backgroundColor_);
        branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
        branchGroup.setUserData(renderable);
        branchGroup.compile();
        sceneBranchGroup_.addChild(branchGroup);
        
        viewingPlatform_.setNominalViewingTransform();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void render3D(Renderable renderable) {
        currentRenderable_                  = renderable;
        BranchGroup         branchGroup;
        double              nominalBounds   = 1.0;
        double              scale           = 1.0;
        
        sceneBranchGroup_.removeAllChildren();
        
        scale = nominalBounds / renderable.getBoundingRadius3D();
        
        branchGroup = renderable.getScene3D(scale, backgroundColor_);
        branchGroup.setCapability(BranchGroup.ALLOW_DETACH);
        branchGroup.setUserData(renderable);
        branchGroup.compile();
        sceneBranchGroup_.addChild(branchGroup);
        
        viewingPlatform_.setNominalViewingTransform();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setBackgroundColor(Color3f backgroundColor) {
        backgroundColor_ = backgroundColor;
        
        if (currentRenderable_ != null) {
            this.render3D(currentRenderable_);
        }
    } // eom
} // eoc