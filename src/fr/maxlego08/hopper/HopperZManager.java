package fr.maxlego08.hopper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.listener.ListenerAdapter;
import fr.maxlego08.hopper.zcore.utils.storage.Persist;

public class HopperZManager extends ListenerAdapter implements HopperManager {

	private volatile HopperPlugin plugin;
	private volatile Map<Location, Hopper> hoppers = new HashMap<Location, Hopper>();
	private List<Hopper> hopperList = new ArrayList<Hopper>();

	/**
	 * 
	 * @param plugin
	 */
	public HopperZManager(HopperPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	public void add() {
		hopperList.add(new HopperObject());
	}

	/**
	 * 
	 * @return plugin
	 */
	public HopperPlugin getPlugin() {
		return plugin;
	}

	@Override
	public void save(Persist persist) {
		hopperList = new ArrayList<>(hoppers.values());
		persist.save(this, "hoppers");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, HopperZManager.class, "hoppers");
		hoppers = new HashMap<>();
		hopperList.forEach(hopper -> hoppers.put(hopper.getLocation(), hopper));
	}

	@Override
	public List<Hopper> getHoppers() {
		return hopperList;
	}

}
