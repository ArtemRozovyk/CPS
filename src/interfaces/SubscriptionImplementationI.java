package interfaces;

public interface SubscriptionImplementationI {

	void subscribe(String topic, String inboundPortURI);
	void subscribe(String[] topics, String inboutPortURI);
	void subscribe(String topic, MessageFilterI filter, String inboutPortURI);
	void unsubscribe(String topic, String inboundPortURI);
}
