package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Stack;

/**
 * This shape represents the model in the MVC and contains a list of the shapes currently present as well as
 * stacks for actions and changes to be used for the undo and redo features.
 * It implements observable for the view to be able to listen to changes within it, and implements serializable to be able to be
 * saved and loaded from a file and sent across a network.
 * @author malaksadek
 */
public class Model extends Observable implements Serializable {
    private static final long serialVersionUID = 3;
    private Stack actions;
    private ArrayList<Shape> shapes;
    private Stack lastChange;

    /**
     * Constructor for the class, sets up the array list and stacks.
     */
    public Model(){
        shapes = new ArrayList<>();
        actions = new Stack();
        lastChange = new Stack();
    }

    /**
     * Getter method for the array of shapes present in the model.
     * @return the array of shapes in the model
     */
    public ArrayList<Shape> getShapes() {
        return shapes;
    }

    /**
     * Getter method for the stack of actions performed, this is used for the undo feature.
     * @return the stack of actions performed
     */
    public Stack getActions() {
        return actions;
    }

    /**
     * Getter method for the last item in the lastChange stack, this is used for the redo feature.
     * @return the top shape on the lastChange stack (the last shape that was created, edited, or deleted and then that action was undone)
     */
    public Shape getLastChange() {
        if(!lastChange.empty())
            return ((Action)lastChange.peek()).s;
        else
            return null;
    }

    //////////////////////////// Adding the shapes ///////////////////////////////

    //For all these methods, the appropriate type of shape is added to the shape list
    //Then the action is added to the actions stack and given the type 'c' for create
    //Finally the update method is called to inform the view

    /**
     * A line is added to the model.
     * @param l - the line to be added
     */
    public void createLine(Line l) {
        shapes.add(l);
        actions.push(new Action(l, 'c'));
        update();
    }

    /**
     * A triangle is added to the model.
     * @param t - the triangle to be added
     */
    public void createTriangle(Triangle t) {
        shapes.add(t);
        actions.push(new Action(t, 'c'));
        update();
    }

    /**
     * A square is added to the model.
     * @param s - the square to be added
     */
    public void createSquare(Square s) {
        shapes.add(s);
        actions.push(new Action(s, 'c'));
        update();
    }

    /**
     * A rectangle is added to the model.
     * @param r - the rectangle to be added
     */
    public void createRectangle(Rectangle r) {
        shapes.add(r);
        actions.push(new Action(r, 'c'));
        update();
    }

    /**
     * A ellipse is added to the model.
     * @param e - the ellipse to be added
     */
    public void createEllipse(Ellipse e) {
        shapes.add(e);
        actions.push(new Action(e, 'c'));
        update();
    }

    /**
     * A circle is added to the model.
     * @param c - the circle to be added
     */
    public void createCircle(Circle c) {
        shapes.add(c);
        actions.push(new Action(c, 'c'));
        update();
    }

    /**
     * A hexagon is added to the model.
     * @param h - the hexagon to be added
     */
    public void createHexagon(Hexagon h) {
        shapes.add(h);
        actions.push(new Action(h, 'c'));
        update();
    }

    /**
     * A octagon is added to the model.
     * @param o - the octagon to be added
     */
    public void createOctagon(Octagon o) {
        shapes.add(o);
        actions.push(new Action(o, 'c'));
        update();
    }

    /**
     * A parallelogram is added to the model.
     * @param p - the parallelogram to be added
     */
    public void createParallelogram(Parallelogram p) {
        shapes.add(p);
        actions.push(new Action(p, 'c'));
        update();
    }

    /**
     * A polygon is added to the model.
     * @param p - the polygon to be added
     */
    public void createPolygon(Polygon p) {
        shapes.add(p);
        actions.push(new Action(p, 'c'));
        update();
    }

    //////////////////////////// Modifying the history ///////////////////////////////

