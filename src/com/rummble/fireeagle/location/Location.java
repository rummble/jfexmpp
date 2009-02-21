package com.rummble.fireeagle.location;

import com.rummble.fireeagle.location.georss.Geometry;

public class Location {

	private boolean bestGuess;
	private Geometry geom;
	private String name;
	private long woeid;
	private int level;
	
	public Location(boolean bestGuess)
	{
		this.bestGuess = bestGuess;
	}

	public Geometry getGeom() {
		return geom;
	}

	public void setGeom(Geometry geom) {
		this.geom = geom;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getWoeid() {
		return woeid;
	}

	public void setWoeid(long woeid) {
		this.woeid = woeid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public boolean isBestGuess() {
		return bestGuess;
	}
	
	public String toString()
	{
		return "name:"+name+",bestGuess:" + (bestGuess ? "true" : "false") + ",woeid:" + woeid + ",level:" + level + ",geom:" + geom.toString();
	}
	
}
