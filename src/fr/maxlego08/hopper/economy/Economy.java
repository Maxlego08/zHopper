package fr.maxlego08.hopper.economy;

import org.bukkit.Bukkit;

import fr.maxlego08.hopper.api.events.EconomyCurrencyEvent;
import fr.maxlego08.hopper.zcore.enums.Message;

public enum Economy {

	VAULT, PLAYERPOINT, TOKENMANAGER, MYSQLTOKEN, CUSTOM,

	;

	public static Economy getOrDefault(String string, Economy eco) {
		for (Economy economy : values())
			if (string.equalsIgnoreCase(economy.name()))
				return economy;
		return eco;
	}

	public String toCurrency() {
		switch (this) {
		case PLAYERPOINT:
			return Message.PLAYERPOINT_CURRENCY.getMessage();
		case VAULT:
			return Message.VAULT_CURRENCY.getMessage();
		case TOKENMANAGER:
			return Message.TOKENMANAGER_CURRENCY.getMessage();
		case MYSQLTOKEN:
			return Message.MYSQLTOKEN_CURRENCY.getMessage();
		case CUSTOM:
			EconomyCurrencyEvent event = new EconomyCurrencyEvent();
			Bukkit.getPluginManager().callEvent(event);
			return event.getCurrency();
		default:
			return "$";
		}
	}
	
}
