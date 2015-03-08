package com.github.fragsforfree.fooddiversity.events;

import java.util.logging.Level;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.github.fragsforfree.fooddiversity.FoodDiversity;
import com.github.fragsforfree.fooddiversity.enums.CONFIG;
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;

public class PlayerInteract implements Listener {

	private FoodDiversity plugin;
	private World world;
	private Location loc;

	public PlayerInteract(FoodDiversity plugin){
		this.plugin = plugin;
	}	
	
    /**
     * PlayerInteractEvent from the bukkit api
     * need to handle the consume of a cake
     * if the consume is needed to block, the damaged clicked block will be restored
     * 
     * @param event
     */
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onInteract(PlayerInteractEvent event){
    	if((event.getPlayer().getFoodLevel() < 20) && (event.getClickedBlock() != null)) {
	    	if(Material.CAKE_BLOCK == event.getClickedBlock().getType()){
				if (plugin.getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
					plugin.getLogger().log(Level.INFO, MESSAGE.CLICKED_CAKE.getMessage());
				}    	
				
				ItemStack item = new ItemStack(Material.CAKE);
				plugin.checkFoodDiversity(event.getPlayer(), item);
				
				String uuid = event.getPlayer().getUniqueId().toString();				
	        	if(plugin.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath()) && (plugin.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath()))){
	        		if (Action.RIGHT_CLICK_BLOCK == event.getAction()){
		        		if (event.getClickedBlock().getData() > 0) {
		        			event.getClickedBlock().setData((byte) (event.getClickedBlock().getData() -1)); //lose at least one damage, if the cake is full
		        		} else {
		        			loc = event.getClickedBlock().getLocation();
		        			world = event.getClickedBlock().getWorld();
		        			
		        			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,  new Runnable() {
		        				
		        				public void run(){	        						        					
		        					world.getBlockAt(loc).setData((byte) (world.getBlockAt(loc).getData() -1));;
		        					if (plugin.getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
		        						plugin.getServer().getLogger().log(Level.INFO, MESSAGE.SCHEDULER_RUN.getMessage());
		        					}
		        				}	        				
		        				
		        			}, 10L);
	
		        		}	        			
	        		}
	        		
	        	}
	    	}	    		
    	}	
    }	
	
}
