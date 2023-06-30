package ca.bungo.papernicknames.commands;

import ca.bungo.papernicknames.Nicknames;
import ca.bungo.papernicknames.utility.ChatUtility;
import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NicknameCommand extends Command {
    public NicknameCommand(@NotNull String name) {
        super(name);
        this.description = "Disguise your Identity!";
        this.setPermission("nickname.use");
        this.permissionMessage(MiniMessage.miniMessage().deserialize("<dark_red>You do not have permission for this command!"));
        this.setAliases(List.of("unnick"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player player)) return false;

        if (label.equalsIgnoreCase("unnick") || label.equalsIgnoreCase("nicknames:unnick")) {
            Nicknames.INSTANCE.nicknameManager.removeNickname(player);
            player.sendMessage(ChatUtility.formatMessage("&cSuccessfully removed your nickname!"));
        }
        else {

            StringBuilder builder = new StringBuilder();
            for(String s : args)
                builder.append(s).append(" ");
            String nickname = builder.substring(0, builder.length()-1);

            if(nickname.length() > Nicknames.INSTANCE.getConfig().getInt("max-name-length")){
                player.sendMessage(ChatUtility.formatMessage("&4Error: The name &e" + nickname + "&4 is too long. Max Size: &e" + Nicknames.INSTANCE.getConfig().getInt("max-name-length")));
                return false;
            }

            Nicknames.INSTANCE.nicknameManager.nickPlayer(player, nickname);

            player.sendMessage(ChatUtility.formatMessage("&aAttempted to set your nickname to &e" + nickname));
        }

        return false;
    }

}
