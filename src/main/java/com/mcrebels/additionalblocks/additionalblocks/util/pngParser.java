package com.mcrebels.additionalblocks.additionalblocks.util;

import com.mcrebels.additionalblocks.additionalblocks.AdditionalBlocks;
import com.sun.source.util.TreePathScanner;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.bukkit.plugin.java.JavaPlugin.getPlugin;

public class pngParser {

    private String name = "itemsconfig.yml";
    private Path file;
    private Path dir;
    Plugin plugin = getPlugin(AdditionalBlocks.class);
    private YamlConfiguration yaml;


    public void readFiles(){
        File directoryPath = new File(plugin.getDataFolder()+"/pack/assets/mcrebels/textures");

        createYAML();
    }
    List<File> textureList = new ArrayList<File>();

    private void iterate(File path){

        File[] filesList = path.listFiles(pngfilter);

        //Adds model files to
        for (File file : filesList) {
            if (file.isFile()){
                //this.getLogger().log(Level.INFO,"FileName: "+file.getName());
                textureList.add(file);
            }
            else if (file.isDirectory()){
                iterate(new File(file.getPath()));
            }
        }
    }

    private void createYAML(){
        yaml = new YamlConfiguration();
        for (File f :
                textureList) {
            if (yaml.get(f.getPath())!=null) {
                yaml.createSection(f.getPath());
            }
        }
        dir = plugin.getDataFolder().toPath();
        file = dir.resolve(name);
        plugin.saveResource(name, false);
        save();


    }
    public void save() {
        try {
            yaml.save(file.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    FilenameFilter pngfilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            String lowercaseName = name.toLowerCase();
            if (lowercaseName.endsWith(".png")) {
                return true;
            } else {
                return false;
            }
        }
    };
}
