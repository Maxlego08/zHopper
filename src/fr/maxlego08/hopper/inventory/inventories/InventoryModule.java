package fr.maxlego08.hopper.inventory.inventories;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.exceptions.InventoryOpenException;
import fr.maxlego08.hopper.inventory.InventoryResult;
import fr.maxlego08.hopper.inventory.VInventory;
import fr.maxlego08.hopper.zcore.utils.inventory.Button;

public class InventoryModule extends VInventory {

	public InventoryModule() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public InventoryResult openInventory(HopperPlugin main, Player player, int page, Object... args)
			throws InventoryOpenException {

		Hopper hopper = (Hopper) args[0];
		Level level = hopper.toLevel();

		createInventory("§eModules", 27);

		level.getModules().forEach(module -> {
			Button button = module.getButton();
			addItem(button.getSlot(), button.getInitButton()).setClick(event -> {

			});
		});

		return InventoryResult.SUCCESS;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, HopperPlugin plugin, Player player) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onDrag(InventoryDragEvent event, HopperPlugin plugin, Player player) {
		// TODO Auto-generated method stub

	}

}
