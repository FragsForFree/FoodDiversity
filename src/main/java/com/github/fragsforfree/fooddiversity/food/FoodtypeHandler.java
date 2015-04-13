package com.github.fragsforfree.fooddiversity.food;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Material;

import com.github.fragsforfree.fooddiversity.FoodDiversity;
import com.github.fragsforfree.fooddiversity.enums.MESSAGE;
import com.github.fragsforfree.fooddiversity.messages.MessageHandler;

public class FoodtypeHandler {

	private List<Foodtype> Foodtypes;
	private final FoodDiversity PLUGIN;
	
	public FoodtypeHandler(FoodDiversity plugin) {
		this.PLUGIN = plugin;
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
			MessageHandler.sendConsoleDebug(PLUGIN, Level.INFO, "Name des Foodtype: " + _name, PLUGIN.getConfigurationDebugmode());
			Foodtype _foodtype = new Foodtype(_name, _maxeateninrow);
			for (String food: listOfFood)
			{
				try{
					Material _material = Material.getMaterial(food);
					MessageHandler.sendConsoleDebug(PLUGIN, Level.INFO, MESSAGE.CHECK_MAT.getMessage().replace("%material", _material.name()), PLUGIN.getConfigurationDebugmode());
					if (this.getfoodtypename(_material.name()) == null) {						
						_foodtype.addfood(_material);
					}
					else
					{
						MessageHandler.sendConsoleDebug(PLUGIN, Level.WARNING, MESSAGE.INVALID_CONFIG_COUNT.getMessage().replace("%material", _material.name()), PLUGIN.getConfigurationDebugmode());
					}
				} catch (Exception ex) {
					MessageHandler.sendConsole(PLUGIN, Level.WARNING, MESSAGE.INVALID_CONFIG_MAT.getMessage().replace("%material", food));
				}							
			}
			Foodtypes.add(_foodtype);
		}					
	}


	/**
	 * get the foodtypeobject from a foodtypename
	 * @param _name - name to search and read
	 * @return foodtypeobject
	 */
	public Foodtype getfoodtype(String _name) {
		for (Foodtype _foodtype: Foodtypes){			
			if (_foodtype.getname().equalsIgnoreCase(_name)) {
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
			MessageHandler.sendConsole(PLUGIN, Level.WARNING, MESSAGE.INVALID_CONFIG_MAT.getMessage().replace("%material", _food));
		}
		return null;
	}
	
	public String getStringOfFoodtypes(){
		String list = "";
		for (Foodtype _foodtype: Foodtypes){
			list = list + _foodtype.getname() + " | ";
		}	
		return list;
	}
	
	public List<String> getListFoodtypes(){
		List<String> foodtypes = new ArrayList<String>();
		for (Foodtype _foodtype: Foodtypes){
			foodtypes.add(_foodtype.getname());
		}
		return foodtypes;
	}
	
	public String getListFood(String _name){
		Foodtype _foodtype = this.getfoodtype(_name);
		return _foodtype.getFoodList();		
	}

	public void removeFoodtype(String foodtype) {
		for (Foodtype _foodtype: Foodtypes){
			if (_foodtype.getname().equalsIgnoreCase(foodtype)){ Foodtypes.remove(_foodtype);}
		}
		
	}

	public void addFood(Material food, String foodtype) {
		this.getfoodtype(foodtype).addfood(food);		
	}

	public void removeFood(Material food, String foodtype) {
		this.getfoodtype(foodtype).removefood(food);		
	}

	public void setItemInRow(String foodtype, Integer _maxeateninrow) {
		this.getfoodtype(foodtype).setmaxeateninrow(_maxeateninrow);		
	}
	
}
