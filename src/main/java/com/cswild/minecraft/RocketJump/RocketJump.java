package com.cswild.minecraft.RocketJump;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class RocketJump extends JavaPlugin {
    public static CommandManager cm;
    public static RocketJump main;
    public static ConfigManager configManager;

    @Override
    public void onEnable(){
        main = this;
        configManager = new ConfigManager();
        cm = new CommandManager();
        getCommand(CommandManager.cmd).setExecutor(cm);

        Bukkit.getPluginManager().registerEvents(new EventManager(), this);
    }

}
