package ca.bungo.nicknames.managers;

import ca.bungo.nicknames.NicknamesUpdated;
import ca.bungo.nicknames.types.Nickname;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;

public class NicknameManager {

    private Team hiddenTeam;
    private final Map<String, Nickname> nicknames = new HashMap<>();

    public NicknameManager(){
        hiddenTeam = NicknamesUpdated.getInstance().mainScoreboard.getTeam("hidden-name-team");
        if(hiddenTeam == null)
            hiddenTeam = NicknamesUpdated.getInstance().mainScoreboard.registerNewTeam("hidden-name-team");
        hiddenTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    public void nicknamePlayer(Player player, String nickname){
        hiddenTeam.addEntry(player.getName());
        if(!nicknames.containsKey(player.getUniqueId().toString())){
            nicknames.put(player.getUniqueId().toString(), new Nickname(player, nickname));
        }else{
            nicknames.get(player.getUniqueId().toString()).setNickname(nickname);
        }
    }

    public void createLocalized(Player requester, boolean enabled){
        for(Nickname name : nicknames.values()){
            name.showRealName(requester, enabled);
        }
    }

    public void unnicknamePlayer(Player player){
        hiddenTeam.removeEntry(player.getName());
        Nickname nickname = nicknames.remove(player.getUniqueId().toString());
        if(nickname != null)
            nickname.unNick();
    }

    public void loadNames(Player player){
        for(Nickname nickname : nicknames.values()){
            nickname.loadNickname(player);
        }
    }

}
