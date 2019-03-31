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

import de.iabg.j3d.event.SceneListener;

import javax.media.j3d.BranchGroup;

import javax.vecmath.Color3f;

/*******************************************************************************
 * This interface declares basic functionality for an object that can be
 * rendered in Java3D.  The use of listeners allows the {@link Renderer} to
 * update the scene based on scene events.  Although the rendering will be
 * displayed in 3D, there are instances where a 2D representation of the object
 * is useful.  Therefore, this interface declares methods necessary to render a
 * planar 2D scene in Java3D as well.
 * 
 * Typically, a {@code Renderer} will register listeners on a {@code Renderable}
 * object and listen for events.  The {@code Renderer} will then call the
 * appropriate method to get the scene of the fired source.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public interface Renderable {
    
    
    
    /***************************************************************************
     * Adds a {@link SceneListener} to this object.
     * 
     * @param   listener  the {@code SceneListener} to add
     **************************************************************************/
    public void addSceneListener(SceneListener listener);
    
    
    
    /***************************************************************************
     * Sends a {@code SceneEvent}, whose source is this object, to each
     * listener.  This method reports that a 2D scene, rather than 3D scene,
     * should be rendered.  There may be instances when multiple
     * {@code Renderable} objects are aggregated, so a reference of the given
     * source is also sent.
     * 
     * @param   source  the object to be rendered in 2D
     **************************************************************************/
    public void fireScene2DChanged(Object source);
    
    
    
    /***************************************************************************
     * Sends a {@code SceneEvent}, whose source is this object, to each
     * listener.  This method reports that a 3D scene, rather than 2D scene,
     * should be rendered.  There may be instances when multiple
     * {@code Renderable} objects are aggregated, so a reference of the given
     * source is also sent.
     * 
     * @param   source  the object to be rendered in 3D
     **************************************************************************/
    public void fireScene3DChanged(Object source);
    
    
    
    /***************************************************************************
     * Returns the 2D distance of the furthest point from the origin.  The 2D
     * distance is calculated from only the x and y coordinates.
     * 
     * @return  the 2D distance of the furthest point
     **************************************************************************/
    public double getBoundingRadius2D();
    
    
    
    /***************************************************************************
     * Returns the 3D distance of the furthest point from the origin.
     * 
     * @return  the 3D distance of the furthest point
     **************************************************************************/
    public double getBoundingRadius3D();
    
    
    
    /***************************************************************************
     * Creates a 2D rendering of a Java3D scene that this object represents at
     * the given scale and with the given background color.  If there is no
     * scene present, only the background should be created with the given
     * background color.
     * 
     * @param   scale           the scale of the scene
     * @param   backgroundColor the background color of the scene
     * @return  a branch group containing the background and the scene that this
     *          object represents
     **************************************************************************/
    public BranchGroup getScene2D(double scale, Color3f backgroundColor);
    
    
    
    /***************************************************************************
     * Creates a 3D rendering of a Java3D scene that this object represents at
     * the given scale and with the given background color.  If there is no
     * scene present, only the background should be created with the given
     * background color.
     * 
     * @param   scale           the scale of the scene
     * @param   backgroundColor the background color of the scene
     * @return  a branch group containing the background and the scene that this
     *          object represents
     **************************************************************************/
    public BranchGroup getScene3D(double scale, Color3f backgroundColor);
    
    
    
    /***************************************************************************
     * Removes a {@link SceneListener} from this object.
     * 
     * @param   listener  the {@code SceneListener} to remove
     **************************************************************************/
    public void removeSceneListener(SceneListener listener);
} // eoi