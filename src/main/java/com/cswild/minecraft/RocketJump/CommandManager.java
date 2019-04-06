package com.cswild.minecraft.RocketJump;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
    public final static String cmd = "RocketJump";

    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if(command.getName().equalsIgnoreCase(cmd)){
            if(commandSender instanceof Player){
                if(args.length > 0) {
                    if (args[0].equalsIgnoreCase("give"))
                        if(args.length > 3){
                            if(commandSender.hasPermission("rocketjump.give")) {
                                Player player = Bukkit.getServer().getPlayer(args[2]);

                                if (player != null)
                                    if (ItemManager.AddRocketLauncher(player, args[1]))
                                        commandSender.sendMessage(ChatColor.GREEN + "Launcher Add");
                                    else
                                        commandSender.sendMessage(ChatColor.RED + "Launcher Adding Failed");
                            } else
                                commandSender.sendMessage(ChatColor.RED+"You don\'t have permission to do that");
                        } else {
                            Player player = (Player) commandSender;
                            ItemManager.AddRocketLauncher(player,args[1]);
                        }
                }
            }
        }
        return true;
    }
}
