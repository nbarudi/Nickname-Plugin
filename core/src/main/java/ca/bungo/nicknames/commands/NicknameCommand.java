package ca.bungo.nicknames.commands;

import ca.bungo.nicknames.Nickname;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NicknameCommand extends Command {

    Map<String, Team> teamStorage = new HashMap<>();
    Map<String, String> trueName = new HashMap<>();

    public NicknameCommand(@NotNull String name) {
        super(name);
        this.description = "Nickname yourself!";
        this.usageMessage = "/" + this.getName() + " <Name> || /unnick";
        this.setPermission("nicknames.use");
        this.setAliases(List.of("unnick"));
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {

        if(!(sender instanceof Player player)) return false;
        MiniMessage mm = MiniMessage.miniMessage();
        if(args.length == 0 && !commandLabel.equalsIgnoreCase("unnick")){
            player.sendMessage(mm.deserialize("<dark_red>Invalid Usage: <yellow>" + this.usageMessage));
            return false;
        }
        else if(commandLabel.equalsIgnoreCase("unnick")) {
            Nickname.getInstance().abstractedHandler.showPlayerName(player);
            Team team = teamStorage.get(player.getUniqueId().toString());
            if(team != null){
                team.removePlayer(player);
            }
            player.sendMessage(mm.deserialize("<red>Removed your Nickname!"));
            return false;
        }

        int maxLength = Nickname.getInstance().getConfig().getInt("max-name-length");

        StringBuilder nameBuilder = new StringBuilder();
        for (String arg : args) {
            nameBuilder.append(arg).append(" ");
        }

        String nickname = nameBuilder.substring(0, nameBuilder.length()-1);

        if(nickname.length() > maxLength){
            player.sendMessage(mm.deserialize("<red>Nickname too long! Max Length: <yellow>" + maxLength));
            return false;
        }

        ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

        Scoreboard mainScoreboard = scoreboardManager.getMainScoreboard();

        Team playerTeam = teamStorage.remove(player.getUniqueId().toString());

        if(playerTeam == null){
            if(mainScoreboard.getTeam(player.getUniqueId().toString()) != null)
                playerTeam = mainScoreboard.getTeam(player.getUniqueId().toString());
            else
                playerTeam = mainScoreboard.registerNewTeam(player.getUniqueId().toString());
        }

        assert playerTeam != null;

        if(trueName.get(player.getUniqueId().toString()) == null){
            trueName.put(player.getUniqueId().toString(), player.getName());
        }

        playerTeam.prefix(Component.text(nickname));
        playerTeam.suffix(mm.deserialize("<gray>[" + trueName.get(player.getUniqueId().toString())  + "]"));

        playerTeam.addPlayer(player);

        Nickname.getInstance().abstractedHandler.hidePlayerName(player);

        teamStorage.put(player.getUniqueId().toString(), playerTeam);

        player.sendMessage(mm.deserialize("<green>Set your name to <yellow>" + nickname));

        for(Player other : Bukkit.getOnlinePlayers()){
            other.setScoreboard(mainScoreboard);
        }

        return false;
    }

}
