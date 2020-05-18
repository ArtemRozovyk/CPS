package components;

import fr.sorbonne_u.components.AbstractComponent;
import message.Message;
import plugins.PublisherManagementPlugin;
import plugins.PublisherPublicationPlugin;

import java.util.Arrays;

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
public class PublisherFrance extends AbstractComponent {
    protected final static String FRANCE_PUB_PLUGIN_URI = "publisher-france-pub-plugin-uri";
    protected final static String FRANCE_MAN_PLUGIN_URI = "publisher-france-man-plugin-uri";

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
     * @param nbThreads            number of threads used by the component
     * @param nbSchedulableThreads number of schedulable threads
     */
    protected PublisherFrance(int nbThreads, int nbSchedulableThreads) {
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
     * @param reflectionInboundPortURI uri of the owner inbound port
     * @throws Exception
     */
    protected PublisherFrance(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);

        this.tracer.setTitle("publisher-france");
        this.tracer.setRelativePosition(1, 1);
    }

    /**
     * Action executed by the component
     */
    @Override
    public void execute() throws Exception {
        super.execute();
        PublisherPublicationPlugin pluginPublication = new PublisherPublicationPlugin();
        pluginPublication.setPluginURI(FRANCE_PUB_PLUGIN_URI);
        this.installPlugin(pluginPublication);

        PublisherManagementPlugin managementPlugin = new PublisherManagementPlugin();
        managementPlugin.setPluginURI(FRANCE_MAN_PLUGIN_URI);
        this.installPlugin(managementPlugin);

        String topic;
        String msg;
        
        /*createTopic("France");
        createTopic("IDF");
        createTopic("Paris");*/
        // 15 msg France, 15 msg IDF, 20 msg Paris
        for (int i = 0; i < 10; i++) {
            if (i < 4) {
                topic = "France";
                msg = "13 degrees in average in France";
            } else if (i < 7) {
                topic = "IDF";
                msg = "10 degrees in average in IDF";
            } else {
                topic = "Paris";
                msg = "6 degrees in Paris";
            }
            logMessage("Publishing message " + i + " for topic : " + topic);
            publish(new Message(msg), topic);

        }

        Thread.sleep(2000);
        logMessage("Topics available :" + Arrays.toString(getTopics()));
        createTopic("Acapulco");
        logMessage("Topic created : " + "Acapulco");
        Thread.sleep(1000);
        logMessage("Topics available :" + Arrays.toString(getTopics()));
        destroy("Acapulco");
        logMessage("Topic destroyed : " + "Acapulco");
        Thread.sleep(1000);
        logMessage("Topics available :" + Arrays.toString(getTopics()));
        logMessage("USA is a topic : " + isTopic("USA"));
        logMessage("Acapulco is a topic : " + isTopic("Acapulco"));


    }


    /**
     * @see interfaces.PublicationCI#publish(interfaces.MessageI, String)
     */
    private void publish(Message message, String topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(FRANCE_PUB_PLUGIN_URI)).publish(message, topic);
    }

    /**
     * @see interfaces.PublicationCI#publish(interfaces.MessageI[], String)
     */
    private void publish(Message[] message, String topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(FRANCE_PUB_PLUGIN_URI)).publish(message, topic);
    }

    /**
     * @see interfaces.PublicationCI#publish(interfaces.MessageI, String[])
     */
    private void publish(Message message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(FRANCE_PUB_PLUGIN_URI)).publish(message, topic);
    }

    /**
     * @see interfaces.PublicationCI#publish(interfaces.MessageI[], String[])
     */
    private void publish(Message[] message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(FRANCE_PUB_PLUGIN_URI)).publish(message, topic);
    }

    /**
     * @see interfaces.ManagementCI#createTopic(String)
     */
    public void createTopic(String topic) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(FRANCE_MAN_PLUGIN_URI)).createTopic(topic);
    }

    /**
     * @see interfaces.ManagementCI#createTopics(String[])
     */
    public void createTopic(String[] topics) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(FRANCE_MAN_PLUGIN_URI)).createTopic(topics);
    }

    /**
     * @see interfaces.ManagementCI#destroyTopic(String)
     */
    public void destroy(String topic) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(FRANCE_MAN_PLUGIN_URI)).destroyTopic(topic);
    }

    /**
     * @see interfaces.ManagementCI#isTopic(String)
     */
    public boolean isTopic(String topic) throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(FRANCE_MAN_PLUGIN_URI)).isTopic(topic);
    }

    /**
     * @see interfaces.ManagementCI#getTopics()
     */
    public String[] getTopics() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(FRANCE_MAN_PLUGIN_URI)).getTopics();
    }

    /**
     * @see interfaces.ManagementCI#getPublicatinPortURI()
     */
    public String getPublicatinPortURI() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(FRANCE_MAN_PLUGIN_URI)).getPublicatinPortURI();
    }
}

