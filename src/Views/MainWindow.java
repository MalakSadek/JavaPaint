package Views;

import Controller.Controller;
import Models.Model;
import Models.Shape;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Path2D;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Observable;
import java.util.Observer;


/**
 * This is the view class in the MVC, it handles anything related to the GUI and user interactions.
 */
public class MainWindow extends JFrame implements ActionListener, Observer {

	private Controller controller;
	private Model model;

	//GUI Components
	private JButton[] toolbarButtons;
	private JButton currentButton, polygon, delete;
	private JToolBar jtb1, jtb2;
	private JComboBox jcb;
	private JCheckBox fill;
	private JFileChooser jfc;
	private JMenuBar menu;
	private JLabel currentTool, currentColor, currentPartner;
	private JPanel canvas;
	private JMenu file, edit, draw, networking;
	private JMenuItem save, saveas, load, close, sync, connect, undo, redo, clear, color, line, backgroundcolor, circle, ellipse, hexagon, octagon, parallelogram, rectangle, square, triangle;
	private JSlider slider;
	private Rectangle boundingBox;

	//Internal attributes
	private Color selectedColor;
	private int selectedTool = -1, mouseStartX, mouseEndX, mouseStartY, mouseEndY, strokeWidth = 2;
	private Shape selectedShape, previousShape, drawingShape;
	private boolean fillShape = false, shiftPressed = false, equilateral = true, undoPressed = false, chosenSaveFile = false;
	private File saveFile;

	//////////////////////// Setup Methods ////////////////////////

	/**
	 * Constructor for the class, it connects to a controller and listens to a model.
	 * @param controller - the connected controller class which bridges between the view and model
	 * @param model - the model the view listens to and changes through the controller
	 */
	public MainWindow(Controller controller, Model model) {

		 //Sets up the controller and model and begins observing the model
		 this.controller = controller;
		 this.model = model;
		 model.addObserver(this);

		 //Setting up the GUI elements
		 setLayout(new BorderLayout());
		 jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		 boundingBox = new Rectangle();
		 polygon = new JButton("Draw Polygon");

		 delete = new JButton("Delete Shape");

		 Integer[] choices = { 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20 };
		 jcb = new JComboBox<Integer>(choices);

		 fill = new JCheckBox("Fill Shape");
		 currentTool = new JLabel("None");
		 currentColor = new JLabel("    ");

		 slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 2);

		 canvas = new JPanel();

		 jtb1 = new JToolBar("Drawing Tools", JToolBar.VERTICAL);
		 jtb2 = new JToolBar("Control Tools", JToolBar.HORIZONTAL);

		 menu = new javax.swing.JMenuBar();

		 toolbarButtons = new JButton[18];

		 selectedColor = Color.BLACK;

		 addToolbarElements();
		 addMenuElements();
		 addViewElements();
		 addActionListenerForComponents(this);

