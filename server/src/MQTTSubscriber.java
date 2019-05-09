import java.io.DataOutputStream;
import java.io.IOException;

public class MQTTSubscriber {

    private MQTTTopic mqttTopic;
    public String DeviceName;
    public MQTTSubscriber(MQTTTopic topic,String DeviceName) throws IOException {
        this.mqttTopic = topic;
        this.DeviceName = DeviceName;
        this.mqttTopic.attach(this);

    }

    public void update(DataOutputStream dataOutputStream) throws IOException {
        System.out.println("Sending Data");
        dataOutputStream.writeUTF("publish " + DeviceName + " " + mqttTopic.getState());
    }
}
