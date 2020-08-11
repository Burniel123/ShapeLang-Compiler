package org.shapelang;

import javafx.scene.Group;
import javafx.scene.Node;

public class Background extends Group
{
    public Background()
    {
        super();
    }

    public void addObjectToScreen(Node node)
    {
        getChildren().add(node);
    }

    public void removeObject(Node node)
    {
        getChildren().remove(node);
    }
}
