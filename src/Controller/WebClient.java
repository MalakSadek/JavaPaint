package Controller;

import Models.Model;
import java.io.*;
import java.net.Socket;

/**
 * A class for WebClient, used by the controller to send requests to the server.
 * @author Malak Sadek
 */
public class WebClient {
    private Socket s;
    private String hostName;
    private int port;
    private Model model;
    private String destination;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    /**
     * The constructor for this class.
     * @param hostName - would be the client's, however is always localhost for this case
     * @param port - would be the client's, however is always 12345 for this case
     * @param model - the model to be sent to the server
     * @param destination - the other client's IP address, obtained by the view from the user
     * @throws IOException
     */
    public WebClient(String hostName, int port, Model model, String destination) throws IOException {
        this.hostName = hostName;
        this.port = port;
        this.s = new Socket(this.hostName, this.port);
        this.model = model;
        this.destination = destination;
        System.out.println("Client connected to " + this.hostName + " on port " + this.port + ".");

        try {
            out =  new ObjectOutputStream(s.getOutputStream());
            in = new ObjectInputStream(s.getInputStream());
            out.flush();
        } catch (IOException io) {
            io.printStackTrace();
            cleanUp();
        }
    }

    /**
     * This method sends the request to the server and also waits for a response.
     */
    public Model sendRequestToServer() throws IOException, ClassNotFoundException {
        //The client sends its address and the address of the other client it wants to sync with
        byte[] requestBytes = (s.getInetAddress()+" "+destination).getBytes();
        out.write(requestBytes);
        out.flush();

        //The client then blocks, waiting for the server, and then reads its ack byte
        while(in.available() == 0){}
        in.read();

        //It sends an ack byte to the server, along with its current model
        out.write(1);
        out.flush();
        out.writeObject(model);
        out.flush();
        System.out.println("Sent to request to server...");

        //It then waits for the server's response and reads the returned model
        while(in.available() == 0){}
        in.read();
        model = (Model)in.readObject();
        System.out.println("Received model...");
        return model;
    }

    /**
     * This method closes all buffers cleanly and is called at the end of servicing the client or during catch clauses if an exception occurs.
     */
    public void cleanUp(){
        try {
            in.close();
            out.close();
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
