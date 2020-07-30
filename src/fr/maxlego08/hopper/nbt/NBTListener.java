package fr.maxlego08.hopper.nbt;

import org.bukkit.inventory.ItemStack;

import fr.maxlego08.hopper.api.Hopper;

public interface NBTListener {

	/**
	 * 
	 * @param hopper
	 * @return
	 */
	ItemStack createItemStack(Hopper hopper);
	
	/**
	 * 
	 * @param itemStack
	 * @return
	 */
	int createHopper(ItemStack itemStack);
	
}
