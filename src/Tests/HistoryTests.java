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

import static org.junit.Assert.*;

/**
 * These are JUnit tests for the model's history features.
 */
public class HistoryTests {

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
     * This test creates a shape and then un-does it and checks whether it was successfully removed.
     */
    @Test
    public void undoCreate() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, false);

        controller.undo();

        assertEquals(model.getShapes().size(), 0);
        assertEquals(model.getActions().size(), 0);

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, null, controller.getSelectedColor(), "Rectangle");

        if(!r.equalColor(model.getLastChange()) || !r.equalPosition(model.getLastChange()) || !r.equalSize(model.getLastChange()) || !r.equalStroke(model.getLastChange()))
            fail("Last change in model is wrong!");
    }

    /**
     * This test creates a shape and edits it and then un-does the edit and checks whether it was successfully reversed.
     */
    @Test
    public void undoEdit() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.updateModel(new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle"), selectedShape, 1);

        controller.undo();

        assertEquals(model.getShapes().size(), 1);
        assertEquals(model.getActions().size(), 1);

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle");

        if(!r.equalColor(model.getLastChange()) || !r.equalPosition(model.getLastChange()) || !r.equalSize(model.getLastChange()) || !r.equalStroke(model.getLastChange()))
            fail("Last change in model is wrong!");

    }

    /**
     * This test creates a shape and then deletes it and then un-does it and checks whether it was successfully recreated.
     */
    @Test
    public void undoDelete() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.delete(selectedShape);
        controller.updateModel(selectedShape, null, 4);

        controller.undo();

        assertEquals(model.getShapes().size(), 1);
        assertEquals(model.getActions().size(), 1);

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle");

        if(!r.equalColor(model.getLastChange()) || !r.equalPosition(model.getLastChange()) || !r.equalSize(model.getLastChange()) || !r.equalStroke(model.getLastChange()))
            fail("Last change in model is wrong!");

    }

    /**
     * This test creates a shape and then edits it and then deletes it, and then un-does each step and checks whether it was handled successfully.
     */
    @Test
    public void undoCombo() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        Shape selectedShape = controller.selectShape(20, 20);

        controller.updateModel(new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle"), selectedShape, 1);

        selectedShape = controller.selectShape(20, 20);

        controller.delete(selectedShape);
        controller.updateModel(selectedShape, null, 4);

        //Undo delete
        controller.undo();

        assertEquals(model.getShapes().size(), 1);
        assertEquals(model.getActions().size(), 3);

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle");

        if(!r.equalColor(model.getLastChange()) || !r.equalPosition(model.getLastChange()) || !r.equalSize(model.getLastChange()) || !r.equalStroke(model.getLastChange()))
            fail("Last change in model is wrong!");

        //Undo edit
        controller.undo();

        assertEquals(model.getShapes().size(), 1);
        assertEquals(model.getActions().size(), 1);

        if(!r.equalColor(model.getLastChange()) || !r.equalPosition(model.getLastChange()) || !r.equalSize(model.getLastChange()) || !r.equalStroke(model.getLastChange()))
            fail("Last change in model is wrong!");

        //Undo create
        controller.undo();

        assertEquals(model.getShapes().size(), 0);
        assertEquals(model.getActions().size(), 0);

        if(!r.equalColor(model.getLastChange()) || !r.equalPosition(model.getLastChange()) || !r.equalSize(model.getLastChange()) || !r.equalStroke(model.getLastChange()))
            fail("Last change in model is wrong!");

    }

    /**
     * This test creates a shape and then un-does it and then re-does it and checks whether it was successfully recreated.
     */
    @Test
    public void redoCreate() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, false);

        controller.undo();
        controller.redo();

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, null, controller.getSelectedColor(), "Rectangle");

        assertEquals(model.getShapes().size(), 1);
        assertNull(model.getLastChange());

        if(!r.equalColor(model.getShapes().get(0)) || !r.equalPosition(model.getShapes().get(0)) || !r.equalSize(model.getShapes().get(0)) || !r.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !r.equalColor(((Action)model.getActions().peek()).getS()) || !r.equalPosition(((Action)model.getActions().peek()).getS()) || !r.equalSize(((Action)model.getActions().peek()).getS()) || !r.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a shape and edits it then un-does it and re-does it and checks whether the edit was successfully redone.
     */
    @Test
    public void redoEdit() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.updateModel(new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle"), selectedShape, 1);

        controller.undo();
        controller.redo();

        assertEquals(model.getShapes().size(), 1);
        assertEquals(model.getActions().size(), 3);
        assertNull(model.getLastChange());

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle");

        if(!r.equalColor(model.getShapes().get(0)) || !r.equalPosition(model.getShapes().get(0)) || !r.equalSize(model.getShapes().get(0)) || !r.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='e' || !r.equalColor(((Action)model.getActions().peek()).getS()) || !r.equalPosition(((Action)model.getActions().peek()).getS()) || !r.equalSize(((Action)model.getActions().peek()).getS()) || !r.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a shape and deletes it then un-does it and re-does it and checks whether the shape was successfully deleted.
     */
    @Test
    public void redoDelete() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.delete(selectedShape);
        controller.updateModel(selectedShape, null, 4);

        controller.undo();
        controller.redo();

        assertEquals(model.getShapes().size(), 0);
        assertEquals(model.getActions().size(), 2);
        assertNull(model.getLastChange());

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle");

        if(((Action)model.getActions().peek()).getType() !='d' || !r.equalColor(((Action)model.getActions().peek()).getS()) || !r.equalPosition(((Action)model.getActions().peek()).getS()) || !r.equalSize(((Action)model.getActions().peek()).getS()) || !r.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a shape, then edits it, then deletes it, then un-does all that, then re-does it step by step and checks if each step is successfully handled.
     */
    @Test
    public void redoCombo() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.updateModel(new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle"), selectedShape, 1);

        selectedShape = controller.selectShape(20, 20);
        controller.delete(selectedShape);
        controller.updateModel(selectedShape, null, 4);

        controller.undo();
        controller.undo();
        controller.undo();

        //Redo create
        controller.redo();

        assertEquals(model.getShapes().size(), 1);
        assertEquals(model.getActions().size(), 1);

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle");

        if(((Action)model.getActions().peek()).getType() !='c' || !r.equalColor(((Action)model.getActions().peek()).getS()) || !r.equalPosition(((Action)model.getActions().peek()).getS()) || !r.equalSize(((Action)model.getActions().peek()).getS()) || !r.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");

        //Redo edit
        controller.redo();

        assertEquals(model.getShapes().size(), 1);
        assertEquals(model.getActions().size(), 3);

        if(!r.equalColor(model.getShapes().get(0)) || !r.equalPosition(model.getShapes().get(0)) || !r.equalSize(model.getShapes().get(0)) || !r.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='e' || !r.equalColor(((Action)model.getActions().peek()).getS()) || !r.equalPosition(((Action)model.getActions().peek()).getS()) || !r.equalSize(((Action)model.getActions().peek()).getS()) || !r.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");

        //Redo delete
        controller.redo();

        assertEquals(model.getShapes().size(), 0);
        assertNull(model.getLastChange());

        if(!r.equalColor(((Action)model.getActions().peek()).getS()) || !r.equalPosition(((Action)model.getActions().peek()).getS()) || !r.equalSize(((Action)model.getActions().peek()).getS()) || !r.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

}
//8