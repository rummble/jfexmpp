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

/*
 * 
<message from="fireeagle.com" to="clive@shirley.playtxt.net">
  <event xmlns="http://jabber.org/protocol/pubsub#event">
    <items node="/api/0.1/user/tcdjUwn6zaRw">
      <item>
        <rsp xmlns:georss="http://www.georss.org/georss" stat="ok">
          <user token="tcdjUwn6zaRw" writable="true" located-at="2009-02-08T08:11:11-08:00" readable="true">
            <location-hierarchy timezone="Europe/London" string="23424975|24554868|12602167|13911|26345313">
              <location best-guess="false">
                <georss:point>52.2031135559 0.1422471553</georss:point>
                <label/>
                <level>0</level>
                <level-name>exact</level-name>
                <located-at>2009-02-07T03:31:44-08:00</located-at>
                <name>26 Ainsworth Street, Cambridge, Cambridgeshire, England</name>
                <normal-name>CB1 2</normal-name>
                <place-id exact-match="false">RMUaKDubB581q8YZ3g</place-id>
                <woeid exact-match="false">26345313</woeid>
                <query>lat=52.2031&amp;lon=0.1423</query>
              </location>
              <location best-guess="false">
                <georss:box>52.1924285889 0.1273699999 52.2091293335 0.1457500011</georss:box>
                <label/>
                <level>1</level>
                <level-name>postal</level-name>
                <located-at>2009-02-07T03:31:44-08:00</located-at>
                <name>Cambridge, Cambridgeshire, England</name>
                <normal-name>CB1 2</normal-name>
                <place-id exact-match="true">RMUaKDubB581q8YZ3g</place-id>
                <woeid exact-match="true">26345313</woeid>
              </location>
              <location best-guess="true">
                <georss:box>50.8077507019 -0.225060001 50.895778656 -0.039069999</georss:box>
                <label/>
                <level>3</level>
                <level-name>city</level-name>
                <located-at>2009-02-08T08:11:11-08:00</located-at>
                <name>Brighton, East Sussex, England</name>
                <normal-name>Brighton</normal-name>
                <place-id exact-match="true">DWFHqjmYApXbYg</place-id>
                <woeid exact-match="true">13911</woeid>
              </location>
              <location best-guess="false">
                <georss:box>50.7334289551 -0.2450699955 51.1474494934 0.8678699732</georss:box>
                <label/>
                <level>4</level>
                <level-name>region</level-name>
                <located-at>2009-02-08T08:11:11-08:00</located-at>
                <name>East Sussex, England</name>
                <normal-name>East Sussex</normal-name>
                <place-id exact-match="true">1iv4fG2YA5oikDRZ2g</place-id>
                <woeid exact-match="true">12602167</woeid>
              </location>
              <location best-guess="false">
                <georss:box>49.8662185669 -6.4506998062 55.8111686707 1.7633299828</georss:box>
                <label/>
                <level>5</level>
                <level-name>state</level-name>
                <located-at>2009-02-08T08:11:11-08:00</located-at>
                <name>England, United Kingdom</name>
                <normal-name>England</normal-name>
                <place-id exact-match="true">pn4MsiGbBZlXeplyXg</place-id>
                <woeid exact-match="true">24554868</woeid>
              </location>
              <location best-guess="false">
                <georss:box>49.1620903015 -13.7422199249 60.8629302979 1.7633399963</georss:box>
                <label/>
                <level>6</level>
                <level-name>country</level-name>
                <located-at>2009-02-08T08:11:11-08:00</located-at>
                <name>United Kingdom</name>
                <normal-name>United Kingdom</normal-name>
                <place-id exact-match="true">DevLebebApj4RVbtaQ</place-id>
                <woeid exact-match="true">23424975</woeid>
              </location>
            </location-hierarchy>
          </user>
        </rsp>
      </item>
    </items>
  </event>
</message>

 */
