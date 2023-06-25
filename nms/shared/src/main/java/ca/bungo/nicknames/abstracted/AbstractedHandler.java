package ca.bungo.nicknames.abstracted;

import org.bukkit.entity.Player;

public abstract class AbstractedHandler {
    protected AbstractedLink helper;

    public AbstractedHandler(AbstractedLink helper){
        this.helper = helper;
    }

    public abstract void hidePlayerName(Player player);
    public abstract void showPlayerName(Player player);

}
