package com.rummble.fireeagle.smack.packet;

import org.jivesoftware.smack.packet.IQ;

import com.rummble.fireeagle.smack.provider.PubSubProvider;



public class PubSubSubscription extends IQ {

	private String node;
	private String jid;
	private String subscription;
	
	 public String getChildElementXML()
	 {
		 return "<pubsub xmlns=\"" + PubSubProvider.getNS() + "\"><subscription node=\"" + node +"\" jid=\"" + jid + "\" subscription=\"" + subscription +"\"/></pubsub>";
	 }

	public String getNode() {
		return node;
	}

	public void setNode(String node) {
		this.node = node;
	}

	public String getJid() {
		return jid;
	}

	public void setJid(String jid) {
		this.jid = jid;
	}

	public String getSubscription() {
		return subscription;
	}

	public void setSubscription(String subscription) {
		this.subscription = subscription;
	}

	
	
}
