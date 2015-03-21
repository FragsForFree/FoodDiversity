package com.github.fragsforfree.fooddiversity.command;

import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.github.fragsforfree.fooddiversity.FoodDiversity;
import com.github.fragsforfree.fooddiversity.enums.CONFIG;
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;
import com.github.fragsforfree.fooddiversity.enums.STRINGS;

public class FoodDiversityCommandExecuter implements CommandExecutor {

	private FoodDiversity plugin;
	
	public FoodDiversityCommandExecuter(FoodDiversity plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {

    	if ((sender instanceof Player && sender.hasPermission(STRINGS.PERM_ADMIN.getString())) || (sender instanceof ConsoleCommandSender)){    		
    		switch(args.length){
 	    		
    		case 2:
    			
    			if(args[0].toLowerCase().equalsIgnoreCase("list")){
    				
    				if(args[1].toLowerCase().equalsIgnoreCase("foodtypes")){
    					this.plugin.listFoodtypes(sender);
    					return true;
    				}
    				
    			}
    		
    		case 3:
    			
    			if(args[0].toLowerCase().equalsIgnoreCase("list")){
    				
    				if(args[1].toLowerCase().equalsIgnoreCase("food")){
    					this.plugin.listFood(sender, args[2]);			
    					return true;
    				}
    				
    			}
    			
    			if(args[0].toLowerCase().equalsIgnoreCase("set")){    				    			
	    			
	    			if(args[1].toLowerCase().equalsIgnoreCase("debug")){
    					this.plugin.setConfigDebug(sender, args[2]);
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
    			
    			this.sendHelp(sender);
    			return true;
    		
    		case 4:
    			
	    		if(args[0].toLowerCase().equalsIgnoreCase("add")){    			
	    			if(args[1].toLowerCase().equalsIgnoreCase("foodtype")){	    					    				
	    				if(this.isInteger(args[3])){
	    					List<String> list = Arrays.asList(args[4].split(";").toString().toUpperCase());
	    					this.plugin.addFoodtype(sender, args[2], list,  Integer.valueOf(args[3]));
	    				}    				
	    			}	    			
	    		}
    			
	    	default:	    			    		
	    		this.sendHelp(sender);
	    		return true;    			
    		}

    	}   	
    	
    	return false;		
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
    
    private void sendHelp(CommandSender sender){
		
    	if (sender.hasPermission(STRINGS.PERM_ADMIN.getString()) || (sender instanceof ConsoleCommandSender)){	
			sender.sendMessage(ChatColor.GREEN + EnumCommandhelp.TITLE.getTip());
			sender.sendMessage(ChatColor.GOLD + EnumCommandhelp.Cmd_Help.getCommand() + ChatColor.WHITE + EnumCommandhelp.Cmd_Help.getTip());
			sender.sendMessage(ChatColor.GOLD + EnumCommandhelp.Cmd_Set_Debug.getCommand() + ChatColor.WHITE + EnumCommandhelp.Cmd_Set_Debug.getTip());
			sender.sendMessage(ChatColor.GOLD + EnumCommandhelp.Cmd_List_Foodtypes.getCommand() + ChatColor.WHITE + EnumCommandhelp.Cmd_List_Foodtypes.getTip());
			sender.sendMessage(ChatColor.GOLD + EnumCommandhelp.Cmd_List_Food.getCommand() + ChatColor.WHITE + EnumCommandhelp.Cmd_List_Food.getTip());			
			sender.sendMessage(ChatColor.GOLD + EnumCommandhelp.LINE_03.getCommand() + ChatColor.WHITE + EnumCommandhelp.LINE_03.getTip());
			sender.sendMessage(ChatColor.GOLD + EnumCommandhelp.LINE_04.getCommand() + ChatColor.WHITE + EnumCommandhelp.LINE_04.getTip());
			sender.sendMessage(ChatColor.GOLD + EnumCommandhelp.LINE_05.getCommand() + ChatColor.WHITE + EnumCommandhelp.LINE_05.getTip());
    	}
    	
    }

}