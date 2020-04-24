package fr.maxlego08.hopper.zcore.enums;

public enum Permission {

	ZHOPPER_RELOAD("zhopper.reload"),
	ZHOPPER_CONFIG("zhopper.config"),
	ZHOPPER_GIVE("zhopper.give"),
	
	;

	private final String permission;

	private Permission(String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
}
