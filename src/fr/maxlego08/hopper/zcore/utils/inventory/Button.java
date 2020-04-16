package fr.maxlego08.hopper.zcore.utils.inventory;

import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.maxlego08.hopper.zcore.utils.ZUtils;

public class Button extends ZUtils {

	private final int slot;
	private final String name;
	private final int item;
	private final int data;
	private final List<String> lore;

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
		item.setItemMeta(itemM);
		return item;
	}

	public ItemStack getInitButton(String type, String replace) {
		ItemStack item = getItem();
		ItemMeta itemM = item.getItemMeta();
		if (hasName())
			itemM.setDisplayName(getName().replace(type, replace));
		if (hasLore())
			itemM.setLore(getLore());
		item.setItemMeta(itemM);
		return item;
	}

}
