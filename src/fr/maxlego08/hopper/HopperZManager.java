package fr.maxlego08.hopper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.api.events.HopperCreateEvent;
import fr.maxlego08.hopper.api.events.HopperSoftDestroyEvent;
import fr.maxlego08.hopper.zcore.enums.Message;
import fr.maxlego08.hopper.zcore.utils.ZUtils;
import fr.maxlego08.hopper.zcore.utils.storage.Persist;

public class HopperZManager extends ZUtils implements HopperManager {

	private volatile HopperPlugin plugin;
	private volatile Map<Location, Hopper> hoppers = new HashMap<Location, Hopper>();
	private static List<Hopper> hopperList = new ArrayList<Hopper>();

	/**
	 * 
	 * @param plugin
	 */
	public HopperZManager(HopperPlugin plugin) {
		super();
		this.plugin = plugin;
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
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, HopperZManager.class, "hoppers");
		hoppers = new HashMap<>();
		hopperList.forEach(hopper -> hoppers.put(hopper.getLocation(), hopper));
	}

	@Override
	public List<Hopper> getHoppers() {
		return hopperList;
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

		Hopper hopper = new HopperObject(player.getUniqueId(), block.getLocation());

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


}
