# Java Paint
A Java program that has the same features as Microsoft Paint 🎨 💻 (2019).

This GUI-based application implemented in Java and the `Java/FX` and `Java/Swing` libraries. It displays a canvas and a set of tools to allow the user to draw among a set of other features such as saving/loading canvases or syncing canvases with another user.

## Features:
* Draw lines, rectangles, squares, circles, triangles, ellipses, hexagons, octagons, and other polygons (specify number of sides)
* Fill the shapes with color, draw them as outlines only, change the stroke width and color
* Resize or move shapes after they are drawn
* Clear the canvas or change its background color
* Undo or redo any action
* Save the file (by serializing it) or load an existing file
* Connect to another user and sync canvases with them (each person gets all the other drawings the person has made added to their canvas)

### Screenshot of GUI:

![picture alt](https://github.com/MalakSadek/JavaPaint/blob/master/Screenshot.png "Screenshot of GUI")

### Installation

Clone the project: 
            `git clone https://github.com/MalakSadek/JavaPaint`

Cd into the directory and compile the files, then run the source code:

            cd JavaPaint
            javac src/*.java src/*.java


## Usage

Run the GUI:

            cd src/
            java MVCMain
            
 Run the server:
 
            cd src/
            java ServerMain
            
 # Javadocs

    1. Generate the Javadocs: javadoc -d javadoc src/*.java src/exception/*.java

    2. Open javadoc/index.html in your web browser.

# Contact

* email: mfzs1@st-andrews.ac.uk
* LinkedIn: www.linkedin.com/in/malak-sadek-17aa65164/
* website: http://malaksadekapps.com/
