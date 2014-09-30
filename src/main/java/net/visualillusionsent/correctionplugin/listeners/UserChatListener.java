package net.visualillusionsent.correctionplugin.listeners;

import net.visualillusionsent.correctionplugin.Message;
import net.visualillusionsent.correctionplugin.MessageManager;
import net.visualillusionsent.correctionplugin.StringUtils;
import net.visualillusionsent.vibotx.api.events.EventListener;
import net.visualillusionsent.vibotx.api.events.EventMethod;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.List;

/**
 * Created by Aaron(somners) on 9/25/2014.
 */
public class UserChatListener implements EventListener {

    private static UserChatListener instance;

    @EventMethod
    public void onChannelMessage(MessageEvent event) {
        if (!event.getMessage().startsWith("s/")) {
            MessageManager.addTell(event.getChannel().getName(), new Message(event.getUser().getNick(), event.getMessage()));
            return;
        }

        String[] params = event.getMessage().split("/");
        /* Not enough parameters to continue */
        if (params.length < 1) {
            return;
        }

        Message last = null;
        /* retreive our parameters */
        String substring = params.length > 1 ? params[1] : "";
        if (substring.isEmpty()) return;
        String replacer = params.length > 2 ? params[2] : "";
        String flags = params.length > 3 ? params[3] : "";
        /* calculate a few flags for later */
        boolean ignorecase = flags.isEmpty() ? false : StringUtils.containsString(flags, "i", true);
        boolean replaceAll = flags.isEmpty() ? false : StringUtils.containsString(flags, "g", true);
        /* select our message */
        List<Message> messages = MessageManager.getMessages(event.getChannel().getName());
        /* if null, no messages sent in this channel have been tracked. return */
        if (messages == null) {
            return;
        }
        last = getLastMessage(messages, substring, ignorecase);
        /* no message? return! */
        if (last == null) {
            return;
        }
        /* replace our & characters */
        replacer = StringUtils.replaceAll(replacer, "&", substring, true);

        String newMessage = "";
        /* replace all? or just the first? */
        if (replaceAll) {
            newMessage = StringUtils.replaceAll(last.getMessage(), substring, replacer, ignorecase);
        } else {
            newMessage = StringUtils.replaceFirst(last.getMessage(), substring, replacer, ignorecase);
        }
        /* chat the message back to the channel */
        event.getChannel().send().message(last.getSender() +": "+ newMessage);
    }

    /**
     * Gets the last message fitting the given parameters.
     * @param messages list of messages to check
     * @param substring substring that must be contained
     * @param ignorecase ignore case sensitivity?
     * @return most recent matching {@link Message} or null
     */
    public Message getLastMessage(List<Message> messages, String substring, boolean ignorecase) {
        for(Message m : messages) {
            if (StringUtils.containsString(m.getMessage(), substring, ignorecase)) {
                return m;
            }
        }
        return null;
    }

}
