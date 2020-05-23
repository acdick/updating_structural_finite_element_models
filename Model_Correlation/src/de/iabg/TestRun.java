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
package de.iabg;

import de.iabg.mode.JModeCorrelationFrame;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/*******************************************************************************
 * This class contains the main method of this Master's Thesis project and
 * initializes and instance of the program.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 11, 2008
 ******************************************************************************/
public class TestRun {
    
    
    
    /***************************************************************************
     * Sets the Look and Feel for the local operating system.  The object to
     * perform a model correlation created for the user.
     **************************************************************************/
    public TestRun() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            new JModeCorrelationFrame("Model Correlation");
        }
        catch (UnsupportedLookAndFeelException exception) {
            System.out.println(exception.getMessage());
        }
        catch (ClassNotFoundException exception) {
            System.out.println(exception.getMessage());
        }
        catch (InstantiationException exception) {
            System.out.println(exception.getMessage());
        }
        catch (IllegalAccessException exception) {
            System.out.println(exception.getMessage());
        }
    } // eom
    
    
    
    /***************************************************************************
     * The main method of this Master's Thesis project.  This method
     * instantiates this class and begins the program.
     * 
     * @param   args    the default arguments
     **************************************************************************/
    public static void main(String[] args) {
        new TestRun();
    } // eom
} // eoc