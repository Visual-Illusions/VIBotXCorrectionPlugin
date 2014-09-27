package net.visualillusionsent.correctionplugin;

/**
 * Created by Aaron (somners) on 9/23/2014.
 */
public class Message {

    private String sender, message;

    public Message(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getMessage() {
        return message;
    }
}
