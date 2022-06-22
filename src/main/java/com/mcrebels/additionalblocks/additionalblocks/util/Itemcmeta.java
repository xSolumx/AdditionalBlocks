package com.mcrebels.additionalblocks.additionalblocks.util;

import com.google.common.base.Converter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class Itemcmeta implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (label.equals("Itemcmeta")){

            //Shows the player their current slots Custom model Data if any
            if (args.length == 0){
                if (sender instanceof Player){
                    Player player = (Player) sender;
                    if (player.getInventory().getItemInMainHand().hasItemMeta()){
                        if (player.getInventory().getItemInMainHand().getItemMeta().hasCustomModelData()) {
                            String returnString = "CustomModelData: "+player.getInventory().getItemInMainHand().getItemMeta().getCustomModelData();
                            return true;
                        }
                    }
                }
            }

            //Change the Item's Custom model Data
            if(String.valueOf(Integer.getInteger(args[0])).equals("null")){
                //this is a string deal with it if needed
                return true;
            }

        }
        return false;
    }
}
