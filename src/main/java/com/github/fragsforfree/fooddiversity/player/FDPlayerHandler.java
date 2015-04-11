package com.github.fragsforfree.fooddiversity.player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import com.github.fragsforfree.fooddiversity.FoodDiversity;
import com.github.fragsforfree.fooddiversity.messages.MessageHandler;

public class FDPlayerHandler {

	private final FoodDiversity plugin;
	private List<FDPlayer> fdplayers;
	
	public FDPlayerHandler(FoodDiversity plugin){
		this.plugin = plugin;
		this.fdplayers = new ArrayList<FDPlayer>();
	}
	
	public void addFDPlayer(String uuid, String name){
		if (getFDPlayer(uuid) == null){
			FDPlayer fdplayer = new FDPlayer(uuid, name);
			fdplayers.add(fdplayer);
		}
		else
		{
			MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' allready loaded!", plugin.getDebug());	
		}
	}
	
	private FDPlayer getFDPlayer(String uuid){
		for(FDPlayer fdplayer:fdplayers){
			if (fdplayer.getUuid().equalsIgnoreCase(uuid)){
				return fdplayer;
			}
		}
		return null;
	}
	
	public void removeFDPlayer(String uuid){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			fdplayers.remove(fdplayer);
		}
		else
		{
			MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		}
	}
	
	public String getValueName(String uuid){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			return fdplayer.getName();
		}
		MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		return null;		
	}
	
	public void setValueLasteatentype(String uuid, String value){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			fdplayer.setLasteatentype(value);
		}
		else
		{
			MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		}
	}
	
	public String getValueLasteatentype(String uuid){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			return fdplayer.getLasteatentype();
		}
		MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		return null;
	}
	
	public void setValueEateninrow(String uuid, Integer value){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			fdplayer.setEateninrow(value);
		}
		else
		{
			MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		}
	}

	public Integer getValueEateninrow(String uuid){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			return fdplayer.getEateninrow();
		}
		MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		return null;
	}	
	
	public void setValueToblock(String uuid, boolean value){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			fdplayer.setToBlock(value);
		}
		else
		{
			MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		}
	}

	public boolean getValueToBlock(String uuid){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			return fdplayer.getToBlock();
		}
		MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		return false;
	}		
	
	public void setValueIsConsuming(String uuid, boolean value){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			fdplayer.setIsConsuming(value);
		}
		else
		{
			MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		}
	}

	public boolean getValueIsConsuming(String uuid){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			return fdplayer.getIsConsuming();
		}
		MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		return false;
	}		
	
	public void setValueIsCake(String uuid, boolean value){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			fdplayer.setIsCake(value);
		}
		else
		{
			MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		}
	}
	
	public boolean getValueIsCake(String uuid){
		FDPlayer fdplayer = getFDPlayer(uuid);
		if (fdplayer != null){
			return fdplayer.getIsCake();
		}
		MessageHandler.sendConsoleDebug(plugin, Level.WARNING, "User with uuid '" + uuid + "' not loaded!", plugin.getDebug());
		return false;
	}	
}
