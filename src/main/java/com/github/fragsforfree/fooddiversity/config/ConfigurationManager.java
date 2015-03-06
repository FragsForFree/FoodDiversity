package com.github.fragsforfree.fooddiversity.config;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import com.github.fragsforfree.fooddiversity.fooddiversity;
import com.github.fragsforfree.fooddiversity.enums.CONFIG;
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;

public class ConfigurationManager {

	private final Plugin PLUGIN;
	
	public ConfigurationManager(fooddiversity instance) {
		this.PLUGIN = instance;
	}
	
    /**
     * initialising the main config object, sets with default values and check
     * if there is a new version of the configfile 
     */
    public void initialiseConfig(){
    	FileConfiguration config = PLUGIN.getConfig();    	

    	config.addDefault(CONFIG.PLUGIN_CONFIGVERSION.getPath(), CONFIG.PLUGIN_CONFIGVERSION.getInt());
    	config.addDefault(CONFIG.PLUGIN_DEBUG.getPath(), CONFIG.PLUGIN_DEBUG.getBoolean());
    	config.addDefault(CONFIG.CONFIG_ITEMSINROW_FRUIT.getPath(), CONFIG.CONFIG_ITEMSINROW_FRUIT.getInt());
    	config.addDefault(CONFIG.CONFIG_ITEMSINROW_MEAT.getPath(), CONFIG.CONFIG_ITEMSINROW_MEAT.getInt());
    	config.addDefault(CONFIG.CONFIG_ITEMSINROW_SPEZIAL.getPath(), CONFIG.CONFIG_ITEMSINROW_SPEZIAL.getInt());    	
    	config.addDefault(CONFIG.CONFIG_ITEMS_MEAT.getPath(), CONFIG.CONFIG_ITEMS_MEAT.getListOfStrings());
    	config.addDefault(CONFIG.CONFIG_ITEMS_FRUIT.getPath(), CONFIG.CONFIG_ITEMS_FRUIT.getListOfStrings());
    	config.addDefault(CONFIG.CONFIG_ITEMS_SPEZIAL.getPath(), CONFIG.CONFIG_ITEMS_SPEZIAL.getListOfStrings());
    	config.addDefault(CONFIG.CONFIG_MESSAGE_DIVERSITY.getPath(), CONFIG.CONFIG_MESSAGE_DIVERSITY.getString());
    	
    	config.options().copyDefaults(true); 
    	PLUGIN.saveConfig();
    	
    	if(config.getInt(CONFIG.PLUGIN_CONFIGVERSION.getPath()) != CONFIG.PLUGIN_CONFIGVERSION.getInt()){
    		PLUGIN.getLogger().log(Level.WARNING, MESSAGE.INVALID_CONFIG_VERSION.getMessage());
    	}    	
    }
    
    /**
     * check the materiallist in the main config file. 
     * throw a warning, if a entry is not a valid material
     */
    public void checkConfigItems(){		    	
    	List<String> listOfMeat = PLUGIN.getConfig().getStringList(CONFIG.CONFIG_ITEMS_MEAT.getPath());
		List<String> listOfFruit = PLUGIN.getConfig().getStringList(CONFIG.CONFIG_ITEMS_FRUIT.getPath());
		List<String> listOfSpezial = PLUGIN.getConfig().getStringList(CONFIG.CONFIG_ITEMS_SPEZIAL.getPath());		
		List<String> listOfStrings = new ArrayList<String>();
		
		listOfStrings = this.checkItemEntries(listOfMeat, listOfStrings);
		listOfStrings = this.checkItemEntries(listOfFruit, listOfStrings);
		listOfStrings = this.checkItemEntries(listOfSpezial, listOfStrings);
		
		for (String string: listOfStrings)
		{
			try{
				Material.valueOf(string);
				if (PLUGIN.getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
					PLUGIN.getLogger().log(Level.INFO, MESSAGE.CHECK_MAT.getMessage().replace("%material", string));
				}				
			} catch (Exception ex) {
				PLUGIN.getLogger().log(Level.WARNING, MESSAGE.INVALID_CONFIG_MAT.getMessage().replace("%material", string));
			}							
		}				
    }  
    
    /**
     * put the content of listOfTypes in the listOfStrings Lists
     * checks if the material is already in the listOfStrings
     * throw a warning, if there is a material count more than once
     * 
     * @param listOfTypes - foodtype-list of material
     * @param listOfStrings - whole list of material 
     * @return listOfStrings - used later on method checkConfigItems()
     */
    private List<String> checkItemEntries(List<String> listOfTypes, List<String> listOfStrings){
		for (String string: listOfTypes){			
			if(listOfStrings.contains(string) && (!listOfStrings.isEmpty())){
				PLUGIN.getLogger().log(Level.WARNING, MESSAGE.INVALID_CONFIG_COUNT.getMessage().replace("%material", string));				
			} else {
			listOfStrings.add(string);
			}
		}
		return listOfStrings;
    }    
	
}
