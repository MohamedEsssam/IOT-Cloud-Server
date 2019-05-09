import javax.xml.crypto.Data;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class MQTTTopic {

    private ArrayList<MQTTSubscriber> subscribers = new ArrayList<MQTTSubscriber>();
    private String state = "no";
    public DataOutputStream dataOutputStream;

    public String getState(){
        return state;
    }

    public void updateState(String state, DataOutputStream dataOutputStream) throws IOException {
        this.state = state;
        this.dataOutputStream = dataOutputStream;
        notifyAllSubscribers();
    }

    public void attach(MQTTSubscriber subscriber) throws IOException {
        System.out.println(subscriber.DeviceName);
        subscribers.add(subscriber);
        //if there is a value published before a subscriber subscribe to the topic
        // send them the latest value published
        if(!state.equalsIgnoreCase("no"))
            notifyAllSubscribers();
    }

    private void notifyAllSubscribers() throws IOException {
        for(MQTTSubscriber subscriber : subscribers){
            subscriber.update(dataOutputStream);
        }
    }


}
