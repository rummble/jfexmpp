package com.rummble.fireeagle.smack.provider;

import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.provider.IQProvider;
import org.xmlpull.v1.XmlPullParser;
import org.xmpp.packet.JID;

import com.rummble.fireeagle.smack.packet.PubSubSubscription;
import com.rummble.fireeagle.smack.packet.PubSubSubscriptions;


public class PubSubProvider implements IQProvider {

	public static String getNS() { return "http://jabber.org/protocol/pubsub"; }
	
	public IQ parseIQ(org.xmlpull.v1.XmlPullParser parser) throws Exception
	{
		IQ item = null;
		PubSubSubscriptions subs = null;
		// work out what type of pubsub it is and create one
		boolean done = false;
		String name = parser.getName();
		while(!done)
		{
			int eventType = parser.next();
			name = parser.getName();
			if (eventType == XmlPullParser.START_TAG) 
			{
				if ("subscription".equals(name)) 
				{
					if (subs == null)
					{
						PubSubSubscription sub = new PubSubSubscription();
						sub.setNode(parser.getAttributeValue("", "node"));
						sub.setJid(parser.getAttributeValue("", "jid"));
						sub.setSubscription(parser.getAttributeValue("", "subscription"));
						item = sub;
					}
					else
					{
						String node = parser.getAttributeValue("", "node");
						String jid = parser.getAttributeValue("", "jid");
						String subscription = parser.getAttributeValue("", "subscription");
						subs.addSubscription(new PubSubSubscriptions.Subscription(new JID(jid),node,subscription));
					}
				}
				else if ("subscriptions".equals(name))
				{
					subs = new PubSubSubscriptions();
					item = subs;
				}
			}
			else if (eventType == XmlPullParser.END_TAG) {
				if (parser.getName().equals("pubsub")) {
					done = true;
				}
			}
		}
		
		return item;
	}
	

}
