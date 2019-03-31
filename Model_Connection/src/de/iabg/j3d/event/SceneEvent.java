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

import java.util.EventObject;

/*******************************************************************************
 * {@code SceneEvent} is used to notify interested parties that the scene has
 * changed in the event source.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class SceneEvent extends EventObject {
    
    
    
    /***************************************************************************
     * Constructs a {@code SceneEvent} object.
     * 
     * @param   source  the source of the event (typically {@code this}),
     *                  however, it may be another source if scene sources are
     *                  aggregated
     **************************************************************************/
    public SceneEvent(Object source) {
        super(source);
    } // eom
} // eoc