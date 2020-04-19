package fr.maxlego08.hopper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.api.events.HopperModuleRegisterEvent;
import fr.maxlego08.hopper.economy.Economy;
import fr.maxlego08.hopper.modules.Module;
import fr.maxlego08.hopper.modules.ModuleBlockBreak;
import fr.maxlego08.hopper.modules.ModuleKillMob;
import fr.maxlego08.hopper.modules.ModuleLinkContainer;
import fr.maxlego08.hopper.modules.ModuleSuction;
import fr.maxlego08.hopper.zcore.logger.Logger;
import fr.maxlego08.hopper.zcore.logger.Logger.LogType;
import fr.maxlego08.hopper.zcore.utils.ZUtils;

public class LevelObject extends ZUtils implements Level {

	private final String name;
	private final int level;
	private final long price;
	private final Economy economy;
	private final ItemStack itemStack;
	private HopperManager hopperManager;
	private List<Module> modules = new ArrayList<Module>();
	private final transient Map<Integer, Map<String, Object>> defaultProprieties = new HashMap<Integer, Map<String, Object>>();
	private Map<String, Object> properties = new HashMap<String, Object>();

	/**
	 * 
	 * @param name
	 * @param level
	 * @param price
	 * @param economy
	 * @param itemStack
	 */
	public LevelObject(String name, int level, long price, Economy economy, ItemStack itemStack) {
		super();

		this.initDefault();
		this.properties = getProperties(level);

		this.name = name;
		this.level = level;
		this.price = price;
		this.economy = economy;
		this.itemStack = itemStack;

		// On va register les modules en fonction des options du niveau

		modules.add(new ModuleSuction(1));
		modules.add(new ModuleLinkContainer(2));
		modules.add(new ModuleKillMob(3));
		modules.add(new ModuleBlockBreak(4));

		HopperModuleRegisterEvent event = new HopperModuleRegisterEvent(modules, this);
		event.callEvent();

		if (event.isCancelled())
			modules.clear();
	}

	/**
	 * 
	 * @param name
	 * @param level
	 * @param price
	 * @param economy
	 * @param itemStack
	 * @param properties
	 */
	public LevelObject(String name, int level, long price, Economy economy, ItemStack itemStack,
			Map<String, Object> properties) {
		this(name, level, price, economy, itemStack);
		this.properties = properties;
	}

	private void initDefault() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("maxDistanceLink", 5);
		properties.put("maxLink", 1);
		properties.put("maxItemPerSecond", 1);
		properties.put("maxDistanceSuction", 2);
		properties.put("monsterKill", false);
		properties.put("passiveKill", false);
		properties.put("maxDistanceKill", 0);
		properties.put("maxKillPerSecond", 0);
		properties.put("milliSecondModuleItem", 1000);
		properties.put("milliSecondModuleSuction", 1000);
		properties.put("milliSecondModuleKill", 1000);
		properties.put("milliSecondModuleBlock", 1000);
		properties.put("maxDistanceBlock", 1);
		this.defaultProprieties.put(1, properties);

		properties = new HashMap<String, Object>();
		properties.put("maxDistanceLink", 10);
		properties.put("maxLink", 2);
		properties.put("maxItemPerSecond", 2);
		properties.put("maxDistanceSuction", 5);
		properties.put("monsterKill", false);
		properties.put("passiveKill", true);
		properties.put("maxDistanceKill", 5);
		properties.put("maxKillPerSecond", 1);
		properties.put("milliSecondModuleItem", 1000);
		properties.put("milliSecondModuleSuction", 1000);
		properties.put("milliSecondModuleKill", 1000);
		properties.put("milliSecondModuleBlock", 1000);
		properties.put("maxDistanceBlock", 2);
		this.defaultProprieties.put(2, properties);

