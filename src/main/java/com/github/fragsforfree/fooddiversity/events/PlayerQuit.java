package com.github.fragsforfree.fooddiversity.events;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.fragsforfree.fooddiversity.FoodDiversity;
import com.github.fragsforfree.fooddiversity.PlayerDB;
import com.github.fragsforfree.fooddiversity.enums.CONFIG;
import com.github.fragsforfree.fooddiversity.food.FoodtypeHandler;
import com.github.fragsforfree.fooddiversity.messages.MessageHandler;
import com.github.fragsforfree.fooddiversity.player.FDPlayerHandler;

public class PlayerQuit implements Listener {

	private FoodDiversity plugin;
	private PlayerDB playerDB;
	private FDPlayerHandler fdplayerHandler;
	private FoodtypeHandler	foodtypeHandler;
	
	public PlayerQuit(FoodDiversity plugin, PlayerDB _playerDB, FDPlayerHandler _fdplayerHandler, FoodtypeHandler _foodtypeHandler){
		this.plugin = plugin;
		this.playerDB = _playerDB;
		this.fdplayerHandler = _fdplayerHandler;
		this.foodtypeHandler = _foodtypeHandler;
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		String uuid = event.getPlayer().getUniqueId().toString();

    	this.playerDB.set(uuid, null);
    	this.playerDB.set(uuid + CONFIG.PLAYERDB_NAME.getPath(), this.fdplayerHandler.getValueName(uuid));
    	this.playerDB.set(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath(), this.fdplayerHandler.getValueLasteatentype(uuid));
    	this.playerDB.set(uuid + CONFIG.PLAYERDB_EATENINROW.getPath(), this.fdplayerHandler.getValueEateninrow(uuid));
    	this.playerDB.set(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath(), this.fdplayerHandler.getValueToBlock(uuid));
    	this.playerDB.set(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath(), this.fdplayerHandler.getValueIsConsuming(uuid));
    	this.playerDB.set(uuid + CONFIG.PLAYERDB_ISCAKE.getPath(), this.fdplayerHandler.getValueIsCake(uuid));
    	
    	List<String> foodtypes = this.foodtypeHandler.getListFoodtypes(); 
    	for(String foodtype : foodtypes){
    		this.playerDB.set(uuid + CONFIG.PLAYERDB_DIVERSITY.getPath() + foodtype, this.fdplayerHandler.getDiversityValue(uuid, foodtype));
    	}
    	this.playerDB.save();
    	MessageHandler.sendConsoleDebug(this.plugin, Level.INFO, "Player: " + this.fdplayerHandler.getValueName(uuid) + " saved.", this.plugin.getConfigurationDebugmode());
    	
    	this.fdplayerHandler.removeFDPlayer(uuid);    	    	
	}
}
