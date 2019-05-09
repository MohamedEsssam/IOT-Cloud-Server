import java.io.IOException;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws Exception {
        try {
            Thread t1 = MQTTBroker.getInstance();
            Thread t2 = PeerDiscovery.getInstance();
            t1.start();
            t2.start();
        } catch (Exception e) {
            e.printStackTrace();



        }
    }
}
