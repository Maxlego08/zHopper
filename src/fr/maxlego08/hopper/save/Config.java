package fr.maxlego08.hopper.save;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.block.Block;

import fr.maxlego08.hopper.zcore.utils.inventory.Button;
import fr.maxlego08.hopper.zcore.utils.storage.Persist;
import fr.maxlego08.hopper.zcore.utils.storage.Saveable;

public class Config implements Saveable {

	public static int taskTickPerSecond = 20;
	public static int inventoryHopperSize = 27;
	public static String inventoryHopperName = "§eHopper";
	public static int hopperInformationSlot = 4;

	public static boolean useLevelForEveryHopper = true;
	public static boolean giveCustomHopperIfLevelIsDefault = false;
	
	public static String hopperName = "§eHopper";
	public static Map<Integer, List<String>> hopperLorePerLevel = new HashMap<Integer, List<String>>();
	public static List<String> defaultLore = Arrays.asList("§f§l» §7Hopper Level: §6%level%");
	
	public static boolean enableModuleSuction = true;
	public static boolean enableModuleKillMob = true;
	public static boolean enableModuleLinkChest = true;
	public static boolean enableModuleItemTransfert = true;
	public static boolean enableModuleBlockBreak = true;
	public static boolean enableModuleSuctionChunk = true;
	public static boolean closeInventoryOnReload = false;
	public static boolean debugMode = false;
	
	public static List<String> blacklistBlockBreak = new ArrayList<String>();

	public static Button suctionButton = new Button(0, "§eSuction", 154, 0, "§f§l» §eDescription§7:",
			"  §7Pick up objects in a radius according to the level of the hopper.", "", "§f§l» §e%status%");
	
	public static Button killButton = new Button(1, "§eKill", 256, 0, "§f§l» §eDescription§7:",
			"  §7Kill entities in a radius according to the level of the hopper.", "", "§f§l» §e%status%");
	public static Button linkButton = new Button(2, "§eLink", 54, 0, "§f§l» §eDescription§7:",
			"  §7Transfer the items from the hopper to the different chests", "", "§f§l» §e%status%");
	public static Button blockButton = new Button(3, "§eBlock", 2, 0, "§f§l» §eDescription§7:",
			"  §7Destroys a certain number of blocks above the hopper", "", "§f§l» §e%status%");
	public static Button suctionChunkButton = new Button(4, "§eSuction chunk", 154, 0, "§f§l» §eDescription§7:",
			"  §7Pick up objects in chunk of the hopper.", "", "§f§l» §e%status%");
	
	public static Button backButton = new Button(22, "§cBack to hopper", 166, 0);

	public static Button removeHopperButton = new Button(22, "§cRemove hopper", 166, 0, "",
			"§f§l» §cClick to remove hopper", "");
	public static Button linkedChestButton = new Button(11, "§eLinked chest", 54, 0, "",
			"§f§l» §eAmount of chest link: §6%amount%", "%chestLocations%", "", "§f§l» §eClick to link an other chest.",
			"");
	public static Button moduleConfigurationButton = new Button(15, "§eModules", 138, 0);
	public static String linkedChestLine = "§f§l» §e%x%, %y%, %z%";

	public static boolean runModuleBlockBreakAsync = false;
	public static boolean runModuleKilLMobAsync = false;
	public static boolean runModuleLinkContainerAsync = false;
	public static boolean runModuleSuctionAsync = false;
	public static boolean runModuleSuctionChunkAsync = false;
	
	static {

		blacklistBlockBreak.add(Material.BEDROCK.name());
		blacklistBlockBreak.add(Material.HOPPER.name());

		List<String> loreLevel1 = new ArrayList<>();
		loreLevel1.add("§f§l» §7Hopper Level: §6%level%");
		
		List<String> loreLevel2 = new ArrayList<>();
		loreLevel2.add("§f§l» §7Hopper Level: §6%level%");
		
		List<String> loreLevel3 = new ArrayList<>();
		loreLevel3.add("§f§l» §7Hopper Level: §6%level%");
		
		hopperLorePerLevel.put(1, loreLevel1);
		hopperLorePerLevel.put(2, loreLevel2);
		hopperLorePerLevel.put(3, loreLevel3);
		
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
