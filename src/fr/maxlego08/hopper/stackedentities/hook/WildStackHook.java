package fr.maxlego08.hopper.stackedentities.hook;

import org.bukkit.entity.LivingEntity;

import com.bgsoftware.wildstacker.api.WildStackerAPI;
import com.bgsoftware.wildstacker.api.objects.StackedEntity;

public class WildStackHook extends SHook {

	private final StackedEntity stackedEntity;

	public WildStackHook(LivingEntity entity) {
		super(entity);
		this.stackedEntity = WildStackerAPI.getStackedEntity(entity);
	}

	@Override
	public int getSize() {
		return this.stackedEntity.getStackAmount();
	}

	@Override
	public void setSize(int amount) {
		this.stackedEntity.setStackAmount(amount, true);
	}

}
