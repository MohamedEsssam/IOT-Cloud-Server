import java.io.IOException;
import java.net.*;
import java.util.HashMap;

public class MQTTBroker {

    //list of the topics
    //for each topic there is 2 lists, one for publisher & the other for the subscribers
    //hash map between the topic name and its topic object
    //when the message contains publish or subscribe, we must get the topic object for the specified topic in the message to handel the operation

    private static final MQTTBroker mqttBrokerInstance = new MQTTBroker();
    private static HashMap<String, MQTTTopic> topics = new HashMap<>();

    private MQTTBroker(){}

    public static MQTTBroker getInstance(){
        return  mqttBrokerInstance;
    }

    public void brokerAlwaysOn () throws IOException {

        ServerSocket mqttBrokerSocket = new ServerSocket();

       while(true) {
           Socket acceptedSocket = mqttBrokerSocket.accept();
           //get the message from the socket
           //know the topic specified in the message and get its MQTTTopic from the hash map topics


       }
    }

    private MQTTTopic getTopic(String topicName){
        return topics.get(topicName);
    }

    //create topic method
    private void createTop(String topicName){
        MQTTTopic newTopic = new MQTTTopic();
        topics.put(topicName,newTopic);
    }
}
