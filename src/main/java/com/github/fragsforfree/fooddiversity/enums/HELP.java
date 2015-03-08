package com.github.fragsforfree.fooddiversity.enums;

public enum HELP {
	TITLE("", "Plugin FoodDiversity v1.1 by FragsForFree"),
	LINE_01("/fooddiversity help ", "shows this helppage"),
	LINE_02("/fooddiversity set debug [true/false] ", "set the debugmodes (default: false)"),
	LINE_03("/fooddiversity set fruit [integer] ", "set the eaten in row trigger for the type of fruit food"),
	LINE_04("/fooddiversity set meat [integer] ", "set the eaten in row trigger for the type of meat food"),
	LINE_05("/fooddiversity set spezial [integer] ", "set the eaten in row trigger for the type of spezial food");

	private String command;
	private String tip;
	
	HELP(String command, String tip){
		this.command = command;
		this.tip = tip;
	}

	public String getCommand(){
		return this.command;
	}
	
	public String getTip(){
		return this.tip;
	}
	
}
