package fr.maxlego08.hopper.modules;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.utils.inventory.Button;

public class ModuleBlockBreak extends Module {

	public ModuleBlockBreak(int priority) {
		super("BlockBreak", priority);
		runAsync = false;
	}

	@Override
	public void execute(Hopper hopper, Level level) {

		Location location = hopper.getLocation();

		if (location == null)
			return;

		int nb = level.getIntegerAsProperty("maxDistanceBlock");

		if (nb < 1)
			return;

		Block block = location.getBlock().getRelative(BlockFace.UP);
		for (int a = 0; a != nb; a++) {

			if (!Config.isBlacklist(block))
				block.breakNaturally();
			block = block.getRelative(BlockFace.UP);
		}

	}

	@Override
	public boolean isCooldown(Hopper hopper, Level level) {
		return super.isCooldown(hopper, "moduleblockbreak", level.getLongAsProperty("milliSecondModuleBlock"));
	}

	@Override
	public Button getButton() {
		return Config.blockButton;
	}

}
