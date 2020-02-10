package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import interfaces.MessageI;
import message.Message;
import ports.PublisherPublicationOutboundPort;

public class Publisher extends AbstractComponent{
	
	protected PublisherPublicationOutboundPort ppop;
	
	//protected String publisherPublicationOutboundPortURI;

	protected Publisher(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}

	protected Publisher(String uri,
			String publicationOutboundPortURI) throws Exception
	{
		super(uri, 0, 1) ;
		
		//Publish the reception port (an outbound port is always local)
		this.ppop = new PublisherPublicationOutboundPort(publicationOutboundPortURI, this);
		this.ppop.localPublishPort();
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		
		this.tracer.setTitle("publisher") ;
		this.tracer.setRelativePosition(1, 0) ;
	}
	
	
	@Override
	public void execute() throws Exception{
		publish(new Message("Hello bro"), "nothing");
	}
	
	public void publish(MessageI m, String topic) throws Exception {
		logMessage("Publishing message "+m);

		ppop.publish(m, topic);

	}

}
