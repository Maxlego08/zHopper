package fr.maxlego08.hopper.zcore.enums;

public enum Inventory {

	INVENTORY_TEST(1),
	INVENTORY_CONFIGURATION(2),
	
	;
	
	private final int id;

	private Inventory(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
