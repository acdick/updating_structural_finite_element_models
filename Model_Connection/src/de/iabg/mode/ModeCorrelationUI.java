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
package de.iabg.mode;

import javax.swing.JOptionPane;

/*******************************************************************************
 * This interface declares basic functionality for the user interface of a
 * {@link JModeCorrelationPanel}.  This interface tries to follow the structure
 * described in Swing for a Model-View-Controller.  Generally, the
 * {@code JModeCorrelationPanel} is instantiated and then a
 * {@code ModeCorelationUI} is installed onto it.  Any implementing classes of
 * this interface must be able to add and remove their own layout, components,
 * listeners, keyboard actions, and defaults.  If the user interface is not
 * completely uninstalled when called, it can lead to unpredictable results in
 * the {@code JModeCorrelationPanel}.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 12, 2008
 ******************************************************************************/
public interface ModeCorrelationUI {
    /** A constant for an error message */
    public static final int ERROR_MESSAGE = JOptionPane.ERROR_MESSAGE;
    
    /** A constant for a warning message */
    public static final int WARNING_MESSAGE = JOptionPane.WARNING_MESSAGE;
    
    
    
    /***************************************************************************
     * Returns the mode connection output file name.
     * 
     * @return  the mode connection output file name
     **************************************************************************/
    public String getConnectionOutput();
    
    
    
    /***************************************************************************
     * Returns the mode correlation output file name.
     * 
     * @return  the mode correlation output file name
     **************************************************************************/
    public String getCorrelationOutput();
    
    
    
    /***************************************************************************
     * Returns the mass matrix input file name.
     * 
     * @return  the mass matrix input file name
     **************************************************************************/
    public String getMassInput();
    
    
    
    /***************************************************************************
     * Returns the current mode correlation lower tolerance.
     * 
     * @return  the current mode correlation lower tolerance
     **************************************************************************/
    public double getLowerTolerance();
    
    
    
    /***************************************************************************
     * Returns the current mode correlation upper tolerance.
     * 
     * @return  the current mode correlation upper tolerance
     **************************************************************************/
    public double getUpperTolerance();
    
    
    
    /***************************************************************************
     * Installs layout, components, listeners, keyboard actions, defaults, and
     * initializes any values or fields.
     * 
     * @param   correlationPanel    the {@link JModeCorrelationPanel} where the
     *                              user interface will be installed
     **************************************************************************/
    public void installUI(JModeCorrelationPanel correlationPanel);
    
    
    
    /***************************************************************************
     * Replaces the mode connection output file with the given one.
     * 
     * @param   fileName    the new mode connection output file name
     **************************************************************************/
    public void setConnectionOutput(String fileName);
    
    
    
    /***************************************************************************
     * Replaces the mode correlation output file with the given one.
     * 
     * @param   fileName    the new mode correlation output file name
     **************************************************************************/
    public void setCorrelationOutput(String fileName);
    
    
    
    /***************************************************************************
     * Replaces the mode correlation lower tolerance with the given one.
     * 
     * @param   lowerTolerance  the new mode correlation lower tolerance
     **************************************************************************/
    public void setLowerTolerance(double lowerTolerance);
    
    
    
    /***************************************************************************
     * Replaces the mass matrix input file with the given one.
     * 
     * @param   fileName    the new mass matrix input file name
     **************************************************************************/
    public void setMassInput(String fileName);
    
    
    
    /***************************************************************************
     * Replaces the mode correlation upper tolerance with the given one.
     * 
     * @param   upperTolerance  the new mode correlation upper tolerance
     **************************************************************************/
    public void setUpperTolerance(double upperTolerance);
    
    
    
    /***************************************************************************
     * Shows a file chooser dialog for the supported mode connection output
     * file types and returns the selected file.
     * 
     * @return  the selected mode connection output file
     **************************************************************************/
    public String showConnectionSaveDialog();
    
    
    
    /***************************************************************************
     * Shows a file chooser dialog for the supported mode correlation output
     * file types and returns the selected file.
     * 
     * @return  the selected mode correlation output file
     **************************************************************************/
    public String showCorrelationSaveDialog();
    
    
    
    /***************************************************************************
     * Shows a file chooser dialog for the supported mass matrix input file
     * types and returns the selected file.
     * 
     * @return  the selected mass matrix input file
     **************************************************************************/
    public String showMassOpenDialog();
    
    
    
    /***************************************************************************
     * Shows a message dialog for error or warning messages.
     * 
     * @param   message     the message of the error or warning
     * @param   title       the title of the error or warning
     * @param   messageType an {@code ERROR_MESSAGE} or {@code WARNING_MESSAGE}
     **************************************************************************/
    public void showMessageDialog(Object message, String title,
            int messageType);
    
    
    
    /***************************************************************************
     * Uninstalls layout, components, listeners, keyboard actions, and defaults.
     * 
     * @param   correlationPanel    the {@link JModeCorrelationPanel} where the
     *                              user interface will be uninstalled
     **************************************************************************/
    public void uninstallUI(JModeCorrelationPanel correlationPanel);
} // eoi