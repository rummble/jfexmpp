package com.rummble.fireeagle.smack.packet;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.dom4j.Namespace;
import org.dom4j.tree.DefaultElement;
import org.jivesoftware.smack.packet.IQ;
import org.xmpp.packet.JID;

import com.rummble.fireeagle.FireeagleConsumer;
import com.rummble.fireeagle.oauth.OAuthUtils;

public class PubSubUnsubscribeOAuth extends IQ {

	private static Logger logger = Logger.getLogger(PubSubUnsubscribeOAuth.class.getName());
	
	private static final Base64 BASE64 = new Base64();
	
	JID from;
	JID to;
	DefaultElement unsub;
	
	public PubSubUnsubscribeOAuth()
	{
		
	}
	
	public PubSubUnsubscribeOAuth(JID from,JID to)
	{
		this.setType(IQ.Type.SET);
		this.from = from;
		this.to = to;
		this.setTo(to.toString());
		this.setFrom(from.toBareJID());
	}
	
	public boolean createOauthElement(String consumerKey,String consumerSecret,String token,String tokenSecret)
	{
		try
		{
			String nonce = "" + System.nanoTime();
			String timestamp = "" + (System.currentTimeMillis() / 1000);
			
			Namespace subNS = new Namespace("","http://jabber.org/protocol/pubsub");
			unsub = new DefaultElement("pubsub",subNS);
			DefaultElement subscribe = new DefaultElement("unsubscribe",subNS);
			subscribe.addAttribute("node", FireeagleConsumer.getPubsubUserNode() + token);
			subscribe.addAttribute("jid", from.toBareJID());
			
			
			Namespace ns = new Namespace("","urn:xmpp:oauth");
			DefaultElement oauth = new DefaultElement("oauth",ns);
			//Consumer key
			DefaultElement consumerKeyE = new DefaultElement("oauth_consumer_key",ns);
			consumerKeyE.addText(consumerKey);
			oauth.add(consumerKeyE);
			//token
			DefaultElement tokenE = new DefaultElement("oauth_token",ns);
			tokenE.addText(token);
			oauth.add(tokenE);
			//Nonce
			DefaultElement nonceE = new DefaultElement("oauth_nonce",ns);
			nonceE.addText(nonce);
			oauth.add(nonceE);
			// Signature
			DefaultElement signatureE = new DefaultElement("oauth_signature",ns);
			String baseString = OAuthUtils.getBaseString(from,to,consumerKey,token,nonce,timestamp);
			String signature = new String(BASE64.encode(OAuthUtils.computeSignature(baseString,consumerSecret,tokenSecret)));
			//signatureE.addText(OAuth.percentEncode(signature));
			signatureE.addText(signature);
			oauth.add(signatureE);
			//signature method
			DefaultElement methodE = new DefaultElement("oauth_signature_method",ns);
			methodE.addText("HMAC-SHA1");
			oauth.add(methodE);
			//timestamp
			DefaultElement timeE = new DefaultElement("oauth_timestamp",ns);
			timeE.addText(timestamp);
			oauth.add(timeE);
			
			//version
			DefaultElement versionE = new DefaultElement("oauth_version",ns);
			versionE.addText("1.0");
			oauth.add(versionE);
			
			unsub.add(subscribe);
			unsub.add(oauth);
			
			logger.info("Created:" + unsub.toString());
			return true;
			
		}
		catch (IOException e)
    	{
    		logger.error("Exception in subscribe for token " + token,e);
    		return false;
    	} catch (GeneralSecurityException e)
    	{
    		logger.error("Exception in subscribe for " + token,e);
    		return false;
		}
	}
	
	
	
	
	 public String getChildElementXML()
	 {
		 return unsub.asXML();
	 }

}
