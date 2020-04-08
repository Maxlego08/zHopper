package fr.maxlego08.hopper.zcore.utils;

public class Result {

	private final int emptySlot;
	private final int freeSpace;

	public Result(int emptySlot, int freeSpace) {
		super();
		this.emptySlot = emptySlot;
		this.freeSpace = freeSpace;
	}

	/**
	 * @return the emptySlot
	 */
	public int getEmptySlot() {
		return emptySlot;
	}

	/**
	 * @return the freeSpace
	 */
	public int getFreeSpace() {
		return freeSpace;
	}

}
