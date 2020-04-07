package fr.maxlego08.hopper.nbt.versions;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.HopperObject;
import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.nbt.NBTListener;
import fr.maxlego08.hopper.zcore.utils.builder.ItemBuilder;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

public class NBT1_8 implements NBTListener {

	@Override
	public ItemStack createItemStack(Hopper hopper) {

		ItemBuilder itemStack = new ItemBuilder(Material.HOPPER);

		if (hopper == null || hopper.getLevel() <= 1)
			return itemStack.build();

		itemStack.setName("�eHopper");
		itemStack.addLine("�f�l� �7Niveau du hopper: �6%s", hopper.getLevel());

		net.minecraft.server.v1_8_R3.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack.build());
		NBTTagCompound compound = itemStackNMS.getTag();
		compound.setInt("level", hopper.getLevel());
		itemStackNMS.setTag(compound);

		return CraftItemStack.asBukkitCopy(itemStackNMS);
	}

	@Override
	public Hopper getHopper(ItemStack itemStack) {

		net.minecraft.server.v1_8_R3.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound compound = itemStackNMS.getTag();

		if (compound.hasKey("level")) {

			return new HopperObject(compound.getInt("level"));

		}

		return null;
	}

}