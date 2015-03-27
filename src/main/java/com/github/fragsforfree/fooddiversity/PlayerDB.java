package com.github.fragsforfree.fooddiversity;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import com.github.fragsforfree.fooddiversity.messages.MessageHandler;

public class PlayerDB {

	private final Plugin PLUGIN;
	private final String FILENAME;
	private final File FOLDER;
	private FileConfiguration config;
	private File configFile;
	
	public PlayerDB(String filename, FoodDiversity instance){
		if(!filename.endsWith(".yml")){
			filename += ".yml";
		}
		
		this.FILENAME = filename;
		this.PLUGIN = instance;
		this.FOLDER = this.PLUGIN.getDataFolder();
		this.config = null;
		this.configFile = null;
		reload();
		
	}
	
	public PlayerDB(File folder, String filename, FoodDiversity instance){
		if(!filename.endsWith(".yml")){
			filename += ".yml";
		}
		
		this.FILENAME = filename;
		this.PLUGIN = instance;
		this.FOLDER = folder;
		this.config = null;
		this.configFile = null;
		reload();
	}
	
	public FileConfiguration getConfig(){
		if (config == null){
			reload();
		}
		return config;
	}
	
	public void reload(){
		if(!this.FOLDER.exists()){
			try{
				if(this.FOLDER.mkdir()){
					MessageHandler.sendConsole(PLUGIN, Level.INFO, "Folder " + this.FOLDER.getName() + " created.");				
				} else {
					MessageHandler.sendConsole(PLUGIN, Level.WARNING, "Unable to create folder " + this.FOLDER.getName() + ".");
				}
			} catch(Exception e) {				
			}
		}
		
		configFile = new File(this.FOLDER, this.FILENAME);
		if(!configFile.exists()){
			try{
				configFile.createNewFile();		
			} catch (IOException e){
				MessageHandler.sendConsole(PLUGIN, Level.WARNING, "error creating new file: " + e.toString());
			}
		}
		config = YamlConfiguration.loadConfiguration(configFile);	
	}
		
	public void saveDefaultConfig(){
		if(configFile == null){
			configFile = new File(this.PLUGIN.getDataFolder(), this.FILENAME);
		}
		if(!configFile.exists()){
			this.PLUGIN.saveResource(this.FILENAME, false);
		}
	}
	
	public void save(){
		if(config == null || configFile == null){
			return;
		}
		try {
			getConfig().save(configFile);
		} catch (IOException ex) {
			MessageHandler.sendConsole(PLUGIN, Level.WARNING, "Could not save PlayerDB to " + configFile.getName() + ": " + ex);
		}
	}
	
	public void set(String path, Object o){
		getConfig().set(path, o);
	}
		
}
