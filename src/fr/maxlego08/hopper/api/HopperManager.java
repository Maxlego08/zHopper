package fr.maxlego08.hopper.api;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import fr.maxlego08.hopper.zcore.utils.storage.Saveable;

public interface HopperManager extends Saveable{

	/**
	 * 
	 * @return
	 */
	List<Hopper> getHoppers();
	
	/**
	 * 
	 * @param location
	 * @return
	 */
	Hopper getHopper(Location location);
	
	/**
	 * 
	 * @param block
	 * @param player
	 */
	void createHopper(Block block, Player player);

	/**
	 * 
	 * @param block
	 * @param player
	 * @param event
	 */
	void destroyHopper(Block block, Player player, BlockBreakEvent event);
	
}
