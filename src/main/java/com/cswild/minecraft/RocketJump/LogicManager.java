package com.cswild.minecraft.RocketJump;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.LinkedList;
import java.util.List;

public class LogicManager {
    public static List<Entity> getNearbyEntity(Location loc, double range){
        List<Entity> entities = new LinkedList<Entity>();
        for(Entity e : loc.getWorld().getEntities()) {
            if (e instanceof LivingEntity && e.getLocation().distance(loc) <= range)
                entities.add(e);
        }
        return entities;
    }

    public static void explosionEffect(Location loc,Entity entity, String type,double power){
        Vector force = entity.getLocation().toVector().subtract(loc.toVector()).normalize();
        if(type.equalsIgnoreCase("simple")) {
            force.setY(force.getY() + (force.getY() < -0.6 ? -1.0 : 1.0));
            force.normalize().multiply(power);
        }
        else if(type.equalsIgnoreCase("additive")){
            force.setY(force.getY() + (force.getY() < -0.6 ? -1.0 : 1.0));
            force = entity.getVelocity().add(force).normalize().multiply(power);
        }
        entity.setVelocity(force);
//        entity.setVelocity(entity.getVelocity().add(force).normalize().multiply(power));
        entity.setFallDistance(0.f);
    }
}
