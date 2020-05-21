package components;

import fr.sorbonne_u.components.*;
import message.*;
import plugins.*;

import java.util.*;

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
public class PublisherTest extends AbstractComponent {
    protected final static String TEST_PUB_PLUGIN_URI = "publisher-test-pub-plugin-uri";
    protected final static String TEST_MAN_PLUGIN_URI = "publisher-test-man-plugin-uri";



    String uriCorrespondingBroker;

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
    protected PublisherTest(int nbThreads, int nbSchedulableThreads) {
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
    protected PublisherTest(String reflectionInboundPortURI,String brokerUri) throws Exception {
        super(reflectionInboundPortURI, 0, 1);
        this.uriCorrespondingBroker=brokerUri;
        this.tracer.setTitle("publisher-test");
        this.tracer.setRelativePosition(0, 2);
    }

    /**
     * Action executed by the component
     */
    @Override
    public void execute() throws Exception {
        super.execute();
        PublisherPublicationPlugin pluginPublication = new PublisherPublicationPlugin();
        pluginPublication.setPluginURI(TEST_PUB_PLUGIN_URI);
        this.installPlugin(pluginPublication);

        PublisherManagementPlugin managementPlugin = new PublisherManagementPlugin();
        managementPlugin.setPluginURI(TEST_MAN_PLUGIN_URI);
        this.installPlugin(managementPlugin);

        String topic;
        String msg;
        Thread.sleep(3000);

        /*createTopic("Test");
        createTopic("IDF");
        createTopic("Paris");*/
        // 15 msg Test, 15 msg IDF, 20 msg Paris
        for (int i = 0; i < 10; i++) {
            if (i < 4) {
                topic = "Test";
                msg = "13 degrees in average in Test";
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
        ((PublisherPublicationPlugin) this.getPlugin(TEST_PUB_PLUGIN_URI)).publish(message, topic);
    }
    public String getUriCorrespondingBroker() {
        return uriCorrespondingBroker;
    }
    /**
     * @see interfaces.PublicationCI#publish(interfaces.MessageI[], String)
     */
    private void publish(Message[] message, String topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(TEST_PUB_PLUGIN_URI)).publish(message, topic);
    }

    /**
     * @see interfaces.PublicationCI#publish(interfaces.MessageI, String[])
     */
    private void publish(Message message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(TEST_PUB_PLUGIN_URI)).publish(message, topic);
    }

    /**
     * @see interfaces.PublicationCI#publish(interfaces.MessageI[], String[])
     */
    private void publish(Message[] message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(TEST_PUB_PLUGIN_URI)).publish(message, topic);
    }

    /**
     * @see interfaces.ManagementCI#createTopic(String)
     */
    public void createTopic(String topic) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(TEST_MAN_PLUGIN_URI)).createTopic(topic);
    }

    /**
     * @see interfaces.ManagementCI#createTopics(String[])
     */
    public void createTopic(String[] topics) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(TEST_MAN_PLUGIN_URI)).createTopic(topics);
    }

    /**
     * @see interfaces.ManagementCI#destroyTopic(String)
     */
    public void destroy(String topic) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(TEST_MAN_PLUGIN_URI)).destroyTopic(topic);
    }

    /**
     * @see interfaces.ManagementCI#isTopicr(String)
     */
    public boolean isTopic(String topic) throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(TEST_MAN_PLUGIN_URI)).isTopic(topic);
    }

    /**
     * @see interfaces.ManagementCI#getTopics()
     */
    public String[] getTopics() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(TEST_MAN_PLUGIN_URI)).getTopics();
    }

    /**
     * @see interfaces.ManagementCI#getPublicatinPortURI()
     */
    public String getPublicatinPortURI() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(TEST_MAN_PLUGIN_URI)).getPublicatinPortURI();
    }
}

