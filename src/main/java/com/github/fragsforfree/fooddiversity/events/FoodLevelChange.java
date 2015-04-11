package com.github.fragsforfree.fooddiversity.events;
import java.util.logging.Level;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;

import com.github.fragsforfree.fooddiversity.FoodDiversity;
import com.github.fragsforfree.fooddiversity.enums.CONFIG;
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;
import com.github.fragsforfree.fooddiversity.messages.MessageHandler;
import com.github.fragsforfree.fooddiversity.player.FDPlayerHandler;

public class FoodLevelChange implements Listener {

	private FoodDiversity plugin;
	private FDPlayerHandler fdplayerhandler;

	public FoodLevelChange(FoodDiversity plugin, FDPlayerHandler _fdplayerhandler){
		this.plugin = plugin;
		this.fdplayerhandler = _fdplayerhandler;
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
        	
        	if(this.fdplayerhandler.getValueToBlock(uuid) && (this.fdplayerhandler.getValueIsConsuming(uuid))){
        	/**if(plugin.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath()) && (plugin.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath()))){**/
        		this.fdplayerhandler.setValueToblock(uuid, false);
        		/**plugin.playerDB.set(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath(), false);**/    		
	    		
        		if(!this.fdplayerhandler.getValueIsCake(uuid)){
	    		/**if(!plugin.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCAKE.getPath())){**/
		    		ItemStack item = player.getItemInHand();	    			    		
	    	    	MessageHandler.sendConsoleDebug(plugin, Level.INFO, MESSAGE.ITEM_AMOUNT.getMessage().replace("%item", item.getType().toString()).replace("%value", String.valueOf(item.getAmount())), plugin.getDebug());  	
		        	item.setAmount(item.getAmount() + 1);	        	
		        	MessageHandler.sendConsoleDebug(plugin, Level.INFO, MESSAGE.ITEM_AMOUNT_NEW.getMessage().replace("%item",  item.getType().toString()).replace("%value", String.valueOf(item.getAmount())), plugin.getDebug());
		        	player.setItemInHand(item);    	
		    		player.updateInventory();	    			
	    		}	    			    		
        		MessageHandler.sendPlayerMessage(player, (plugin.getConfig().getString(CONFIG.CONFIG_MESSAGE_DIVERSITY.getPath())).replace("%foodtype", this.fdplayerhandler.getValueLasteatentype(uuid)), plugin.getName(), true);	        	
	        	/**MessageHandler.sendPlayerMessage(player, (plugin.getConfig().getString(CONFIG.CONFIG_MESSAGE_DIVERSITY.getPath())).replace("%foodtype", plugin.playerDB.getConfig().getString(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath())), plugin.getName(), true);**/
	    		//player.sendMessage(ChatColor.RED + (plugin.getConfig().getString(CONFIG.CONFIG_MESSAGE_DIVERSITY.getPath())).replace("%foodtype", plugin.playerDB.getConfig().getString(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath())));	        	
	    		event.setCancelled(true); 	    			    		
	    		MessageHandler.sendConsoleDebug(plugin, Level.INFO, MESSAGE.BLOCK_INCREASE.getMessage().replace("%player", event.getEntity().getName()), plugin.getDebug());	        		
        	}
        	this.fdplayerhandler.setValueIsConsuming(uuid, false);
        	/**plugin.playerDB.set(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath(), false);**/
    	}
    	
    }	
	
}
