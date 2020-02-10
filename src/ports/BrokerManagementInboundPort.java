package ports;

import components.Broker;
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
	public void createTopics(String[] topic) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).createTopics(topic);
						return null;
					}
				});
	}

	@Override
	public void destroyTopic(String topic) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).destroyTopic(topic);
						return null;
					}
				});
	}

	@Override
	public boolean isTopic(String topic) throws Exception {
		return this.owner.handleRequestSync(
			new AbstractComponent.AbstractService<Boolean>(){

				@Override
				public Boolean call() throws Exception {
					((Broker)this.getServiceOwner()).isTopic(topic);
					return false;
				}
			
		});
	}

	@Override
	public String[] getTopics() throws Exception {
		return this.owner.handleRequestSync(
				new AbstractComponent.AbstractService<String[]>(){

					@Override
					public String[] call() throws Exception {
						((Broker)this.getServiceOwner()).getTopics();
						return null;
					}
				
			});
	}

	@Override
	public void subscribe(String topic, String inboundPortURI) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).subscribe(topic,inboundPortURI);
						return null;
					}
				});
	}

	@Override
	public void subscribe(String[] topics, String inboutPortURI) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).subscribe(topics, inboutPortURI);
						return null;
					}
				});
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboutPortURI) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).subscribe(topic, filter, inboutPortURI);
						return null;
					}
				});
	}

	@Override
	public void unsubscribe(String topic, String inboundPortURI) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).unsubscribe(topic, inboundPortURI);
						return null;
					}
				});
	}

}
