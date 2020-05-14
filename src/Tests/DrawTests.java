package Tests;

import static org.junit.Assert.*;

import Controller.Controller;
import Models.*;
import Models.Polygon;
import Models.Rectangle;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

/**
 * These are JUnit tests for the model's drawing features.
 */
public class DrawTests {

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
     * This test creates a line and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawLine() {
        model.clear();
        controller.lineTool(10, 50, 10, 50, 5, false);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Line l = new Line(10, 10, 50, 50, 5, null, controller.getSelectedColor(), "Line");

        if(!l.equalColor(model.getShapes().get(0)) || !l.equalPosition(model.getShapes().get(0)) || !l.equalSize(model.getShapes().get(0)) || !l.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !l.equalColor(((Action)model.getActions().peek()).getS()) || !l.equalPosition(((Action)model.getActions().peek()).getS()) || !l.equalSize(((Action)model.getActions().peek()).getS()) || !l.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a filled triangle and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawTriangleFill() {
        model.clear();
        controller.triangleTool(10, 50, 10, 50, 5, true, false);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Triangle t = new Triangle(10, 10, 50, 50, 5, controller.getSelectedColor(), null, false, "Triangle");

        if(!t.equalColor(model.getShapes().get(0)) || !t.equalPosition(model.getShapes().get(0)) || !t.equalSize(model.getShapes().get(0)) || !t.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !t.equalColor(((Action)model.getActions().peek()).getS()) || !t.equalPosition(((Action)model.getActions().peek()).getS()) || !t.equalSize(((Action)model.getActions().peek()).getS()) || !t.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates an unfilled triangle and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawTriangleNoFill() {
        model.clear();
        controller.triangleTool(10, 50, 10, 50, 5, false, false);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Triangle t = new Triangle(10, 10, 50, 50, 5, null, controller.getSelectedColor(), false, "Triangle");

        if(!t.equalColor(model.getShapes().get(0)) || !t.equalPosition(model.getShapes().get(0)) || !t.equalSize(model.getShapes().get(0)) || !t.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !t.equalColor(((Action)model.getActions().peek()).getS()) || !t.equalPosition(((Action)model.getActions().peek()).getS()) || !t.equalSize(((Action)model.getActions().peek()).getS()) || !t.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a filled equilateral triangle and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawEquilateralTriangleFill() {
        model.clear();
        controller.triangleTool(10, 50, 10, 50, 5, true, true);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Triangle t = new Triangle(10, 10, 50, 50, 5, controller.getSelectedColor(), null, false, "Triangle");

        if(!t.equalColor(model.getShapes().get(0)) || !t.equalPosition(model.getShapes().get(0)) || !t.equalSize(model.getShapes().get(0)) || !t.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !t.equalColor(((Action)model.getActions().peek()).getS()) || !t.equalPosition(((Action)model.getActions().peek()).getS()) || !t.equalSize(((Action)model.getActions().peek()).getS()) || !t.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates an unfilled equilateral triangle and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawEquilateralTriangleNoFill() {
        model.clear();
        controller.triangleTool(10, 50, 10, 50, 5, false, true);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Triangle t = new Triangle(10, 10, 50, 50, 5, null, controller.getSelectedColor(), false, "Triangle");

        if(!t.equalColor(model.getShapes().get(0)) || !t.equalPosition(model.getShapes().get(0)) || !t.equalSize(model.getShapes().get(0)) || !t.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !t.equalColor(((Action)model.getActions().peek()).getS()) || !t.equalPosition(((Action)model.getActions().peek()).getS()) || !t.equalSize(((Action)model.getActions().peek()).getS()) || !t.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a filled rectangle and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawRectangleFill() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, controller.getSelectedColor(), null, "Rectangle");

        if(!r.equalColor(model.getShapes().get(0)) || !r.equalPosition(model.getShapes().get(0)) || !r.equalSize(model.getShapes().get(0)) || !r.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !r.equalColor(((Action)model.getActions().peek()).getS()) || !r.equalPosition(((Action)model.getActions().peek()).getS()) || !r.equalSize(((Action)model.getActions().peek()).getS()) || !r.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates an unfilled rectangle and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawRectangleNoFill() {
        model.clear();
        controller.rectangleTool(10, 50, 10, 50, 5, false);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, null, controller.getSelectedColor(), "Rectangle");

        if(!r.equalColor(model.getShapes().get(0)) || !r.equalPosition(model.getShapes().get(0)) || !r.equalSize(model.getShapes().get(0)) || !r.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !r.equalColor(((Action)model.getActions().peek()).getS()) || !r.equalPosition(((Action)model.getActions().peek()).getS()) || !r.equalSize(((Action)model.getActions().peek()).getS()) || !r.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a filled square and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawSquareFill() {
        model.clear();
        controller.squareTool(10, 50, 10, 50, 5, true);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Square s = new Square(10, 10, 50, 50, 5, controller.getSelectedColor(), null, "Square");

        if(!s.equalColor(model.getShapes().get(0)) || !s.equalPosition(model.getShapes().get(0)) || !s.equalSize(model.getShapes().get(0)) || !s.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !s.equalColor(((Action)model.getActions().peek()).getS()) || !s.equalPosition(((Action)model.getActions().peek()).getS()) || !s.equalSize(((Action)model.getActions().peek()).getS()) || !s.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates an unfilled square and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawSquareNoFill() {
        model.clear();
        controller.squareTool(10, 50, 10, 50, 5, false);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Square s = new Square(10, 10, 50, 50, 5, null, controller.getSelectedColor(), "Square");

        if(!s.equalColor(model.getShapes().get(0)) || !s.equalPosition(model.getShapes().get(0)) || !s.equalSize(model.getShapes().get(0)) || !s.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !s.equalColor(((Action)model.getActions().peek()).getS()) || !s.equalPosition(((Action)model.getActions().peek()).getS()) || !s.equalSize(((Action)model.getActions().peek()).getS()) || !s.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a filled ellipse and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawEllipseFill() {
        model.clear();
        controller.ellipseTool(10, 50, 10, 50, 5, true);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Ellipse e = new Ellipse(10, 10, 50, 50, 5, controller.getSelectedColor(), null, "Ellipse");

        if(!e.equalColor(model.getShapes().get(0)) || !e.equalPosition(model.getShapes().get(0)) || !e.equalSize(model.getShapes().get(0)) || !e.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !e.equalColor(((Action)model.getActions().peek()).getS()) || !e.equalPosition(((Action)model.getActions().peek()).getS()) || !e.equalSize(((Action)model.getActions().peek()).getS()) || !e.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates an unfilled ellipse and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawEllipseNoFill() {
        model.clear();
        controller.ellipseTool(10, 50, 10, 50, 5, false);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Ellipse e = new Ellipse(10, 10, 50, 50, 5, null, controller.getSelectedColor(), "Ellipse");

        if(!e.equalColor(model.getShapes().get(0)) || !e.equalPosition(model.getShapes().get(0)) || !e.equalSize(model.getShapes().get(0)) || !e.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !e.equalColor(((Action)model.getActions().peek()).getS()) || !e.equalPosition(((Action)model.getActions().peek()).getS()) || !e.equalSize(((Action)model.getActions().peek()).getS()) || !e.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a filled circle and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawCircleFill() {
        model.clear();
        controller.circleTool(10, 50, 10, 50, 5, true);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Circle c = new Circle(10, 10, 50, 50, 5, controller.getSelectedColor(), null, "Circle");

        if(!c.equalColor(model.getShapes().get(0)) || !c.equalPosition(model.getShapes().get(0)) || !c.equalSize(model.getShapes().get(0)) || !c.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !c.equalColor(((Action)model.getActions().peek()).getS()) || !c.equalPosition(((Action)model.getActions().peek()).getS()) || !c.equalSize(((Action)model.getActions().peek()).getS()) || !c.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates an unfilled circle and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawCircleNoFill() {
        model.clear();
        controller.circleTool(10, 50, 10, 50, 5, false);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Circle c = new Circle(10, 10, 50, 50, 5, null, controller.getSelectedColor(), "Circle");

        if(!c.equalColor(model.getShapes().get(0)) || !c.equalPosition(model.getShapes().get(0)) || !c.equalSize(model.getShapes().get(0)) || !c.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !c.equalColor(((Action)model.getActions().peek()).getS()) || !c.equalPosition(((Action)model.getActions().peek()).getS()) || !c.equalSize(((Action)model.getActions().peek()).getS()) || !c.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a filled hexagon and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawHexagonFill() {
        model.clear();
        controller.hexagonTool(10, 50, 10, 50, 5, true);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Hexagon h = new Hexagon(10, 10, 50, 50, 5, controller.getSelectedColor(), null, "Hexagon");

        if(!h.equalColor(model.getShapes().get(0)) || !h.equalPosition(model.getShapes().get(0)) || !h.equalSize(model.getShapes().get(0)) || !h.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !h.equalColor(((Action)model.getActions().peek()).getS()) || !h.equalPosition(((Action)model.getActions().peek()).getS()) || !h.equalSize(((Action)model.getActions().peek()).getS()) || !h.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates an unfilled hexagon and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawHexagonNoFill() {
        model.clear();
        controller.hexagonTool(10, 50, 10, 50, 5, false);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Hexagon h = new Hexagon(10, 10, 50, 50, 5, null, controller.getSelectedColor(), "Hexagon");

        if(!h.equalColor(model.getShapes().get(0)) || !h.equalPosition(model.getShapes().get(0)) || !h.equalSize(model.getShapes().get(0)) || !h.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !h.equalColor(((Action)model.getActions().peek()).getS()) || !h.equalPosition(((Action)model.getActions().peek()).getS()) || !h.equalSize(((Action)model.getActions().peek()).getS()) || !h.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a filled octagon and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawOctagonFill() {
        controller.octagonTool(10, 50, 10, 50, 5, true);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Octagon o = new Octagon(10, 10, 50, 50, 5, controller.getSelectedColor(), null, "Octagon");

        if(!o.equalColor(model.getShapes().get(0)) || !o.equalPosition(model.getShapes().get(0)) || !o.equalSize(model.getShapes().get(0)) || !o.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !o.equalColor(((Action)model.getActions().peek()).getS()) || !o.equalPosition(((Action)model.getActions().peek()).getS()) || !o.equalSize(((Action)model.getActions().peek()).getS()) || !o.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates an unfilled octagon and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawOctagonNoFill() {
        controller.octagonTool(10, 50, 10, 50, 5, false);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Octagon o = new Octagon(10, 10, 50, 50, 5, null, controller.getSelectedColor(), "Octagon");

        if(!o.equalColor(model.getShapes().get(0)) || !o.equalPosition(model.getShapes().get(0)) || !o.equalSize(model.getShapes().get(0)) || !o.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !o.equalColor(((Action)model.getActions().peek()).getS()) || !o.equalPosition(((Action)model.getActions().peek()).getS()) || !o.equalSize(((Action)model.getActions().peek()).getS()) || !o.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a filled parallelogram and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawParallelogramFill() {
        controller.parallelogramTool(10, 50, 10, 50, 5, true);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Parallelogram p = new Parallelogram(10, 10, 50, 50, 5, controller.getSelectedColor(), null, "Parallelogram");

        if(!p.equalColor(model.getShapes().get(0)) || !p.equalPosition(model.getShapes().get(0)) || !p.equalSize(model.getShapes().get(0)) || !p.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !p.equalColor(((Action)model.getActions().peek()).getS()) || !p.equalPosition(((Action)model.getActions().peek()).getS()) || !p.equalSize(((Action)model.getActions().peek()).getS()) || !p.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates an unfilled parallelogram and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawParallelogramNoFill() {
        controller.parallelogramTool(10, 50, 10, 50, 5, false);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Parallelogram p = new Parallelogram(10, 10, 50, 50, 5, null, controller.getSelectedColor(), "Parallelogram");

        if(!p.equalColor(model.getShapes().get(0)) || !p.equalPosition(model.getShapes().get(0)) || !p.equalSize(model.getShapes().get(0)) || !p.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !p.equalColor(((Action)model.getActions().peek()).getS()) || !p.equalPosition(((Action)model.getActions().peek()).getS()) || !p.equalSize(((Action)model.getActions().peek()).getS()) || !p.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates a filled polygon and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawPolygonNoFill() {
        controller.drawPolygon(10, 50, 10, 50, 5, false, 10);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Polygon p = new Polygon(10, 10, 50, 50, 5, null, controller.getSelectedColor(), 10, "Polygon");

        if(!p.equalColor(model.getShapes().get(0)) || !p.equalPosition(model.getShapes().get(0)) || !p.equalSize(model.getShapes().get(0)) || !p.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !p.equalColor(((Action)model.getActions().peek()).getS()) || !p.equalPosition(((Action)model.getActions().peek()).getS()) || !p.equalSize(((Action)model.getActions().peek()).getS()) || !p.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test creates an unfilled polygon and checks that it has been properly added to the model's shapes array and actions stack.
     */
    @Test
    public void drawPolygonFill() {
        controller.drawPolygon(10, 50, 10, 50, 5, true, 10);

        assertNotEquals(model.getShapes().size(), 0);
        assertEquals(model.getShapes().size(), 1);

        Polygon p = new Polygon(10, 10, 50, 50, 5, controller.getSelectedColor(), null, 10, "Polygon");

        if(!p.equalColor(model.getShapes().get(0)) || !p.equalPosition(model.getShapes().get(0)) || !p.equalSize(model.getShapes().get(0)) || !p.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !p.equalColor(((Action)model.getActions().peek()).getS()) || !p.equalPosition(((Action)model.getActions().peek()).getS()) || !p.equalSize(((Action)model.getActions().peek()).getS()) || !p.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test chooses a fill color and draws a filled shape and checks whether it's the correct color.
     */
    @Test
    public void chooseColorFill() {
        model.clear();
        controller.chooseColor(Color.BLACK);
        controller.rectangleTool(10, 50, 10, 50, 5, true);

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, Color.BLACK, null, "Rectangle");

        if(!r.equalColor(model.getShapes().get(0)) || !r.equalPosition(model.getShapes().get(0)) || !r.equalSize(model.getShapes().get(0)) || !r.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !r.equalColor(((Action)model.getActions().peek()).getS()) || !r.equalPosition(((Action)model.getActions().peek()).getS()) || !r.equalSize(((Action)model.getActions().peek()).getS()) || !r.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }

    /**
     * This test chooses a stroke color and draws a filled shape and checks whether it's the correct color.
     */
    @Test
    public void chooseColorNoFill() {
        model.clear();
        controller.chooseColor(Color.BLACK);
        controller.rectangleTool(10, 50, 10, 50, 5, false);

        Rectangle r = new Rectangle(10, 10, 50, 50, 5, null, Color.BLACK, "Rectangle");

        if(!r.equalColor(model.getShapes().get(0)) || !r.equalPosition(model.getShapes().get(0)) || !r.equalSize(model.getShapes().get(0)) || !r.equalStroke(model.getShapes().get(0)))
            fail("Shapes array in Model is wrong!");
        if(((Action)model.getActions().peek()).getType() !='c' || !r.equalColor(((Action)model.getActions().peek()).getS()) || !r.equalPosition(((Action)model.getActions().peek()).getS()) || !r.equalSize(((Action)model.getActions().peek()).getS()) || !r.equalStroke(((Action)model.getActions().peek()).getS()))
            fail("History stack in Model is wrong!");
    }
}
//23