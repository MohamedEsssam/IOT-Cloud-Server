public class MQTTSubscriber {

    private MQTTTopic mqttTopic;

    public MQTTSubscriber(MQTTTopic topic) {
        this.mqttTopic = topic;
        this.mqttTopic.attach(this);
    }

    public void update() {

        // the update message will be "Publish topicName TopicCurrentState"
        // the message will be send throw the dataOutputStream of the device
    }
}
