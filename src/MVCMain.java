import Controller.Controller;
import Models.Model;
import Views.MainWindow;

/**
 * The main class for running the MVC application.
 */
public class MVCMain {

    /**
     * This is the main method to start the application, it creates a new model and passes that to a new controller and then creates a new view connected to both.
     * @param argv - no arguments needed in this case.
     */
    public static void main (String argv[]) {
        Model model = new Model();
        Controller controller = new Controller(model);
        new MainWindow(controller, model);
    }
}
