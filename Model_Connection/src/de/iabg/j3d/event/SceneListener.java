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

import java.util.EventListener;

/*******************************************************************************
 * Defines an object which listens for {@code SceneEvents}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public interface SceneListener
        extends EventListener {
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener has changed its 2D scene.
     * 
     * @param   sceneEvent  the {@link SceneEvent} from the scene source
     **************************************************************************/
    public void scene2DChanged(SceneEvent sceneEvent);
    
    
    
    /***************************************************************************
     * Invoked when the target of this listener has changed its 3D scene.
     * 
     * @param   sceneEvent  the {@link SceneEvent} from the scene source
     **************************************************************************/
    public void scene3DChanged(SceneEvent sceneEvent);
} // eoi