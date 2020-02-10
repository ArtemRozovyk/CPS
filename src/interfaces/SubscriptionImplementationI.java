package interfaces;

public interface SubscriptionImplementationI {

	void subscribe(String topic, String inboundPortURI) throws Exception;
	void subscribe(String[] topics, String inboutPortURI) throws Exception;
	void subscribe(String topic, MessageFilterI filter, String inboutPortURI) throws Exception;
	void modifyFilet(String topic,MessageFilterI newFilter,String inboundPortUri) throws Exception;
	void unsubscribe(String topic, String inboundPortURI) throws Exception;
}
