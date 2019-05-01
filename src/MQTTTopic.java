import java.util.ArrayList;

public class MQTTTopic {

    private ArrayList<MQTTSubscriber> subscribers = new ArrayList<MQTTSubscriber>();
    private int state;

    public int getState(){
        return state;
    }

    public void updateState(int state){
        this.state = state;
        notifyAllSubscribers();
    }

    public void attach(MQTTSubscriber subscriber){
        subscribers.add(subscriber);
    }

    private void notifyAllSubscribers() {
        for(MQTTSubscriber subscriber : subscribers){
            subscriber.update();
        }
    }


}
