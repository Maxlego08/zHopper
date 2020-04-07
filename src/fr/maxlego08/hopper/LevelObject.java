package fr.maxlego08.hopper;

import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.zcore.utils.ZUtils;

public class LevelObject extends ZUtils implements Level {

	private final String name;
	private final int level;
	private final int maxDistanceLink;
	private final int maxLink;

	/**
	 * 
	 * @param name
	 * @param level
	 * @param maxDistanceLink
	 * @param maxLink
	 */
	public LevelObject(String name, int level, int maxDistanceLink, int maxLink) {
		super();
		this.name = name;
		this.level = level;
		this.maxDistanceLink = maxDistanceLink;
		this.maxLink = maxLink;
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
		// TODO Auto-generated method stub
		return null;
	}
	
}
