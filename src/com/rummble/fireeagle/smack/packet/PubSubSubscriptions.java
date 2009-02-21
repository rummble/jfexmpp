package com.rummble.fireeagle.smack.packet;

import java.util.HashSet;
import java.util.Set;

import org.jivesoftware.smack.packet.IQ;
import org.xmpp.packet.JID;

public class PubSubSubscriptions extends IQ {


	Set<Subscription> subscriptions;
	
	
	public PubSubSubscriptions()
	{
		subscriptions = new HashSet<Subscription>();
	}
	public void addSubscription(Subscription sub)
	{
		subscriptions.add(sub);
	}
	
	public Set<Subscription> getSubscriptions()
	{
		return subscriptions;
	}
	
	
	public static class Subscription
	{
		private JID jid;
		private String node;
		private String subscription;
		
		public Subscription(JID jid, String node, String subscription) {
			this.jid = jid;
			this.node = node;
			this.subscription = subscription;
		}

		public JID getJid() {
			return jid;
		}

		public String getNode() {
			return node;
		}

		public String getSubscription() {
			return subscription;
		}
		
		
	}

	public String getChildElementXML()
	{
		StringBuffer s = new StringBuffer();
		s.append("<pubsub xmlns=\"http://jabber.org/protocol/pubsub\"><subscriptions>");
		if (subscriptions != null)
		{
			for(Subscription sub : subscriptions)
			{
				s.append("<subscription node=\"");
				s.append(sub.getNode());
				s.append("\" jid=\"");
				s.append(sub.getJid().toString());
				s.append("\" subscription=\"");
				s.append(sub.getSubscription());
				s.append("\"/>");
			}
		}
		s.append("</subscriptions></pubsub>");
		return s.toString();
	}
	
	
}
