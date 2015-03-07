package main.java.com.github.fragsforfree.fooddiversity.food;

import java.util.List;
import org.bukkit.Material;

public class Foodtype {

	private String name;
	private List<Material> food;
	
	public Foodtype (String _name) {
		this.setname(_name);
	}
	
	private void setname(String _name) {
		this.name = _name;
	}
	
	public String getname() {
		return this.name;
	}
	
	public void addfood(Material _material) {		
		if (!this.isfoodfound(_material)) {
			this.food.add(_material);
		}		
	}
	
	public boolean isfoodfound(Material _material) {
		if (this.food.contains(_material)) {
			return true;
		}
		else
		{
			return false;
		}		
	}
	
}
