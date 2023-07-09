package ca.bungo.nicknames;

import ca.bungo.nicknames.abstracted.AbstractedHandler;
import ca.bungo.nicknames.abstracted.AbstractedLink;
import ca.bungo.nicknames.commands.NicknameCommand;
import ca.bungo.nicknames.events.ConnectionEvents;
import ca.bungo.nicknames.managers.NicknameManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.lang.reflect.InvocationTargetException;

public final class NicknamesUpdated extends JavaPlugin {

    private static NicknamesUpdated instance;
    public AbstractedHandler abstractedHandler;
    public NicknameManager nicknameManager;
    public ScoreboardManager scoreboardManager;
    public Scoreboard mainScoreboard;

    private final String pkg = this.getClass().getCanonicalName().substring(0,
            this.getClass().getCanonicalName().length()-this.getClass().getSimpleName().length());

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        loadAbstract();

        this.scoreboardManager = Bukkit.getScoreboardManager();
        this.mainScoreboard = scoreboardManager.getMainScoreboard();
        this.nicknameManager = new NicknameManager();

        this.registerCommands();
        this.registerEvents();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerCommands(){
        this.getServer().getCommandMap().register("nicknames", new NicknameCommand("nick"));
    }

    private void registerEvents(){
        this.getServer().getPluginManager().registerEvents(new ConnectionEvents(), this);
    }

    public static NicknamesUpdated getInstance(){
        return instance;
    }

    private void loadAbstract(){

        AbstractedLink helper =  new AbstractedLink(){
            @Override
            public Plugin getInstance() {
                return NicknamesUpdated.getInstance();
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
