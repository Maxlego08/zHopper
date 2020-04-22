package fr.maxlego08.hopper.command.commands;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.command.VCommand;
import fr.maxlego08.hopper.zcore.enums.Inventory;
import fr.maxlego08.hopper.zcore.enums.Permission;
import fr.maxlego08.hopper.zcore.utils.commands.CommandType;

public class CommandHopperConfig extends VCommand {

	public CommandHopperConfig() {
		this.addSubCommand("config");
		this.setConsoleCanUse(false);
		this.setPermission(Permission.ZHOPPER_CONFIG);
	}

	@Override
	protected CommandType perform(HopperPlugin main) {
		createInventory(player, Inventory.INVENTORY_CONFIG.getId());
		return CommandType.SUCCESS;
	}

}
