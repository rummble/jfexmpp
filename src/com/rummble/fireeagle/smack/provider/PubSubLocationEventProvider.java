package com.rummble.fireeagle.smack.provider;

import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.xmlpull.v1.XmlPullParser;

import com.rummble.fireeagle.location.Location;
import com.rummble.fireeagle.location.LocationEvent;
import com.rummble.fireeagle.location.LocationHierarchy;
import com.rummble.fireeagle.location.georss.Box;
import com.rummble.fireeagle.location.georss.Point;
import com.rummble.fireeagle.smack.packet.PubSubLocationEvent;

public class PubSubLocationEventProvider implements PacketExtensionProvider  {

	/*
	 * No argument constructor as required by PacketExtensionProvider
	 */
	public PubSubLocationEventProvider(){
		
	}
	
	public static String getNS() { return "http://jabber.org/protocol/pubsub#event"; }
	
	
	
	 public PacketExtension parseExtension(XmlPullParser parser) throws Exception
	 {
		 PubSubLocationEvent pubSubLocEvent = new PubSubLocationEvent();
		 LocationHierarchy locH = null;
		 String token = null;
		 Location loc = null;
		 boolean done = false;
		 boolean insideLocation = false;
		 String name = parser.getName();
		 while(!done)
		 {
			 int eventType = parser.next();
			 name = parser.getName();
			 if (eventType == XmlPullParser.START_TAG) 
			 {
				 if ("user".equals(name)) 
				 {
					 token = parser.getAttributeValue("", "token");
				 }
				 else if ("location-hierarchy".equals(name)) 
				 {
					 locH = new LocationHierarchy();
				 }				 
				 else if ("location".equals(name)) 
				 {
					 insideLocation = true;
					 String bestGuess = parser.getAttributeValue("", "best-guess");
					 loc = new Location("true".equals(bestGuess));
				 }
				 else if (insideLocation && "level".equals(name))
					 loc.setLevel(Integer.parseInt(parser.nextText()));
				 else if (insideLocation && "name".equals(name))
					 loc.setName(parser.nextText());
				 else if (insideLocation && "woeid".equals(name))
					 loc.setWoeid(Long.parseLong(parser.nextText()));	
				 else if (insideLocation && "point".equals(name))
				 {
					 String pointStr = parser.nextText();
					 String parts[] = pointStr.split(" ");
					 double lat = Double.parseDouble(parts[0]);
					 double lng = Double.parseDouble(parts[1]);
					 Point p = new Point(lat,lng);
					 loc.setGeom(p);
				 }
				 else if (insideLocation && "box".equals(name))
				 {
					 String boxStr = parser.nextText();
					 String parts[] = boxStr.split(" ");
					 double swlat = Double.parseDouble(parts[0]);
					 double swlng = Double.parseDouble(parts[1]);
					 double nelat = Double.parseDouble(parts[2]);
					 double nelng = Double.parseDouble(parts[3]);
					 Box b = new Box(swlat,swlng,nelat,nelng);
					 loc.setGeom(b);
				 }
			 }
			 else if (eventType == XmlPullParser.END_TAG) 
			 {
				 if ("event".equals(name))
				 {
						done = true;
				 }
				 else if ("location".equals(name))
				 {
					 locH.add(loc);
					 insideLocation = false;
				 }
				 else if ("user".equals(name))
				 {
					 LocationEvent locEvent = new LocationEvent(token,locH);
					 pubSubLocEvent.setLoc(locEvent);
				 }
			 }
		 }

		 return pubSubLocEvent;
	 }
	
}
