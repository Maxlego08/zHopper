package fr.maxlego08.hopper;

import org.bukkit.plugin.ServicePriority;

import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.command.CommandManager;
import fr.maxlego08.hopper.inventory.InventoryManager;
import fr.maxlego08.hopper.listener.AdapterListener;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.save.Lang;
import fr.maxlego08.hopper.zcore.ZPlugin;
import fr.maxlego08.hopper.zcore.logger.Logger.LogType;
import fr.maxlego08.hopper.zcore.utils.Metrics;
import fr.maxlego08.hopper.zcore.utils.VersionChecker;
import fr.maxlego08.zitemstacker.api.ItemManager;

/**
 * System to create your plugins very simply Projet:
 * https://github.com/Maxlego08/TemplatePlugin
 * 
 * @author Maxlego08
 *
 */
public class HopperPlugin extends ZPlugin {

	private CommandManager commandManager;
	private InventoryManager inventoryManager;
	private HopperManager hopperManager;
	private HopperListener hopperListener;

	@Override
	public void onEnable() {

		preEnable();

		commandManager = new CommandManager(this);
		commandManager.registerCommands();

		if (!isEnabled())
			return;
		inventoryManager = InventoryManager.getInstance();

		hopperManager = new HopperZManager(this);
		hopperListener = new HopperListener(hopperManager);
		getServer().getServicesManager().register(HopperManager.class, hopperManager, this, ServicePriority.High);

		/* Add Listener */

		addListener(new AdapterListener(this));
		addListener(inventoryManager);
		addListener(hopperListener);

		if (hookPlayerPoints())
			getLog().log("Playerpoint loading done successfully", LogType.SUCCESS);
		if (getServer().getPluginManager().isPluginEnabled("zItemStacker") && getProvider(ItemManager.class) != null)
			getLog().log("zItemManager loading done successfully", LogType.SUCCESS);

		/* Add Saver */

		addSave(Config.getInstance());
		addSave(Lang.getInstance());
		addSave(hopperManager);

		getSavers().forEach(saver -> saver.load(getPersist()));

		hopperManager.run();

		new Metrics(this);

		VersionChecker checker = new VersionChecker(this, 6);
		checker.useLastVersion();

		postEnable();

	}

	@Override
	public void onDisable() {

		preDisable();

		getSavers().forEach(saver -> saver.save(getPersist()));

		postDisable();

	}

	/**
	 * @return the commandManager
	 */
	public CommandManager getCommandManager() {
		return commandManager;
	}

	/**
	 * @return the inventoryManager
	 */
	public InventoryManager getInventoryManager() {
		return inventoryManager;
	}

	/**
	 * 
	 * @return
	 */
	public HopperManager getHopperManager() {
		return hopperManager;
	}

}
