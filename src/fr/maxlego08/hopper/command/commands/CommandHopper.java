package fr.maxlego08.hopper.command.commands;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.command.VCommand;
import fr.maxlego08.hopper.zcore.utils.commands.CommandType;

public class CommandHopper extends VCommand {

	public CommandHopper() {
		this.addSubCommand(new CommandHopperVersion());
		this.addSubCommand(new CommandHopperReload());
	}
	
	
	@Override
	protected CommandType perform(HopperPlugin main) {

		message(sender, "§6zHopper§e, create by Maxlego08");
		message(sender, "§6WIKI§e: https://github.com/Maxlego08/zHopper-API/wiki");

		return CommandType.SUCCESS;
	}

}
