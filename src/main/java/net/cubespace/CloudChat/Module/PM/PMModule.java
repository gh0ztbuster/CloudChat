package net.cubespace.CloudChat.Module.PM;

import net.cubespace.CloudChat.Command.Binder.Binder;
import net.cubespace.CloudChat.Command.Binder.PlayerBinder;
import net.cubespace.CloudChat.Config.CommandAliases;
import net.cubespace.CloudChat.Config.Main;
import net.cubespace.CloudChat.Module.PM.Command.Conversation;
import net.cubespace.CloudChat.Module.PM.Command.PM;
import net.cubespace.CloudChat.Module.PM.Command.SocialSpy;
import net.cubespace.CloudChat.Module.PM.Listener.CheckCommandListener;
import net.cubespace.CloudChat.Module.PM.Listener.ConversationListener;
import net.cubespace.CloudChat.Module.PM.Listener.PMListener;
import net.cubespace.CloudChat.Module.PM.Listener.PlayerQuitListener;
import net.cubespace.lib.Module.Module;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class PMModule extends Module {
    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("msg"))) {
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("msg"), PlayerBinder.class, commandAliases.Msg.toArray(new String[commandAliases.Msg.size()]));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("reply"))) {
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("reply"), Binder.class, commandAliases.Reply.toArray(new String[commandAliases.Reply.size()]));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("socialspy"))) {
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("socialspy"), Binder.class, commandAliases.Socialspy.toArray(new String[commandAliases.Socialspy.size()]));
            plugin.getCommandExecutor().add(this, new SocialSpy(plugin));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("togglepm"))) {
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("togglepm"), Binder.class, commandAliases.TogglePM.toArray(new String[commandAliases.TogglePM.size()]));
        }

        if(!((Main) plugin.getConfigManager().getConfig("main")).DoNotBind.contains(commandAliases.BaseCommands.get("conversation"))) {
            plugin.getBindManager().bind(commandAliases.BaseCommands.get("conversation"), PlayerBinder.class, commandAliases.Conversation.toArray(new String[commandAliases.Conversation.size()]));
            plugin.getCommandExecutor().add(this, new Conversation(plugin));
        }

        plugin.getCommandExecutor().add(this, new PM(plugin));

        plugin.getAsyncEventBus().addListener(this, new PMListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new ConversationListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new PlayerQuitListener(plugin));
        plugin.getAsyncEventBus().addListener(this, new CheckCommandListener(plugin));
    }

    @Override
    public void onDisable() {
        CommandAliases commandAliases = plugin.getConfigManager().getConfig("commandAliases");

        if(plugin.getBindManager().isBound(commandAliases.BaseCommands.get("msg"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("msg"));
        }

        if(plugin.getBindManager().isBound(commandAliases.BaseCommands.get("reply"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("reply"));
        }

        if(plugin.getBindManager().isBound(commandAliases.BaseCommands.get("socialspy"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("socialspy"));
        }

        if(plugin.getBindManager().isBound(commandAliases.BaseCommands.get("togglepm"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("togglepm"));
        }

        if(plugin.getBindManager().isBound(commandAliases.BaseCommands.get("conversation"))) {
            plugin.getBindManager().unbind(commandAliases.BaseCommands.get("conversation"));
        }

        plugin.getCommandExecutor().remove(this);
        plugin.getAsyncEventBus().removeListener(this);
    }
}
