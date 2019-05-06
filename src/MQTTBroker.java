import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.*;
import java.util.HashMap;


public class MQTTBroker {

    //list of the topics
    //when the message contains publish or subscribe, we must get the topic object for the specified topic in the message to handel the operation

    private static final MQTTBroker mqttBrokerInstance = new MQTTBroker();
    private static HashMap<String, MQTTTopic> topics = new HashMap<>();

    private MQTTBroker(){}

    public static MQTTBroker getInstance(){
        return  mqttBrokerInstance;
    }

    public void brokerAlwaysOn () throws IOException {
        byte[] buffer = "hello".getBytes();
        InetAddress address = InetAddress.getByName("192.168.1.255");

        DatagramPacket packet2 = new DatagramPacket(
                buffer, buffer.length, address, 4444);
        DatagramSocket socket = new DatagramSocket(4444,InetAddress.getByName("0.0.0.0"));
        DatagramSocket socket2 = new DatagramSocket();
        socket2.send(packet2);
        socket.setBroadcast(true);
        System.out.println("Listen on " + socket.getLocalAddress() + " from " + socket.getInetAddress() + " port " + socket.getBroadcast());
        byte[] buf = new byte[512];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        HttpServer server = HttpServer.create(new InetSocketAddress(4444), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(MQTTBroker::handleRequest);
        server.start();

       while(true) {

           //get the message from the socket
           //know the topic specified in the message and get its MQTTTopic from the hash map topics

           System.out.println("Waiting for data");
           socket.receive(packet);
           System.out.println(packet.getAddress());
           System.out.println(packet.getPort());
           System.out.println("Data received");
       }
    }

    private MQTTTopic getTopic(String topicName){
        return topics.get(topicName);
    }

    //create topic method
    private void createTopic(String topicName){
        MQTTTopic newTopic = new MQTTTopic();
        topics.put(topicName,newTopic);
    }

    //callback function
    private static void handleRequest(HttpExchange exchange) throws IOException {
        System.out.println(getBody(exchange));
        String response = "Hello Button!";
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    public static String getBody(HttpExchange request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getRequestBody();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            } else {
                stringBuilder.append("");
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        return body;
    }
}
