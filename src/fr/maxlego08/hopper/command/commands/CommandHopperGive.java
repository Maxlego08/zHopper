package fr.maxlego08.hopper.command.commands;

import org.bukkit.entity.Player;

import fr.maxlego08.hopper.HopperPlugin;
import fr.maxlego08.hopper.command.VCommand;
import fr.maxlego08.hopper.zcore.enums.Permission;
import fr.maxlego08.hopper.zcore.utils.commands.CommandType;

public class CommandHopperGive extends VCommand {

	public CommandHopperGive() {
		this.setPermission(Permission.ZHOPPER_GIVE);
		this.addSubCommand("give");
		
		this.addRequireArg("player");
		this.addRequireArg("level");
	}

	@Override
	protected CommandType perform(HopperPlugin main) {
		
		Player player = argAsPlayer(0);
		int level = argAsInteger(1);
		main.getHopperManager().giveHopper(sender, player, level);
		
		return CommandType.SUCCESS;
	}

}
