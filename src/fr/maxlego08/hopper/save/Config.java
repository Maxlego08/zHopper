package fr.maxlego08.hopper.save;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.maxlego08.hopper.zcore.utils.inventory.Button;
import fr.maxlego08.hopper.zcore.utils.storage.Persist;
import fr.maxlego08.hopper.zcore.utils.storage.Saveable;

public class Config implements Saveable {

	public static int taskTickPerSecond = 20;
	public static List<String> blacklistBlockBreak = new ArrayList<String>();
	public static Button suctionButton = new Button(0, "�eSuction", 154, 0, "�f�l� �eDescription�7:",
			"  �7Pick up objects in a radius according to the level of the hopper.", "", "�f�l� �e%status%");
	public static Button killButton = new Button(1, "�eKill", 256, 0, "�f�l� �eDescription�7:",
			"  �7Kill entities in a radius according to the level of the hopper.", "", "�f�l� �e%status%");
	public static Button linkButton = new Button(2, "�eLink", 54, 0, "�f�l� �eDescription�7:",
			"  �7Transfer the items from the hopper to the different chests", "", "�f�l� �e%status%");
	public static Button blockButton = new Button(3, "�eBlock", 2, 0, "�f�l� �eDescription�7:",
			"  �7Destroys a certain number of blocks above the hopper", "", "�f�l� �e%status%");
	public static Button backButton = new Button(22, "�cBack to hopper", 166, 0);

	static {

		blacklistBlockBreak.add(Material.BEDROCK.name());
		blacklistBlockBreak.add(Material.HOPPER.name());

	}

	/**
	 * static Singleton instance.
	 */
	private static volatile Config instance;

	/**
	 * Private constructor for singleton.
	 */
	private Config() {
	}

	/**
	 * Return a singleton instance of Config.
	 */
	public static Config getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (Config.class) {
				if (instance == null) {
					instance = new Config();
				}
			}
		}
		return instance;
	}

	public void save(Persist persist) {
		persist.save(getInstance());
	}

	public void load(Persist persist) {
		persist.loadOrSaveDefault(getInstance(), Config.class);
		if (taskTickPerSecond < 1)
			taskTickPerSecond = 1;
	}

	public static boolean isBlacklist(Block block) {

		String str = block.toString();
		String type = str.split("type=")[1].split(",data")[0];

		return blacklistBlockBreak.stream().filter(s -> s.equalsIgnoreCase(type)).findAny().isPresent();
	}

}
