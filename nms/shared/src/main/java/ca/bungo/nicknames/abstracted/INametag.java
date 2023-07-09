package ca.bungo.nicknames.abstracted;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface INametag {

    void setName(Component name);
    void setLocalizedName(String name, Player localized);
    void setLocalizedName(Component name, Player localized);
    String getName();
    void destroy();
    void spawn(Player player);

}
