package fr.maxlego08.hopper.modules;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.utils.Result;
import fr.maxlego08.hopper.zcore.utils.inventory.Button;

public class ModuleLinkContainer extends Module {

	public ModuleLinkContainer(int p) {
		super("Container", p);
	}

	@Override
	public void execute(Hopper hopper, Level level) {

		int maxItemPerSecond = level.getMaxItemPerSecond();
		
		if (maxItemPerSecond == 0)
			return;
		
		Iterator<Location> iterator = hopper.getLinkedContainers().iterator();

		// On parcours la liste des location
		while (iterator.hasNext()) {

			Location location = iterator.next();

			// On verifie si la location n'est pas null, si elle est null alors
			// on la retire d'ici
			if (location == null)
				iterator.remove();
			else {

				// Si le nombre d'item par seconde à retiré est égal à 0 alors
				// pas besoin de continuer
				if (maxItemPerSecond == 0) {
					continue;
				}

				BlockState blockState = location.getBlock().getState();

				// On récupère l'inventaire en fonction du BlockState, sinon on
				// retire la location
				org.bukkit.inventory.Inventory inventory = null;
				if (blockState instanceof Chest) {
					Chest chest = (Chest) blockState;
					inventory = chest.getInventory();
				} else {
					iterator.remove();
					continue;
				}
				if (inventory == null) {
					continue;
				}

				org.bukkit.block.Hopper bukkitHopper = hopper.toBukkitHopper();

				// On parcours la liste des items dans le hopper
				for (ItemStack itemStack : bukkitHopper.getInventory().getContents()) {

					if (itemStack != null) {

						// Si il peut ajouter des items
						if (maxItemPerSecond > 0) {

							// On va clone en premier l'item a retirer
							ItemStack clone = itemStack.clone();

							// On récupère le nombre d'item de l'itemstack
							int amount = itemStack.getAmount();

							// On récupère le nombre d'item a retirer. Si le
							// nombre d'item est inférieur au nombre d'item dans
							// l'itemstack alors on prend le maxItemPerSecond
							// sinon
							// on prend le nombre d'itel dans l'itemstack
							int toRemove = maxItemPerSecond < amount ? maxItemPerSecond : amount;

							// On retire le nombre d'item a retirer dans
							// maxItemPerSecond

							Result result = getFreeSpaceFor(inventory, itemStack, toRemove);

							// On récupère la place dans l'inventaire
							int freeSpaceInContainer = result.getFreeSpace();

							// Si la place est différente de 0 est que la place
							// est inférieur au nombre d'item a retirer alors on
							// dit que la place restante sera le nombre d'item a
							// retirer
							if (freeSpaceInContainer != 0 && freeSpaceInContainer < toRemove)
								toRemove = freeSpaceInContainer;

							if (result.getEmptySlot() == 0 && freeSpaceInContainer == 0) {
								continue;
							}
							maxItemPerSecond -= toRemove;

							// On ajoute l'item dans l'inventaire siblé
							// On retirer l'item de l'inventaire
							clone.setAmount(toRemove);
							if (amount - toRemove <= 0)
								bukkitHopper.getInventory().remove(itemStack);
							else
								itemStack.setAmount(amount - toRemove);
							inventory.addItem(clone);

						}
					}
				}
			}
		}
	}


	@Override
	public boolean isCooldown(Hopper hopper, Level level) {
		return super.isCooldown(hopper, "modulecontainer", level.getLongAsProperty("milliSecondModuleItem"));
	}

	@Override
	public Button getButton() {
		return Config.linkButton;
	}
	
}
