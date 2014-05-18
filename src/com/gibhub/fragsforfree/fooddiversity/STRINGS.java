package com.gibhub.fragsforfree.fooddiversity;

public enum STRINGS {
	MEAT("Meat"),
	FRUIT("Fruit"),
	SPEZIAL("Spezial"),
	NONFOOD("nonFood"),
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
