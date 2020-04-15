package fr.maxlego08.hopper;

import org.bukkit.scheduler.BukkitRunnable;

import fr.maxlego08.hopper.api.HopperManager;

public class HopperRunnable extends BukkitRunnable {

	private final HopperManager hopperManager;

	public HopperRunnable(HopperManager hopperManager) {
		super();
		this.hopperManager = hopperManager;
	}

	@Override
	public void run() {

		hopperManager.getHoppers().forEach(hopper -> {
			if (!hopper.isValid())
				hopperManager.deleteHopper(hopper);
			else
				hopper.run();
		});

	}

}
