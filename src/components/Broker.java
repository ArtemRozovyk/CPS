package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.examples.basic_cs.components.URIProvider;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIProviderI;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.ports.PortI;
import interfaces.MessageFilterI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import ports.BrokerPublicationInboundPort;
import ports.BrokerReceptionOutboundPort;

public class Broker extends AbstractComponent {

	//protected BrokerPublicationInboundPort bpip;
	
	protected BrokerReceptionOutboundPort brop;
	
	protected String brokerReceptionOutboundPortURI;
	
	protected String brokerPublicationInboundPortURI;

	protected Broker(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}
	
	protected Broker(String uri,
			String receptionOutboundPortURI,
			String publicationInboundPortURI) throws Exception
	{
		super(uri, 0, 1) ;
		
		assert	uri != null :
			new PreconditionException("uri can't be null!") ;
		assert	publicationInboundPortURI != null :
			new PreconditionException("inbound port can't be null!") ;
		
		this.brokerPublicationInboundPortURI = uri;
		
		//Publish the reception port (an outbound port is always local)
		this.brop = new BrokerReceptionOutboundPort(receptionOutboundPortURI, this);
		this.brop.localPublishPort();
		
		//Publish the publication inbound port
		PortI p = new BrokerPublicationInboundPort(publicationInboundPortURI, this);
		p.publishPort();
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		
		this.tracer.setTitle("broker") ;
		this.tracer.setRelativePosition(1, 1) ;
		
		Broker.checkInvariant(this) ;
		assert	this.brokerPublicationInboundPortURI.equals(uri) :
					new PostconditionException("The URI prefix has not "
												+ "been initialised!") ;
		assert	this.isPortExisting(publicationInboundPortURI) :
					new PostconditionException("The component must have a "
							+ "port with URI " + publicationInboundPortURI) ;
		assert	this.findPortFromURI(publicationInboundPortURI).
					getImplementedInterface().equals(PublicationCI.class) :
					new PostconditionException("The component must have a "
							+ "port with implemented interface URIProviderI") ;
		assert	this.findPortFromURI(publicationInboundPortURI).isPublished() :
					new PostconditionException("The component must have a "
							+ "port published with URI " + publicationInboundPortURI) ;
	}

	@Override
	public void	start() throws ComponentStartException{
		
	}
	
	public void publish(MessageI m, String topic) throws Exception {
		logMessage("Transferring message "+m+" to subscriber");
		brop.acceptMessage(m);
	}

	public void publish(MessageI m, String[] topics) throws Exception {

	}

	public void publish(MessageI[] ms, String topic) throws Exception {

	}

	public void publish(MessageI[] ms, String[] topics) throws Exception {

	}
	
	public void createTopic(String topic) {
		
	}
	
	public void createTopics(String[] topic) {
		
	}

	public void destroyTopic(String topic) {
		
	}

	public boolean isTopic(String topic) {
		return false;
	}

	public String[] getTopics() {
		return null;
	}

	public void subscribe(String topic, String inboundPortURI) {
		
	}

	public void subscribe(String[] topics, String inboutPortURI) {
		
	}

	public void subscribe(String topic, MessageFilterI filter, String inboutPortURI) {
		
	}

	public void unsubscribe(String topic, String inboundPortURI) {
		
	}

}
