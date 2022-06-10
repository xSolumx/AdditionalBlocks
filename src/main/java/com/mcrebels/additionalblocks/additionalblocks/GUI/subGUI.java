package com.mcrebels.additionalblocks.additionalblocks.GUI;

import com.mcrebels.additionalblocks.additionalblocks.AdditionalBlocks;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class subGUI implements Listener {
    private static Inventory inv;
    public ArrayList<ItemStack> itemstoAddtoInv;
    Plugin plugin = getPlugin(AdditionalBlocks.class);

    public subGUI(ArrayList<ItemStack> items){
        itemstoAddtoInv = items;
        Component scrollbar = Component.text("");
        Component navbar = Component.text("");
        Component title = Component.text("§f\uF808\ue238\uF808\ue239\uF808\ue239\uF808\ue239\uF808\ue239\uF808\ue239\uF808\ue239\uF808\ue239\uF808\ue239 §f\uF80C\uF809\uF80A\uF809\uF808\uF803\ue001");
        inv = Bukkit.createInventory(null, 54,(title));

        initializeItems();
    }

    public void initializeItems(){

        int counter = 9;
        for (ItemStack is: itemstoAddtoInv) {
            if (counter!=17 && counter!=26 && counter!=35 && counter!=44 && counter < 53){
                inv.setItem(counter, is);
                counter++;
            }
            else if (counter < 53){
                counter++;
            }
            else break;
        }
    }
    public static void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }
}