    /**
     * This method is called by the controller when the undo button is pressed on the view.
     * It modifies the state of the model
     */
    public void undo() {
        //If there is an action which can be undone
        if(!actions.empty()) {

            //The last action performed is obtained
            Action a = (Action) actions.peek();

            //If the last action performed was creating a shape
            if (a.type == 'c') {

                //The action is removed and placed on the lastChange stack (so that it can be redone)
                a = (Action) actions.pop();
                lastChange.push(a);

                //The shape that was created is found in the array of shapes using the Shape equality functions
                for(Shape n : shapes){
                    if((n.equalColor(a.s)) || (n.equalSize(a.s)) || (n.equalPosition(a.s)) || n.equalStroke(a.s))
                        a.s = n;
                }

                //That shape is then removed from the shapes array (to undo creating it)
                shapes.remove(a.s);
                update();
            }
            //If the last action performed was editing a shape
            else if (a.type == 'e') {

                //The action is removed
                a = (Action) actions.pop();

                //The shape that was edited is found in the array of shapes using the Shape equality functions
                for(Shape n : shapes){
                    if((n.equalColor(a.s)) || (n.equalSize(a.s)) || (n.equalPosition(a.s)) || n.equalStroke(a.s))
                        a.s = n;
                }
                //Edits are saved on the action and lastChange stack as two elements, one for the old shape state and one for the new shape state
                //Here the new shape state is removed and added to the lastChange stack (so that it can be redone)
                shapes.remove(a.s);
                lastChange.push(a);

                //The action stack is then rechecked, if it's not empty, the following action is examined
                if (!actions.empty()) {
                    //If the previous entry is a create entry
                    if (((Action) actions.peek()).type == 'c'){
                        //The shape is removed from the shapes array and the action stack and then added to the last change stack
                        shapes.remove(a.s);
                        actions.pop();
                        a = new Action((Action) lastChange.peek());
                        lastChange.push(a);
                        ((Action) lastChange.peek()).type = 'c';
                    } else {
                        //If the previous entry is an edit
                        // The edited shape is added to the shape array and to the last change array (so that it can be redone)
                        Action test = (Action) actions.peek();
                        if (test.s.equalColor(a.s) || test.s.equalPosition(a.s) || test.s.equalSize(a.s) ||test.s.equalStroke(a.s) ) {
                            a = (Action) actions.pop();
                            shapes.add(a.s);
                            lastChange.push(a);
                        }
                    }
                }
                update();
            }
            //If the last action performed was deleting a shape
            else if(a.type == 'd'){

                //The action is removed and placed on the lastChange stack (so that it can be redone)
                a = (Action) actions.pop();
                lastChange.push(a);

                //The shape that was deleted is found in the array of shapes using the Shape equality functions
                for(Shape n : shapes){
                    if((n.equalColor(a.s)) && (n.equalSize(a.s)) && (n.equalPosition(a.s)) && n.equalStroke(a.s))
                        a.s = n;
                }

                //That shape is then added to the shapes array (to undo deleting it)
                shapes.add(a.s);
                update();
            }
        }
    }

    /**
     * This method is called by the controller when the redo button is pressed on the view.
     * It modifies the state of the model
     */
    public void redo() {
        //If there is something to be redone
        if(!lastChange.empty()) {

            //If the last action undone was creating a shape
            if(((Action)lastChange.peek()).type == 'c'){
                //The shape is recreated and added to the action stack again
                shapes.add(((Action)lastChange.peek()).s);
                actions.push(lastChange.pop());
                update();
            }
            //If the last action undone was editing a shape
            else if(((Action)lastChange.peek()).type == 'e') {
                Action a = (Action) lastChange.pop();

                //Since edits are saved as two entries in the stack, the first one is popped and then the second one is checked
                if(!lastChange.empty()) {
                    //If the second one is an edit, both are pushed onto the action stack and the old state is removed from the shapes array and the new one added to it
                    if ((((Action) lastChange.peek()).type == 'e') && ((((Action) lastChange.peek()).s.equalColor(a.s))||(((Action) lastChange.peek()).s.equalPosition(a.s))||(((Action) lastChange.peek()).s.equalSize(a.s))||((Action) lastChange.peek()).s.equalStroke(a.s))) {
                        shapes.remove(a.s);
                        actions.push(a);
                        a = (Action) lastChange.pop();
                        shapes.add(a.s);
                        actions.push(a);
                    }
                    //If the second one is not an edit, it's just added to the shapes array and actions stack
                    else {
                        shapes.add(a.s);
                        actions.push(a);
                    }
                }
                //If the lastChange stack is empty and this is the last action to be undone, it's just added to the shapes array and actions stack
                else {
                    shapes.remove(((Action)actions.peek()).s);
                    shapes.add(a.s);
                    actions.push(a);
                }
                update();
            }
            //If the last action undone was deleting a shape
            else if (((Action)lastChange.peek()).type == 'd'){
                //The shape is removed from the shape array and the action is added to the action stack
                shapes.remove(((Action)lastChange.peek()).s);
                actions.push(lastChange.pop());
                update();
            }
        }

    }

