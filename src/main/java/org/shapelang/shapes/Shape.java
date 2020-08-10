package org.shapelang.shapes;

import javafx.scene.paint.Color;

/**
 * Defines signatures for transformations which all "shapes" must be capable of.
 * To be implemented by all "shape" classes.
 *
 * @author Daniel Burton
 */
public interface Shape
{
    //Resize immediately using the using a scale factor:
    void resize(double scaleFactor);

    //Animated resize over a given time period.
    void resizeTransition(double scaleFactor, float timePeriod);

    //Rotate shape immediately about its centre by the number of degrees specified.
    //I'm assuming we'll be using deg over rad - probably a float either way.
    void rotate(double degrees);

    //Animated rotation over a given time period.
    void rotateTransition(double degrees, float timePeriod);

    //Places the shape on the user-defined grid at (x,y). Can be used for first placement or immediate movement.
    //Integer coords used as user defines their own grid so if they need more precision that's on them.
    void place(int x, int y);

    //Animated movement over a given time period.
    //Currently only supports a single, straight movement.
    void moveTransition(int x, int y, float timePeriod);

    //Sets or changes the color of a shape.
    void setColor(Color color);
}
