package fr.maxlego08.hopper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.events.HopperOpenConfigurationEvent;
import fr.maxlego08.hopper.zcore.enums.Inventory;
import fr.maxlego08.hopper.zcore.utils.ZUtils;

public class HopperObject extends ZUtils implements Hopper {

	private UUID owner;
	private List<UUID> whitelistPlayers = new ArrayList<UUID>();
	private Location location;

	/**
	 * 
	 * @param owner
	 * @param location
	 */
	public HopperObject(UUID owner, Location location) {
		super();
		this.owner = owner;
		this.whitelistPlayers = new ArrayList<UUID>();
		this.location = location;
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

}
