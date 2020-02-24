package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.MessageI;
import interfaces.PublicationCI;


/**
 * The class PublisherPublicationOutboundPort implements the outbound port
 * of a publisher that requires the publication services from the PublicationCI
 * interface
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 */
public class PublisherPublicationOutboundPort 
extends AbstractOutboundPort 
implements PublicationCI {

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
	public PublisherPublicationOutboundPort(ComponentI owner) throws Exception {
		super(PublicationCI.class, owner);
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
	public PublisherPublicationOutboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, PublicationCI.class, owner);
	}

	/**
	 * @see interfaces.PublicationCI#publish(MessageI, String)
	 */
	@Override
	public void publish(MessageI m, String topic) throws Exception {
		((PublicationCI)this.connector).publish(m, topic);
	}

	
	/**
	 * @see interfaces.PublicationCI#publish(MessageI, String[])
	 */
	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		((PublicationCI)this.connector).publish(m, topics);
	}

	
	/**
	 * @see interfaces.PublicationCI#publish(MessageI[], String)
	 */
	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		((PublicationCI)this.connector).publish(ms, topic);
	}

	
	/**
	 * @see interfaces.PublicationCI#publish(MessageI[], String[])
	 */
	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		((PublicationCI)this.connector).publish(ms, topics);
	}
	
	

}
