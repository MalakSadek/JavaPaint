package Controller;

import Models.*;
import Models.Polygon;
import Models.Rectangle;
import Models.Shape;
import java.awt.*;
import java.io.*;

/**
 * The controller class for the MVC model which takes input from the view and makes changes to the model.
 * @author malaksadek
 */
public class Controller {

    private Model model;
    private Color selectedColor;
    private WebClient wc;

    /**
     * Constructor for the class. Black is set as the default color.
     * @param model is the model the controller will read and update
     */
    public Controller(Model model) {
        this.model = model;
        selectedColor = Color.BLACK;
    }

    //////////////////////////// Drawing the shapes ///////////////////////////////

    /**
     * This method is called when the user has selected the line tool and has drawn a line on the view (by dragging mouse).
     * @param sX - the line's starting x position
     * @param eX - the line's ending x position
     * @param sY - the line's starting y position
     * @param eY - the line's ending y position
     * @param strokeWidth - the line's stroke width
     * @param fill - this is unnecessary for the line and so is ignored, it is a byproduct of line inheriting from the shape class
     */
    public void lineTool(int sX, int eX, int sY, int eY, int strokeWidth, boolean fill) {

        //Creates a new line object and sends it to the model
        Line l;
        if(fill) {
             l = new Line(sX, sY, eX, eY, strokeWidth, selectedColor, null,  "Line");
        } else {
            l = new Line(sX, sY, eX, eY, strokeWidth, null, selectedColor,  "Line");
        }

        model.createLine(l);
    }

    /**
     * This method is called when the user has selected the triangle tool and has drawn a triangle on the view (by dragging mouse).
     * @param sX - the triangle's starting x position
     * @param eX - the triangle's ending x position
     * @param sY - the triangle's starting y position
     * @param eY - the triangle's ending y position
     * @param strokeWidth - the triangle's stroke width
     * @param fill - this determines whether the triangle is filled or just a stroke
     * @param equilateral - this determines whether the triangle is equilateral or not (whether the shift key is pressed or not)
     */
    public void triangleTool(int sX, int eX, int sY, int eY, int strokeWidth, boolean fill, boolean equilateral) {

        //Creates a new triangle object and sends it to the model
        Triangle t;
        if(fill) {
            t = new Triangle(sX, sY, eX, eY, strokeWidth, selectedColor, null, equilateral, "Triangle");
        } else {
            t = new Triangle(sX, sY, eX, eY, strokeWidth, null, selectedColor, equilateral, "Triangle");
        }

        model.createTriangle(t);
    }

    /**
     * This method is called when the user has selected the square tool and has drawn a square on the view (by dragging mouse).
     * @param sX - the square's starting x position
     * @param eX - the square's ending x position
     * @param sY - the square's starting y position
     * @param eY - the square's ending y position
     * @param strokeWidth - the square's stroke width
     * @param fill - this determines whether the square is filled or just a stroke
     */
    public void squareTool(int sX, int eX, int sY, int eY, int strokeWidth, boolean fill) {

        //Creates a new square object and sends it to the model
        Square s;
        if(fill) {
            s = new Square(sX, sY, eX, eY, strokeWidth, selectedColor, null, "Square");
        } else {
            s = new Square(sX, sY, eX, eY, strokeWidth, null, selectedColor, "Square");
        }

        model.createSquare(s);
    }

    /**
     * This method is called when the user has selected the rectangle tool and has drawn a rectangle on the view (by dragging mouse).
     * @param sX - the rectangle's starting x position
     * @param eX - the rectangle's ending x position
     * @param sY - the rectangle's starting y position
     * @param eY - the rectangle's ending y position
     * @param strokeWidth - the rectangle's stroke width
     * @param fill - this determines whether the rectangle is filled or just a stroke
     */
    public void rectangleTool(int sX, int eX, int sY, int eY, int strokeWidth, boolean fill) {

        //Creates a new rectangle object and sends it to the model
        Rectangle r;
        if(fill) {
            r = new Rectangle(sX, sY, eX, eY, strokeWidth, selectedColor, null, "Rectangle");
        } else {
            r = new Rectangle(sX, sY, eX, eY, strokeWidth, null, selectedColor, "Rectangle");
        }

        model.createRectangle(r);
    }

    /**
     * This method is called when the user has selected the ellipse tool and has drawn a ellipse on the view (by dragging mouse).
     * @param sX - the ellipse's starting x position
     * @param eX - the ellipse's ending x position
     * @param sY - the ellipse's starting y position
     * @param eY - the ellipse's ending y position
     * @param strokeWidth - the ellipse's stroke width
     * @param fill - this determines whether the ellipse is filled or just a stroke
     */
    public void ellipseTool(int sX, int eX, int sY, int eY, int strokeWidth, boolean fill) {

        //Creates a new ellipse object and sends it to the model
        Ellipse e;
        if(fill) {
            e = new Ellipse(sX, sY, eX, eY, strokeWidth, selectedColor, null, "Ellipse");
        } else {
            e = new Ellipse(sX, sY, eX, eY, strokeWidth, null, selectedColor, "Ellipse");
        }

        model.createEllipse(e);
    }

