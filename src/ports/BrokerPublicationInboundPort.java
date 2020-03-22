package ports;

import components.Broker;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.MessageI;
import interfaces.PublicationCI;

public class BrokerPublicationInboundPort 
extends AbstractInboundPort 
implements PublicationCI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BrokerPublicationInboundPort(ComponentI owner) throws Exception {

		super(PublicationCI.class, owner);
	}
	
	public BrokerPublicationInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri,PublicationCI.class, owner);
	}

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(m, 
								topic);
						return null;
						
					}
					
				});
	}

	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(m, topics);
						return null;
					}
				});
	}

	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(ms, topic);
						return null;
					}
				});
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(ms, topics);
						return null;
					}
				});
	}
}
