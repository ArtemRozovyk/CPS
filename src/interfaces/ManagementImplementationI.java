package interfaces;

/**
 * The interface ManagementImplementationI defines the interface
 * required by a component that needs to manage the topics
 */
public interface ManagementImplementationI {
	
	/**
	 * Creates a topic
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	topic != null			
	 * post	true 					// no post conditions
	 * </pre>
	 * 
	 * @param topic		name of the topic
	 * @throws Exception
	 */
	void createTopic(String topic) throws Exception;
	
	/**
	 * Creates multiple topics
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	topic.length &gt; 0		
	 * post	true 					// no post conditions
	 * </pre>
	 * @param topic		array of topic names
	 * @throws Exception
	 */
	void createTopics(String[] topic) throws Exception;
	
	/**
	 * Destroy a single topic
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	topic != null			
	 * post	true 					// no post conditions
	 * </pre>
	 * @param topic		name of the topic
	 * @throws Exception
	 */
	void destroyTopic(String topic) throws Exception;
	
	/**
	 * Check if a specific topic exists
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	topic != null			
	 * post	ret == false or ret == true
	 * </pre>
	 * @param topic		name of the topic
	 * @return		true if the topic exists, false otherwise
	 * @throws Exception
	 */
	boolean isTopic(String topic) throws Exception;
	
	/**
	 * Get the existing topics
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	true					// no pre conditions			
	 * post	true 					// no post conditions
	 * </pre>
	 * @return 		the array of existing strings
	 * @throws Exception
	 */
	String[] getTopics() throws Exception;




	String getPublicatinPortURI() throws Exception;

}
