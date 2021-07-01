package fr.maxlego08.hopper.modules;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Animals;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.stackedentities.SEntity;
import fr.maxlego08.hopper.zcore.utils.inventory.Button;

public class ModuleKillMob extends Module {

	public ModuleKillMob(int priority) {
		super("KillMob", priority);
		runAsync = Config.runModuleKilLMobAsync;
	}

	@Override
	public void execute(Hopper hopper, Level level) {

		int distance = level.getMaxDistanceKill();
		int maxPerSecond = level.getMaxKillPerSecond();
		boolean canUseModule = (level.canKillMonster() || level.canKillPassive()) && distance > 0 && maxPerSecond > 0;

		if (!canUseModule)
			return;

		Location location = hopper.getLocation().clone();
		location.add(0.5, 1, 0.5);

		World world = hopper.getWorld();

		if (distance <= 0 || world == null)
			return;

		Stream<LivingEntity> stream = world.getNearbyEntities(hopper.getLocation(), distance, distance, distance)
				.stream().filter(entity -> {

					boolean monster = !(!level.canKillMonster() && entity instanceof Monster);
					boolean passive = !(!level.canKillPassive() && entity instanceof Animals);

					return entity instanceof LivingEntity && monster && passive && !(entity instanceof Player);
				}).map(e -> (LivingEntity) e);

		int amount = 0;
		for (LivingEntity entity : stream.collect(Collectors.toList())) {

			if (amount > maxPerSecond)
				return;

			if (isStackeable(entity.getType())) {

				Optional<SEntity> optional = SEntity.hook(entity);
				if (!optional.isPresent()) {
					entity.teleport(location);
					entity.damage(entity.getHealth() * 10);
					amount++;
					continue;
				}

				SEntity stackedEntity = optional.get();
				int stackedAmount = stackedEntity.getSize();

				// Si le nombre d'entity est un alors on s'en fou
				if (stackedAmount == 1) {

					entity.teleport(location);
					entity.damage(entity.getHealth() * 10);
					amount++;

				} else

					for (int a = 0; a < stackedAmount; a++) {

						if (amount > maxPerSecond)
							return;

						stackedAmount = stackedAmount - 1;
						stackedEntity.setSize(stackedAmount);
						LivingEntity spawnedEntity = (LivingEntity) entity.getWorld().spawnEntity(entity.getLocation(),
								stackedEntity.getType());
						spawnedEntity.teleport(location);
						spawnedEntity.damage(entity.getHealth() * 10);
						amount++;

					}
			} else {

				entity.teleport(location);
				entity.damage(entity.getHealth() * 10);
				amount++;
			}
		}

	}

	private boolean isStackeable(EntityType type) {
		switch (type) {
		case ARMOR_STAND:
			return false;
		default:
			return true;
		}
	}

	@Override
	public boolean isCooldown(Hopper hopper, Level level) {
		return super.isCooldown(hopper, "modulekillmob", level.getLongAsProperty("milliSecondModuleKill"));
	}

	@Override
	public Button getButton() {
		return Config.killButton;
	}
}
