package fr.maxlego08.hopper.zcore.enums;

public enum Permission {

	ZHOPPER_RELOAD("zhopper.reload"),
	
	;

	private final String permission;

	private Permission(String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
}
