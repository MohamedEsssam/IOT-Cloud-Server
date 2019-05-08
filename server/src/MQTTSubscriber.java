import java.io.DataOutputStream;
import java.io.IOException;

public class MQTTSubscriber {

    private MQTTTopic mqttTopic;
    private DataOutputStream dataOutputStream;
    public String DeviceName;
    public MQTTSubscriber(MQTTTopic topic, DataOutputStream dataOutputStream,String DeviceName) throws IOException {
        this.mqttTopic = topic;
        this.dataOutputStream = dataOutputStream;
        this.DeviceName = DeviceName;
        this.mqttTopic.attach(this);

    }

    public void update() throws IOException {
        System.out.println("Sending Data");
        dataOutputStream.writeUTF("publish " + DeviceName + " " + mqttTopic.getState());
    }
}
