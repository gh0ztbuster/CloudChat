package net.cubespace.CloudChat.Module.PlayerManager.Command;

import net.cubespace.CloudChat.Command.Parser.NicknameParser;
import net.cubespace.CloudChat.Config.Messages;
import net.cubespace.CloudChat.Module.FormatHandler.Format.FontFormat;
import net.cubespace.CloudChat.Module.PlayerManager.Database.PlayerDatabase;
import net.cubespace.CloudChat.Module.PlayerManager.Event.PlayerNickchangeEvent;
import net.cubespace.CloudChat.Module.PlayerManager.PlayerManager;
import net.cubespace.lib.Chat.MessageBuilder.MessageBuilder;
import net.cubespace.lib.Command.CLICommand;
import net.cubespace.lib.Command.Command;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 29.11.13 13:13
 */
public class Nick implements CLICommand {
    private CubespacePlugin plugin;

    public Nick(CubespacePlugin plugin) {
        this.plugin = plugin;
    }

    @Command(command = "nick", arguments = 1)
    public void nickCommand(CommandSender sender, String[] args) {
        Messages messages = plugin.getConfigManager().getConfig("messages");

        plugin.getPluginLogger().debug("Got a Nickchange");

        //Check if the Sender is a Player since we only can change Players Nicknames
        if (!(sender instanceof ProxiedPlayer)) {
            plugin.getPluginLogger().debug("But not for a Player");

            MessageBuilder messageBuilder = new MessageBuilder();
            messageBuilder.setText(FontFormat.translateString(messages.Command_Nick_NotPlayer)).send(sender);

            return;
        }

        if (args.length > 1) {
            if (!plugin.getPermissionManager().has(sender, "cloudchat.command.nick.other")) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Nick_NoPermissionToChangeOther)).send(sender);
                return;
            }

            ProxiedPlayer player = NicknameParser.getPlayer(plugin, args[0]);
            if (player == null) {
                MessageBuilder messageBuilder = new MessageBuilder();
                messageBuilder.setText(FontFormat.translateString(messages.Command_Nick_OfflinePlayer)).send(sender);
                return;
            }

            //Fire the correct Event to check if this is ok
            PlayerDatabase playerDatabase = ((PlayerManager) plugin.getManagerRegistry().getManager("playerManager")).get(player.getName());
            plugin.getAsyncEventBus().callEvent(new PlayerNickchangeEvent(player, playerDatabase.Nick, args[1]));
        } else {
            //Fire the correct Event to check if this is ok
            PlayerDatabase playerDatabase = ((PlayerManager) plugin.getManagerRegistry().getManager("playerManager")).get(sender.getName());
            plugin.getAsyncEventBus().callEvent(new PlayerNickchangeEvent(sender, playerDatabase.Nick, args[0]));
        }
    }
}
