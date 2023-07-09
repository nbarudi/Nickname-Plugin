package ca.bungo.nicknames.abstracted;

import org.bukkit.entity.Player;

public abstract class AbstractedHandler {
    protected AbstractedLink helper;

    public AbstractedHandler(AbstractedLink helper){
        this.helper = helper;
    }

    public abstract INametag createNametag(Player player);

}
