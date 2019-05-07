import java.io.IOException;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        MQTTBroker mqttBroker  = MQTTBroker.getInstance();
        try {
            mqttBroker.brokerAlwaysOn();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
