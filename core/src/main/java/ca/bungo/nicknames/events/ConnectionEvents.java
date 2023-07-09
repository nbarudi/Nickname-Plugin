package ca.bungo.nicknames.events;

import ca.bungo.nicknames.NicknamesUpdated;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ConnectionEvents implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        NicknamesUpdated.getInstance().nicknameManager.loadNames(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        NicknamesUpdated.getInstance().nicknameManager.unnicknamePlayer(player);
    }

}
