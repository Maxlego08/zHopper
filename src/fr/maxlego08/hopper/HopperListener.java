package fr.maxlego08.hopper;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.listener.ListenerAdapter;

public class HopperListener extends ListenerAdapter {

	private final HopperManager manager;

	public HopperListener(HopperManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	protected void onBlockBreak(BlockBreakEvent event, Player player, Block block) {
		
		if (block.getType().equals(Material.HOPPER))
			manager.destroyHopper(block, player, event);
		
	}

	@Override
	protected void onBlockPlace(BlockPlaceEvent event, Player player, Block block) {
		
		if (block.getType().equals(Material.HOPPER))
			manager.createHopper(block, player);
		
	}

}
