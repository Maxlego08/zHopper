package fr.maxlego08.hopper.modules;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import fr.maxlego08.hopper.HopperZManager;
import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.api.events.HopperModuleRunEvent;
import fr.maxlego08.hopper.zcore.utils.Result;
import fr.maxlego08.hopper.zcore.utils.ZUtils;

public abstract class Module extends ZUtils {

	private final int priority;
	protected boolean runAsync = true;

	public Module(int priority) {
		super();
		this.priority = priority;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	public void preRun(Hopper hopper, Level level) {

		HopperModuleRunEvent event = new HopperModuleRunEvent(hopper, this, runAsync);
		event.callEvent();
		if (event.isCancelled())
			return;
			
		if (runAsync)
			execute(hopper, level);
		else {
			JavaPlugin plugin = ((HopperZManager) hopper.getManager()).getPlugin();
			Bukkit.getScheduler().runTask(plugin, () -> execute(hopper, level));
		}
	}

	public abstract void execute(Hopper hopper, Level level);

	/**
	 * Get max space in inventory
	 * 
	 * @param inventory
	 * @param itemStack
	 * @return
	 */
	protected Result getFreeSpaceFor(org.bukkit.inventory.Inventory inventory, ItemStack itemStack, int amount) {
		int maxSpace = 0;
		int emptySlot = 0;
		for (ItemStack itemStack2 : inventory.getContents()) {
			if (itemStack2 == null)
				emptySlot++;
			if (itemStack2 != null && itemStack2.isSimilar(itemStack)) {
				int space = 64 - itemStack2.getAmount();
				if (space >= amount)
					return new Result(emptySlot, amount);
				maxSpace = Math.max(maxSpace, space);
			}
		}
		return new Result(emptySlot, maxSpace);
	}

}
