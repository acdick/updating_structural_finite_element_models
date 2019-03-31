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

/*******************************************************************************
 * This implementation of a {@link SceneListener} links one {@link Renderable}
 * object with another.  This is usually necessary when an object aggregates
 * {@code Renderable} objects and needs to be notified when each scene is
 * changed.  The notification of the scene change is chained through each
 * subsequent {@code Renderable} object until it reaches a {@code Renderer},
 * which finally renders the scene.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class RenderableSceneListener
        implements SceneListener {
    /** The {@code Renderable} object that is notified of scene events */
    protected Renderable renderable_;
    
    
    
    /***************************************************************************
     * Constructs a {@code SceneListener} with a reference to the given
     * {@code Renderable}.
     * 
     * @param   renderable  the object to be notified of a scene change
     **************************************************************************/
    public RenderableSceneListener(Renderable renderable) {
        renderable_ = renderable;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void scene2DChanged(SceneEvent sceneEvent) {
        renderable_.fireScene2DChanged(sceneEvent.getSource());
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void scene3DChanged(SceneEvent sceneEvent) {
        renderable_.fireScene3DChanged(sceneEvent.getSource());
    } // eom
} // eoc