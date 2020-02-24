package interfaces;

/**
 * The interface PublicationsImplementationI defines the interface 
 * required by a component that needs to publish messages
 */
public interface PublicationsImplementationI {
	
	/**
	 * Publish one message with a topic
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	m != null and topic != null			
	 * post	true 					// no post conditions
	 * </pre>
	 * 
	 * @param m			the message to publish
	 * @param topic		the topic related to the message
	 * @throws Exception
	 */
	void publish(MessageI m, String topic) throws Exception;
	
	/**
	 * Publish one message with multiples topics
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	m != null and topics.length &gt; 0			
	 * post	true 					// no post conditions
	 * </pre>
	 * 
	 * @param m			the message to publish
	 * @param topics	the topics related to the message
	 * @throws Exception
	 */
	void publish(MessageI m, String[] topics) throws Exception;
	
	/**
	 * Publish multiple message related to one topic
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	ms.length &gt; 0 and topic != null			
	 * post	true 					// no post conditions
	 * </pre>
	 * 
	 * @param ms		the messages to publish
	 * @param topic		the topic related to the messages
	 * @throws Exception
	 */
	void publish(MessageI[] ms, String topic) throws Exception;
	
	/**
	 * Publish multiple messages with multiples topics
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	topics.length &gt; 0 and ms.length &gt; 0			
	 * post	true 					// no post conditions
	 * </pre>
	 * 
	 * @param ms		the messages to publish
	 * @param topics	the topics related to the messages
	 * @throws Exception
	 */
	void publish(MessageI[] ms, String[] topics) throws Exception;

}
