package ca.bungo.nicknames.types;

import ca.bungo.nicknames.NicknamesUpdated;
import ca.bungo.nicknames.abstracted.INametag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Nickname {

    private String nickname;
    private String realname;
    private final String uuid;
    private final INametag nametag;

    public Nickname(Player player, String nickname){
        nametag = NicknamesUpdated.getInstance().abstractedHandler.createNametag(player);
        this.nickname = nickname;
        this.realname = player.getName();
        this.uuid = player.getUniqueId().toString();
        this.setNickname(nickname);
    }

    public void setNickname(String name) {
        Player player = Bukkit.getPlayer(UUID.fromString(uuid));
        if(player == null) return;
        if(!player.hasPermission("nicknames.color"))
            name = MiniMessage.miniMessage().escapeTags(name);
        this.nickname = name;
        this.nametag.setName(MiniMessage.miniMessage().deserialize(name));
    }

    public void showRealName(Player requester, boolean enable){
        if(enable){
            nametag.setLocalizedName(MiniMessage.miniMessage()
                    .deserialize("<white>" + this.nickname + " <gray>[" +realname + "]"), requester);
        } else{
            nametag.setLocalizedName(MiniMessage.miniMessage().deserialize(nickname), requester);
        }
    }
    public String getNickname() { return this.nickname; }

    public void unNick(){
        nametag.destroy();
    }

    public void loadNickname(Player player){
        nametag.spawn(player);
    }

}
