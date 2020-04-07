package fr.maxlego08.hopper.api;

public interface Level {

	/**
	 * Get level name
	 * @return name
	 */
	String getName();
	
	/**
	 * Get level to integer
	 * @return integer
	 */
	int getInteger();
	
	/**
	 * 
	 * @return
	 */
	int getMaxDistanceLink();
	
	/**
	 * 
	 * @return
	 */
	int getMaxLink();
	
	/**
	 * 
	 * @return
	 */
	Level next();
	
}
