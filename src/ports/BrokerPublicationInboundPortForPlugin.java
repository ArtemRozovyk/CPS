package ports;

import components.Broker;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import interfaces.MessageI;
import interfaces.PublicationCI;

/**
 * The class BrokerPublicationInboundPortForPlugin implements the server side
 * port used to publish messages
 * 
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 */
public class BrokerPublicationInboundPortForPlugin 
extends AbstractInboundPortForPlugin 
implements PublicationCI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Port creation
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre  uri != null
	 * pre	pluginURI != null
	 * pre	owner != null
	 * pre	owner.isInstalled(pluginURI)
	 * post	true			// no postcondition.
	 * </pre>
	 * 
	 * @param uri			URI of the port
	 * @param pluginURI		URI of the plugin implementing the methods to be called by the port
	 * @param owner			components that owns the port and installed the plugin
	 * @throws Exception
	 */
	public BrokerPublicationInboundPortForPlugin(String uri, String pluginURI, ComponentI owner) throws Exception {
		super(uri, PublicationCI.class, pluginURI, owner);
	}
	
	/**
	 * Port creation
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre 	uri != null
	 * pre	owner != null
	 * pre	owner.isInstalled(pluginURI)
	 * post	true			// no postcondition.
	 * </pre>
	 * 
	 * @param uri			URI of the plugin implementing the methods to be called by the port
	 * @param owner			components that owns the port and installed the plugin
	 * @throws Exception
	 */
	public BrokerPublicationInboundPortForPlugin(String uri, ComponentI owner) throws Exception {
		super(PublicationCI.class, uri, owner);
	}

	/**
	 * @see interfaces.PublicationCI#publish(MessageI, String)
	 */
	@Override
	public void publish(MessageI m, String topic) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(m, 
								topic);
						return null;
						
					}
					
				});
	}

	/**
	 * @see interfaces.PublicationCI#publish(MessageI, String[])
	 */
	@Override
	public void publish(MessageI m, String[] topics) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(m, topics);
						return null;
					}
				});
	}

	/**
	 * @see interfaces.PublicationCI#publish(MessageI[], String)
	 */
	@Override
	public void publish(MessageI[] ms, String topic) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {

					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(ms, topic);
						return null;
					}
				});
	}

	/**
	 * @see interfaces.PublicationCI#publish(MessageI[], String[])
	 */
	@Override
	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {
					@Override
					public Void call() throws Exception {
						((Broker)this.getServiceOwner()).publish(ms, topics);
						return null;
					}
				});
	}
}
