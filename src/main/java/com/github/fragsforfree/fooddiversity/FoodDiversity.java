package com.github.fragsforfree.fooddiversity;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.fragsforfree.fooddiversity.command.FoodDiversityCommandExecuter;
import com.github.fragsforfree.fooddiversity.config.ConfigurationManager;
import com.github.fragsforfree.fooddiversity.enums.CONFIG;
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;
import com.github.fragsforfree.fooddiversity.events.FoodLevelChange;
import com.github.fragsforfree.fooddiversity.events.PlayerInteract;
import com.github.fragsforfree.fooddiversity.events.PlayerItemConsume;
import com.github.fragsforfree.fooddiversity.events.PlayerJoin;
import com.github.fragsforfree.fooddiversity.events.PlayerQuit;
import com.github.fragsforfree.fooddiversity.food.FoodtypeHandler;
import com.github.fragsforfree.fooddiversity.mcstat.MetricsLite;
import com.github.fragsforfree.fooddiversity.messages.MessageHandler;
import com.github.fragsforfree.fooddiversity.permission.EnumPermissions;
import com.github.fragsforfree.fooddiversity.player.FDPlayerHandler;

public class FoodDiversity extends JavaPlugin implements Listener {

	private PlayerDB playerDB;
	private ConfigurationManager configManager;
	public FoodtypeHandler foodtypeHandler;
	private FDPlayerHandler fdplayerHandler;
	private boolean debug;
	
	private void setDebug(boolean value){
		this.debug = value;
	}
	
	public boolean getDebug(){
		return this.debug;
	}
	
	/**
	 * standard onEnable method
	 */
    public void onEnable(){ 
    	foodtypeHandler = new FoodtypeHandler(this);
    	fdplayerHandler = new FDPlayerHandler(this);
    	configManager = new ConfigurationManager(this, this);
    	this.getConfigDebug();
    	configManager.getFoodConfiguration();
    	initialisePlayerDB();
    	addMetrics();    	
    	
    	PluginManager pm = getServer().getPluginManager();
    	pm.registerEvents(new FoodLevelChange(this, this.fdplayerHandler),  this);
    	pm.registerEvents(new PlayerInteract(this, this.fdplayerHandler),  this);
    	pm.registerEvents(new PlayerItemConsume(this),  this);
    	pm.registerEvents(new PlayerJoin(this), this);
    	pm.registerEvents(new PlayerQuit(this), this);
        
        getCommand(this.getName().toLowerCase()).setExecutor(new FoodDiversityCommandExecuter(this));
    }

