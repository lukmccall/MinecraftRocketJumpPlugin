package com.cswild.minecraft.RocketJump;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class ConfigManager {
    private FileConfiguration launchersConfig;
    private File launchersFile;
    private FileConfiguration rocketsConfig;
    private File rocketsFile;

    public ConfigManager(){
        setUp();
    }

    public void setUp(){
        try{
            if(!RocketJump.main.getDataFolder().exists())
                RocketJump.main.getDataFolder().mkdir();
            launchersSetUp();
            rocketsSetUp();
        } catch (IOException e){
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }
    }

    public FileConfiguration getRocketsConfig() {
        return rocketsConfig;
    }

    public FileConfiguration getLaunchersConfig(){
        return launchersConfig;
    }

    private void launchersSetUp() throws IOException {
        launchersFile = new File(RocketJump.main.getDataFolder(), "launchers.yml");
        if(!launchersFile.exists())
            launchersFile.createNewFile();
        reloadLaunchersConfig();
    }

    private void rocketsSetUp() throws IOException {
        rocketsFile = new File(RocketJump.main.getDataFolder(),"rockets.yml");
        if(!rocketsFile.exists())
            rocketsFile.createNewFile();
        reloadRocketsConfig();
    }


    public void saveLaunchersConfig() {
        try {
            launchersConfig.save(launchersFile);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRocketsConfig() {
        try {
            rocketsConfig.save(rocketsFile);
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void reloadLaunchersConfig(){
        launchersConfig = YamlConfiguration.loadConfiguration(launchersFile);
        Reader defConfigStream = null;
        try{
            InputStream res = RocketJump.main.getResource("launchers.yml");
            if(res != null)
                defConfigStream = new InputStreamReader(res,"UTF8");
        }  catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(defConfigStream != null){
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            launchersConfig.setDefaults(defConfig);
            launchersConfig.options().copyDefaults(true);
            saveLaunchersConfig();
            try {
                defConfigStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void reloadRocketsConfig(){
        rocketsConfig = YamlConfiguration.loadConfiguration(rocketsFile);
        Reader defConfigStream = null;
        try{
            InputStream res = RocketJump.main.getResource("rockets.yml");
            if(res != null)
                defConfigStream = new InputStreamReader(res,"UTF8");
        }  catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if(defConfigStream != null){
            YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
            rocketsConfig.setDefaults(defConfig);
            rocketsConfig.options().copyDefaults(true);
            saveRocketsConfig();
            try {
                defConfigStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
