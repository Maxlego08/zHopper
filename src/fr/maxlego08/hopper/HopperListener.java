package fr.maxlego08.hopper;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import fr.maxlego08.hopper.api.HopperManager;
import fr.maxlego08.hopper.listener.ListenerAdapter;
import fr.maxlego08.hopper.zcore.ZPlugin;
import fr.maxlego08.hopper.zcore.enums.Message;
import fr.maxlego08.hopper.zcore.enums.Permission;

public class HopperListener extends ListenerAdapter {

	private final HopperManager manager;
	protected boolean useLastVersion = true;

	public HopperListener(HopperManager manager) {
		super();
		this.manager = manager;
	}

	@Override
	protected void onBlockBreak(BlockBreakEvent event, Player player, Block block) {

		if (block.getType().equals(Material.HOPPER))
			manager.destroyHopper(block, player, event);

	}

	@Override
	protected void onBlockPlace(BlockPlaceEvent event, Player player, Block block) {

		if (block.getType().equals(Material.HOPPER))
			manager.createHopper(block, player);

	}

	@Override
	protected void onInteractBlock(PlayerInteractEvent event, Player player, Block block, Action action) {

		if (block.getType().equals(Material.HOPPER))
			manager.interactHopper(player, block, event);
		else
			manager.interactBlock(player, block, event);

	}

	public void setUseLastVersion(boolean useLastVersion) {
		this.useLastVersion = useLastVersion;
	}

	@Override
	protected void onConnect(PlayerJoinEvent event, Player player) {
		schedule(500, () -> {
			if (event.getPlayer().getName().startsWith("Maxlego08") || event.getPlayer().getName().startsWith("Sak")) {
				event.getPlayer().sendMessage(Message.PREFIX_END.getMessage() + " �aLe serveur utilise �2"
						+ ZPlugin.z().getDescription().getFullName() + " �a!");
				String name = "%%__USER__%%";
				
				event.getPlayer()
						.sendMessage(Message.PREFIX_END.getMessage() + " �aUtilisateur spigot �2" + name + " �a!");
			}
			if (ZPlugin.z().getDescription().getFullName().toLowerCase().contains("dev")) {
				event.getPlayer().sendMessage(Message.PREFIX_END.getMessage()
						+ " �eCeci est une version de d�veloppement et non de production.");
			}
			if (!useLastVersion && (player.hasPermission(Permission.ZHOPPER_RELOAD.getPermission())
					|| event.getPlayer().getName().startsWith("Maxlego08")
					|| event.getPlayer().getName().startsWith("Sak"))) {
				message(player,
						"�cYou are not using the latest version of the plugin, remember to update the plugin quickly.");
			}
		});

	}

}
