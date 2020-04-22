package fr.maxlego08.hopper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.api.events.HopperCreateEvent;
import fr.maxlego08.hopper.api.events.HopperDestroyEvent;
import fr.maxlego08.hopper.api.events.HopperSoftDestroyEvent;
import fr.maxlego08.hopper.api.events.HopperUpgradeEvent;
import fr.maxlego08.hopper.economy.Economy;
import fr.maxlego08.hopper.economy.EconomyUtils;
import fr.maxlego08.hopper.modules.Module;
import fr.maxlego08.hopper.nbt.NBTManager;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.enums.Message;
import fr.maxlego08.hopper.zcore.logger.Logger;
import fr.maxlego08.hopper.zcore.logger.Logger.LogType;
import fr.maxlego08.hopper.zcore.utils.builder.ItemBuilder;
import fr.maxlego08.hopper.zcore.utils.loader.LevelLoader;
import fr.maxlego08.hopper.zcore.utils.loader.Loader;
import fr.maxlego08.hopper.zcore.utils.storage.Persist;

public class HopperZManager extends EconomyUtils implements HopperManager {

	private volatile HopperPlugin plugin;
	private volatile NBTManager manager = new NBTManager();
	private volatile Map<Location, Hopper> hoppers = new HashMap<Location, Hopper>();
	private volatile Map<Integer, Level> levels = new HashMap<>();
	private volatile HopperRunnable runnable;
	private volatile Map<Player, Hopper> linkeds = new HashMap<>();
	private static List<Hopper> hopperList = new ArrayList<Hopper>();

	/**
	 * 
	 * @param plugin
	 */
	public HopperZManager(HopperPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	public void run() {
		if (runnable != null)
			return;
		this.runnable = new HopperRunnable(this);
		this.runnable.runTaskTimerAsynchronously(plugin, Config.taskTickPerSecond, Config.taskTickPerSecond);
	}

	/**
	 * 
	 * @return plugin
	 */
	public HopperPlugin getPlugin() {
		return plugin;
	}

	@Override
	public void save(Persist persist) {
		hopperList = new ArrayList<>(hoppers.values());
		persist.save(this, "hoppers");
		this.saveLevel();
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, HopperZManager.class, "hoppers");
		this.loadLevel();
		hoppers = new HashMap<>();
		hopperList.forEach(hopper -> {
			((HopperObject) hopper).initHopper(this);
			hoppers.put(hopper.getLocation(), hopper);
		});
	}

	@Override
	public List<Hopper> getHoppers() {
		return new ArrayList<>(hoppers.values());
	}

	@Override
	public Hopper getHopper(Location location) {
		return location == null ? null : hoppers.getOrDefault(location, null);
	}

	@Override
	public boolean isHopper(Location location) {
		return getHopper(location) != null;
	}

	@Override
	public void createHopper(Block block, Player player) {

		if (block == null || !block.getType().equals(Material.HOPPER))
			return;

		Hopper hopper = new HopperObject(player.getUniqueId(), block.getLocation(), this, manager.createHopper(player));

		HopperCreateEvent event = new HopperCreateEvent(hopper, player);
		event.callEvent();

		if (event.isCancelled())
			return;

		hoppers.put(block.getLocation(), hopper);
		message(player, Message.HOPPER_CREATE);

	}

	@Override
	public void destroyHopper(Block block, Player player, BlockBreakEvent event) {

		if (block == null || !block.getType().equals(Material.HOPPER))
			return;

		if (!isHopper(block.getLocation()))
			return;

		Hopper hopper = getHopper(block.getLocation());
		HopperSoftDestroyEvent destroyEvent = new HopperSoftDestroyEvent(hopper, player);
		destroyEvent.callEvent();

		event.setCancelled(destroyEvent.isCancelled());

	}

