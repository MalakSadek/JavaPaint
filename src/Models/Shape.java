package Models;

import java.awt.*;
import java.io.Serializable;

/**
 * This class represents a shape object and all specific shape objects inherit from it.
 * @author malaksadek
 */
public class Shape implements Serializable {
    private static final long serialVersionUID = 2;
    private int startX, startY, endX, endY, strokeWidth;
    String shapeType;
    private Color fillColor, strokeColor;

    /**
     * Constructor for the class which takes a shape and creates one like it.
     * @param s - the shape to be copied
     */
    public Shape(Shape s){
        this.startX = s.startX;
        this.startY = s.startY;
        this.endX = s.endX;
        this.endY = s.endY;
        this.strokeWidth = s.strokeWidth;
        this.fillColor = s.fillColor;
        this.strokeColor = s.strokeColor;
        this.shapeType = s.shapeType;
    }

    /**
     * Constructor for the class which creates a shape.
     * @param startX - the starting x coordinate for the shape
     * @param startY - the starting y coordinate for the shape
     * @param endX - the ending x coordinate for the shape
     * @param endY - the ending y coordinate for the shape
     * @param strokeWidth - the stroke width of the shape
     * @param fillColor - the fill color of the shape, set to null if the shape isn't filled
     * @param strokeColor - the stroke color of the shape, set to null if the shape is filled
     * @param shapeType - the type of the shape described as a string
     */
    public Shape(int startX, int startY, int endX, int endY, int strokeWidth, Color fillColor, Color strokeColor, String shapeType) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.fillColor = fillColor;
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
        this.shapeType = shapeType;
    }

    ////////////// Getter methods //////////////

    /**
     * Getter method for the fill color.
     * @return fillColor
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * Getter method for the stroke color.
     * @return strokeColor
     */
    public Color getStrokeColor() {
        return strokeColor;
    }

    /**
     * Getter method for the shape's end x coordinate.
     * @return end x coordinate
     */
    public int getEndX() {
        return endX;
    }

    /**
     * Getter method for the shape's end y coordinate.
     * @return end y coordinate
     */
    public int getEndY() {
        return endY;
    }

    /**
     * Getter method for the shape's start x coordinate.
     * @return start x coordinate
     */
    public int getStartX() {
        return startX;
    }

    /**
     * Getter method for the shape's start y coordinate.
     * @return start y coordinate
     */
    public int getStartY() {
        return startY;
    }

    /**
     * Getter method for the stroke width.
     * @return strokeWidth
     */
    public int getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * Getter method for the shape's type.
     * @return shapeType
     */
    public String getShapeType() {
        return shapeType;
    }

    ////////////// Setter methods //////////////

    /**
     * Setter method for the shape's end x coordinate.
     * @param endX - the shape's end x coordinate
     */
    public void setEndX(int endX) {
        this.endX = endX;
    }

    /**
     * Setter method for the shape's end y coordinate.
     * @param endY - the shape's end y coordinate
     */
    public void setEndY(int endY) {
        this.endY = endY;
    }

    /**
     * Setter method for the shape's fill color.
     * @param fillColor - the shape's fill color
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Setter method for the shape's start x coordinate.
     * @param startX - the shape's start x coordinate
     */
    public void setStartX(int startX) {
        this.startX = startX;
    }

    /**
     * Setter method for the shape's start y coordinate.
     * @param startY - the shape's start y coordinate
     */
    public void setStartY(int startY) {
        this.startY = startY;
    }

    /**
     * Setter method for the shape's stroke color.
     * @param strokeColor - the shape's stroke color
     */
    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    /**
     * Setter method for the shape's stroke width.
     * @param strokeWidth - the shape's stroke width
     */
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    ////////////// Equality methods //////////////

    //These are used to be able to identify the same shape before and after a change has been done to it
    //The assumption is that if two shapes are the same across all attributes except the changed one, they are deemed equivalent.
    //This is not ideal, but works for most cases. There are methods for when the change is in the:
    //Color, position, size, and stroke - since these are the things the user can change in a shape

    /**
     * This method checks whether the given shape is equivalent to this one across all attributes except its fill or stroke color.
     * @param s1 - the shape to compare to
     * @return true or false depending on whether they are identical
     */
    public boolean equalColor(Shape s1){

            return (
                    (s1.getStrokeWidth() == this.getStrokeWidth()) &&
                    (s1.getShapeType().equals(this.getShapeType())) &&
                    (s1.getStartY() == this.getStartY()) &&
                    (s1.getEndY() == this.getEndY()) &&
                    (s1.getStartX() == this.getStartX()) &&
                    (s1.getEndX() == this.getEndX()));
    }

    /**
     * This method checks whether the given shape is equivalent to this one across all attributes except its start and end x and y coordinates.
     * It does also check the shape's width and height which is not supposed to change
     * @param s1 - the shape to compare to
     * @return true or false depending on whether they are identical
     */
    public boolean equalPosition(Shape s1){
        int widths1 = Math.abs(s1.getEndX() - s1.getStartX());
        int heights1 = Math.abs(s1.getEndY() - s1.getStartY());
        int widthThis = Math.abs(this.getEndX() - this.getStartX());
        int heightThis = Math.abs(this.getEndY() - this.getStartY());

        int strokeOrFill = -1;
        if((s1.getStrokeColor() != null) && (this.getStrokeColor() != null)){
            strokeOrFill = 0;
        } else if((s1.getStrokeColor() == null) && (this.getStrokeColor() == null)) {
            strokeOrFill = 1;
        } else return false;

        if(strokeOrFill == 0) {
            return ((s1.getStrokeColor().equals(this.getStrokeColor())) &&
                    (s1.getStrokeWidth() == this.getStrokeWidth()) &&
                    (widths1 == widthThis) &&
                    (heights1 == heightThis) &&
                    (s1.getShapeType().equals(this.getShapeType())));
        } else {
            return ((s1.getFillColor().equals(this.getFillColor())) &&
                    (s1.getStrokeWidth() == this.getStrokeWidth()) &&
                    (widths1 == widthThis) &&
                    (heights1 == heightThis) &&
                    (s1.getShapeType().equals(this.getShapeType())));
        }
    }

    /**
     * This method checks whether the given shape is equivalent to this one across all attributes except its size.
     * It's expected to have the same starting x and y coordinates.
     * @param s1 - the shape to compare to
     * @return true or false depending on whether they are identical
     */
    public boolean equalSize(Shape s1){
        int strokeOrFill = -1;
        if((s1.getStrokeColor() != null) && (this.getStrokeColor() != null)){
            strokeOrFill = 0;
        } else if((s1.getStrokeColor() == null) && (this.getStrokeColor() == null)) {
            strokeOrFill = 1;
        } else return false;

        if(strokeOrFill == 0) {
            return ((s1.getStrokeColor().equals(this.getStrokeColor())) &&
                    (s1.getStrokeWidth() == this.getStrokeWidth()) &&
                    (s1.getShapeType().equals(this.getShapeType())) &&
                    (s1.getStartX() == this.getStartX()) &&
                    (s1.getStartY() == this.getStartY()));
        } else {

            return ((s1.getFillColor().equals(this.getFillColor())) &&
                    (s1.getStrokeWidth() == this.getStrokeWidth()) &&
                    (s1.getShapeType().equals(this.getShapeType())) &&
                    (s1.getStartX() == this.getStartX()) &&
                    (s1.getStartY() == this.getStartY()));
        }
    }

    /**
     * This method checks whether the given shape is equivalent to this one across all attributes except its stroke color.
     * @param s1 - the shape to compare to
     * @return true or false depending on whether they are identical
     */
    public boolean equalStroke(Shape s1){
        int strokeOrFill = -1;
        if((s1.getStrokeColor() != null) && (this.getStrokeColor() != null)){
            strokeOrFill = 0;
        } else if((s1.getStrokeColor() == null) && (this.getStrokeColor() == null)) {
            strokeOrFill = 1;
        } else return false;

        if(strokeOrFill == 0) {
            return ((s1.getStrokeColor().equals(this.getStrokeColor())) &&
                    (s1.getEndX() == this.getEndX()) &&
                    (s1.getEndY() == this.getEndY()) &&
                    (s1.getShapeType().equals(this.getShapeType())) &&
                    (s1.getStartX() == this.getStartX()) &&
                    (s1.getStartY() == this.getStartY()));
        } else {
            return ((s1.getFillColor().equals(this.getFillColor())) &&
                    (s1.getEndX() == this.getEndX()) &&
                    (s1.getEndY() == this.getEndY()) &&
                    (s1.getShapeType().equals(this.getShapeType())) &&
                    (s1.getStartX() == this.getStartX()) &&
                    (s1.getStartY() == this.getStartY()));
        }
    }

}
