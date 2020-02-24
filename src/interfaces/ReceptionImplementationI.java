package interfaces;

/**
 * The interface ReceptionImplementationI defines the interface
 * required by a component that needs to receive messages
 */
public interface ReceptionImplementationI {
	
	/**
	 * Receive one message
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	m != null			
	 * post	true 					// no post conditions
	 * </pre>
	 * 
	 * @param m			the message to receive
	 * @throws Exception
	 */
	void  acceptMessage(MessageI m) throws Exception;
	
	/**
	 * Receive multiple messages
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	ms.length &gt; 0			
	 * post	true 					// no post conditions
	 * </pre>
	 * 
	 * @param ms
	 * @throws Exception
	 */
	void  acceptMessage(MessageI[] ms) throws Exception;
}
