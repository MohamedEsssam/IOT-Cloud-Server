import org.eclipse.paho.client.mqttv3.MqttException;

import java.net.URISyntaxException;

public class Main {

    public static void main(String[] args) throws MqttException, URISyntaxException {
        Subscriber s = new Subscriber(System.getenv("m24.cloudmqtt.com:12410"));
        s.sendMessage("Hello");
        s.sendMessage("Hello 2");
    }
}
