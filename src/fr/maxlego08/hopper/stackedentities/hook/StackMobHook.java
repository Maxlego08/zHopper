package fr.maxlego08.hopper.stackedentities.hook;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.entity.EntityManager;
import uk.antiperson.stackmob.entity.StackEntity;

public class StackMobHook extends SHook {

	private final StackEntity stackMob;

	/**
	 * @param livingEntity
	 */
	public StackMobHook(LivingEntity livingEntity) {
		super(livingEntity);
		StackMob mob = (StackMob) Bukkit.getPluginManager().getPlugin("StackMob");
		EntityManager entityManager = mob.getEntityManager();
		this.stackMob = entityManager.getStackEntity(livingEntity);
	}

	@Override
	public int getSize() {
		return this.stackMob.getSize();
	}

	@Override
	public void setSize(int amount) {
		this.stackMob.setSize(amount, true);
	}

}
