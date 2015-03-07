package com.github.fragsforfree.fooddiversity.cmds;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.github.fragsforfree.fooddiversity.fooddiversity;
import com.github.fragsforfree.fooddiversity.enums.CONFIG;
import com.github.fragsforfree.fooddiversity.enums.HELP;
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;
import com.github.fragsforfree.fooddiversity.enums.STRINGS;

public class FoodDiversityCommandExecuter implements CommandExecutor {

	private fooddiversity plugin;
	
	public FoodDiversityCommandExecuter(fooddiversity plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {

    	if ((sender instanceof Player && sender.hasPermission(STRINGS.PERM_ADMIN.getString())) || (sender instanceof ConsoleCommandSender)){    		
    		switch(args.length){
    		case 1:
    		
    			if(args[0].toLowerCase().equalsIgnoreCase("help")){
    				sender.sendMessage(ChatColor.GREEN + args[0].toLowerCase() + " - " + args.length);	
 	    			sender.sendMessage(ChatColor.GREEN + HELP.TITLE.getTip());
	    			sender.sendMessage(ChatColor.GOLD + HELP.LINE_01.getCommand() + ChatColor.WHITE + HELP.LINE_01.getTip());
	    			sender.sendMessage(ChatColor.GOLD + HELP.LINE_02.getCommand() + ChatColor.WHITE + HELP.LINE_02.getTip());
	    			sender.sendMessage(ChatColor.GOLD + HELP.LINE_03.getCommand() + ChatColor.WHITE + HELP.LINE_03.getTip());
	    			sender.sendMessage(ChatColor.GOLD + HELP.LINE_04.getCommand() + ChatColor.WHITE + HELP.LINE_04.getTip());
	    			sender.sendMessage(ChatColor.GOLD + HELP.LINE_05.getCommand() + ChatColor.WHITE + HELP.LINE_05.getTip());
	    			return true;
    			}
	    		
    		case 3:
    			
    			if(args[0].toLowerCase().equalsIgnoreCase("set")){    				    			
	    			
	    			if(args[1].toLowerCase().equalsIgnoreCase("debug")){
    					if (args[2].equalsIgnoreCase("true") || args[2].equalsIgnoreCase("false")){
    						plugin.getConfig().set(CONFIG.PLUGIN_DEBUG.getPath(), Boolean.valueOf(args[2]));
    						sender.sendMessage(ChatColor.GREEN + MESSAGE.CMD_DEBUG_CHANGE.getMessage().replace("%args", args[2]));
    						plugin.saveConfig();
    					} else {
    						sender.sendMessage(ChatColor.RED + MESSAGE.EXPECT_BOOLEAN.getMessage());
    					}
    					return true;
	    			}
	    			
	    			if(args[1].toLowerCase().equalsIgnoreCase("fruit")){
    					if (isInteger(args[2])){
    						plugin.getConfig().set(CONFIG.CONFIG_ITEMSINROW_FRUIT.getPath(), Integer.parseInt(args[2]));
    						sender.sendMessage(ChatColor.GREEN + MESSAGE.CMD_FRUIT_CHANGE.getMessage().replace("%args", args[2]));
    						plugin.saveConfig();
    					} else {
    						sender.sendMessage(ChatColor.RED + MESSAGE.EXPECT_NUMERIC.getMessage());
    					}	
    					return true;
	    			}
	    			
	    			if(args[1].toLowerCase().equalsIgnoreCase("meat")){
    					if (isInteger(args[2])){
    						plugin.getConfig().set(CONFIG.CONFIG_ITEMSINROW_MEAT.getPath(), Integer.parseInt(args[2]));
    						sender.sendMessage(ChatColor.GREEN + MESSAGE.CMD_MEAT_CHANGE.getMessage().replace("%args", args[2]));
    						plugin.saveConfig();
    					} else {
    						sender.sendMessage(ChatColor.RED + MESSAGE.EXPECT_NUMERIC.getMessage());
    					}	
    					return true;
	    			}
	    			
	    			if(args[1].toLowerCase().equalsIgnoreCase("spezial")){
    					if (isInteger(args[2])){
    						plugin.getConfig().set(CONFIG.CONFIG_ITEMSINROW_SPEZIAL.getPath(), Integer.parseInt(args[2]));
    						sender.sendMessage(ChatColor.GREEN + MESSAGE.CMD_SPEZIAL_CHANGE.getMessage().replace("%args", args[2]));
    						plugin.saveConfig();
    					} else {
    						sender.sendMessage(ChatColor.RED + MESSAGE.EXPECT_NUMERIC.getMessage());
    					}	
    					return true;
	    			}

    			}
    			
	    	default:
	    		return false;    			
    		}

    	}   	
    	
    	return true;		
	}
	
    /**
     * checks the value on integer
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

}