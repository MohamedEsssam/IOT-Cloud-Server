public class MQTTSubscriber {

    private MQTTTopic mqttTopic;

    public MQTTSubscriber(MQTTTopic topic) {
        this.mqttTopic = topic;
        this.mqttTopic.attach(this);
    }

    public void update() {

        //send message to the device throw the socket of the server and the device socket
    }
}
