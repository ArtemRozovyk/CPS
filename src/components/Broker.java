package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import interfaces.MessageI;
import ports.BrokerPublicationInboundPort;
import ports.BrokerReceptionOutboundPort;

public class Broker extends AbstractComponent {

	protected BrokerPublicationInboundPort bpip;
	
	protected BrokerReceptionOutboundPort brop;
	
	protected String brokerReceptionOutboundPortURI;
	
	protected String brokerPublicationInboundPortURI;

	public Broker(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}

	@Override
	public void	start() throws ComponentStartException{
		
	}
	
	
	
	
	public void publish(MessageI m, String topic) throws Exception {
		brop.acceptMessage(m);
	}

	public void publish(MessageI m, String[] topics) throws Exception {

	}

	public void publish(MessageI[] ms, String topic) throws Exception {

	}

	public void publish(MessageI[] ms, String[] topics) throws Exception {

	}

}
