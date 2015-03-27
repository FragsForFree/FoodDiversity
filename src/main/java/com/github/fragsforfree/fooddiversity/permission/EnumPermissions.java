package com.github.fragsforfree.fooddiversity.permission;

public enum EnumPermissions {
	FoodDiversityAdmin("fooddiversity.admin"),
	FoodDiversityImmun("fooddiversity.immun");
		
	private String value;
	
	EnumPermissions(String value){
		this.value = value;
	}
	
	public String getString(){
		return this.value;
	}
}
