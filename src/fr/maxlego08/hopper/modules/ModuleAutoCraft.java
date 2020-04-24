package fr.maxlego08.hopper.modules;

import fr.maxlego08.hopper.api.Hopper;
import fr.maxlego08.hopper.api.Level;
import fr.maxlego08.hopper.save.Config;
import fr.maxlego08.hopper.zcore.utils.inventory.Button;

public class ModuleAutoCraft extends Module {

	public ModuleAutoCraft(String name, int priority) {
		super(name, priority);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(Hopper hopper, Level level) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isCooldown(Hopper hopper, Level level) {
		return super.isCooldown(hopper, "module-autocraft", level.getLongAsProperty("milliSecondModuleAutoCraft"));
	}

	@Override
	public Button getButton() {
		return Config.craftButton;
	}

}
