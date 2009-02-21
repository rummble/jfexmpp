package com.rummble.fireeagle.location.georss;

public class Box extends Geometry {
	private double swlat;
	private double swlng;
	private double nelat;
	private double nelng;
	public Box(double swlat, double swlng, double nelat, double nelng) {
		super();
		this.swlat = swlat;
		this.swlng = swlng;
		this.nelat = nelat;
		this.nelng = nelng;
	}
	public double getSwlat() {
		return swlat;
	}
	public void setSwlat(double swlat) {
		this.swlat = swlat;
	}
	public double getSwlng() {
		return swlng;
	}
	public void setSwlng(double swlng) {
		this.swlng = swlng;
	}
	public double getNelat() {
		return nelat;
	}
	public void setNelat(double nelat) {
		this.nelat = nelat;
	}
	public double getNelng() {
		return nelng;
	}
	public void setNelng(double nelng) {
		this.nelng = nelng;
	}
	
	public String toString()
	{
		return "Box:[swlat:"+swlat+",swlng:"+swlng+",nelat:"+nelat+",nelng"+nelng+"]";
	}
	
	
}
