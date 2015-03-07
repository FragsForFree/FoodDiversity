package main.java.com.github.fragsforfree.fooddiversity.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import main.java.com.github.fragsforfree.fooddiversity.fooddiversity;

public class PlayerItemConsume implements Listener {

	private fooddiversity plugin;

	public PlayerItemConsume(fooddiversity plugin){
		this.plugin = plugin;
	}		
	
    /**
     * PlayerItemConsumeEvent from the bukkit api
     * need to identify a food consume
     * 
     * @param event
     */
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
    	// Issue https://bukkit.atlassian.net/browse/BUKKIT-4169
    	// so i had to do this code of awesome bullshit. Not nice, but it works
    	plugin.checkFoodDiversity(event.getPlayer(), event.getItem());
    }	
	
}
