package fr.maxlego08.hopper.modules;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.wildstacker.api.WildStackerAPI;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.utils.Result;
import fr.maxlego08.hopper.zcore.utils.inventory.Button;

public class ModuleSuction extends Module {

	private final boolean isWildStacker = Bukkit.getPluginManager().isPluginEnabled("WildStacker");

	public ModuleSuction(int priority) {
		super("Suction", priority);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Hopper hopper, Level level) {

		int distance = level.getMaxDistanceSuction();

		World world = hopper.getWorld();
		Inventory inventory = hopper.toBukkitHopper().getInventory();

		if (distance <= 0 || world == null)
			return;

		// On r�cup�re tout les items valides dans un certain rayon
		Stream<Item> stream = world.getNearbyEntities(hopper.getLocation(), distance, distance, distance).stream()
				.filter(entity -> entity instanceof Item && entity.isValid()).map(entity -> (Item) entity);
		List<Item> items = stream.collect(Collectors.toList());
		items.forEach(item -> {

			ItemStack itemStack = item.getItemStack();
			ItemStack clone = itemStack.clone();

			int defaultAmount = getAmount(item);
			int amount = defaultAmount;

			Result result = getFreeSpaceFor(inventory, itemStack, amount);
			int freeSpaceInContainer = result.getFreeSpace();

			if (freeSpaceInContainer != 0 && freeSpaceInContainer < amount)
				amount = freeSpaceInContainer;

			int maxAmount = result.getEmptySlot() * 64;
			if (amount > maxAmount)
				amount = maxAmount;
			
			if (result.getEmptySlot() == 0 && freeSpaceInContainer == 0)
				return;

			int toRemove = defaultAmount - amount;

			if (toRemove == 0)
				item.remove();
			else
				setAmount(item, toRemove);

			clone.setAmount(amount);
			inventory.addItem(clone);

		});

	}

	/**
	 * 
	 * @param item
	 * @return
	 */
	private int getAmount(Item item) {
		if (isWildStacker)
			return WildStackerAPI.getItemAmount(item);
		else
			return item.getItemStack().getAmount();
	}

	private void setAmount(Item item, int toRemove) {
		if (isWildStacker)
			WildStackerAPI.getStackedItem(item).setStackAmount(toRemove, true);
		else
			item.getItemStack().setAmount(toRemove);
	}
	
	@Override
	public boolean isCooldown(Hopper hopper, Level level) {
		return super.isCooldown(hopper, "modulesuction", level.getLongAsProperty("milliSecondModuleSuction"));
	}

	@Override
	public Button getButton() {
		return Config.suctionButton;
	}

}
