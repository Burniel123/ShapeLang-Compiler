package org.shapelang.shapes;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.shapelang.common.parsercom.TokeniseException;

/**
 * A basic circle, defined by its radius and positioned by (x,y) coordinates for its centre.
 *
 * @author Daniel Burton
 */
public class SLCircle extends Circle implements Shape
{
    /**
     * Standard constructor to create an SLCircle using its radius.
     * @param radius - the radius of this SLCircle, in pixels.
     */
    public SLCircle(double radius)
    {
        super(radius);
    }

    /**
     * Enlarges an existing SLCircle by a provided scale factor (can be <1 for reduction in size).
     * @param scaleFactor - factor by which the SLCircle should be enlarged.
     */
    @Override
    public void resize(double scaleFactor)
    {
        setRadius(getRadius() * scaleFactor);
    }

    /**
     * Animated resize over a given time frame.
     * @param scaleFactor - factor by which the SLCircle should be enlarged.
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
     * Rotates an existing SLCircle by a provided degree count.
     * Although a standard circle looks identical when rotated, this exists to support future
     * potential options such as patterned/textured shape fills, etc.
     * @param degrees - number of degrees by which the SLCircle should be rotated.
     */
    @Override
    public void rotate(double degrees)
    {
        setRotate(degrees);
    }

    /**
     * Animated rotation over a given time frame.
     * @param degrees - number of degrees by which the SLCircle should be rotated.
     * @param timePeriod - in seconds, the amount of time the animation should take.
     */
    @Override
    public void rotateTransition(double degrees, float timePeriod)
    {
        RotateTransition rt = new RotateTransition(Duration.seconds(timePeriod), this);
        rt.setByAngle(180);
        rt.play();
    }

    /**
     * Moves the centre of an existing SLCircle to a given set of coordinates.
     * Can be used for first-time placement or immediate change of location.
     * @param x - new x-coordinate for the centre of the SLCircle.
     * @param y - new y-coordinate for the centre of the SLCircle.
     */
    @Override
    public void place(int x, int y)
    {
        setCenterX(x);
        setCenterY(y);
    }

    /**
     * Animated centre relocation over a given time frame.
     * @param x - new x-coordinate for the centre of the SLCircle.
     * @param y - new y-coordinate for the centre of the SLCircle.
     * @param timePeriod - in seconds, the amount of time the animation should take.
     */
    @Override
    public void moveTransition(int x, int y, float timePeriod)
    {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(timePeriod), this);
        tt.setToX(x - getCenterX());
        tt.setToY(y - getCenterY());
        tt.play();
    }

    /**
     * Changes the color of an existing SLFreeTriangle.
     * @param color - JavaFX Color to set as the shape's new color.
     */
    @Override
    public void setColor(Color color)
    {
        setFill(color);
    }

    /**
     * By @p2titus
     * Maps String -> SLCircle by parsing size
     * @param line - text to parse
     */
    public static SLCircle parseSize(String[] line) throws TokeniseException{
        if(!"put".equals(line[0]) || !"circle".equals(line[1]))
            throw new TokeniseException("WRONG SHAPE IN PARSER");
        else {
            final double radius = getRadius(line);
            return new SLCircle(radius);
        }
    }

    private static double getRadius(String line[]) throws TokeniseException {
        boolean isNext = false;
        final int radius;
        for(String word: line) {
            if(isNext)
                return parseSize(word);
            else switch(word) {
                case "radius":
                    isNext = true;
                    break;
                default:
                    break;
            }
        }
        throw new TokeniseException("ERROR: Size not found for circle");
    }

    private static double parseSize(String word) {
        return Double.parseDouble(word);
    }
}
