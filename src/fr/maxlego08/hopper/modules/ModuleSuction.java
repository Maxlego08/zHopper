package fr.maxlego08.hopper.modules;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.utils.ItemDecoder;
import fr.maxlego08.hopper.zcore.utils.Result;
import fr.maxlego08.hopper.zcore.utils.inventory.Button;

public class ModuleSuction extends Module {

	public ModuleSuction(int priority) {
		super("Suction", priority);
		this.runAsync = Config.runModuleSuctionAsync;
	}

	@Override
	public void execute(Hopper hopper, Level level) {

		int distance = level.getMaxDistanceSuction();

		World world = hopper.getWorld();
		Inventory inventory = hopper.toBukkitHopper().getInventory();

		if (distance <= 0 || world == null)
			return;

		Collection<Entity> entities;

		if (ItemDecoder.getNMSVersion() == 1.7)
			entities = world.getEntitiesByClasses(Entity.class).stream()
					.filter(e -> e.getLocation().distance(hopper.getLocation()) <= distance)
					.collect(Collectors.toList());
		else
			entities = world.getNearbyEntities(hopper.getLocation(), distance, distance, distance);

		// On r�cup�re tout les items valides dans un certain rayon
		Stream<Item> stream = entities.stream().filter(entity -> entity instanceof Item && entity.isValid())
				.map(entity -> (Item) entity);
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
			if (freeSpaceInContainer != 0 && freeSpaceInContainer < amount)
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

	@Override
	public boolean isCooldown(Hopper hopper, Level level) {
		return super.isCooldown(hopper, "modulesuction", level.getLongAsProperty("milliSecondModuleSuction"));
	}

	@Override
	public Button getButton() {
		return Config.suctionButton;
	}

}
