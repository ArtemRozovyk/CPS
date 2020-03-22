package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import interfaces.ManagementCI;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import message.MessageFilterI;
import plugins.PublisherManagementPlugin;
import plugins.SubscriberManagementPlugin;
import plugins.SubscriberReceptionPlugin;

/**
 * Variant of the Subscriber component. It has a different behavior.
 * It is used to publish messages.
 * 
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 */
public class SubscriberCuba extends AbstractComponent implements ReceptionCI {
    protected final static String SUB_CUBA_RECEPT_PLUGIN_URI = "sub-cuba-recept-uri";
    protected final static String SUB_CUBA_MANAGE_PLUGIN_URI = "sub-cuba-manage-uri";

    static int i = 0;
    static final Object iGuard = new Object();
    private String subscriberReceptionInboundPortURI = "subscriber-cuba-reception-inbound-port-uri-1";


    /**
     * Subscriber creation
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	reflectionInboundPortURI != null
	 * post	true			// no postcondition.
	 * </pre>
	 * 
     * @param reflectionInboundPortURI		uri of the owner inbound port
     * @throws Exception
     */
    protected SubscriberCuba(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);
        this.tracer.setTitle("sub-cuba");
        this.tracer.setRelativePosition(0, 1);
    }


    /**
     * Action executed by the component
     */
    @Override
    public void execute() throws Exception {
        super.execute();
        SubscriberManagementPlugin smp = new SubscriberManagementPlugin();
        smp.setPluginURI(SUB_CUBA_MANAGE_PLUGIN_URI);
        this.installPlugin(smp);

        //unstailling the plgin that will create port and publish it
        SubscriberReceptionPlugin subscriberPlugin
                = new SubscriberReceptionPlugin(subscriberReceptionInboundPortURI);
        subscriberPlugin.setPluginURI(SUB_CUBA_RECEPT_PLUGIN_URI);
        this.installPlugin(subscriberPlugin);


        /*
        SubscriberReceptionPlugin subscriberPlugin = new SubscriberReceptionPlugin();
        subscriberPlugin.setPluginURI(SUB_CUBA_RECEPT_PLUGIN_URI);
        this.installPlugin(subscriberPlugin);
        */

        //test scenario

        subscribe("Colorado"); // 40 msg
        subscribe("USA"); // 35 msg
        unsubscribe("USA");
        subscribe("IDF"); // 15 msg
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
    @Override
    public void acceptMessage(MessageI[] ms) throws Exception {
    	for (MessageI m : ms) {
			acceptMessage(m);
		}
    }

    /**
     * @see interfaces.ManagementCI#unsubscribe(String, String)
     */
    public void unsubscribe(String topic) throws Exception {


        logMessage("Unsubscribing from " + topic);
     //   System.out.println(((SubscriberManagementPlugin) this.getPlugin(
       //         SUB_CUBA_MANAGE_PLUGIN_URI)));
        ((SubscriberManagementPlugin) this.getPlugin(
                SUB_CUBA_MANAGE_PLUGIN_URI)).unsubscribe(topic, subscriberReceptionInboundPortURI);
    }

    /**
     * @see interfaces.ManagementCI#subscribe(String, String)
     */
    public void subscribe(String topic) throws Exception {


        logMessage("Subscribing to " + topic);

        //sending the port over the Broker
        ((SubscriberManagementPlugin) this.getPlugin(
                SUB_CUBA_MANAGE_PLUGIN_URI)).subscribe(topic, subscriberReceptionInboundPortURI);


        assert this.subscriberReceptionInboundPortURI.equals("subscriber-cuba-reception-inbound-port-uri-1") :
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
     * @see interfaces.ManagementCI#subscribe(String[], String)
     */
    public void subscribe(String[] topics) throws Exception {
    	((SubscriberManagementPlugin) this.getPlugin(SUB_CUBA_MANAGE_PLUGIN_URI)).subscribe(topics, subscriberReceptionInboundPortURI);
    }
    
    /**
     * @see interfaces.ManagementCI#subscribe(String, MessageFilterI, String)
     */
    public void subscribe(String topic, MessageFilterI filter) throws Exception {
    	((SubscriberManagementPlugin) this.getPlugin(SUB_CUBA_MANAGE_PLUGIN_URI)).subscribe(topic, filter, subscriberReceptionInboundPortURI);
    }

    /**
     * @see interfaces.ManagementCI#modifyFilter(String, MessageFilterI, String)
     */
    public void modifyFilter(String topic, MessageFilterI newFilter) throws Exception {
    	((SubscriberManagementPlugin) this.getPlugin(SUB_CUBA_MANAGE_PLUGIN_URI)).modifyFilter(topic, newFilter, subscriberReceptionInboundPortURI);
    }
    
    /**
     * @see interfaces.ManagementCI#createTopic(String)
     */
    public void createTopic(String topic) throws Exception {
    	((SubscriberManagementPlugin) this.getPlugin(SUB_CUBA_MANAGE_PLUGIN_URI)).createTopic(topic);
    }
    
    /**
     * @see interfaces.ManagementCI#createTopics(String[])
     */
    public void createTopic(String[] topics) throws Exception {
    	((SubscriberManagementPlugin) this.getPlugin(SUB_CUBA_MANAGE_PLUGIN_URI)).createTopic(topics);
    }
    
    /**
     * @see interfaces.ManagementCI#destroyTopic(String)
     */
    public void destroy(String topic) throws Exception {
    	((SubscriberManagementPlugin) this.getPlugin(SUB_CUBA_MANAGE_PLUGIN_URI)).destroyTopic(topic);
    }
    
    /**
     * @see interfaces.ManagementCI#isTopic(String)
     */
    public boolean isTopic(String topic) throws Exception {
    	return ((SubscriberManagementPlugin) this.getPlugin(SUB_CUBA_MANAGE_PLUGIN_URI)).isTopic(topic);
    }
    
    /**
     * @see interfaces.ManagementCI#getTopics()
     */
    public String[] getTopics() throws Exception {
    	return ((SubscriberManagementPlugin) this.getPlugin(SUB_CUBA_MANAGE_PLUGIN_URI)).getTopics();
    }
    
    /**
     * @see interfaces.ManagementCI#getPublicatinPortURI()
     */
    public String getPublicatinPortURI() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(SUB_CUBA_MANAGE_PLUGIN_URI)).getPublicatinPortURI();
    }


}
