package com.mcrebels.additionalblocks.additionalblocks.Listeners;

import com.mcrebels.additionalblocks.additionalblocks.AdditionalBlocks;
import com.mcrebels.additionalblocks.additionalblocks.Items.ItemBuilder;
import com.mcrebels.additionalblocks.additionalblocks.Items.ItemHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.TileState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import java.util.logging.Level;

public class customModelPlacement implements Listener {
    public static final NamespacedKey ITEM_ID = new NamespacedKey(AdditionalBlocks.get(), "id");

    @EventHandler
    public void onplace(PlayerInteractEvent e){
        if (e.getAction().isRightClick()){
            if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)){
                return;
            }
            ItemBuilder iteminHand = new ItemBuilder(e.getPlayer().getInventory().getItemInMainHand());
            if (e.getPlayer().hasPermission("additionalblocks.admin")) {
                if (iteminHand.hasCustomTag()) {
                    if (ItemHandler.isCustomItem(iteminHand.build())) {
                        if (e.getInteractionPoint() != null) {
                            Player player = (Player) e.getPlayer();
                            Location location = e.getInteractionPoint();
                            Block clickedBlock = e.getClickedBlock();

                            Block relative = clickedBlock.getRelative(e.getBlockFace());
                            Location relaLocation = relative.getLocation().clone().toCenterLocation();
                            relaLocation.setDirection(e.getPlayer().getFacing().getDirection());
                            BoundingBox.of(relaLocation, 1.0, 0.5, 1.0);

                            relaLocation.getBlock().setType(Material.BARRIER);

                        //Use this for bouncy block bounds
                        /*Slime slime = relaLocation.getWorld().spawn(relaLocation.clone().add(0,-0.5,0),Slime.class);
                        slime.setSize(2);
                        slime.setWander(false);
                        slime.setAI(false);
                        slime.setInvisible(true);
                        slime.setInvulnerable(true);
                        slime.setCustomName("njksdfhlvksyugksdfygysdfuvsjksdf");

                         */
                            //attaching the item to an armour stand
                            ArmorStand as = relaLocation.getWorld().spawn(relaLocation, ArmorStand.class);
                            as.setItem(EquipmentSlot.HEAD, e.getPlayer().getInventory().getItemInMainHand());
                            as.setArms(false);
                            as.setBasePlate(true);
                            as.setVisible(false);
                            as.setInvulnerable(true);
                            as.setMarker(true);
                            as.setGravity(false);
                            as.setCollidable(false);
                            as.setCanTick(false);
                            Bukkit.getLogger().log(Level.INFO, "Placed CustomItem");
                        }
                    }
                } else {

                    //Bukkit.getLogger().log(Level.INFO, "This item is not custom");
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        if (e.getBlock().getType().equals(Material.BARRIER)){
            Location location = e.getBlock().getLocation().toCenterLocation();
            if (!location.getNearbyEntitiesByType(ArmorStand.class,1).isEmpty()){
                location.getNearbyEntitiesByType(ArmorStand.class,1).forEach(Entity::remove);
                Bukkit.getLogger().log(Level.INFO,"removed custom block");
            }
            else {
                Bukkit.getLogger().log(Level.INFO,"no armourstands in block");
            }
        }
    }

    @EventHandler
    public void slimeBreak(EntityDamageByEntityEvent e){
        if (e.getEntity().getType().equals(EntityType.SLIME)){
            if (e.getEntity().getName().equals("njksdfhlvksyugksdfygysdfuvsjksdf")){
                if (e.getDamager() instanceof Player) {
                    if(((Player)e.getDamager()).hasPermission("additionalblocks.admin")) {
                        Location l = e.getEntity().getLocation();
                        if (!l.getNearbyEntitiesByType(ArmorStand.class, 1).isEmpty()) {
                            l.getNearbyEntitiesByType(ArmorStand.class, 1).forEach(Entity::remove);
                            Bukkit.getLogger().log(Level.INFO, "removed custom block");
                        }
                        e.getEntity().remove();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSelectBlockClick(PlayerInteractEvent e){
        //to be implemented
    }

    public static final BlockFace[] axis = { BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST };
    public static final BlockFace[] radial = { BlockFace.NORTH, BlockFace.NORTH_EAST, BlockFace.EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH, BlockFace.SOUTH_WEST, BlockFace.WEST, BlockFace.NORTH_WEST };



}
