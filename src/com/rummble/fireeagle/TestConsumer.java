package com.rummble.fireeagle;

import java.util.Set;

import org.apache.log4j.BasicConfigurator;

import com.rummble.fireeagle.location.LocationEvent;

public class TestConsumer implements FireeagleConsumerListener {

	private static final String CMD_SUBSCRIBE = "subscribe";
	private static final String CMD_UNSUBSCRIBE = "unsubscribe";
	private static final String CMD_SUBSCRIPTIONS = "subscriptions";
	private static final String CMD_WAIT = "wait";
	private boolean finished = false;
	
	
	public void waitForEvents() throws InterruptedException
	{
		while(!finished)
		{
			Thread.sleep(500);
		}
	}
	
	
	public void locationEvent(LocationEvent loc)
	{
		if (loc != null)
		{
			System.out.println(loc.toString());
		}
	}
	
	public void subscribeSuccess(String token)
	{ 
		System.out.println("Subscribe success for " + token);
		finished = true;
	}
	public void subscribeFailure(String token,int errorCode,String message) 
	{ 
		System.out.println("Subcribe failure for token " + token + " error code:" + errorCode + " message:" + message);
		finished = true;
	}
	public void subscriptionsSuccess(Set<String> tokens) 
	{ 
		for(String token: tokens)
		{
			System.out.println("Subscription to " + token);
		}
		finished = true;
	}
	
	public void subscriptionsFailure(String token,int errorCode,String message) 
	{ 
		System.out.println("Subcriptions failure for token " + token + " error code:" + errorCode + " message:" + message);
		finished = true;
	}
	public void unsubscribeSuccess(String token) 
	{ 
		System.out.println("Unsubscribe success for " + token);
		finished = true;
	}
	public void unsubscribeFailure(String token,int errorCode,String message) 
	{ 
		System.out.println("Unsubcriptions failure for token " + token + " error code:" + errorCode + " message:" + message);
		finished = true;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String token = null;
		String tokenSecret = null;
		String consumerKey = null;
		String consumerSecret = null;		
		String cmd = null;
		String username = null;
		String password = null;
		String server = null;
		String feServer = "fireeagle.com";
		String resource = "fePubSub";
		String node = null;
		boolean smackDebug = false; // requires smackx-debug.jar in classpath
		
		// Set up a simple configuration that logs on the console.
	     BasicConfigurator.configure();

		
		for(int i=0;i<args.length;i++)
		{
			if ("-cmd".equals(args[i]))
				cmd = args[++i];
			else if ("-t".equals(args[i]))
				token = args[++i];
			else if ("-ts".equals(args[i]))
				tokenSecret = args[++i];
			else if ("-ck".equals(args[i]))
				consumerKey = args[++i];
			else if ("-cs".equals(args[i]))
				consumerSecret = args[++i];
			else if ("-u".equals(args[i]))
				username = args[++i];
			else if ("-p".equals(args[i]))
				password = args[++i];
			else if ("-s".equals(args[i]))
				server = args[++i];
			else if ("-fs".equals(args[i]))
				feServer = args[++i];
			else if ("-r".equals(args[i]))
				resource = args[++i];
			else if ("-n".equals(args[i]))
				node = args[++i];
			else if ("-debug".equals(args[i]))
				smackDebug = true;
		}
		
		if (cmd == null)
		{
			System.out.println("Must have cmd: -cmd subscribe|unsubscribe|subscriptions|wait");
			return;
		}
		if (username == null)
		{
			System.out.println("Must supply XMPP username: -u");
			return;
		}
		if (password == null)
		{
			System.out.println("Must supply password for XMPP username: -p");
			return;
		}
		if (server == null)
		{
			System.out.println("Must supply an XMPP server to log into: -s");
			return;
		}
		
		if (CMD_SUBSCRIBE.equals(cmd) || CMD_UNSUBSCRIBE.equals(cmd) || CMD_SUBSCRIPTIONS.equals(cmd))
		{
			if (token == null)
			{
				System.out.println("Must supply token: -t");
				return;				
			}
			if (tokenSecret == null)
			{
				System.out.println("Must supply token secret: -ts");
				return;
			}
			if (consumerKey == null)
			{
				System.out.println("Must supply consumer key: -ck");
				return;
			}
			if (tokenSecret == null)
			{
				System.out.println("Must supply token secret: -ts");
				return;
			}
		}
		else if (!CMD_WAIT.equals(cmd))
		{
			System.out.println("Unknown cmd: -cmd subscribe|unsubscribe|subscriptions|wait");
			return;
		}

		if (node != null)
			FireeagleConsumer.setPubsubUserNode(node);
		
		FireeagleConsumer consumer = new FireeagleConsumer(username,password,resource,server,feServer);
		if (smackDebug)
			consumer.setDebug(true);
		
		System.out.print("Connecting to server " + server + "...");
        if (!consumer.connect())
        {
        	System.out.println("Failed to connect to server");
        	return;
        }
        System.out.println("success");
        
        TestConsumer tc = new TestConsumer();
        consumer.setListener(tc);
        
        if (CMD_SUBSCRIBE.equals(cmd))
        {
        	consumer.subscribe(consumerKey,consumerSecret, token, tokenSecret);
        }
        else if (CMD_UNSUBSCRIBE.equals(cmd))
        {
        	consumer.unsubscribe(consumerKey,consumerSecret, token, tokenSecret);
        }
        else if (CMD_SUBSCRIPTIONS.equals(cmd))
        {
        	consumer.subscriptions(consumerKey,consumerSecret, token, tokenSecret);
        }
        
        try {
			tc.waitForEvents();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
