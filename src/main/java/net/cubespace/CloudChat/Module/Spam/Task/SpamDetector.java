package net.cubespace.CloudChat.Module.Spam.Task;

import net.cubespace.CloudChat.CloudChatPlugin;
import net.cubespace.CloudChat.Config.Sub.SpamEntry;
import net.cubespace.CloudChat.Module.Mute.MuteManager;
import net.cubespace.CloudChat.Module.Spam.Message.DispatchCmdMessage;
import net.cubespace.CloudChat.Module.Spam.SpamCounter;
import net.cubespace.CloudChat.Module.Spam.SpamModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.12.13 00:55
 */
public class SpamDetector implements Runnable {
    private CloudChatPlugin plugin;
    private SpamModule spamModule;
    private MuteManager muteManager;

    public SpamDetector(SpamModule spamModule, CloudChatPlugin plugin) {
        this.spamModule = spamModule;
        this.plugin = plugin;
        this.muteManager = plugin.getManagerRegistry().getManager("muteManager");
    }

    @Override
    public void run() {
        //Get all Spam Players
        HashMap<String, HashMap<SpamEntry, SpamCounter>> playerSpamHashMap = spamModule.getSpamManager().getPlayerSpamCounter();

        for(Map.Entry<String, HashMap<SpamEntry, SpamCounter>> playerSpam : playerSpamHashMap.entrySet()) {
            for(Map.Entry<SpamEntry, SpamCounter> spamCounterEntry : playerSpam.getValue().entrySet()) {
                if(spamCounterEntry.getValue().getMessageCount() >= spamCounterEntry.getKey().AmountOfMessages) {
                    switch (spamCounterEntry.getKey().ActionIfReached) {
                        case MUTE:
                            muteManager.addGlobalMute(playerSpam.getKey());
                            plugin.getProxy().getPlayer(playerSpam.getKey()).sendMessage(spamCounterEntry.getKey().MuteMessage.replace("%amount", spamCounterEntry.getKey().HowLongToMuteInSeconds.toString()));

                            final String player = playerSpam.getKey();
                            plugin.getProxy().getScheduler().schedule(plugin, new Runnable() {
                                @Override
                                public void run() {
                                    muteManager.removeGlobalMute(player);
                                }
                            }, spamCounterEntry.getKey().HowLongToMuteInSeconds, TimeUnit.SECONDS);
                            break;

                        case KICK:
                            plugin.getProxy().getPlayer(playerSpam.getKey()).disconnect(spamCounterEntry.getKey().KickMessage);
                            break;

                        case DISPATCH_CMD:
                            plugin.getPluginMessageManager("CloudChat").sendPluginMessage(new DispatchCmdMessage(plugin.getProxy().getPlayer(playerSpam.getKey()), spamCounterEntry.getKey().CommandToDispatch.replace("%player", playerSpam.getKey())));
                            break;
                    }
                }
            }
        }
    }
}