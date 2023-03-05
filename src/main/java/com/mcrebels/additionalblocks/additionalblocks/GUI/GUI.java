package com.mcrebels.additionalblocks.additionalblocks.GUI;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;


import java.util.List;
import java.util.concurrent.CompletionException;

// NOT IN USE
public class GUI {
    // NOT IN USE
    private final Component Title;
    private final InventoryType InventoryType;
    private final List<ItemStack> Items;
    private final Inventory Inv;


    public GUI(Component title, InventoryType type, List<ItemStack> items) {
        Title = title;
        InventoryType = type;
        Items = items;
        Inv = Bukkit.createInventory(null, InventoryType, Title);
    }

    public void OpenInventory(Player player, GUI inv){
        player.openInventory(inv.Inv);
    }
}
