//DRH846
//11280305
//CMPT 381

package a4.drh846.a4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PublishSubscribe {
    private Map<String, ArrayList<Subscriber>> channels = new HashMap<>();

    // create new channel
    public void createChannel(String channelKey) {
        channels.put(channelKey, new ArrayList<>());
    }

    public void subscribe(String channelKey, Subscriber s) {
        ArrayList<Subscriber> channel = channels.get(channelKey);
        if (channel != null)
            channel.add(s);
    }

    public void publish(String channelKey) {
        for (Subscriber s : channels.get(channelKey)) {
            s.notify(channelKey);
        }
    }

}
