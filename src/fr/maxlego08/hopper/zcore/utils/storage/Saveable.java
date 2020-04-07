package fr.maxlego08.hopper.zcore.utils.storage;

public interface Saveable {
	
	void save(Persist persist);
	void load(Persist persist);
}
