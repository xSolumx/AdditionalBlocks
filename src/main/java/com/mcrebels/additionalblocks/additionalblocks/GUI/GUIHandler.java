package com.mcrebels.additionalblocks.additionalblocks.GUI;

import com.mcrebels.additionalblocks.additionalblocks.Items.ItemHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.Console;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class GUIHandler implements Listener {

    public static GUIHandler GUIhandler;
    public static GUIHandler getGUIhandler(){
        return GUIhandler;
    }


    private List<mainGUI> mainGUIs = new ArrayList<>();
    public List<mainGUI> getMainGUIs() {
        return mainGUIs;
    }

    private ItemHandler handler;
    private List<Integer> navbarslots = Arrays.asList(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8});
    private List<Integer> scrollbarslots = Arrays.asList(new Integer[]{17, 26, 35, 44, 53});


    private Integer buildingSize;
    private Integer decorationSize;
    private Integer natureSize;
    private Integer miscSize;


    private Component titleSection = Component.text(" §f\uF80C\uF809\uF80A\uF809\uF808\uF803\ue001");
    private Component navBar = Component.text("§f\uF808%n%\uF808%n%\uF808%n%\uF808%n%\uF808%n%\uF808%n%\uF808%n%\uF808%n%\uF808%n%");
    private Component scrollBar = Component.text("");

    private Component title = Component.text("§f\uF808\ue238\uF808\ue000\uF808\ue239\uF808\ue239\uF808\ue239\uF808\ue239\uF808\ue239\uF808\ue239\uF808\ue239 §f\uF80C\uF809\uF80A\uF809\uF808\uF803\ue001");

    public GUIHandler(ItemHandler handler){
        this.handler = handler;
        GUIhandler = this;


        //get categories
        buildingSize = handler.getItemBuildings().size();
        decorationSize = handler.getItemDecorations().size();
        natureSize = handler.getItemNature().size();
        miscSize = handler.getItemMisc().size();

        Bukkit.getLogger().log(Level.INFO, "GUI Handler Items :: " + buildingSize + " : " + decorationSize +" : " + natureSize + " : " + miscSize);

        createMainGuis();

    }

    public void createMainGuis(){
        if (buildingSize >= 0){
            mainGUI buildingGUI = new mainGUI(0);
            buildingGUI.addItems(handler.getItemBuildings());
            mainGUIs.add(buildingGUI);
        }
        if (decorationSize >= 0){
            mainGUI decorationGUI = new mainGUI(1);
            decorationGUI.addItems(handler.getItemDecorations());
            mainGUIs.add(decorationGUI);
        }
        if (natureSize >= 0){
            mainGUI natureGUI = new mainGUI(2);
            natureGUI.addItems(handler.getItemNature());
            mainGUIs.add(natureGUI);
        }
        if (miscSize >= 0){
            mainGUI miscGUI = new mainGUI(3);
            miscGUI.addItems(handler.getItemMisc());
            mainGUIs.add(miscGUI);
        }
    }

    //This should return the correct title component showing the active tab on the GUI
    public Component getNavBar(int guiSlot){
        int slotsUsed = getMainGUIs().size();

        Component navreturn = navBar;
        for (int i=0; i<9; i++){
                if (i == guiSlot){
                    navreturn = navreturn.replaceText(TextReplacementConfig.builder().match("%n%").replacement("§f\ue238").times(1).build());
                }
                else {
                    if (i <= 3){
                        navreturn = navreturn.replaceText(TextReplacementConfig.builder().match("%n%").replacement("§f\ue000").times(1).build());
                    }
                    else {
                        navreturn = navreturn.replaceText(TextReplacementConfig.builder().match("%n%").replacement("§f\ue239").times(1).build());
                    }
                }
        }
        navreturn = navreturn.append(titleSection);
        return navreturn;
    }

    public void setNavBar(){
        for (mainGUI mg :
                mainGUIs) {
            int currentSlot = mg.navSlot;
        }
    }

    public void changeTab(HumanEntity ent, Integer page){
        Bukkit.getLogger().log(Level.WARNING,mainGUIs.size() + " Main GUIs exist");
        try {
            mainGUIs.get(page).openInventory(ent);
        }catch (Exception e){
            Bukkit.getLogger().log(Level.INFO,"Page does not exist");
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        for (mainGUI i :
                mainGUIs) {
            if (i.getInv().equals(e.getInventory())) {
                final Player p = (Player) e.getWhoClicked();

                if (navbarslots.contains(e.getRawSlot())){
                    GUIHandler.getGUIhandler().changeTab(p,e.getRawSlot());
                    e.setCancelled(true);
                }
                if (scrollbarslots.contains(e.getRawSlot())){
                    if (i.getItemsInInv().size() > 40){
                        //add some code to handle scrolling
                        if (e.getRawSlot() == 17){
                            //i.setpage(itemsPage1);
                            Bukkit.getLogger().log(Level.INFO, "set page 1");
                        }
                        if (e.getRawSlot() == 26){
                            //i.setpage(itemsPage2);
                            Bukkit.getLogger().log(Level.INFO, "set page 2");
                        }
                        if (e.getRawSlot() == 35){
                            //i.setpage(itemsPage3);
                            Bukkit.getLogger().log(Level.INFO, "set page 3");
                        }
                    }
                    e.setCancelled(true);
                }


                int slotClicked = e.getRawSlot();
                final ItemStack clickedItem = e.getCurrentItem();

                //gives the player an item and resets it in the GUI
                if (e.getWhoClicked().getItemOnCursor().getType() != Material.AIR){
                    if (e.getRawSlot() < 54) {
                        e.setCancelled(true);
                        e.getWhoClicked().setItemOnCursor(clickedItem);
                    }
                }
                else {
                    if (e.getRawSlot() <54) {
                        e.setCancelled(true);
                        e.getWhoClicked().setItemOnCursor(clickedItem);
                    }
                }
                break;

            }
            else {
                //debugging block
            }
        }

    }

}
