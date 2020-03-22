package components;

import fr.sorbonne_u.components.AbstractComponent;
import interfaces.ManagementCI;
import interfaces.PublicationCI;
import message.Message;
import message.MessageFilterI;
import plugins.PublisherManagementPlugin;
import plugins.PublisherPublicationPlugin;

/**
 * Variant of the Publisher component. It has a different behavior.
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
public class PublisherAlaska extends AbstractComponent {
    protected final static String ALASKA_PUB_PLUGIN_URI = "publisher-alaska-pub-plugin-uri";
    protected final static String ALASKA_MAN_PLUGIN_URI = "publisher-alaska-man-plugin-uri";

    /**
   	 * Publisher creation
   	 * 
   	 * <p><strong>Contract</strong></p>
   	 * 
   	 * <pre>
   	 * pre	nbThreads > 0
   	 * post	true			// no postcondition.
   	 * </pre>
   	 * 
   	 * @param nbThreads					number of threads used by the component
   	 * @param nbSchedulableThreads		number of schedulable threads
   	 */
    protected PublisherAlaska(int nbThreads, int nbSchedulableThreads) {
        super(nbThreads, nbSchedulableThreads);
    }

    /**
     * Publisher creation
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
    protected PublisherAlaska(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);

        this.tracer.setTitle("publisher-alaska");
        this.tracer.setRelativePosition(1, 2);
    }

    static int k = 0;

    /**
     * Action executed by the component
     */
    @Override
    public void execute() throws Exception {
        super.execute();
        PublisherPublicationPlugin pluginPublication = new PublisherPublicationPlugin();
        pluginPublication.setPluginURI(ALASKA_PUB_PLUGIN_URI);
        this.installPlugin(pluginPublication);

        PublisherManagementPlugin managementPlugin = new PublisherManagementPlugin();
        managementPlugin.setPluginURI(ALASKA_MAN_PLUGIN_URI);
        this.installPlugin(managementPlugin);
        String topic;
        String msg;
        
        /*createTopic("USA");
        createTopic("Alaska");
        createTopic("Anchorage");*/
        Thread.sleep(1900);
        // 35 msg USA, 40 msg Alaska, 15 msg Anchorage


        for (int i = 0; i < 10; i++) {
            if (i < 4) {
                topic = "USA";
                msg = "10 degrees in average in the US : " + i;
            } else if (i < 7) {
                topic = "Alaska";
                msg = "-10 degrees in average in Alaska";
            } else {
                topic = "Anchorage";
                msg = "-20 degrees in Anchorage";
            }
            logMessage("Publishing message " + i + " for topic : " + topic);
            publish(new Message(msg), topic);
        }



        Thread.sleep(12000);
        logMessage("Publishing for multiple topics ");
        Message mmsg = new Message("A message for multiple topics");
        publish(mmsg, new String[]{"IDF", "Denver"});

        Thread.sleep(2000);

        logMessage("Publishing mulpiple messages for multiple topics ");
        Message mmsg1 = new Message("Message sent in Array 1");
        Message mmsg2 = new Message("Message sent in Array 2");
        publish(new Message[]{mmsg1, mmsg2}, new String[]{"IDF", "Denver"});

    }

    /**
     * @see interfaces.PublicationCI#publish(interfaces.MessageI, String)
     */
    private void publish(Message message, String topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(ALASKA_PUB_PLUGIN_URI)).publish(message, topic);
    }

    /**
     * @see interfaces.PublicationCI#publish(interfaces.MessageI[], String)
     */
    private void publish(Message[] message, String topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(ALASKA_PUB_PLUGIN_URI)).publish(message, topic);
    }

    /**
     * @see interfaces.PublicationCI#publish(interfaces.MessageI, String[])
     */
    private void publish(Message message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(ALASKA_PUB_PLUGIN_URI)).publish(message, topic);
    }

    /**
     * @see interfaces.PublicationCI#publish(interfaces.MessageI[], String[])
     */
    private void publish(Message[] message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(ALASKA_PUB_PLUGIN_URI)).publish(message, topic);
    }

    /**
     * @see interfaces.ManagementCI#createTopic(String)
     */
    public void createTopic(String topic) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(ALASKA_MAN_PLUGIN_URI)).createTopic(topic);
    }

    /**
     * @see interfaces.ManagementCI#createTopics(String[])
     */
    public void createTopic(String[] topics) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(ALASKA_MAN_PLUGIN_URI)).createTopic(topics);
    }

    /**
     * @see interfaces.ManagementCI#destroyTopic(String)
     */
    public void destroy(String topic) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(ALASKA_MAN_PLUGIN_URI)).destroyTopic(topic);
    }

    /**
     * @see interfaces.ManagementCI#isTopic(String)
     */
    public boolean isTopic(String topic) throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(ALASKA_MAN_PLUGIN_URI)).isTopic(topic);
    }

    /**
     * @see interfaces.ManagementCI#getTopics()
     */
    public String[] getTopics() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(ALASKA_MAN_PLUGIN_URI)).getTopics();
    }
    
    /**
     * @see interfaces.ManagementCI#getPublicatinPortURI()
     */
    public String getPublicatinPortURI() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(ALASKA_MAN_PLUGIN_URI)).getPublicatinPortURI();
    }
}

