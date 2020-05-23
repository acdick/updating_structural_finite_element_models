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

import de.iabg.j3d.ColorConstants;

import de.iabg.mesh.io.RigidMeshConnectionWriter;

import de.iabg.swing.KeyList;

import java.io.File;
import java.io.IOException;

import javax.swing.ComboBoxModel;

/*******************************************************************************
 * This implementation of {@link MeshConnectionModel} extends a
 * {@code DefaultMeshConnectionModel} to create a rigid connection.
 * 
 * @author  Adam C. Dick, BSE
 * @version November 1, 2008
 ******************************************************************************/
public class RigidMeshConnectionModel extends DefaultMeshConnectionModel {
    /** The connected degrees of freedom */
    protected String connectionDOF_;
    
    /** The connection identifier */
    protected int connectionIdentifier_;
    
    /** A {@link javax.swing.ComboBoxModel} for the independent mesh */
    protected KeyList connectionIndependent_;
    
    /** A {@link javax.swing.ComboBoxModel} for the connection type */
    protected KeyList connectionType_;
    
    
    
    /***************************************************************************
     * Constructs a {@code RigidMeshConnectionModel} with no stored data.
     * 
     * @param   connectionPanel the {@code JMeshConnectionPanel} that this model
     *                          is designed for
     **************************************************************************/
    public RigidMeshConnectionModel(JMeshConnectionPanel connectionPanel) {
        super(connectionPanel);
        
        firstMeshPanel_         = new JMeshPanel("Model A");
        lastMeshPanel_          = new JMeshPanel("Model B");
        connectionIndependent_  = new KeyList();
        connectionType_         = new KeyList();
        
        firstMeshPanel_.getGeometryColorModel().setSelectedItem(
                ColorConstants.GREEN);
        lastMeshPanel_.getGeometryColorModel().setSelectedItem(
                ColorConstants.YELLOW);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public void exportNodeConnections(String fileName)
            throws IOException {
        MeshConnectionWriter    connectionWriter;
        long                    time;
        
        nodeConnectionFile_ = new File(fileName.trim());
        
        connectionPanel_.fireLogChanged("Creating source file: " + fileName);
        connectionWriter = new RigidMeshConnectionWriter(nodeConnectionFile_);
        connectionWriter.setFirstMeshFile(firstMeshPanel_.getGeometryFile());
        connectionWriter.setLastMeshFile(lastMeshPanel_.getGeometryFile());
        connectionWriter.setIndependentMesh(
                connectionIndependent_.getSelectedItem());
        connectionWriter.setConnectionDegreesOfFreedom(connectionDOF_);
        connectionWriter.setConnectionIdentifier(connectionIdentifier_);
        connectionWriter.setConnectionType(connectionType_.getSelectedItem());
        
        time = System.currentTimeMillis();
        connectionWriter.exportNodeConnections(nodeConnections_);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Wrote: " + nodeConnections_.getConnectionCount() +
                " node connections (total time: " + time + " seconds)");
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public ComboBoxModel getConnectionTypeModel() {
        connectionType_.clear();
        connectionType_.add("RBE2");
        connectionType_.setSelectedItem("RBE2");
        
        return connectionType_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public ComboBoxModel getIndependentMeshModel() {
        connectionIndependent_.clear();
        connectionIndependent_.add(connectionPanel_.getFirstMesh().getName());
        connectionIndependent_.add(connectionPanel_.getLastMesh().getName());
        connectionIndependent_.setSelectedItem(
                connectionPanel_.getFirstMesh().getName());
        
        return connectionIndependent_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public void setConnectionDegreesOfFreedom(String connectionDOF) {
        connectionDOF_ = connectionDOF;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    @Override
    public void setConnectionIdentifier(int connectionIdentifier) {
        connectionIdentifier_ = connectionIdentifier;
    } // eom
} // eoc