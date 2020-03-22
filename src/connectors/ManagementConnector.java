package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ManagementCI;
import message.MessageFilterI;

/**
 * The class ManagementConnector defines the connector for
 * the ManagementCI interface
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant	true
 * </pre>
 */
public class ManagementConnector 
extends AbstractConnector 
implements ManagementCI  {

	/**
	 * @see interfaces.ManagementImplementationI#createTopic(String)
	 */
	@Override
	public void createTopic(String topic) throws Exception {
		((ManagementCI)this.offering).createTopic(topic);
	}

	
	/**
	 * @see interfaces.ManagementImplementationI#createTopics(String[])
	 */
	@Override
	public void createTopics(String[] topic) throws Exception {
		((ManagementCI)this.offering).createTopics(topic);
	}

	
	/**
	 * @see interfaces.ManagementImplementationI#destroyTopic(String)
	 */
	@Override
	public void destroyTopic(String topic) throws Exception {
		((ManagementCI)this.offering).destroyTopic(topic);
	}

	
	/**
	 * @see interfaces.ManagementImplementationI#isTopic(String)
	 */
	@Override
	public boolean isTopic(String topic) throws Exception {
		return ((ManagementCI)this.offering).isTopic(topic);
	}

	/**
	 * @see interfaces.ManagementImplementationI#getTopics()
	 */
	@Override
	public String[] getTopics() throws Exception {
		return ((ManagementCI)this.offering).getTopics();
	}

    @Override
    public String getPublicatinPortURI() throws Exception {
        return null;
    }

    /**
	 * @see interfaces.SubscriptionImplementationI#subscribe(String, String)
	 */
	@Override
	public void subscribe(String topic, String inboundPortURI) throws Exception {
		((ManagementCI)this.offering).subscribe(topic, inboundPortURI);
	}

	
	/**
	 * @see interfaces.SubscriptionImplementationI#subscribe(String[], String)
	 */
	@Override
	public void subscribe(String[] topics, String inboutPortURI) throws Exception {
		((ManagementCI)this.offering).subscribe(topics, inboutPortURI);
	}

	/**
	 * @see interfaces.SubscriptionImplementationI#subscribe(String, MessageFilterI, String)
	 */
	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboutPortURI) throws Exception {
		((ManagementCI)this.offering).subscribe(topic, filter, inboutPortURI);
	}

	/**
	 * @see interfaces.SubscriptionImplementationI#unsubscribe(String, String)
	 */
	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		((ManagementCI)this.offering).unsubscribe(topic, inboundPortURI);
	}

	/**
	 * @see interfaces.SubscriptionImplementationI#modifyFilter(String, MessageFilterI, String)
	 */
	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		((ManagementCI)this.offering).modifyFilter(topic, newFilter, inboundPortUri);
		
	}


	@Override
	public String getPublicatinPortURI() throws Exception {
		return ((ManagementCI)this.offering).getPublicatinPortURI();
	}
	
}
