package fr.maxlego08.hopper;

import fr.maxlego08.hopper.api.FakeHopper;
import fr.maxlego08.hopper.api.HopperManager;

public class HopperFakeObject extends HopperObject implements FakeHopper{

	/**
	 * 
	 * @param hopperManager
	 * @param level
	 */
	public HopperFakeObject(HopperManager hopperManager, int level) {
		super(null, null, hopperManager, level);
	}
	
	@Override
	public boolean isFake() {
		return true;
	}

}
