package com.github.fragsforfree.fooddiversity.enums;

import java.util.Arrays;
import java.util.List;

public enum CONFIG {
	PLUGIN_CONFIGVERSION("Plugin.Configversion", 1),
	PLUGIN_DEBUG("Plugin.Debug", false),
	PLUGIN_PATH_DATA("//Data"),
	CONFIG_ITEMSINROW_FRUIT("Config.ItemsInRow.Fruit", 3),
	CONFIG_ITEMSINROW_MEAT("Config.ItemsInRow.Meat", 3),
	CONFIG_ITEMSINROW_SPEZIAL("Config.ItemsInRow.Spezial", 3),
	CONFIG_ITEMSINROW("Config.ItemsInRow."),
	CONFIG_ITEMS_MEAT("Config.Items.Meat", Arrays.asList("COOKED_CHICKEN", "ROTTEN_FLESH", "RAW_CHICKEN", "COOKED_BEEF", "RAW_BEEF", "RAW_FISH", "COOKED_FISH", "PORK", "SPIDER_EYE")),
	CONFIG_ITEMS_FRUIT("Config.Items.Fruit", Arrays.asList("APPLE", "MELON", "CARROT", "GOLDEN_APPLE", "GOLDEN_CARROT")),
	CONFIG_ITEMS_SPEZIAL("Config.Items.Spezial", Arrays.asList("COOKIE", "CAKE", "BREAD", "POTATO", "BAKED_POTATO", "PUMPKIN_PIE", "MUSHROOM_SOUP", "POISONOUS_POTATO")),
	CONFIG_MESSAGE_DIVERSITY("Config.Message.Diversity", "You need more variety in the food, try something other than %foodtype"),
	PLAYERDB_EATENINROW(".eateninrow"),
	PLAYERDB_LASTEATENTYPE(".lasteatentype"),
	PLAYERDB_NAME(".name"),
	PLAYERDB_FILE("", "Player.yml"),
	PLAYERDB_TOBLOCK(".toblock", false),
	PLAYERDB_ISCONSUMING(".isconsuming", false),
	PLAYERDB_ISCAKE(".iscake", false);
	
	private String path;
	private int intdef;	
	private boolean booldef;
	private List<String> listOfStrings;
	private String stringdef;
	
	CONFIG(String path, int intdef){
		this.path = path;
		this.intdef = intdef;
	}
	
	CONFIG(String path, boolean booldef){
		this.path = path;
		this.booldef = booldef;
	}	
		
	CONFIG(String path, List<String> listOfStrings){
		this.path = path;
		this.listOfStrings = listOfStrings;
	}
	
	CONFIG(String path, String stringdef){
		this.path = path;
		this.stringdef = stringdef;
	}

	CONFIG(String path){
		this.path = path;
	}	
	
	public String getPath(){
		return this.path;
	}
	
	public int getInt(){
		return this.intdef;
	}
	
	public boolean getBoolean(){
		return this.booldef;
	}
	
	public List<String> getListOfStrings(){
		return this.listOfStrings;
	}
	
	public String getString(){
		return this.stringdef;
	}
	
}
