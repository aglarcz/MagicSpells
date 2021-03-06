package com.nisovin.magicspells.variables;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

public class PlayerStringVariable extends PlayerVariable {

	Map<String, String> data;
	
	
	public PlayerStringVariable() {
		data = new HashMap<String, String>();
	}
	
	@Override
	public void loadExtraData(ConfigurationSection section) {
		super.loadExtraData(section);
		defaultStringValue = section.getString("default-value", "");
	}
	
	@Override
	public String getStringValue(String player) {
		String ret = data.get(player);
		if (ret == null) ret = defaultStringValue;
		return ret;
	}
	
	@Override
	public void parseAndSet(String player, String textValue) {
		data.put(player, textValue);
	}
	
	@Override
	public void reset(String player) {
		data.remove(player);
	}
	
}
