package com.github.fragsforfree.fooddiversity.food;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import com.github.fragsforfree.fooddiversity.fooddiversity;
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;

public class FoodtypeHandler {

	private List<Foodtype> Foodtypes;
	private final Plugin PLUGIN;
	
	public FoodtypeHandler(fooddiversity instance) {
		this.PLUGIN = instance;
	}
	
	public void addFoodtype(String _name) {
		if (getfoodtype(_name) == null) {
			Foodtypes.add(new Foodtype(_name));
		}					
	}
	
	public void addFood (String _foodtypename, String _food) {
		Foodtype _foodtype = getfoodtype(_foodtypename);		
		if (_foodtype != null) {
			Material _material = this.getMaterial(_food);
			if (_material != null) {_foodtype.addfood(_material);}	
		}
	}

	private Foodtype getfoodtype(String _name) {
		for (Foodtype _foodtype: Foodtypes){			
			if (_foodtype.getname() == _name) {
				return _foodtype;
			}			
		}
		return null;		
	}
	
	public String getfoodtypename(String _food) {
		Material _material = this.getMaterial(_food);
		
		if (_material != null) {
			for (Foodtype _foodtype: Foodtypes){
				if (_foodtype.isfoodfound(_material)){
					return _foodtype.getname();
				}
			}			
		}
		return null;
	}
	
	private Material getMaterial(String _food){
		try 
		{
			Material _material = Material.getMaterial(_food);
			return _material;
		} catch (Exception ex) {
			PLUGIN.getLogger().log(Level.WARNING, MESSAGE.INVALID_CONFIG_MAT.getMessage().replace("%material", _food));
		}
		return null;
	}
	
}