    /**
     * This method is called when the user has selected the circle tool and has drawn a circle on the view (by dragging mouse).
     * @param sX - the circle's starting x position
     * @param eX - the circle's ending x position
     * @param sY - the circle's starting y position
     * @param eY - the circle's ending y position
     * @param strokeWidth - the circle's stroke width
     * @param fill - this determines whether the circle is filled or just a stroke
     */
    public void circleTool(int sX, int eX, int sY, int eY, int strokeWidth, boolean fill) {

        //Creates a new circle object and sends it to the model
        Circle c;
        if(fill) {
            c = new Circle(sX, sY, eX, eY, strokeWidth, selectedColor, null, "Circle");
        } else {
            c = new Circle(sX, sY, eX, eY, strokeWidth, null, selectedColor, "Circle");
        }

        model.createCircle(c);
    }

    /**
     * This method is called when the user has selected the hexagon tool and has drawn a hexagon on the view (by dragging mouse).
     * @param sX - the hexagon's starting x position
     * @param eX - the hexagon's ending x position
     * @param sY - the hexagon's starting y position
     * @param eY - the hexagon's ending y position
     * @param strokeWidth - the hexagon's stroke width
     * @param fill - this determines whether the hexagon is filled or just a stroke
     */
    public void hexagonTool(int sX, int eX, int sY, int eY, int strokeWidth, boolean fill) {

        //Creates a new hexagon object and sends it to the model
        Hexagon h;
        if(fill) {
            h = new Hexagon(sX, sY, eX, eY, strokeWidth, selectedColor, null, "Hexagon");
        } else {
            h = new Hexagon(sX, sY, eX, eY, strokeWidth, null, selectedColor, "Hexagon");
        }

        model.createHexagon(h);
    }

    /**
     * This method is called when the user has selected the octagon tool and has drawn a octagon on the view (by dragging mouse).
     * @param sX - the octagon's starting x position
     * @param eX - the octagon's ending x position
     * @param sY - the octagon's starting y position
     * @param eY - the octagon's ending y position
     * @param strokeWidth - the octagon's stroke width
     * @param fill - this determines whether the octagon is filled or just a stroke
     */
    public void octagonTool(int sX, int eX, int sY, int eY, int strokeWidth, boolean fill) {

        //Creates a new octagon object and sends it to the model
        Octagon o;
        if(fill) {
            o = new Octagon(sX, sY, eX, eY, strokeWidth, selectedColor, null, "Octagon");
        } else {
            o = new Octagon(sX, sY, eX, eY, strokeWidth, null, selectedColor, "Octagon");
        }

        model.createOctagon(o);
    }

    /**
     * This method is called when the user has selected the parallelogram tool and has drawn a parallelogram on the view (by dragging mouse).
     * @param sX - the parallelogram's starting x position
     * @param eX - the parallelogram's ending x position
     * @param sY - the parallelogram's starting y position
     * @param eY - the parallelogram's ending y position
     * @param strokeWidth - the parallelogram's stroke width
     * @param fill - this determines whether the parallelogram is filled or just a stroke
     */
    public void parallelogramTool(int sX, int eX, int sY, int eY, int strokeWidth, boolean fill) {

        //Creates a new parallelogram object and sends it to the model
        Parallelogram p;
        if(fill) {
            p = new Parallelogram(sX, sY, eX, eY, strokeWidth, selectedColor, null, "Parallelogram");
        } else {
            p = new Parallelogram(sX, sY, eX, eY, strokeWidth, null, selectedColor, "Parallelogram");
        }

        model.createParallelogram(p);
    }

    /**
     *
     * @param sX - the polygon's starting x position
     * @param eX - the polygon's ending x position
     * @param sY - the polygon's starting y position
     * @param eY - the polygon's ending y position
     * @param strokeWidth - the polygon's stroke width
     * @param fill - this determines whether the polygon is filled or just a stroke
     * @param numberOfSides - the polygon's number of sides - selected from the menu in the view
     */
    public void drawPolygon(int sX, int eX, int sY, int eY, int strokeWidth, boolean fill, Integer numberOfSides){

        //Creates a new polygon object and sends it to the model
        Polygon p;
        if(fill) {
            p = new Polygon(sX, sY, eX, eY, strokeWidth, selectedColor, null, numberOfSides, "Polygon");
        } else {
            p = new Polygon(sX, sY, eX, eY, strokeWidth, null, selectedColor, numberOfSides, "Polygon");
        }

        model.createPolygon(p);
    }

    /**
     * This method sets the currently selected color.
     * This is then used when drawing shapes or changing their color.
     * @param c - the color the user has selected in the view
     */
    public void chooseColor(Color c) {
        selectedColor = c;
    }

    /**
     * Getter function for selectedColor.
     * @return the color currnetly selected by the user
     */
    public Color getSelectedColor() {
        return selectedColor;
    }

    //////////////////////////// History methods ///////////////////////////////

