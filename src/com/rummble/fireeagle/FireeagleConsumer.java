package com.rummble.fireeagle;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.XMPPError;
import org.jivesoftware.smack.provider.ProviderManager;
import org.xmpp.packet.JID;

import com.rummble.fireeagle.location.LocationEvent;
import com.rummble.fireeagle.smack.packet.PubSubLocationEvent;
import com.rummble.fireeagle.smack.packet.PubSubSubscribeOAuth;
import com.rummble.fireeagle.smack.packet.PubSubSubscription;
import com.rummble.fireeagle.smack.packet.PubSubSubscriptions;
import com.rummble.fireeagle.smack.packet.PubSubSubscriptionsOAuth;
import com.rummble.fireeagle.smack.packet.PubSubUnsubscribeOAuth;
import com.rummble.fireeagle.smack.packet.PubSubSubscriptions.Subscription;
import com.rummble.fireeagle.smack.provider.PubSubLocationEventProvider;
import com.rummble.fireeagle.smack.provider.PubSubProvider;

public class FireeagleConsumer implements PacketListener {

	private static Logger logger = Logger.getLogger( FireeagleConsumer.class.getName() );
	
	private String username;
	private String password;
	private String resource;
	private String server;
	private static String pubsubUserNode;
	JID client;
	JID fireeagle;
	private XMPPConnection conn;
	private FireeagleConsumerListener listener;
	
	// Maps of packetIds to tokens for messages we have sent and are waiting an answer
	private Map<String,String> pendingSubscribes;
	private Map<String,String> pendingUnsubscribes;
	private Map<String,String> pendingSubscriptions;
	
	public FireeagleConsumer(String username,String password,String resource,String server,String fireeagleServer) 
	{
		pubsubUserNode = "/api/0.1/user/"; 
		this.username = username;
		this.password = password;
		this.resource = resource;
		this.server = server;
		this.fireeagle = new JID(fireeagleServer);
		
		// add an IQ provider for the pubsub IQ packets we will handle
		ProviderManager.getInstance().addIQProvider("pubsub", PubSubProvider.getNS(), new PubSubProvider());
		// add a packet extension for the location events that come back from FireEagle
		ProviderManager.getInstance().addExtensionProvider("event", PubSubLocationEventProvider.getNS(), new PubSubLocationEventProvider());

		pendingSubscribes = Collections.synchronizedMap(new HashMap<String,String>());
		pendingUnsubscribes = Collections.synchronizedMap(new HashMap<String,String>());
		pendingSubscriptions = Collections.synchronizedMap(new HashMap<String,String>());		
	}
	
	public void setListener(FireeagleConsumerListener listener)
	{
		this.listener = listener;
	}
	
	/**
	 * 
	 * Must be called before a connection is created.
	 * 
	 * @param val - debug true/false
	 */
	public void setDebug(boolean val)
	{
		XMPPConnection.DEBUG_ENABLED = val;
	}
	
	/**
	 * 
	 * Connect to XMPP server
	 * 
	 * @return - whether we are connected
	 */
	public boolean connect() 
	{
		try
		{
			conn=new XMPPConnection(server);
			conn.connect();
			if (conn.isConnected())
			{
				// Required hack for smack 3_1_0 bug
				// May not be needed for all XMPP servers but doesn't seem to cause harm
				SASLAuthentication.supportSASLMechanism("PLAIN", 0);
				
				conn.login(username, password,resource);
				client = new JID(conn.getUser());
				conn.addPacketListener(this, null);
				return true;
			}
			else
			{
				logger.error("Bad connection on createConnection");
				return false;
			}
		}
		catch (XMPPException ex)
		{
			logger.warn("Error creating connection " + ex.getMessage(),ex);
			return false;
		}
	}
	
	
	/**
	 * Subscribe to node for token 
	 * @param consumer
	 * @param token
	 * @param tokenSecret
	 */
	public void subscribe(String consumerKey,String consumerSecret,String token,String tokenSecret)
	{
		
		PubSubSubscribeOAuth sub = new PubSubSubscribeOAuth(client,fireeagle);
		if(sub.createOauthElement(consumerKey,consumerSecret,token,tokenSecret))
		{
			logger.info(sub.toXML());
			pendingSubscribes.put(sub.getPacketID(),token);
			conn.sendPacket(sub);
		}
		else
			logger.error("Failed to create subscription packet for ");
	}
	
