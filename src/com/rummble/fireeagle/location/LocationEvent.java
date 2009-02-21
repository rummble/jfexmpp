package com.rummble.fireeagle.location;

public class LocationEvent {

	private String token;
	private LocationHierarchy locationHierarchy;
	
	public LocationEvent(String token, LocationHierarchy locationHierarchy) {
		super();
		this.token = token;
		this.locationHierarchy = locationHierarchy;
	}

	public String getToken() {
		return token;
	}

	public LocationHierarchy getLocationHierarchy() {
		return locationHierarchy;
	}
	
	public String toString()
	{
		return "LocationEvent for " + token + " [" + locationHierarchy.toString() + "]";
	}
	
}
