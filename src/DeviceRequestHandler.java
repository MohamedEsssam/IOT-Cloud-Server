import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class DeviceRequestHandler extends Thread {

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final Socket deviceSocket;

    public DeviceRequestHandler(Socket deviceSocket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        this.deviceSocket = deviceSocket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run()
    {
        String deviceRequest;
        String serverResponse;
        while (true)
        {
            try {
                System.out.print("Connection success with socket : " +  deviceSocket.getPort()+ "\n");
                // Ask user what he wants
                dataOutputStream.writeUTF("Connection success");

                // receive the request from device
                deviceRequest = dataInputStream.readUTF();

                //TODO: check if the connection between the device and the server will be always exist or not.
                if(deviceRequest.equals("Exit"))
                {
                    System.out.println("Client " + this.deviceSocket + " sends exit...");
                    System.out.println("Closing this connection.");
                    this.deviceSocket.close();
                    System.out.println("Connection closed");
                    break;
                }

                switch (deviceRequest) {

                    //TODO: change cases to the cases that will be found when a device send a request to the server.

                    //if the request is subscribe, check if the topic is already exist or not
                    //if yes, add the address of the subscribing device to the list of subscriber in the topic
                    // if no, send message to the device to tell it that the topic isn't exist
                    case "Subscribe" :
                        System.out.println("Subscribe request");
                        dataOutputStream.writeUTF("done subscribe");
                        break;

                    //if the request is publish, check if the topic is already exist or not
                    //if yes, send message to the device to tell it that the topic is exist and cannot publish on it
                    //if no, create a new topic and assign the device as its publisher
                    case "Publish" :
                        System.out.println("Publish request");
                        dataOutputStream.writeUTF("done publish");
                        break;

                    //if the request isn't subscribe nor publish, send invalid request
                    default:
                        dataOutputStream.writeUTF("Invalid request");
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }

        try
        {
            // closing resources after the connection is closed
            this.dataInputStream.close();
            this.dataOutputStream.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }



}
