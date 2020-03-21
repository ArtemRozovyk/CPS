package components;

import fr.sorbonne_u.components.AbstractComponent;
import interfaces.ManagementCI;
import interfaces.PublicationCI;
import message.Message;
import plugins.PublisherManagementPlugin;
import plugins.PublisherPublicationPlugin;

public class PublisherAlaska extends AbstractComponent {
    protected final static String ALASKA_PUB_PLUGIN_URI = "publisher-alaska-pub-plugin-uri";
    protected final static String ALASKA_MAN_PLUGIN_URI = "publisher-alaska-man-plugin-uri";

    protected PublisherAlaska(int nbThreads, int nbSchedulableThreads) {
        super(nbThreads, nbSchedulableThreads);
    }

    protected PublisherAlaska(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);

        this.tracer.setTitle("publisher-alaska");
        this.tracer.setRelativePosition(1, 2);
    }

    static int k = 0;

    @Override
    public void execute() throws Exception {
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
        Thread.sleep(3000);
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
    }


    private void publish(Message message, String topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(ALASKA_PUB_PLUGIN_URI)).publish(message, topic);
    }

    private void publish(Message[] message, String topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(ALASKA_PUB_PLUGIN_URI)).publish(message, topic);
    }

    private void publish(Message message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(ALASKA_PUB_PLUGIN_URI)).publish(message, topic);
    }

    private void publish(Message[] message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(ALASKA_PUB_PLUGIN_URI)).publish(message, topic);
    }

    public void createTopic(String topic) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(ALASKA_MAN_PLUGIN_URI)).createTopic(topic);
    }


}

