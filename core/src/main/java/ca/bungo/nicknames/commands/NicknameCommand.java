package ca.bungo.nicknames.commands;

import ca.bungo.nicknames.NicknamesUpdated;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NicknameCommand extends Command {

    List<Player> showingNames = new ArrayList<>();

    public NicknameCommand(@NotNull String name) {
        super(name);
        this.description = "Change your name!";
        this.usageMessage = "/nick <NAME> || /unnick || /names";
        this.setAliases(List.of("unnick", "names"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player player)) return false;

        if(label.equalsIgnoreCase("unnick") || label.equalsIgnoreCase("nicknames:unnick")){
            if(!player.hasPermission("nicknames.use")){
                player.sendMessage(Component.text("Sorry! You do not have permission for this command!", NamedTextColor.DARK_RED));
                return false;
            }
            NicknamesUpdated.getInstance().nicknameManager.unnicknamePlayer(player);
            player.sendMessage(Component.text("Removed your nickname!", NamedTextColor.YELLOW));
        }
        else if(label.equalsIgnoreCase("names") || label.equalsIgnoreCase("nicknames:names")){
            if(showingNames.contains(player)){
                showingNames.remove(player);
                NicknamesUpdated.getInstance().nicknameManager.createLocalized(player, false);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Now <red>Hiding <yellow>real player names!"));
            }
            else {
                showingNames.add(player);
                NicknamesUpdated.getInstance().nicknameManager.createLocalized(player, true);
                player.sendMessage(MiniMessage.miniMessage().deserialize("<yellow>Now <green>Showing <yellow>real player names!"));
            }
        }
        else {
            if(!player.hasPermission("nicknames.use")){
                player.sendMessage(Component.text("Sorry! You do not have permission for this command!", NamedTextColor.DARK_RED));
                return false;
            }
            if(args.length == 0){
                player.sendMessage(Component.text("Error! Invalid Usage: " + this.usageMessage, NamedTextColor.DARK_RED));
                return false;
            }
            StringBuilder builder = new StringBuilder();
            for(String s : args)
                builder.append(s).append(" ");
            String name = builder.substring(0, builder.length()-1);
            int maxLength = NicknamesUpdated.getInstance().getConfig().getInt("max-name-length", 150);
            if(name.length() > maxLength){
                player.sendMessage(Component.text("Error! Your nickname is too long! ", NamedTextColor.DARK_RED)
                        .append(Component.text("Max Name Size: " + maxLength, NamedTextColor.YELLOW)));
                return false;
            }
            NicknamesUpdated.getInstance().nicknameManager.nicknamePlayer(player, name);
        }
        updateShownNames();
        return false;
    }

    private void updateShownNames(){
        for(Player player : showingNames){
            NicknamesUpdated.getInstance().nicknameManager.createLocalized(player, true);
        }
    }
}