    /**
     * This method essentially resets the model by emptying its shapes array and its action and lastChange stacks.
     */
    public void clear() {
        shapes.clear();
        actions.clear();
        lastChange.clear();
        update();
    }

    /**
     * This method is called when an existing shape is edited.
     * @param newShape - the shape's state after the edit took place
     * @param oldShape - the shape's state before the edit took place
     * @param changeType - the type of edit that took place
     */
    public void modifyShapes(Shape newShape, Shape oldShape, int changeType){
        int index = 0;
        for(Shape n : shapes) {
            switch (changeType){
                //If the change is changing the shape's color
                case 0:
                    if (n.equalColor(oldShape)) {
                        //This replaces the old shape with the new shape and adds the before and after states to the actions stack
                        shapes.set(index, newShape);
                        actions.push(new Action(oldShape, 'e'));
                        actions.push(new Action(newShape, 'e'));
                    }
                    break;
                //If the change is changing the shape's position
                case 1:
                    if (n.equalPosition(oldShape)) {
                        //This replaces the old shape with the new shape and adds the before and after states to the actions stack
                        shapes.set(index, newShape);
                        actions.push(new Action(oldShape, 'e'));
                        actions.push(new Action(newShape, 'e'));
                    }
                    break;
                    //If the change is changing the shape's size
                case 2:
                    if (n.equalSize(oldShape)) {
                        //This replaces the old shape with the new shape and adds the before and after states to the actions stack
                        shapes.set(index, newShape);
                        actions.push(new Action(oldShape, 'e'));
                        actions.push(new Action(newShape, 'e'));
                    }
                    break;
                    //If the change is changing the shape's stroke
                case 3:
                    if (n.equalStroke(oldShape)) {
                        //This replaces the old shape with the new shape and adds the before and after states to the actions stack
                        shapes.set(index, newShape);
                        actions.push(new Action(oldShape, 'e'));
                        actions.push(new Action(newShape, 'e'));
                    }
                    break;
                    //If the change is deleting the shape
                case 4:
                    if(n.equalColor(newShape) && n.equalPosition(newShape) && n.equalSize(newShape) && n.equalStroke(newShape)) {
                        //This adds the delete action to the actions stack
                        actions.push(new Action(newShape, 'd'));
                    }
                    break;
                default:
                    break;
            }
            index++;
        }
        //If the shapes array is empty and the method was called, then the action performed must have been a delete
        if(shapes.size() == 0) {
            actions.push(new Action(newShape, 'd'));
        }
    }

    /**
     * This method empties the lastChange stack so that no more redos can be done.
     */
    public void clearRedo(){
        lastChange.clear();
    }

    /**
     * This method deletes the given shape from the model.
     * @param selectedShape - the shape to be deleted from the model
     */
    public void delete(Shape selectedShape){
        int index = 0, shapeIndex = -1;

        //This finds the index of the shape to be deleted in the model and then removes it from the model
        for(Shape n: shapes) {
            if(n.equalSize(selectedShape) && n.equalPosition(selectedShape) && n.equalStroke(selectedShape) && n.equalColor(selectedShape)){
                shapeIndex = index;
            }
            index++;
        }
        shapes.remove(shapeIndex);
        update();
    }

    /**
     * This method adds all the shapes of a given model to this models.
     * @param m - the model who's shapes should be added to this model
     */
    public void addShapes(Model m){
        shapes.addAll(m.shapes);
    }

    /**
     * This method is called whenever a change is made to the model.
     * It notifies the model's observers (the view) that a change has happened so that this can be reflected in the view
     */
    private void update(){
        this.setChanged();
        this.notifyObservers();
    }
}
