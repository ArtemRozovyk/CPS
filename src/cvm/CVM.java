package cvm;
import components.Broker;
import components.Publisher;
import components.PublisherAlaska;
import components.Subscriber;
import connectors.ManagementConnector;
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
	public static final String PUBLISHER_ALASKA_COMPONENT_URI="my-URI-publisher-alaska";
	public static final String BROKER_COMPONENT_URI="my-URI-broker";
	public static final String SUBSCRIBER1_COMPONENT_URI="my-URI-subscriber1";
	public static final String SUBSCRIBER2_COMPONENT_URI="my-URI-subscriber2";

	public static final String BROKER_PUBLICATION_INBOUND_PORT="i-broker-publication";
	public static final String BROKER_MANAGEMENT_INBOUND_PORT="i-broker-management";
	public static final String PUBLISHER_MANAGEMENT_INBOUND_PORT="i-publisher-management";
	public static final String BROKER_RECEPTION_OUTBOUND_PORT="o-broker-reception";
	public static final String PUBLISHER_PUBLICATION_OUTBOUND_PORT="o-publisher-publication";
	public static final String SUBSCRIBER1_MANAGEMENT_OUTBOUND_PORT="o-subscriber1-management";
	public static final String SUBSCRIBER2_MANAGEMENT_OUTBOUND_PORT="o-subscriber2-management";

	public CVM()throws Exception {
		super();
	}
	
	protected String brokerURI;
	protected String publisherURI;
	protected String publisherAlaksaURI;
	protected String subscriberURI1;
	protected String subscriberURI2;

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
						BROKER_PUBLICATION_INBOUND_PORT,
						BROKER_MANAGEMENT_INBOUND_PORT});
		
		assert this.isDeployedComponent(this.brokerURI);
		this.toggleTracing(this.brokerURI);
		this.toggleLogging(this.brokerURI);
		
		// Create the Publisher component
		this.publisherURI = AbstractComponent.createComponent(
				Publisher.class.getCanonicalName(),
				new Object[] {PUBLISHER_COMPONENT_URI,
						PUBLISHER_PUBLICATION_OUTBOUND_PORT,
						PUBLISHER_MANAGEMENT_INBOUND_PORT});
		
		assert this.isDeployedComponent(this.publisherURI);
		this.toggleTracing(this.publisherURI);
		this.toggleLogging(this.publisherURI);
		
		
		//Create the Subscriber1 Component
		this.subscriberURI1 = AbstractComponent.createComponent(
				Subscriber.class.getCanonicalName(),
				new Object[] {SUBSCRIBER1_COMPONENT_URI,
						SUBSCRIBER1_MANAGEMENT_OUTBOUND_PORT,
						BROKER_MANAGEMENT_INBOUND_PORT
				});
		
		assert this.isDeployedComponent(this.subscriberURI1);
		this.toggleTracing(this.subscriberURI1);
		this.toggleLogging(this.subscriberURI1);


		//Create the Subscriber1 Component
		this.subscriberURI2 = AbstractComponent.createComponent(
				Subscriber.class.getCanonicalName(),
				new Object[] {SUBSCRIBER2_COMPONENT_URI,
						SUBSCRIBER2_MANAGEMENT_OUTBOUND_PORT
						,BROKER_MANAGEMENT_INBOUND_PORT
				});

		assert this.isDeployedComponent(this.subscriberURI2);
		this.toggleTracing(this.subscriberURI2);
		this.toggleLogging(this.subscriberURI2);


/*
	Exception :Attempt to connect a server component port i-broker-management
		//Conect the ports
		this.doPortConnection(
				this.brokerURI,
				BROKER_MANAGEMENT_INBOUND_PORT,
				SUBSCRIBER1_MANAGEMENT_OUTBOUND_PORT,
				ManagementConnector.class.getCanonicalName());

		this.doPortConnection(
				this.brokerURI,
				BROKER_MANAGEMENT_INBOUND_PORT,
				SUBSCRIBER2_MANAGEMENT_OUTBOUND_PORT,
				ManagementConnector.class.getCanonicalName());*/

		 this.doPortConnection(
				this.publisherURI,
				PUBLISHER_PUBLICATION_OUTBOUND_PORT,
				BROKER_PUBLICATION_INBOUND_PORT,
				PublicationConnector.class.getCanonicalName());

		//Port connections
		//Reception IT IS DONE WHEN SUBSCRIBING
	/*	this.doPortConnection(
				this.brokerURI,
				BROKER_RECEPTION_OUTBOUND_PORT,
				SUBSCRIBER_RECEPTION_INBOUND_PORT,
				ReceptionConnector.class.getCanonicalName());*/
		
		//Publication
		
		//Deployment



		//TODO PLUGIN TESTING


		this.publisherAlaksaURI= AbstractComponent.createComponent(
				PublisherAlaska.class.getCanonicalName(),
				new Object[] {PUBLISHER_ALASKA_COMPONENT_URI});



		assert this.isDeployedComponent(this.publisherAlaksaURI);
		this.toggleTracing(this.publisherAlaksaURI);
		this.toggleLogging(this.publisherAlaksaURI);











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
