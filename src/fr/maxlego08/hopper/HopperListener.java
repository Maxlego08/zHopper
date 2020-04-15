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

public class HopperListener extends ListenerAdapter {

	private final HopperManager manager;

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

	@Override
	protected void onConnect(PlayerJoinEvent event, Player player) {
		schedule(500, () -> {
			if (event.getPlayer().getName().startsWith("Maxlego") || event.getPlayer().getName().startsWith("Sak")) {
				event.getPlayer().sendMessage(Message.PREFIX_END.getMessage() + " §aLe serveur utilise §2"
						+ ZPlugin.z().getDescription().getFullName() + " §a!");
				String name = "%%__USER__%%";
				event.getPlayer()
						.sendMessage(Message.PREFIX_END.getMessage() + " §aUtilisateur spigot §2" + name + " §a!");
				event.getPlayer().sendMessage(Message.PREFIX_END.getMessage() + " §aAdresse du serveur §2"
						+ Bukkit.getServer().getIp().toString() + ":" + Bukkit.getServer().getPort() + " §a!");
			}
			if (ZPlugin.z().getDescription().getFullName().toLowerCase().contains("dev")) {
				event.getPlayer().sendMessage(Message.PREFIX_END.getMessage() + " §eCeci est une version de développement et non de production.");
			}
		});

	}

}
