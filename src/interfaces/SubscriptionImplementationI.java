package interfaces;

import message.MessageFilterI;

/**
 * The interface SubscriptionImplementationI defines the interfaces
 * required by a component that needs to subscribe to topics
 *
 */
public interface SubscriptionImplementationI {

	/**
	 * Subscribe to a topic
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	topic != null and inboundPortURI != null		
	 * post	true 					// no post conditions
	 * </pre>
	 * @param topic				the name of the topic you want to subscribe to
	 * @param inboundPortURI 	the URI of the inbound port of the component
	 * @throws Exception
	 */
	void subscribe(String topic, String inboundPortURI) throws Exception;
	
	/**
	 * Subscribe to multiple topics
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	topic.length &gt; 0 and inboundPortURI != null		
	 * post	true 					// no post conditions
	 * </pre>
	 * @param topics			array of the topics you want to subscribe to
	 * @param inboutPortURI		the URI of the inbound port of the component
	 * @throws Exception
	 */
	void subscribe(String[] topics, String inboutPortURI) throws Exception;
	
	/**
	 * Subscribe to a topic with a filter
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	topic != null and filter != null and inboundPortURI != null		
	 * post	true 					// no post conditions
	 * </pre>
	 * 
	 * @param topic			the name of the topic you want to subscribe to
	 * @param filter		the filter you want to apply to the topic
	 * @param inboutPortURI	the URI of the inbound port of the component
	 * @throws Exception
	 */
	void subscribe(String topic, MessageFilterI filter, String inboutPortURI) throws Exception;
	
	/**
	 * Modify a filter applied to a topic
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	topic != null and newFilter != null and inboundPortURI != null		
	 * post	true 					// no post conditions
	 * </pre>
	 * 
	 * @param topic			the name of the topic you want to subscribe to
	 * @param newFilter		the filter you want to apply to the topic
	 * @param inboutPortURI	the URI of the inbound port of the component
	 * @throws Exception
	 */
	void modifyFilter(String topic,MessageFilterI newFilter,String inboundPortUri) throws Exception;
	
	/**
	 * Unsubscribe from a given topic
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	topic != null and inboundPortURI != null		
	 * post	true 					// no post conditions
	 * </pre>
	 * 
	 * @param topic			the name of the topic you want to unsubscribe from
	 * @param inboutPortURI	the URI of the inbound port of the component
	 * @throws Exception
	 */
	void unsubscribe(String topic, String inboundPortURI) throws Exception;
}