    /**
     * integrate McStats
     */
    private void addMetrics(){
    	try {
    	    MetricsLite metrics = new MetricsLite(this);
    	    metrics.start();
    	} catch (IOException e) {
        	MessageHandler.sendConsoleDebug(this, Level.INFO, MESSAGE.METRICS_FAILED.getMessage(), this.getDebug());
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
        	MessageHandler.sendConsoleDebug(this, Level.INFO, MESSAGE.ITEM_ISCAKE.getMessage(), this.getDebug());
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
    	if (!player.hasPermission(EnumPermissions.FoodDiversityImmun.getString())){    		    	
    		type = this.getFoodtype(item);	    	
	 
	    	if(type != null){
		    	this.fdplayerHandler.setValueIsCake(uuid, this.isCake(item));
	    		/**playerDB.set(uuid + CONFIG.PLAYERDB_ISCAKE.getPath(), this.isCake(item));**/

	    		String eatentype = this.fdplayerHandler.getValueLasteatentype(uuid);
	    		int eaten = this.fdplayerHandler.getValueEateninrow(uuid);		    	
	    		/**String eatentype = playerDB.getConfig().getString(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath());
	    		int eaten = playerDB.getConfig().getInt(uuid + CONFIG.PLAYERDB_EATENINROW.getPath());**/
		    	if(eaten >= this.foodtypeHandler.getmaxeateninrowfromfoodtype(type) && type.equals(eatentype)){    		
		    		MessageHandler.sendConsoleDebug(this, Level.INFO, MESSAGE.HAS_REACHED_LIMIT.getMessage().replace("%player",  name).replace("%value", String.valueOf(eaten)).replace("%type", eatentype), this.getDebug());    		
		    		this.fdplayerHandler.setValueToblock(uuid, true);
		    		/**playerDB.set(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath(), true);**/
		    	} else {
		    		if (type.equals(eatentype)){
		    			eaten = eaten + 1;		    			
		    		} else {
		    			eaten = 1;
		    		}
		    		this.fdplayerHandler.setValueEateninrow(uuid, eaten);
		    		this.fdplayerHandler.setValueLasteatentype(uuid, type);
		    		this.fdplayerHandler.setValueToblock(uuid, false);
		    		/**playerDB.set(uuid + CONFIG.PLAYERDB_EATENINROW.getPath(), eaten);
		    		playerDB.set(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath(), type);
		    		playerDB.set(uuid + CONFIG.PLAYERDB_NAME.getPath(), name);
		    		playerDB.set(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath(), false);		    		
		    		playerDB.save();**/
		    		MessageHandler.sendConsoleDebug(this, Level.INFO, MESSAGE.HAS_EATEN.getMessage().replace("%player",  name).replace("%value", String.valueOf(eaten)).replace("%type", type), this.getDebug());		    		
		    	}     		
	    	} else {
	    		this.fdplayerHandler.setValueToblock(uuid, false);
	    		/**playerDB.set(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath(), false);**/
	    	}
	    	this.fdplayerHandler.setValueIsConsuming(uuid, true);
	    	/**playerDB.set(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath(), true);**/
    	} else {
    		MessageHandler.sendConsoleDebug(this, Level.INFO, MESSAGE.IS_IMMUN.getMessage().replace("%player", name), this.getDebug()); 		
    	}
    }   
    
    public void getConfigDebug(){
    	this.setDebug(this.configManager.getDebug());
    }
    
    public void setConfigDebug(CommandSender sender, String value){
    	if ((value.toLowerCase().equalsIgnoreCase("true") || 
    			value.toLowerCase().equalsIgnoreCase("false"))){
    		this.configManager.setDebug(sender, Boolean.valueOf(value));
    		this.setDebug(Boolean.valueOf(value));
    	}
    	else
    	{
        	MessageHandler.sendMessage(this, sender, MESSAGE.EXPECT_BOOLEAN.getMessage(), true);
    	}
    }
    
    public void listFoodtypes(CommandSender sender){
    	String list = "";
    	list = this.foodtypeHandler.getListFoodtypes();
    	MessageHandler.sendMessage(this, sender, "loaded foodtypes: " + list, false); 	
    }
    
    public void listFood(CommandSender sender, String foodtype){
    	String list = "";
    	if(this.foodtypeHandler.getfoodtype(foodtype) != null){
	    	list = this.foodtypeHandler.getListFood(foodtype);
	    	MessageHandler.sendMessage(this, sender, "food of " + foodtype.toUpperCase() + ": " + list, false);    		    		
    	}
    	else
    	{
	    	MessageHandler.sendMessage(this, sender, "unknown foodtype: " + foodtype, true); 		
    	}
    }
    
    public void addFoodtype(CommandSender sender, String foodtype, List<String> food, int itemInRow){
    	if (this.configManager.addFoodtype(foodtype, food, itemInRow)){
    		this.foodtypeHandler.addFoodtype(foodtype, food, itemInRow);
    	}
    	else
    	{
	    	MessageHandler.sendMessage(this, sender, "failed to add foodtype (config)", true);    		
    	}
    }

	public void removeFoodtype(CommandSender sender, String foodtype) {
		if (this.configManager.removeFoodtype(foodtype)){
			this.foodtypeHandler.removeFoodtype(foodtype);
		}
		else
		{
			MessageHandler.sendMessage(this, sender, "failed to remove foodtype (config)", true);
		}
		
	}

	public void addFood(CommandSender sender, String food, String foodtype) {		
		if (this.checkValidMaterial(food) == true){
			if (this.configManager.addFood(food, foodtype)){
				this.foodtypeHandler.addFood(Material.getMaterial(food), foodtype);
			}
			else
			{
				MessageHandler.sendMessage(this, sender, "failed to add food (config)", true);
			}			
		}
		else
		{
			MessageHandler.sendMessage(this, sender, "invalid food", true);
		}		
	}
	
	public boolean checkValidMaterial(String name){
		Material material = Material.getMaterial(name);
		if (material != null) { return true; }
		return false;
	}

	public void removeFood(CommandSender sender, String food) {
		String foodtype;
		foodtype = this.foodtypeHandler.getfoodtypename(food);
		if (this.checkValidMaterial(food) == true && foodtype != null){
			if (this.configManager.removeFood(food, foodtype)){
				this.foodtypeHandler.removeFood(Material.getMaterial(food), foodtype);
			}
		}
		else
		{
			MessageHandler.sendMessage(this, sender, "invalid food", true);
		}
		
	}

	public void setItemInRow(CommandSender sender, String foodtype, String iteminrow) {
		if (this.isNumeric(iteminrow)){
			if (this.configManager.setItemInRow(foodtype, Integer.valueOf(iteminrow))){
				this.foodtypeHandler.setItemInRow(foodtype, Integer.valueOf(iteminrow));
			}
			else
			{
				MessageHandler.sendMessage(this, sender, "failed to set iteminrow (config)", true);
			}			
		}
		else
		{
			MessageHandler.sendMessage(this, sender, "invalid iteminrow value", true);
		}
		
	}
	
    private boolean isNumeric(String str)  
    {  
      try  
      {  
        Double.parseDouble(str);  
      }  
      catch(NumberFormatException nfe)  
      {  
        return false;  
      }  
      return true;  
    }
    
    public void PlayerJoin(String uuid, String name){    	
    	this.fdplayerHandler.addFDPlayer(uuid, name);
    	if (this.playerDB.getConfig().contains(uuid)){
    		this.fdplayerHandler.setValueLasteatentype(uuid, this.playerDB.getConfig().getString(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath()));
    		this.fdplayerHandler.setValueEateninrow(uuid, this.playerDB.getConfig().getInt(uuid + CONFIG.PLAYERDB_EATENINROW.getPath()));
    		this.fdplayerHandler.setValueToblock(uuid, this.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath()));
    		this.fdplayerHandler.setValueIsConsuming(uuid, this.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath()));
    		this.fdplayerHandler.setValueIsCake(uuid, this.playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCAKE.getPath()));
    	}
    	else
    	{
    		this.fdplayerHandler.setValueLasteatentype(uuid, "nothing");
    		this.fdplayerHandler.setValueEateninrow(uuid, 0);
    		this.fdplayerHandler.setValueToblock(uuid, false);
    		this.fdplayerHandler.setValueIsConsuming(uuid, false);
    		this.fdplayerHandler.setValueIsCake(uuid, false);    		
    	}
    	
    }
    
    public void PlayerQuid(String uuid){
    	this.playerDB.set(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath(), this.fdplayerHandler.getValueLasteatentype(uuid));
    	this.playerDB.set(uuid + CONFIG.PLAYERDB_EATENINROW.getPath(), this.fdplayerHandler.getValueEateninrow(uuid));
    	this.playerDB.set(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath(), this.fdplayerHandler.getValueToBlock(uuid));
    	this.playerDB.set(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath(), this.fdplayerHandler.getValueIsConsuming(uuid));
    	this.playerDB.set(uuid + CONFIG.PLAYERDB_ISCAKE.getPath(), this.fdplayerHandler.getValueIsCake(uuid));
    	this.playerDB.save();
    	this.fdplayerHandler.removeFDPlayer(uuid);    	
    }
    
}
