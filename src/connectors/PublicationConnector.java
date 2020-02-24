package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.MessageI;
import interfaces.PublicationCI;


/**
 * The class PublicationConnector defines the connector for
 * the PublicationCI interface
 * 
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 *
 */
public class PublicationConnector 
extends AbstractConnector 
implements PublicationCI{

	/**
	 * @see interfaces.PublicationCI#publish(MessageI, String)
	 */
	@Override
	public void publish(MessageI m, String topic) throws Exception {
		((PublicationCI)this.offering).publish(m, topic);
	}

	
	/**
	 * @see interfaces.PublicationCI#publish(MessageI, String[])
	 */
	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		((PublicationCI)this.offering).publish(m, topics);
	}

	
	/**
	 * @see interfaces.PublicationCI#publish(MessageI[], String)
	 */
	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		((PublicationCI)this.offering).publish(ms, topic);
	}

	
	/**
	 * @see interfaces.PublicationCI#publish(MessageI[], String[])
	 */
	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		((PublicationCI)this.offering).publish(ms, topics);
	}

}
