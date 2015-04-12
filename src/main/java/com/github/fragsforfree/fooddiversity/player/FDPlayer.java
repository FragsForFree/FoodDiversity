package com.github.fragsforfree.fooddiversity.player;

import java.util.HashMap;
import java.util.Map;

public class FDPlayer {

	private String uuid;
	private String name;	
	private String lasteatentype;
	private Integer eateninrow;
	private boolean toblock;
	private boolean isconsuming;
	private boolean iscake;
	private Map<String, Integer> diversityvalues;
	
	public String getUuid(){
		return uuid;
	}
	
	public String getName(){
		return name;
	}
	
	public void setLasteatentype(String _lasteatentype){
		this.lasteatentype = _lasteatentype;
	}
	
	public String getLasteatentype(){
		return lasteatentype;
	}
	
	public void setEateninrow(Integer _eateninrow){
		this.eateninrow = _eateninrow;
	}
	
	public Integer getEateninrow(){
		return eateninrow;
	}
	
	public void setToBlock(boolean value){
		this.toblock = value;
	}
	
	public boolean getToBlock(){
		return toblock;
	}
	
	public void setIsConsuming(boolean value){
		this.isconsuming = value;
	}
	
	public boolean getIsConsuming(){
		return isconsuming;
	}
	
	public void setIsCake(boolean value){
		this.iscake = value;
	}
	
	public boolean getIsCake(){
		return iscake;
	}
	
	public void addDiversityEntry(String key, Integer value){
		if(!this.diversityvalues.containsKey(key)){
			this.diversityvalues.put(key, value);
		}
	}
	
	public void removeDiversityEntry(String key){
		this.diversityvalues.remove(key);
	}
	
	public String getDiversityString(){
		return this.diversityvalues.toString();
	}
	
	public Integer getDiversityValue(String key){
		return this.diversityvalues.get(key);
	}
	
	public FDPlayer (String _uuid, String _name){
		this.uuid = _uuid;
		this.name = _name;
		this.diversityvalues = new HashMap<String, Integer>();
	}
		
}
