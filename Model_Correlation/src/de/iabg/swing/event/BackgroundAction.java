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
package de.iabg.swing.event;

import de.iabg.j3d.ColorConstants;

import de.iabg.swing.DrawingPanel;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import javax.vecmath.Color3f;

/*******************************************************************************
 * This {@code Action} handles toggling the background color of a
 * {@link DrawingPanel} between black and white.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public class BackgroundAction extends AbstractAction {
    /** The {@code DrawingPanel} whose background will be toggled */
    protected DrawingPanel drawingPanel_;
    
    
    
    /***************************************************************************
     * Constructs an {@code Action} with a reference to the given
     * {@code DrawingPanel}.
     * 
     * @param   drawingPanel    the {@code DrawingPanel} whose background will
     *                          be toggled
     **************************************************************************/
    public BackgroundAction(DrawingPanel drawingPanel) {
        super("Toggle background");
        
        drawingPanel_ = drawingPanel;
        
        this.putValue(SHORT_DESCRIPTION, "Toggle black or white background");
    } // eom
    
    
    
    /***************************************************************************
     * Toggles the stored {@code DrawingPanel} background color from white to
     * black or black to white.
     * 
     * @param   actionEvent the {@code ActionEvent}
     **************************************************************************/
    public void actionPerformed(ActionEvent actionEvent) {
        Color3f backgroundColor = drawingPanel_.getBackgroundColor();
        
        if (backgroundColor.equals(ColorConstants.BLACK_COLOR)) {
            drawingPanel_.setBackgroundColor(ColorConstants.WHITE_COLOR);
        }
        else {
            drawingPanel_.setBackgroundColor(ColorConstants.BLACK_COLOR);
        }
    } // eom
} // eoc