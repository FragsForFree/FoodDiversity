package com.github.fragsforfree.fooddiversity;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.fragsforfree.fooddiversity.cmds.FoodDiversityCommandExecuter;
import com.github.fragsforfree.fooddiversity.config.ConfigurationManager;
import com.github.fragsforfree.fooddiversity.enums.CONFIG;
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;
import com.github.fragsforfree.fooddiversity.enums.STRINGS;
import com.github.fragsforfree.fooddiversity.events.FoodLevelChange;
import com.github.fragsforfree.fooddiversity.events.PlayerInteract;
import com.github.fragsforfree.fooddiversity.events.PlayerItemConsume;
import com.github.fragsforfree.fooddiversity.food.FoodtypeHandler;
import com.github.fragsforfree.fooddiversity.mcstat.MetricsLite;

public class FoodDiversity extends JavaPlugin implements Listener {

	public PlayerDB playerDB;
	private ConfigurationManager configManager;
	public FoodtypeHandler foodtypeHandler;
	
	/**
	 * standard onEnable method
	 */
    public void onEnable(){ 
    	foodtypeHandler = new FoodtypeHandler(this);
    	configManager = new ConfigurationManager(this, this); 
    	initialisePlayerDB();
    	addMetrics();    	
    	
    	PluginManager pm = getServer().getPluginManager();
    	pm.registerEvents(new FoodLevelChange(this),  this);
    	pm.registerEvents(new PlayerInteract(this),  this);
    	pm.registerEvents(new PlayerItemConsume(this),  this);
        
        getCommand(STRINGS.CMD.getString().toLowerCase()).setExecutor(new FoodDiversityCommandExecuter(this));
    }

    /**
     * integrate McStats
     */
    private void addMetrics(){
    	try {
    	    MetricsLite metrics = new MetricsLite(this);
    	    metrics.start();
    	} catch (IOException e) {
        	if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
        		this.getLogger().log(Level.INFO, MESSAGE.METRICS_FAILED.getMessage());
        	} 
    	}
    }
    
    /**
     * initialises the database of players to manage the plugin handling
     */
    private void initialisePlayerDB(){
    	File FOLDER = new File(this.getDataFolder() + CONFIG.PLUGIN_PATH_DATA.getPath());
    	playerDB = new PlayerDB(FOLDER, CONFIG.PLAYERDB_FILE.getString(), this);
    	playerDB.save();
    }
    
    /**
     * standard onDisable method
     */
    public void onDisable(){ 
    }
    
    /**
     * search for the the foodtype and return it back 
     * 
     * @param item - the consumed item
     * @return - a string of foodtype
     */
    private String getFoodtype(ItemStack item){	
		String itemstring = item.getType().toString();		
		return this.foodtypeHandler.getfoodtypename(itemstring);
	}
    
    /**
     * checks it the item is a cake
     * 
     * @param item - item to check
     * @return [true/false]
     */
    private boolean isCake(ItemStack item){
    	if(Material.CAKE == item.getType()){
        	if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
        		this.getLogger().log(Level.INFO, MESSAGE.ITEM_ISCAKE.getMessage());
        	} 
    		return true;
    	}
    	return false;
    }           
	
	/**
	 * main check for the blocking of consume
	 * sets the toblock and isconsuming values in the playerDB
	 * 
	 * @param player - player to identify in playerDB
	 * @param item - consumed item
	 */
    public void checkFoodDiversity(Player player, ItemStack item){
    	String type;    	
    	String uuid = player.getUniqueId().toString();
    	String name = player.getName(); 	
    	if (!player.hasPermission(STRINGS.PERM_IMMUN.getString())){    		    	
    		type = this.getFoodtype(item);	    	
	 
	    	if(type != null){
		    	playerDB.set(uuid + CONFIG.PLAYERDB_ISCAKE.getPath(), this.isCake(item));
	    		
	    		String eatentype = playerDB.getConfig().getString(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath());
	    		int eaten = playerDB.getConfig().getInt(uuid + CONFIG.PLAYERDB_EATENINROW.getPath());
		    	if(eaten >= this.foodtypeHandler.getmaxeateninrowfromfoodtype(type) && type.equals(eatentype)){    		
		    		if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
		    			this.getLogger().log(Level.INFO, MESSAGE.HAS_REACHED_LIMIT.getMessage().replace("%player",  name).replace("%value", String.valueOf(eaten)).replace("%type", eatentype)); 
		    		}	    		
		    		playerDB.set(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath(), true);
		    	} else {
		    		if (type.equals(eatentype)){
		    			eaten = eaten + 1;		    			
		    		} else {
		    			eaten = 1;
		    		}
		    		playerDB.set(uuid + CONFIG.PLAYERDB_EATENINROW.getPath(), eaten);
		    		playerDB.set(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath(), type);
		    		playerDB.set(uuid + CONFIG.PLAYERDB_NAME.getPath(), name);
		    		playerDB.set(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath(), false);		    		
		    		playerDB.save();
		    		if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
		    			this.getLogger().log(Level.INFO, MESSAGE.HAS_EATEN.getMessage().replace("%player",  name).replace("%value", String.valueOf(eaten)).replace("%type", type));
		    		}		    		
		    	}     		
	    	} else {
	    		playerDB.set(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath(), false);
	    	}
	    	playerDB.set(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath(), true);
    	} else {
    		if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
    			this.getLogger().log(Level.INFO, MESSAGE.IS_IMMUN.getMessage().replace("%player", name));
    		}  		
    	}
    }            
    
}
