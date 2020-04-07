package fr.maxlego08.hopper.nbt;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.zcore.utils.ItemDecoder;

public class NBTManager {

	private final double version = ItemDecoder.getNMSVersion();
	private final NBTListener listener;

	public NBTManager() {

		if (version == 1.8) {

		} else
			listener = null;
	}

	public void dropItem(Hopper hopper) {

	}

}
