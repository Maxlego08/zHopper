package fr.maxlego08.hopper.zcore.utils.loader;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.LevelObject;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.economy.Economy;

public class LevelLoader implements Loader<Level> {

	private final Loader<ItemStack> loader = new ItemStackYAMLoader();

	@Override
	public Level load(YamlConfiguration configuration, String path) {
		int level = configuration.getInt(path + "level");
		String name = configuration.getString(path + "name");
		long price = configuration.getLong(path + "price");
		Economy economy = Economy.valueOf(configuration.getString(path + "economy"));
		Map<String, Object> properties = new HashMap<String, Object>();
		configuration.getConfigurationSection(path + ".properties.").getKeys(false).forEach(key -> {
			properties.put(key, configuration.get(path + ".properties." + key));
		});
		ItemStack itemStack = loader.load(configuration, path + ".item.");
		return new LevelObject(name, level, price, economy, itemStack, properties);
	}

	@Override
	public void save(Level object, YamlConfiguration configuration, String path) {
		configuration.set(path + ".level", object.getInteger());
		configuration.set(path + ".name", object.getName());
		configuration.set(path + ".price", object.getPrice());
		configuration.set(path + ".economy", object.getEconomy().name());
		object.getProperties().forEach((key, value) -> configuration.set(path + ".properties." + key, value));
		loader.save(object.getItemStack(), configuration, path + ".item.");
	}

}
