package fr.maxlego08.hopper.zcore.utils.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.exceptions.MaterialException;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.utils.ItemDecoder;
import fr.maxlego08.hopper.zcore.utils.ZUtils;

public class Button extends ZUtils implements Cloneable {

	private final int slot;
	private final String name;
	private final int item;
	private final int data;
	private final List<String> lore;
	private boolean isGlow;

	public Button(int slot, String name, int item, int data, String... lore) {
		super();
		this.slot = slot;
		this.name = name;
		this.item = item;
		this.data = data;
		this.lore = Arrays.asList(lore);
	}

	public Button(int slot, String name, int item, int data, List<String> lore) {
		super();
		this.slot = slot;
		this.name = name;
		this.item = item;
		this.data = data;
		this.lore = lore;
	}

	public int getSlot() {
		return slot;
	}

	public String getName() {
		return name;
	}

	public List<String> getLore() {
		return lore;
	}

	public int getData() {
		return data;
	}

	public int getItemInInteger() {
		return item;
	}

	@SuppressWarnings("deprecation")
	public ItemStack getItem() {
		
		Material material = getMaterial(item);
		
		if (material == null)
			throw new MaterialException("item with id " + item + " doesn't exist !");
		
		return new ItemStack(getMaterial(item), 1, (byte) data);
	}

	public boolean hasLore() {
		return lore != null;
	}

	public boolean hasName() {
		return name != null;
	}

	public ItemStack getInitButton() {
		ItemStack item = getItem();
		ItemMeta itemM = item.getItemMeta();
		if (hasName())
			itemM.setDisplayName(getName());
		if (hasLore())
			itemM.setLore(getLore());
		if (isGlow && ItemDecoder.getNMSVersion() != 1.7) {
			
			itemM.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
			itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			
		}
		item.setItemMeta(itemM);
		return item;
	}

	public ItemStack getInitButton(String type, String replace) {
		ItemStack item = getItem();
		ItemMeta itemM = item.getItemMeta();
		if (hasName())
			itemM.setDisplayName(getName().replace(type, replace));
		if (hasLore()) {
			List<String> lore = new ArrayList<String>();
			getLore().forEach(line -> lore.add(line.replace(type, replace)));
			itemM.setLore(lore);
		}
		
		if (isGlow && ItemDecoder.getNMSVersion() != 1.7) {
			
			itemM.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
			itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			
		}
		item.setItemMeta(itemM);
		return item;
	}

	public ItemStack getButton(Hopper hopper) {

		ItemStack item = getItem();
		ItemMeta itemM = item.getItemMeta();
		if (hasName())
			itemM.setDisplayName(getName().replace("%amount%", String.valueOf(hopper.getLinkedContainers().size())));
		if (hasLore()) {
			List<String> lore = new ArrayList<String>();
			getLore().forEach(line -> {
				if (line.equalsIgnoreCase("%chestLocations%")) {
					for (Location location : hopper.getLinkedContainers())
						lore.add(Config.linkedChestLine.replace("%x%", String.valueOf(location.getBlockX()))
								.replace("%z%", String.valueOf(location.getBlockZ()))
								.replace("%y%", String.valueOf(location.getBlockY())));
				} else
					lore.add(line.replace("%amount%", String.valueOf(hopper.getLinkedContainers().size())));
			});
			itemM.setLore(lore);
		}

		if (isGlow && ItemDecoder.getNMSVersion() != 1.7) {

			itemM.addEnchant(Enchantment.ARROW_DAMAGE, 10, true);
			itemM.addItemFlags(ItemFlag.HIDE_ENCHANTS);

		}

		item.setItemMeta(itemM);
		return item;
	}

	@Override
	public Button clone() {
		try {
			return (Button) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void glow() {
		isGlow = true;
	}

	public void glowDown() {
		isGlow = false;
	}

}
