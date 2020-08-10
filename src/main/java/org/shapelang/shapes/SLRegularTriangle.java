package org.shapelang.shapes;

import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;

public class SLRegularTriangle extends Polygon implements Shape
{
    private double baseLen = 0;
    private double height = 0;
    private int centreX = 0;
    private int centreY = 0;

    public SLRegularTriangle(double baseLen, double height)
    {
        super();
        this.baseLen = baseLen;
        this.height = height;
    }

    @Override
    public void resize(double scaleFactor)
    {
        setScaleX(scaleFactor);
        setScaleY(scaleFactor);
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
        centreX = x;
        centreY = y;

        getPoints().clear();
        double topPointY = y - (height/2);
        double leftPointX = x - (baseLen/2);
        double rightPointX = x + (baseLen/2);
        double lowerPointsY = y + (height/2);

        getPoints().addAll(leftPointX, lowerPointsY, rightPointX, lowerPointsY, (double)x, topPointY);
    }

    @Override
    public void moveTransition(int x, int y, float timePeriod)
    {
        TranslateTransition tt = new TranslateTransition(Duration.seconds(timePeriod), this);

        tt.setToX(x - centreX);
        tt.setToY(y - centreY);
        tt.play();
    }

    @Override
    public void setColor(Color color)
    {
        setFill(color);
    }
}