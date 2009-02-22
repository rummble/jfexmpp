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

----

The TestConsumer class has a few extra options that may be useful.

 -debug  This switches on the Smack debug. You must have the smackx-debug.jar in the classpath. If you don't use this then you can leave the smackx-debug.jar out of the classpath. See IgniteRealtimes documentation about this.

 -fs <Fireeagle server>  The host name of the Fireeagle XMPP server. Assumed to be fireeagle.com if not specified.

 -n <node prefix> The node prefix used in the calls. Assumed to be 
/api/0.1/user/ if not specified.

----

Dependencies:

Uses Smack and Whack libraries from IgniteRealtime. http://www.igniterealtime.org/

Uses OAuth library  mainly for percent encoding. http://code.google.com/p/oauth/

Uses dom4j to create the custom XML. http://www.dom4j.org/

Uses commons-codec for base64 encoding. http://commons.apache.org/codec/

Uses log4j for logging. http://logging.apache.org/log4j/1.2/index.html

