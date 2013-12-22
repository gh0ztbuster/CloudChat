package net.cubespace.CloudChat.Database;

import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Comment;
import net.craftminecraft.bungee.bungeeyaml.supereasyconfig.Config;
import net.cubespace.CloudChat.CloudChatPlugin;

import java.io.File;
import java.util.ArrayList;

/**
 * @author geNAZt (fabian.fassbender42@googlemail.com)
 * @date Last changed: 26.11.13 19:48
 */
public class ChannelDatabase extends Config {
    public ChannelDatabase(CloudChatPlugin plugin, String channel) {
        CONFIG_FILE = new File(plugin.getDataFolder(), "database" + File.separator + "channels" + File.separator + channel + ".yml");
        CONFIG_HEADER = "Channel Database for " + channel;
    }

    public ChannelDatabase(File channelFile) {
        CONFIG_FILE = channelFile;
    }

    @Comment("The full Name of the Channel")
    public String Name;

    @Comment("The short Accessor for the Channel")
    public String Short;

    @Comment("The used printout Format")
    public String Format;

    @Comment("If the User must follow this Channel")
    public Boolean Forced;

    @Comment("The Password of the Channel \"\" for none")
    public String Password = "";

    @Comment("The people who can invite others")
    public ArrayList<String> CanInvite = new ArrayList<>();
}
