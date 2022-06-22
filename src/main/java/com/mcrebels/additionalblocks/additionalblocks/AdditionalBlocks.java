package com.mcrebels.additionalblocks.additionalblocks;
import com.mcrebels.additionalblocks.additionalblocks.GUI.GUIHandler;
import com.mcrebels.additionalblocks.additionalblocks.GUI.mainGUI;
import com.mcrebels.additionalblocks.additionalblocks.Items.ItemHandler;
import com.mcrebels.additionalblocks.additionalblocks.Listeners.commandListener;
import com.mcrebels.additionalblocks.additionalblocks.Listeners.customModelPlacement;
import com.mcrebels.additionalblocks.additionalblocks.util.JSONParser;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public final class AdditionalBlocks extends JavaPlugin {
    private static AdditionalBlocks additionalBlocks;
    private static ArrayList<ItemStack> allCustomItems = new ArrayList<>();

    private static ItemHandler itemHandler;
    private static GUIHandler guiHandler;

    public static GUIHandler getGuiHandler() {
        return guiHandler;
    }

    public static ItemHandler getItemHandler(){
        return itemHandler;
    }

    public static Plugin get(){
        return additionalBlocks;
    }


    @Override
    public void onEnable() {
        additionalBlocks = this;
        this.getLogger().log(Level.INFO,"Downloading resource pack...");
        //Downloads resource pack file from source and directly unpacks it
        //Would be nice to set up an auto release for the texture pack on github for this
        try {
            BufferedInputStream is = new BufferedInputStream(new URL("https://www.dropbox.com/s/h9fgwyzva2xddez/pack.zip?dl=1").openStream());
            this.getLogger().log(Level.INFO,"Unpacking resource pack...");
            unzip(is, Paths.get(getDataFolder() + "/pack"));
            this.getLogger().log(Level.WARNING, "Extraction succesful");
        }catch (Exception e){
            e.printStackTrace();
        }

        //Attempts to get all Json files in the target directory
        File directoryPath = new File(getDataFolder()+"/pack/assets/mcrebels/models");

        FilenameFilter jsonfilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                String lowercaseName = name.toLowerCase();
                if (lowercaseName.endsWith(".json")) {
                    return true;
                } else {
                    return false;
                }
            }
        };

        File[] filesList = directoryPath.listFiles(jsonfilter);
        List<File> modelList = new ArrayList<File>();

        //Adds model files to
        for (File file : filesList) {
            if (file.isFile()){
                //this.getLogger().log(Level.INFO,"FileName: "+file.getName());
                modelList.add(file);
            }
        }


        //Creates all custom predicates from the paper.json
        itemHandler = new ItemHandler("/pack/assets/minecraft/models/item/paper.json");
        guiHandler = new GUIHandler(itemHandler);

        //register Listeners
        Bukkit.getPluginManager().registerEvents(guiHandler, this);
        Bukkit.getPluginManager().registerEvents(new customModelPlacement(), this);

        //register GUI Command
        this.getCommand("ci").setExecutor(new commandListener());
        this.getCommand("openinv").setExecutor(new commandListener());
    }



    public static void unzip(InputStream is, Path targetDir) throws IOException {
        targetDir = targetDir.toAbsolutePath();
        try (ZipInputStream zipIn = new ZipInputStream(is)) {
            for (ZipEntry ze; (ze = zipIn.getNextEntry()) != null; ) {
                Path resolvedPath = targetDir.resolve(ze.getName()).normalize();
                if (!resolvedPath.startsWith(targetDir)) {
                    // see: https://snyk.io/research/zip-slip-vulnerability
                    throw new RuntimeException("Entry with an illegal path: "
                            + ze.getName());
                }
                if (ze.isDirectory()) {
                    Files.createDirectories(resolvedPath);
                } else {
                    Files.createDirectories(resolvedPath.getParent());
                    Files.copy(zipIn, resolvedPath,REPLACE_EXISTING);
                }
            }
        }
    }

    @Override
    public void onDisable() {
        Bukkit.getPluginManager().disablePlugin(this);
        // Plugin shutdown logic
    }
}
