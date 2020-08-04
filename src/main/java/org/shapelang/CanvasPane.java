package org.shapelang;

import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;

public class CanvasPane extends Pane
{
    private final Canvas canvas;

    public CanvasPane(double width, double height)
    {
        canvas = new Canvas(width, height);
        setWidth(width);
        setWidth(height);

        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        getChildren().add(canvas);
    }
}
