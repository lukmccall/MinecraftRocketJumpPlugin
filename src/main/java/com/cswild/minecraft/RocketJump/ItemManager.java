package com.cswild.minecraft.RocketJump;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ItemManager {
    public static HashMap<UUID,Long> cooldown = new HashMap<UUID,Long>();

    public static ItemStack RocketLauncher(String type){
        ConfigurationSection cs = RocketJump.configManager.getLaunchersConfig().getConfigurationSection(type);
        ItemStack newLauncher = new ItemStack(Material.getMaterial(cs.getString("material")),1);
        ItemMeta meta = newLauncher.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',cs.getString("name")));
        List<String> lore = cs.getStringList("lorem");
        int i = 0;
        for(String s : lore)
            lore.set(i++, ChatColor.translateAlternateColorCodes('&',s));
        meta.setLore(lore);
        newLauncher.setItemMeta(meta);
        newLauncher = setRocketLauncherTag(newLauncher, type);
        return newLauncher;
    }

    public static boolean AddRocketLauncher(Player p, String type){
        if(RocketJump.configManager.getLaunchersConfig().getConfigurationSection(type) == null)
            return false;
        p.getInventory().addItem(RocketLauncher(type));
        return  true;
    }

    public static ItemStack setRocketLauncherTag(ItemStack item, String type){
        net.minecraft.server.v1_12_R1.ItemStack nms = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag;
        if((tag = nms.getTag()) == null){
            nms.setTag(new NBTTagCompound());
            tag = nms.getTag();
        }
        tag.setString("RocketLauncher", type);
        nms.setTag(tag);
        return CraftItemStack.asCraftMirror(nms);
    }
    public static NBTTagCompound getNBTTag(ItemStack item){
        return CraftItemStack.asNMSCopy(item).getTag();
    }

    public static void spawnRocket(Player p, String type){
        ConfigurationSection  cs = RocketJump.configManager.getRocketsConfig().getConfigurationSection(type);
        Location loc = p.getEyeLocation().toVector().add(p.getLocation().getDirection().multiply(2)).toLocation(p.getWorld(),p.getLocation().getYaw(),p.getLocation().getPitch());
        Fireball fireball = p.getWorld().spawn(loc,Fireball.class);
        fireball.setShooter(p);
        fireball.setIsIncendiary(false);
        fireball.setYield(0);
        fireball.setGravity(cs.getBoolean("gravity"));
        fireball.setFallDistance((float) cs.getDouble("distance"));
        fireball.setBounce(false);
        fireball.setMetadata("rocket", new FixedMetadataValue(RocketJump.main, type ));
        fireball.setVelocity(p.getEyeLocation().getDirection().multiply(cs.getDouble("speed")));
    }

    public static long getMilliSecondsUntilCanFire(Player p, long fireRate){
        long sec = System.nanoTime() / 1000000;
        if(cooldown.get(p.getUniqueId()) == null){
            cooldown.put(p.getUniqueId(),sec + fireRate);
            return 0;
        }
        long canShoot = cooldown.get(p.getUniqueId());
        long ret;
        if( (ret = canShoot - sec) <= 0) {
            cooldown.put(p.getUniqueId(),sec + fireRate);
            return 0;
        }
        return ret;
    }
}