	/**
	 * Unsubscribe from node for token
	 * @param consumer
	 * @param token
	 * @param tokenSecret
	 */
	public void unsubscribe(String consumerKey,String consumerSecret,String token,String tokenSecret)
	{
		
		PubSubUnsubscribeOAuth sub = new PubSubUnsubscribeOAuth(client,fireeagle);
		if(sub.createOauthElement(consumerKey,consumerSecret,token,tokenSecret))
		{
			logger.info(sub.toXML());
			pendingUnsubscribes.put(sub.getPacketID(),token);
			conn.sendPacket(sub);
		}
		else
			logger.error("Failed to create unsubscription packet for ");
	}
	
	/**
	 * Get subscriptions
	 * @param consumer
	 * @param token
	 * @param tokenSecret
	 */
	public void subscriptions(String consumerKey,String consumerSecret,String token,String tokenSecret)
	{
		
		PubSubSubscriptionsOAuth sub = new PubSubSubscriptionsOAuth(client,fireeagle);
		if(sub.createOauthElement(consumerKey,consumerSecret,token,tokenSecret))
		{
			logger.info(sub.toXML());
			pendingSubscriptions.put(sub.getPacketID(),token);
			conn.sendPacket(sub);
		}
		else
			logger.error("Failed to create subscription packet for ");
	}
	
	/**
	 * Process a packet from Smack.
	 */
	public void processPacket(Packet packet) 
	{
		logger.info("Received:" + packet.toXML());
		if (packet instanceof PubSubSubscription)
		{
			pendingSubscribes.remove(packet.getPacketID());
			PubSubSubscription subscription = (PubSubSubscription) packet;
			logger.info("Got subscription packet for " + subscription.getNode());
			if (listener != null)
				listener.subscribeSuccess(subscription.getNode());
		}
		else if (packet instanceof Message)
		{
			Message message = (Message) packet;
			PacketExtension pe = message.getExtension(PubSubLocationEventProvider.getNS());
			if (pe != null && pe instanceof PubSubLocationEvent)
			{
				LocationEvent locEvent = ((PubSubLocationEvent) pe).getLoc();
				if (listener != null)
				{
					listener.locationEvent(locEvent);
				}
			}
		}
		else if (packet instanceof PubSubSubscriptions)
		{
			pendingSubscriptions.remove(packet.getPacketID());
			PubSubSubscriptions subs = (PubSubSubscriptions) packet;
			logger.info("Got subscriptions packet");
			if (listener != null)
			{
				// return tokens to listener
				HashSet<String> tokens = new HashSet<String>();
				for(Subscription sub : subs.getSubscriptions())
				{
					String node = sub.getNode();
					String token = node.substring(pubsubUserNode.length());
					if (node.startsWith(pubsubUserNode))
						tokens.add(token);
				}
				listener.subscriptionsSuccess(tokens);
			}
		}
		else
		{
			if (packet instanceof IQ)
			{
				IQ iq = (IQ) packet;
				String packetID = packet.getPacketID();
				if (pendingSubscribes.containsKey(packetID))
				{
					if(iq.getType() == IQ.Type.ERROR && listener != null)
					{
						XMPPError error = packet.getError();
						if (error != null)
							listener.subscribeFailure(pendingSubscribes.get(packetID),error.getCode(), error.getMessage());
						else
							listener.subscribeFailure(pendingSubscribes.get(packetID),-1, "Unknown Error");
					}
					pendingSubscribes.remove(packetID);
				}
				else if (pendingUnsubscribes.containsKey(packetID))
				{
					if(iq.getType() == IQ.Type.ERROR && listener != null)
					{
						XMPPError error = packet.getError();
						if (error != null)
							listener.unsubscribeFailure(pendingUnsubscribes.get(packetID),error.getCode(), error.getMessage());
						else
							listener.unsubscribeFailure(pendingUnsubscribes.get(packetID),-1, "Unknown Error");
					}
					else if (iq.getType() == IQ.Type.RESULT && listener != null)
					{
						listener.unsubscribeSuccess(pendingUnsubscribes.get(packetID));
					}
					pendingUnsubscribes.remove(packetID);
				}
				else if (pendingSubscriptions.containsKey(packetID))
				{
					if(iq.getType() == IQ.Type.ERROR && listener != null)
					{
						XMPPError error = packet.getError();
						if (error != null)
							listener.subscriptionsFailure(pendingSubscriptions.get(packetID),error.getCode(), error.getMessage());
						else
							listener.subscriptionsFailure(pendingSubscriptions.get(packetID),-1, "Unknown Error");
					}
					pendingSubscriptions.remove(packetID);
				}
			}
		}
		
	}

	public static String getPubsubUserNode() {
		return pubsubUserNode;
	}

	public static void setPubsubUserNode(String node) {
		pubsubUserNode = node;
	}

}
