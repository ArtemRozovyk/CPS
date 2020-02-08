import components.Broker;
import components.Publisher;
import components.Subscriber;
import connectors.PublicationConnector;
import connectors.ReceptionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

/**
 * The class <code>CVM</code> implements the single JVM assembly
 */
public class CVM extends AbstractCVM{

	public CVM(boolean isDistributed) throws Exception {
		super(isDistributed);
	}
	public static final String PUBLISHER_COMPONENT_URI="my-URI-publisher";
	public static final String BROKER_COMPONENT_URI="my-URI-broker";
	public static final String SUBSCRIBER_COMPONENT_URI="my-URI-subscriber";
	
	public static final String BROKER_PUBLICATION_INBOUND_PORT="i-broker-publication";
	public static final String BROKER_RECEPTION_OUTBOUND_PORT="o-broker-reception";
	public static final String PUBLISHER_PUBLICATION_OUTBOUND_PORT="o-publisher-publication";
	public static final String SUBSCRIBER_RECEPTION_INBOUND_PORT="i-subscriber-reception";
	
	public CVM()throws Exception {
		super();
	}
	
	protected String brokerURI;
	protected String publisherURI;
	protected String subscriberURI;
	
	/**
	 * Creates the components, publishes theirs ports 
	 * and links them together
	 */
	@Override
	public void deploy() throws Exception{
		assert	!this.deploymentDone() ;
		
		//Create the Broker component
		this.brokerURI = AbstractComponent.createComponent(
				Broker.class.getCanonicalName(),
				new Object[] {BROKER_COMPONENT_URI,
						BROKER_RECEPTION_OUTBOUND_PORT,
						BROKER_PUBLICATION_INBOUND_PORT});
		
		assert this.isDeployedComponent(this.brokerURI);
		this.toggleTracing(this.brokerURI);
		this.toggleLogging(this.brokerURI);
		
		// Create the Publisher component
		this.publisherURI = AbstractComponent.createComponent(
				Publisher.class.getCanonicalName(),
				new Object[] {PUBLISHER_COMPONENT_URI,
						PUBLISHER_PUBLICATION_OUTBOUND_PORT});
		
		assert this.isDeployedComponent(this.publisherURI);
		this.toggleTracing(this.publisherURI);
		this.toggleLogging(this.publisherURI);
		
		
		//Create the Subscriber Component
		this.subscriberURI = AbstractComponent.createComponent(
				Subscriber.class.getCanonicalName(),
				new Object[] {SUBSCRIBER_COMPONENT_URI,
						SUBSCRIBER_RECEPTION_INBOUND_PORT});
		
		assert this.isDeployedComponent(this.subscriberURI);
		this.toggleTracing(this.subscriberURI);
		this.toggleLogging(this.subscriberURI);
		
		//Port connections
		//Reception
		this.doPortConnection(
				this.brokerURI,
				BROKER_RECEPTION_OUTBOUND_PORT,
				SUBSCRIBER_RECEPTION_INBOUND_PORT,
				ReceptionConnector.class.getCanonicalName());
		
		//Publication
		this.doPortConnection(
				this.publisherURI,
				PUBLISHER_PUBLICATION_OUTBOUND_PORT,
				BROKER_PUBLICATION_INBOUND_PORT,
				PublicationConnector.class.getCanonicalName());
		
		//Deployment
		super.deploy();
		assert this.deploymentDone();
	}
	
	/**
	 * Disconnect the components
	 */
	@Override
	public void shutdown() throws Exception
	{
		assert this.allFinalised();
		
		super.shutdown();
	}
	
	public static void main(String[] args)
	{
		try {
			CVM c = new CVM() ;
			c.startStandardLifeCycle(20000L) ;
			Thread.sleep(5000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
