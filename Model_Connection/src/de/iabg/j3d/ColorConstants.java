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
 * This interface provides some default colors with their common names.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 10, 2008
 ******************************************************************************/
public interface ColorConstants {
    /** A string for black */
    public static final String BLACK = "Black";
    
    /** A color for black */
    public static final Color3f BLACK_COLOR = new Color3f(0.0f, 0.0f, 0.0f);
    
    /** A string for blue */
    public static final String BLUE = "Blue";
    
    /** A color for blue */
    public static final Color3f BLUE_COLOR = new Color3f(0.0f, 0.0f, 1.0f);
    
    /** A string for cyan */
    public static final String CYAN = "Cyan";
    
    /** A color for cyan */
    public static final Color3f CYAN_COLOR = new Color3f(0.0f, 1.0f, 1.0f);
    
    /** A string for green */
    public static final String GREEN = "Green";
    
    /** A color for green */
    public static final Color3f GREEN_COLOR = new Color3f(0.0f, 1.0f, 0.0f);
    
    /** A string for magenta */
    public static final String MAGENTA = "Magenta";
    
    /** A color for magenta */
    public static final Color3f MAGENTA_COLOR = new Color3f(1.0f, 0.0f, 1.0f);
    
    /** A string for red */
    public static final String RED = "Red";
    
    /** A color for red */
    public static final Color3f RED_COLOR = new Color3f(1.0f, 0.0f, 0.0f);
    
    /** A string for white */
    public static final String WHITE = "White";
    
    /** A color for white */
    public static final Color3f WHITE_COLOR = new Color3f(1.0f, 1.0f, 1.0f);
    
    /** A string for yellow */
    public static final String YELLOW = "Yellow";
    
    /** A color for yellow */
    public static final Color3f YELLOW_COLOR = new Color3f(1.0f, 1.0f, 0.0f);
} // eoi