	@Override
	public void interactHopper(Player player, Block block, PlayerInteractEvent event) {

		if (block == null || !block.getType().equals(Material.HOPPER))
			return;

		if (!isHopper(block.getLocation()))
			return;

		if (!event.getAction().equals(Action.LEFT_CLICK_BLOCK))
			return;

		Hopper hopper = getHopper(block.getLocation());
		hopper.openConfiguration(player);
		event.setCancelled(true);

	}

	@Override
	public Level getLevel(int level) {
		return levels.getOrDefault(level, getDefaultLevel());
	}

	@Override
	public Level getDefaultLevel() {
		Level level = levels.getOrDefault(1, null);
		if (level == null) {
			level = new LevelObject("Premier level", 1, 0, Economy.VAULT,
					new ItemBuilder(Material.STONE, "§cSomething is rong").build());
			((LevelObject) level).setHopperManager(this);
		}
		return level;
	}

	@Override
	public void destroyHopper(Player player, Hopper hopper) {

		ItemStack itemStack = manager.dropItem(hopper);

		HopperDestroyEvent event = new HopperDestroyEvent(hopper, player, itemStack);
		event.callEvent();
		if (event.isCancelled())
			return;

		hopper.getWorld().dropItem(hopper.getLocation(), event.getItemStack());
		hopper.destroy();
		player.closeInventory();
		this.deleteHopper(hopper);

		message(player, Message.HOPPER_DESTROY);

	}

	@Override
	public void loadLevel() {
		File file = new File(plugin.getDataFolder() + File.separator + "levels.yml");

		if (!file.exists()) {
			this.saveDefaultLevel();
			return;
		}
		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);

		if (configuration.getString("levels") == null) {
			this.saveDefaultLevel();
			return;
		}

		levels = new HashMap<>();
		Loader<Level> loader = new LevelLoader();

		for (String str : configuration.getConfigurationSection("levels.").getKeys(false)) {

			String path = "levels." + str + ".";
			Level level = loader.load(configuration, path);
			((LevelObject) level).setHopperManager(this);
			levels.put(level.getInteger(), level);

		}

