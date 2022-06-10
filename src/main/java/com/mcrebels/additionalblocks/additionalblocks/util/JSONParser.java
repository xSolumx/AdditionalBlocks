package com.mcrebels.additionalblocks.additionalblocks.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.mcrebels.additionalblocks.additionalblocks.AdditionalBlocks;
import com.mcrebels.additionalblocks.additionalblocks.Items.ItemBuilder;
import com.mcrebels.additionalblocks.additionalblocks.Items.ItemHandler;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class JSONParser {
    public static ArrayList<ItemStack> parse(String path) throws Exception {

        ArrayList<ItemStack> toReturn = new ArrayList<ItemStack>();
        Plugin plugin = getPlugin(AdditionalBlocks.class);

        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createParser(new File(path));
        JsonToken current;
        current = jp.nextToken();
        StringBuffer parentModel = new StringBuffer(64);
        if (current != JsonToken.START_OBJECT) {
            Bukkit.getLogger().log(Level.INFO,("Error: root should be object: quiting."));
            return null;
        }
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            // move from field name to field value
            current = jp.nextToken();
            if (fieldName.equals("textures") && (parentModel.length() == 0)){
                Bukkit.getLogger().log(Level.INFO, "textures Found");
                try {
                    JsonNode node = jp.readValueAsTree();
                    parentModel.insert(0,(node.findValue("layer0").asText()));
                    Bukkit.getLogger().log(Level.INFO, "parent texture Found:" + parentModel);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("overrides")) {
                if (current == JsonToken.START_ARRAY) {
                    // For each of the records in the array
                    while (jp.nextToken() != JsonToken.END_ARRAY){
                        // read the record into a tree model,
                        JsonNode node = jp.readValueAsTree();
                        Integer cmd = node.findValue("custom_model_data").asInt();
                        String modelPath = node.findValue("model").asText();
                        // And now we have random access to everything in the object
                        if (!(parentModel.length() == 0)){
                            try {
                                String s = parentModel.toString().split("/")[1];
                                String n = modelPath.toString().split("/")[modelPath.toString().split("/").length-1];
                                Bukkit.getLogger().log(Level.INFO,s);

                                NamespacedKey itemIDkey = new NamespacedKey(plugin,"id");
                                NamespacedKey itemCatkey = new NamespacedKey(plugin,"catid");
                                ItemBuilder itemBuilder = new ItemBuilder(new ItemStack(Material.getMaterial(s.toUpperCase())));
                                itemBuilder.addCustomTag(itemIDkey, PersistentDataType.INTEGER,cmd);
                                itemBuilder.setCustomModelData(cmd);
                                itemBuilder.setDisplayName(n);
                                ItemStack temp = itemBuilder.build();

                                toReturn.add(temp);
                                Bukkit.getLogger().log(Level.INFO, "Item Added to return list");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else {
                            Bukkit.getLogger().log(Level.INFO, "apparently the string is still null???");
                            Bukkit.getLogger().log(Level.INFO, parentModel.toString());
                        }
                    }
                } else {
                    Bukkit.getLogger().log(Level.INFO,("Error: records should be an array: skipping."));
                    jp.skipChildren();
                }
            } else {
                Bukkit.getLogger().log(Level.INFO,("Unprocessed property: " + fieldName));
                jp.skipChildren();
            }
        }

        return toReturn;
    }

    public static ArrayList<ItemStack> parseBlockstate(String path) throws IOException {
        ArrayList<ItemStack> toReturn = new ArrayList<ItemStack>();

        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createParser(new File(path));
        JsonToken current;
        current = jp.nextToken();
        StringBuffer parentModel = new StringBuffer(64);
        if (current != JsonToken.START_OBJECT) {
            Bukkit.getLogger().log(Level.INFO,("Error: root should be object: quiting."));
            return null;
        }
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            // move from field name to field value
            current = jp.nextToken();
            if (fieldName.equals("variants")) {
                if (current == JsonToken.START_OBJECT) {
                    // For each of the records in the object
                    while (jp.nextToken() != JsonToken.END_OBJECT && jp.nextToken() != null){
                        // read the record into a tree model,
                        String iParent = jp.getCurrentName();
                        AtomicReference<String> model = new AtomicReference<>("");
                        AtomicReference<String> category = new AtomicReference<>("");

                        JsonNode node = jp.readValueAsTree();

                        node.fields().forEachRemaining( n -> {
                            if (n.getKey().equals("model")){
                                model.set(n.getValue().asText());
                            }

                            if (n.getKey().equals("category")){
                                category.set(n.getValue().asText());
                            }
                        });

                        String[] iSplit = iParent.split(",");
                        String Instrument = iSplit[0].split("=")[1];
                        String Note = iSplit[1].split("=")[1];
                        String Powered = iSplit[2].split("=")[1];
                        Bukkit.getLogger().log(Level.INFO,"Instrument: " + Instrument);
                        Bukkit.getLogger().log(Level.INFO,"Note: " + Note);
                        Bukkit.getLogger().log(Level.INFO,"Powered: " + Powered);
                        Bukkit.getLogger().log(Level.INFO,"===========================");

                        ItemStack temp = new ItemStack(Material.NOTE_BLOCK,1);
                        ItemMeta meta = temp.getItemMeta();
                        meta.displayName(Component.text(iParent));
                        temp.setItemMeta(meta);
                        toReturn.add(temp);

                    }

                } else {
                    Bukkit.getLogger().log(Level.INFO,("Error: records should be an array: skipping."));
                    jp.skipChildren();
                }


            } else {
                Bukkit.getLogger().log(Level.INFO,("Unprocessed property: " + fieldName));
                jp.skipChildren();
            }
        }

        return toReturn;
    }

    public static ArrayList<ItemBuilder> parseIB(String path) throws Exception {

        ArrayList<ItemBuilder> toReturn = new ArrayList<ItemBuilder>();
        Plugin plugin = getPlugin(AdditionalBlocks.class);

        JsonFactory f = new MappingJsonFactory();
        JsonParser jp = f.createParser(new File(path));
        JsonToken current;
        current = jp.nextToken();
        StringBuffer parentModel = new StringBuffer(64);
        if (current != JsonToken.START_OBJECT) {
            Bukkit.getLogger().log(Level.INFO,("Error: root should be object: quiting."));
            return null;
        }
        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            // move from field name to field value
            current = jp.nextToken();
            if (fieldName.equals("textures") && (parentModel.length() == 0)){
                Bukkit.getLogger().log(Level.INFO, "textures Found");
                try {
                    JsonNode node = jp.readValueAsTree();
                    parentModel.insert(0,(node.findValue("layer0").asText()));
                    Bukkit.getLogger().log(Level.INFO, "parent texture Found:" + parentModel);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
            if (fieldName.equals("overrides")) {
                if (current == JsonToken.START_ARRAY) {
                    // For each of the records in the array
                    while (jp.nextToken() != JsonToken.END_ARRAY){
                        // read the record into a tree model,
                        JsonNode node = jp.readValueAsTree();
                        Integer cmd = node.findValue("custom_model_data").asInt();
                        String modelPath = node.findValue("model").asText();
                        String category = null;
                        if (node.findValue("category") != null) {
                            category = node.findValue("category").asText();
                        }
                        // And now we have random access to everything in the object
                        if (!(parentModel.length() == 0)){
                            try {
                                String s = parentModel.toString().split("/")[1];
                                String n = modelPath.toString().split("/")[modelPath.toString().split("/").length-1];
                                String parent = parentModel.toString().split("/")[1];
                                Bukkit.getLogger().log(Level.INFO,n+" "+s+" "+category);


                                ItemBuilder itemBuilder = new ItemBuilder(new ItemStack(Material.getMaterial(s.toUpperCase())));
                                itemBuilder.setCustomTag(ItemHandler.ITEM_ID, PersistentDataType.STRING,parent+cmd);
                                itemBuilder.setCustomTag(ItemHandler.CATEGORY_ID, PersistentDataType.STRING, Objects.requireNonNullElse(category, "misc"));
                                Bukkit.getLogger().log(Level.INFO, "tage in handler: " + itemBuilder.getCustomTag(ItemHandler.CATEGORY_ID,PersistentDataType.STRING));

                                itemBuilder.setCustomModelData(cmd);
                                itemBuilder.setDisplayName(n);
                                itemBuilder.regen();

                                toReturn.add(itemBuilder);
                                Bukkit.getLogger().log(Level.INFO, "Item Added to return list");
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else {
                            Bukkit.getLogger().log(Level.INFO, "apparently the string is still null???");
                            Bukkit.getLogger().log(Level.INFO, parentModel.toString());
                        }
                    }
                } else {
                    Bukkit.getLogger().log(Level.INFO,("Error: records should be an array: skipping."));
                    jp.skipChildren();
                }
            } else {
                Bukkit.getLogger().log(Level.INFO,("Unprocessed property: " + fieldName));
                jp.skipChildren();
            }
        }

        return toReturn;
    }
}
