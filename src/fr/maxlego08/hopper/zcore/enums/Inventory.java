package fr.maxlego08.hopper.zcore.enums;

public enum Inventory {

	INVENTORY_TEST(1),
	INVENTORY_CONFIGURATION(2),
	INVENTORY_MODULE(3),
	INVENTORY_CONFIG(4),
	
	;
	
	private final int id;

	private Inventory(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

}
