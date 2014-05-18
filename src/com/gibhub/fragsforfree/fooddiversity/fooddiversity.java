package com.gibhub.fragsforfree.fooddiversity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import com.gibhub.fragsforfree.fooddiversity.mcstat.MetricsLite;

public class fooddiversity extends JavaPlugin implements Listener {

	private PlayerDB playerDB;
	public World world;
	public Location loc;
	
	/**
	 * standard onEnable method
	 */
    public void onEnable(){ 
    	initialiseConfig();
    	checkConfigItems();
    	initialisePlayerDB();
    	addMetrics();
    	
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    /**
     * integrade McStats
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
     * initialises the main config object, sets with default values and check
     * if there is a new version of the configfile 
     */
    private void initialiseConfig(){
    	FileConfiguration config = getConfig();    	

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
    	saveConfig();
    	
    	if(config.getInt(CONFIG.PLUGIN_CONFIGVERSION.getPath()) != CONFIG.PLUGIN_CONFIGVERSION.getInt()){
    		this.getLogger().log(Level.WARNING, MESSAGE.INVALID_CONFIG_VERSION.getMessage());
    	}    	
    }
  
    /**
     * check the materiallist in the main config file. 
     * throw a warning, if a entry is not a valid material
     */
    private void checkConfigItems(){
		List<String> listOfMeat = getConfig().getStringList(CONFIG.CONFIG_ITEMS_MEAT.getPath());
		List<String> listOfFruit = getConfig().getStringList(CONFIG.CONFIG_ITEMS_FRUIT.getPath());
		List<String> listOfSpezial = getConfig().getStringList(CONFIG.CONFIG_ITEMS_SPEZIAL.getPath());		
		List<String> listOfStrings = new ArrayList<String>();
		
		listOfStrings = this.checkItemEntries(listOfMeat, listOfStrings);
		listOfStrings = this.checkItemEntries(listOfFruit, listOfStrings);
		listOfStrings = this.checkItemEntries(listOfSpezial, listOfStrings);
		
		for (String string: listOfStrings)
		{
			try{
				Material.valueOf(string);
				if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
					this.getLogger().log(Level.INFO, MESSAGE.CHECK_MAT.getMessage().replace("%material", string));
				}				
			} catch (Exception ex) {
				this.getLogger().log(Level.WARNING, MESSAGE.INVALID_CONFIG_MAT.getMessage().replace("%material", string));
			}							
		}				
    }
    
