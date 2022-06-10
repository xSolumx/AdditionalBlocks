package com.mcrebels.additionalblocks.additionalblocks.Items;

import com.mcrebels.additionalblocks.additionalblocks.AdditionalBlocks;
import com.mcrebels.additionalblocks.additionalblocks.util.JSONParser;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public class ItemHandler {
    public static enum Categories{BUILDING,DECORATION,PLANTS,MISC}
    public static NamespacedKey ITEM_ID = new NamespacedKey(AdditionalBlocks.get(),"id");
    public static NamespacedKey CATEGORY_ID = new NamespacedKey(AdditionalBlocks.get(),"catid");

    private static ArrayList<ItemBuilder> allItems = new ArrayList<>();

    private ArrayList<ItemBuilder> itemBuildings = new ArrayList<>();
    private ArrayList<ItemBuilder> itemDecorations = new ArrayList<>();
    private ArrayList<ItemBuilder> itemNature = new ArrayList<>();
    private ArrayList<ItemBuilder> itemWeapon = new ArrayList<>();
    private ArrayList<ItemBuilder> itemMisc = new ArrayList<>();

    public ItemHandler(){
        loadCustomItems();
        sortItemsToCategories();
        //construst class
    }

    public ArrayList<ItemBuilder> getItemBuildings() {
        return itemBuildings;
    }

    public ArrayList<ItemBuilder> getItemDecorations(){
        return itemDecorations;
    }

    public ArrayList<ItemBuilder> getItemMisc(){
        return itemMisc;
    }
    public ArrayList<ItemBuilder> getItemNature() {
        return itemNature;
    }

    public ArrayList<ItemBuilder> getItemWeapon() {
        return itemWeapon;
    }

    public void sortItemsToCategories(){
        for (ItemBuilder i :
                allItems) {
            String id = i.getCustomTag(ITEM_ID, PersistentDataType.STRING);
            String category = i.getCustomTag(CATEGORY_ID,PersistentDataType.STRING);
            Bukkit.getLogger().log(Level.INFO,"cat_tag: >" + category+"<");
            if (Objects.equals(category, "building")){
                itemBuildings.add(i);
                Bukkit.getLogger().log(Level.INFO,"added buildingItem: " + i.build().getItemMeta().getPersistentDataContainer().get(CATEGORY_ID,PersistentDataType.STRING));
            }
            if (Objects.equals(category, "decoration")){
                itemDecorations.add(i);
                Bukkit.getLogger().log(Level.INFO,"added decorationItem: " + i.build().getItemMeta().getPersistentDataContainer().get(CATEGORY_ID,PersistentDataType.STRING));
            }
            if (Objects.equals(category, "nature")){
                itemNature.add(i);
                Bukkit.getLogger().log(Level.INFO,"added natureItem: " + i.build().getItemMeta().getPersistentDataContainer().get(CATEGORY_ID,PersistentDataType.STRING));
            }
            if (Objects.equals(category, "misc")){
                itemMisc.add(i);
                Bukkit.getLogger().log(Level.INFO,"added miscItem: " + i.build().getItemMeta().getPersistentDataContainer().get(CATEGORY_ID,PersistentDataType.STRING));
            }
        }
        Bukkit.getLogger().log(Level.INFO,"Loaded " + itemMisc.size() + " misc Items");
        Bukkit.getLogger().log(Level.INFO,"Loaded " + itemDecorations.size() + " Decoration Items");
        Bukkit.getLogger().log(Level.INFO,"Loaded " + itemBuildings.size() + " Building Items");
    }

    private static void loadCustomItems(){
        try {
            allItems.addAll(JSONParser.parseIB( AdditionalBlocks.get().getDataFolder()+"\\pack\\assets\\minecraft\\models\\item\\paper.json"));
            Bukkit.getLogger().log(Level.INFO,"==============Itemhandler loaded===============");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isCustomItem(ItemStack i){
        if (i.hasItemMeta()){
            if (!i.getItemMeta().getPersistentDataContainer().isEmpty()){
                for (ItemBuilder b:
                     allItems) {
                    if (i.getItemMeta().getPersistentDataContainer().has(ITEM_ID)){
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
