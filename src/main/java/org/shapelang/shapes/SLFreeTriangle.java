package org.shapelang.shapes;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

/**
 * A triangle whose shape is defined by explicitly stating the display coordinates of its three vertices.
 * This allows for more irregularly-shaped triangles to be constructed than by using the SLRegularTriangle.
 *
 * @author Daniel Burton
 */
public class SLFreeTriangle extends Polygon implements Shape
{
    private Double[] points;

    /**
     * Standard constructor to create an SLFreeTriangle using three sets of xy coordinates.
     * @param x1 - x-coordinate of first vertex.
     * @param y1 - y-coordinate of first vertex.
     * @param x2 - x-coordinate of second vertex.
     * @param y2 - y-coordinate of second vertex.
     * @param x3 - x-coordinate of third vertex.
     * @param y3 - y-coordinate of third vertex.
     */
    public SLFreeTriangle(double x1, double y1,  double x2, double y2, double x3, double y3)
    {
        super();
        points = new Double[]{x1, y1, x2, y2, x3, y3};
    }

    /**
     * Enlarges an existing SLFreeTriangle by a provided scale factor (can be <1 for reduction in size).
     * @param scaleFactor - factor by which the SLFreeTriangle should be enlarged.
     */
    @Override
    public void resize(double scaleFactor)
    {
        //A ScaleTransition is used because it's the only way to get JavaFX to do the heavy lifting here.
        //The time limit is short enough such that the transition itself is barely distinguishable from
        //a standard resize.
        ScaleTransition st = new ScaleTransition(Duration.seconds(0.01), this);
        st.setByX(scaleFactor);
        st.setByY(scaleFactor);
        st.play();
    }

    /**
     * Animated resize over a given time frame.
     * @param scaleFactor - factor by which the SLFreeTriangle should be enlarged.
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
     * Rotates an existing SLFreeTriangle by a provided degree count.
     * @param degrees - number of degrees by which the SLFreeTriangle should be rotated.
     */
    @Override
    public void rotate(double degrees)
    {
        setRotate(degrees);
    }

    /**
     * Animated rotation over a given time frame.
     * @param degrees - number of degrees by which the SLFreeTriangle should be rotated.
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
     * Moves the centre of an existing SLFreeTriangle to a given set of coordinates.
     * Should NOT be used for first-time placement - use place() for this.
     * @param x - new x-coordinate for the centre of the SLFreeTriangle.
     * @param y - new y-coordinate for the centre of the SLFreeTriangle.
     */
    @Override
    public void place(int x, int y)
    {
        getPoints().clear();
        getPoints().addAll(points);
        double centreX = (getPoints().get(0) + getPoints().get(2) + getPoints().get(4))/3;
        double centreY = (getPoints().get(1) + getPoints().get(3) + getPoints().get(5))/3;

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.01), this);
        tt.setToX(x - centreX);
        tt.setToY(y - centreY);
        tt.play();

        tt.setOnFinished(e ->
        {
            Double[] newPoints = new Double[6];
            for(int i = 0; i < 6; i++)
            {
                newPoints[i] = (Double)getPoints().get(i);
            }
            points = newPoints;
        });
    }

    /**
     * Used for first-time placement of the shape using its known vertex coordinates.
     * To relocate using new centre coordinates, use place(int x, int y).
     */
    public void place()
    {
        getPoints().addAll(points);
    }

    /**
     * Animated centre relocation over a given time frame.
     * @param x - new x-coordinate for the centre of the SLFreeTriangle.
     * @param y - new y-coordinate for the centre of the SLFreeTriangle.
     * @param timePeriod - in seconds, the amount of time the animation should take.
     */
    @Override
    public void moveTransition(int x, int y, float timePeriod)
    {
        double centreX = (getPoints().get(0) + getPoints().get(2) + getPoints().get(4))/3;
        double centreY = (getPoints().get(1) + getPoints().get(3) + getPoints().get(5))/3;

        //A TranslateTransition is used because it's the only way to get JavaFX to do the heavy lifting here.
        //The time limit is short enough such that the transition itself is barely distinguishable from
        //a standard resize.
        TranslateTransition tt = new TranslateTransition(Duration.seconds(timePeriod), this);
        tt.setToX(x - centreX);
        tt.setToY(y - centreY);
        tt.play();

        tt.setOnFinished(e ->
        {
            Double[] newPoints = new Double[6];
            for(int i = 0; i < 6; i++)
            {
                newPoints[i] = (Double)getPoints().get(i);
            }
            points = newPoints;
        });
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
}
