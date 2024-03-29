package fr.maxlego08.hopper.zcore;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.maxlego08.hopper.HopperObject;
import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.listener.ListenerAdapter;
import fr.maxlego08.hopper.zcore.logger.Logger;
import fr.maxlego08.hopper.zcore.logger.Logger.LogType;
import fr.maxlego08.hopper.zcore.utils.gson.InterfaceSerializer;
import fr.maxlego08.hopper.zcore.utils.gson.ItemStackAdapter;
import fr.maxlego08.hopper.zcore.utils.gson.LocationAdapter;
import fr.maxlego08.hopper.zcore.utils.gson.PotionEffectAdapter;
import fr.maxlego08.hopper.zcore.utils.storage.Persist;
import fr.maxlego08.hopper.zcore.utils.storage.Saveable;
import net.milkbowl.vault.economy.Economy;

public abstract class ZPlugin extends JavaPlugin {

	private final Logger log = new Logger(this.getDescription().getFullName());
	private Gson gson;
	private Persist persist;
	private static ZPlugin plugin;
	private long enableTime;
	private List<Saveable> savers = new ArrayList<>();
	private List<ListenerAdapter> listenerAdapters = new ArrayList<>();
	private Economy economy = null;
	private PlayerPoints playerPoints;
	private PlayerPointsAPI playerPointsAPI;

	public ZPlugin() {
		plugin = this;
	}

	protected boolean preEnable() {

		enableTime = System.currentTimeMillis();

		log.log("=== ENABLE START ===");
		log.log("Plugin Version V<&>c" + getDescription().getVersion(), LogType.INFO);

		getDataFolder().mkdirs();

		gson = getGsonBuilder().create();
		persist = new Persist(this);

		setupEconomy();

		return true;

	}

	protected void postEnable() {

		log.log("=== ENABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}

	protected void preDisable() {

		enableTime = System.currentTimeMillis();
		log.log("=== DISABLE START ===");

	}

	protected void postDisable() {

		log.log("=== DISABLE DONE <&>7(<&>6" + Math.abs(enableTime - System.currentTimeMillis()) + "ms<&>7) <&>e===");

	}

	/**
	 * Build gson
	 * 
	 * @return
	 */
	public GsonBuilder getGsonBuilder() {
		return new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls()
				.excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.VOLATILE)
				.registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
				.registerTypeAdapter(PotionEffect.class, new PotionEffectAdapter())
				.registerTypeAdapter(Hopper.class, InterfaceSerializer.interfaceSerializer(HopperObject.class))
				.registerTypeAdapter(Location.class, new LocationAdapter());
	}

	/**
	 * Add a listener
	 * 
	 * @param listener
	 */
	public void addListener(Listener listener) {
		if (listener instanceof Saveable)
			addSave((Saveable) listener);
		Bukkit.getPluginManager().registerEvents(listener, this);
	}

	/**
	 * Add a listener from ListenerAdapter
	 * 
	 * @param adapter
	 */
	public void addListener(ListenerAdapter adapter) {
		if (adapter instanceof Saveable)
			addSave((Saveable) adapter);
		listenerAdapters.add(adapter);
	}

	/**
	 * Add a Saveable
	 * 
	 * @param saver
	 */
	public void addSave(Saveable saver) {
		this.savers.add(saver);
	}

	/**
	 * Get logger
	 * 
	 * @return loggers
	 */
	public Logger getLog() {
		return this.log;
	}

	/**
	 * Get gson
	 * 
	 * @return {@link Gson}
	 */
	public Gson getGson() {
		return gson;
	}

	public Persist getPersist() {
		return persist;
	}

	/**
	 * Get all saveables
	 * 
	 * @return savers
	 */
	public List<Saveable> getSavers() {
		return savers;
	}

	public static ZPlugin z() {
		return plugin;
	}

	/**
	 * @return boolean ture if economy is setup
	 */
	protected boolean setupEconomy() {
		try {
			RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager()
					.getRegistration(Economy.class);
			if (economyProvider != null) {
				economy = economyProvider.getProvider();
			}
		} catch (NoClassDefFoundError e) {
		}
		return (economy != null);
	}

	public Economy getEconomy() {
		return economy;
	}

	/**
	 * 
	 * @return listenerAdapters
	 */
	public List<ListenerAdapter> getListenerAdapters() {
		return listenerAdapters;
	}

	/**
	 * @return boolean
	 */
	protected boolean hookPlayerPoints() {
		try {
			final Plugin plugin = (Plugin) this.getServer().getPluginManager().getPlugin("PlayerPoints");
			if (plugin == null)
				return false;
			playerPoints = PlayerPoints.class.cast(plugin);
			playerPointsAPI = playerPoints.getAPI();
		} catch (Exception e) {
		}
		return playerPoints != null;
	}

	/**
	 * @return the playerPoints
	 */
	public PlayerPoints getPlayerPoints() {
		return playerPoints;
	}

	/**
	 * @return the playerPointsAPI
	 */
	public PlayerPointsAPI getPlayerPointsAPI() {
		return playerPointsAPI;
	}
	
	public <T> T getProvider(Class<T> classz) {
		RegisteredServiceProvider<T> provider = getServer().getServicesManager().getRegistration(classz);
		if (provider == null) {
			log.log("Unable to retrieve the provider " + classz.toString(), LogType.WARNING);
			return null;
		}
		return provider.getProvider() != null ? (T) provider.getProvider() : null;
	}

}
