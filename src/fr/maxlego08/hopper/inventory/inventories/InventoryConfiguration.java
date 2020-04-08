package fr.maxlego08.hopper.inventory.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.exceptions.InventoryOpenException;
import fr.maxlego08.hopper.inventory.InventoryResult;
import fr.maxlego08.hopper.inventory.VInventory;
import fr.maxlego08.hopper.zcore.utils.builder.ItemBuilder;

public class InventoryConfiguration extends VInventory {

	@Override
	public InventoryResult openInventory(HopperPlugin main, Player player, int page, Object... args)
			throws InventoryOpenException {

		Hopper hopper = (Hopper) args[0];
		HopperManager manager = hopper.getManager();
		Level level = hopper.toLevel();

		createInventory("§7Hopper", 27);

		ItemBuilder builder = new ItemBuilder(getMaterial(384), "§eNiveau du hopper");
		builder.addLine("");
		builder.addLine("§f§l» §eNiveau du hopper§6 " + level.getInteger());
		builder.addLine("§f§l» §eNom du niveau§6 " + level.getName());
		builder.addLine("§f§l» §eNombre de container maximum§6 " + level.getMaxLink());
		builder.addLine("§f§l» §eDistance maximum de link§6 " + level.getMaxDistanceLink() + " §eblocks");
		builder.addLine("");
		addItem(4, builder);

		builder = new ItemBuilder(Material.BARRIER, "§cRécupérer le hopper");
		builder.addLine("");
		builder.addLine("§f§l» §cClique pour récupérer le hopper");
		builder.addLine("");
		addItem(22, builder).setClick(event -> manager.destroyHopper(player, hopper));

		return InventoryResult.SUCCESS;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, HopperPlugin plugin, Player player) {

	}

	@Override
	protected void onDrag(InventoryDragEvent event, HopperPlugin plugin, Player player) {

	}

}