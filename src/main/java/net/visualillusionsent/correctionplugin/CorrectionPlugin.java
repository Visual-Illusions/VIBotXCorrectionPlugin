package net.visualillusionsent.correctionplugin;

import net.visualillusionsent.correctionplugin.listeners.UserChatListener;
import net.visualillusionsent.vibotx.VIBotX;
import net.visualillusionsent.vibotx.api.events.EventHandler;
import net.visualillusionsent.vibotx.api.events.EventMethodSignatureException;
import net.visualillusionsent.vibotx.api.plugin.JavaPlugin;

/**
 * Created by Aaron on 9/23/2014.
 */
public class CorrectionPlugin extends JavaPlugin {
    public static CorrectionPlugin instance;

    @Override
    public boolean enable() {
        instance = this;
        try {
            EventHandler.getInstance().registerListener(new UserChatListener(), this);
        } catch (EventMethodSignatureException e) {
            VIBotX.log.error("Error Registering Event Listeners in Correction Plugin.", e);
        }
        return true;
    }

    @Override
    public void disable() {

    }

    public CorrectionPlugin instance() {
        return instance;
    }
}
