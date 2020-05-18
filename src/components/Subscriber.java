package components;

import connectors.ManagementConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import interfaces.ManagementCI;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import ports.SubscriberManagementOutbondPort;
import ports.SubscriberReceptionInboundPort;

/**
 * Subscriber component. It is used to publish messages.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant		true
 * </pre>
 */
public class Subscriber extends AbstractComponent implements ReceptionCI {

    protected String subscriberReceptionInboundPortURI;
    protected String myManagementOutbondPortURI;
    protected String brokerManagementInboundPortURI;

    protected SubscriberManagementOutbondPort smop;
    protected SubscriberReceptionInboundPort srip;

    public final static String SUBSCRIBER_RECEPTION_PLUGIN =
            "subscriber-reception-plugin-uri";

    /**
     * Subscriber creation
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	nbThreads > 0
     * post	true			// no postcondition.
     * </pre>
     *
     * @param nbThreads            number of threads used by the component
     * @param nbSchedulableThreads number of schedulable threads
     */
    protected Subscriber(int nbThreads, int nbSchedulableThreads) {
        super(nbThreads, nbSchedulableThreads);
    }

    static int pos = 0;

    /**
     * Subscriber creation
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	uri != null && brokerManagementInboundPortURi != null && managementOutboundPort != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @param uri                            uri of the component
     * @param brokerManagementInboundPortURi uri of management inbound port
     * @param managementOutboundPortURI      uri of the management outbound port
     * @throws Exception
     */
    protected Subscriber(
            String uri,
            String managementOutboundPortURI,
            String brokerManagementInboundPortURi) throws Exception {
        super(uri, 1, 0);

        assert uri != null :
                new PreconditionException("uri can't be null!");
        //assert	receptionInboundPortURI != null :
        //			new PreconditionException("receptionInboundPortURI can't be null!") ;

        addOfferedInterface(ReceptionCI.class);
        addRequiredInterface(ManagementCI.class);

        //Publish the management outbound port
        myManagementOutbondPortURI = managementOutboundPortURI;
        this.brokerManagementInboundPortURI = brokerManagementInboundPortURi;

        this.smop = new SubscriberManagementOutbondPort(managementOutboundPortURI, this);
        this.smop.localPublishPort();

        this.subscriberReceptionInboundPortURI = "subscriber-reception-inbound-port-uri-0";
        srip = new SubscriberReceptionInboundPort(subscriberReceptionInboundPortURI, this);
        srip.publishPort();

        // Install the plugin
			/*SubscriberReceptionPlugin receptionPlugin = new SubscriberReceptionPlugin();
			receptionPlugin.setPluginURI(SUBSCRIBER_RECEPTION_PLUGIN);
			this.installPlugin(receptionPlugin);*/

        //Publish the reception inbound port
        //PortI p = new SubscriberReceptionInboundPort(receptionInboundPortURI, this) ;
        //p.publishPort() ;

        if (AbstractCVM.isDistributed) {
            this.executionLog.setDirectory(System.getProperty("user.dir"));
        } else {
            this.executionLog.setDirectory(System.getProperty("user.home"));
        }
        this.tracer.setTitle("subscriber");
        this.tracer.setRelativePosition(0, pos++);
        Subscriber.checkInvariant(this);
        assert this.isPortExisting(managementOutboundPortURI) :
                new PostconditionException("The component must have a "
                        + "port with URI " + managementOutboundPortURI);
        assert this.findPortFromURI(managementOutboundPortURI).
                getImplementedInterface().equals(ManagementCI.class) :
                new PostconditionException("The component must have a "
                        + "port with implemented interface URIProviderI");
        assert this.findPortFromURI(managementOutboundPortURI).isPublished() :
                new PostconditionException("The component must have a "
                        + "port published with URI " + managementOutboundPortURI);
    }

    /**
     * Starts the components, connect the ports together.
     */
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

    /**
     * Action executed by the component
     */
    @Override
    public void execute() throws Exception {
        subscribe("France"); // 15 msg
        subscribe("London"); // 20 msg
        subscribe("Denver"); // 45 msg
        subscribe("USA");
    }

    /**
     * @see interfaces.ReceptionCI#acceptMessage(MessageI)
     */
    public void acceptMessage(MessageI m) throws Exception {
        logMessage("Getting message " + m);
    }

    /**
     * @see interfaces.ReceptionCI#acceptMessage(MessageI[])
     */
    public void acceptMessage(MessageI[] ms) throws Exception {
        for (MessageI m : ms) {
            acceptMessage(m);
        }
    }

    /**
     * @see interfaces.ManagementCI#subscribe(String, String)
     */
    public void subscribe(String topic) throws Exception {
        logMessage("Subscribing to " + topic);

        smop.subscribe(topic, subscriberReceptionInboundPortURI);
        assert this.subscriberReceptionInboundPortURI.equals("subscriber-reception-inbound-port-uri-0") :
                new PostconditionException("The URI prefix has not "
                        + "been initialised!");
        assert this.isPortExisting(subscriberReceptionInboundPortURI) :
                new PostconditionException("The component must have a "
                        + "port with URI " + subscriberReceptionInboundPortURI);
        assert this.findPortFromURI(subscriberReceptionInboundPortURI).isPublished() :
                new PostconditionException("The component must have a "
                        + "port published with URI " + subscriberReceptionInboundPortURI);
    }

    /**
     * Shutdown of the component, unpublish and destroy the ports
     */
    @Override
    public void shutdown() throws ComponentShutdownException {
        try {
            this.srip.unpublishPort();
            this.srip.destroyPort();
            this.smop.unpublishPort();
            this.smop.destroyPort();

        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }

        super.shutdown();
    }
}
