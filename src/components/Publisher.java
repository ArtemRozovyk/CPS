package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import interfaces.ManagementCI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import message.Message;
import ports.PublisherManagementOutboundPort;
import ports.PublisherPublicationOutboundPort;

import java.util.concurrent.TimeUnit;

/**
 * Publisher component. It is used to publish messages.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant		true
 * </pre>
 */
public class Publisher extends AbstractComponent {

    protected PublisherPublicationOutboundPort ppop;
    protected PublisherManagementOutboundPort pmop;

    public final static String PUBLISHER_CLIENT_PUBLICATION_PLUGIN =
            "publisher-publication-plugin-uri";



    /**
     * Publisher creation
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	uri != null && publicationOutboundPortURI != null && managementOutboundPort != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @param uri                        uri of the component
     * @param publicationOutboundPortURI uri of publication outbound port
     * @param managementOutboundPort     uri of the management outbound port
     * @throws Exception
     */
    protected Publisher(String uri,
                        String publicationOutboundPortURI,
                        String managementOutboundPort) throws Exception {
        super(uri, 0, 1);
        addRequiredInterface(PublicationCI.class);
        addRequiredInterface(ManagementCI.class);

        //Publish the reception port (an outbound port is always local)
        this.ppop = new PublisherPublicationOutboundPort(publicationOutboundPortURI, this);
        this.ppop.localPublishPort();

        this.pmop = new PublisherManagementOutboundPort(managementOutboundPort, this);
        this.pmop.localPublishPort();

        // Install the plugin
		/*PublisherPublicationClientPlugin pluginPublication = new PublisherPublicationClientPlugin();
		pluginPublication.setPluginURI(PUBLISHER_CLIENT_PUBLICATION_PLUGIN);
		this.installPlugin(pluginPublication);*/

        if (AbstractCVM.isDistributed) {
            this.executionLog.setDirectory(System.getProperty("user.dir"));
        } else {
            this.executionLog.setDirectory(System.getProperty("user.home"));
        }
        this.tracer.setTitle("publisher");
        this.tracer.setRelativePosition(1, 0);
    }

    /**
     * Action executed by the component
     */
    @Override
    public void execute() throws Exception {

        String topic;
        String msg;
        
        /*createTopic("Colorado");
        createTopic("Denver");
        createTopic("South Park City");*/

        // 40 msg Colorado, 45 msg Denver, 35 msg South Park City
        for (int i = 0; i < 10; i++) {
            if (i < 4) {
                topic = "Colorado";
                msg = "0 degree in average in the Colorado";
            } else if (i < 7) {
                topic = "Denver";
                msg = "-4 degrees in Denver";
            } else {
                topic = "South Park City";
                msg = "-7 degrees in South Park City";
            }
            logMessage("Publishing message " + i + " for topic : " + topic);
            publish(new Message(msg), topic);
        }
    }


    /**
     * @see interfaces.PublicationCI#publish(MessageI, String)
     */
    public void publish(MessageI m, String topic) throws Exception {
        this.scheduleTask(new AbstractComponent.AbstractTask() {
            @Override
            public void run() {
                try {
                    ((Publisher)
                            this.getTaskOwner()).ppop.publish(m, topic);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * @see interfaces.PublicationCI#publish(MessageI[], String)
     */
    public void publish(MessageI[] ms, String topic) throws Exception {
        this.scheduleTask(new AbstractComponent.AbstractTask() {
            @Override
            public void run() {
                try {

                    ((Publisher)
                            this.getTaskOwner()).ppop.publish(ms, topic);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * @see interfaces.PublicationCI#publish(MessageI, String[])
     */
    public void publish(MessageI m, String[] topics) throws Exception {
        this.scheduleTask(new AbstractComponent.AbstractTask() {
            @Override
            public void run() {
                try {
                    ((Publisher)
                            this.getTaskOwner()).ppop.publish(m, topics);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * @see interfaces.PublicationCI#publish(MessageI[], String[])
     */
    public void publish(MessageI[] ms, String[] topics) throws Exception {

        this.scheduleTask(new AbstractComponent.AbstractTask() {
            @Override
            public void run() {
                try {

                    ((Publisher)
                            this.getTaskOwner()).ppop.publish(ms, topics);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 1000, TimeUnit.MILLISECONDS);
    }

    /**
     * Shutdown of the component, unpublish and destroy the ports
     */
    @Override
    public void shutdown() throws ComponentShutdownException {
        try {
            this.ppop.unpublishPort();
            this.ppop.destroyPort();
            this.pmop.unpublishPort();
            this.pmop.destroyPort();

        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }

        super.shutdown();
    }
}
