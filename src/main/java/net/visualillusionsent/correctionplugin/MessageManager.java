package net.visualillusionsent.correctionplugin;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Aaron on 9/24/2014.
 */
public class MessageManager {

    private static HashMap<String, LinkedList<Message>> messages = new HashMap<String, LinkedList<Message>>();

    public static List<Message> getMessages(String channel) {
        if (messages.containsKey(channel)) {
            return messages.get(channel);
        }
        return null;
    }

    public static void addTell(String channel, Message message) {
        if (messages.containsKey(channel)) {
            LinkedList<Message> temp = messages.get(channel);
            temp.addFirst(message);
            if (temp.size() > 50) {
                temp.removeLast();
            }
        }
        else {
            LinkedList<Message> temp = new LinkedList<Message>();
            temp.addFirst(message);
            messages.put(channel, temp);
        }
    }
}
