package ports;

import components.Broker;
import components.Subscriber;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.MessageI;
import interfaces.ReceptionCI;

public class SubscriberReceptionInboundPort 
extends AbstractInboundPort 
implements ReceptionCI{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SubscriberReceptionInboundPort(ComponentI owner) throws Exception {
		super(ReceptionCI.class, owner);
	}
	
	public SubscriberReceptionInboundPort(String uri, ComponentI owner) throws Exception {
		super(uri,ReceptionCI.class, owner);
	}

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.owner.handleRequestAsync(
				new AbstractComponent.AbstractService<Void>() {

					@Override
					public Void call() throws Exception {
						((Subscriber)this.getServiceOwner()).acceptMessage(m);
						return null;
					}
				});
	}

	@Override
	public void acceptMessage(MessageI[] ms) {
		// TODO Auto-generated method stub
		
	}

}
