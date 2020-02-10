package ports;

import components.Broker;
import components.Subscriber;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ManagementCI;
import interfaces.MessageFilterI;

public class BrokerManagementInboundPort 
extends AbstractInboundPort 
implements ManagementCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BrokerManagementInboundPort(Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
	}
	
	public BrokerManagementInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ManagementCI.class, owner);
	}

	@Override
	public void createTopic(String topic) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).createTopic(topic);
						return null;
					}
				});
	}

	@Override
	public void createTopics(String[] topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroyTopic(String topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTopic(String topic) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getTopics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void subscribe(String topic, String inboundPortURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(String[] topics, String inboutPortURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboutPortURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe(String topic, String inboundPortURI) {
		// TODO Auto-generated method stub
		
	}

}
