package com.github.fragsforfree.fooddiversity.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.fragsforfree.fooddiversity.FoodDiversity;

public class PlayerQuit implements Listener {

	private FoodDiversity plugin;
	
	public PlayerQuit(FoodDiversity plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		String uuid = event.getPlayer().getUniqueId().toString();
		plugin.PlayerQuid(uuid);
	}
}
