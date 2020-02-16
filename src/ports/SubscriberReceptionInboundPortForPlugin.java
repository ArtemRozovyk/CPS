package ports;

import components.Subscriber;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.forplugins.AbstractInboundPortForPlugin;
import interfaces.MessageI;
import interfaces.ReceptionCI;

public class SubscriberReceptionInboundPortForPlugin 
extends AbstractInboundPortForPlugin 
implements ReceptionCI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubscriberReceptionInboundPortForPlugin(String uri, String pluginURI, ComponentI owner) throws Exception {
		super(uri, ReceptionCI.class, pluginURI, owner);
	}
	
	public SubscriberReceptionInboundPortForPlugin(String uri, ComponentI owner) throws Exception {
		super(ReceptionCI.class, uri, owner);
	}

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {

					@Override
					public Void call() throws Exception {
						((Subscriber)this.getServiceOwner()).acceptMessage(m);
						return null;
					}
				});
	}

	@Override
	public void acceptMessage(MessageI[] ms) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>(this.pluginURI) {

					@Override
					public Void call() throws Exception {
						((Subscriber)this.getServiceOwner()).acceptMessage(ms);
						return null;
					}
				});
	}
}
