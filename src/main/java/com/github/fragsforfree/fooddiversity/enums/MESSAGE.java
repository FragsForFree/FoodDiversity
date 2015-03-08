package com.github.fragsforfree.fooddiversity.enums;

public enum MESSAGE {
	CHECK_MAT("Checking materials from config: %material"),
	INVALID_CONFIG_MAT("Invalid Configfile: Item '%material' is not a vailid material!"),
	INVALID_CONFIG_COUNT("Invalid Configfile: Item '%material' exists more than one times!"),
	INVALID_CONFIG_VERSION("Invalid Configfile: The configversion has changed, please rename old configfile. Check the new config.xml after server restart."),
	FOUND_ITEM_VIA_CONFIGFILE("%item was found as %type via configfile!"),
	NOT_FOUND_ITEM_VIA_CONFIGFILE("%item was not found as %type via configfile!"),
	ITEM_AMOUNT("Item '%item' amount is %value !"),
	ITEM_AMOUNT_NEW("Item '%item' new amount is %value !"),
	BLOCK_INCREASE("Player %player is eating, block increasing foodlevel!"),
	IS_IMMUN("Player %player has immun rights!"),
	HAS_EATEN("Player %player has eaten %value %type!"),
	HAS_REACHED_LIMIT("Player %player has reach the limit of %value %type!"),
	EXPECT_NUMERIC("Expected numeric value"),
	EXPECT_BOOLEAN("Expected value [true/false]"),
	CLICKED_CAKE("The spezial Material cake was clicked!"),
	ITEM_ISCAKE("The Item is a cake!"),
	CMD_DEBUG_CHANGE("debug change to '%args'"),
	CMD_FRUIT_CHANGE("fruit change to '%args'"),
	CMD_MEAT_CHANGE("meat change to '%args'"),
	CMD_SPEZIAL_CHANGE("spezial change to '%args'"),
	SCHEDULER_RUN("Scheduler runs!"),
	METRICS_FAILED("Integration of McStats failed!");

	
	private String message;
	
	MESSAGE(String message){
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}
}
