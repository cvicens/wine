package com.redhat.wine.cellar;

public class WineRepositoryResponse {
    
	private final long id;
	private final String status;
	private final String description;
    private final Wine[] wines;

    public WineRepositoryResponse(long id, String status, String description, Wine[] wines) {
        this.id = id;
		this.status = status;
		this.description = description;
		this.wines = wines;
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

	public Wine[] getWines() {
		return wines;
	}
}