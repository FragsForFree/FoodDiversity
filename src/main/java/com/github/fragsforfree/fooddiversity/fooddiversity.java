package main.java.com.github.fragsforfree.fooddiversity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.com.github.fragsforfree.fooddiversity.cmds.FoodDiversityCommandExecuter;
import main.java.com.github.fragsforfree.fooddiversity.config.ConfigurationManager;
import main.java.com.github.fragsforfree.fooddiversity.enums.CONFIG;
import main.java.com.github.fragsforfree.fooddiversity.enums.MESSAGE;
import main.java.com.github.fragsforfree.fooddiversity.enums.STRINGS;
import main.java.com.github.fragsforfree.fooddiversity.events.FoodLevelChange;
import main.java.com.github.fragsforfree.fooddiversity.events.PlayerInteract;
import main.java.com.github.fragsforfree.fooddiversity.events.PlayerItemConsume;
import main.java.com.github.fragsforfree.fooddiversity.mcstat.MetricsLite;

public class fooddiversity extends JavaPlugin implements Listener {

	public PlayerDB playerDB;
	private ConfigurationManager configManager;
	
	/**
	 * standard onEnable method
	 */
    public void onEnable(){ 
    	configManager = new ConfigurationManager(this);
    	configManager.initialiseConfig(); 
    	configManager.checkConfigItems();
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
		
		List<String> listOfMeat = getConfig().getStringList(CONFIG.CONFIG_ITEMS_MEAT.getPath());
		List<String> listOfFruit = getConfig().getStringList(CONFIG.CONFIG_ITEMS_FRUIT.getPath());
		List<String> listOfSpezial = getConfig().getStringList(CONFIG.CONFIG_ITEMS_SPEZIAL.getPath());
		
		if(isListvalue(itemstring, listOfMeat, STRINGS.MEAT.getString())){return STRINGS.MEAT.getString();}
		if(isListvalue(itemstring, listOfFruit, STRINGS.FRUIT.getString())){return STRINGS.FRUIT.getString();}
		if(isListvalue(itemstring, listOfSpezial, STRINGS.SPEZIAL.getString())){return STRINGS.SPEZIAL.getString();}
		return STRINGS.NONFOOD.getString();
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
     * search the material in the main config
     * 
     * @param itemstring - item(material) to search
     * @param listOfStrings - foodtype-list of material
     * @param type - foodtype to search
     * @return [true/false]
     */
    private boolean isListvalue(String itemstring, List<String> listOfStrings, String type){
		for (String string: listOfStrings)
		{
			if(string.equals(itemstring)){
	    		if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
	    			this.getLogger().log(Level.INFO, MESSAGE.FOUND_ITEM_VIA_CONFIGFILE.getMessage().replace("%item", itemstring).replace("%type", type));
	    		}				
				return true;
			};
		}
		if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
			this.getLogger().log(Level.INFO, MESSAGE.NOT_FOUND_ITEM_VIA_CONFIGFILE.getMessage().replace("%item", itemstring).replace("%type", type));
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
	 
	    	if(type != STRINGS.NONFOOD.getString()){
		    	playerDB.set(uuid + CONFIG.PLAYERDB_ISCAKE.getPath(), this.isCake(item));
	    		
	    		String eatentype = playerDB.getConfig().getString(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath());
	    		int eaten = playerDB.getConfig().getInt(uuid + CONFIG.PLAYERDB_EATENINROW.getPath());
		    	if(eaten >= getConfig().getInt(CONFIG.CONFIG_ITEMSINROW.getPath() + type) && type.equals(eatentype)){    		
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
