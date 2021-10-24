package fr.maxlego08.hopper.stackedentities.hook;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

import fr.maxlego08.hopper.stackedentities.SEntity;

public abstract class SHook implements SEntity {

	private final LivingEntity entity;

	/**
	 * @param entity
	 */
	public SHook(LivingEntity entity) {
		super();
		this.entity = entity;
	}

	@Override
	public LivingEntity getEntity() {
		return this.entity;
	}

	@Override
	public EntityType getType() {
		return this.entity.getType();
	}

}
