package fr.maxlego08.hopper.zcore.utils.builder;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.zcore.utils.storage.Persist;
import fr.maxlego08.hopper.zcore.utils.storage.Saveable;

public class CooldownBuilder implements Saveable{

	public static HashMap<String, HashMap<UUID, Long>> cooldowns = new HashMap<>();

	public static HashMap<UUID, Long> getCooldownMap(String s) {
		if (cooldowns.containsKey(s)) {
			return cooldowns.get(s);
		}
		return null;
	}

	public static void clear() {
		cooldowns.clear();
	}

	public static void createCooldown(String s) {
		if (!cooldowns.containsKey(s)) {
			cooldowns.put(s, new HashMap<>());
		}
	}

	public static void removeCooldown(String s, Player joueur) {
		if (!cooldowns.containsKey(s)) {
			throw new IllegalArgumentException("! Attention ! " + String.valueOf(s) + " n'existe pas.");
		}
		(cooldowns.get(s)).remove(joueur.getUniqueId());
	}

	public static void addCooldown(String s, Player joueur, int seconds){
		cooldowns.putIfAbsent(s, new HashMap<>());
		long next = System.currentTimeMillis() + seconds * 1000L;
		(cooldowns.get(s)).put(joueur.getUniqueId(), Long.valueOf(next));
	}
	
	public static void addCooldown(String s, Hopper joueur, long seconds){
		cooldowns.putIfAbsent(s, new HashMap<>());
		long next = System.currentTimeMillis() + seconds;
		(cooldowns.get(s)).put(joueur.getUniqueId(), Long.valueOf(next));
	}

	public static boolean isCooldown(String s, Player joueur) {
		return (cooldowns.containsKey(s)) && ((cooldowns.get(s)).containsKey(joueur.getUniqueId()))
				&& (System.currentTimeMillis() <= ((Long) (cooldowns.get(s)).get(joueur.getUniqueId())).longValue());
	}
	
	public static boolean isCooldown(String s, Hopper joueur) {
		cooldowns.putIfAbsent(s, new HashMap<>());
		return (cooldowns.containsKey(s)) && ((cooldowns.get(s)).containsKey(joueur.getUniqueId()))
				&& (System.currentTimeMillis() <= ((Long) (cooldowns.get(s)).get(joueur.getUniqueId())).longValue());
	}

	public static long getCooldownPlayer(String s, Player joueur) {
		return ((Long) (cooldowns.get(s)).getOrDefault(joueur.getUniqueId(), 0l)).longValue() - System.currentTimeMillis();
	}

	public static String getCooldownAsString(String s, Player player) {
		return TimerBuilder.getStringTime(getCooldownPlayer(s, player) / 1000);
	}
	
	private static transient CooldownBuilder i = new CooldownBuilder();

	@Override
	public void save(Persist persist) {
		persist.save(i, "cooldowns");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(i, CooldownBuilder.class, "cooldowns");
	}
}
