package components;

import connectors.ManagementConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.ports.PortI;
import interfaces.ManagementCI;
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
	protected SubscriberReceptionInboundPort srip;

	public final static String	SUBSCRIBER_RECEPTION_PLUGIN =
			"subscriber-reception-plugin-uri" ;

    protected Subscriber(int nbThreads, int nbSchedulableThreads) {
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

            addOfferedInterface(ReceptionCI.class);
            addRequiredInterface(ManagementCI.class);

			//Publish the management outbound port
			myManagementOutbondPortURI=managementOutboundPortURI;
			this.brokerManagementInboundPortURI=brokerManagementInboundPortURi;

			this.smop = new SubscriberManagementOutbondPort(managementOutboundPortURI, this);
			this.smop.localPublishPort();

            this.subscriberReceptionInboundPortURI = "subscriber-reception-inbound-port-uri-0";
            srip = new SubscriberReceptionInboundPort(subscriberReceptionInboundPortURI, this) ;
            srip.publishPort() ;

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
						getImplementedInterface().equals(ManagementCI.class) :
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
		subscribe("France"); // 15 msg
		subscribe("London"); // 20 msg
		subscribe("Denver"); // 45 msg
		subscribe("USA");
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
	    logMessage("Subscribing to " + topic);

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
    @Override
    public void	shutdown() throws ComponentShutdownException
    {
        try {
            this.srip.unpublishPort() ;
            this.srip.destroyPort() ;
            this.smop.unpublishPort() ;
            this.smop.destroyPort() ;

        } catch (Exception e) {
            throw new ComponentShutdownException(e) ;
        }

        super.shutdown() ;
    }
}
