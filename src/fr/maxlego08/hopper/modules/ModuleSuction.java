package fr.maxlego08.hopper.modules;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.World;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;

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

		Stream<Item> stream = world.getNearbyEntities(hopper.getLocation(), distance, distance, distance).stream()
				.filter(entity -> entity instanceof Item).map(entity -> (Item) entity);
		List<Item> items = stream.collect(Collectors.toList());
		items.forEach(item -> {
			
			if (isFull(inventory))
				return;
			
		});
	

	}

}
