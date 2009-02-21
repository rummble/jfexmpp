package com.rummble.fireeagle.smack.packet;

import org.jivesoftware.smack.packet.PacketExtension;

import com.rummble.fireeagle.location.LocationEvent;

public class PubSubLocationEvent  implements PacketExtension {

	LocationEvent loc;
	
	public String getElementName() {
        return "event";
    }
	
	public String getNamespace() {
        return "http://jabber.org/protocol/pubsub#event";
    }
	
	 public String toXML() {
		 return "";
	 }

	public LocationEvent getLoc() {
		return loc;
	}

	public void setLoc(LocationEvent loc) {
		this.loc = loc;
	}
}