    /**
     * This method calls the model's undo method.
     */
    public void undo() {
        model.undo();
    }

    /**
     * This method calls the model's redo method.
     */
    public void redo() {
        model.redo();
    }

    /**
     * This method determines whether there are still any actions to be redone or not by checking the model's last change.
     * @return if the model's last change is null, then no more actions can be redone (and the button will be greyed out in the view)
     */
    public boolean stillRedo(){
        return model.getLastChange() != null;
    }

    /**
     * This method saves the current model to a file that the user has chosen in the view.
     * This is made possible by having the model and action classes as well as all classes that inherit from shape be serializable.
     * @param directory - where the user has selected that they want the model to be saved.
     */
    public void save(File directory) {

        //How to save a serializable class in a file obtained from:
        //https://www.geeksforgeeks.org/serialization-in-java/

        String filename = directory+".ser";
        try
        {
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file);

            out.writeObject(model);

            out.close();
            file.close();
        }
        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
    }

    /**
     * This method loads a model from a file that the user has chosen in the view and sets that to the current model.
     * If a model isn't properly loaded from the file, the old model is used.
     * @param selectedFile - where the user has selected that they want the model to be saved.
     */
    public Model load(File selectedFile) {
        Model temp = model;
        //How to load a serializable class from a file obtained from:
        //https://www.geeksforgeeks.org/serialization-in-java/

        try
        {
            FileInputStream file = new FileInputStream(selectedFile);
            ObjectInputStream in = new ObjectInputStream(file);

            model = (Model)in.readObject();

            in.close();
            file.close();

            if(model != null)
                return model;
            else {
                model = temp;
                return model;
            }
        }

        catch(IOException | ClassNotFoundException ex)
        {
            model = temp;
            return model;
        }

    }

    /**
     * This method calls the model's clear method.
     */
    public void clear() {
        model.clear();
    }

    /**
     * This method is called when the user clicks on the canvas, therefore trying to select a shape.
     * @param mx - the mouse's x coordinate
     * @param my - the mouse's y coordinate
     * @return if the mouse coordinates overlap a shape in the model, that shape is returned, or else null is returned as nothing is selected
     */
    public Shape selectShape(int mx, int my){

        int index = 0;

        //This iterates over all the shapes currently in the model
        for (Shape n : model.getShapes()) {

            //This finds the minimum and maximum x and y coordinates of the shape
            //because the starting coordinates might not necessarily be smaller than the ending ones
            //if the user draws from right to left or top to bottom
            int minX = Math.min(n.getStartX(), n.getEndX());
            int minY = Math.min(n.getStartY(), n.getEndY());
            int maxX = Math.max(n.getStartX(), n.getEndX());
            int maxY = Math.max(n.getStartY(), n.getEndY());

            //If the mouse's coordinate is greater than the smallest shape coordinate and less than the largest shape coordinate, it's in that shape
            boolean inX = false, inY = false;
            if((mx < maxX) && (mx > minX)) {
                inX = true;
            }
            if((my < maxY) && (my > minY)) {
                inY = true;
            }

            if(inX && inY) {
                return n;
            }
            index++;
        }

        //If the mouse is not overlapping any shape, null is returned
        return null;
    }

    /**
     * This passes the old shape state and new shape state to the model to update it whenever a change occurs to an existing shape.
     * @param newShape the shape after the change has occurred to it
     * @param oldShape the shape before the change had occurred to it
     * @param changeType the kind of change that happened to the shape (color, movement, fill, stroke width, size, or deletion)
     */
    public void updateModel(Shape newShape, Shape oldShape, int changeType){
        model.modifyShapes(newShape, oldShape, changeType);
    }

    /**
     * This method clears the model's lastChange stack, deleting all possible redo states.
     */
    public void clearRedo(){
        model.clearRedo();
    }

    /**
     * This method calls the model's delete method and passes it the shape the user currently has selected.
     * @param selectedShape - this is the shape the user currently has selected
     */
    public void delete(Shape selectedShape){
        model.delete(selectedShape);
    }

    //////////////////////////// Networking methods ///////////////////////////////

    /**
     * This method calls the appropriate WebClient method to sync the current canvas with another user and updates the model.
     * @return the updated model
     */
    public Model sync() {

        //If the user has already connected to another user
        if(wc != null) {
            Model temp = model;
            //The newly synced model is requested from the server, if a null or empty one is returned, the old model is used
            try {
                model = wc.sendRequestToServer();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                model = temp;
            }
            if ((model == null) || (model.getShapes().isEmpty()))
                model = temp;
            return model;
        }
        else
            return model;
    }

    /**
     * This method calls the appropriate WebClient method to connect to another user
     * @param IPAddress - this is the IP address of the person the user wants to connect to, supplied by the user
     * @throws IOException
     */
    public void connect(String IPAddress) throws IOException {
        wc = new WebClient("localhost", 12349, model, IPAddress);
    }

    /**
     * This method calls the appropriate WebClient method to disconnect from another user
     */
    public void disconnect(){
        if(wc != null)
            wc.cleanUp();
    }

}
