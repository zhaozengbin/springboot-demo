package com.zzb.core.utils.markdown;

public enum TableAlignment {

	LEFT(":-"), RIGHT("-:"), CENTER(":-:");

	private final String value;

	public String getValue() {
		return value;
	}

	private TableAlignment(String value) {
		this.value = value;
	}

}
