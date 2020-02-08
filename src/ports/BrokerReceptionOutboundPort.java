package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.MessageI;
import interfaces.ReceptionCI;

public class BrokerReceptionOutboundPort 
extends AbstractOutboundPort 
implements ReceptionCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BrokerReceptionOutboundPort( ComponentI owner) throws Exception {
		super(ReceptionCI.class, owner);
	}
	
	public BrokerReceptionOutboundPort( String uri, ComponentI owner) throws Exception {
		super(uri,ReceptionCI.class, owner);
	}
	

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		((ReceptionCI)this.connector).acceptMessage(m);
	}

	@Override
	public void acceptMessage(MessageI[] ms) throws Exception {
		((ReceptionCI)this.connector).acceptMessage(ms);
		
	}

}
