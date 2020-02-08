package components;

import fr.sorbonne_u.components.AbstractComponent;
import interfaces.MessageI;
import ports.PublisherPublicationOutboundPort;

public class Publisher extends AbstractComponent{
	
	protected PublisherPublicationOutboundPort ppop;
	
	protected String publisherPublicationOutboundPortURI;
	
	public Publisher(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}
	
	protected Publisher (String publisherPublicationOutboundPortURI) {
		super(publisherPublicationOutboundPortURI,1,0);
		this.publisherPublicationOutboundPortURI=publisherPublicationOutboundPortURI;
	}
	@Override
	public void execute() throws Exception{
		publish(new Message("Hello bro"), "nothing");
	}
	
	public void publish(MessageI m, String topic) throws Exception {
		ppop.publish(m, topic);

	}

}
