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

import de.iabg.mode.io.ExcelModeCorrelationWriter;
import de.iabg.mode.io.NastranDirectMatrixReader;
import de.iabg.mode.io.OrthogonalityCheckUpdateWriter;

import de.iabg.j3d.event.SceneEvent;
import de.iabg.j3d.event.SceneListener;

import de.iabg.mesh.JMeshConnectionPanel;
import de.iabg.mesh.JMeshPanel;
import de.iabg.mesh.NodeConnectionMatrix;

import de.iabg.swing.KeyList;

import de.iabg.swing.event.LogEvent;
import de.iabg.swing.event.LogListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Arrays;

import javax.media.j3d.BranchGroup;

import javax.swing.ComboBoxModel;
import javax.swing.JTable;

import javax.swing.event.EventListenerList;

import javax.swing.table.TableModel;

import javax.vecmath.Color3f;

/*******************************************************************************
 * This implementation of {@link ModeCorrelationModel} attempts to create all
 * the functionality described in the {@code ModeCorrelationModel} class
 * description. Refer to the class API for more details.
 * 
 * Much of the implementation of this class should be straight-forward, however,
 * a more in-depth summary of the connection algorithm may clarify some
 * conceptual issues.  There exist two {@link ModeMatrix} objects, one for the
 * {@code First Modes} and one for the {@code Last Modes}.  A
 * {@link ModeCorrelationMatrix} is created by calling
 * {@link ModeMatrix#getReducedOrthogonalityCheck(de.iabg.mode.MassMatrix,
 * de.iabg.mode.ModeMatrix, de.iabg.swing.KeyList, de.iabg.swing.KeyList,
 * de.iabg.swing.KeyList, de.iabg.swing.KeyList)} and using the selected mass
 * matrix,{@code First Modes}, {@code Last Modes}, and their corresponding mode
 * keys as arguments.  Once that has been completed, a
 * {@link ModeConnectionMatrix} with a minimum correlation tolerance is created
 * by calling {@link ModeCorrelationMatrix#getPreferredConnection(double)}.  In
 * these two steps, the two {@code ModeMatrix} objects are connected.  The user
 * can change the connection by simply changing the values in the {@code JTable}
 * provided. Changes in this table are backed by the
 * {@code ModeConnectionMatrix}, so no additional action must be taken.
 * 
 * To render the correlated modes in Java3D, instances of
 * {@link ModeCorrelationScene2D} and {@code ModeCorrelationScene3D} are used.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 14, 2008
 ******************************************************************************/
