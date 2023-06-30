package ca.bungo.papernicknames;

import ca.bungo.papernicknames.commands.NicknameCommand;
import ca.bungo.papernicknames.managers.NicknameManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public final class Nicknames extends JavaPlugin {

    public static Nicknames INSTANCE;

    public NicknameManager nicknameManager;

    private ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();

    public Scoreboard scoreboard = scoreboardManager.getMainScoreboard();


    @Override
    public void onEnable() {
        this.saveDefaultConfig();

        INSTANCE = this;
        this.nicknameManager = new NicknameManager();

        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        this.nicknameManager.cleanup();
    }

    private void registerCommands(){
        this.getServer().getCommandMap().register("nicknames", new NicknameCommand("nick"));
    }

}
