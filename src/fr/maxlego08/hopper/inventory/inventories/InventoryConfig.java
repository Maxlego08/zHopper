package fr.maxlego08.hopper.inventory.inventories;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.exceptions.InventoryOpenException;
import fr.maxlego08.hopper.inventory.InventoryResult;
import fr.maxlego08.hopper.inventory.VInventory;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.enums.Options;
import fr.maxlego08.hopper.zcore.utils.builder.ItemBuilder;

public class InventoryConfig extends VInventory {

	@Override
	public InventoryResult openInventory(HopperPlugin main, Player player, int page, Object... args)
			throws InventoryOpenException {

		createInventory("§ezHopper - Config", 54);

		int slot = 0;
		for (Options option : Options.values()) {

			ItemBuilder builder = new ItemBuilder(option.getMaterial(), "§e" + name(option.name()));
			if (option.isToggle())
				builder.glow();
			builder.addLine("§7Click to %s§7.", !option.isToggle() ? "§aactivate" : "§cdeactivate");
			addItem(slot++, builder).setClick(event -> {
				option.toggle();
				main.getInventoryManager().update(player);
			});

		}

		addItem(49, new ItemBuilder(Material.NETHER_STAR, "§eReload").addLine("§7Reload the plugin configuration")
				.addLine("§7The modifications made will be taken into account during the reload")).setClick(event -> {

					main.getSavers().forEach(load -> load.save(main.getPersist()));
					main.getHopperManager().updateLevel();
					if (Config.closeInventoryOnReload)
						main.getInventoryManager().close();
					else
						main.getInventoryManager().update();
					
					message(player, "§eReload !");

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
