package components;

import connectors.ManagementConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.ports.PortI;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import ports.BrokerManagementInboundPort;
import ports.SubscriberManagementOutbondPort;
import ports.SubscriberReceptionInboundPort;

public class Subscriber extends AbstractComponent implements ReceptionCI{
	
	protected String subscriberReceptionInboundPortURI;
	protected String myManagementOutbondPortURI;
	protected String brokerManagementInboundPortURI;

	protected SubscriberManagementOutbondPort smop;
	protected BrokerManagementInboundPort bmip;
	
	public final static String	SUBSCRIBER_RECEPTION_PLUGIN =
			"subscriber-reception-plugin-uri" ;

	public Subscriber(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}
	static int pos=0;

	protected Subscriber(
			String uri,
			String managementOutboundPortURI,
			String brokerManagementInboundPortURi) throws Exception
		{
			super(uri, 1, 0);

			assert	uri != null :
						new PreconditionException("uri can't be null!") ;
			//assert	receptionInboundPortURI != null :
			//			new PreconditionException("receptionInboundPortURI can't be null!") ;


			//Publish the management outbound port
			myManagementOutbondPortURI=managementOutboundPortURI;
			this.brokerManagementInboundPortURI=brokerManagementInboundPortURi;

			this.smop = new SubscriberManagementOutbondPort(managementOutboundPortURI, this);
			this.smop.localPublishPort();
			
			// Install the plugin
			/*SubscriberReceptionPlugin receptionPlugin = new SubscriberReceptionPlugin();
			receptionPlugin.setPluginURI(SUBSCRIBER_RECEPTION_PLUGIN);
			this.installPlugin(receptionPlugin);*/

			//Publish the reception inbound port
			//PortI p = new SubscriberReceptionInboundPort(receptionInboundPortURI, this) ;
			//p.publishPort() ;

			if (AbstractCVM.isDistributed) {
				this.executionLog.setDirectory(System.getProperty("user.dir")) ;
			} else {
				this.executionLog.setDirectory(System.getProperty("user.home")) ;
			}
			this.tracer.setTitle("subscriber") ;
			this.tracer.setRelativePosition(0, pos++) ;
			Subscriber.checkInvariant(this) ;
			assert	this.isPortExisting(managementOutboundPortURI) :
						new PostconditionException("The component must have a "
								+ "port with URI " + managementOutboundPortURI) ;
			assert	this.findPortFromURI(managementOutboundPortURI).
						getImplementedInterface().equals(ReceptionCI.class) :
						new PostconditionException("The component must have a "
								+ "port with implemented interface URIProviderI") ;
			assert	this.findPortFromURI(managementOutboundPortURI).isPublished() :
						new PostconditionException("The component must have a "
								+ "port published with URI " + managementOutboundPortURI) ;
		}

	@Override
	public void start() throws ComponentStartException {
		super.start();
		try {
			this.doPortConnection(
					myManagementOutbondPortURI,
					brokerManagementInboundPortURI,
					ManagementConnector.class.getCanonicalName());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void execute() throws Exception {
		subscribe("weather0");
	}

	public void acceptMessage(MessageI m) throws Exception {
		logMessage("Getting message "+m);
	}
	
	public void acceptMessage(MessageI[] ms) throws Exception 
	{
		for (int i = 0; i < ms.length; i++) {
			logMessage("Getting message " + ms[i]);
		}
	}

	public void subscribe(String topic) throws Exception {
	    this.subscriberReceptionInboundPortURI = "subscriber-reception-inbound-port-uri-0";
	    logMessage("Subscribing to weather0");
		PortI p = new SubscriberReceptionInboundPort(subscriberReceptionInboundPortURI, this) ;
		p.publishPort() ;
		smop.subscribe(topic, subscriberReceptionInboundPortURI);
		assert	this.subscriberReceptionInboundPortURI.equals("subscriber-reception-inbound-port-uri-0") :
				new PostconditionException("The URI prefix has not "
						+ "been initialised!") ;
		assert	this.isPortExisting(subscriberReceptionInboundPortURI) :
				new PostconditionException("The component must have a "
						+ "port with URI " + subscriberReceptionInboundPortURI) ;
		assert	this.findPortFromURI(subscriberReceptionInboundPortURI).isPublished() :
				new PostconditionException("The component must have a "
						+ "port published with URI " + subscriberReceptionInboundPortURI) ;
	}
}
