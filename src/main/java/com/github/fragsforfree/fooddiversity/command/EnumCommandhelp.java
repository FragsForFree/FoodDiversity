package com.github.fragsforfree.fooddiversity.command;

public enum EnumCommandhelp {
	TITLE("", "Plugin FoodDiversity v1.1.1-Snapshot by FragsForFree"),
	Cmd_Help("/fooddiversity help ", "shows this helppage"),
	Cmd_Set_Debug("/fooddiversity set debug [true/false] ", "set the debugmodes (default: false)"),
	Cmd_List_Foodtypes("/fooddiversity list foodtypes ", "list all loaded foodtypes"),
	Cmd_List_Food("/fooddiversity list food [foodtype] ", "list all food of given foodtype"),
	LINE_03("/fooddiversity set fruit [integer] ", "set the eaten in row trigger for the type of fruit food"),
	LINE_04("/fooddiversity set meat [integer] ", "set the eaten in row trigger for the type of meat food"),
	LINE_05("/fooddiversity set spezial [integer] ", "set the eaten in row trigger for the type of spezial food");
	
	private String command;
	private String tip;
	
	EnumCommandhelp(String command, String tip){
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
