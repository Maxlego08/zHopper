package fr.maxlego08.hopper.modules;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.zcore.utils.Result;

public class ModuleSuction extends Module {

	public ModuleSuction(int priority) {
		super(priority);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Hopper hopper, Level level) {

		int distance = level.getMaxDistanceSuction();

		World world = hopper.getWorld();
		Inventory inventory = hopper.toBukkitHopper().getInventory();

		if (distance <= 0 || world == null)
			return;

		// On récupère tout les items valides dans un certain rayon
		Stream<Item> stream = world.getNearbyEntities(hopper.getLocation(), distance, distance, distance).stream()
				.filter(entity -> entity instanceof Item && entity.isValid()).map(entity -> (Item) entity);
		List<Item> items = stream.collect(Collectors.toList());
		items.forEach(item -> {

			ItemStack itemStack = item.getItemStack();
			ItemStack clone = itemStack.clone();

			int defaultAmount = itemStack.getAmount();
			int amount = itemStack.getAmount();

			Result result = getFreeSpaceFor(inventory, itemStack, amount);
			int freeSpaceInContainer = result.getFreeSpace();

			if (freeSpaceInContainer != 0 && freeSpaceInContainer < amount)
				amount = freeSpaceInContainer;

			if (result.getEmptySlot() == 0 && freeSpaceInContainer == 0)
				return;

			int toRemove = defaultAmount - amount;

			if (toRemove <= 0)
				item.remove();
			else {
				itemStack.setAmount(toRemove);
				clone.setAmount(amount);
			}

			inventory.addItem(clone);

		});

	}

	@Override
	public boolean isCooldown(Hopper hopper, Level level) {
		return super.isCooldown(hopper, "modulesuction", level.getLongAsProperty("milliSecondModuleSuction"));
	}

}
