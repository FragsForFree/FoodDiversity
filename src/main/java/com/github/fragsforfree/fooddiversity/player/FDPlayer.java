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
	
	public void addDiversityValues(String key, Integer addvalue, boolean addvalueonkey){
		Integer value;
		for (Map.Entry<String, Integer> entry : this.diversityvalues.entrySet())
		{
		    if (entry.getKey().contains(key)){
		    	if (addvalueonkey){
		    		value = entry.getValue();
		    		value = value + addvalue;
		    		if (value >= 20){ value = 20; }
		    		entry.setValue(value);
		    	}
		    }
		    else
		    {
		    	if (!addvalueonkey){
		    		value = entry.getValue();
		    		value = value - addvalue;
		    		if (value <= 0){ value = 0; }
		    		entry.setValue(value);		    		
		    	}
		    }
		}		
	}
	
	public double getDiversityaverage(){
		double value = 0;
		Integer count = 0;
		for (Map.Entry<String, Integer> entry : this.diversityvalues.entrySet())
		{
			value = value + entry.getValue();
			count = count + 1;
		}
		return (value / count);
	}
	
}
