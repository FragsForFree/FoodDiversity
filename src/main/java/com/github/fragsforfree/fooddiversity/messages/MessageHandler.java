package com.github.fragsforfree.fooddiversity.messages;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public final class MessageHandler {

	private MessageHandler(){		 
	}
	
	public static void sendMessage(Plugin plugin, CommandSender sender, String Message, boolean Error){
    	if (sender instanceof ConsoleCommandSender){
    		MessageHandler.sendConsole(plugin, Level.INFO, Message);
    	}
    	if (sender instanceof Player){
    		MessageHandler.sendPlayerMessage((Player) sender, Message, plugin.getName(), Error);
    	} 
	}
	
	public static void sendConsole(Plugin plugin, Level loglvl, String Message){
		plugin.getLogger().log(loglvl, Message);
	}
	
	public static void sendConsoleDebug(Plugin plugin, Level loglvl, String Message, boolean debug){
		if (debug == true){
			plugin.getLogger().log(loglvl, Message);
		}
	}
	
	public static void sendPlayerMessage(Player player, String Message, String Pluginname, boolean Error){
		String chatmessage = ChatColor.GOLD + "[" + ChatColor.GREEN + Pluginname + ChatColor.GOLD + "] "; 
	    if (Error == true){
	    	chatmessage = chatmessage + ChatColor.RED + "ERROR ";
	    }
	    chatmessage = chatmessage + ChatColor.WHITE + Message;
		player.sendMessage(chatmessage);		
	}
	
	public static void sendCommandHelp(Player player, String Command, String Tip){
			player.sendMessage(ChatColor.GOLD + Command + ChatColor.WHITE + Tip);
	}
	
}
