package com.mcrebels.additionalblocks.additionalblocks.GUI;

import com.mcrebels.additionalblocks.additionalblocks.Items.ItemBuilder;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class mainGUI implements Listener {
    public Inventory getInv() {
        return inv;
    }

    private Inventory inv;
    private ArrayList<ItemBuilder> itemBuilders = new ArrayList<>();

    public ArrayList<ItemStack> getItemsInInv() {
        return itemsInInv;
    }

    private ArrayList<ItemStack> itemsInInv = new ArrayList<>();

    public ArrayList<ItemBuilder> getCustomItemBuilders(){
        return itemBuilders;
    }

    //5 pages of scrolling, 200 items max
    private ArrayList<ItemStack> itemsPage1 = new ArrayList<>();
    private ArrayList<ItemStack> itemsPage2 = new ArrayList<>();
    private ArrayList<ItemStack> itemsPage3 = new ArrayList<>();
    private ArrayList<ItemStack> itemsPage4 = new ArrayList<>();
    private ArrayList<ItemStack> itemsPage5 = new ArrayList<>();

    Integer navSlot;

    private List<Integer> navbarslots = Arrays.asList(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
    private List<Integer> scrollbarslots = Arrays.asList(new Integer[]{17, 26, 35, 44, 53});

    public mainGUI(Integer navSlot){
        this.navSlot = navSlot;
        Component title = GUIHandler.getGUIhandler().getNavBar(navSlot);
        inv = Bukkit.createInventory(null, 54,(title));
        setpage(itemsPage1);
    }
    public mainGUI(){
        this.navSlot = 0;
        Component title = GUIHandler.getGUIhandler().getNavBar(navSlot);
        inv = Bukkit.createInventory(null, 54,(title));
        setpage(itemsPage1);
    }

    public void addItems(ArrayList<ItemBuilder> itemBuilders){
        this.itemBuilders = itemBuilders;

        for (ItemBuilder b :
                itemBuilders) {
            itemsInInv.add(b.build());
        }

        //set items in the first page
        //idk why it has to be 44, when it should be 40, but it just is
        Bukkit.getLogger().log(Level.WARNING,"addItems() -> size: " + itemsInInv.size());
        for (int i = 0; i <itemsInInv.size() && i<44 ; i++){
            if (itemsInInv.get(i) != null) {
                itemsPage1.add(itemsInInv.get(i));
            }
        }
    }

    public void setpage(ArrayList<ItemStack> items){
        int counter = 9;
        int itemsset = 0;
        for (ItemStack is: items) {
            if (counter!=17 && counter!=26 && counter!=35 && counter!=44 && itemsset < 40){
                inv.setItem(counter, null);
                inv.setItem(counter, is);
                counter++;
                itemsset++;
            }
            else if (counter < 54){
                counter++;
                inv.setItem(counter, null);
                inv.setItem(counter, is);
                counter++;
                itemsset++;
            }
        }
        counter = 9;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
         ent.openInventory(inv);
         setpage(itemsPage1);
    }


    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (e.getInventory().equals(inv)) {
            e.setCancelled(true);
        }
    }

}
