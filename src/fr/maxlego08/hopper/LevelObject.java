package fr.maxlego08.hopper;

import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.economy.Economy;
import fr.maxlego08.hopper.zcore.utils.ZUtils;
import fr.maxlego08.hopper.zcore.utils.builder.ItemBuilder;

public class LevelObject extends ZUtils implements Level {

	private final String name;
	private final int level;
	private final int maxDistanceLink;
	private final int maxLink;
	private final int maxItemPerSecond;
	private final long price;
	private final Economy economy;
	private HopperManager hopperManager;

	/**
	 * 
	 * @param name
	 * @param level
	 * @param maxDistanceLink
	 * @param maxLink
	 * @param maxItemPerSecond
	 * @param price
	 * @param economy
	 */
	public LevelObject(String name, int level, int maxDistanceLink, int maxLink, int maxItemPerSecond, long price,
			Economy economy) {
		super();
		this.name = name;
		this.level = level;
		this.maxDistanceLink = maxDistanceLink;
		this.maxLink = maxLink;
		this.maxItemPerSecond = maxItemPerSecond;
		this.price = price;
		this.economy = economy;
	}

	/**
	 * 
	 * @param hopperManager
	 */
	public void setHopperManager(HopperManager hopperManager) {
		this.hopperManager = hopperManager;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the level
	 */
	public int getInteger() {
		return level;
	}

	/**
	 * @return the maxDistanceLink
	 */
	public int getMaxDistanceLink() {
		return maxDistanceLink;
	}

	/**
	 * @return the maxLink
	 */
	public int getMaxLink() {
		return maxLink;
	}

	@Override
	public Level next() {
		return hopperManager.next(level);
	}

	@Override
	public ItemBuilder build() {

		Level next = next();

		ItemBuilder builder = new ItemBuilder(getMaterial(384), "§eNiveau du hopper");
		builder.addLine("");
		builder.addLine("§f§l» §eNiveau du hopper§6 " + getInteger());
		builder.addLine("§f§l» §eNom du niveau§6 " + getName());
		builder.addLine("§f§l» §eNombre de container maximum§6 " + getMaxLink());
		builder.addLine("§f§l» §eDistance maximum de link§6 " + getMaxDistanceLink() + " §eblocks");

		if (next != null) {
			builder.addLine("");
			builder.addLine("§f§l» §eProchain niveau§b " + next.getInteger());
			builder.addLine("§f§l» §eNom du niveau§b " + next.getName());
			builder.addLine("§f§l» §eNombre de container maximum§b " + next.getMaxLink());
			builder.addLine("§f§l» §eDistance maximum de link§b " + next.getMaxDistanceLink() + " §eblocks");
			builder.addLine("§f§l» §ePrix§b " + next.getPrice() + next.getEconomy().toCurrency());
		}

		builder.addLine("");

		return builder;
	}

	@Override
	public boolean isDefault() {
		return level == 1;
	}

	@Override
	public long getPrice() {
		return price;
	}

	@Override
	public Economy getEconomy() {
		return economy;
	}

	@Override
	public int getMaxItemPerSecond() {
		return maxItemPerSecond;
	}

}
