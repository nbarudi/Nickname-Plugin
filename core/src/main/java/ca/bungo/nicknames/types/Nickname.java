package ca.bungo.nicknames.types;

import ca.bungo.nicknames.NicknamesUpdated;
import ca.bungo.nicknames.abstracted.INametag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;

public class Nickname {

    private String nickname;
    private String realname;
    private final INametag nametag;

    public Nickname(Player player, String nickname){
        nametag = NicknamesUpdated.getInstance().abstractedHandler.createNametag(player);
        this.setNickname(nickname);
        this.nickname = nickname;
        this.realname = player.getName();
    }

    public void setNickname(String name) {
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
