package fr.maxlego08.hopper.modules;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.bgsoftware.wildstacker.api.WildStackerAPI;

import fr.maxlego08.hopper.HopperZManager;
import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.api.events.HopperModuleRunEvent;
import fr.maxlego08.hopper.zcore.ZPlugin;
import fr.maxlego08.hopper.zcore.utils.Result;
import fr.maxlego08.hopper.zcore.utils.ZUtils;
import fr.maxlego08.hopper.zcore.utils.inventory.Button;
import fr.maxlego08.zitemstacker.api.ItemManager;

public abstract class Module extends ZUtils {

	private final String name;
	private final int priority;
	protected boolean runAsync = true;
	protected Map<Hopper, Long> cooldows = new HashMap<Hopper, Long>();
	private ItemManager itemManager;
	
	protected final boolean isWildStacker = Bukkit.getPluginManager().isPluginEnabled("WildStacker");
	protected final boolean isZitemStacker = Bukkit.getPluginManager().isPluginEnabled("zItemStacker");

	public Module(String name, int priority) {
		super();
		this.priority = priority;
		this.name = name;
	}

	/**
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}

	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param hopper
	 * @param level
	 */
	public void preRun(Hopper hopper, Level level) {

		if (!hopper.isActive(this))
			return;

		if (isCooldown(hopper, level))
			return;

		HopperModuleRunEvent event = new HopperModuleRunEvent(hopper, this, level, runAsync);
		event.callEvent();
		if (event.isCancelled())
			return;

		if (runAsync)
			execute(hopper, level);
		else {
			Plugin plugin = ((HopperZManager) hopper.getManager()).getPlugin();
			Bukkit.getScheduler().runTask(plugin, () -> execute(hopper, level));
		}
	}

	/**
	 * 
	 * @param hopper
	 * @param level
	 */
	public abstract void execute(Hopper hopper, Level level);

	/**
	 * 
	 * @param hopper
	 * @param level
	 * @return
	 */
	public abstract boolean isCooldown(Hopper hopper, Level level);

	/**
	 * 
	 * @return
	 */
	public abstract Button getButton();

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

	protected ItemManager getItemManager() {
		return itemManager == null ? itemManager = ZPlugin.z().getProvider(ItemManager.class) : itemManager;
	}
	
	/**
	 * 
	 * @param item
	 * @return
	 */
	protected int getAmount(Item item) {
		if (isWildStacker)
			return WildStackerAPI.getItemAmount(item);
		else if (isZitemStacker) {

			ItemManager itemManager = getItemManager();
			return itemManager.getItemAmount(item);

		} else
			return item.getItemStack().getAmount();
	}

	protected void setAmount(Item item, int toRemove) {
		if (isWildStacker)
			WildStackerAPI.getStackedItem(item).setStackAmount(toRemove, true);
		else if (isZitemStacker) {
			
			ItemManager itemManager = getItemManager();
			itemManager.setAmount(item, toRemove);
			
		} else
			item.getItemStack().setAmount(toRemove);
	}

}
