package com.redhat.wine.pairing;

public class WinePairingRequest {
    
    private final String typeOfFood;

    public WinePairingRequest(String typeOfFood) {
        this.typeOfFood = typeOfFood;
    }

	/**
	 * @return the typeOfFood
	 */
	public String getTypeOfFood() {
		return typeOfFood;
	}
}