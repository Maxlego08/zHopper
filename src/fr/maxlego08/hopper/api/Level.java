package fr.maxlego08.hopper.api;

import java.util.List;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.economy.Economy;
import fr.maxlego08.hopper.modules.Module;

public interface Level {

	/**
	 * Get level name
	 * @return name
	 */
	String getName();
	
	/**
	 * Get level to integer
	 * @return integer
	 */
	int getInteger();
	
	/**
	 * 
	 * @return the property maxDistanceLink
	 */
	int getMaxDistanceLink();
	
	/**
	 * 
	 * @return the property maxDistanceSuction
	 */
	int getMaxDistanceSuction();
	
	/**
	 * 
	 * @return the property maxDistanceKill
	 */
	int getMaxDistanceKill();
	
	/**
	 * 
	 * @return the property maxLink
	 */
	int getMaxLink();
	
	/**
	 * 
	 * @return the property maxItemPerSecond
	 */
	int getMaxItemPerSecond();
	
	/**
	 * 
	 * @return the property maxKillPerSecond 
	 */
	int getMaxKillPerSecond();
	
	/**
	 * 
	 * @return
	 */
	Level next();
	
	/**
	 * 
	 * @return
	 */
	ItemStack build();
	
	/**
	 * 
	 * @return
	 */
	ItemStack getItemStack();
	
	/**
	 * 
	 * @return
	 */
	boolean isDefault();
	
	/**
	 * 
	 * @return the price
	 */
	long getPrice();
	
	/**
	 * 
	 * @return
	 */
	Economy getEconomy();
	
	/**
	 * 
	 * @return You will get a copy of the list of modules
	 */
	List<Module> getModules();
	
	/**
	 * 
	 * @return the property 
	 */
	boolean canKillMonster();
	
	/**
	 * @return the property passiveKill
	 */
	boolean canKillPassive();
	
	/**
	 * Allows to recover a property in a Object
	 * @param key
	 * @return object
	 */
	Object getProperty(String key);
	
	/**
	 * Allows to recover a property in a integer
	 * @param key
	 * @return integer
	 */
	int getIntegerAsProperty(String key);
	
	/**
	 * Allows to recover a property in a boolean
	 * @param key
	 * @return boolean
	 */
	boolean getBooleanAsProperty(String key);
	
	/**
	 * Allows to recover a property in a long
	 * @param key
	 * @return long
	 */
	long getLongAsProperty(String key);
	
	/**
	 * Used to retrieve the list of properties
	 * @return map of properties
	 */
	Map<String, Object> getProperties();
	
	/**
	 * 
	 * @param hopper
	 */
	void run(Hopper hopper);
	
	/**
	 * When you add a module the priorities will be recalculated
	 * @param module
	 */
	void addModule(Module module);
	
}