    /**
     * put the content of listOfTypes in the listOfStrings Lists
     * checks if the material is allready in the listOfStrings
     * throw a warning, if there is a material count more than once
     * 
     * @param listOfTypes - foodtype-list of material
     * @param listOfStrings - whole list of material 
     * @return listOfStrings - used later on method checkConfigItems()
     */
    private List<String> checkItemEntries(List<String> listOfTypes, List<String> listOfStrings){
		for (String string: listOfTypes){			
			if(listOfStrings.contains(string) && (!listOfStrings.isEmpty())){
				this.getLogger().log(Level.WARNING, MESSAGE.INVALID_CONFIG_COUNT.getMessage().replace("%material", string));				
			} else {
			listOfStrings.add(string);
			}
		}
		return listOfStrings;
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
     * standard onCommand method
     * filled with spezial plugin commands
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
    	if ((sender instanceof Player && sender.hasPermission(STRINGS.PERM_ADMIN.getString())) || (sender instanceof ConsoleCommandSender)){    		
	    	if (command.getName().toLowerCase().equalsIgnoreCase(STRINGS.CMD.getString())){
	    		switch(args.length){
	    		case 1:
		    		switch(args[0].toLowerCase()){
		    		case "help":
		    			sender.sendMessage(ChatColor.GREEN + HELP.TITLE.getTip());
		    			sender.sendMessage(ChatColor.GOLD + HELP.LINE_01.getCommand() + ChatColor.WHITE + HELP.LINE_01.getTip());
		    			sender.sendMessage(ChatColor.GOLD + HELP.LINE_02.getCommand() + ChatColor.WHITE + HELP.LINE_02.getTip());
		    			sender.sendMessage(ChatColor.GOLD + HELP.LINE_03.getCommand() + ChatColor.WHITE + HELP.LINE_03.getTip());
		    			sender.sendMessage(ChatColor.GOLD + HELP.LINE_04.getCommand() + ChatColor.WHITE + HELP.LINE_04.getTip());
		    			sender.sendMessage(ChatColor.GOLD + HELP.LINE_05.getCommand() + ChatColor.WHITE + HELP.LINE_05.getTip());		    			
		    			break;
		    		default:
		    			return false;
		    		}
		    		break;
	    		case 3:
	    			switch(args[0].toLowerCase()){
		    		case "set":		
		    			switch(args[1].toLowerCase()){
		    			case "debug":
	    					if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")){
	    						getConfig().set(CONFIG.PLUGIN_DEBUG.getPath(), Boolean.valueOf(args[2]));
	    						sender.sendMessage(ChatColor.GREEN + MESSAGE.CMD_DEBUG_CHANGE.getMessage().replace("%args", args[2]));
	    						saveConfig();
	    					} else {
	    						sender.sendMessage(ChatColor.RED + MESSAGE.EXPECT_BOOLEAN.getMessage());
	    					}
	    					break;
		    			case "fruit":
	    					if (isInteger(args[2])){
	    						getConfig().set(CONFIG.CONFIG_ITEMSINROW_FRUIT.getPath(), Integer.parseInt(args[2]));
	    						sender.sendMessage(ChatColor.GREEN + MESSAGE.CMD_FRUIT_CHANGE.getMessage().replace("%args", args[2]));
	    						saveConfig();
	    					} else {
	    						sender.sendMessage(ChatColor.RED + MESSAGE.EXPECT_NUMERIC.getMessage());
	    					}
	    					break;
		    			case "meat":
	    					if (isInteger(args[2])){
	    						getConfig().set(CONFIG.CONFIG_ITEMSINROW_MEAT.getPath(), Integer.parseInt(args[2]));
	    						sender.sendMessage(ChatColor.GREEN + MESSAGE.CMD_MEAT_CHANGE.getMessage().replace("%args", args[2]));
	    						saveConfig();
	    					} else {
	    						sender.sendMessage(ChatColor.RED + MESSAGE.EXPECT_NUMERIC.getMessage());
	    					}
	    					break;
		    			case "spezial":
	    					if (isInteger(args[2])){
	    						getConfig().set(CONFIG.CONFIG_ITEMSINROW_SPEZIAL.getPath(), Integer.parseInt(args[2]));
	    						sender.sendMessage(ChatColor.GREEN + MESSAGE.CMD_SPEZIAL_CHANGE.getMessage().replace("%args", args[2]));
	    						saveConfig();
	    					} else {
	    						sender.sendMessage(ChatColor.RED + MESSAGE.EXPECT_NUMERIC.getMessage());
	    					}
	    					break;
		    			default:
		    				return false;
		    			}
		    			break;
		    		default:
		    			return false;
		    		}
	    			break;
		    	default:
		    		return false;    			
	    		}	
	    	}
    	}
    	
    	
    	return true;
    }
    
    /**
     * checks the value on interger
     * 
     * @param string - string to check
     * @return [true/false]
     */
    private boolean isInteger(String string) {
		try{
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
    
    /**
     * PlayerItemConsumeEvent from the bukkit api
     * need to identify a food consume
     * 
     * @param event
     */
    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
    	// Issue https://bukkit.atlassian.net/browse/BUKKIT-4169
    	// so i had to do this code of awesome bullshit. Not nice, but it works
    	this.checkFoodDiversity(event.getPlayer(), event.getItem());
    }
    
    /**
     * PlayerInteractEvent from the bukkit api
     * need to handle the consume of a cake
     * if the consume is needed to block, the damaged clicked block will be restored
     * 
     * @param event
     */
	@SuppressWarnings("deprecation")
	@EventHandler
    public void onInteract(PlayerInteractEvent event){
    	if((event.getPlayer().getFoodLevel() < 20) && (event.getClickedBlock() != null)) {
	    	if(Material.CAKE_BLOCK == event.getClickedBlock().getType()){
				if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
					this.getLogger().log(Level.INFO, MESSAGE.CLICKED_CAKE.getMessage());
				}    	
				
				ItemStack item = new ItemStack(Material.CAKE);
				checkFoodDiversity(event.getPlayer(), item);
				
				String uuid = event.getPlayer().getUniqueId().toString();				
	        	if(playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath()) && (playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath()))){
	        		if (Action.RIGHT_CLICK_BLOCK == event.getAction()){
		        		if (event.getClickedBlock().getData() > 0) {
		        			event.getClickedBlock().setData((byte) (event.getClickedBlock().getData() -1)); //lose at least one damage, if the cake is full
		        		} else {
		        			this.loc = event.getClickedBlock().getLocation();
		        			this.world = event.getClickedBlock().getWorld();
		        			
		        			final Plugin plugin = this;
		        			this.getServer().getScheduler().scheduleSyncDelayedTask(plugin,  new Runnable() {
		        				
		        				public void run(){	        						        					
		        					world.getBlockAt(loc).setData((byte) (world.getBlockAt(loc).getData() -1));;
		        					if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
		        						plugin.getServer().getLogger().log(Level.INFO, MESSAGE.SCHEDULER_RUN.getMessage());
		        					}
		        				}	        				
		        				
		        			}, 10L);
	
		        		}	        			
	        		}
	        		
	        	}
	    	}	    		
    	}	
    }
	
	/**
	 * main check for the blocking of consume
	 * sets the toblock and isconsuming values in the playerDB
	 * 
	 * @param player - player to identify in playerDB
	 * @param item - consumed item
	 */
    private void checkFoodDiversity(Player player, ItemStack item){
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
    
    /**
     * workaround method: https://bukkit.atlassian.net/browse/BUKKIT-4169
     * FoodLevelChangeEvent from bukkit api
     * roleback the eventactions if needed. 
     * 
     * @param event
     */
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {    	    	   		
    	if (event.getEntityType() == EntityType.PLAYER) {
    		    		
    		Player player = (Player) event.getEntity();	        		        	
        	String uuid = player.getUniqueId().toString();
        	        	
        	if(playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath()) && (playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath()))){
	    		playerDB.set(uuid + CONFIG.PLAYERDB_TOBLOCK.getPath(), false);	    		
	    		
	    		if(!playerDB.getConfig().getBoolean(uuid + CONFIG.PLAYERDB_ISCAKE.getPath())){
		    		ItemStack item = player.getItemInHand();	    			    		
	    	    	if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
		        		this.getLogger().log(Level.INFO, MESSAGE.ITEM_AMOUNT.getMessage().replace("%item", item.getType().toString()).replace("%value", String.valueOf(item.getAmount())));
		        	}    	
		        	item.setAmount(item.getAmount() + 1);	        	
		        	if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
		        		this.getLogger().log(Level.INFO, MESSAGE.ITEM_AMOUNT_NEW.getMessage().replace("%item",  item.getType().toString()).replace("%value", String.valueOf(item.getAmount())));
		        	} 
		        	player.setItemInHand(item);    	
		    		player.updateInventory();	    			
	    		}	    			    		
	        	
	        	player.sendMessage(ChatColor.RED + (getConfig().getString(CONFIG.CONFIG_MESSAGE_DIVERSITY.getPath())).replace("%foodtype", playerDB.getConfig().getString(uuid + CONFIG.PLAYERDB_LASTEATENTYPE.getPath())));	        	
	    		event.setCancelled(true); 	    			    		
	    		if (getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
	    			this.getLogger().log(Level.INFO, MESSAGE.BLOCK_INCREASE.getMessage().replace("%player", event.getEntity().getName()));
	    		}	        		
        	}
        	playerDB.set(uuid + CONFIG.PLAYERDB_ISCONSUMING.getPath(), false);
    	}

    	
    }
        
    
}
