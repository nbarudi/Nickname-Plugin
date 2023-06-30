package ca.bungo.papernicknames.utility;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.Stack;

public class ChatUtility {

    public static Component formatMessage(String message){

        Stack<String> storage = new Stack<>();

        message = message.replace("&1", "<dark_blue>");
        message = message.replace("&2", "<dark_green>");
        message = message.replace("&3", "<dark_aqua>");
        message = message.replace("&4", "<dark_red>");
        message = message.replace("&5", "<dark_purple>");
        message = message.replace("&6", "<gold>");
        message = message.replace("&7", "<gray>");
        message = message.replace("&8", "<dark_gray>");
        message = message.replace("&9", "<blue>");
        message = message.replace("&0", "<black>");

        message = message.replace("&a", "<green>");
        message = message.replace("&b", "<aqua>");
        message = message.replace("&c", "<red>");
        message = message.replace("&d", "<light_purple>");
        message = message.replace("&e", "<yellow>");
        message = message.replace("&f", "<white>");


        message = message.replace("&k", "<obf>");
        message = message.replace("&l", "<b>");
        message = message.replace("&m", "<st>");
        message = message.replace("&n", "<u>");
        message = message.replace("&o", "<i>");

        Component finalComp = Component.text("");
        for(String section : message.split("&r")){
            finalComp = finalComp.append(MiniMessage.miniMessage().deserialize(section));
        }

        return finalComp;
    }

}
