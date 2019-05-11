import java.io.*;
import java.net.*;
import java.util.HashMap;

public class MQTTBroker extends Thread {

    //list of the topics
    //when the message contains publish or subscribe, we must get the topic object for the specified topic in the message to handel the operation

    private static final MQTTBroker mqttBrokerInstance = new MQTTBroker();
    private static HashMap<String, MQTTTopic> topics = new HashMap<>();
    private ServerSocket serverWelcomingSocket;

    private MQTTBroker() {
    }

    public static MQTTBroker getInstance() {
        return mqttBrokerInstance;
    }

    public static HashMap<String, MQTTTopic> getTopics() {
        return topics;
    }

    @Override
    public void run() {
        // server welcoming socket listening on port 5678 with ip address of the machine
        try {
            serverWelcomingSocket = new ServerSocket(6789);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // running infinite loop for getting client request
        while (true) {
            // socket object to receive incoming client requests
            Socket acceptedSocket = null;
            try {
                acceptedSocket = serverWelcomingSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("A new client is connected : " + acceptedSocket);

            // obtaining input and out streams
            DataInputStream dataInputStream = null;
            try {
                dataInputStream = new DataInputStream(acceptedSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            DataOutputStream dataOutputStream = null;
            try {
                dataOutputStream = new DataOutputStream(acceptedSocket.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("Assigning new thread for this client");

            // create a new thread object
            Thread t = null;
            t = new  AndroidDeviceRequestHandler(acceptedSocket, dataInputStream, dataOutputStream);
            //t = new DeviceRequestHandler(acceptedSocket, dataInputStream, dataOutputStream);
            // Invoking the start() method
            t.start();
        }
    }
}
