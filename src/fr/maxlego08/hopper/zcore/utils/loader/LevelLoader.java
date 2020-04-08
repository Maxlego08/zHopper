package fr.maxlego08.hopper.zcore.utils.loader;

import org.bukkit.configuration.file.YamlConfiguration;

import fr.maxlego08.hopper.LevelObject;
import fr.maxlego08.hopper.api.Level;

public class LevelLoader implements Loader<Level> {

	@Override
	public Level load(YamlConfiguration configuration, String path) {

		int level = configuration.getInt(path + "level");
		String name = configuration.getString(path + "name");
		int maxDistanceLink = configuration.getInt(path + "maxDistanceLink");
		int maxLink = configuration.getInt(path + "maxLink");

		return new LevelObject(name, level, maxDistanceLink, maxLink);
	}

	@Override
	public void save(Level object, YamlConfiguration configuration, String path) {

		configuration.set(path + ".level", object.getInteger());
		configuration.set(path + ".name", object.getName());
		configuration.set(path + ".maxDistanceLink", object.getMaxDistanceLink());
		configuration.set(path + ".maxLink", object.getMaxLink());

	}

}
