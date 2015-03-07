package main.java.com.github.fragsforfree.fooddiversity.events;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import main.java.com.github.fragsforfree.fooddiversity.fooddiversity;
import main.java.com.github.fragsforfree.fooddiversity.enums.CONFIG;
import main.java.com.github.fragsforfree.fooddiversity.enums.MESSAGE;

public class FoodLevelChange implements Listener {

	private fooddiversity plugin;

	public FoodLevelChange(fooddiversity plugin){
		this.plugin = plugin;
	}	
	
    /**
     * workaround method: https://bukkit.atlassian.net/browse/BUKKIT-4169
     * FoodLevelChangeEvent from bukkit api
     * roleback the event actions if needed. 
     * 
     * @param event
     */
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {    	    	   		
    	if (event.getEntityType() == EntityType.PLAYER) {
    		    		
    		Player player = (Player) event.getEntity();	        		        	
        	String uuid = player.getUniqueId().toString();
        	        	
        	if(plugin.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath()) && (plugin.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath()))){
	    		plugin.playerDB.set(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath(), false);	    		
	    		
	    		if(!plugin.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCAKE.getPath())){
		    		ItemStack item = player.getItemInHand();	    			    		
	    	    	if (plugin.getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
		        		plugin.getLogger().log(Level.INFO, MESSAGE.ITEM_AMOUNT.getMessage().replace("%item", item.getType().toString()).replace("%value", String.valueOf(item.getAmount())));
		        	}    	
		        	item.setAmount(item.getAmount() + 1);	        	
		        	if (plugin.getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
		        		plugin.getLogger().log(Level.INFO, MESSAGE.ITEM_AMOUNT_NEW.getMessage().replace("%item",  item.getType().toString()).replace("%value", String.valueOf(item.getAmount())));
		        	} 
		        	player.setItemInHand(item);    	
		    		player.updateInventory();	    			
	    		}	    			    		
	        	
	        	player.sendMessage(ChatColor.RED + (plugin.getConfig().getString(CONFIG.CONFIG_MESSAGE_DIVERSITY.getPath())).replace("%foodtype", plugin.playerDB.getConfig().getString(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath())));	        	
	    		event.setCancelled(true); 	    			    		
	    		if (plugin.getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
	    			plugin.getLogger().log(Level.INFO, MESSAGE.BLOCK_INCREASE.getMessage().replace("%player", event.getEntity().getName()));
	    		}	        		
        	}
        	plugin.playerDB.set(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath(), false);
    	}
    	
    }	
	
}