		Logger.info(file.getAbsolutePath() + " loaded successfully !", LogType.SUCCESS);
		Logger.info("Loading " + levels.size() + " levels", LogType.SUCCESS);
	}

	@Override
	public void saveLevel() {
		File file = new File(plugin.getDataFolder() + File.separator + "levels.yml");
		if (file.exists())
			file.delete();
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		Loader<Level> loader = new LevelLoader();
		levels.forEach((integer, level) -> {
			String path = "levels." + integer + ".";
			loader.save(level, configuration, path);
		});

		try {
			configuration.save(file);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void saveDefaultLevel() {

		this.levels = new HashMap<>();

		ItemBuilder builder = new ItemBuilder(getMaterial(384), "§eHopper");
		builder.addLine("§f§l» §eNiveau§8: §6%level%");
		builder.addLine("§f§l» §eNom§8: §6%name%");
		builder.addLine("§f§l» §eNombre de containeur§8: §6%maxLink%");
		builder.addLine("§f§l» §eDistance maximum§8: §6%maxItemPerSecond%");
		builder.addLine("");
		builder.addLine("§f§l» §eProchain Niveau§8: §6%nextLevel%");
		builder.addLine("§f§l» §eNom§8: §6%nextName%");
		builder.addLine("§f§l» §eNombre de containeur§8: §6%nextMaxLink%");
		builder.addLine("§f§l» §eDistance maximum§8: §6%nextMaxItemPerSecond%");
		Level level1 = new LevelObject("Premier level", 1, 0, Economy.VAULT, builder.build());

		builder = new ItemBuilder(getMaterial(384), "§eHopper");
		builder.addLine("§f§l» §eNiveau§8: §6%level%");
		builder.addLine("§f§l» §eNom§8: §6%name%");
		builder.addLine("§f§l» §eNombre de containeur§8: §6%maxLink%");
		builder.addLine("§f§l» §eDistance maximum§8: §6%maxItemPerSecond%");
		builder.addLine("");
		builder.addLine("§f§l» §eProchain Niveau§8: §6%nextLevel%");
		builder.addLine("§f§l» §eNom§8: §6%nextName%");
		builder.addLine("§f§l» §eNombre de containeur§8: §6%nextMaxLink%");
		builder.addLine("§f§l» §eDistance maximum§8: §6%nextMaxItemPerSecond%");

		Level level2 = new LevelObject("Deuxième level", 2, 100, Economy.VAULT, builder.build());

		builder = new ItemBuilder(getMaterial(384), "§eHopper");
		builder.addLine("§f§l» §eNiveau§8: §6%level%");
		builder.addLine("§f§l» §eNom§8: §6%name%");
		builder.addLine("§f§l» §eNombre de containeur§8: §6%maxLink%");
		builder.addLine("§f§l» §eDistance maximum§8: §6%maxItemPerSecond%");

		Level level3 = new LevelObject("Troisième level", 3, 250, Economy.VAULT, builder.build());

		((LevelObject) level1).setHopperManager(this);
		((LevelObject) level2).setHopperManager(this);
		((LevelObject) level3).setHopperManager(this);

		levels.put(1, level1);
		levels.put(2, level2);
		levels.put(3, level3);

		this.saveLevel();

	}

	@Override
	public Level next(int level) {
		return levels.getOrDefault(level + 1, null);
	}

	@Override
	public void updateLevel(Hopper hopper, Player player) {

		Level currentLevel = hopper.toLevel();
		Level level = currentLevel.next();

		if (level == null) {
			message(player, Message.HOPPER_LEVEL_ERROR);
			return;
		}

		if (!hasMoney(level.getEconomy(), player, level.getPrice())) {
			message(player, Message.HOPPER_LEVEL_ERROR_MONEY);
			return;
		}

		HopperUpgradeEvent event = new HopperUpgradeEvent(hopper, currentLevel, level, player);
		event.callEvent();
		if (event.isCancelled())
			return;

		level = event.getNewLevel();

		withdrawMoney(level.getEconomy(), player, level.getPrice());
		hopper.setLevel(level.getInteger());
		player.closeInventory();

		message(player, Message.HOPPER_LEVEL_SUCCESS, level.getInteger());
	}

	@Override
	public void deleteHopper(Hopper hopper) {
		if (hopper == null)
			return;
		hoppers.remove(hopper.getLocation());
	}

	@Override
	public void linkHopper(Player player, Hopper hopper) {

		if (hopper.canLink()) {
			message(player, Message.HOPPER_LINK_ERROR);
			return;
		}

		if (linkeds.containsKey(player)) {
			message(player, Message.HOPPER_LINK_ERROR_ALREADY);
			return;
		}

		player.closeInventory();
		linkeds.put(player, hopper);
		message(player, Message.HOPPER_LINK_START);

	}

	@Override
	public void interactBlock(Player player, Block block, PlayerInteractEvent event) {

		if (linkeds.containsKey(player)) {

			Hopper hopper = linkeds.get(player);
			BlockState blockState = block.getState();

			linkeds.remove(player);

			if (blockState instanceof Chest) {
				event.setCancelled(true);
				hopper.linkContainer(player, block);
			} else
				message(player, Message.HOPPER_LINK_ERROR_CONTAINER);

		}

	}

	@Override
	public void addModule(Module module) {
		levels.values().forEach(level -> level.addModule(module));
	}

	@Override
	public void addProperty(String key, Object value) {
		levels.values().forEach(level -> level.addProperty(key, value));
	}

	@Override
	public void addPropertyIfAbsent(String key, Object value) {
		levels.values().forEach(level -> level.addPropertyIfAbsent(key, value));
	}

	@Override
	public void removeProperty(String key) {
		levels.values().forEach(level -> level.removeProperty(key));
	}

	@Override
	public void updateLevel() {
		levels.values().forEach(level -> level.updateModule());
	}

}
