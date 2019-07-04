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
package de.iabg.mesh.io;

import de.iabg.mesh.KeyReader;

import de.iabg.swing.KeyList;
import de.iabg.swing.KeyMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

/*******************************************************************************
 * This implementation of {@link KeyReader} parses a Nastran file containing
 * node subset data.  Refer to {@code MeshConnectionReader} class API for more
 * details.
 * 
 * The Nastran file can contain node subset data pertaining to nodes or to
 * elements.  This reader only parses data from nodes.  Refer to the latest
 * version of the MSC.Nastran Quick Reference Guide for more information for
 * the file format.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 26, 2008
 ******************************************************************************/
public class NastranSetReader extends FileReader
        implements KeyReader {
    /** A list for the data from the file */
    protected ArrayList<String> data_;
    
    /** A constant for elements */
    protected static final int ELEMENT_FORMAT = 1000;
    
    /** A constant for nodes */
    protected static final int NODE_FORMAT = 1001;
    
    /** Storage for the groups of node subsets */
    protected KeyMap nodeKeyLists_;
    
    /** Storage for the node subsets */
    protected KeyList nodeKeys_;
    
    /** A constant for sets */
    protected static final int SET_FORMAT = 1002;
    
    /** A constant for undefined lines */
    protected static final int UNDEFINED_FORMAT = 1099;
    
    
    
    /***************************************************************************
     * Constructs a {@code NastranSetReader} from the given file.
     * 
     * @param   file                            the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     **************************************************************************/
    public NastranSetReader(File file)
            throws FileNotFoundException {
        super(file);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a node key from the given key and adds it to the stored node keys.
     * The key may be a single key or a range.  If it is a range, the entire
     * range is added to the node keys. 
     * 
     * @param   key the key to be parsed
     **************************************************************************/
    protected void addNodeKey(String key) {
        String  firstString;
        String  lastString;
        Integer firstKey;
        Integer lastKey;
        
        if (key.toUpperCase().contains("THRU")) {
            firstString = key.substring(0, key.toUpperCase().indexOf("T"));
            lastString  = key.substring(key.toUpperCase().indexOf("U") + 1);
            
            firstKey    = Integer.parseInt(firstString.trim());
            lastKey     = Integer.parseInt(lastString.trim());
            while (firstKey <= lastKey) {
                nodeKeys_.add(firstKey.toString());
                firstKey++;
            }
        }
        else {
            nodeKeys_.add(key.trim());
        }
    } // eom
    
    
    
    /***************************************************************************
     * Parses all node keys from the given line and adds them to the given node
     * key map, using the given name as the map key.
     * 
     * @param   line    the line to be parsed
     * @param   name    the name of the group
     **************************************************************************/
    protected void addNodeKeys(String line, String name) {
        nodeKeys_               = new KeyList();
        Scanner     scanner;
        String      key;
        
        line    = line.substring(line.indexOf(" ") + 1).trim();
        scanner = new Scanner(line);
        scanner.useDelimiter("=");
        
        if (name == null) {
            name = scanner.next().trim();
        }
        else {
            name = scanner.next().trim() + "_" + name.trim();
        }
        
        line    = line.substring(line.indexOf("=") + 1).trim();
        scanner = new Scanner(line);
        scanner.useDelimiter(",");
        
        while (scanner.hasNext()) {
            key = scanner.next().trim();
            this.addNodeKey(key);
        }
        
        nodeKeyLists_.put(name, nodeKeys_);
    } // eom
    
    
    
    /***************************************************************************
     * Returns the format of the given line.
     * 
     * @param   line    the line to be parsed
     * @return  the line format
     **************************************************************************/
    protected int getFormat(String line) {
        if (line.toUpperCase().startsWith("$ ELEMENTS")) {
            return ELEMENT_FORMAT;
        }
        else if (line.toUpperCase().startsWith("$ NODES")) {
            return NODE_FORMAT;
        }
        else if (line.toUpperCase().startsWith("SET")) {
            return SET_FORMAT;
        }
        else {
            return UNDEFINED_FORMAT;
        }
    } // eom
    
    
    
    /***************************************************************************
     * Parses and returns the node key list name from the given line.
     * 
     * @param   line    the line to be parsed
     * @return  the name of the node key list
     **************************************************************************/
    protected String getNodeKeyListName(String line) {
        Scanner scanner;
        String  name;
        
        scanner = new Scanner(line);
        scanner.useDelimiter(":");
        
        scanner.next();
        name = scanner.next().trim();
        
        return name;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importNodeKeyLists(KeyMap nodeKeyLists) {
        nodeKeyLists_ = nodeKeyLists;
        nodeKeyLists_.clear();
        
        String line;
        String name;
        
        for (int i = 0; i < data_.size(); i++) {
            line = data_.get(i);
            
            switch (this.getFormat(line)) {
                case ELEMENT_FORMAT:
                    i++;
                    break;
                case NODE_FORMAT:
                    name = this.getNodeKeyListName(line);
                    line = data_.get(++i);
                    this.addNodeKeys(line, name);
                    break;
                case SET_FORMAT:
                    this.addNodeKeys(line, null);
                    break;
                default:
            }
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void readFile()
            throws IOException {
        data_                           = new ArrayList<String>();
        BufferedReader  bufferedReader  = null;
        String          line;
        StringBuilder   commandLine;
        
        try {
            bufferedReader = new BufferedReader(this);
            
            line = bufferedReader.readLine();
            while (line != null) {
                switch (this.getFormat(line)) {
                    case SET_FORMAT:
                        commandLine = new StringBuilder();
                        commandLine.append(line.trim());
                        
                        while (line.trim().endsWith(",")) {
                            line = bufferedReader.readLine();
                            commandLine.append(line.trim());
                        }
                        
                        data_.add(commandLine.toString());
                        break;
                    default:
                        data_.add(line);
                }
                
                line = bufferedReader.readLine();
            }
        }
        finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
    } // eom
} // eoc