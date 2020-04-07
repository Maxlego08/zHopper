package fr.maxlego08.hopper.nbt;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.nbt.versions.NBT1_8;
import fr.maxlego08.hopper.zcore.utils.ItemDecoder;

public class NBTManager {

	private final double version = ItemDecoder.getNMSVersion();
	private final NBTListener listener;

	public NBTManager() {

		if (version == 1.8) {

			listener = new NBT1_8();

		} else
			listener = null;
	}

	public void dropItem(Hopper hopper) {
		ItemStack itemStack = listener.createItemStack(hopper);
		hopper.getWorld().dropItem(hopper.getLocation(), itemStack);
	}

}
