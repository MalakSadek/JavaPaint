package Tests;

import Controller.Controller;
import Models.Polygon;
import Models.Rectangle;
import Models.*;
import Models.Shape;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

import static org.junit.Assert.*;

/**
 * These are JUnit tests for the model's editing features.
 */
public class EditTests {

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
     * This test selects a shape in the model's shapes and checks that the correct shape was selected.
     */
    @Test
    public void selectShape() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        Rectangle r = (Rectangle) controller.selectShape(20, 20);

        if (!r.equalColor(model.getShapes().get(0)) || !r.equalPosition(model.getShapes().get(0)) || !r.equalSize(model.getShapes().get(0)) || !r.equalStroke(model.getShapes().get(0)))
            fail("Select shape is wrong");
    }

    /**
     * This test selects a outside all the shapes in the model's shapes and checks that there was no shape selected.
     */
    @Test
    public void deselectShape() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        Rectangle r = (Rectangle) controller.selectShape(70, 70);

        if (r != null)
            fail("Deselect shape is wrong");
    }

    /**
     * This test creates a shape and then changes its fill color and checks that the change successfully took place and was recorded.
     */
    @Test
    public void changeShapeColorFill() {
        model.clear();
        controller.chooseColor(Color.RED);
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.updateModel(new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle"), selectedShape, 1);

        if (model.getShapes().get(0).getFillColor() != Color.BLACK)
            fail("Change shape color is wrong");
        if (((Action) model.getActions().peek()).getS().getFillColor() != Color.BLACK || ((Action) model.getActions().peek()).getType() != 'e')
            fail("Storing change shape color in history is wrong");
    }

    /**
     * This test creates a shape and then changes its stroke color and checks that the change successfully took place and was recorded.
     */
    @Test
    public void changeShapeColorNoFill() {
        model.clear();
        controller.chooseColor(Color.RED);
        controller.rectangleTool(10, 50, 10, 50, 5, false);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.updateModel(new Rectangle(10, 10, 50, 50, 5, null, Color.BLACK, "Rectangle"), selectedShape, 1);

        if (model.getShapes().get(0).getStrokeColor() != Color.BLACK)
            fail("Change shape color is wrong");
        if (((Action) model.getActions().peek()).getS().getStrokeColor() != Color.BLACK || ((Action) model.getActions().peek()).getType() != 'e')
            fail("Storing change shape color in history is wrong");
    }

    /**
     * This test creates a shape and then changes its position and checks that the change successfully took place and was recorded.
     */
    @Test
    public void moveShape() {
        model.clear();
        controller.chooseColor(Color.BLACK);
        controller.rectangleTool(10, 50, 10, 50, 5, false);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.updateModel(new Rectangle(20, 20, 60, 60, 5, null, Color.BLACK, "Rectangle"), selectedShape, 1);

        if (model.getShapes().get(0).getStartY() != 20 || model.getShapes().get(0).getStartX() != 20 || model.getShapes().get(0).getEndX() != 60 || model.getShapes().get(0).getEndY() != 60)
            fail("Change shape position is wrong");
        if (((Action) model.getActions().peek()).getS().getStartY() != 20 || ((Action) model.getActions().peek()).getS().getStartX() != 20 || ((Action) model.getActions().peek()).getS().getEndX() != 60 || ((Action) model.getActions().peek()).getS().getEndY() != 60 || ((Action) model.getActions().peek()).getType() != 'e')
            fail("Storing change shape position in history is wrong");
    }

    /**
     * This test creates a shape and then changes its size and checks that the change successfully took place and was recorded.
     */
    @Test
    public void resizeShape() {
        model.clear();
        controller.chooseColor(Color.BLACK);
        controller.rectangleTool(10, 50, 10, 50, 5, false);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.updateModel(new Rectangle(10, 10, 60, 60, 5, null, Color.BLACK, "Rectangle"), selectedShape, 1);

        if (model.getShapes().get(0).getStartY() != 10 || model.getShapes().get(0).getStartX() != 10 || model.getShapes().get(0).getEndX() != 60 || model.getShapes().get(0).getEndY() != 60)
            fail("Change shape resize is wrong");
        if (((Action) model.getActions().peek()).getS().getStartY() != 10 || ((Action) model.getActions().peek()).getS().getStartX() != 10 || ((Action) model.getActions().peek()).getS().getEndX() != 60 || ((Action) model.getActions().peek()).getS().getEndY() != 60 || ((Action) model.getActions().peek()).getType() != 'e')
            fail("Storing change shape resize in history is wrong");
    }

    /**
     * This test creates a shape and then removes its fill and checks that the change successfully took place and was recorded.
     */
    @Test
    public void changeShapeFill() {
        model.clear();
        controller.chooseColor(Color.RED);
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.updateModel(new Rectangle(10, 10, 50, 50, 5, null, Color.RED, "Rectangle"), selectedShape, 1);

        if (model.getShapes().get(0).getFillColor() != null || model.getShapes().get(0).getStrokeColor() != Color.RED)
            fail("Change shape fill is wrong");
        if (((Action) model.getActions().peek()).getS().getFillColor() != null || ((Action) model.getActions().peek()).getS().getStrokeColor() != Color.RED || ((Action) model.getActions().peek()).getType() != 'e')
            fail("Storing change shape fill in history is wrong");
    }

    /**
     * This test creates a shape and then changes its stroke width and checks that the change successfully took place and was recorded.
     */
    @Test
    public void changeShapeStrokeWidth() {
        model.clear();
        controller.chooseColor(Color.RED);
        controller.rectangleTool(10, 50, 10, 50, 5, false);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.updateModel(new Rectangle(10, 10, 50, 50, 10, null, Color.RED, "Rectangle"), selectedShape, 1);

        if (model.getShapes().get(0).getStrokeWidth() != 10)
            fail("Change shape stroke width is wrong");
        if (((Action) model.getActions().peek()).getS().getStrokeWidth() != 10 || ((Action) model.getActions().peek()).getType() != 'e')
            fail("Storing change shape stroke width in history is wrong");
    }

    /**
     * This test creates a shape and then deletes it and checks that the change successfully took place and was recorded.
     */
    @Test
    public void deleteShape() {
        model.clear();
        controller.chooseColor(Color.RED);
        controller.rectangleTool(10, 50, 10, 50, 5, false);

        Shape selectedShape = controller.selectShape(20, 20);
        controller.delete(selectedShape);
        controller.updateModel(selectedShape, null, 4);

        for(Shape n:model.getShapes()){
            if (n.equalColor(selectedShape) && n.equalStroke(selectedShape) && n.equalPosition(selectedShape) && n.equalSize(selectedShape))
                fail("Delete shape is wrong");
        }

        if ((!((Action) model.getActions().peek()).getS().equalColor(selectedShape) || !((Action) model.getActions().peek()).getS().equalStroke(selectedShape) || !((Action) model.getActions().peek()).getS().equalPosition(selectedShape) || !((Action) model.getActions().peek()).getS().equalSize(selectedShape) || ((Action) model.getActions().peek()).getType() != 'd'))
                fail("Storing delete shape in history is wrong");
    }
}
//9