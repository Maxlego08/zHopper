package fr.maxlego08.hopper.command.commands;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.command.VCommand;
import fr.maxlego08.hopper.zcore.utils.commands.CommandType;

public class CommandHopperVersion extends VCommand {

	public CommandHopperVersion() {
		this.addSubCommand("version");
		this.addSubCommand("v");
		this.addSubCommand("ver");
	}

	@Override
	protected CommandType perform(HopperPlugin main) {
		
		message(sender, "§eVersion du plugin§7: §a" + main.getDescription().getVersion());
		message(sender, "§eAuteur§7: §aMaxlego08");
		message(sender, "§eDiscord§7: §ahttps://discord.groupez.dev");
		message(sender, "§aBuy it for §d10€§7: §2https://groupez.dev/resources/6");
		
		return CommandType.SUCCESS;
	}

}
