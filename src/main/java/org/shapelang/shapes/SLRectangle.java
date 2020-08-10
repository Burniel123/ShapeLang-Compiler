package org.shapelang.shapes;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.shapelang.common.Twople;

public class SLRectangle extends Rectangle implements Shape
{
    public SLRectangle(double width, double height)
    {
        super(width, height);
    }

    @Override
    public void resize(double scaleFactor)
    {
        setWidth(getWidth() * scaleFactor);
        setHeight(getHeight() * scaleFactor);
    }

    @Override
    public void resizeTransition(double scaleFactor, float timePeriod)
    {
        ScaleTransition st = new ScaleTransition(Duration.seconds(timePeriod), this);
        st.setByX(scaleFactor);
        st.setByY(scaleFactor);
        st.play();
    }

    @Override
    public void rotate(double degrees)
    {
        setRotate(degrees);
    }

    @Override
    public void rotateTransition(double degrees, float timePeriod)
    {
        RotateTransition rt = new RotateTransition(Duration.seconds(timePeriod), this);
        rt.setByAngle(degrees);
        rt.play();
    }

    @Override
    public void place(int x, int y)
    {
        double diffX = getWidth()/2;
        double diffY = getHeight()/2;

        setX(x - diffX);
        setY(y - diffY);
    }

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

    @Override
    public void setColor(Color color)
    {
        setFill(color);
    }
}
