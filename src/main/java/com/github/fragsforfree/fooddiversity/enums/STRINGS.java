package com.github.fragsforfree.fooddiversity.enums;

public enum STRINGS {
	MEAT("Meat"),
	FRUIT("Fruit"),
	SPEZIAL("Spezial"),
	PERM_ADMIN("fooddiversity.admin"),
	PERM_IMMUN("fooddiversity.immun"),
	CMD("fooddiversity");

	private String value;
	
	STRINGS(String value){
		this.value = value;
	}
	
	public String getString(){
		return this.value;
	}	
	
}
