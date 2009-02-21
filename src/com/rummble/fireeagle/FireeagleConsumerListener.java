package com.rummble.fireeagle;

import java.util.Set;

import com.rummble.fireeagle.location.LocationEvent;

public interface FireeagleConsumerListener {

	public void subscribeSuccess(String token);
	public void subscribeFailure(String token,int errorCode,String message);
	public void subscriptionsSuccess(Set<String> tokens);
	public void subscriptionsFailure(String token,int errorCode,String message);
	public void unsubscribeSuccess(String token);
	public void unsubscribeFailure(String token,int errorCode,String message);
	public void locationEvent(LocationEvent loc);
	
	
}
