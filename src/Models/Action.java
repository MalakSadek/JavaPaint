package Models;

import java.io.Serializable;

/**
 * This class represents an action by the user.
 * It consists of a shape, and the type of action performed on it
 * @author malaksadek
 */
public class Action implements Serializable {
    private static final long serialVersionUID = 1;
    Shape s;
    char type; //e - edit, c - create, d - delete

    /**
     * The constructor for this class.
     * @param s - the shape associated with the action
     * @param type - the type of the action: c - create shape, e - edit shape, d - delete shape
     */
    Action(Shape s, char type){
        this.s = s;
        this.type = type;
    }

    /**
     * Another constructor for the class that takes an action and creates one like it.
     * @param a - the action used to copy the class
     */
    Action(Action a){
        this.s = a.s;
        this.type = a.type;
    }

    /**
     * Getter method for the action's shape.
     * @return the shape associated with the action
     */
    public Shape getS() {
        return s;
    }

    /**
     * Getter method for the action's type.
     * @return the type associated with the action
     */
    public char getType() {
        return type;
    }
}
