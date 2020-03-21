package components;

import fr.sorbonne_u.components.AbstractComponent;
import interfaces.ManagementCI;
import interfaces.PublicationCI;
import message.Message;
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
        PublisherPublicationPlugin pluginPublication = new PublisherPublicationPlugin();
        pluginPublication.setPluginURI(UK_PUB_PLUGIN_URI);
        this.installPlugin(pluginPublication);

        PublisherManagementPlugin managementPlugin = new PublisherManagementPlugin();
        managementPlugin.setPluginURI(UK_MAN_PLUGIN_URI);
        this.installPlugin(managementPlugin);
        String topic;
        String msg;
        
        /*createTopic("UK");
        createTopic("London");
        createTopic("Cambridge");*/

        // 15 msg UK, 20 msg London, 35 msg Cambridge
        for (int i = 0; i < 10; i++) {
            if (i < 4) {
                topic = "UK";
                msg = "6 degrees in average in the UK";
            } else if (i < 7) {
                topic = "London";
                msg = "10 degrees in London";
            } else {
                topic = "Cambridge";
                msg = "5 degrees in Cambridge";
            }
            logMessage("Publishing message " + i + " for topic : " + topic);
            publish(new Message(msg), topic);
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

