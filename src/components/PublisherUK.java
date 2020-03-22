package components;

import fr.sorbonne_u.components.AbstractComponent;
import interfaces.ManagementCI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import message.Message;
import message.MessageFilterI;
import message.Properties;
import message.TimeStamp;
import plugins.PublisherManagementPlugin;
import plugins.PublisherPublicationPlugin;

public class PublisherUK extends AbstractComponent {
    protected final static String UK_PUB_PLUGIN_URI = "publisher-uk-pub-plugin-uri";
    protected final static String UK_MAN_PLUGIN_URI = "publisher-uk-man-plugin-uri";

    protected PublisherUK(int nbThreads, int nbSchedulableThreads) {
        super(nbThreads, nbSchedulableThreads);
    }

    protected PublisherUK(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);

        this.tracer.setTitle("publisher-uk");
        this.tracer.setRelativePosition(1, 3);
    }

    @Override
    public void execute() throws Exception {
        super.execute();
        PublisherPublicationPlugin pluginPublication = new PublisherPublicationPlugin();
        pluginPublication.setPluginURI(UK_PUB_PLUGIN_URI);
        this.installPlugin(pluginPublication);

        PublisherManagementPlugin managementPlugin = new PublisherManagementPlugin();
        managementPlugin.setPluginURI(UK_MAN_PLUGIN_URI);
        this.installPlugin(managementPlugin);
        String topic;
        /*createTopic("UK");
        createTopic("London");
        createTopic("Cambridge");*/
        Message msg;
        // 15 msg UK, 20 msg London, 35 msg Cambridge
        for (int i = 0; i < 10; i++) {
            if (i < 4) {
                topic = "UK";
                msg = new Message("6 degrees in average in the UK");
            } else if (i < 7) {
                topic = "London";
                msg = new Message("10 degrees in London");
            } else {
                topic = "Cambridge";
                TimeStamp ts = new TimeStamp(System.currentTimeMillis(), "PbUK");

                msg = new Message("PayloadUK", ts, "5 degrees in Cambridge", new Properties());
                msg.getProperties().putProp("Integer", 3);
                msg.getProperties().putProp("Long", 3L);
                msg.getProperties().putProp("Character", '3');
                msg.getProperties().putProp("Short", (short) 3);
                msg.getProperties().putProp("Byte", (byte) 3);
                msg.getProperties().putProp("Float", 3f);
                msg.getProperties().putProp("String", "3");
                msg.getProperties().putProp("Boolean", true);
                msg.getProperties().putProp("Double", 3.0);
                msg.getTimeStamp().setTime(System.currentTimeMillis());
                msg.getTimeStamp().setTimestamper("PbUK");
            }

            logMessage("Publishing message " + i + " for topic : " + topic);
            publish(msg, topic);
        }
    }


    private void publish(Message message, String topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(UK_PUB_PLUGIN_URI)).publish(message, topic);
    }

    private void publish(Message[] message, String topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(UK_PUB_PLUGIN_URI)).publish(message, topic);
    }

    private void publish(Message message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(UK_PUB_PLUGIN_URI)).publish(message, topic);
    }

    private void publish(Message[] message, String[] topic) throws Exception {
        ((PublisherPublicationPlugin) this.getPlugin(UK_PUB_PLUGIN_URI)).publish(message, topic);
    }

    public void createTopic(String topic) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(UK_MAN_PLUGIN_URI)).createTopic(topic);
    }

    public void createTopic(String[] topics) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(UK_MAN_PLUGIN_URI)).createTopic(topics);
    }

    public void destroy(String topic) throws Exception {
        ((PublisherManagementPlugin) this.getPlugin(UK_MAN_PLUGIN_URI)).destroyTopic(topic);
    }

    public boolean isTopic(String topic) throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(UK_MAN_PLUGIN_URI)).isTopic(topic);
    }

    public String[] getTopics() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(UK_MAN_PLUGIN_URI)).getTopics();
    }

}

