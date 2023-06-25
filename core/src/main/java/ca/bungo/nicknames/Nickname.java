package ca.bungo.nicknames;

import ca.bungo.nicknames.abstracted.AbstractedHandler;
import ca.bungo.nicknames.abstracted.AbstractedLink;
import ca.bungo.nicknames.commands.NicknameCommand;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;

public final class Nickname extends JavaPlugin {

    private static Nickname instance;
    public AbstractedHandler abstractedHandler;

    private final String pkg = this.getClass().getCanonicalName().substring(0,
            this.getClass().getCanonicalName().length()-this.getClass().getSimpleName().length());

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        this.saveDefaultConfig();
        loadAbstract();
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Nickname getInstance(){
        return instance;
    }


    private void registerCommands(){
        getServer().getCommandMap().register("nickname", new NicknameCommand("nick"));
    }

    private void loadAbstract(){

        AbstractedLink helper =  new AbstractedLink(){
            @Override
            public Plugin getInstance() {
                return Nickname.getInstance();
            }
        };

        String ver = Bukkit.getServer().getClass().getPackage().getName().replace('.', ',').split(",")[3];
        getLogger().info("Attempting to load version: " + ver);
        try {
            Class<?> handler = Class.forName(pkg + "abstracted." + ver + ".Abstracted" + ver);
            this.abstractedHandler = (AbstractedHandler) handler.getConstructor(AbstractedLink.class).newInstance(helper);
            getLogger().info("Loaded NMS version: " + ver + "!");
        } catch(ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
                InvocationTargetException e){
            e.printStackTrace();
            getLogger().warning("Failed to find Abstract Handlder for version: " + ver);
            getLogger().warning("Attempted Class: " + pkg + "abstracted." + ver + ".Abstracted" + ver);
        }
    }
}
