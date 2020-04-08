package fr.maxlego08.hopper.economy;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import fr.maxlego08.hopper.api.events.EconomyDepositEvent;
import fr.maxlego08.hopper.api.events.EconomyHasMoneyEvent;
import fr.maxlego08.hopper.api.events.EconomyWithdrawMoney;
import fr.maxlego08.hopper.zcore.ZPlugin;
import fr.maxlego08.hopper.zcore.logger.Logger;
import fr.maxlego08.hopper.zcore.logger.Logger.LogType;
import fr.maxlego08.hopper.zcore.utils.ZUtils;
import me.bukkit.mTokens.Inkzzz.Tokens;
import me.realized.tokenmanager.api.TokenManager;

public class EconomyUtils extends ZUtils {

	private transient TokenManager tokenManager = (TokenManager) Bukkit.getPluginManager().getPlugin("TokenManager");

	/**
	 * @param economy
	 * @param player
	 * @param price
	 * @return
	 */
	protected boolean hasMoney(Economy economy, Player player, long price) {
		switch (economy) {
		case MYSQLTOKEN:
			return Tokens.getInstance().getAPI().getTokens(player) >= price;
		case TOKENMANAGER:
			return tokenManager.getTokens(player).getAsLong() >= price;
		case PLAYERPOINT:
			return ZPlugin.z().getPlayerPointsAPI().look(player.getUniqueId()) >= (int) price;
		case VAULT:
			return super.getBalance(player) >= price;
		case CUSTOM:
			EconomyHasMoneyEvent event = new EconomyHasMoneyEvent(player, price);
			Bukkit.getPluginManager().callEvent(event);
			return event.hasMoney();
		default:
			return false;
		}
	}

	/**
	 * @param economy
	 * @param player
	 * @param value
	 */
	// protected void depositMoney(Economy economy, Player player, long value) {
	// depositMoney(economy, player.getName(), value);
	// }

	/**
	 * 
	 * @param economy
	 * @param player
	 * @param value
	 */
	@SuppressWarnings("deprecation")
	protected void depositMoney(Economy economy, String player, long value) {

		switch (economy) {
		case MYSQLTOKEN:
			Player player2 = Bukkit.getPlayer(player);
			if (player2 != null)
				Tokens.getInstance().getAPI().giveTokens(player2, (int) value);
			else
				Logger.info("IMPOSSIBLE DE DONNER LES MYSQLPOINTS AU JOUEUR " + player + " NOMBRE DE POINT: " + value,
						LogType.ERROR);
			break;
		case TOKENMANAGER:
			tokenManager.addTokens(
					Bukkit.getOnlineMode() ? Bukkit.getOfflinePlayer(player).getUniqueId().toString() : player,
					(long) value);
			break;
		case PLAYERPOINT:
			ZPlugin.z().getPlayerPointsAPI().give(player, (int) value);
			break;
		case VAULT:
			super.depositMoney(player, value);
			break;
		case CUSTOM:
			EconomyDepositEvent event = new EconomyDepositEvent(player, value);
			Bukkit.getPluginManager().callEvent(event);
			break;
		default:
			Logger.info("IMPOSSIBLE DE DONNER LA MONEY AU JOUEUR " + player + " NOMBRE DE POINT: " + value,
					LogType.ERROR);
			break;
		}
	}

	/**
	 * 
	 * @param economy
	 * @param player
	 * @param value
	 */
	protected void depositMoney(Economy economy, Player player, long value) {
		switch (economy) {
		case MYSQLTOKEN:
			Player player2 = Bukkit.getPlayer(player.getName());
			if (player2 != null)
				Tokens.getInstance().getAPI().giveTokens(player2, (int) value);
			else
				Logger.info("IMPOSSIBLE DE DONNER LES MYSQLPOINTS AU JOUEUR " + player + " NOMBRE DE POINT: " + value,
						LogType.ERROR);
			break;
		case TOKENMANAGER:
			tokenManager.addTokens(player, (long) value);
			break;
		case PLAYERPOINT:
			ZPlugin.z().getPlayerPointsAPI().give(player.getUniqueId(), (int) value);
			break;
		case VAULT:
			super.depositMoney(player, value);
			break;
		case CUSTOM:
			EconomyDepositEvent event = new EconomyDepositEvent(player.getName(), value);
			Bukkit.getPluginManager().callEvent(event);
			break;			
		default:
			Logger.info("IMPOSSIBLE DE DONNER LA MONEY AU JOUEUR " + player + " NOMBRE DE POINT: " + value,
					LogType.ERROR);
			break;
		}
	}

	/**
	 * 
	 * @param economy
	 * @param player
	 * @param value
	 */
	protected void withdrawMoney(Economy economy, Player player, long value) {
		switch (economy) {
		case MYSQLTOKEN:
			Tokens.getInstance().getAPI().takeTokens(player, (int) value);	
		case TOKENMANAGER:
			tokenManager.removeTokens(player, value);
			break;
		case PLAYERPOINT:
			ZPlugin.z().getPlayerPointsAPI().take(player.getUniqueId(), (int) value);
			break;
		case VAULT:
			super.withdrawMoney(player, value);
			break;
		case CUSTOM:
			EconomyWithdrawMoney event = new EconomyWithdrawMoney(player, value);
			Bukkit.getPluginManager().callEvent(event);
			break;			
		default:
			Logger.info("IMPOSSIBLE DE PRENDRE LA MONEY AU JOUEUR " + player + " NOMBRE DE POINT: " + value,
					LogType.ERROR);
			break;
		}
	}

}
