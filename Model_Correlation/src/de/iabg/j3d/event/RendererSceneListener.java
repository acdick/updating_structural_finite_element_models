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
package de.iabg.j3d.event;

import de.iabg.j3d.Renderable;
import de.iabg.j3d.Renderer;

/*******************************************************************************
 * This implementation of a {@link SceneListener} links a {@link Renderer} with
 * a {@link Renderable} object.  The {@code Renderer} typically creates an
 * instance of this class and registers the instance onto one or more
 * {@code Renderable} objects.  The {@code Renderable} objects fire scene events
 * to this listener, which passes the source to the appropriate 2D or 3D method
 * of the {@code Renderer}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class RendererSceneListener
        implements SceneListener {
    /** The {@code Renderer} that handles scene events */
    protected Renderer renderer_;
    
    
    
    /***************************************************************************
     * Constructs a {@code SceneListener} with a reference to the given
     * {@code Renderer}.
     * 
     * @param   renderer    the object where the scene will be rendered
     **************************************************************************/
    public RendererSceneListener(Renderer renderer) {
        renderer_ = renderer;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void scene2DChanged(SceneEvent sceneEvent) {
        renderer_.render2D((Renderable) sceneEvent.getSource());
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void scene3DChanged(SceneEvent sceneEvent) {
        renderer_.render3D((Renderable) sceneEvent.getSource());
    } // eom
} // eoc