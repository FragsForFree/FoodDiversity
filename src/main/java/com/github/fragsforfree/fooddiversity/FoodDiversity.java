package com.github.fragsforfree.fooddiversity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	private FoodtypeHandler foodtypeHandler;
	private FDPlayerHandler fdplayerHandler;
	
	public boolean getConfigurationDebugmode(){
		return this.configManager.getDebug();
	}
	
	public boolean getConfigurationFeatureItemInRow(){
		return this.configManager.getFeatureItemInRow();
	}
	
	public boolean getConfigurationFeatureDiversity(){
		return this.configManager.getFeatureDiversity();
	}
	
	/**
	 * standard onEnable method
	 */
    public void onEnable(){ 
    	foodtypeHandler = new FoodtypeHandler(this);
    	fdplayerHandler = new FDPlayerHandler(this);
    	configManager = new ConfigurationManager(this, this.foodtypeHandler);
    	configManager.getFoodConfiguration();
    	initialisePlayerDB();
    	addMetrics();    	
    	
    	PluginManager pm = getServer().getPluginManager();
    	pm.registerEvents(new FoodLevelChange(this, this.fdplayerHandler),  this);
    	pm.registerEvents(new PlayerInteract(this, this.fdplayerHandler),  this);
    	pm.registerEvents(new PlayerItemConsume(this),  this);
    	pm.registerEvents(new PlayerJoin(this, this.playerDB, this.fdplayerHandler, this.foodtypeHandler), this);
    	pm.registerEvents(new PlayerQuit(this, this.playerDB, this.fdplayerHandler, this.foodtypeHandler), this);
        
        getCommand(this.getName().toLowerCase()).setExecutor(new FoodDiversityCommandExecuter(this));
        
        MessageHandler.sendConsole(this, Level.INFO, "Debugmodus: " + this.getConfigurationDebugmode());
        MessageHandler.sendConsole(this, Level.INFO, "Feature ItemInRow: " + this.getConfigurationFeatureItemInRow());
        MessageHandler.sendConsole(this, Level.INFO, "Feature Diversity: " + this.getConfigurationFeatureDiversity());
    }

    /**
     * integrate McStats
     */
    private void addMetrics(){
    	try {
    	    MetricsLite metrics = new MetricsLite(this);
    	    metrics.start();
    	} catch (IOException e) {
        	MessageHandler.sendConsoleDebug(this, Level.INFO, MESSAGE.METRICS_FAILED.getMessage(), this.getConfigurationDebugmode());
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
        	MessageHandler.sendConsoleDebug(this, Level.INFO, MESSAGE.ITEM_ISCAKE.getMessage(), this.getConfigurationDebugmode());
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

	    		String eatentype = this.fdplayerHandler.getValueLasteatentype(uuid);
	    		int eaten = this.fdplayerHandler.getValueEateninrow(uuid);		    	
		    	
	    		if (this.getConfigurationFeatureItemInRow()){
		    		this.fdplayerHandler.setValueIsCake(uuid, this.isCake(item));
	    			if(eaten >= this.foodtypeHandler.getmaxeateninrowfromfoodtype(type) && type.equals(eatentype)){    		
			    		MessageHandler.sendConsoleDebug(this, Level.INFO, MESSAGE.HAS_REACHED_LIMIT.getMessage().replace("%player",  name).replace("%value", String.valueOf(eaten)).replace("%type", eatentype), this.getConfigurationDebugmode());    		
			    		this.fdplayerHandler.setValueToblock(uuid, true);
			    	} else {
			    		this.fdplayerHandler.setValueToblock(uuid, false);
			    		MessageHandler.sendConsoleDebug(this, Level.INFO, MESSAGE.HAS_EATEN.getMessage().replace("%player",  name).replace("%value", String.valueOf(eaten)).replace("%type", type), this.getConfigurationDebugmode());		    		
			    	}
	    		}
		    		
	    		if (type.equals(eatentype)){
		    		eaten = eaten + 1;
		    	} else {
		    		eaten = 1;
		    	}
		    	this.fdplayerHandler.setValueEateninrow(uuid, eaten);
		    	this.fdplayerHandler.setValueLasteatentype(uuid, type);
		    		    		
	    	} else {
	    		this.fdplayerHandler.setValueToblock(uuid, false);
	    	}
	    	this.fdplayerHandler.setValueIsConsuming(uuid, true);
    	} else {
    		MessageHandler.sendConsoleDebug(this, Level.INFO, MESSAGE.IS_IMMUN.getMessage().replace("%player", name), this.getConfigurationDebugmode()); 		
    	}
    }       
    
    public void setConfigDebug(CommandSender sender, String value){
    	if ((value.toLowerCase().equalsIgnoreCase("true") || 
    			value.toLowerCase().equalsIgnoreCase("false"))){
    		this.configManager.setDebug(sender, Boolean.valueOf(value));
    	}
    	else
    	{
        	MessageHandler.sendMessage(this, sender, MESSAGE.EXPECT_BOOLEAN.getMessage(), true);
    	}
    }

    public void setConfigFeatureItemInRow(CommandSender sender, String value){
    	if ((value.toLowerCase().equalsIgnoreCase("true") || 
    			value.toLowerCase().equalsIgnoreCase("false"))){
    		this.configManager.setFeatureItemInRow(sender, Boolean.valueOf(value));
    	}
    	else
    	{
        	MessageHandler.sendMessage(this, sender, MESSAGE.EXPECT_BOOLEAN.getMessage(), true);
    	}
    }    
  
    public void setConfigFeatureDiversity(CommandSender sender, String value){
    	if ((value.toLowerCase().equalsIgnoreCase("true") || 
    			value.toLowerCase().equalsIgnoreCase("false"))){
    		this.configManager.setFeatureDiversity(sender, Boolean.valueOf(value));
    	}
    	else
    	{
        	MessageHandler.sendMessage(this, sender, MESSAGE.EXPECT_BOOLEAN.getMessage(), true);
    	}
    }     
    
    public void listFoodtypes(CommandSender sender){
    	String list = "";
    	list = this.foodtypeHandler.getStringOfFoodtypes();
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
    	List<String> checkedfood = new ArrayList<String>();
    	for (String _foodname : food){
    		if ((this.foodtypeHandler.getfoodtypename(_foodname) == null) && (this.checkValidMaterial(_foodname) == true)){
    			checkedfood.add(_foodname);
    			MessageHandler.sendMessage(this, sender, "foodtype '" + foodtype + "' was added!", false);
    		} else
    		{
    			MessageHandler.sendMessage(this, sender, "Food '" + _foodname + "' already in a foodtypegrou or not a valid food,  ignore entry", true);
    		}
    	} 	
    	
    	if (!checkedfood.isEmpty()){
    	
	    	if (this.configManager.addFoodtype(foodtype, checkedfood, itemInRow)){
	    		this.foodtypeHandler.addFoodtype(foodtype, checkedfood, itemInRow);
	    	}
	    	else
	    	{
		    	MessageHandler.sendMessage(this, sender, "failed to add foodtype (config)", true);    		
	    	}
    	} else
    	{
    		MessageHandler.sendMessage(this, sender, "There is no food left, ignore foodtypeentry", true);
    	}
    }

	public void removeFoodtype(CommandSender sender, String foodtype) {
		if (this.configManager.removeFoodtype(foodtype)){
			this.foodtypeHandler.removeFoodtype(foodtype);
			this.fdplayerHandler.removeDiversityEntry(foodtype);
			MessageHandler.sendMessage(this, sender, "foodtype '" + foodtype + "' was removed!", false);
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
				MessageHandler.sendMessage(this, sender, "food '" + food + "' was added to foodtype '" + foodtype + "'", false);
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
				MessageHandler.sendMessage(this, sender, "food '" + food + "' was removed from foodtype '" + foodtype + "'", false);
				
				if(this.foodtypeHandler.getListFood(foodtype).equalsIgnoreCase("")){
					this.removeFoodtype(sender, foodtype);
				}
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
				MessageHandler.sendMessage(this, sender, "iteminrow value for foodtype '" + foodtype + "' set to '" + iteminrow + "'", false);
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
    
}
