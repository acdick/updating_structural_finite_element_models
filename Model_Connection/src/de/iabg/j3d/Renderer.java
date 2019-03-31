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

import javax.vecmath.Color3f;

/*******************************************************************************
 * This interface declares basic functionality for an object that can render
 * scenes in Java3D.  The ability to add {@link Renderable} objects allows the
 * {@code Renderer} to update the scene based on scene events.  Although the
 * rendering will be displayed in 3D, there are instances where a 2D
 * representation of the object is useful.  Therefore, this interface declares
 * methods necessary to render a planar 2D scene in Java3D as well.
 * 
 * Typically, a {@code Renderer} will register listeners on a {@code Renderable}
 * object and listen for events.  The {@code Renderer} will then call the
 * appropriate method to get the scene of the fired source.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public interface Renderer {
    
    
    
    /***************************************************************************
     * Adds a {@link Renderable} object to this {@code Renderer}.
     * 
     * @param   renderable  the {@code Renderable} object to add
     **************************************************************************/
    public void addRenderable(Renderable renderable);
    
    
    
    /***************************************************************************
     * Returns the currently stored background color in this {@code Renderer}.
     * 
     * @return  the current background color
     **************************************************************************/
    public Color3f getBackgroundColor();
    
    
    
    /***************************************************************************
     * Removes a {@link Renderable} object from this {@code Renderer}.
     * 
     * @param   renderable  the {@code Renderable} object to remove
     **************************************************************************/
    public void removeRenderable(Renderable renderable);
    
    
    
    /***************************************************************************
     * Renders the 2D scene of the given {@link Renderable} object
     * 
     * @param   renderable  the object to be rendered
     **************************************************************************/
    public void render2D(Renderable renderable);
    
    
    
    /***************************************************************************
     * Renders the 3D scene of the given {@link Renderable} object
     * 
     * @param   renderable  the object to be rendered
     **************************************************************************/
    public void render3D(Renderable renderable);
    
    
    
    /***************************************************************************
     * Replaces the currently stored background color in this {@code Renderer}
     * with the given color.
     * 
     * @param   backgroundColor the new background color
     **************************************************************************/
    public void setBackgroundColor(Color3f backgroundColor);
} // eoi