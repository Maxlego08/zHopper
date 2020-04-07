package fr.maxlego08.hopper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.api.events.HopperOpenConfigurationEvent;
import fr.maxlego08.hopper.zcore.enums.Inventory;
import fr.maxlego08.hopper.zcore.utils.ZUtils;

public class HopperObject extends ZUtils implements Hopper {

	private UUID owner;
	private List<UUID> whitelistPlayers = new ArrayList<UUID>();
	private Location location;
	private int level;
	private transient Level levelObject;
	private transient HopperManager hopperManager;

	/**
	 * 
	 * @param owner
	 * @param location
	 */
	public HopperObject(UUID owner, Location location, HopperManager hopperManager) {
		super();
		this.owner = owner;
		this.whitelistPlayers = new ArrayList<UUID>();
		this.location = location;
		this.hopperManager = hopperManager;
	}

	/**
	 * 
	 * @param hopperManager
	 */
	public void initHopper(HopperManager hopperManager){
		this.hopperManager = hopperManager;
	}
	
	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public List<UUID> getWhitelistPlayers() {
		return whitelistPlayers;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public Block getBlock() {
		return location == null ? null : location.getBlock();
	}

	@Override
	public org.bukkit.block.Hopper toBukkitHopper() {
		return location == null ? null : (org.bukkit.block.Hopper) location.getBlock().getState();
	}

	@Override
	public World getWorld() {
		return location == null ? null : location.getWorld();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HopperObject [owner=" + owner + ", whitelistPlayers=" + whitelistPlayers + ", location=" + location
				+ "]";
	}

	@Override
	public void openConfiguration(Player player) {
		
		HopperOpenConfigurationEvent event = new HopperOpenConfigurationEvent(this, player);
		event.callEvent();
		
		if (event.isCancelled())
			return;
		
		createInventory(player, Inventory.INVENTORY_CONFIGURATION, this);
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
		this.levelObject = this.hopperManager.getLevel(level);
	}

	@Override
	public Level toLevel() {
		return levelObject == null ? levelObject = hopperManager.getLevel(level) : levelObject;
	}

	@Override
	public HopperManager getManager() {
		return hopperManager;
	}

	@Override
	public void destroy() {
		if (location != null)
			getBlock().setType(Material.AIR);
	}

}
