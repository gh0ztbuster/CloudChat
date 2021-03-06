package net.cubespace.CloudChat.Event;

import net.cubespace.lib.EventBus.Event;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 13.01.14 23:27
 */
@SuppressWarnings("ALL")
public class CheckCommandEvent implements Event {
    private final ProxiedPlayer sender;
    private String message;
    private final boolean isCommand;
    private boolean cancelParent = false;

    public CheckCommandEvent(ProxiedPlayer sender, String message, boolean isCommand) {
        this.sender = sender;
        this.message = message;
        this.isCommand = isCommand;
    }

    public String getMessage() {
        return message;
    }

    public ProxiedPlayer getSender() {
        return sender;
    }

    public boolean isCommand() {
        return isCommand;
    }

    public boolean isCancelParent() {
        return cancelParent;
    }

    public void setCancelParent(boolean cancelParent) {
        this.cancelParent = cancelParent;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
