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

import de.iabg.j3d.ColorConstants;

import java.awt.Font;

import java.awt.geom.Line2D;

import javax.media.j3d.Appearance;
import javax.media.j3d.Background;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.Bounds;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Font3D;
import javax.media.j3d.FontExtrusion;
import javax.media.j3d.IndexedQuadArray;
import javax.media.j3d.LineArray;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

/*******************************************************************************
 * This class creates a content scene graph for the visualization of a 3D matrix
 * in Java3D.  The matrix is an instance of {@link ModeCorrelationMatrix} and
 * is rendered as a 3D matrix with the cells displayed as simple colored cubes.
 * The color of each cell depends on its value with respect to two tolerances,
 * which are simply defined as an upper and lower bound.  Cell values below the
 * lower bound are rendered as green, those above the uppper bound are rendered
 * as red, and those in between are rendered as yellow. These cells are
 * generated using Java3D quadrilaterals and then setting the appropriate polyon
 * attributes.  A copy of the quadrilaterals are created and rendered as lines
 * in order to outline the edges of the original quadrilaterals.
 * 
 * A grid is generated to border each cell and contrasts the color of the
 * background.  The background color should only be white or black, and the grid
 * color, also called the foreground in this class, will be the opposite color.
 * This is also true of the labels for the rows and columns.  In the case of
 * this {@code ModeCorrelationMatrix}, the row labels are the
 * {@code First Modes} and their frequencies, while the column labels are the
 * {@code Last Modes} and their frequencies.  The grids are created using Java3D
 * lines and the labels are created with Java3D text.
 * 
 * A background is created for the scene according to the specified color, which
 * should be either black or white, to give the greatest contrast.  The entire
 * scene is also scaled by the specified value so that the renderer can properly
 * view the entire contents of this scene graph.
 * 
 * The techniques used in this class are straight-forward applications of the
 * Java3D API and tutorial.  Refer to that API for more details.
 * 
 * @author  Adam C. Dick, BSE
 * @version September 12, 2008
 ******************************************************************************/
public class ModeCorrelationScene3D extends BranchGroup {
    /** The background color of the scene */
    protected Color3f backgroundColor_;
    
    /** The {@code ModeCorrelationMatrix} that this class renders */
    protected ModeCorrelationMatrix correlation_;
    
    /** Quadrilaterals to render the faces of the cells */
    protected IndexedQuadArray filledIndexedQuads_;
    
    /** The {@code First Mode} name */
    protected String firstName_;
    
    /** The foreground color of the scene */
    protected Color3f foregroundColor_;
    
    /** The {@code Last Mode} name */
    protected String lastName_;
    
    /** Quadrilaterals to render the outline of the cells */
    protected IndexedQuadArray linedIndexedQuads_;
    
    /** Lines to render the grid of the matrix */
    protected LineArray lines_;
    
    /** The lower tolerance for the cell color */
    protected double lowerTolerance_;
    
    /** The number of components in 3D space */
    protected static final int N_COMPONENTS = 3;
    
    /** The number of coordinate indices per cube */
    protected static final int N_INDICES = 24;
    
    /** The number of vertices of a cube */
    protected static final int N_VERTICES = 8;
    
    /** The number of columns or {@code Last Modes} */
    protected int nColumns_;
    
    /** The number of columns or {@code First Modes} */
    protected int nRows_;
    
    /** The number of tick marks along the z-axis */
    protected int nTicks_;
    
    /** The scale of the scene */
    protected double scale_;
    
    /** The upper tolerance for the cell color */
    protected double upperTolerance_;
    
    /** The values of the {@code ModeCorrelationMatrix} */
    protected double[] values_;
    
    
    
