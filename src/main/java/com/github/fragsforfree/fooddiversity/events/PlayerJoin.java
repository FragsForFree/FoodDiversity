package com.github.fragsforfree.fooddiversity.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.fragsforfree.fooddiversity.FoodDiversity;

public class PlayerJoin implements Listener{

	private FoodDiversity plugin;
	
	public PlayerJoin(FoodDiversity plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
    	String uuid = event.getPlayer().getUniqueId().toString();
    	String name = event.getPlayer().getName();
    	plugin.PlayerJoin(uuid, name);
	}
	
}
