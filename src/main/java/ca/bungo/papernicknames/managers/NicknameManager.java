package ca.bungo.papernicknames.managers;

import ca.bungo.papernicknames.Nicknames;
import ca.bungo.papernicknames.types.Nicks;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class NicknameManager {

    private Map<String, Nicks> nicknames;

    public NicknameManager(){
         nicknames = new HashMap<>();
    }

    public Nicks nickPlayer(Player player, String nickname){
        Nicks nick = nicknames.get(player.getUniqueId().toString());

        if(nick != null){
            nick.setNickName(nickname);
        }else{
            nick = new Nicks(player, nickname);
            nicknames.put(player.getUniqueId().toString(), nick);
        }
        return nick;
    }

    public void removeNickname(Player player){
        Nicks nick = nicknames.get(player.getUniqueId().toString());
        if(nick == null) return;

        nick.setNickName("");
    }

    public void cleanup(){
        for(Nicks nick : nicknames.values()){
            nick.getPlayerTeam().unregister();
        }

        nicknames.clear();
    }

}
