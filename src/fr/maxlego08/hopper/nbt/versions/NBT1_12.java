package fr.maxlego08.hopper.nbt.versions;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.nbt.NBTListener;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.utils.builder.ItemBuilder;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

public class NBT1_12 implements NBTListener {

	@Override
	public ItemStack createItemStack(Hopper hopper) {

		ItemBuilder itemStack = new ItemBuilder(Material.HOPPER);

		if (hopper == null || hopper.getLevel() <= 1 && !Config.giveCustomHopperIfLevelIsDefault)
			return itemStack.build();

		itemStack.setName(Config.hopperName);
		Config.hopperLore.forEach(lore -> itemStack.addLine(lore, hopper.getLevel()));

		net.minecraft.server.v1_12_R1.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack.build());
		NBTTagCompound compound = itemStackNMS.getTag();
		compound.setInt("level", hopper.getLevel());
		itemStackNMS.setTag(compound);

		return CraftItemStack.asBukkitCopy(itemStackNMS);
	}

	@Override
	public int createHopper(ItemStack itemStack) {
		net.minecraft.server.v1_12_R1.ItemStack itemStackNMS = CraftItemStack.asNMSCopy(itemStack);
		NBTTagCompound compound = itemStackNMS.getTag();
		return compound == null ? 0 : compound.hasKey("level") ? compound.getInt("level") : 0;
	}

}
