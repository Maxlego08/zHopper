package fr.maxlego08.hopper.api;

import java.util.List;

import org.bukkit.event.Listener;

import fr.maxlego08.hopper.zcore.utils.storage.Saveable;

public interface HopperManager extends Listener, Saveable{

	List<Hopper> getHoppers();
	
}
