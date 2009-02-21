package com.rummble.fireeagle.oauth;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.oauth.OAuth;

import org.xmpp.packet.JID;

public class OAuthUtils {

	
	/**
	 * Create a base string as described in http://xmpp.org/extensions/xep-0235.html
	 * @param from
	 * @param to
	 * @param consumerKey
	 * @param accessToken
	 * @param nonce
	 * @param timestamp
	 * @return
	 */
	public static String getBaseString(JID from,JID to,String consumerKey,String accessToken,String nonce,String timestamp) 
	{
		StringBuffer bs = new StringBuffer();
		bs.append("iq&");
		bs.append(OAuth.percentEncode(from.toBareJID()));
		bs.append(OAuth.percentEncode("&"));
		bs.append(OAuth.percentEncode(to.toBareJID()));
		bs.append("&");
		bs.append(OAuth.percentEncode("oauth_consumer_key=" + consumerKey + "&"));
		bs.append(OAuth.percentEncode("oauth_nonce=" + nonce + "&"));
		bs.append(OAuth.percentEncode("oauth_signature_method=HMAC-SHA1&"));
		bs.append(OAuth.percentEncode("oauth_timestamp=" + timestamp + "&"));
		bs.append(OAuth.percentEncode("oauth_token=" + accessToken + "&"));
		bs.append(OAuth.percentEncode("oauth_version=1.0"));
			
		return bs.toString();
	}
	    
	    
	/** ISO-8859-1 or US-ASCII would work, too. */
	private static final String ENCODING = OAuth.ENCODING;
	private static final String MAC_NAME = "HmacSHA1";
	public static byte[] computeSignature(String baseString,String consumerSecret,String tokenSecret) throws GeneralSecurityException, UnsupportedEncodingException 
	{
		SecretKey key = null;	
		String keyString = OAuth.percentEncode(consumerSecret) + '&' + OAuth.percentEncode(tokenSecret);
		byte[] keyBytes = keyString.getBytes(ENCODING);
		key = new SecretKeySpec(keyBytes, MAC_NAME);
		Mac mac = Mac.getInstance(MAC_NAME);
		mac.init(key);
		byte[] text = baseString.getBytes(ENCODING);
		return mac.doFinal(text);
	}
	
}
