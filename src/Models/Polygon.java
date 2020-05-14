package Models;

import java.awt.*;

/**
 * This class inherits from shape and only has a constructor which calls shape's constructor.
 * @author malaksadek
 */
public class Polygon extends Shape {

    int numberOfSides;
    /**
     * Constructor for the class, calls shape's constructor and also stores its number of sides.
     * @param startX - the starting x coordinate for the shape
     * @param startY - the starting y coordinate for the shape
     * @param endX - the ending x coordinate for the shape
     * @param endY - the ending y coordinate for the shape
     * @param strokeWidth - the stroke width of the shape
     * @param fillColor - the fill color of the shape, set to null if the shape isn't filled
     * @param strokeColor - the stroke color of the shape, set to null if the shape is filled
     * @param shapeType - the type of the shape described as a string, Polygon in this case
     * @param numberOfSides - the number of sides of the polygon
     */
    public Polygon(int startX, int startY, int endX, int endY, int strokeWidth, Color fillColor, Color strokeColor, int numberOfSides, String shapeType) {
        super(startX, startY, endX, endY, strokeWidth, fillColor, strokeColor, shapeType);
        this.numberOfSides = numberOfSides;
    }

    /**
     * Getter function for the number of sides of the polygon.
     * @return the number of sides of the polygon
     */
    public int getNumberOfSides() {
        return numberOfSides;
    }
}
