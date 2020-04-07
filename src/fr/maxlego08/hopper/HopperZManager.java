package fr.maxlego08.hopper;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.listener.ListenerAdapter;
import fr.maxlego08.hopper.zcore.utils.storage.Persist;

public class HopperZManager extends ListenerAdapter implements HopperManager {

	private volatile HopperPlugin plugin;
	private Map<Location, Hopper> hoppers = new HashMap<Location, Hopper>();

	public HopperZManager(HopperPlugin plugin) {
		super();
		this.plugin = plugin;
	}

	public void add(){
		this.hoppers.put(new Location(Bukkit.getWorld("vultaria"), 0, 64, 0), new HopperObject());
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
		persist.save(this, "hoppers");
	}

	@Override
	public void load(Persist persist) {
		persist.loadOrSaveDefault(this, HopperZManager.class, "hoppers");
	}

}
