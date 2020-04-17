package fr.maxlego08.hopper.zcore.enums;

public enum Message {

	PREFIX("§8(§ezHopper§8)"),
	PREFIX_END("§8(§ezHopper§8)", false),
	
	TELEPORT_MOVE("§cVous ne devez pas bouger !", false),
	TELEPORT_MESSAGE("§7Téléportatio dans §3%s §7secondes !", false),
	TELEPORT_ERROR("§cVous avez déjà une téléportation en cours !", false),
	TELEPORT_SUCCESS("§7Téléportation effectué !", false),
	
	INVENTORY_NULL("§cImpossible de trouver l'inventaire avec l'id §6%s§c.", false),
	INVENTORY_CLONE_NULL("§cLe clone de l'inventaire est null !", false),
	INVENTORY_OPEN_ERROR("§cUne erreur est survenu avec l'ouverture de l'inventaire §6%s§c.", false),
	INVENTORY_BUTTON_PREVIOUS("§f» §7Page précédente", false),
	INVENTORY_BUTTON_NEXT("§f» §7Page suivante", false),
	
	TIME_DAY("%02d jour(s) %02d heure(s) %02d minute(s) %02d seconde(s)", false),
	TIME_HOUR("%02d heure(s) %02d minute(s) %02d seconde(s)", false),
	TIME_HOUR_SIMPLE("%02d:%02d:%02d", false),
	TIME_MINUTE("%02d minute(s) %02d seconde(s)"),
	TIME_SECOND("%02d seconde(s)", false),
	
	COMMAND_SYNTAXE_ERROR("§cYou have to execute the command like this§7: §a%s"),
	COMMAND_NO_PERMISSION("§cYou do not have permission to execute this command."),
	COMMAND_NO_CONSOLE("§cOnly a player can execute this command."),
	COMMAND_NO_ARG("§cUnable to find the command with its arguments."),
	COMMAND_SYNTAXE_HELP("§a%s §b» §7%s"),
	
	HOPPER_CREATE("§eYou just added a hopper §8(§7To configure it you must left click on it§8)"),
	HOPPER_DESTROY("§eYou just broke your hopper"), 
	HOPPER_LEVEL_ERROR("§cYou have reached the maximum level to hopper"), 
	HOPPER_LEVEL_ERROR_MONEY("§cYou don't have enough money to improve the hopper."), 
	HOPPER_LEVEL_SUCCESS("§eYou have just taken your hopper to the level §f%s§e."), 
	HOPPER_LINK_START("§eeYou can connect a container to your hopper. §8(§7Just click on a chest or whatever§8)"), 
	HOPPER_LINK_ERROR("§cYou can no longer connect other chest."), 
	HOPPER_LINK_SUCCESS("§aYou just connected your hopper."), 
	HOPPER_LINK_ERROR_ALREADY("§cYou are already connecting a hopper with a chest."), 
	HOPPER_LINK_ERROR_CONTAINER("§cYou must click on a §fchest§c."), 
	HOPPER_LINK_ERROR_DISTANCE("§cYou must be less than §f%s§c block your hopper to link it."), 
	HOPPER_LINK_ERROR_ALREADY_LINK("§cYou have already linked this block."), 
	HOPPER_OPEN_ERROR("§cOnly the owner of the hopper can open it."), 
	
	CLIKC_TO_ENABLE("§aClick to enable"),
	CLIKC_TO_DISABLE("§cClick to disable"),
	
	PLAYERPOINT_CURRENCY("P"),
	VAULT_CURRENCY("$"),
	TOKENMANAGER_CURRENCY("T"),
	MYSQLTOKEN_CURRENCY("M"),
	
	;

	private String message;
	private boolean use = true;

	private Message(String message) {
		this.message = message;
	}

	private Message(String message, boolean use) {
		this.message = message;
		this.use = use;
	}

	public String getMessage() {
		return message;
	}

	public String toMsg() {
		return message;
	}

	public String msg() {
		return message;
	}
	public boolean isUse() {
		return use;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

}

