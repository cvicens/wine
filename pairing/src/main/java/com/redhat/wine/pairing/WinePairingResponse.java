package com.redhat.wine.pairing;

public class WinePairingResponse {
    
	private final long id;
	private final String status;
	private final String description;
    private final WineType[] wineTypes;

    public WinePairingResponse(long id, String status, String description, WineType[] wineTypes) {
        this.id = id;
		this.status = status;
		this.description = description;
		this.wineTypes = wineTypes;
    }

	public long getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public String getDescription() {
		return description;
	}

	public WineType[] getWineTypes() {
		return wineTypes;
	}
}