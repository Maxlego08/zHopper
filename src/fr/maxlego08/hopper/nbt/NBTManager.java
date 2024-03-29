package fr.maxlego08.hopper.nbt;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.api.EnumVersion;
import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.nbt.versions.NBT1_10;
import fr.maxlego08.hopper.nbt.versions.NBT1_11;
import fr.maxlego08.hopper.nbt.versions.NBT1_12;
import fr.maxlego08.hopper.nbt.versions.NBT1_13;
import fr.maxlego08.hopper.nbt.versions.NBT1_14;
import fr.maxlego08.hopper.nbt.versions.NBT1_15;
import fr.maxlego08.hopper.nbt.versions.NBT1_16;
import fr.maxlego08.hopper.nbt.versions.NBT1_16_R3;
import fr.maxlego08.hopper.nbt.versions.NBT1_7;
import fr.maxlego08.hopper.nbt.versions.NBT1_8;
import fr.maxlego08.hopper.nbt.versions.NBT1_9;
import fr.maxlego08.hopper.zcore.utils.ItemDecoder;

public class NBTManager {

	private final double version = ItemDecoder.getNMSVersion();
	private final NBTListener listener;

	public NBTManager() {

		if (version == 1.8) {

			listener = new NBT1_8();
		} else if (version == 1.15) {
			listener = new NBT1_15();
		} else if (version == 1.16) {

			EnumVersion enumVersion = ItemDecoder.getVersion();
			if (enumVersion.equals(EnumVersion.V_16_R2))
				listener = new NBT1_16();
			else
				listener = new NBT1_16_R3();

		} else if (version == 1.14) {
			listener = new NBT1_14();
		} else if (version == 1.13) {
			listener = new NBT1_13();
		} else if (version == 1.12) {
			listener = new NBT1_12();
		} else if (version == 1.11) {
			listener = new NBT1_11();
		} else if (version == 1.10) {
			listener = new NBT1_10();
		} else if (version == 1.9) {
			listener = new NBT1_9();
		} else if (version == 1.7) {
			listener = new NBT1_7();
		} else
			listener = null;
	}

	public ItemStack createItemStack(Hopper hopper) {
		return listener.createItemStack(hopper);
	}

	@SuppressWarnings("deprecation")
	public int createHopper(Player player) {
		return listener.createHopper(player.getItemInHand());
	}

}
