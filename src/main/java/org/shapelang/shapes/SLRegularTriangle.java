package org.shapelang.shapes;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

/**
 * A triangle whose shape is defined by its base and height and positioned by the (x,y) coordinates of its centre.
 * By default, the triangle is isosceles and is placed pointing up.
 * To create less regular triangles, use SLFreeTriangle.
 *
 * @author Daniel Burton
 */
public class SLRegularTriangle extends Polygon implements Shape
{
    private double baseLen = 0;
    private double height = 0;
    private int centreX = 0;
    private int centreY = 0;

    /**
     * Standard constructor to create an SLRegularTriangle using its base and height.
     * @param baseLen - length of this SLRegularTriangle's base, in pixels.
     * @param height - height of this SLRegularTriangle, in pixels.
     */
    public SLRegularTriangle(double baseLen, double height)
    {
        super();
        this.baseLen = baseLen;
        this.height = height;
    }

    /**
     * Enlarges an existing SLRegularTriangle by a provided scale factor (can be <1 for reduction in size).
     * @param scaleFactor - factor by which the SLRegularTriangle should be enlarged.
     */
    @Override
    public void resize(double scaleFactor)
    {
        setScaleX(scaleFactor);
        setScaleY(scaleFactor);
    }

    /**
     * Animated resize over a given time frame.
     * @param scaleFactor - factor by which the SLRegularTriangle should be enlarged.
     * @param timePeriod - in seconds, the amount of time the animation should take.
     */
    @Override
    public void resizeTransition(double scaleFactor, float timePeriod)
    {
        ScaleTransition st = new ScaleTransition(Duration.seconds(timePeriod), this);
        st.setByX(scaleFactor);
        st.setByY(scaleFactor);
        st.play();
    }

    /**
     * Rotates an existing SLRegularTriangle by a provided degree count.
     * @param degrees - number of degrees by which the SLRegularTriangle should be rotated.
     */
    @Override
    public void rotate(double degrees)
    {
        setRotate(degrees);
    }

    /**
     * Animated rotation over a given time frame.
     * @param degrees - number of degrees by which the SLRegularTriangle should be rotated.
     * @param timePeriod - in seconds, the amount of time the animation should take.
     */
    @Override
    public void rotateTransition(double degrees, float timePeriod)
    {
        RotateTransition rt = new RotateTransition(Duration.seconds(timePeriod), this);
        rt.setByAngle(degrees);
        rt.play();
    }

    /**
     * Moves the centre of an existing SLRegularTriangle to a given set of coordinates.
     * Can be used for first-time placement or immediate change of location.
     * @param x - new x-coordinate for the centre of the SLRegularTriangle.
     * @param y - new y-coordinate for the centre of the SLRegularTriangle.
     */
    @Override
    public void place(int x, int y)
    {
        centreX = x;
        centreY = y;

        getPoints().clear();
        double topPointY = y - (height/2);
        double leftPointX = x - (baseLen/2);
        double rightPointX = x + (baseLen/2);
        double lowerPointsY = y + (height/2);

        getPoints().addAll(leftPointX, lowerPointsY, rightPointX, lowerPointsY, (double)x, topPointY);
    }

    /**
     * Animated centre relocation over a given time frame.
     * @param x - new x-coordinate for the centre of the SLRegularTriangle.
     * @param y - new y-coordinate for the centre of the SLRegularTriangle.
     * @param timePeriod - in seconds, the amount of time the animation should take.
     */
    @Override
    public void moveTransition(int x, int y, float timePeriod)
    {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(timePeriod), this);

        tt.setToX(x - centreX);
        tt.setToY(y - centreY);
        tt.play();
    }

    /**
     * Changes the color of an existing SLRegularTriangle.
     * @param color - JavaFX Color to set as the shape's new color.
     */
    @Override
    public void setColor(Color color)
    {
        setFill(color);
    }
}
