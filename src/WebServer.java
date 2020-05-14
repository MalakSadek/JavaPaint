import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A class for the Server. It accepts connections from clients and then creates threads and assigns them to clients to handle their requests.
 * It can be run by using java WebServerMain 12345
 * @author Malak Sadek
 */
class WebServer {
    //The number of clients that are connected to the server
    public static int numberOfConnections = 0;
    //The server can only handle a maximum of 5 clients at a time
    private static final int THREADLIMIT = 5;

    private Socket connection;

    /**
     * The constructor for the class.
     * It binds to a socket and then listens for incoming connections and assigns the clients to a thread
     * @throws IOException
     */
    WebServer() throws IOException {

        ServerSocket s = new ServerSocket(12349);
        System.out.println("Server started ... listening on port " + WebServerMain.getListeningPort() + " ...");

        numberOfConnections = 0;
        //Server keeps listening for new connections
        while (true) {
            if (numberOfConnections < THREADLIMIT) {

                //Accept any new connections continuously
                connection = s.accept();
                System.out.println("Server got new connection request from " + connection.getInetAddress());
                //Create new handler for this connection
                ConnectionHandler thread = new ConnectionHandler(connection, numberOfConnections);
                thread.start();
                numberOfConnections++;

            } else {
                System.out.println("Cannot accept more requests at the moment, please wait... ");
            }
        }
    }
}
