package com.rummble.fireeagle.location.georss;

public class Point extends Geometry {
	private double lat;
	private double lng;
	
	public Point(double lat, double lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}
	
	public String toString()
	{
		return "Point:[lat:"+lat+",lng:"+lng+"]";
	}
	
	
}
