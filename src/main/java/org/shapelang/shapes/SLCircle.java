package org.shapelang.shapes;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class SLCircle extends Circle implements Shape<Integer>
{
    public SLCircle(int radius)
    {
        super(radius);
    }

    @Override
    public void resize(Integer newSize)
    {
        setRadius(newSize);
    }

    @Override
    public void resizeTransition(Integer newSize, float timePeriod)
    {
        ScaleTransition st = new ScaleTransition(Duration.seconds(timePeriod), this);
        st.setByX(newSize);
        st.setByY(newSize);
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
        rt.setByAngle(180);
        rt.play();
    }

    @Override
    public void place(int x, int y)
    {
        setCenterX(x);
        setCenterY(y);
    }

    @Override
    public void moveTransition(int x, int y, float timePeriod)
    {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(timePeriod), this);
        tt.setToX(x);
        tt.setToY(y);
        tt.play();
    }

    @Override
    public void setColor(Color color)
    {
        setFill(color);
    }
}
