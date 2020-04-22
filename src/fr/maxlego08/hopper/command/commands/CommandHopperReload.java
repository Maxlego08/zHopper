package fr.maxlego08.hopper.command.commands;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.command.VCommand;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.enums.Permission;
import fr.maxlego08.hopper.zcore.utils.commands.CommandType;

public class CommandHopperReload extends VCommand {

	public CommandHopperReload() {
		this.addSubCommand("reload");
		this.setPermission(Permission.ZHOPPER_RELOAD);
	}

	@Override
	protected CommandType perform(HopperPlugin main) {

		main.getSavers().forEach(load -> load.load(main.getPersist()));
		main.getHopperManager().updateLevel();
		if (Config.closeInventoryOnReload)
			main.getInventoryManager().close();
		else
			main.getInventoryManager().update();

		message(sender, "§aReloaded !");

		return CommandType.SUCCESS;
	}

}
