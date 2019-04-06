package com.cswild.minecraft.RocketJump;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;


public class EventManager implements Listener {

    @EventHandler(priority=EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event){
        Player player = event.getPlayer();
        if((event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
            ItemStack item = player.getInventory().getItemInMainHand();
            NBTTagCompound tag = ItemManager.getNBTTag(item);
            if(item != null && tag != null && tag.hasKey("RocketLauncher")) {
                String type = tag.getString("RocketLauncher");
                ConfigurationSection gun  = RocketJump.configManager.getLaunchersConfig().getConfigurationSection(type);
                long time;
                if( (time = ItemManager.getMilliSecondsUntilCanFire(player, (long)(gun.getDouble("fire_rate") * 1000))) == 0)
                    ItemManager.spawnRocket(player, gun.getString("rockets"));
                else
                    player.sendMessage(ChatColor.RED+"Cooldown: " + time/1000.0);
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onProjectileDamage(EntityDamageByEntityEvent event){
       if(event.getDamager().hasMetadata("rocket")) {
            event.setCancelled(true);
       }
    }

    @EventHandler
    public void onProjectileHit(final ProjectileHitEvent event) {
        Entity projectile = event.getEntity();
        if(projectile.hasMetadata("rocket")){
            String type = projectile.getMetadata("rocket").get(0).asString();
            ConfigurationSection cs = RocketJump.configManager.getRocketsConfig().getConfigurationSection(type);
            List<Entity> near = LogicManager.getNearbyEntity(projectile.getLocation(),cs.getDouble("range"));
            boolean selfDamage = cs.getBoolean("self_damage");
            double dmg  = cs.getDouble("damage");
            for (Entity e : near) {
                LivingEntity le = (LivingEntity) e;
                if(dmg != 0.0 && (selfDamage || le != ((Projectile) projectile).getShooter()))
                    le.damage(dmg);
                if(le.getHealth() > 0.0)
                    LogicManager.explosionEffect(projectile.getLocation(), e, cs.getString("calc_type"), cs.getDouble("power"));
            }
        }
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event){
        ItemManager.cooldown.remove(event.getPlayer().getUniqueId());
    }

}
