package ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import plugins.SubscriberReceptionPlugin;

/**
 * The class SubscriberReceptionInboundPortForPlugin implements the server side
 * port used to receive messages
 * 
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 */
public class SubscriberReceptionInboundPortForPlugin 
extends AbstractInboundPortForPlugin 
implements ReceptionCI{

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
	public SubscriberReceptionInboundPortForPlugin(String uri, String pluginURI, ComponentI owner) throws Exception {
		super(uri, ReceptionCI.class, pluginURI, owner);
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
	public SubscriberReceptionInboundPortForPlugin(String uri, ComponentI owner) throws Exception {
		super(ReceptionCI.class, uri, owner);
	}

	
	/**
	 * @see interfaces.ReceptionCI#acceptMessage(MessageI)
	 */
	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {
					@Override
					public Void call() throws Exception {
						((SubscriberReceptionPlugin)this.getServiceProviderReference()).acceptMessage(m);
						return null;
					}
				});
	}

	
	/**
	 * @see interfaces.ReceptionCI#acceptMessage(MessageI[])
	 */
	@Override
	public void acceptMessage(MessageI[] ms) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {

					@Override
					public Void call() throws Exception {
						((SubscriberReceptionPlugin)this.getServiceProviderReference()).acceptMessage(ms);
						return null;
					}
				});
	}
}
