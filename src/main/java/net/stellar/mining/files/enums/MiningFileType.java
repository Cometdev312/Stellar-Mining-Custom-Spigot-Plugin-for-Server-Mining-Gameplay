package net.stellar.mining.files.enums;

public enum MiningFileType {

	BLOCKS("blocks"),
	SETTINGS("settings"),
	MESSAGES("messages");
	
	String name;
	
	MiningFileType(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
