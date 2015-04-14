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
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;
import com.github.fragsforfree.fooddiversity.messages.MessageHandler;
import com.github.fragsforfree.fooddiversity.player.FDPlayerHandler;

public class PlayerInteract implements Listener {

	private FoodDiversity plugin;
	private FDPlayerHandler fdplayerhandler;
	private World world;
	private Location loc;

	public PlayerInteract(FoodDiversity plugin, FDPlayerHandler _fdplayerhandler){
		this.plugin = plugin;
		this.fdplayerhandler = _fdplayerhandler;
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
				MessageHandler.sendConsoleDebug(plugin, Level.INFO, MESSAGE.CLICKED_CAKE.getMessage(), plugin.getConfigurationDebugmode());   	
				
				ItemStack item = new ItemStack(Material.CAKE);
				plugin.checkFoodDiversity(event.getPlayer(), item);
				
				String uuid = event.getPlayer().getUniqueId().toString();				
				
				if (this.plugin.getConfigurationFeatureItemInRow()){
					if(this.fdplayerhandler.getValueToBlock(uuid) && (this.fdplayerhandler.getValueIsConsuming(uuid))){
		        		if (Action.RIGHT_CLICK_BLOCK == event.getAction()){
			        		if (event.getClickedBlock().getData() > 0) {
			        			event.getClickedBlock().setData((byte) (event.getClickedBlock().getData() -1)); //lose at least one damage, if the cake is full
			        		} else {
			        			loc = event.getClickedBlock().getLocation();
			        			world = event.getClickedBlock().getWorld();
			        			
			        			plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin,  new Runnable() {
			        				
			        				public void run(){	        						        					
			        					world.getBlockAt(loc).setData((byte) (world.getBlockAt(loc).getData() -1));;
			        					MessageHandler.sendConsoleDebug(plugin, Level.INFO, MESSAGE.SCHEDULER_RUN.getMessage(), plugin.getConfigurationDebugmode());
			        				}	        				
			        				
			        			}, 10L);
		
			        		}	        			
		        		}		        		
		        	}
				}
	    	}	    		
    	}	
    }	
	
}
