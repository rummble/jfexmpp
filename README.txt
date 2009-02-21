A test consumer TestConsumer is provided with the library.

It has four commands, subscribe, unsubscribe, subscriptions and wait. Wait, just waits for location events 
from fireeagle and prints them out.

Examples:

After setting the classpath to the jars in /lib and fexmpp.jar:

java com.rummble.fireeagle.TestConsumer -cmd subscribe -u <xmpp-user> -s <xmpp-server> -p <password-for-user> 
	-ck <consumer-key> -cs <consumer-secret> -t <token> -ts <token-secret>

java com.rummble.fireeagle.TestConsumer -cmd unsubscribe -u <xmpp-user> -s <xmpp-server> -p <password-for-user> 
	-ck <consumer-key> -cs <consumer-secret> -t <token> -ts <token-secret>
	
java com.rummble.fireeagle.TestConsumer -cmd subscriptions -u <xmpp-user> -s <xmpp-server> -p <password-for-user> 
	-ck <consumer-key> -cs <consumer-secret> -t <general-token> -ts <general-token-secret>
	
java com.rummble.fireeagle.TestConsumer -cmd wait -u <xmpp-user> -s <xmpp-server> -p <password-for-user> 