package com.github.fragsforfree.fooddiversity.command;

public enum EnumCommandhelp {
	TITLE("", "Plugin FoodDiversity v1.1.1-Snapshot by FragsForFree"),
	Cmd_Help("/fooddiversity help ", "shows this helppage"),
	Cmd_Set_Debug("/fooddiversity set debug [true/false] ", "set the debugmodes (default: false)"),
	Cmd_Set_Feature_ItemInRow("/fooddiversity set feature iteminrow [true/false] ", "set the feature iteminrow (default: true)"),
	Cmd_Set_Feature_Diversity("/fooddiversity set feature diversity [true/false] ", "set the feature diversity (default: true)"),
	Cmd_List_Foodtypes("/fooddiversity list foodtypes ", "list all loaded foodtypes"),
	Cmd_List_Food("/fooddiversity list food [foodtype] ", "list all food of given foodtype"),
	Cmd_Add_Foodtypes("/fooddiversity add foodtype <foodtype> <iteminrow[integer]> ", "create a new foodtype"),
	Cmd_Add_Food("/fooddiversity add food <food> <foodtype> ", "adds food to a foodtype"),
	Cmd_Remove_Foodtypes("/fooddiversity remove foodtype <foodtype> ", "removes the foodtype"),
	Cmd_Remove_Food("/fooddiversity remove food <food> ", "remove food from a foodtype"),
	Cmd_Set_ItemInRow("/fooddiversity set iteminrow <foodtype> <iteminrow[integer]> ", "set the eaten in row trigger for the type of given foodtype");
	
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
