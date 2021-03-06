package net.cubespace.CloudChat.Command.Binder;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import net.cubespace.CloudChat.Module.ChannelManager.ChannelManager;
import net.cubespace.CloudChat.Module.ChannelManager.Database.ChannelDatabase;
import net.cubespace.lib.CubespacePlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.TabExecutor;

public class ChannelBinder extends Binder implements TabExecutor {
    public ChannelBinder(CubespacePlugin plugin, String name, String ... aliases) {
        super(plugin, name, aliases);
    }

    @Override
    public Iterable<String> onTabComplete(final CommandSender commandSender, String[] args) {
        ChannelManager channelManager = plugin.getManagerRegistry().getManager("channelManager");

        final String lastArg = ( args.length > 0 ) ? args[args.length - 1] : "";

        return Iterables.transform(Iterables.filter(channelManager.getChannels(), new Predicate<ChannelDatabase>() {
            @Override
            public boolean apply(ChannelDatabase database) {
                return plugin.getPermissionManager().has(commandSender, "cloudchat.channel." + database.Name) && (database.Name.toLowerCase().startsWith(lastArg.toLowerCase()) || database.Short.toLowerCase().startsWith(lastArg.toLowerCase()));
            }
        }), new Function<ChannelDatabase, String>() {
            @Override
            public String apply(ChannelDatabase database) {
                return database.Name;
            }
        });
    }
}
