package fr.maxlego08.hopper.nbt;

import org.bukkit.entity.Player;
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
		}else			if (version == 1.15) {
				
				listener = new NBT1_8();

		} else
			listener = null;
	}

	public ItemStack dropItem(Hopper hopper) {
		return listener.createItemStack(hopper);
	}

	@SuppressWarnings("deprecation")
	public int createHopper(Player player) {
		return listener.createHopper(player.getItemInHand());
	}

}