		//How to make the frame the same size as the screen obtained from:
		//https://stackoverflow.com/questions/3680221/how-can-i-get-screen-resolution-in-java

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int) screenSize.getWidth();
		int screenHeight = (int) screenSize.getHeight();
		setSize (screenWidth, screenHeight);

		//How to detect when the window is about to be closed obtained from:
		//https://stackoverflow.com/questions/9093448/how-to-capture-a-jframes-close-button-click-event

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeDialog();
			}
		});

		setVisible(true);
		revalidate();
		paintAll(getGraphics());

		//Detects key pressed and mouse movements
		detectKeyPress();
		monitorMouse();
	}

	/**
	 * This method detects if a keyboard key is pressed while the user is drawing on the canvas
	 */
	private void detectKeyPress() {
		canvas.setFocusable(true);
		canvas.requestFocus();
		canvas.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
			}

			@Override
			public void keyPressed(KeyEvent e) {
				//If the key pressed is the shift button, the currently selected tool is checked
				if(e.isShiftDown()) {
					shiftPressed = true;
					//If the selected tool is the ellipse tool, it is transformed into the circle tool
					if (toolbarButtons[4] == currentButton) {
						currentTool.setText("Circle Tool");
						selectedTool = 4;
					}
					//If the selected tool is the rectangle tool, it is transformed into the square tool
					else if (toolbarButtons[2] == currentButton) {
						currentTool.setText("Square Tool");
						selectedTool = 2;
					}
					//If the selected tool is the triangle tool, it is transformed into a free-form triangle instead of an equilateral one
					else if (toolbarButtons[1] == currentButton) {
						equilateral = false;
						currentTool.setText("Free-Form Triangle Tool");
						selectedTool = 1;
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				//If the shift button has been released, the currently selected tool is checked
				if(!e.isShiftDown()) {
					shiftPressed = false;
					//If the selected tool is the ellipse tool, it is transformed back into the ellipse tool instead of the circle tool
					if (toolbarButtons[4] == currentButton) {
						currentTool.setText("Ellipse Tool");
						selectedTool = 5;
					}
					//If the selected tool is the rectangle tool, it is transformed back into the rectangle tool instead of the square tool
					else if (toolbarButtons[2] == currentButton) {
						currentTool.setText("Rectangle Tool");
						selectedTool = 3;
					}
					//If the selected tool is the triangle tool, it is transformed back into the equilateral triangle tool instead of the free form triangle tool
					else if(toolbarButtons[1] == currentButton) {
						equilateral = true;
						currentTool.setText("Equilateral Triangle Tool");
						selectedTool = 1;
					}
				}
			}
		});
	}

	/**
	 * This method monitors the mouse's behaviour in relation to the canvas.
	 */
	private void monitorMouse() {
		canvas.setFocusable(true);
		canvas.grabFocus();
		addMouseListener(new MouseListener() {

			//If the mouse is clicked on the canvas, the clicked place becomes the end coordinates for the mouse and the start coordinates become the previous end coordinates
			@Override
			public void mouseClicked(MouseEvent e) {
				mouseStartX = mouseEndX;
				mouseStartY = mouseEndY;
				mouseEndX = e.getX();
				mouseEndY = e.getY();

				//If no tool is selected and the user has clicked, it's checked whether they have selected a shape
				if(selectedTool == -1) {
					//If no shape is selected, it attempts to select a shape
					if(selectedShape == null) {
						selectedShape = selectShape(mouseEndX, mouseEndY);
					}
					else {
						//If a shape is already selected and the user clicks on the same shape again, it is deselected
						if(selectShape(mouseEndX, mouseEndY)!=null && selectShape(mouseEndX, mouseEndY).equalColor(selectedShape)&&selectShape(mouseEndX, mouseEndY).equalStroke(selectedShape)&&selectShape(mouseEndX, mouseEndY).equalPosition(selectedShape)&&selectShape(mouseEndX, mouseEndY).equalSize(selectedShape)) {
							canvas.paintAll(getGraphics());
							revalidate();
							paintAll(getGraphics());
							selectedShape = null;
							redraw();
							delete.setVisible(false);
						}
						//If a shape is already selected and the user clicks elsewhere, the shape is moved to that point
						else {
							changeShapePosition();
							delete.setVisible(false);
						}

					}

				}
			}

			//If the mouse is pressed, that location becomes the mouse's new start position
			@Override
			public void mousePressed(MouseEvent e) {
				mouseStartX = e.getX();
				mouseStartY = e.getY();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				//If the mouse is released, the canvas is refreshed
				mouseEndX = e.getX();
				mouseEndY = e.getY();
				drawingShape = null;
				canvas.paintAll(getGraphics());
				revalidate();
				paintAll(getGraphics());
				redraw();

				//If the undo button was just pressed, and a new action was taken, this is considered a new path that deviates from the previous undo-redo tree
				//and so the redo stack is cleared out and the history starts over with this new action performed
				if(undoPressed) {
					toolbarButtons[11].setEnabled(false);
					redo.setEnabled(false);
					controller.clearRedo();
					undoPressed = false;
				}

				//If no tool is selected and a shape was selected, the selected shape's size is updated (because the user dragged it out or in while pressing shift)
				if(selectedTool == -1 && selectedShape != null){
					changeShapeSize();
					delete.setVisible(false);
				}
				//If any other tool is selected, then releasing the mouse draws the desired shape
				else
					drawShape(mouseStartX, mouseEndX, mouseStartY, mouseEndY, 0);

				//The history is checked to see whether the undo and redo buttons should be active or whether no actions can be undone or redone
				checkUndoRedo();
			}

			//When the mouse enters the canvas it's tracked
			@Override
			public void mouseEntered(MouseEvent e) {
				mouseStartX = mouseEndX;
				mouseStartY = mouseEndY;
			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});

		addMouseMotionListener(new MouseMotionListener() {

			//When the mouse is dragged, it's coordinates are updated and the canvas is redrawn
			@Override
			public void mouseDragged(MouseEvent e) {
				mouseEndX = e.getX();
				mouseEndY = e.getY();

				if (selectedTool != 1 && selectedTool != 6 && selectedTool != 7 && selectedTool !=8){
					redraw();
				}
			}

			//When the mouse moves it's tracked
			@Override
			public void mouseMoved(MouseEvent e) {
				mouseStartX = e.getX();
				mouseStartY = e.getY();
			}
		});
	}

	/**
	 * This method adds all the needed components to the top and bottom toolbars.
	 */
	private void addToolbarElements(){
		//This creates the slider that controls the shape's stroke width
		//Information obtained from: https://examples.javacodegeeks.com/desktop-java/swing/java-swing-slider-example/
		slider.setMajorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);
		Hashtable position = new Hashtable();
		position.put(1, new JLabel("1"));
		position.put(10, new JLabel("10"));
		position.put(20, new JLabel("20"));
		slider.setLabelTable(position);

		//This adds the buttons to the toolbar
		for(int i = 0; i < 18; i++) {
			toolbarButtons[i] = new JButton();
		}

		toolbarButtons[0].setText("Line Tool");
		toolbarButtons[1].setText("Triangle Tool");
		toolbarButtons[2].setText("Rectangle Tool");
		toolbarButtons[3].setText("Square Tool");
		toolbarButtons[4].setText("Ellipse Tool");
		toolbarButtons[5].setText("Circle Tool");
		toolbarButtons[6].setText("Hexagon Tool");
		toolbarButtons[7].setText("Octagon Tool");
		toolbarButtons[8].setText("Parallelogram Tool");
		toolbarButtons[9].setText("Choose Color");
		toolbarButtons[10].setText("Undo");
		toolbarButtons[11].setText("Redo");
		toolbarButtons[12].setText("Clear");
		toolbarButtons[13].setText("Save");
		toolbarButtons[14].setText("Deselect Tool");
		toolbarButtons[15].setText("Sync Canvas");
		toolbarButtons[16].setText("Disconnect");
		toolbarButtons[17].setText("Connect");

		//These are only available if a user connects to another user
		toolbarButtons[15].setVisible(false);
		toolbarButtons[16].setVisible(false);

		//Networking components
		currentPartner = new JLabel("No one");
		jtb2.add(new JLabel("Currently synced with: "));
		jtb2.add(currentPartner);
		jtb2.addSeparator(new Dimension(10, jtb2.getHeight()));
		jtb2.add(toolbarButtons[17]);
		jtb2.addSeparator(new Dimension(60, jtb2.getHeight()));

		//Currently selected color
		jtb2.add(new JLabel("Current Color: "));
		currentColor.setOpaque(true);
		currentColor.setBackground(Color.BLACK);
		jtb2.add(currentColor);
		jtb2.addSeparator(new Dimension(20, jtb2.getHeight()));

		//Currently selected tool
		jtb2.add(new JLabel("Current Tool: "));
		jtb2.add(currentTool);
		jtb2.addSeparator(new Dimension(20, jtb2.getHeight()));

		for (int i = 0; i < 18; i++) {
			if (i < 10) {
				jtb1.add(toolbarButtons[i]);
			} else if(i!=15 && i!=16 && i!=17){
				jtb2.add(toolbarButtons[i]);
			}
		}

		//The shape fill checkbox
		jtb2.add(fill);
		jtb2.addSeparator(new Dimension(60, jtb2.getHeight()));

		jtb2.add(delete);
		jtb1.addSeparator(new Dimension(20, jtb2.getHeight()));
		jtb1.add(new JLabel("Stroke Width: "));
		jtb1.add(slider);

		//Delete is only available if a shape is selected
		delete.setVisible(false);
		jtb2.add(toolbarButtons[15]);
		jtb2.add(toolbarButtons[16]);

		//The polygon number of sides combo box
		jtb1.add(new JLabel("More Sides:"));
		jtb1.add(jcb);
		jtb1.add(polygon);

		//Setting the toolbars' attributes
		jtb1.setOrientation(JToolBar.VERTICAL);
		jtb1.setLayout(new FlowLayout(FlowLayout.CENTER));
		jtb2.setLayout(new FlowLayout(FlowLayout.CENTER));
		jtb2.setOrientation(JToolBar.HORIZONTAL);
		jtb1.setFloatable(false);
		jtb2.setFloatable(false);
	}

	/**
	 * This method adds the toolbars, menus, and canvas to the frame.
	 */
	private void addViewElements(){
		getContentPane().add(canvas, BorderLayout.CENTER);
		getContentPane().add(jtb1, BorderLayout.SOUTH);
		getContentPane().add(jtb2, BorderLayout.NORTH);
		setJMenuBar(menu);

		//The canvas' default color is white
		canvas.setBackground(Color.WHITE);

		//This ensures that if the window is resized, all the components are redrawn on the canvas
		canvas.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				getGraphics().setClip(canvas.getX(), canvas.getY(), canvas.getWidth(), canvas.getHeight());
				canvas.paintAll(getGraphics());
				revalidate();
				paintAll(getGraphics());
				selectedShape = null;
				redraw();
			}
		});
	}

	/**
	 * This method adds all the buttons to the menus.
	 */
	private void addMenuElements(){
		//Top level menus
		 file = new JMenu ("File");
		 edit = new JMenu ("Edit");
		 draw = new JMenu ("Draw");
		 networking = new JMenu("Networking");

		 //File menu buttons
		 save = new JMenuItem ("Save");
		 saveas = new JMenuItem("Save As");
		 load = new JMenuItem ("Load");
		 close = new JMenuItem ("Close");
		 backgroundcolor = new JMenuItem("Change Background Color");

		 //Edit menu buttons
		 undo = new JMenuItem ("Undo");
		 redo = new JMenuItem ("Redo");
		 clear = new JMenuItem ("Clear");

		 //Undo and redo are disabled at the beginning
		 undo.setEnabled(false);
		 redo.setEnabled(false);
		 toolbarButtons[10].setEnabled(false);
		 toolbarButtons[11].setEnabled(false);

		 //Draw menu buttons
		 color = new JMenuItem ("Select Color");
		 line = new JMenuItem("Line Mode");
		 circle = new JMenuItem("Circle Tool");
		 ellipse = new JMenuItem("Ellipse Tool");
		 hexagon = new JMenuItem("Hexagon Tool");
		 octagon = new JMenuItem("Octagon Tool");
		 parallelogram = new JMenuItem("Parallelogram Tool");
		 rectangle = new JMenuItem("Rectangle Tool");
		 square = new JMenuItem("Square Tool");
		 triangle = new JMenuItem("Equilateral Triangle Tool");

		 //Networking menu buttons
		 sync = new JMenuItem("Sync canvas");
		 connect = new JMenuItem("Connect to user");

		file.add (save);
		file.add(saveas);
		file.add (load);
		file.add (close);
		file.add(backgroundcolor);

		edit.add (undo);
		edit.add (redo);
		edit.add(clear);

		networking.add(sync);
		networking.add(connect);

		draw.add(line);
		draw.add(color);
		draw.add(circle);
		draw.add(ellipse);
		draw.add(hexagon);
		draw.add(octagon);
		draw.add(parallelogram);
		draw.add(rectangle);
		draw.add(square);
		draw.add(triangle);

		menu.add(file);
		menu.add(edit);
		menu.add(draw);
		menu.add(networking);
	}

	/**
	 * This sets the action listener for various buttons and components.
	 * @param al - the action listener, which is just this frame
	 */
	private void addActionListenerForComponents(ActionListener al) {

		//Buttons
		for(int i = 0; i < 18; i++) {
			toolbarButtons[i].addActionListener(al);
		}

		save.addActionListener(al);
		saveas.addActionListener(al);
		load.addActionListener(al);
		close.addActionListener(al);
		undo.addActionListener(al);
		redo.addActionListener(al);
		clear.addActionListener(al);
		line.addActionListener(al);
		color.addActionListener(al);
		polygon.addActionListener(al);
		circle.addActionListener(al);
		ellipse.addActionListener(al);
		hexagon.addActionListener(al);
		octagon.addActionListener(al);
		parallelogram.addActionListener(al);
		rectangle.addActionListener(al);
		square.addActionListener(al);
		triangle.addActionListener(al);
		backgroundcolor.addActionListener(al);
		delete.addActionListener(al);
		sync.addActionListener(al);
		connect.addActionListener(al);

		//The stroke width slider
		slider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				if(e.getSource() == slider){
					//If no tool is selected and a shape is selected, the stroke width of the shape is updated
					changeShapeStrokeWidth();
					delete.setVisible(false);
				}
			}
		});

		//The fill shape checkbox
		fill.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getSource() == fill) {
					//If the checkbox is checked, then the next drawn shape will be filled.
					//If a shape is currently selected and is not filled, it will be filled with the currently selected color.
					if (e.getStateChange() == ItemEvent.SELECTED) {
						fillShape = true;
						if(selectedTool == -1 && selectedShape != null){
							changeShapeFill(1);
						}
					}
					//If the checkbox is unchecked, then the next drawn shape will be stroked.
					//If a shape is currently selected and is filled, it will be stroked with the currently selected color.
					else {
						fillShape = false;
						if(selectedTool == -1 && selectedShape != null){
							changeShapeFill(0);
						}
					}
					delete.setVisible(false);
					canvas.grabFocus();
				}
			}
		});
	}


	/**
	 * Getter function for the frame's graphics
	 * @return the frame's graphics
	 */
	@Override
	public Graphics getGraphics() {
		return super.getGraphics();
	}

	/**
	 * This method is called when the observed object (the model) is updated. The canvas is then redrawn.
	 * @param o - the observable object
	 * @param arg - any arguments passed
	 */
	@Override
	public void update(Observable o, Object arg) {
		redraw();
	}

	/**
	 * This method iterates over the shapes in the model and draws them on the canvas.
	 */
	private void redraw(){
		canvas.grabFocus();
		for (Shape n : model.getShapes()) {
			createShape(n);
		}
	}

	//////////////////////// Editing Methods ////////////////////////

	/**
	 * This method is called when the mouse is clicked on the canvas. It calls the controller's selectShape method.
	 * @param mx - the mouse's x coordinate
	 * @param my - the mouse's y coordinate
	 * @return the selected shape or null if no shape is selected
	 */
	private Shape selectShape(int mx, int my){
		Shape s = null;
		try{
			s = new Shape (controller.selectShape(mx, my));
		} catch(NullPointerException ignored){

		}

		//If a shape has been selected, a bounding box is drawn around it and the delete button is set to visible
		if(s != null) {
			if(s.getShapeType().equals("Line"))
				boundingBox.setRect(s.getStartX()-10, s.getStartY()-10, Math.abs(s.getStartX()-s.getEndX())+20, Math.abs(s.getStartY()-s.getEndY())+20);
			else if (s.getShapeType().equals("Parallelogram"))
				boundingBox.setRect(Math.min(s.getEndX(), s.getStartX())-10, Math.min(s.getStartY(), s.getEndY())-10, Math.abs(s.getStartX()-s.getEndX())+200, Math.abs(s.getStartY()-s.getEndY())+20);
			else
				boundingBox.setRect(Math.min(s.getEndX(), s.getStartX())-10, Math.min(s.getStartY(), s.getEndY())-10, Math.abs(s.getStartX()-s.getEndX())+20, Math.abs(s.getStartY()-s.getEndY())+20);
			Graphics2D g = (Graphics2D) getGraphics();
			g.setColor(Color.gray);
			g.draw(boundingBox);
			delete.setVisible(true);
		}
		//If no shape is selected, the bounding box is deleted and the delete button is set to invisible
		else {
			delete.setVisible(false);
			getGraphics().dispose();
		}

		return s;
	}

	/**
	 * This method changes an existing shape's position.
	 */
	private void changeShapePosition(){
		int width, height;
		width = Math.abs(selectedShape.getStartX() - selectedShape.getEndX());
		height = Math.abs(selectedShape.getStartY() - selectedShape.getEndY());
		previousShape = new Shape(selectedShape);
		selectedShape.setStartX(mouseEndX);
		selectedShape.setStartY(mouseEndY);
		selectedShape.setEndX(mouseEndX+width);
		selectedShape.setEndY(mouseEndY+height);
		canvas.paintAll(getGraphics());
		revalidate();
		paintAll(getGraphics());
		controller.updateModel(selectedShape, previousShape, 1);
		selectedShape = null;
		redraw();
	}

	/**
	 * This method changes an existing shape's size.
	 */
	private void changeShapeSize(){
		if(shiftPressed) {
			previousShape = new Shape(selectedShape);
			selectedShape.setEndX(mouseEndX);
			selectedShape.setEndY(mouseEndY);
			canvas.paintAll(getGraphics());
			revalidate();
			paintAll(getGraphics());
			controller.updateModel(selectedShape, previousShape, 2);
			selectedShape = null;
			redraw();
		}
	}

	/**
	 * This method changes an existing shape's stroke width.
	 */
	private void changeShapeStrokeWidth() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				strokeWidth = slider.getValue();
				if(selectedTool == -1 && selectedShape != null){
					previousShape = new Shape(selectedShape);
					selectedShape.setStrokeWidth(strokeWidth);
					canvas.paintAll(getGraphics());
					revalidate();
					paintAll(getGraphics());
					controller.updateModel(selectedShape, previousShape, 3);
					selectedShape = null;
					redraw();
				}
			}
		});
	}

	/**
	 * This method changes an existing shape's fill.
	 */
	private void changeShapeFill(int fill) {
		if(fill == 0) {
			previousShape = new Shape(selectedShape);
			selectedShape.setStrokeColor(selectedColor);
			selectedShape.setFillColor(null);
			canvas.paintAll(getGraphics());
			revalidate();
			paintAll(getGraphics());
			controller.updateModel(selectedShape, previousShape, 0);
			selectedShape = null;
			redraw();
		} else if (fill == 1) {
			previousShape = new Shape(selectedShape);
			selectedShape.setFillColor(selectedColor);
			canvas.paintAll(getGraphics());
			revalidate();
			paintAll(getGraphics());
			controller.updateModel(selectedShape, previousShape, 0);
			selectedShape = null;
			redraw();
		}
	}

	/**
	 * This method changes an existing shape's color.
	 */
	private void changeShapeColor(Color color){

		previousShape = new Shape(selectedShape);

		if(selectedShape.getFillColor() == null) {
			selectedShape.setStrokeColor(color);
		} else {
			selectedShape.setFillColor(color);
		}
		canvas.paintAll(getGraphics());
		revalidate();
		paintAll(getGraphics());
		controller.updateModel(selectedShape, previousShape, 0);
		selectedShape = null;
		delete.setVisible(false);
		redraw();
	}

	//////////////////////// Saving & Loading & Undo/Redo Methods ////////////////////////

	/**
	 * This method is called to save the canvas to a file.
	 */
	private void saveCanvas() {
		//JFileChooser example obtained from:
		//https://www.mkyong.com/swing/java-swing-jfilechooser-example/
		//If a file has been chosen before, it is saved to by default unless the user selects 'save as'
		if(!chosenSaveFile) {
			jfc.setDialogTitle("Choose a directory to save your file: ");
			jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			int returnValue = jfc.showSaveDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				System.out.println("You selected the directory: " + jfc.getSelectedFile());
				controller.save(jfc.getSelectedFile());
				chosenSaveFile = true;
				saveFile = jfc.getSelectedFile();
			}
		} else {
			controller.save(saveFile);
		}
	}

	/**
	 * This method loads the canvas from a file.
	 */
	private void loadCanvas(){

		//JFileChooser example obtained from:
		//https://www.mkyong.com/swing/java-swing-jfilechooser-example/
		JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter("SER Files", "ser", "SER");
		jfc.addChoosableFileFilter(filter);

		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			model = controller.load(jfc.getSelectedFile());
		}

		if(model == null)
			model = new Model();

		canvas.paintAll(getGraphics());
		revalidate();
		paintAll(getGraphics());
		redraw();
	}

	/**
	 * This method checks the mode's undo and redo stacks to enable or disable the buttons accordingly
	 */
	private void checkUndoRedo(){
		if(model.getActions().empty()){
			undo.setEnabled(false);
			toolbarButtons[10].setEnabled(false);
		} else {
			undo.setEnabled(true);
			toolbarButtons[10].setEnabled(true);

			if(controller.stillRedo()) {
				toolbarButtons[11].setEnabled(true);
				redo.setEnabled(true);
			} else {
				toolbarButtons[11].setEnabled(false);
				redo.setEnabled(false);
			}
		}
	}

	//////////////////////// Dialog Methods ////////////////////////

	/**
	 * This method shows a dialog that asks the user if they want to save before quitting.
	 */
	private void closeDialog() {
		//How to have custom buttons in a dialog obtained from:
		//https://stackoverflow.com/questions/13334198/java-custom-buttons-in-showinputdialog

		//This dialog asks the user if they want to just close the application or save first
		Object[] options1 = { "Close", "Save First",
				"Cancel" };

		JPanel panel = new JPanel();
		panel.add(new JLabel("Are you sure you want to close?"));

		int result = JOptionPane.showOptionDialog(null, panel, "Quit Malak's Paint",
				JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options1, null);

		//If the user just wants to quit the application
		if (result == JOptionPane.YES_OPTION){
			setVisible(false);
			dispose();
		}
		//If the user wants to save first
		else if (result == JOptionPane.NO_OPTION){
			saveCanvas();
		}
	}

	/**
	 * This method opens a dialog to ask the user to enter the IP address of the user they want to connect to.
	 */
	private void connectDialog() {

		//How to have custom buttons in a dialog obtained from:
		//https://stackoverflow.com/questions/13334198/java-custom-buttons-in-showinputdialog

		//Creates a dialog to ask the user for the IP address of the user they want to connect to
		Object[] options = { "Connect",
				"Cancel" };

		JPanel panel = new JPanel();
		panel.add(new JLabel("Please enter the IP Address you want to connect to: "));
		JTextField IPAddress = new JTextField("IP Address");
		panel.add(IPAddress);

		int result = JOptionPane.showOptionDialog(null, panel, "Connect to User",
				JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE,
				null, options, null);
		if (result == JOptionPane.YES_OPTION){
			try {
				controller.connect(IPAddress.getText());
				currentPartner.setText(IPAddress.getText());

				//Disconnect and sync buttons become visible, connect button becomes invisible
				toolbarButtons[15].setVisible(true);
				toolbarButtons[16].setVisible(true);
				toolbarButtons[17].setVisible(false);
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	//////////////////////// Drawing Methods ////////////////////////

	/**
	 * This method sets the point of an n sided polygon and is also used to create hexagons and octagons
	 * @param polygon - the polygon to add points to
	 * @param n - the shape who's coordinates should be used
	 * @param numberOfSides - the desired number of sides in the polygon
	 */
	private void setupPolygon(Polygon polygon, Shape n, int numberOfSides){
		//How to draw a polygon obtained from:
		//https://stackoverflow.com/questions/35853902/drawing-hexagon-using-java-error
		for (int i = 0; i < numberOfSides; i++) {
			int xval = (int) (n.getStartX()+((n.getEndX()-n.getStartX())/2) + (n.getEndX()-n.getStartX())/2
					* Math.cos(i * 2 * Math.PI / numberOfSides));
			int yval = (int) (n.getStartY()+((n.getEndY()-n.getStartY())/2) + (n.getEndY()-n.getStartY())/2
					* Math.sin(i * 2 * Math.PI / numberOfSides));
			polygon.addPoint(xval, yval);
		}
	}

	/**
	 * This method draws the given shape.
	 * @param n - the shape to be drawn
	 */
	private void createShape(Shape n){

		//Set shape fill or stroke color
		Graphics2D g = (Graphics2D) getGraphics();
		if(n.getFillColor() == null)
			g.setColor(n.getStrokeColor());
		else
			g.setColor(n.getFillColor());

		//Set shape stroke width
		g.setStroke(new BasicStroke(n.getStrokeWidth()));

		//This finds the minimum and maximum x and y coordinates of the shape
		//because the starting coordinates might not necessarily be smaller than the ending ones
		//if the user draws from right to left or top to bottom
		int minX = Math.min(n.getStartX(), n.getEndX());
		int minY = Math.min(n.getStartY(), n.getEndY());
		int maxX = Math.max(n.getStartX(), n.getEndX());
		int maxY = Math.max(n.getStartY(), n.getEndY());

		Polygon polygon;

		//This draws the appropriate graphics based on the shape type
		switch(n.getShapeType()){
			case "Circle":
				if(n.getFillColor() == null){
					g.drawOval(minX, minY, maxX - minX, maxX - minX);
				} else {
					g.drawOval(minX, minY, maxX - minX, maxX - minX);
				}
				break;
			case "Ellipse":
				if(n.getFillColor() == null){
					g.drawOval(minX, minY, maxX - minX, maxY - minY);
				} else {
					g.drawOval(minX, minY, maxX - minX, maxY - minY);
				}
				break;
			case "Hexagon":
				polygon = new Polygon();

				setupPolygon(polygon, n, 6);

				if(n.getFillColor() == null){
					g.drawPolygon(polygon);
				} else {
					g.fillPolygon(polygon);
				}

				break;
			case "Line":
				g.drawLine(n.getStartX(), n.getEndX(), n.getStartY(), n.getEndY());
				break;
			case "Octagon":
				polygon = new Polygon();

				setupPolygon(polygon, n, 8);

				if(n.getFillColor() == null){
					g.drawPolygon(polygon);
				} else {
					g.fillPolygon(polygon);
				}

				break;
			case "Parallelogram":
				//How to draw a parallelogram obtained from:
				//https://stackoverflow.com/questions/28017537/how-do-you-draw-and-fill-a-parallelogram-in-java-swing

				Path2D.Double parallelogram;
				parallelogram = new Path2D.Double();
				if(n.getStartX() > n.getEndX()) {
					parallelogram.moveTo(n.getEndX(),n.getEndY());
					parallelogram.lineTo(n.getStartX(),n.getEndY());
					parallelogram.lineTo(n.getStartX()+(Math.abs(n.getEndX()-n.getStartX())),n.getStartY());
					parallelogram.lineTo(n.getStartX(),n.getStartY());
					parallelogram.lineTo(n.getEndX(),n.getEndY());
				} else {
					parallelogram.moveTo(n.getStartX(),n.getStartY());
					parallelogram.lineTo(n.getEndX(),n.getStartY());
					parallelogram.lineTo(n.getEndX()+(Math.abs(n.getEndX()-n.getStartX())),n.getEndY());
					parallelogram.lineTo(n.getEndX(),n.getEndY());
					parallelogram.lineTo(n.getStartX(),n.getStartY());
				}

				if(n.getFillColor() == null){
					g.draw(parallelogram);
				} else {
					g.fill(parallelogram);
				}

				break;
			case "Rectangle":
				if(n.getFillColor() == null){
					g.drawRect(minX, minY, maxX - minX, maxY - minY);
				} else {
					g.fillRect(minX, minY, maxX - minX, maxY - minY);
				}
				break;
			case "Square":
				if(n.getFillColor() == null){
					g.drawRect(minX, minY, maxX - minX, maxX - minX);
				} else {
					g.fillRect(minX, minY, maxX - minX, maxX - minX);
				}
				break;
			case "Triangle":
				if(equilateral) {
					//Equilateral triangle, obtained from:
					//https://stackoverflow.com/questions/29447994/how-do-i-draw-a-triangle-in-java
					if(n.getFillColor() == null){
						g.drawPolygon(new Polygon(new int[] {n.getStartX(), n.getStartX()+((n.getEndX()-n.getStartX())/2), n.getEndX()}, new int[] {n.getStartY(), n.getEndY(), n.getStartY()}, 3));
					} else {
						g.fillPolygon(new Polygon(new int[] {n.getStartX(), n.getStartX()+((n.getEndX()-n.getStartX())/2), n.getEndX()}, new int[] {n.getStartY(), n.getEndY(), n.getStartY()}, 3));
					}
				} else {
					//Free-form triangle, adapted from the 'how to draw a parallelogram' source through experimentation
					Path2D.Double triangle;
					triangle = new Path2D.Double();
					triangle.moveTo(mouseStartX,mouseStartY);
					triangle.lineTo(mouseEndX,mouseStartY);
					triangle.lineTo(mouseEndX,mouseEndY);
					triangle.closePath();
					if(n.getFillColor() == null){
						g.draw(triangle);
					} else {
						g.fill(triangle);
					}
				}
				break;
			case "Polygon":
				polygon = new Polygon();
				setupPolygon(polygon, n, ((Models.Polygon) n).getNumberOfSides());

				if(n.getFillColor() == null){
					g.drawPolygon(polygon);
				} else {
					g.fillPolygon(polygon);
				}
				break;
			default:
				break;
		}
	}

	/**
	 * This method either calls the needed controller method to create the appropriate shape based on the selected tool.
	 * Or just creates a shape to be displayed as the user is dragging out to draw (and then removed afterwards)
	 * @param sX - the shape's starting x coordinate
	 * @param eX - the shape's ending x coordinate
	 * @param sY - the shape's starting y coordinate
	 * @param eY - the shape's ending y coordinate
	 * @param type - whether the function is called to have the controller create the shape or just display it as the user is dragging
	 */
	private void drawShape(int sX, int eX, int sY, int eY, int type) {
		canvas.grabFocus();
		switch (selectedTool){
			//Line
			case 0:
				controller.lineTool(sX, sY, eX, eY, strokeWidth, fillShape);
				break;
			//Triangle
			case 1:
				controller.triangleTool(sX, eX, sY, eY, strokeWidth, fillShape, equilateral);
				break;
			//Square
			case 2:
				controller.squareTool(sX, eX, sY, eY, strokeWidth, fillShape);
				break;
			//Rectangle
			case 3:
				controller.rectangleTool(sX, eX, sY, eY, strokeWidth, fillShape);
				break;
			//Circle
			case 4:
				controller.circleTool(sX, eX, sY, eY, strokeWidth, fillShape);
				break;
			//Ellipse
			case 5:
				controller.ellipseTool(sX, eX, sY, eY, strokeWidth, fillShape);
				break;
			//Hexagon
			case 6:
				controller.hexagonTool(sX, eX, sY, eY, strokeWidth, fillShape);
				//Octagon
				break;
			case 7:
				controller.octagonTool(sX, eX, sY, eY, strokeWidth, fillShape);
				break;
			//Parallelogram
			case 8:
				controller.parallelogramTool(sX, eX, sY, eY, strokeWidth, fillShape);
				break;
			//Polygon - the number of sides is obtained from the combo box (jcb)
			case 9:
				controller.drawPolygon(sX, eX, sY, eY, strokeWidth, fillShape, (Integer)jcb.getSelectedItem());
			default:
				break;
		}
	}

	/**
	 * This method is called whenever a button is clicked.
	 * @param e - the action event
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		//The current tool is automatically deselected when another button is pressed
		canvas.requestFocus();
		selectedTool = -1;
		selectedShape = null;
		currentTool.setText("None");
		currentButton = null;
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

		//Line button pressed
		if((e.getSource() == toolbarButtons[0])||(e.getSource() == line)) {
			selectedTool = 0;
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			currentButton = toolbarButtons[0];
			currentTool.setText("Line Tool");
		}

		//Triangle button pressed
		else if ((e.getSource() == toolbarButtons[1]||(e.getSource() == triangle))) {
			currentTool.setText("Equilateral Triangle Tool");
			currentButton = toolbarButtons[1];
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			selectedTool = 1;
		}

		//Rectangle button pressed
		else if ((e.getSource() == toolbarButtons[2])||(e.getSource() == rectangle)) {
			currentTool.setText("Rectangle Tool");
			currentButton = toolbarButtons[2];
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			selectedTool = 3;
		}

		//Square button pressed
		else if ((e.getSource() == toolbarButtons[3])||(e.getSource() == square)) {
			currentTool.setText("Square Tool");
			currentButton = toolbarButtons[3];
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			selectedTool = 2;
		}

		//Ellipse button pressed
		else if ((e.getSource() == toolbarButtons[4])||(e.getSource() == ellipse)) {
			currentTool.setText("Ellipse Tool");
			currentButton = toolbarButtons[4];
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			selectedTool = 5;
		}

		//Circle button pressed
		else if ((e.getSource() == toolbarButtons[5])||(e.getSource() == circle)) {
			currentTool.setText("Circle Tool");
			currentButton = toolbarButtons[5];
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			selectedTool = 4;
		}

		//Hexagon button pressed
		else if ((e.getSource() == toolbarButtons[6])||(e.getSource() == hexagon)) {
			currentTool.setText("Hexagon Tool");
			currentButton = toolbarButtons[6];
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			selectedTool = 6;
		}

		//Octagon button pressed
		else if ((e.getSource() == toolbarButtons[7])||(e.getSource() == octagon)) {
			currentTool.setText("Octagon Tool");
			currentButton = toolbarButtons[7];
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			selectedTool = 7;
		}

		//Parallelogram button pressed
		else if ((e.getSource() == toolbarButtons[8])||(e.getSource() == parallelogram)) {
			currentTool.setText("Parallelogram Tool");
			currentButton = toolbarButtons[8];
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			selectedTool = 8;
		}

		//Choose color button pressed
		else if ((e.getSource() == toolbarButtons[9]) || (e.getSource() == color)) {

			//Opens a new color chooser
			Color color = JColorChooser.showDialog(this,"Color Chooser", Color.BLACK);

			if(color == null) {
				color = selectedColor;
			}
			currentColor.setBackground(color);
			controller.chooseColor(color);
			selectedColor = color;

			//If no tool is selected and a shape is selected, its color is changed
			if(selectedTool == -1 && selectedShape != null){
				changeShapeColor(selectedColor);
				delete.setVisible(false);
			}
		}

		//Undo button pressed
		else if ((e.getSource() == toolbarButtons[10])||(e.getSource()==undo)) {
			controller.undo();

			//If there are no more actions left to undo, the undo button is disabled
			if(model.getActions().empty()) {
				toolbarButtons[11].setEnabled(true);
				redo.setEnabled(true);
				undoPressed = true;
			}
			canvas.paintAll(getGraphics());
			revalidate();
			paintAll(getGraphics());
			redraw();
		}

		//Redo button pressed
		else if ((e.getSource() == toolbarButtons[11])||(e.getSource() == redo)) {
			controller.redo();

			//If there are no more actions left to redo
			if(!controller.stillRedo()) {
				redo.setEnabled(false);
				toolbarButtons[11].setEnabled(false);
			}
			canvas.paintAll(getGraphics());
			revalidate();
			paintAll(getGraphics());
			redraw();
		}

		//Clear button pressed
		else if ((e.getSource() == toolbarButtons[12])||(e.getSource() == clear)) {
			if(JOptionPane.showConfirmDialog(getParent(), "Are you sure? This will erase everything, including your history!") == JOptionPane.OK_OPTION){
				controller.clear();
				undo.setEnabled(false);
				redo.setEnabled(false);
				delete.setVisible(false);
				canvas.repaint();
			}
		}

		//Save button pressed
		else if ((e.getSource() == toolbarButtons[13])||(e.getSource() == save)||(e.getSource() == saveas)) {

			//If the Save As button is pressed, or the Save button is pressed for the first time, the user is given a file chooser
			//If the user has previously chosen a save file then this is used by default
			if(e.getSource() == saveas)
				chosenSaveFile = false;

			saveCanvas();
		}

		//Deselect tool button pressed
		else if (e.getSource() == toolbarButtons[14]) {
			selectedTool = -1;
			currentTool.setText("None");
			currentButton = null;
			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}

		//Set background color button pressed (menu only)
		else if (e.getSource() == backgroundcolor) {
			Color color = JColorChooser.showDialog(this,"Color Chooser", Color.BLACK);
			canvas.setBackground(color);
		}

		//Close button pressed (menu only)
		else if (e.getSource() == close) {
			closeDialog();
		}

		//Load button pressed (menu only)
		else if (e.getSource() == load) {
			loadCanvas();
		}

		//Polygon button pressed
		else if (e.getSource() == polygon) {
			currentTool.setText("Polygon Tool");
			currentButton = polygon;
			setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
			selectedTool = 9;
		}

		//Delete button pressed
		else if (e.getSource() == delete){
			canvas.paintAll(getGraphics());
			revalidate();
			controller.updateModel(selectedShape, null, 4);
			controller.delete(selectedShape);
			selectedShape = null;
			delete.setVisible(false);
			paintAll(getGraphics());
			redraw();
		}

		//Connect button pressed
		else if (e.getSource() == connect || e.getSource() == toolbarButtons[17]) {
			connectDialog();
		}

		//Sync button pressed
		else if (e.getSource() == sync || e.getSource() == toolbarButtons[15]) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					model = controller.sync();
					canvas.grabFocus();
					canvas.paintAll(getGraphics());
					revalidate();
					paintAll(getGraphics());
					redraw();
				}
			});
		}

		//Disconnect button pressed
		else if (e.getSource() == toolbarButtons[16]) {
			//Disconnect and sync buttons become invisible, connect button becomes visible
			toolbarButtons[15].setVisible(false);
			toolbarButtons[16].setVisible(false);
			toolbarButtons[17].setVisible(true);
			controller.disconnect();
			currentPartner.setText("No one");
		}
		checkUndoRedo();
	}
}
