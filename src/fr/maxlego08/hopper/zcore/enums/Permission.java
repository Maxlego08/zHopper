package fr.maxlego08.hopper.zcore.enums;

public enum Permission {

	
	;

	private final String permission;

	private Permission(String permission) {
		this.permission = permission;
	}
	
	public String getPermission() {
		return permission;
	}
	
}
