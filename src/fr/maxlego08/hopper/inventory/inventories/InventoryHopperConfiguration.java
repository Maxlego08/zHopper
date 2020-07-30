package fr.maxlego08.hopper.inventory.inventories;

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
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.enums.Inventory;
import fr.maxlego08.hopper.zcore.utils.inventory.Button;

public class InventoryHopperConfiguration extends VInventory {

	@Override
	public InventoryResult openInventory(HopperPlugin main, Player player, int page, Object... args)
			throws InventoryOpenException {

		Hopper hopper = (Hopper) args[0];
		HopperManager manager = hopper.getManager();
		Level level = hopper.toLevel();

		createInventory(Config.inventoryHopperName, Config.inventoryHopperSize);

		addItem(Config.hopperInformationSlot, level.build()).setClick(event -> manager.updateLevel(hopper, player));

		Button button = Config.removeHopperButton;
		addItem(button.getSlot(), button.getInitButton()).setClick(event -> manager.destroyHopper(player, hopper));

		if (Config.enableModuleLinkChest) {
			button = Config.linkedChestButton;
			addItem(button.getSlot(), button.getButton(hopper)).setClick(event -> manager.linkHopper(player, hopper));
		}

		button = Config.moduleConfigurationButton;
		addItem(button.getSlot(), button.getInitButton()).setClick(event -> {
			createInventory(player, Inventory.INVENTORY_MODULE.getId(), page, hopper);
		});

		return InventoryResult.SUCCESS;
	}

	@Override
	protected void onClose(InventoryCloseEvent event, HopperPlugin plugin, Player player) {

	}

	@Override
	protected void onDrag(InventoryDragEvent event, HopperPlugin plugin, Player player) {

	}

}