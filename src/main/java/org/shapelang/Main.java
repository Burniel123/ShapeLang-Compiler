package org.shapelang;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.shapelang.shapes.SLFreeTriangle;
import org.shapelang.shapes.SLRegularTriangle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for the ShapeLang compiler.
 *
 * @author Daniel Burton & Michael Hallam
 */
public class Main extends Application
{

    /**
     * Here's what will actually happen here:
     * - Methods will be referenced which parse/check etc. in a separate thread.
     * (This may actually be done from an init() method now I think about it).
     * - A background of the size requested will be created and the scene/stage will be loaded.
     * (As soon as it knows what the requested size is).
     * - Objects will be created and edited as the supplied code is parsed.
     * - A method in the Background class exists to manage the addition/removal of these objects.
     *
     * Here's what's happening here for now:
     * - Basically just a testing ground.
     */
    @Override
    public void start(Stage stage)
    {
        SLFreeTriangle ft = new SLFreeTriangle(10,10,100,100,100,200);
        //ft.place(200,200);
        SLRegularTriangle rt = new SLRegularTriangle(20,20);
        rt.place(100,100);
        rt.resize(10);

        //SLCircle circle = new SLCircle(20);
        //circle.place(300,300);
        Background bg = new Background();
        bg.addObjectToScreen(rt);
        //grp.setMinSize(640, 480);
        var scene = new Scene(bg, 640, 480);
        stage.setScene(scene);
        stage.show();
        ft.resizeTransition(2,2);
    }

    @Override
    public void init()
    {
        List<String> params = getParameters().getRaw();

        if(params.size() != 1)
            //TODO: Handle this.
            System.err.println("Please provide a valid file.");

        StringBuilder fileContents = new StringBuilder();
        String line = null;
        try
        {
            File toCompile = new File(params.get(0));
            BufferedReader reader = new BufferedReader(new FileReader(toCompile));
            while((line = reader.readLine()) != null)
            {
                fileContents.append(line);
            }
        }
        catch(IOException e)
        {
            System.err.println("Unable to read supplied file. Please check you have supplied a valid, existing .txt file.");
            Platform.exit();
        }
    }

    //LEAVE THIS ALONE!
    public static void main(String[] args)
    {
        launch(args);
    }

}