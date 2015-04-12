package com.github.fragsforfree.fooddiversity.config;

import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.github.fragsforfree.fooddiversity.FoodDiversity;
import com.github.fragsforfree.fooddiversity.enums.CONFIG;
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;
import com.github.fragsforfree.fooddiversity.messages.MessageHandler;

public class ConfigurationManager {

	private final Plugin PLUGIN;
	private FoodDiversity cFooddiversity;
	
	public ConfigurationManager(Plugin instance, FoodDiversity _fooddiversity) {
		this.PLUGIN = instance;
		this.cFooddiversity = _fooddiversity;
		this.initialiseConfig();
	}
	
    /**
     * initialising the main config object, sets with default values and check
     * if there is a new version of the configfile 
     */
    public void initialiseConfig(){
    	FileConfiguration config = PLUGIN.getConfig(); 
    	config.addDefault(CONFIG.PLUGIN_CONFIGVERSION.getPath(), CONFIG.PLUGIN_CONFIGVERSION.getInt());
    	config.addDefault(CONFIG.PLUGIN_DEBUG.getPath(), CONFIG.PLUGIN_DEBUG.getBoolean());   	
    	if (PLUGIN.getConfig().getKeys(true).isEmpty()) {
        	config.addDefault(CONFIG.CONFIG_ITEMSINROW_FRUIT.getPath(), CONFIG.CONFIG_ITEMSINROW_FRUIT.getInt());
        	config.addDefault(CONFIG.CONFIG_ITEMSINROW_MEAT.getPath(), CONFIG.CONFIG_ITEMSINROW_MEAT.getInt());
        	config.addDefault(CONFIG.CONFIG_ITEMSINROW_SPEZIAL.getPath(), CONFIG.CONFIG_ITEMSINROW_SPEZIAL.getInt());    	
        	config.addDefault(CONFIG.CONFIG_ITEMS_MEAT.getPath(), CONFIG.CONFIG_ITEMS_MEAT.getListOfStrings());
        	config.addDefault(CONFIG.CONFIG_ITEMS_FRUIT.getPath(), CONFIG.CONFIG_ITEMS_FRUIT.getListOfStrings());
        	config.addDefault(CONFIG.CONFIG_ITEMS_SPEZIAL.getPath(), CONFIG.CONFIG_ITEMS_SPEZIAL.getListOfStrings());    		
    	}
    	config.addDefault(CONFIG.CONFIG_MESSAGE_DIVERSITY.getPath(), CONFIG.CONFIG_MESSAGE_DIVERSITY.getString());
    	config.addDefault(CONFIG.CONFIG_FEATURE_ITEMINROW.getPath(), CONFIG.CONFIG_FEATURE_ITEMINROW.getBoolean());
    	config.addDefault(CONFIG.CONFIG_FEATURE_DIVERSITY.getPath(), CONFIG.CONFIG_FEATURE_DIVERSITY.getBoolean());
    	
    	config.options().copyDefaults(true); 
    	PLUGIN.saveConfig();
    	
    	if(config.getInt(CONFIG.PLUGIN_CONFIGVERSION.getPath()) != CONFIG.PLUGIN_CONFIGVERSION.getInt()){    		
    		if(config.getInt(CONFIG.PLUGIN_CONFIGVERSION.getPath()) == 1 && CONFIG.PLUGIN_CONFIGVERSION.getInt() == 2){
    			config.set(CONFIG.PLUGIN_CONFIGVERSION.getPath(), 2);
    			PLUGIN.saveConfig();
    		}
    		else
    		{
    			MessageHandler.sendConsole(PLUGIN, Level.WARNING, MESSAGE.INVALID_CONFIG_VERSION.getMessage());
    		}
    	}    	
    }
    
