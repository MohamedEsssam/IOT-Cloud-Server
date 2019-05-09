import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class DeviceRequestHandler extends Thread {

    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final Socket deviceSocket;
    char flag = 0;
    private String[] requestContent;
    HashMap<String,MQTTTopic> topics = MQTTBroker.getTopics();
    public DeviceRequestHandler(Socket deviceSocket, DataInputStream dataInputStream, DataOutputStream dataOutputStream) throws IOException {
        this.deviceSocket = deviceSocket;
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    @Override
    public void run()
    {
        String deviceRequest=null;
        while (true)
        {
            try {
                System.out.print("Connection success with socket : " +  deviceSocket.getPort()+ "\n");
                // Ask user what he wants
                dataOutputStream.writeUTF("Connection successful");

                // receive the request from device
                try{
                    deviceRequest = dataInputStream.readUTF();
                }catch (Exception e)
                {
                    System.out.println("Connection Closed");
                    this.deviceSocket.close();
                    return;
                }

                    System.out.println(deviceRequest);
                    requestContent = split(deviceRequest);
                    if (requestContent[0].equalsIgnoreCase("Exit")) {
                        System.out.println("Client " + this.deviceSocket + " sends exit...");
                        System.out.println("Closing this connection.");
                        this.deviceSocket.close();
                        System.out.println("Connection closed");
                        break;
                    }

                    switch (requestContent[0]) {


                        //if the request is subscribe, check if the topic is already exist or not
                        //if yes, add the address of the subscribing device to the list of subscriber in the topic
                        case "subscribe":
                            addSubscriber();
                            break;

                        //if the request is publish, check if the topic is already exist or not
                        //if yes, send message to the device to tell it that the topic is exist and cannot publish on it
                        //if no, create a new topic and assign the device as its publisher
                        case "publish":
                            System.out.println("Publish request");
                            publish();
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

    private String[] split(String deviceRequest)
    {
        String[] request;
        request = deviceRequest.split("\\s+");
        return request;
    }

    private void addSubscriber() throws IOException {
        System.out.println("Subscribe request");
        if(!topics.containsKey(requestContent[2]))
        {
            MQTTTopic topic = new MQTTTopic();
            topics.put(requestContent[2],topic);
        }
        new MQTTSubscriber(topics.get(requestContent[2]),dataOutputStream,requestContent[1]);
        dataOutputStream.writeUTF("done subscribe");
    }

    private void publish() throws IOException {
        if(!topics.containsKey(requestContent[1]))
        {
            MQTTTopic topic = new MQTTTopic();
            topics.put(requestContent[1],topic);
        }
        topics.get(requestContent[1]).updateState(requestContent[2]);
    }

}
