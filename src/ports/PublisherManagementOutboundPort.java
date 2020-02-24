package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ManagementCI;
import message.MessageFilterI;

/**
 * The class PublisherPublicationOutboundPort implements the outbound port
 * of a publisher that requires the management services from the ManagementCI
 * interface
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 */
public class PublisherManagementOutboundPort 
extends AbstractOutboundPort 
implements ManagementCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the port with the given owner
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	owner != null
	 * post	true			// no postcondition.
	 * </pre>
	 * 
	 * @param owner		owner of the port
	 * @throws Exception
	 */
	public PublisherManagementOutboundPort( ComponentI owner) throws Exception {
		super(ManagementCI.class, owner);
	}
	
	/**
	 * Create the port with the given URI and the given owner
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	uri != null and owner != null
	 * post	true			// no postcondition.
	 * </pre>
	 * 
	 * @param uri		uri of the port
	 * @param owner		owner of the port
	 * @throws Exception
	 */
	public PublisherManagementOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ManagementCI.class, owner);
	}

	/**
	 * @see interfaces.ManagementCI#createTopic(String)
	 */
	@Override
	public void createTopic(String topic) throws Exception {
		((ManagementCI)this.connector).createTopic(topic);
	}

	/**
	 * @see interfaces.ManagementCI#createTopics(String[])
	 */
	@Override
	public void createTopics(String[] topics) throws Exception {
		((ManagementCI)this.connector).createTopics(topics);
	}

	/**
	 * @see interfaces.ManagementCI#destroyTopic(String)
	 */
	@Override
	public void destroyTopic(String topic) throws Exception {
		((ManagementCI)this.connector).destroyTopic(topic);
	}

	/**
	 * @see interfaces.ManagementCI#isTopic(String)
	 */
	@Override
	public boolean isTopic(String topic) throws Exception {
		return ((ManagementCI)this.connector).isTopic(topic);
	}

	/**
	 * @see interfaces.ManagementCI#getTopics()
	 */
	@Override
	public String[] getTopics() throws Exception {
		return ((ManagementCI)this.connector).getTopics();
	}

	/**
	 * @see interfaces.ManagementCI#subscribe(String, String)
	 */
	@Override
	public void subscribe(String topic, String inboundPortURI) throws Exception {
		((ManagementCI)this.connector).subscribe(topic, inboundPortURI);
		
	}

	/**
	 * @see interfaces.ManagementCI#subscribe(String[], String)
	 */
	@Override
	public void subscribe(String[] topics, String inboutPortURI) throws Exception {
		((ManagementCI)this.connector).subscribe(topics, inboutPortURI);
	}

	/**
	 * @see interfaces.ManagementCI#subscribe(String, MessageFilterI, String)
	 */
	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboutPortURI) throws Exception {
		((ManagementCI)this.connector).subscribe(topic, filter, inboutPortURI);
		
	}

	/**
	 * @see interfaces.ManagementCI#unsubscribe(String, String)
	 */
	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		((ManagementCI)this.connector).unsubscribe(topic, inboundPortURI);
	}

	/**
	 * @see interfaces.ManagementCI#modifyFilter(String, MessageFilterI, String)
	 */
	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		((ManagementCI)this.connector).modifyFilter(topic, newFilter, inboundPortUri);
	}
 
}