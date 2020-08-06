package org.shapelang;

import javafx.scene.layout.GridPane;

//This class is probably obsolete now.
public class CanvasPane extends GridPane
{
    //private final Canvas canvas;
   // private final ArrayList<Pane> cells = new ArrayList<Pane>();
    private final int numCols;
    private final int numRows;

    public CanvasPane(int width, int height)
    {
        super();
        numCols = width;
        numRows = height;
    }

    public double getCellWidth()
    {
        return getScene().getWindow().getWidth()/numCols;
    }

    public double getCellHeight()
    {
        return getScene().getWindow().getHeight()/numRows;
    }
}
