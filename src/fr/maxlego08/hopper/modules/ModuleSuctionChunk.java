package fr.maxlego08.hopper.modules;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.bgsoftware.wildstacker.api.WildStackerAPI;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.utils.Result;
import fr.maxlego08.hopper.zcore.utils.inventory.Button;

public class ModuleSuctionChunk extends Module {

	private final boolean isWildStacker = Bukkit.getPluginManager().isPluginEnabled("WildStacker");

	/**
	 * 
	 * @param priority
	 */
	public ModuleSuctionChunk(int p) {
		super("SuctionChunk", p);
		this.runAsync = Config.runModuleSuctionChunkAsync;
	}

	@Override
	public void execute(Hopper hopper, Level level) {

		World world = hopper.getWorld();
		Location location = hopper.getLocation();
		Inventory inventory = hopper.toBukkitHopper().getInventory();

		if (world == null || location == null)
			return;

		Predicate<Entity> predicate = entity -> entity.getLocation().getChunk().equals(location.getChunk())
				&& entity instanceof Item && entity.isValid();
		
		Stream<Item> stream = world.getEntities().stream().filter(predicate).map(entity -> (Item) entity);

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
			if (amount > maxAmount && maxAmount != 0)
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
		return super.isCooldown(hopper, "modulesuctionchunk", level.getLongAsProperty("milliSecondModuleSuctionChunk"));
	}

	@Override
	public Button getButton() {
		return Config.suctionChunkButton;
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

	/**
	 * 
	 * @param item
	 * @param toRemove
	 */
	private void setAmount(Item item, int toRemove) {
		if (isWildStacker)
			WildStackerAPI.getStackedItem(item).setStackAmount(toRemove, true);
		else
			item.getItemStack().setAmount(toRemove);
	}

}
