package Models;

import java.awt.*;

/**
 * This class inherits from shape and only has a constructor which calls shape's constructor.
 * @author malaksadek
 */
public class Triangle extends Shape {
    private boolean equilateral;

    /**
     * Constructor for the class, calls shape's constructor and also stores whether or not it's equilateral.
     * @param startX - the starting x coordinate for the shape
     * @param startY - the starting y coordinate for the shape
     * @param endX - the ending x coordinate for the shape
     * @param endY - the ending y coordinate for the shape
     * @param strokeWidth - the stroke width of the shape
     * @param fillColor - the fill color of the shape, set to null if the shape isn't filled
     * @param strokeColor - the stroke color of the shape, set to null if the shape is filled
     * @param shapeType - the type of the shape described as a string, Triangle in this case
     * @param equilateral - whether or not it's an equilateral triangle
     */
    public Triangle(int startX, int startY, int endX, int endY, int strokeWidth, Color fillColor, Color strokeColor, boolean equilateral, String shapeType) {
        super(startX, startY, endX, endY, strokeWidth, fillColor, strokeColor, shapeType);
        this.equilateral = equilateral;
    }

    /**
     * Getter function for whether or not it's equilateral.
     * @return equilateral
     */
    public boolean getEquilateral(){
        return equilateral;
    }

}
