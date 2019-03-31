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

import de.iabg.mesh.GeometryReader;
import de.iabg.mesh.NodeConnectionMatrix;
import de.iabg.mesh.NodeMatrix;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Scanner;

/*******************************************************************************
 * This implementation of {@link GeometryReader} parses a Nastran file
 * containing geometry data, namely nodes, edges, triangles, and quadrilaterals.
 * Refer to {@code GeometryReader} class API for more details.
 * 
 * The Nastran file can come in three formats, called free, large, and small.
 * This parser is designed to handle each format for every line.  Refer to the
 * latest version of the MSC.Nastran Quick Reference Guide for more information
 * for the file format.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 13, 2008
 ******************************************************************************/
public class NastranBulkDataReader extends FileReader
        implements GeometryReader {
    /** A constant for comments */
    protected static final int COMMENT_FORMAT = 1000;
    
    /** A list for the data from the file */
    protected ArrayList<String> data_;
    
    /** Storage for the edges */
    protected NodeConnectionMatrix edges_;
    
    /** A constant for free continuation lines */
    protected static final int FREE_CONTINUATION_FORMAT = 1001;
    
    /** A constant for free lines */
    protected static final int FREE_FIELD_FORMAT = 1002;
    
    /** A constant for large continuation lines */
    protected static final int LARGE_CONTINUATION_FORMAT = 1003;
    
    /** A constant for large lines */
    protected static final int LARGE_FIELD_FORMAT = 1004;
    
    /** Storage for the nodes */
    protected NodeMatrix nodes_;
    
    /** Storage for the quadrilaterals */
    protected NodeConnectionMatrix quads_;
    
    /** A constant for small continuation lines */
    protected static final int SMALL_CONTINUATION_FORMAT = 1005;
    
    /** A constant for small lines */
    protected static final int SMALL_FIELD_FORMAT = 1006;
    
    /** Storage for the triangles*/
    protected NodeConnectionMatrix triangles_;
    
    
    
    /***************************************************************************
     * Constructs a {@code NastranBulkDataReader} from the given file.
     * 
     * @param   file                            the file to be imported
     * @throws  java.io.FileNotFoundException   if the file could not be found
     **************************************************************************/
    public NastranBulkDataReader(File file)
            throws FileNotFoundException {
        super(file);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a free edge from the given line and adds it to the stored edges at
     * the given index.
     * 
     * @param   edgeIndex   the index of the edge
     * @param   line        the line to be parsed
     **************************************************************************/
    protected void addFreeEdge(int edgeIndex, String line) {
        Scanner scanner;
        String  firstNode;
        String  lastNode;
        
        scanner = new Scanner(line);
        scanner.useDelimiter(",");
        
        scanner.next();
        scanner.next();
        scanner.next();
        
        firstNode   = scanner.next().trim();
        lastNode    = scanner.next().trim();
        
        edges_.setFirstNodeNameAt(firstNode, edgeIndex);
        edges_.setLastNodeNameAt(lastNode, edgeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a free node from the given line and adds it to the stored nodes at
     * the given index.
     * 
     * @param   nodeIndex   the index of the node
     * @param   line        the line to be parsed
     **************************************************************************/
    protected void addFreeNode(int nodeIndex, String line) {
        Scanner scanner;
        String  name;
        double  x;
        double  y;
        double  z;
        
        scanner = new Scanner(line);
        scanner.useDelimiter(",");
        
        scanner.next();
        name = scanner.next().trim();
        
        scanner.next();
        x = Double.parseDouble((scanner.next().trim()));
        y = Double.parseDouble((scanner.next().trim()));
        z = Double.parseDouble((scanner.next().trim()));
        
        nodes_.setNodeNameAt(name, nodeIndex);
        nodes_.setXCoordinateAt(x, nodeIndex);
        nodes_.setYCoordinateAt(y, nodeIndex);
        nodes_.setZCoordinateAt(z, nodeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a free quadrilateral from the given line and adds it to the stored
     * quadrilaterals at the given index.
     * 
     * @param   quadIndex   the index of the quadrilateral
     * @param   line        the line to be parsed
     **************************************************************************/
    protected void addFreeQuadrilateral(int quadIndex, String line) {
        Scanner scanner;
        String  firstNode;
        String  secondNode;
        String  thirdNode;
        String  fourthNode;
        
        scanner = new Scanner(line);
        scanner.useDelimiter(",");
        
        scanner.next();
        scanner.next();
        scanner.next();
        
        firstNode   = scanner.next().trim();
        secondNode  = scanner.next().trim();
        thirdNode   = scanner.next().trim();
        fourthNode   = scanner.next().trim();
        
        quads_.setFirstNodeNameAt(firstNode, 4 * quadIndex + 0);
        quads_.setLastNodeNameAt(secondNode, 4 * quadIndex + 0);
        quads_.setFirstNodeNameAt(secondNode, 4 * quadIndex + 1);
        quads_.setLastNodeNameAt(thirdNode, 4 * quadIndex + 1);
        quads_.setFirstNodeNameAt(thirdNode, 4 * quadIndex + 2);
        quads_.setLastNodeNameAt(fourthNode, 4 * quadIndex + 2);
        quads_.setFirstNodeNameAt(fourthNode, 4 * quadIndex + 3);
        quads_.setLastNodeNameAt(firstNode, 4 * quadIndex + 3);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a free triangle from the given line and adds it to the stored
     * triangles at the given index.
     * 
     * @param   triangleIndex   the index of the triangle
     * @param   line            the line to be parsed
     **************************************************************************/
    protected void addFreeTriangle(int triangleIndex, String line) {
        Scanner scanner;
        String  firstNode;
        String  secondNode;
        String  thirdNode;
        
        scanner = new Scanner(line);
        scanner.useDelimiter(",");
        
        scanner.next();
        scanner.next();
        scanner.next();
        
        firstNode   = scanner.next().trim();
        secondNode  = scanner.next().trim();
        thirdNode   = scanner.next().trim();
        
        triangles_.setFirstNodeNameAt(firstNode, 3 * triangleIndex + 0);
        triangles_.setLastNodeNameAt(secondNode, 3 * triangleIndex + 0);
        triangles_.setFirstNodeNameAt(secondNode, 3 * triangleIndex + 1);
        triangles_.setLastNodeNameAt(thirdNode, 3 * triangleIndex + 1);
        triangles_.setFirstNodeNameAt(thirdNode, 3 * triangleIndex + 2);
        triangles_.setLastNodeNameAt(firstNode, 3 * triangleIndex + 2);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a large edge from the given line and adds it to the stored edges
     * at the given index.
     * 
     * @param   edgeIndex   the index of the edge
     * @param   line        the line to be parsed
     **************************************************************************/
    protected void addLargeEdge(int edgeIndex, String line) {
        String firstNode;
        String lastNode;
        
        firstNode   = line.substring(40, 56).trim();
        lastNode    = line.substring(56, 72).trim();
        
        edges_.setFirstNodeNameAt(firstNode, edgeIndex);
        edges_.setLastNodeNameAt(lastNode, edgeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a large node from the given line and adds it to the stored nodes
     * at the given index.
     * 
     * @param   nodeIndex   the index of the node
     * @param   line        the line to be parsed
     **************************************************************************/
    protected void addLargeNode(int nodeIndex, String line) {
        String name;
        double x;
        double y;
        double z;
        
        name    = line.substring(8, 24).trim();
        x       = Double.parseDouble(line.substring(40, 56).trim());
        y       = Double.parseDouble(line.substring(56, 72).trim());
        z       = Double.parseDouble(line.substring(72, 88).trim());
        
        nodes_.setNodeNameAt(name, nodeIndex);
        nodes_.setXCoordinateAt(x, nodeIndex);
        nodes_.setYCoordinateAt(y, nodeIndex);
        nodes_.setZCoordinateAt(z, nodeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a large quadrilateral from the given line and adds it to the
     * stored quadrilaterals at the given index.
     * 
     * @param   quadIndex   the index of the quadrilateral
     * @param   line        the line to be parsed
     **************************************************************************/
    protected void addLargeQuadrilateral(int quadIndex, String line) {
        String firstNode;
        String secondNode;
        String thirdNode;
        String fourthNode;
        
        firstNode   = line.substring(40, 56).trim();
        secondNode  = line.substring(56, 72).trim();
        thirdNode   = line.substring(72, 88).trim();
        fourthNode  = line.substring(88, 104).trim();
        
        quads_.setFirstNodeNameAt(firstNode, 4 * quadIndex + 0);
        quads_.setLastNodeNameAt(secondNode, 4 * quadIndex + 0);
        quads_.setFirstNodeNameAt(secondNode, 4 * quadIndex + 1);
        quads_.setLastNodeNameAt(thirdNode, 4 * quadIndex + 1);
        quads_.setFirstNodeNameAt(thirdNode, 4 * quadIndex + 2);
        quads_.setLastNodeNameAt(fourthNode, 4 * quadIndex + 2);
        quads_.setFirstNodeNameAt(fourthNode, 4 * quadIndex + 3);
        quads_.setLastNodeNameAt(firstNode, 4 * quadIndex + 3);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a large triangle from the given line and adds it to the stored
     * triangles at the given index.
     * 
     * @param   triangleIndex   the index of the triangle
     * @param   line            the line to be parsed
     **************************************************************************/
    protected void addLargeTriangle(int triangleIndex, String line) {
        String firstNode;
        String secondNode;
        String thirdNode;
        
        firstNode   = line.substring(40, 56).trim();
        secondNode  = line.substring(56, 72).trim();
        thirdNode   = line.substring(72, 88).trim();
        
        triangles_.setFirstNodeNameAt(firstNode, 3 * triangleIndex + 0);
        triangles_.setLastNodeNameAt(secondNode, 3 * triangleIndex + 0);
        triangles_.setFirstNodeNameAt(secondNode, 3 * triangleIndex + 1);
        triangles_.setLastNodeNameAt(thirdNode, 3 * triangleIndex + 1);
        triangles_.setFirstNodeNameAt(thirdNode, 3 * triangleIndex + 2);
        triangles_.setLastNodeNameAt(firstNode, 3 * triangleIndex + 2);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a small edge from the given line and adds it to the stored edges
     * at the given index.
     * 
     * @param   edgeIndex   the index of the edge
     * @param   line        the line to be parsed
     **************************************************************************/
    protected void addSmallEdge(int edgeIndex, String line) {
        String firstNode;
        String lastNode;
        
        firstNode   = line.substring(24, 32).trim();
        lastNode    = line.substring(32, 40).trim();
        
        edges_.setFirstNodeNameAt(firstNode, edgeIndex);
        edges_.setLastNodeNameAt(lastNode, edgeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a small node from the given line and adds it to the stored nodes
     * at the given index.
     * 
     * @param   nodeIndex   the index of the node
     * @param   line        the line to be parsed
     **************************************************************************/
    protected void addSmallNode(int nodeIndex, String line) {
        String name;
        double x;
        double y;
        double z;
        
        name    = line.substring(8, 16).trim();
        x       = Double.parseDouble(line.substring(24, 32).trim());
        y       = Double.parseDouble(line.substring(32, 40).trim());
        z       = Double.parseDouble(line.substring(40, 48).trim());
        
        nodes_.setNodeNameAt(name, nodeIndex);
        nodes_.setXCoordinateAt(x, nodeIndex);
        nodes_.setYCoordinateAt(y, nodeIndex);
        nodes_.setZCoordinateAt(z, nodeIndex);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a small quadrilateral from the given line and adds it to the
     * stored quadrilaterals at the given index.
     * 
     * @param   quadIndex   the index of the quadrilateral
     * @param   line        the line to be parsed
     **************************************************************************/
    protected void addSmallQuadrilateral(int quadIndex, String line) {
        String firstNode;
        String secondNode;
        String thirdNode;
        String fourthNode;
        
        firstNode   = line.substring(24, 32).trim();
        secondNode  = line.substring(32, 40).trim();
        thirdNode   = line.substring(40, 48).trim();
        fourthNode  = line.substring(48, 56).trim();
        
        quads_.setFirstNodeNameAt(firstNode, 4 * quadIndex + 0);
        quads_.setLastNodeNameAt(secondNode, 4 * quadIndex + 0);
        quads_.setFirstNodeNameAt(secondNode, 4 * quadIndex + 1);
        quads_.setLastNodeNameAt(thirdNode, 4 * quadIndex + 1);
        quads_.setFirstNodeNameAt(thirdNode, 4 * quadIndex + 2);
        quads_.setLastNodeNameAt(fourthNode, 4 * quadIndex + 2);
        quads_.setFirstNodeNameAt(fourthNode, 4 * quadIndex + 3);
        quads_.setLastNodeNameAt(firstNode, 4 * quadIndex + 3);
    } // eom
    
    
    
    /***************************************************************************
     * Parses a small triangle from the given line and adds it to the stored
     * triangles at the given index.
     * 
     * @param   triangleIndex   the index of the triangle
     * @param   line            the line to be parsed
     **************************************************************************/
    protected void addSmallTriangle(int triangleIndex, String line) {
        String firstNode;
        String secondNode;
        String thirdNode;
        
        firstNode   = line.substring(24, 32).trim();
        secondNode  = line.substring(32, 40).trim();
        thirdNode   = line.substring(40, 48).trim();
        
        triangles_.setFirstNodeNameAt(firstNode, 3 * triangleIndex + 0);
        triangles_.setLastNodeNameAt(secondNode, 3 * triangleIndex + 0);
        triangles_.setFirstNodeNameAt(secondNode, 3 * triangleIndex + 1);
        triangles_.setLastNodeNameAt(thirdNode, 3 * triangleIndex + 1);
        triangles_.setFirstNodeNameAt(thirdNode, 3 * triangleIndex + 2);
        triangles_.setLastNodeNameAt(firstNode, 3 * triangleIndex + 2);
    } // eom
    
    
    
    /***************************************************************************
     * If the length of the given line is less than 72 characters, white space
     * characters are appended to the line until it reaches that size.
     * 
     * @param   line    the line to be filled
     * @return  the new line that has been filled
     **************************************************************************/
    protected String fillFields(String line) {
        StringBuilder   filledLine = new StringBuilder(line);
        int             lineLength = 72;
        
        while (filledLine.length() < lineLength) {
            filledLine.append(" ");
        }
        
        return filledLine.toString();
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of edges in the data.
     * 
     * @return  the number of edges
     **************************************************************************/
    protected int getEdgeCount() {
        int nEdges = 0;
        
        for (String line : data_) {
            if (line.toUpperCase().startsWith("CBAR")) {
                nEdges++;
            }
            else if (line.toUpperCase().startsWith("CBEAM")) {
                nEdges++;
            }
        }
        
        return nEdges;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the format of the given line.
     * 
     * @param   line    the line to be parsed
     * @return  the line format
     **************************************************************************/
    protected int getFormat(String line) {
        if (line.startsWith("$")) {
            return COMMENT_FORMAT;
        }
        else if (line.startsWith(",")) {
            return FREE_CONTINUATION_FORMAT;
        }
        else if (line.contains(",")) {
            return FREE_FIELD_FORMAT;
        }
        else if (line.startsWith("*")) {
            return LARGE_CONTINUATION_FORMAT;
        }
        else if (line.contains("*")) {
            return LARGE_FIELD_FORMAT;
        }
        else if (line.startsWith("        ")) {
            return SMALL_CONTINUATION_FORMAT;
        }
        else {
            return SMALL_FIELD_FORMAT;
        }
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of nodes in the data.
     * 
     * @return  the number of nodes
     **************************************************************************/
    protected int getNodeCount() {
        int nNodes = 0;
        
        for (String line : data_) {
            if (line.toUpperCase().startsWith("GRID")) {
                nNodes++;
            }
        }
        
        return nNodes;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of quadrilaterals in the data.
     * 
     * @return  the number of quadrilaterals
     **************************************************************************/
    protected int getQuadrilateralCount() {
        int nQuadrilaterals = 0;
        
        for (String line : data_) {
            if (line.toUpperCase().startsWith("CQUAD4")) {
                nQuadrilaterals++;
            }
        }
        
        return nQuadrilaterals;
    } // eom
    
    
    
    /***************************************************************************
     * Returns the number of triangles in the data.
     * 
     * @return  the number of triangles
     **************************************************************************/
    protected int getTriangleCount() {
        int nTriangles = 0;
        
        for (String line : data_) {
            if (line.toUpperCase().startsWith("CTRIA3")) {
                nTriangles++;
            }
        }
        
        return nTriangles;
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importEdges(NodeConnectionMatrix edges) {
        edges_ = edges;
        edges_.setConnectionCount(this.getEdgeCount());
        
        int edgeIndex = 0;
        
        for (String line : data_) {
            if (line.toUpperCase().startsWith("CBAR") ||
                    line.toUpperCase().startsWith("CBEAM")) {
                switch (this.getFormat(line)) {
                    case FREE_FIELD_FORMAT:
                        this.addFreeEdge(edgeIndex, line);
                        break;
                    case LARGE_FIELD_FORMAT:
                        this.addLargeEdge(edgeIndex, line);
                        break;
                    case SMALL_FIELD_FORMAT:
                        this.addSmallEdge(edgeIndex, line);
                        break;
                    default:
                }
                
                edgeIndex++;
            }
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importNodes(NodeMatrix nodes) {
        nodes_ = nodes;
        nodes_.setNodeCount(this.getNodeCount());
        
        int nodeIndex = 0;
        
        for (String line : data_) {
            if (line.toUpperCase().startsWith("GRID")) {
                switch (this.getFormat(line)) {
                    case FREE_FIELD_FORMAT:
                        this.addFreeNode(nodeIndex, line);
                        break;
                    case LARGE_FIELD_FORMAT:
                        this.addLargeNode(nodeIndex, line);
                        break;
                    case SMALL_FIELD_FORMAT:
                        this.addSmallNode(nodeIndex, line);
                        break;
                    default:
                }
                
                nodeIndex++;
            }
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importQuadrilaterals(NodeConnectionMatrix quads) {
        quads_ = quads;
        quads_.setConnectionCount(4 * this.getQuadrilateralCount());
        
        int quadIndex = 0;
        
        for (String line : data_) {
            if (line.toUpperCase().startsWith("CQUAD4")) {
                switch (this.getFormat(line)) {
                    case FREE_FIELD_FORMAT:
                        this.addFreeQuadrilateral(quadIndex, line);
                        break;
                    case LARGE_FIELD_FORMAT:
                        this.addLargeQuadrilateral(quadIndex, line);
                        break;
                    case SMALL_FIELD_FORMAT:
                        this.addSmallQuadrilateral(quadIndex, line);
                        break;
                    default:
                }
                
                quadIndex++;
            }
        }
    } // eom
    
    
    
    /***************************************************************************
     **************************************************************************/
    public void importTriangles(NodeConnectionMatrix triangles) {
        triangles_ = triangles;
        triangles_.setConnectionCount(3 * this.getTriangleCount());
        
        int triangleIndex = 0;
        
        for (String line : data_) {
            if (line.toUpperCase().startsWith("CTRIA3")) {
                switch (this.getFormat(line)) {
                    case FREE_FIELD_FORMAT:
                        this.addFreeTriangle(triangleIndex, line);
                        break;
                    case LARGE_FIELD_FORMAT:
                        this.addLargeTriangle(triangleIndex, line);
                        break;
                    case SMALL_FIELD_FORMAT:
                        this.addSmallTriangle(triangleIndex, line);
                        break;
                    default:
                }
                
                triangleIndex++;
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
                line = this.fillFields(line);
                
                switch (this.getFormat(line)) {
                    case FREE_CONTINUATION_FORMAT:
                        commandLine = new StringBuilder();
                        commandLine.append(data_.get(data_.size() - 1));
                        commandLine.append(line.trim());
                        data_.set(data_.size() - 1, commandLine.toString());
                        break;
                    case LARGE_CONTINUATION_FORMAT:
                    case SMALL_CONTINUATION_FORMAT:
                        commandLine = new StringBuilder();
                        commandLine.append(data_.get(data_.size() - 1));
                        commandLine.append(line.substring(8));
                        data_.set(data_.size() - 1, commandLine.toString());
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