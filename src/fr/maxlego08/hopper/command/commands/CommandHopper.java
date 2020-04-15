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

		message(sender, "§6zHopper§e, développé par Maxlego08.");

		return CommandType.SUCCESS;
	}

}
