package fr.maxlego08.hopper.stackedentities;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import fr.maxlego08.hopper.stackedentities.hook.StackMobHook;
import fr.maxlego08.hopper.stackedentities.hook.WildStackHook;

public interface SEntity {

	LivingEntity getEntity();

	int getSize();

	void setSize(int amount);

	EntityType getType();

	public static Optional<SEntity> hook(LivingEntity entity) {
		if (Bukkit.getPluginManager().isPluginEnabled("WildStacker"))
			return Optional.of(new WildStackHook(entity));
		else if (Bukkit.getPluginManager().isPluginEnabled("StackMob"))
			return Optional.of(new StackMobHook(entity));
		return Optional.empty();
	}

}
