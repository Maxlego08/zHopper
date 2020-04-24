package fr.maxlego08.hopper.zcore.enums;

import org.bukkit.Material;

import fr.maxlego08.hopper.save.Config;

public enum Options {

	MODULE_SUCTION,

	MODULE_LINK_CHEST(Material.CHEST),

	MODULE_BLOCK_BREAK(Material.STONE),

	MODULE_KILL_MOB(Material.DIAMOND_SWORD),

	MODULE_ITEM_TRANSFERT(Material.IRON_INGOT),
	
	CLOSE_INVENTORY_ON_RELOAD,
	
	USE_LEVEL_FOR_EVERY_HOPPER,
	
	GIVE_CUSTOM_HOPPER_IF_LEVEL_IS_DEFAULT,

	;

	private Material material = Material.PAPER;

	private Options() {
	}

	private Options(Material material) {
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}

	public void toggle() {
		switch (this) {
		case MODULE_BLOCK_BREAK:
			Config.enableModuleBlockBreak = !Config.enableModuleBlockBreak;
			break;
		case MODULE_ITEM_TRANSFERT:
			Config.enableModuleItemTransfert = !Config.enableModuleItemTransfert;
			break;
		case MODULE_KILL_MOB:
			Config.enableModuleKillMob = !Config.enableModuleKillMob;
			break;
		case MODULE_LINK_CHEST:
			Config.enableModuleLinkChest = !Config.enableModuleLinkChest;
			break;
		case MODULE_SUCTION:
			Config.enableModuleSuction = !Config.enableModuleSuction;
			break;
		case CLOSE_INVENTORY_ON_RELOAD:
			Config.closeInventoryOnReload = !Config.closeInventoryOnReload;
			break;
		case USE_LEVEL_FOR_EVERY_HOPPER:
			Config.useLevelForEveryHopper = !Config.useLevelForEveryHopper;
			break;
		case GIVE_CUSTOM_HOPPER_IF_LEVEL_IS_DEFAULT:
			Config.giveCustomHopperIfLevelIsDefault = !Config.giveCustomHopperIfLevelIsDefault;
			break;
		default:
			break;
		}
	}

	public boolean isToggle() {
		switch (this) {
		case MODULE_BLOCK_BREAK:
			return Config.enableModuleBlockBreak;
		case MODULE_ITEM_TRANSFERT:
			return Config.enableModuleItemTransfert;
		case MODULE_KILL_MOB:
			return Config.enableModuleKillMob;
		case MODULE_LINK_CHEST:
			return Config.enableModuleLinkChest;
		case MODULE_SUCTION:
			return Config.enableModuleSuction;
		case CLOSE_INVENTORY_ON_RELOAD:
			return Config.closeInventoryOnReload;
		case USE_LEVEL_FOR_EVERY_HOPPER:
			return Config.useLevelForEveryHopper;
		case GIVE_CUSTOM_HOPPER_IF_LEVEL_IS_DEFAULT:
			return Config.giveCustomHopperIfLevelIsDefault;
		default:
			break;
		}
		return false;
	}

}
