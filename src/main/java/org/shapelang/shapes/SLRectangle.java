package org.shapelang.shapes;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * A basic rectangle, defined by its width and height, and positioned by (x,y) coordinates for its centre.
 *
 * @author Daniel Burton
 */
public class SLRectangle extends Rectangle implements Shape
{
    /**
     * Standard constructor to create an SLRectangle using its width and height.
     * @param width - the width of this SLRectangle, in pixels.
     * @param height - the height of this SLRectangle, in pixels.
     */
    public SLRectangle(double width, double height)
    {
        super(width, height);
    }

    /**
     * Enlarges an existing SLRectangle by a provided scale factor (can be <1 for reduction in size).
     * @param scaleFactor - factor by which the SLRectangle should be enlarged.
     */
    @Override
    public void resize(double scaleFactor)
    {
        setWidth(getWidth() * scaleFactor);
        setHeight(getHeight() * scaleFactor);
    }

    /**
     * Animated resize over a given time frame.
     * @param scaleFactor - factor by which the SLRectangle should be enlarged.
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
     * Rotates an existing SLRectangle by a provided degree count.
     * @param degrees - number of degrees by which the SLRectangle should be rotated.
     */
    @Override
    public void rotate(double degrees)
    {
        setRotate(degrees);
    }

    /**
     * Animated rotation over a given time frame.
     * @param degrees - number of degrees by which the SLRectangle should be rotated.
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
     * Moves the centre of an existing SLRectangle to a given set of coordinates.
     * Can be used for first-time placement or immediate change of location.
     * @param x - new x-coordinate for the centre of the SLRectangle.
     * @param y - new y-coordinate for the centre of the SLRectangle.
     */
    @Override
    public void place(int x, int y)
    {
        double diffX = getWidth()/2;
        double diffY = getHeight()/2;

        setX(x - diffX);
        setY(y - diffY);
    }

    /**
     * Animated centre relocation over a given time frame.
     * @param x - new x-coordinate for the centre of the SLRectangle.
     * @param y - new y-coordinate for the centre of the SLRectangle.
     * @param timePeriod - in seconds, the amount of time the animation should take.
     */
    @Override
    public void moveTransition(int x, int y, float timePeriod)
    {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(timePeriod), this);
        double diffX = getWidth()/2;
        double diffY = getHeight()/2;
        tt.setToX(x - diffX - getX());
        tt.setToY(y - diffY - getY());
        tt.play();
    }

    /**
     * Changes the color of an existing SLRectangle.
     * @param color - JavaFX Color to set as the shape's new color.
     */
    @Override
    public void setColor(Color color)
    {
        setFill(color);
    }
}
