package ca.bungo.papernicknames.types;

import ca.bungo.papernicknames.Nicknames;
import ca.bungo.papernicknames.utility.ChatUtility;
import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.UUID;

public class Nicks {

    private String playerUUID;

    private Team playerTeam;
    private String nickName;


    public Nicks(Player player, String nickName){
        this.nickName = nickName;
        this.playerUUID = player.getUniqueId().toString();
        this.playerTeam = Nicknames.INSTANCE.scoreboard.registerNewTeam(playerUUID);

        this.updateNickname();
    }


    public Team getPlayerTeam() {
        return playerTeam;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
        updateNickname();
    }

    private void updateNickname(){
        Player lPlayer = Bukkit.getPlayer(UUID.fromString(this.playerUUID));
        if(lPlayer == null) return;

        if(this.nickName.isEmpty()){
            playerTeam.removePlayer(lPlayer);
            playerTeam.prefix(Component.text(""));
            playerTeam.suffix(Component.text(""));
        }else{
            playerTeam.addPlayer(lPlayer);
            playerTeam.color(NamedTextColor.GRAY);
            playerTeam.prefix(ChatUtility.formatMessage("&f" + nickName + " &7["));
            playerTeam.suffix(ChatUtility.formatMessage("&7]"));
        }
    }
}
