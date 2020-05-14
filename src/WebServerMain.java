import java.io.IOException;

/**
 * A class for the main that runs the server.
   @author Malak Sadek
 */
public class WebServerMain {

    //These are made static as many other classes access them (to create the resource folder for example)
    private static int listeningPort;

    /**
     * The main function for running the server.
     * It takes in the port the server will listen on
     * @param args -  port number
     */
    public static void main(String[] args) {
        //Invalid command
        if (args.length < 1) {
            System.out.print("Usage: java WebServerMain <port>");
            System.exit(1);
        } else {

            //Try-catch block with exception obtained from: https://www.baeldung.com/java-check-string-number
            try {
                listeningPort = Integer.parseInt(args[0]);
                WebServer s = new WebServer();
            } catch (NumberFormatException nfe) {
                System.out.print("Invalid port! Usage: java WebServerMain <document_root> <port>");
                System.exit(1);
            } catch (IOException ioe) {
                System.out.print("Could not listen on port! Usage: java WebServerMain <document_root> <port>");
                System.exit(1);
            }
        }
    }

    /**
     * Getter function for listeningPort.
     * @return - listeningPort
     */
    public static int getListeningPort() {
        return listeningPort;
    }
}
