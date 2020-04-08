package fr.maxlego08.hopper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.api.events.HopperLinkEvent;
import fr.maxlego08.hopper.api.events.HopperOpenConfigurationEvent;
import fr.maxlego08.hopper.zcore.enums.Inventory;
import fr.maxlego08.hopper.zcore.enums.Message;
import fr.maxlego08.hopper.zcore.utils.Result;
import fr.maxlego08.hopper.zcore.utils.ZUtils;

public class HopperObject extends ZUtils implements Hopper {

	private UUID owner;
	private List<UUID> whitelistPlayers = new ArrayList<UUID>();
	private Location location;
	private int level = 1;
	private List<Location> linkedContainers = new ArrayList<>();
	private transient Level levelObject;
	private transient HopperManager hopperManager;

	/**
	 * 
	 * @param owner
	 * @param location
	 * @param hopperManager
	 * @param level
	 */
	public HopperObject(UUID owner, Location location, HopperManager hopperManager, int level) {
		super();
		this.owner = owner;
		this.whitelistPlayers = new ArrayList<UUID>();
		this.location = location;
		this.hopperManager = hopperManager;
		this.level = level;
		this.linkedContainers = new ArrayList<>();
	}

	/**
	 * 
	 * @param hopperManager
	 */
	public void initHopper(HopperManager hopperManager) {
		this.hopperManager = hopperManager;
	}

	@Override
	public UUID getOwner() {
		return owner;
	}

	@Override
	public List<UUID> getWhitelistPlayers() {
		return whitelistPlayers;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public Block getBlock() {
		return location == null ? null : location.getBlock();
	}

	@Override
	public org.bukkit.block.Hopper toBukkitHopper() {
		return location == null ? null : (org.bukkit.block.Hopper) location.getBlock().getState();
	}

	@Override
	public World getWorld() {
		return location == null ? null : location.getWorld();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HopperObject [owner=" + owner + ", whitelistPlayers=" + whitelistPlayers + ", location=" + location
				+ "]";
	}

	@Override
	public void openConfiguration(Player player) {

		HopperOpenConfigurationEvent event = new HopperOpenConfigurationEvent(this, player);
		event.callEvent();

		if (event.isCancelled())
			return;

		createInventory(player, Inventory.INVENTORY_CONFIGURATION, this);
	}

	@Override
	public int getLevel() {
		return level;
	}

	@Override
	public void setLevel(int level) {
		this.level = level;
		this.levelObject = this.hopperManager.getLevel(level);
	}

	@Override
	public Level toLevel() {
		return levelObject == null ? levelObject = hopperManager.getLevel(level) : levelObject;
	}

	@Override
	public HopperManager getManager() {
		return hopperManager;
	}

	@Override
	public void destroy() {
		if (location != null)
			getBlock().setType(Material.AIR);
	}

	@Override
	public List<Location> getLinkedContainers() {
		return linkedContainers;
	}

	@Override
	public boolean isValid() {
		return location != null && getBlock().getType().equals(Material.HOPPER);
	}

	@Override
	public void linkContainer(Player player, Block block) {

		if (location == null)
			return;

		if (!block.getLocation().getWorld().equals(location.getWorld())
				|| block.getLocation().distance(location) > toLevel().getMaxDistanceLink()) {
			message(player, Message.HOPPER_LINK_ERROR_DISTANCE, toLevel().getMaxDistanceLink());
			return;
		}

		HopperLinkEvent event = new HopperLinkEvent(player, this, block);
		event.callEvent();

		if (event.isCancelled())
			return;

		linkedContainers.add(block.getLocation());
		message(player, Message.HOPPER_LINK_SUCCESS);
	}

	@Override
	public boolean canLink() {
		return linkedContainers.size() >= toLevel().getMaxLink();
	}

	@Override
	public void run() {

		int maxItemPerSecond = toLevel().getMaxItemPerSecond();
		Iterator<Location> iterator = this.linkedContainers.iterator();

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

				org.bukkit.block.Hopper hopper = this.toBukkitHopper();

				// On parcours la liste des items dans le hopper
				for (ItemStack itemStack : hopper.getInventory().getContents()) {

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
								hopper.getInventory().remove(itemStack);
							else
								itemStack.setAmount(amount - toRemove);
							inventory.addItem(clone);

						}
					}
				}
			}
		}
	}

	/**
	 * Get max space in inventory
	 * 
	 * @param inventory
	 * @param itemStack
	 * @return
	 */
	private Result getFreeSpaceFor(org.bukkit.inventory.Inventory inventory, ItemStack itemStack, int amount) {
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
