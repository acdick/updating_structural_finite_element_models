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
import javax.media.j3d.LineArray;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Text3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;

/*******************************************************************************
 * This class creates a content scene graph for the visualization of a 2D matrix
 * in Java3D.  The matrix is an instance of {@link ModeCorrelationMatrix} and
 * is rendered as a planar 2D matrix with the cells displayed as simple colored
 * squares.  The color of each cell depends on its value with respect to two
 * tolerances, which are simply defined as an upper and lower bound.  Cell
 * values below the lower bound are rendered as green, those above the uppper
 * bound are rendered as red, and those in between are rendered as yellow.
 * These cells are generated using Java3D quadrilaterals and then setting the
 * appropriate polyon attributes.  A copy of the quadrilaterals are created and
 * rendered as lines in order to outline the edges of the original
 * quadrilaterals.
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
public class ModeCorrelationScene2D extends BranchGroup {
    /** The background color of the scene */
    protected Color3f backgroundColor_;
    
    /** The {@code ModeCorrelationMatrix} that this class renders */
    protected ModeCorrelationMatrix correlation_;
    
    /** Quadrilaterals to render the faces of the cells */
    protected QuadArray filledQuads_;
    
    /** The {@code First Mode} name */
    protected String firstName_;
    
    /** The foreground color of the scene */
    protected Color3f foregroundColor_;
    
    /** The {@code Last Mode} name */
    protected String lastName_;
    
    /** Quadrilaterals to render the outline of the cells */
    protected QuadArray linedQuads_;
    
    /** Lines to render the grid of the matrix */
    protected LineArray lines_;
    
    /** The lower tolerance for the cell color */
    protected double lowerTolerance_;
    
    /** The number of components in 3D space */
    protected static final int N_COMPONENTS = 3;
    
    /** The number of coordinate indices per quadrilateral */
    protected static final int N_INDICES = 12;
    
    /** The number of vertices of a quadrilateral */
    protected static final int N_VERTICES = 4;
    
    /** The number of columns or {@code Last Modes} */
    protected int nColumns_;
    
    /** The number of columns or {@code First Modes} */
    protected int nRows_;
    
    /** The scale of the scene */
    protected double scale_;
    
    /** The upper tolerance for the cell color */
    protected double upperTolerance_;
    
    /** The values of the {@code ModeCorrelationMatrix} */
    protected double[] values_;
    
    
    
    /***************************************************************************
     * Constructs a {@code ModeCorrelationScene2D} from the given parameters,
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
    public ModeCorrelationScene2D(ModeCorrelationMatrix correlation,
            double scale, Color3f backgroundColor, double lowerTolerance,
            double upperTolerance, String firstName, String lastName) {
        super();
        
        double[]            values  = correlation.values();
        nRows_                      = correlation.getRowCount();
        nColumns_                   = correlation.getColumnCount();
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
            values_         = new double[values.length];
            filledQuads_    = new QuadArray(N_VERTICES * nRows_ * nColumns_,
                    QuadArray.COORDINATES | QuadArray.COLOR_3);
            linedQuads_     = new QuadArray(N_VERTICES * nRows_ * nColumns_,
                    QuadArray.COORDINATES);
            lines_          = new LineArray(2 * ((nRows_ + nColumns_ + 2)),
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
     * Each cell is given a unit size and then the coordinates are scaled.
     **************************************************************************/
    protected void installLineCoordinates() {
        double[]    coordinates =
                new double[2 * N_COMPONENTS * ((nRows_ + nColumns_ + 2))];
        double      xLast;
        double      yLast;
        double      zLast;
        double      xNext;
        double      yNext;
        double      zNext;
        
        for (int i = 0; i < nRows_ + 1; i++) {
            xLast = 0.0;
            yLast = scale_ * (-i);
            zLast = 0.0;
            xNext = scale_ * nColumns_;
            yNext = scale_ * (-i);
            zNext = 0.0;
            
            coordinates[(i * 2 * N_COMPONENTS) + 0] = xLast;
            coordinates[(i * 2 * N_COMPONENTS) + 1] = yLast;
            coordinates[(i * 2 * N_COMPONENTS) + 2] = zLast;
            coordinates[(i * 2 * N_COMPONENTS) + 3] = xNext;
            coordinates[(i * 2 * N_COMPONENTS) + 4] = yNext;
            coordinates[(i * 2 * N_COMPONENTS) + 5] = zNext;
        }
        
        for (int i = 0; i < nColumns_ + 1; i++) {
            xLast = scale_ * i;
            yLast = 0.0;
            zLast = 0.0;
            xNext = scale_ * i;
            yNext = scale_ * (-nRows_);
            zNext = 0.0;
            
            coordinates[2 * N_COMPONENTS * (i + nRows_ + 1) + 0] = xLast;
            coordinates[2 * N_COMPONENTS * (i + nRows_ + 1) + 1] = yLast;
            coordinates[2 * N_COMPONENTS * (i + nRows_ + 1) + 2] = zLast;
            coordinates[2 * N_COMPONENTS * (i + nRows_ + 1) + 3] = xNext;
            coordinates[2 * N_COMPONENTS * (i + nRows_ + 1) + 4] = yNext;
            coordinates[2 * N_COMPONENTS * (i + nRows_ + 1) + 5] = zNext;
        }
        
        lines_.setCoordinates(0, coordinates);
    } // eom
    
    
    
    /***************************************************************************
     * Installs the quadrilateral colors of the cell faces.  Cell values less
     * than the lower tolerance are set as green, those greater than the upper
     * tolerance are set as red, and those in between are set as yellow.
     **************************************************************************/
    protected void installQuadColors() {
        Color3f[] colors = new Color3f[N_VERTICES * nRows_ * nColumns_];
        
        for (int i = 0; i < values_.length; i++) {
            if (values_[i] < lowerTolerance_) {
                for (int j = 0; j < N_VERTICES; j++) {
                    colors[(i * N_VERTICES) + j] = ColorConstants.GREEN_COLOR;
                }
            }
            else if (values_[i] < upperTolerance_) {
                for (int j = 0; j < N_VERTICES; j++) {
                    colors[(i * N_VERTICES) + j] = ColorConstants.YELLOW_COLOR;
                }
            }
            else {
                for (int j = 0; j < N_VERTICES; j++) {
                    colors[(i * N_VERTICES) + j] = ColorConstants.RED_COLOR;
                }
            }
        }
        
        filledQuads_.setColors(0, colors);
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
        
        for (int i = 0; i < nRows_; i++) {
            for (int j = 0; j < nColumns_; j++) {
                xLast = scale_ * (j + offset);
                yLast = scale_ * (-i - offset);
                zLast = 0.0;
                xNext = scale_ * (j + 1 - offset);
                yNext = scale_ * (-i - 1 + offset);
                
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
            }
        }
        
        filledQuads_.setCoordinates(0, coordinates);
        linedQuads_.setCoordinates(0, coordinates);
    } // eom
    
    
    
    /***************************************************************************
     * Installs the grid coordinates, the cell face coordinates and colors,
     * the text for the labels, and adds these shapes to this scene graph.
     **************************************************************************/
    protected void installScene() {
        this.installLineCoordinates();
        this.installQuadColors();
        this.installQuadCoordinates();
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
        
        polygonAttributes = new PolygonAttributes();
        polygonAttributes.setBackFaceNormalFlip(true);
        polygonAttributes.setCullFace(PolygonAttributes.CULL_NONE);
        
        appearance = new Appearance();
        appearance.setPolygonAttributes(polygonAttributes);
        
        shape3D = new Shape3D(filledQuads_, appearance);
        this.addChild(shape3D);
        
        coloringAttributes = new ColoringAttributes();
        coloringAttributes.setColor(ColorConstants.BLACK_COLOR);
        
        polygonAttributes = new PolygonAttributes();
        polygonAttributes.setPolygonMode(PolygonAttributes.POLYGON_LINE);
        
        appearance = new Appearance();
        appearance.setColoringAttributes(coloringAttributes);
        appearance.setPolygonAttributes(polygonAttributes);
        
        shape3D = new Shape3D(linedQuads_, appearance);
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
     * {@code Last Mode} and the name and frequency of each individual mode.
     * Transform groups are applied to each text shape in order to easily read
     * the text in the scene.  Since there is no text array in Java3D, each
     * shape is added directly to this scene graph.
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
        Transform3D         transform3D;
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
        transform3D     = new Transform3D();
        
        transform3D.setScale(scale_);
        transformGroup = new TransformGroup(transform3D);
        
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
        
        transform3D = new Transform3D();
        transform3D.rotZ(Math.PI / 2.0);
        transform3D.setScale(scale_);
        transformGroup = new TransformGroup(transform3D);
        
        for (int i = 0; i < nColumns_; i++) {
            point3f = new Point3f(0.0f, (float) -(i + 1), 0.0f);
            text    = String.format(" %2s: %6.2f Hz", lastNames[i],
                    lastFrequencies[i]);
            
            text3D = new Text3D(font3D, text, point3f);
            text3D.setAlignment(Text3D.ALIGN_FIRST);
            
            shape3D = new Shape3D(text3D, appearance);
            transformGroup.addChild(shape3D);
        }
        
        this.addChild(transformGroup);
        
        transform3D = new Transform3D();
        transform3D.rotZ(Math.PI / 2.0);
        transform3D.setScale(scale_);
        transformGroup = new TransformGroup(transform3D);
        
        point3f = new Point3f((float) -(nRows_ / 2.0f), (float) 8.0f, 0.0f);
        text    = String.format("%s", firstName_);
        text3D  = new Text3D(font3D, text, point3f);
        text3D.setAlignment(Text3D.ALIGN_CENTER);
        shape3D = new Shape3D(text3D, appearance);
        transformGroup.addChild(shape3D);
        this.addChild(transformGroup);
        
        transform3D = new Transform3D();
        transform3D.setScale(scale_);
        transformGroup = new TransformGroup(transform3D);
        
        point3f = new Point3f((float) nColumns_ / 2.0f, 8.0f, 0.0f);
        text    = String.format("%s", lastName_);
        text3D  = new Text3D(font3D, text, point3f);
        text3D.setAlignment(Text3D.ALIGN_CENTER);
        shape3D = new Shape3D(text3D, appearance);
        transformGroup.addChild(shape3D);
        this.addChild(transformGroup);
    } // eom
} // eoc