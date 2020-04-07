package fr.maxlego08.hopper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.zcore.utils.ZUtils;

public class HopperObject extends ZUtils implements Hopper {

	private UUID owner;
	private List<UUID> whitelistPlayers = new ArrayList<UUID>();
	private Location location;

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

}
