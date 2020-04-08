package fr.maxlego08.hopper.zcore.utils.loader;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.maxlego08.hopper.LevelObject;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.economy.Economy;
import fr.maxlego08.hopper.zcore.logger.Logger;

public class LevelLoader implements Loader<Level> {

	@Override
	public Level load(YamlConfiguration configuration, String path) {

		int level = configuration.getInt(path + "level");
		String name = configuration.getString(path + "name");
		int maxDistanceLink = configuration.getInt(path + "maxDistanceLink");
		int maxLink = configuration.getInt(path + "maxLink");
		int maxItemPerSecond = configuration.getInt(path + "maxItemPerSecond");
		long price = configuration.getLong(path + "price");
		Economy economy = Economy.valueOf(configuration.getString(path + "economy"));

		if (maxDistanceLink > 64){
			maxDistanceLink = 64;
			Logger.info("Impossible de mettre plus de 64 pour le maxItemPerSecond !");
		}
		
		return new LevelObject(name, level, maxDistanceLink, maxLink, maxItemPerSecond, price, economy);
	}

	@Override
	public void save(Level object, YamlConfiguration configuration, String path) {

		configuration.set(path + ".level", object.getInteger());
		configuration.set(path + ".name", object.getName());
		configuration.set(path + ".maxDistanceLink", object.getMaxDistanceLink());
		configuration.set(path + ".maxLink", object.getMaxLink());
		configuration.set(path + ".maxItemPerSecond", object.getMaxItemPerSecond());
		configuration.set(path + ".price", object.getPrice());
		configuration.set(path + ".economy", object.getEconomy().name());

	}

}