    /***************************************************************************
     * Constructs a {@code ModeCorrelationScene3D} from the given parameters,
     * instantiates all the required objects, and installs the scene graph.
     * 
     * @param   correlation     the {@code ModeCorrelationMatrix} that this
     *                          scene graph represents
     * @param   scale           the scale of the scene
     * @param   backgroundColor the background color of the scene
     * @param   lowerTolerance  the lower tolerance of the cell color
     * @param   upperTolerance  the uppser tolerance of the cell color
     * @param   firstName       the name of the {@code First Modes}
     * @param   lastName        the name of the {@code Last Modes}
     **************************************************************************/
    public ModeCorrelationScene3D(ModeCorrelationMatrix correlation,
            double scale, Color3f backgroundColor, double lowerTolerance,
            double upperTolerance, String firstName, String lastName) {
        super();
        
        double[]            values  = correlation.values();
        nRows_                      = correlation.getRowCount();
        nColumns_                   = correlation.getColumnCount();
        nTicks_                     = 5;
        scale_                      = scale;
        lowerTolerance_             = lowerTolerance;
        upperTolerance_             = upperTolerance;
        correlation_                = correlation;
        backgroundColor_            = backgroundColor;
        firstName_                  = firstName;
        lastName_                   = lastName;
        
        if (backgroundColor_.equals(ColorConstants.BLACK_COLOR)) {
            foregroundColor_ = ColorConstants.WHITE_COLOR;
        }
        else {
            foregroundColor_ = ColorConstants.BLACK_COLOR;
        }
        
        if (lowerTolerance > upperTolerance) {
            throw new IllegalArgumentException();
        }
        
        if (!correlation.isEmpty()) {
            values_             = new double[values.length];
            filledIndexedQuads_ = new IndexedQuadArray(
                    N_VERTICES * nRows_ * nColumns_,
                    IndexedQuadArray.COORDINATES | IndexedQuadArray.COLOR_3,
                    N_INDICES * nRows_ * nColumns_);
            linedIndexedQuads_  = new IndexedQuadArray(
                    N_VERTICES * nRows_ * nColumns_,
                    IndexedQuadArray.COORDINATES,
                    N_INDICES * nRows_ * nColumns_);
            lines_              = new LineArray(
                    4 * (nRows_ + nColumns_ + nTicks_ + 2),
                    LineArray.COORDINATES);
            
            System.arraycopy(values, 0, values_, 0, values.length);
            
            this.installScene();
        }
        
        this.installBackground();
    } // eom
    
    
    
    /***************************************************************************
     * Installs a background for the scene using the stored background color.
     **************************************************************************/
    protected void installBackground() {
        Background  background;
        Bounds      bounds;
        
        bounds      = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        background  = new Background(backgroundColor_);
        background.setApplicationBounds(bounds);
        this.addChild(background);
    } // eom
    
    
    