		properties = new HashMap<String, Object>();
		properties.put("maxDistanceLink", 20);
		properties.put("maxLink", 5);
		properties.put("maxItemPerSecond", 32);
		properties.put("maxDistanceSuction", 10);
		properties.put("monsterKill", true);
		properties.put("passiveKill", true);
		properties.put("maxDistanceKill", 10);
		properties.put("maxKillPerSecond", 3);
		properties.put("milliSecondModuleItem", 1000);
		properties.put("milliSecondModuleSuction", 1000);
		properties.put("milliSecondModuleKill", 1000);
		properties.put("milliSecondModuleBlock", 1000);
		properties.put("maxDistanceBlock", 3);
		this.defaultProprieties.put(3, properties);

	}

	/**
	 * 
	 * @param level
	 * @return
	 */
	private Map<String, Object> getProperties(int level) {
		if (!this.defaultProprieties.containsKey(level)) {
			Map<String, Object> properties = new HashMap<String, Object>();
			this.defaultProprieties.put(level, properties);
		}
		return this.defaultProprieties.get(level);
	}

	/**
	 * 
	 * @param hopperManager
	 */
	public void setHopperManager(HopperManager hopperManager) {
		this.hopperManager = hopperManager;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the level
	 */
	public int getInteger() {
		return level;
	}

	/**
	 * @return the maxDistanceLink
	 */
	public int getMaxDistanceLink() {
		return getIntegerAsProperty("maxDistanceLink");
	}

	/**
	 * @return the maxLink
	 */
	public int getMaxLink() {
		return getIntegerAsProperty("maxLink");
	}

	@Override
	public Level next() {
		return hopperManager.next(level);
	}

	@Override
	public ItemStack build() {

		Level next = next();

		ItemStack builder = itemStack.clone();
		ItemMeta itemMeta = builder.getItemMeta();

		if (itemMeta.hasDisplayName())
			itemMeta.setDisplayName(toString(itemMeta.getDisplayName(), next));

		if (itemMeta.hasLore()) {
			List<String> lore = new ArrayList<>();
			itemMeta.getLore().forEach(e -> lore.add(toString(e, next)));
			itemMeta.setLore(lore);
		}

		builder.setItemMeta(itemMeta);
		return builder;
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private String toString(String string, Level level) {

		string = string.replace("&", "§");
		string = string.replace("%level%", String.valueOf(getInteger()));
		string = string.replace("%name%", getName());
		string = string.replace("%price%", String.valueOf(getPrice()));
		string = string.replace("%economy%", getEconomy().toCurrency());
		for (Entry<String, Object> entry : getProperties().entrySet())
			string = string.replace("%" + entry.getKey() + "%", entry.getValue().toString());

		if (level != null) {
			string = string.replace("%nextLevel%", String.valueOf(level.getInteger()));
			string = string.replace("%nextName%", level.getName());
			string = string.replace("%nextPrice%", String.valueOf(level.getPrice()));
			string = string.replace("%nextEconomy%", level.getEconomy().toCurrency());
			for (Entry<String, Object> entry : level.getProperties().entrySet())
				string = string.replace("%next" + name(entry.getKey()) + "%", entry.getValue().toString());
		}
		return string;
	}

	@Override
	public boolean isDefault() {
		return level == 1;
	}

	@Override
	public long getPrice() {
		return price;
	}

	@Override
	public Economy getEconomy() {
		return economy;
	}

	@Override
	public int getMaxItemPerSecond() {
		return getIntegerAsProperty("maxItemPerSecond");
	}

	@Override
	public List<Module> getModules() {
		return new ArrayList<>(modules);
	}

	@Override
	public void run(Hopper hopper) {
		Collections.sort(modules, Comparator.comparingInt(Module::getPriority));

		Module module;
		for (Iterator<Module> iterator = modules.iterator(); iterator.hasNext(); module.preRun(hopper, this))
			module = iterator.next();
	}

	@Override
	public int getMaxDistanceSuction() {
		return getIntegerAsProperty("maxDistanceSuction");
	}

	@Override
	public int getMaxDistanceKill() {
		return getIntegerAsProperty("maxDistanceKill");
	}

	@Override
	public boolean canKillMonster() {
		return getBooleanAsProperty("monsterKill");
	}

	@Override
	public boolean canKillPassive() {
		return getBooleanAsProperty("passiveKill");
	}

	@Override
	public int getMaxKillPerSecond() {
		return getIntegerAsProperty("maxKillPerSecond");
	}

	@Override
	public void addModule(Module module) {
		modules.add(module);
	}

	@Override
	public Object getProperty(String key) {
		return properties.getOrDefault(key, defaultProprieties.get(level).getOrDefault(key, null));
	}

	@Override
	public int getIntegerAsProperty(String key) {
		Object object = getProperty(key);
		if (object == null)
			Logger.info("Attention la propriété '" + key + "' n'existe pas !", LogType.WARNING);
		return object != null && object instanceof Integer ? (int) object : 0;
	}

	@Override
	public boolean getBooleanAsProperty(String key) {
		Object object = getProperty(key);
		if (object == null)
			Logger.info("Attention la propriété '" + key + "' n'existe pas !", LogType.WARNING);
		return object != null && object instanceof Boolean ? (boolean) object : false;
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}

	@Override
	public ItemStack getItemStack() {
		return itemStack;
	}

	@Override
	public long getLongAsProperty(String key) {
		Object object = getProperty(key);
		if (object == null)
			Logger.info("Attention la propriété '" + key + "' n'existe pas !", LogType.WARNING);
		return object != null && object instanceof Long ? (long) object
				: object != null && object instanceof Integer ? (int) object : -1l;
	}

	@Override
	public void addProperty(String key, Object value) {
		properties.put(key, value);
	}

	@Override
	public void addPropertyIfAbsent(String key, Object value) {
		properties.putIfAbsent(key, value);
	}

	@Override
	public void removeProperty(String key) {
		if (!isDefaultProperty(key))
			properties.remove(key);
	}

	@Override
	public boolean isDefaultProperty(String key) {
		return defaultProprieties.containsKey(key);
	}

}
