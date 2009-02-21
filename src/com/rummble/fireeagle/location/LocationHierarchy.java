package com.rummble.fireeagle.location;

import java.util.Map;
import java.util.TreeMap;

public class LocationHierarchy {

	private Map<Integer,Location> hierarchy;
	private Location bestGuess;
	
	public LocationHierarchy(){
		hierarchy = new TreeMap<Integer,Location>();
	}
	
	public void add(Location location)
	{
		hierarchy.put(location.getLevel(), location);
		if (location.isBestGuess())
			bestGuess = location;
	}

	public Map<Integer, Location> getHierarchy() {
		return hierarchy;
	}

	public Location getBestGuess() {
		return bestGuess;
	}
	
	public String toString()
	{
		StringBuffer s = new StringBuffer();
		for(Location l: hierarchy.values())
		{
			s.append("[");
			s.append(l.toString());
			s.append("] ");
		}
		return s.toString();
	}
	
}