public class OrthogonalityCheckModel
        implements ModeCorrelationModel {
    /** The file of the currently stored mode connections */
    protected File connectionFile_;
    
    /** The file of the currently stored mode correlations */
    protected File correlationFile_;
    
    /** The {@code TableModel} of the currently stored mode correlations */
    protected ModeCorrelationTableModel correlationModel_;
    
    /** The {@code First Mode} panel */
    protected JModePanel firstModePanel_;
    
    /** The {@code Last Mode} panel */
    protected JModePanel lastModePanel_;
    
    /** A list for event listeners */
    protected EventListenerList listenerList_;
    
    /** The file of the currently stored mass matrices */
    protected File massFile_;
    
    /** Storage for the mass matrices */
    protected MassMatrixMap massMatrices_;
    
    /** The {@link JMeshConnectionPanel} */
    protected JMeshConnectionPanel meshConnectionPanel_;
    
    
    
    /***************************************************************************
     * Constructs a {@code OrthogonalityCheckModel} with no stored data. This
     * constructor also initializes all the storage objects, which should not be
     * destroyed during the life of this object.  This is because listeners may
     * register themselves to these objects, and will not receive notifications
     * if the references to these objects are changed.
     * 
     * @param   modeMirrorPanel     the {@code JModeMirrorPanel}
     * @param   meshConnectionPanel the {@code JMeshConnectionPanel}
     **************************************************************************/
    public OrthogonalityCheckModel(JModeMirrorPanel modeMirrorPanel,
            JMeshConnectionPanel meshConnectionPanel) {
        firstModePanel_         = modeMirrorPanel.getFirstMode();
        lastModePanel_          = modeMirrorPanel.getLastMode();
        meshConnectionPanel_    = meshConnectionPanel;
        listenerList_           = new EventListenerList();
        correlationModel_       = new ModeCorrelationTableModel();
        massMatrices_           = new MassMatrixMap();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void addLogListener(LogListener listener) {
        listenerList_.add(LogListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void addSceneListener(SceneListener listener) {
        listenerList_.add(SceneListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void correlateModes() {
        ModeMatrix              firstModes;
        KeyList                 firstModeKeys;
        KeyList                 firstNodeKeys;
        ModeMatrix              lastModes;
        KeyList                 lastModeKeys;
        KeyList                 lastNodeKeys;
        KeyList                 massNodeKeys;
        NodeConnectionMatrix    nodeConnection;
        ModeCorrelationMatrix   modeCorrelation;
        long                    time;
        
        massNodeKeys    = new KeyList();
        massNodeKeys.addAll(Arrays.asList(
                massMatrices_.getSelectedMatrix().getNodeNames()));
        nodeConnection  = new NodeConnectionMatrix(
                meshConnectionPanel_.getNodeConnections());
        nodeConnection.sortFirstNodes(massNodeKeys);
        firstModes      = firstModePanel_.getModes();
        firstModeKeys   = firstModePanel_.getSelectedModeKeys();
        firstNodeKeys   = nodeConnection.getFirstNodeKeys();
        lastModes       = lastModePanel_.getModes();
        lastModeKeys    = lastModePanel_.getSelectedModeKeys();
        lastNodeKeys    = nodeConnection.getLastNodeKeys();
        
        this.fireLogChanged("Correlating modes:");
        time = System.currentTimeMillis();
        modeCorrelation = firstModes.getReducedOrthogonalityCheck(
                massMatrices_.getSelectedMatrix(), lastModes, firstModeKeys,
                firstNodeKeys, lastModeKeys, lastNodeKeys);
        correlationModel_.setModeCorrelation(modeCorrelation);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Computed: Orthogonality Check (total time: " +
                time + " seconds)");
        this.fireScene3DChanged(this);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void deselectAllFirstModeKeys() {
        correlationModel_.deselectAllFirstModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void exportModeConnection(String fileName)
            throws IOException {
        ModeConnectionWriter    connectionWriter;
        KeyList                 massNodeKeys;
        NodeConnectionMatrix    nodeConnection;
        long                    time;
        
        massNodeKeys    = new KeyList();
        massNodeKeys.addAll(Arrays.asList(
                massMatrices_.getSelectedMatrix().getNodeNames()));
        nodeConnection  = new NodeConnectionMatrix(
                meshConnectionPanel_.getNodeConnections());
        nodeConnection.sortFirstNodes(massNodeKeys);
        connectionFile_ = new File(fileName.trim());
        
        this.fireLogChanged("Creating source file: " + fileName);
        time = System.currentTimeMillis();
        connectionWriter = new OrthogonalityCheckUpdateWriter(connectionFile_);
        connectionWriter.setCorrelationWeights(
                correlationModel_.getModeWeightings());
        connectionWriter.setEquationIdentifier(100000);
        connectionWriter.setFirstModeFile(firstModePanel_.getModeFile());
        connectionWriter.setFirstMeshFile(
                meshConnectionPanel_.getFirstMesh().getGeometryFile());
        connectionWriter.setFirstModes(firstModePanel_.getModes());
        connectionWriter.setFrequencyWeights(
                correlationModel_.getFrequencyWeightings());
        connectionWriter.setLastModeFile(lastModePanel_.getModeFile());
        connectionWriter.setLastMeshFile(
                meshConnectionPanel_.getLastMesh().getGeometryFile());
        connectionWriter.setLastModes(lastModePanel_.getModes());
        connectionWriter.setMasses(massMatrices_.getSelectedMatrix());
        connectionWriter.setMassFile(massFile_);
        connectionWriter.exportModeConnections(
                correlationModel_.getModeConnection(), nodeConnection);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged(
                "Wrote: Orthogonality Check optimization file " +
                "(total time: " + time + " seconds)");
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void exportModeCorrelation(String fileName)
            throws IOException {
        correlationFile_                            = new File(fileName.trim());
        ModeCorrelationWriter   correlationWriter;
        JMeshPanel              firstMeshPanel;
        JMeshPanel              lastMeshPanel;
        long                    time;
        
        firstMeshPanel  = meshConnectionPanel_.getFirstMesh();
        lastMeshPanel   = meshConnectionPanel_.getLastMesh();
        
        this.fireLogChanged("Creating source file: " + fileName);
        time = System.currentTimeMillis();
        correlationWriter = new ExcelModeCorrelationWriter(correlationFile_);
        correlationWriter.setFirstModeFile(firstModePanel_.getModeFile());
        correlationWriter.setFirstMeshFile(firstMeshPanel.getGeometryFile());
        correlationWriter.setFirstName(firstMeshPanel.getName());
        correlationWriter.setLastModeFile(lastModePanel_.getModeFile());
        correlationWriter.setLastMeshFile(lastMeshPanel.getGeometryFile());
        correlationWriter.setLastName(lastMeshPanel.getName());
        correlationWriter.exportModeCorrelations(this.getModeCorrelation());
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Wrote: Orthogonality Check (total time: " + time +
                " seconds)");
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void fireLogChanged(String log) {
        LogListener[] listeners;
        listeners = listenerList_.getListeners(LogListener.class);
        
        for (LogListener listener : listeners) {
            listener.logChanged(new LogEvent(this, log));
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void fireScene2DChanged(Object source) {
        SceneListener[] listeners;
        listeners = listenerList_.getListeners(SceneListener.class);
        
        for (SceneListener listener : listeners) {
            listener.scene2DChanged(new SceneEvent(source));
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void fireScene3DChanged(Object source) {
        SceneListener[] listeners;
        listeners = listenerList_.getListeners(SceneListener.class);
        
        for (SceneListener listener : listeners) {
            listener.scene3DChanged(new SceneEvent(source));
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getBoundingRadius2D() {
        return correlationModel_.getBoundingRadius2D();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public double getBoundingRadius3D() {
        return correlationModel_.getBoundingRadius3D();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public JModePanel getFirstMode() {
        return firstModePanel_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public KeyList getFirstSelectedModeKeys() {
        return correlationModel_.getFirstSelectedModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public JModePanel getLastMode() {
        return lastModePanel_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public ComboBoxModel getMassKeyModel() {
        return massMatrices_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public MassMatrixMap getMassMatrices() {
        return massMatrices_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public JMeshConnectionPanel getMeshConnection() {
        return meshConnectionPanel_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public ModeCorrelationMatrix getModeCorrelation() {
        return correlationModel_.getModeCorrelation();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public TableModel getModeCorrelationTableModel() {
        return correlationModel_;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene2D(double scale, Color3f backgroundColor) {
        JMeshPanel firstMeshPanel   = meshConnectionPanel_.getFirstMesh();
        JMeshPanel lastMeshPanel    = meshConnectionPanel_.getLastMesh();
        
        correlationModel_.setFirstName(firstMeshPanel.getName());
        correlationModel_.setLastName(lastMeshPanel.getName());
        
        return correlationModel_.getScene2D(scale, backgroundColor);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public BranchGroup getScene3D(double scale, Color3f backgroundColor) {
        JMeshPanel firstMeshPanel   = meshConnectionPanel_.getFirstMesh();
        JMeshPanel lastMeshPanel    = meshConnectionPanel_.getLastMesh();
        
        correlationModel_.setFirstName(firstMeshPanel.getName());
        correlationModel_.setLastName(lastMeshPanel.getName());
        
        return correlationModel_.getScene3D(scale, backgroundColor);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasMassKey() {
        boolean hasMassKey = false;
        
        if (massMatrices_.getSelectedMatrix() != null) {
            hasMassKey = true;
        }
        
        return hasMassKey;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasMassMatrices() {
        return !massMatrices_.isEmpty();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasModeKeys() {
        boolean hasModeKeys = false;
        
        if (firstModePanel_.hasModeKeys()) {
            if (lastModePanel_.hasModeKeys()) {
                hasModeKeys = true;
            }
        }
        
        return hasModeKeys;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean hasModes() {
        boolean hasModes = false;
        
        if (firstModePanel_.hasModes()) {
            if (lastModePanel_.hasModes()) {
                hasModes = true;
            }
        }
        
        return hasModes;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importMassMatrices(String fileName)
            throws  FileNotFoundException,
                    IOException {
        MaterialReader  materialReader;
        long            time;
        
        massFile_ = new File(fileName.trim());
        massMatrices_.clear();
        
        this.fireLogChanged("Opening source file: " + fileName);
        materialReader = new NastranDirectMatrixReader(massFile_);
        materialReader.readFile();
        
        time = System.currentTimeMillis();
        materialReader.importMassMatrices(massMatrices_);
        time = (System.currentTimeMillis() - time) / 1000L;
        this.fireLogChanged("Read: " + massMatrices_.size() +
                " mass matrices (total time: " + time + " seconds)");
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean isConsistent() {
        boolean     isConsistent    = false;
        KeyList     firstNodeKeys;
        KeyList     lastNodeKeys;
        String[]    firstNodeNames;
        String[]    lastNodeNames;
        String[]    massNodeNames;
        int         nComponents     = 3;
        
        firstNodeKeys   = meshConnectionPanel_.getNodeConnections().
                getFirstNodeKeys();
        lastNodeKeys    = meshConnectionPanel_.getNodeConnections().
                getLastNodeKeys();
        firstNodeNames  = firstModePanel_.getModes().getNodeNames();
        lastNodeNames   = lastModePanel_.getModes().getNodeNames();
        massNodeNames   = massMatrices_.getSelectedMatrix().getNodeNames();
        
        if (Arrays.asList(firstNodeNames).containsAll(firstNodeKeys.values())) {
            if (Arrays.asList(lastNodeNames).containsAll(
                    lastNodeKeys.values())) {
                if (firstNodeKeys.values().containsAll(
                        Arrays.asList(massNodeNames))) {
                    if (firstNodeKeys.size() ==
                            massNodeNames.length / nComponents) {
                        isConsistent = true;
                    }
                }
            }
        }
        
        return isConsistent;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean isModeConnected() {
        return correlationModel_.isConnected();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean isModeCorrelated() {
        return correlationModel_.isCorrelated();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public boolean isNodeConnected() {
        return meshConnectionPanel_.isConnected();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void removeLogListener(LogListener listener) {
        listenerList_.remove(LogListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void removeSceneListener(SceneListener listener) {
        listenerList_.remove(SceneListener.class, listener);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void selectAllFirstModeKeys() {
        correlationModel_.selectAllFirstModeKeys();
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setColumnProperties(JTable correlationTable) {
        correlationModel_.setColumnProperties(correlationTable);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setDefaultKeys() {
        correlationModel_.setDefaultKeys();
        this.fireLogChanged("Set: " + this.getFirstSelectedModeKeys().size() +
                " default mode keys");
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setLowerTolerance(double lowerTolerance) {
        correlationModel_.setLowerTolerance(lowerTolerance);
        this.fireScene3DChanged(this);
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void setUpperTolerance(double upperTolerance) {
        correlationModel_.setUpperTolerance(upperTolerance);
        this.fireScene3DChanged(this);
    } // eom
    
    
    
    /***************************************************************************
     * Returns a formatted string containing the data of the {@code First Mesh},
     * {@code Last Mesh}, {@code First Mode}, {@code Last Mode}, mass matrices,
     * the number of currently stored node connections, and the number of
     * currently stored mode correlations.
     * 
     * @return  the string representation of this {@code JModeCorrelationPanel}
     **************************************************************************/
    @Override
    public String toString() {
        JMeshPanel      firstMeshPanel  = meshConnectionPanel_.getFirstMesh();
        JMeshPanel      lastMeshPanel   = meshConnectionPanel_.getLastMesh();
        StringBuilder   result          = new StringBuilder();
        
        result.append(String.format("%s", firstMeshPanel.toString()));
        result.append(String.format("%n"));
        
        result.append(String.format("%-16s", "MATERIAL"));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Masses:"));
        result.append(String.format(" %7d", massMatrices_.getSize()));
        result.append(String.format("%n"));
        
        result.append(String.format("%s", firstModePanel_.toString()));
        result.append(String.format("%n"));
        
        result.append(String.format("%s", lastMeshPanel.toString()));
        result.append(String.format("%s", lastModePanel_.toString()));
        result.append(String.format("%n"));
        
        result.append(String.format("%-16s", "CONNECTIONS"));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Nodes:"));
        result.append(String.format(" %7d", meshConnectionPanel_.
                getNodeConnections().getConnectionCount()));
        result.append(String.format("%n"));
        result.append(String.format("%n"));
        
        result.append(String.format("%-16s", "CORRELATIONS"));
        result.append(String.format("%n"));
        
        result.append(String.format("%-16s", "ORTHO"));
        result.append(String.format("%n"));
        
        result.append(String.format("%-8s", "Modes:"));
        result.append(String.format(" %3d%s%3d",
                correlationModel_.getFirstModeCount(), "x",
                correlationModel_.getLastModeCount()));
        result.append(String.format("%n"));
        
        return result.toString();
    } // eom
} // eoc