    /**
     *  get the foodtype-configuration and give each one to the foodtypehandler
     */
    public void getFoodConfiguration() {
    	int itemInRow;
    	Set<String> keys = PLUGIN.getConfig().getKeys(true);
    	for (String key: keys){
    		    		
    		if (key.startsWith("Config.Items.")) {    		    			
    			String foodtype = key.replace("Config.Items.", "");    			
    			try {
    				itemInRow = PLUGIN.getConfig().getInt("Config.ItemsInRow." + foodtype);
    			}
    			catch (Exception  ex)
    			{
    				MessageHandler.sendConsole(PLUGIN, Level.WARNING, "configuration failure under 'Config.ItemsInRow." + foodtype +"', use defaultvalue '3'");
    				itemInRow = 3;
    			}
    			List<String> listOfFood = PLUGIN.getConfig().getStringList(key);
    			this.cFooddiversity.foodtypeHandler.addFoodtype(foodtype, listOfFood, itemInRow);    			
    		}
    	} 	
    }
    
    public boolean getDebug(){
    	return this.PLUGIN.getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath());
    }
    
    public void setDebug(CommandSender sender, boolean value){
    	this.PLUGIN.getConfig().set(CONFIG.PLUGIN_DEBUG.getPath(), value);    	
    	this.PLUGIN.saveConfig();
    	MessageHandler.sendMessage(PLUGIN, sender, MESSAGE.CMD_DEBUG_CHANGE.getMessage().replace("%args", String.valueOf(value)), false);
    }
	
    public boolean addFoodtype(String foodtype, List<String> food, int itemInRow){
    	boolean result = false;
    	if(!this.PLUGIN.getConfig().contains("Config.Items." + foodtype) && 
    			!this.PLUGIN.getConfig().contains("Config.ItemsInRow." + foodtype)){
    		this.PLUGIN.getConfig().createSection("Config.Items." + foodtype);
    		this.PLUGIN.getConfig().set("Config.Items." + foodtype, food);
    		this.PLUGIN.getConfig().createSection("Config.ItemsInRow." + foodtype);    		
    		this.PLUGIN.getConfig().set("Config.ItemsInRow." + foodtype, itemInRow);
    		this.PLUGIN.saveConfig();
    		result = true;
    	}    	
    	return result;
    }

	public boolean removeFoodtype(String foodtype) {
		boolean result = false;
		if(!this.PLUGIN.getConfig().contains("Config.Items." + foodtype) && 
    			!this.PLUGIN.getConfig().contains("Config.ItemsInRow." + foodtype)){
			this.PLUGIN.getConfig().set("Config.Items." + foodtype, null);
			this.PLUGIN.getConfig().set("Config.ItemsInRow." + foodtype, null); 
		}
		return result;
	}

	public boolean addFood(String food, String foodtype) {
		boolean result = false;
		List<String> foodData;
		if(!this.PLUGIN.getConfig().contains("Config.Items." + foodtype) && 
    			!this.PLUGIN.getConfig().contains("Config.ItemsInRow." + foodtype)){
			foodData = (List<String>) this.PLUGIN.getConfig().getList("Config.Items." + foodtype);
			foodData.add(food);
			this.PLUGIN.getConfig().set("Config.Items." + foodtype, foodData);
		}
		return result;
	}

	public boolean removeFood(String food, String foodtype) {
		boolean result = false;
		List<String> foodData;
		if(!this.PLUGIN.getConfig().contains("Config.Items." + foodtype) && 
    			!this.PLUGIN.getConfig().contains("Config.ItemsInRow." + foodtype)){
			foodData = (List<String>) this.PLUGIN.getConfig().getList("Config.Items." + foodtype);
			foodData.remove(foodData.indexOf(food));
			this.PLUGIN.getConfig().set("Config.Items." + foodtype, foodData);
		}		
		return result;
	}

	public boolean setItemInRow(String foodtype, Integer iteminrow) {
		boolean result = false;
		if(!this.PLUGIN.getConfig().contains("Config.Items." + foodtype) && 
    			!this.PLUGIN.getConfig().contains("Config.ItemsInRow." + foodtype))
			this.PLUGIN.getConfig().set("Config.ItemsInRow." + foodtype, iteminrow);
		return result;
	}
    
}
