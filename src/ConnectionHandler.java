import Models.Model;
import java.io.*;
import java.net.Socket;

/**
 * A class for the server thread, created to handle concurrent clients.
 * This is copied and adapted from my practical 3 submission for CS5001.
 * How to save and load a serializable class in a file obtained from: https://www.geeksforgeeks.org/serialization-in-java/ as mentioned in Controller
 * @author Malak Sadek
 */
public class ConnectionHandler extends Thread {

    private String request, receiver;
    private Model model, serverModel;

    ObjectOutputStream OOS;
    ObjectInputStream OIS;
    int threadNumber;
    Socket connection;

    /**
     * This creates a new thread to service a client.
     * @param conn is the socket connecting between the server thread & client
     * @param threadNumber is the current index number for a given thread (0-4)
     * @throws IOException
     */
    ConnectionHandler(Socket conn, int threadNumber) throws IOException {
        this.connection = conn;
        this.threadNumber = threadNumber;

        try {
            OOS =  new ObjectOutputStream(conn.getOutputStream());
            OIS = new ObjectInputStream(conn.getInputStream());
            OOS.flush();

            //This line can be used to empty the model in the server's save file by just writing a new empty one to the file
            //emptyCache();

            serviceClient();

        } catch (IOException | ClassNotFoundException io) {
            io.printStackTrace();
            cleanUp();
        }
    }

    /**
     * This method is automatically called when the thread is started.
     */
    @Override
    public void run() {
        super.run();
        try {
            serviceClient();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            try {
                cleanUp();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * This class handles all the logic the thread does, one step at a time.
     * It reads the client request, then responds to it, and finally performs clean up
     */
    private void serviceClient() throws IOException, ClassNotFoundException {
        receiveRequest();
        sendResponse();
        cleanUp();
    }

    /**
     * This method receives the request from a client.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    private void receiveRequest() throws IOException, ClassNotFoundException {

        int index = 0;
        byte[] requestBytes = new byte[10000];

        //Here the server blocks, waiting for the client to send it information
        while(OIS.available()==0){}
        //The first information the client sends is its address and the address of the client it wants to sync with
        int requestLength = OIS.read(requestBytes);
        request = new String(requestBytes, 0, requestLength);
        String[] components = request.split(" ");
        receiver = components[0];

        //The client waits for the server to respond, so the server sends an ack byte after it has read this information
        OOS.write(1);
        OOS.flush();

        //The server then waits for the client and reads its own ack byte
        while(OIS.available()==0){}
        OIS.read();

        //The server then reads the model sent by the client
        model = (Model)OIS.readObject();

        //The server reads the latest model it has in its storage file
        FileInputStream file = new FileInputStream(System.getProperty("user.dir")+"/serverModel.ser");
        ObjectInputStream in = new ObjectInputStream(file);

        serverModel = (Model)in.readObject();

        //The server then combines the model it has in store with the model just sent by the client and resaves this in store
        serverModel.addShapes(model);
        in.close();
        file.close();

        FileOutputStream fileOut = new FileOutputStream(System.getProperty("user.dir")+"/serverModel.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);

        out.writeObject(model);

        out.close();
        fileOut.close();

        System.out.println(index + ": received request: " + request);
    }

    /**
     * This method sends the created response back to the client over the socket.
     * @throws IOException
     */
    private void sendResponse() throws IOException {
        //The server then sends the updated model file back to the client
            if(receiver.contains(connection.getInetAddress().toString())) {
                //It starts by sending an ack byte (which the client is waiting for)
                OOS.write(1);
                OOS.flush();
                //It then sends the updated model
                OOS.writeObject(serverModel);
                OOS.flush();
                System.out.println( "Sent model");
            }
    }

    /**
     * This method closes all buffers cleanly and is called at the end of servicing the client or during catch clauses if an exception occurs.
     * @throws IOException
     */
    private void cleanUp() throws IOException {

        for(int i = 0; i < 5; i++){
            OOS.close();
            OIS.close();
            connection.close();
        }

        if (WebServer.numberOfConnections > 0) {
            WebServer.numberOfConnections--;
        } else {
            WebServer.numberOfConnections = 0;
        }

        //emptyCache();
    }

    /**
     * This method is called to clear the server's model file and just add an empty model to it to start all over
     */
    private void emptyCache() {

        try {
            //Finding the current working directory obtained from: https://stackoverflow.com/questions/3153337/how-to-get-current-working-directory-in-java
            FileOutputStream fileOut  = new FileOutputStream(System.getProperty("user.dir")+"/serverModel.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new Model());
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
