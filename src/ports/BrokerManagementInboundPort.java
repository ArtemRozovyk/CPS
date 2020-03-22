package ports;

import components.Broker;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ManagementCI;
import message.MessageFilterI;

/**
 * The class BrokerManagementInboundPort defines the inbound port exposing
 * the interface ManagementCI for the Subscriber and Publisher components
 *
 *<p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 */
public class BrokerManagementInboundPort 
extends AbstractInboundPort 
implements ManagementCI {

	private static final long serialVersionUID = 1L;

	/**
	 * Create the port for a given owner
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	owner instanceof Broker
	 * post	true			// no postcondition.
	 * </pre>
	 * 
	 * @param owner		component owning the port
	 * @throws Exception
	 */
	public BrokerManagementInboundPort(ComponentI owner) throws Exception {
		super(ManagementCI.class, owner);
	}
	
	/**
	 * Create the port for a given owner and a given uri
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	uri != null and owner instanceof Broker
	 * post	true			// no postcondition.
	 * </pre>
	 * 
	 * @param uri		URI under which the port will be published
	 * @param owner		component owning the port
	 * @throws Exception
	 */
	public BrokerManagementInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri, ManagementCI.class, owner);
	}

	/**
	 * @see interfaces.ManagementCI#createTopic(String)
	 */
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

	/**
	 * @see interfaces.ManagementCI#createTopics(String[])
	 */
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

	/**
	 * @see interfaces.ManagementCI#destroyTopic(String)
	 */
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

	/**
	 * @see interfaces.ManagementCI#isTopic(String)
	 */
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

	/**
	 * @see interfaces.ManagementCI#getTopics()
	 */
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

	/**
	 * @see interfaces.ManagementCI#getPublicatinPortURI()
	 */
    @Override
    public String getPublicatinPortURI() throws Exception {
        return this.owner.handleRequestSync(
				new AbstractComponent.AbstractService<String>(){

					@Override
					public String call() throws Exception {
						((Broker)this.getServiceOwner()).getPublicationPortURI();
						return null;
					}
				
			});
    }

    /**
	 * @see interfaces.ManagementCI#subscribe(String, String)
	 */
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

	/**
	 * @see interfaces.ManagementCI#subscribe(String[], String)
	 */
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

	/**
	 * @see interfaces.ManagementCI#subscribe(String, MessageFilterI, String)
	 */
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

	/**
	 * @see interfaces.ManagementCI#unsubscribe(String, String)
	 */
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

	/**
	 * @see interfaces.ManagementCI#modifyFilter(String, MessageFilterI, String)
	 */
	@Override
	public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).modifyFilter(topic, newFilter, inboundPortUri);
						return null;
					}
				});
	}

}
