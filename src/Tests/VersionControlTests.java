package Tests;

import Controller.Controller;
import Models.Action;
import Models.Model;
import Models.Rectangle;
import Models.Shape;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;
import java.io.*;

import static org.junit.Assert.fail;

/**
 * These are JUnit tests for the model's version control features.
 * How to save and load a serializable class in a file obtained from: https://www.geeksforgeeks.org/serialization-in-java/ as mentioned in Controller
 */
public class VersionControlTests {

    private Model model;
    private Controller controller;

    /**
     * This is run before the tests to set up the components.
     */
    @Before
    public void setup() {
        model = new Model();
        controller = new Controller(model);
    }

    /**
     * This test saves a model to a file and then loads it and checks whether it is the same model.
     */
    @Test
    public void saveAndLoad() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        String testFile =  System.getProperty("user.dir")+"/testModel.ser";
        try
            {
                FileOutputStream file = new FileOutputStream(testFile);
                ObjectOutputStream out = new ObjectOutputStream(file);

                out.writeObject(model);

                out.close();
                file.close();

                FileInputStream fileOut = new FileInputStream(testFile);
                ObjectInputStream in = new ObjectInputStream(fileOut);

                Model loadModel = (Model)in.readObject();

                for(Shape n:model.getShapes()){
                    for(Shape m:loadModel.getShapes())
                        if (!n.equalSize(m) || !n.equalPosition(m) || !n.equalStroke(m) || !n.equalColor(m))
                            fail("Error saving and loading");
                }

                in.close();
                fileOut.close();

            }
            catch(IOException | ClassNotFoundException ex)
            {
                fail("Error saving and loading!");
            }
    }

    /**
     * This test saves a model to a file and then loads it and checks whether it is the same model.
     */
    @Test
    public void clear() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, true);
        model.clear();
        if(model.getShapes().size() != 0 || model.getActions().size() != 0)
            fail("Error clearing canvas!");
    }
}
//2