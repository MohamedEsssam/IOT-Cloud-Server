import java.io.DataOutputStream;
import java.io.IOException;

public class MQTTSubscriber {

    private MQTTTopic mqttTopic;
    public String DeviceName;
    private DataOutputStream dataOutputStream;

    public MQTTSubscriber(MQTTTopic topic, DataOutputStream dataOutputStream, String DeviceName) throws IOException {
        this.mqttTopic = topic;
        this.DeviceName = DeviceName;
        this.dataOutputStream = dataOutputStream;
        this.mqttTopic.attach(this);

    }

    public void update() throws IOException {
        System.out.println("Sending Data");
        dataOutputStream.writeUTF("publish " + DeviceName + " " + mqttTopic.getState());
    }
}
