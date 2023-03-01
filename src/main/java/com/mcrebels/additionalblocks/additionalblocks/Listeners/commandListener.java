package com.mcrebels.additionalblocks.additionalblocks.Listeners;

import com.mcrebels.additionalblocks.additionalblocks.AdditionalBlocks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.logging.Level;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class commandListener implements CommandExecutor {

    public ArrayList<ItemStack> itemsInInv;
    Plugin plugin = getPlugin(AdditionalBlocks.class);

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            if (sender.hasPermission("additionalblocks.admin")) {
                Player player = (Player) sender;
                AdditionalBlocks.getGuiHandler().changeTab(player, 0);
            } else {
                Bukkit.getLogger().log(Level.WARNING, "Command sender not Player");
            }
        }
        return true;
        }

    }

