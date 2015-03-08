package com.github.fragsforfree.fooddiversity.food;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import com.github.fragsforfree.fooddiversity.FoodDiversity;
import com.github.fragsforfree.fooddiversity.enums.CONFIG;
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;

public class FoodtypeHandler {

	private List<Foodtype> Foodtypes;
	private final Plugin PLUGIN;
	
	public FoodtypeHandler(FoodDiversity instance) {
		this.PLUGIN = instance;
		this.Foodtypes = new ArrayList<Foodtype>();
	}
	
	/**
	 * create new foodtypeobject and put it into a list
	 * @param _name - name value for the foodtypeobject
	 * @param listOfFood - list of strings to add one by one to the foodtypeobject
	 * @param _maxeateninrow - int value for the foodtypeobject
	 */
	public void addFoodtype(String _name, List<String> listOfFood, int _maxeateninrow) {
		if (getfoodtype(_name) == null) {
			PLUGIN.getLogger().log(Level.INFO, "Name des Foodtype: " + _name, ""); 
			Foodtype _foodtype = new Foodtype(_name, _maxeateninrow);
			for (String food: listOfFood)
			{
				try{
					Material _material = Material.getMaterial(food);
					if (PLUGIN.getConfig().getBoolean(CONFIG.PLUGIN_DEBUG.getPath())) {
						PLUGIN.getLogger().log(Level.INFO, MESSAGE.CHECK_MAT.getMessage().replace("%material", _material.name()));
						if (this.getfoodtypename(_material.name()) == null) {						
							_foodtype.addfood(_material);
						}
						else
						{
							PLUGIN.getLogger().log(Level.WARNING, MESSAGE.INVALID_CONFIG_COUNT.getMessage().replace("%material", _material.name()));	
						}
					}				
				} catch (Exception ex) {
					PLUGIN.getLogger().log(Level.WARNING, MESSAGE.INVALID_CONFIG_MAT.getMessage().replace("%material", food));
				}							
			}
			Foodtypes.add(_foodtype);
		}					
	}

	/**
	private void addFood (String _foodtypename, String _food) {
		Foodtype _foodtype = getfoodtype(_foodtypename);		
		if (_foodtype != null) {
			Material _material = this.getMaterial(_food);
			if (_material != null) {_foodtype.addfood(_material);}	
		}
	}
	*/

	/**
	 * get the foodtypeobject from a foodtypename
	 * @param _name - name to search and read
	 * @return foodtypeobject
	 */
	private Foodtype getfoodtype(String _name) {
		for (Foodtype _foodtype: Foodtypes){			
			if (_foodtype.getname() == _name) {
				return _foodtype;
			}			
		}
		return null;		
	}
	
	/**
	 * get the foodtypename value from a food
	 * @param _food - food to search and read
	 * @return string - name from the found foodtypeobject
	 */
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
	
	/**
	 * get the maxeateninrow value from a foodtype
	 * @param _foodtypename - foodtype to search and read
	 * @return int - maxeateninrow from the found foodtypeobject
	 */
	public int getmaxeateninrowfromfoodtype(String _foodtypename) {
		for (Foodtype _foodtype: Foodtypes){
			if (_foodtype.getname() == _foodtypename){
				return _foodtype.getmaxeateninrow();
			}
		}
		return 0;
	}
	
	/**
	 * check if a string convert into a bukkit materialobject
	 * @param _food - string to convert
	 * @return materialobject
	 */
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
