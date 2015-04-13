package com.github.fragsforfree.fooddiversity.events;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.github.fragsforfree.fooddiversity.FoodDiversity;
import com.github.fragsforfree.fooddiversity.PlayerDB;
import com.github.fragsforfree.fooddiversity.enums.CONFIG;
import com.github.fragsforfree.fooddiversity.food.FoodtypeHandler;
import com.github.fragsforfree.fooddiversity.messages.MessageHandler;
import com.github.fragsforfree.fooddiversity.player.FDPlayerHandler;

public class PlayerJoin implements Listener{

	private FoodDiversity plugin;
	private PlayerDB playerDB;
	private FDPlayerHandler fdplayerHandler;
	private FoodtypeHandler	foodtypeHandler;
	
	public PlayerJoin(FoodDiversity plugin, PlayerDB _playerDB, FDPlayerHandler _fdplayerHandler, FoodtypeHandler _foodtypeHandler){
		this.plugin = plugin;
		this.playerDB = _playerDB;
		this.fdplayerHandler = _fdplayerHandler;
		this.foodtypeHandler = _foodtypeHandler;		
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event){
    	String uuid = event.getPlayer().getUniqueId().toString();
    	String name = event.getPlayer().getName();
    	
     	List<String> foodtypes = this.foodtypeHandler.getListFoodtypes();   	
    	this.fdplayerHandler.addFDPlayer(uuid, name);
    	if (this.playerDB.getConfig().contains(uuid)){
    		this.fdplayerHandler.setValueLasteatentype(uuid, this.playerDB.getConfig().getString(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath()));
    		this.fdplayerHandler.setValueEateninrow(uuid, this.playerDB.getConfig().getInt(uuid + CONFIG.PLAYERDB_EATENINROW.getPath()));
    		this.fdplayerHandler.setValueToblock(uuid, this.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath()));
    		this.fdplayerHandler.setValueIsConsuming(uuid, this.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath()));
    		this.fdplayerHandler.setValueIsCake(uuid, this.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCAKE.getPath()));
	    	for(String foodtype : foodtypes){
	    		this.fdplayerHandler.addDiversityEntry(uuid, foodtype, this.playerDB.getConfig().getInt(uuid + CONFIG.PLAYERDB_DIVERSITY.getPath() + foodtype));
	    	}		
    	}
    	else
    	{
    		this.fdplayerHandler.setValueLasteatentype(uuid, "nothing");
    		this.fdplayerHandler.setValueEateninrow(uuid, 0);
    		this.fdplayerHandler.setValueToblock(uuid, false);
    		this.fdplayerHandler.setValueIsConsuming(uuid, false);
    		this.fdplayerHandler.setValueIsCake(uuid, false);
	    	for(String foodtype : foodtypes){
	    		this.fdplayerHandler.addDiversityEntry(uuid, foodtype, 5);
	    	}    		
    	}    	
    	
    	MessageHandler.sendConsoleDebug(this.plugin, Level.INFO, "Diversity: " + this.fdplayerHandler.getDiversityString(uuid), this.plugin.getConfigurationDebugmode());    	
	}
	
}
