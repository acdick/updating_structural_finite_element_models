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
package de.iabg.mesh;

import de.iabg.mesh.plaf.RigidMeshConnectionUI;

import de.iabg.swing.DrawingPanel;
import de.iabg.swing.HistoryPanel;
import de.iabg.swing.StatusPanel;

import java.awt.BorderLayout;

import javax.swing.JFrame;

/*******************************************************************************
 * A {@code JFrame} for a rigidly-connected {@link JMeshConnectionPanel}.  This
 * component adds a {@link HistoryPanel}, a {@link StatusPanel}, and a
 * {@link DrawingPanel} to itself and wires them together with the
 * {@code JMeshConnectionPanel}.  In this way, all of the properties of the
 * {@code JMeshConnectionPanel} are displayed.
 * 
 * @author  Adam C. Dick, BSE
 * @version November 1, 2008
 ******************************************************************************/
public class JMeshConnectionFrame extends JFrame {
    
    
    
    /***************************************************************************
     * Constructs a {@code JMeshConnectionFrame} and installs the user
     * interface.
     * 
     * @param   title   the title of this component
     **************************************************************************/
    public JMeshConnectionFrame(String title) {
        super(title.trim());
        
        this.installUI();
    } // eom
    
    
    
    /***************************************************************************
     * Initializes all fields or default settings after all components have been
     * added.
     **************************************************************************/
    protected void initialize() {
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    } // eom
    
    
    
    /***************************************************************************
     * Installs and lays out all components on the container.
     **************************************************************************/
    protected void installComponents() {
        JMeshConnectionPanel    connectionPanel;
        MeshConnectionModel     connectionModel;
        MeshConnectionUI        connectionUI;
        connectionPanel = new JMeshConnectionPanel(this.getName());
        connectionModel = new RigidMeshConnectionModel(connectionPanel);
        connectionUI    = new RigidMeshConnectionUI();
        connectionPanel.setConnectionModel(connectionModel);
        connectionPanel.setConnectionUI(connectionUI);
        this.getContentPane().add(connectionPanel, BorderLayout.CENTER);
        
        DrawingPanel drawingPanel = new DrawingPanel();
        drawingPanel.addRenderable(connectionPanel);
        this.getContentPane().add(drawingPanel, BorderLayout.LINE_END);
        
        HistoryPanel historyPanel = new HistoryPanel();
        historyPanel.addLoggable(connectionPanel);
        this.getContentPane().add(historyPanel, BorderLayout.PAGE_END);
        
        StatusPanel statusPanel = new StatusPanel();
        statusPanel.addLoggable(connectionPanel);
        this.getContentPane().add(statusPanel, BorderLayout.LINE_START);
        
        this.pack();
    } // eom
    
    
    
    /***************************************************************************
     * Installs all global default settings of the container.
     **************************************************************************/
    protected void installDefaults() {
        
    } // eom
    
    
    
    /***************************************************************************
     * Installs all keyboard actions and mnemonics.
     **************************************************************************/
    protected void installKeyboardActions() {
        
    } // eom
    
    
    
    /***************************************************************************
     * Installs the layout manager of this container.
     **************************************************************************/
    protected void installLayout() {
        this.getContentPane().setLayout(new BorderLayout());
    } // eom
    
    
    
    /***************************************************************************
     * Installs all listeners for all components in this container.
     **************************************************************************/
    protected void installListeners() {
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    } // eom
    
    
    
    /***************************************************************************
     * Installs layout, components, listeners, keyboard actions, defaults, and
     * initializes any values or fields.
     **************************************************************************/
    protected void installUI() {
        this.installDefaults();
        this.installLayout();
        this.installComponents();
        this.installListeners();
        this.installKeyboardActions();
        this.initialize();
    } // eom
} // eoc