    /***************************************************************************
     * Installs the coordinates of the grid for the cells at the stored scale.
     * Each cell is given a unit size and then the coordinates are scaled.  A
     * net is also generated along the rear x-plane and the right y-plane in
     * order to give the user an idea of the value of each cell.
     **************************************************************************/
    protected void installLineCoordinates() {
        double[]    coordinates = new double[
                4 * N_COMPONENTS * (nRows_ + nColumns_ + nTicks_ + 2)];
        double      xLast;
        double      yLast;
        double      zLast;
        double      xNext;
        double      yNext;
        double      zNext;
        int         lineIndex;
        
        for (int i = 0; i <= nRows_; i++) {
            xLast = 0.0;
            yLast = scale_ * (-i);
            zLast = 0.0;
            xNext = scale_ * nColumns_;
            yNext = scale_ * (-i);
            zNext = 0.0;
            
            coordinates[(i * 4 * N_COMPONENTS) +  0] = xLast;
            coordinates[(i * 4 * N_COMPONENTS) +  1] = yLast;
            coordinates[(i * 4 * N_COMPONENTS) +  2] = zLast;
            coordinates[(i * 4 * N_COMPONENTS) +  3] = xNext;
            coordinates[(i * 4 * N_COMPONENTS) +  4] = yNext;
            coordinates[(i * 4 * N_COMPONENTS) +  5] = zNext;
            
            xLast = scale_ * nColumns_;
            yLast = scale_ * (-i);
            zLast = 0.0;
            xNext = scale_ * nColumns_;
            yNext = scale_ * (-i);
            zNext = 1.0;
            
            coordinates[(i * 4 * N_COMPONENTS) +  6] = xLast;
            coordinates[(i * 4 * N_COMPONENTS) +  7] = yLast;
            coordinates[(i * 4 * N_COMPONENTS) +  8] = zLast;
            coordinates[(i * 4 * N_COMPONENTS) +  9] = xNext;
            coordinates[(i * 4 * N_COMPONENTS) + 10] = yNext;
            coordinates[(i * 4 * N_COMPONENTS) + 11] = zNext;
        }
        
        lineIndex = 4 * N_COMPONENTS * (nRows_ + 1);
        
        for (int i = 0; i <= nColumns_; i++) {
            xLast = scale_ * i;
            yLast = 0.0;
            zLast = 0.0;
            xNext = scale_ * i;
            yNext = scale_ * (-nRows_);
            zNext = 0.0;
            
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  0] = xLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  1] = yLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  2] = zLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  3] = xNext;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  4] = yNext;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  5] = zNext;
            
            xLast = scale_ * i;
            yLast = 0.0;
            zLast = 0.0;
            xNext = scale_ * i;
            yNext = 0.0;
            zNext = 1.0;
            
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  6] = xLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  7] = yLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  8] = zLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  9] = xNext;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) + 10] = yNext;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) + 11] = zNext;
        }
        
        lineIndex += 4 * N_COMPONENTS * (nColumns_ + 1);
        
        for (int i = 0; i < nTicks_; i++) {
            xLast = 0.0;
            yLast = 0.0;
            zLast = 1.0 / nTicks_ * (i + 1);
            xNext = scale_ * nColumns_;
            yNext = 0.0;
            zNext = 1.0 / nTicks_ * (i + 1);
            
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  0] = xLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  1] = yLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  2] = zLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  3] = xNext;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  4] = yNext;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  5] = zNext;
            
            xLast = scale_ * nColumns_;
            yLast = 0.0;
            zLast = 1.0 / nTicks_ * (i + 1);
            xNext = scale_ * nColumns_;
            yNext = scale_ * (-nRows_);
            zNext = 1.0 / nTicks_ * (i + 1);
            
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  6] = xLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  7] = yLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  8] = zLast;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) +  9] = xNext;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) + 10] = yNext;
            coordinates[lineIndex + (i * 4 * N_COMPONENTS) + 11] = zNext;
        }
        
        lines_.setCoordinates(0, coordinates);
    } // eom
    
    
    
    /***************************************************************************
     * Installs the quadrilateral color indices since this class takes advantage
     * of the indexed arrays in Java3D.  This minimizes the storage requirements
     * of the shapes, although the work that the renderer must do is increased.
     **************************************************************************/
    protected void installQuadColorIndices() {
        int[] indices = new int[N_INDICES * nRows_ * nColumns_];
        
        for (int i = 0; i < values_.length; i++) {
            for (int j = 0; j < N_INDICES; j++) {
                indices[(i * N_INDICES) + j] = i;
            }
        }
        
        filledIndexedQuads_.setColorIndices(0, indices);
    } // eom
    
    
    
    /***************************************************************************
     * Installs the quadrilateral colors of the cell faces.  Cell values less
     * than the lower tolerance are set as green, those greater than the upper
     * tolerance are set as red, and those in between are set as yellow.
     **************************************************************************/
    protected void installQuadColors() {
        Color3f[] colors = new Color3f[nRows_ * nColumns_];
        
        for (int i = 0; i < values_.length; i++) {
            if (values_[i] < lowerTolerance_) {
                colors[i] = ColorConstants.GREEN_COLOR;
            }
            else if (values_[i] < upperTolerance_) {
                colors[i] = ColorConstants.YELLOW_COLOR;
            }
            else {
                colors[i] = ColorConstants.RED_COLOR;
            }
        }
        
        filledIndexedQuads_.setColors(0, colors);
    } // eom
    
    
    
    /***************************************************************************
     * Installs the quadrilateral coordinate indices since this class takes
     * advantage of the indexed arrays in Java3D.  This minimizes the storage
     * requirements of the shapes, although the work that the renderer must do
     * is increased.
     **************************************************************************/
    protected void installQuadCoordinateIndices() {
        int[] indices = new int[N_INDICES * nRows_ * nColumns_];
        
        for (int i = 0; i < values_.length; i++) {
            indices[(i * N_INDICES) +  0] = (i * N_VERTICES) + 0;
            indices[(i * N_INDICES) +  1] = (i * N_VERTICES) + 1;
            indices[(i * N_INDICES) +  2] = (i * N_VERTICES) + 2;
            indices[(i * N_INDICES) +  3] = (i * N_VERTICES) + 3;
            indices[(i * N_INDICES) +  4] = (i * N_VERTICES) + 7;
            indices[(i * N_INDICES) +  5] = (i * N_VERTICES) + 6;
            indices[(i * N_INDICES) +  6] = (i * N_VERTICES) + 5;
            indices[(i * N_INDICES) +  7] = (i * N_VERTICES) + 4;
            indices[(i * N_INDICES) +  8] = (i * N_VERTICES) + 3;
            indices[(i * N_INDICES) +  9] = (i * N_VERTICES) + 2;
            indices[(i * N_INDICES) + 10] = (i * N_VERTICES) + 6;
            indices[(i * N_INDICES) + 11] = (i * N_VERTICES) + 7;
            indices[(i * N_INDICES) + 12] = (i * N_VERTICES) + 1;
            indices[(i * N_INDICES) + 13] = (i * N_VERTICES) + 0;
            indices[(i * N_INDICES) + 14] = (i * N_VERTICES) + 4;
            indices[(i * N_INDICES) + 15] = (i * N_VERTICES) + 5;
            indices[(i * N_INDICES) + 16] = (i * N_VERTICES) + 0;
            indices[(i * N_INDICES) + 17] = (i * N_VERTICES) + 3;
            indices[(i * N_INDICES) + 18] = (i * N_VERTICES) + 7;
            indices[(i * N_INDICES) + 19] = (i * N_VERTICES) + 4;
            indices[(i * N_INDICES) + 20] = (i * N_VERTICES) + 2;
            indices[(i * N_INDICES) + 21] = (i * N_VERTICES) + 1;
            indices[(i * N_INDICES) + 22] = (i * N_VERTICES) + 5;
            indices[(i * N_INDICES) + 23] = (i * N_VERTICES) + 6;
        }
        
        filledIndexedQuads_.setCoordinateIndices(0, indices);
        linedIndexedQuads_.setCoordinateIndices(0, indices);
    } // eom
    
    
    
    /***************************************************************************
     * Installs the coordinates of the faces of the cells at the stored scale.
     * Each cell is given a unit size, whose borders are offset and then scaled.
     * The same coordinates are used for both the filled quadrilaterals and the
     * lined quadrilaterals.
     **************************************************************************/
    protected void installQuadCoordinates() {
        double[]    coordinates = new double[N_INDICES * nRows_ * nColumns_];
        double      offset      = 0.1;
        double      xLast;
        double      yLast;
        double      zLast;
        double      xNext;
        double      yNext;
        double      zNext;
        
        for (int i = 0; i < nRows_; i++) {
            for (int j = 0; j < nColumns_; j++) {
                xLast = scale_ * (j + offset);
                yLast = scale_ * (-i - offset);
                zLast = 0.0;
                xNext = scale_ * (j + 1 - offset);
                yNext = scale_ * (-i - 1 + offset);
                zNext = values_[(i * nColumns_) + j];
                
                coordinates[(((i * nColumns_) + j) * N_INDICES) +  0] = xLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) +  1] = yLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) +  2] = zLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) +  3] = xNext;
                coordinates[(((i * nColumns_) + j) * N_INDICES) +  4] = yLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) +  5] = zLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) +  6] = xNext;
                coordinates[(((i * nColumns_) + j) * N_INDICES) +  7] = yNext;
                coordinates[(((i * nColumns_) + j) * N_INDICES) +  8] = zLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) +  9] = xLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 10] = yNext;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 11] = zLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 12] = xLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 13] = yLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 14] = zNext;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 15] = xNext;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 16] = yLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 17] = zNext;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 18] = xNext;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 19] = yNext;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 20] = zNext;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 21] = xLast;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 22] = yNext;
                coordinates[(((i * nColumns_) + j) * N_INDICES) + 23] = zNext;
            }
        }
        
        filledIndexedQuads_.setCoordinates(0, coordinates);
        linedIndexedQuads_.setCoordinates(0, coordinates);
    } // eom
    
    
    
    /***************************************************************************
     * Installs the grid coordinates, the cell face coordinates and colors, the
     * cell face coordinate and color indices, the text for the labels, and adds
     * these shapes to this scene graph.
     **************************************************************************/
    protected void installScene() {
        this.installLineCoordinates();
        this.installQuadColors();
        this.installQuadColorIndices();
        this.installQuadCoordinates();
        this.installQuadCoordinateIndices();
        this.installText();
        this.installShapes();
    } // eom
    
    
    
    /***************************************************************************
     * Installs the stored coordinate arrays to the scene graph with the stored
     * color coordinates.  This includes the filled quadrilaterals, the lined
     * quadrilaterals, and the grid lines.  Attributes have also been applied to
     * each shape as necessary to optimally render the scene.
     **************************************************************************/
    protected void installShapes() {
        Appearance          appearance;
        ColoringAttributes  coloringAttributes;
        PolygonAttributes   polygonAttributes;
        Shape3D             shape3D;
        
        shape3D = new Shape3D(filledIndexedQuads_);
        this.addChild(shape3D);
        
        coloringAttributes = new ColoringAttributes();
        coloringAttributes.setColor(ColorConstants.BLACK_COLOR);
        
        polygonAttributes = new PolygonAttributes();
        polygonAttributes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        
        appearance = new Appearance();
        appearance.setColoringAttributes(coloringAttributes);
        appearance.setPolygonAttributes(polygonAttributes);
        
        shape3D = new Shape3D(linedIndexedQuads_, appearance);
        this.addChild(shape3D);
        
        coloringAttributes = new ColoringAttributes();
        coloringAttributes.setColor(foregroundColor_);
        
        appearance = new Appearance();
        appearance.setColoringAttributes(coloringAttributes);
        
        shape3D = new Shape3D(lines_, appearance);
        this.addChild(shape3D);
    } // eom
    
    
    
    /***************************************************************************
     * Installs the row and column labels of this matrix using the stored
     * foreground color at the stored scale.  The text consists of the axis
     * name, which is either the name of the {@code First Mode} or
     * {@code Last Mode}, the name and frequency of each individual mode, and
     * the z-axis label and tick marks.  Transform groups are applied to each
     * text shape in order to easily read the text in the scene.  Since there is
     * no text array in Java3D, each shape is added directly to this scene
     * graph.
     **************************************************************************/
    protected void installText() {
        Appearance          appearance;
        ColoringAttributes  coloringAttributes;
        Font                font;
        FontExtrusion       fontExtrusion;
        Font3D              font3D;
        Point3f             point3f;
        Shape3D             shape3D;
        Text3D              text3D;
        Transform3D         transformX;
        Transform3D         transformZ;
        TransformGroup      transformGroup;
        double[]            firstFrequencies;
        String[]            firstNames;
        double[]            lastFrequencies;
        String[]            lastNames;
        String              text;
        
        firstFrequencies    = correlation_.getFirstModeFrequencies();
        firstNames          = correlation_.getFirstModeNames();
        lastFrequencies     = correlation_.getLastModeFrequencies();
        lastNames           = correlation_.getLastModeNames();
        
        coloringAttributes = new ColoringAttributes();
        coloringAttributes.setColor(foregroundColor_);
        
        appearance = new Appearance();
        appearance.setColoringAttributes(coloringAttributes);
        
        font            = new Font("Helvetica", Font.PLAIN, 1);
        fontExtrusion   = new FontExtrusion(new Line2D.Double(0, 0, 0, 0));
        font3D          = new Font3D(font, fontExtrusion);
        transformX      = new Transform3D();
        
        transformX.setScale(scale_);
        transformGroup = new TransformGroup(transformX);
        
        for (int i = 0; i < nRows_; i++) {
            point3f = new Point3f(0.0f, (float) (-i - 1), 0.0f);
            text    = String.format("%s: %6.2f Hz ", firstNames[i],
                    firstFrequencies[i]);
            
            text3D = new Text3D(font3D, text, point3f);
            text3D.setAlignment(Text3D.ALIGN_LAST);
            
            shape3D = new Shape3D(text3D, appearance);
            transformGroup.addChild(shape3D);
        }
        
        this.addChild(transformGroup);
        
        transformZ = new Transform3D();
        transformZ.rotZ(Math.PI / 2.0);
        transformZ.setScale(scale_);
        transformGroup = new TransformGroup(transformZ);
        
        for (int i = 0; i < nColumns_; i++) {
            point3f = new Point3f((float) -nRows_, (float) -(i + 1), 0.0f);
            text    = String.format("%s: %6.2f Hz ", lastNames[i],
                    lastFrequencies[i]);
            
            text3D = new Text3D(font3D, text, point3f);
            text3D.setAlignment(Text3D.ALIGN_LAST);
            
            shape3D = new Shape3D(text3D, appearance);
            transformGroup.addChild(shape3D);
        }
        
        this.addChild(transformGroup);
        
        transformX = new Transform3D();
        transformX.rotX(Math.PI / 2.0);
        transformX.setScale(scale_);
        transformGroup = new TransformGroup(transformX);
        
        for (int i = 0; i <= nTicks_; i++) {
            point3f = new Point3f(
                    0.0f, (float) ((1.0 * i) / (nTicks_ * scale_) - 0.5), 0.0f);
            text    = String.format("%3.1f ", 1.0 / nTicks_ * i);
            
            text3D = new Text3D(font3D, text, point3f);
            text3D.setAlignment(Text3D.ALIGN_LAST);
            
            shape3D = new Shape3D(text3D, appearance);
            transformGroup.addChild(shape3D);
        }
        
        this.addChild(transformGroup);
        
        transformZ = new Transform3D();
        transformZ.rotZ(Math.PI / 2.0);
        transformZ.setScale(scale_);
        transformGroup = new TransformGroup(transformZ);
        
        point3f = new Point3f((float) -(nRows_ / 2.0f), (float) 8.0f, 0.0f);
        text    = String.format("%s", firstName_);
        text3D  = new Text3D(font3D, text, point3f);
        text3D.setAlignment(Text3D.ALIGN_CENTER);
        shape3D = new Shape3D(text3D, appearance);
        transformGroup.addChild(shape3D);
        this.addChild(transformGroup);
        
        transformX = new Transform3D();
        transformX.setScale(scale_);
        transformGroup = new TransformGroup(transformX);
        
        point3f = new Point3f((float) nColumns_ / 2.0f, (float) -nRows_ - 8.0f,
                0.0f);
        text    = String.format("%s", lastName_);
        text3D  = new Text3D(font3D, text, point3f);
        text3D.setAlignment(Text3D.ALIGN_CENTER);
        shape3D = new Shape3D(text3D, appearance);
        transformGroup.addChild(shape3D);
        this.addChild(transformGroup);
        
        transformX = new Transform3D();
        transformX.rotX(Math.PI / 2.0);
        transformZ = new Transform3D();
        transformZ.rotZ(Math.PI / 2.0);
        transformX.mul(transformZ);
        transformX.setScale(scale_);
        transformGroup = new TransformGroup(transformX);
        
        point3f = new Point3f(0.5f / (float) scale_, 3.0f, 0.0f);
        text    = String.format("%s", "Correlation");
        text3D  = new Text3D(font3D, text, point3f);
        text3D.setAlignment(Text3D.ALIGN_CENTER);
        shape3D = new Shape3D(text3D, appearance);
        transformGroup.addChild(shape3D);
        this.addChild(transformGroup);
    } // eom
} // eoc