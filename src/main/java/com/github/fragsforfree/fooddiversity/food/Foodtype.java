package com.github.fragsforfree.fooddiversity.food;

import java.util.List;
import java.util.ArrayList;
import org.bukkit.Material;

public class Foodtype {

	private String name;
	private List<Material> food;
	private int maxeateninrow;
	
	public Foodtype (String _name, int _maxeateninrow) {
		this.setname(_name);
		this.setmaxeateninrow(_maxeateninrow);
		this.food = new ArrayList<Material>();
	}
	
	/**
	 * set the value name
	 * @param _name - value to set
	 */
	private void setname(String _name) {
		this.name = _name;
	}
	
	/**
	 * returns value name
	 * @return string - name
	 */
	public String getname() {
		return this.name;
	}
	
	/**
	 * set the value maxeateninrow
	 * @param _maxeateninrow - value to set
	 */
	public void setmaxeateninrow(int _maxeateninrow) {
		this.maxeateninrow = _maxeateninrow;
	}
	
	/**
	 * returns value maxeateninrow
	 * @return int - maxeateninrow
	 */
	public int getmaxeateninrow() {
		return this.maxeateninrow;
	}
	
	/**
	 * add material to foodlist
	 * @param _material - material to add
	 */
	public void addfood(Material _material) {		
		if (!this.isfoodfound(_material)) {
			this.food.add(_material);
		}		
	}
	
	/**
	 * searching for material
	 * @param _material - material to search
	 * @return true/false - found or not
	 */
	public boolean isfoodfound(Material _material) {
		if (this.food.contains(_material)) {
			return true;
		}
		else
		{
			return false;
		}		
	}
	
	public String getFoodList(){
		String list = "";
		for(Material material : food){
			list = list + material.name() + " | ";
		}
		return list;
	}

	public void removefood(Material _material) {
		if (this.isfoodfound(_material)) {
			this.food.remove(_material);
		}		
		
	}
	
}
