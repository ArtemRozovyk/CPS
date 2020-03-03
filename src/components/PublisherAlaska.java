package components;

import fr.sorbonne_u.components.AbstractComponent;
import message.Message;
import plugins.PublisherManagementPlugin;
import plugins.PublisherPublicationPlugin;

public class PublisherAlaska extends AbstractComponent {
    protected final static String	ALASKA_PUB_PLUGIN_URI = "publisher-alaska-pub-plugin-uri" ;
    protected final static String	ALASKA_MAN_PLUGIN_URI = "publisher-alaska-man-plugin-uri" ;

    protected PublisherAlaska(int nbThreads, int nbSchedulableThreads) {
        super(nbThreads, nbSchedulableThreads);
    }

    protected PublisherAlaska(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);
        PublisherPublicationPlugin pluginPublication = new PublisherPublicationPlugin();
        pluginPublication.setPluginURI(ALASKA_PUB_PLUGIN_URI);
        this.installPlugin(pluginPublication);

        PublisherManagementPlugin managementPlugin = new PublisherManagementPlugin();
        managementPlugin.setPluginURI(ALASKA_MAN_PLUGIN_URI);
        this.installPlugin(managementPlugin);

        this.tracer.setTitle("publisher-alaska") ;
        this.tracer.setRelativePosition(1, 2) ;
    }
    static int k =0;
    @Override
    public void execute() throws Exception{

        String topic = "weather0";
        createTopic(topic);
        //createTopic("weather3");

        String msg = "120 degrees in Florida";
        for (int i =0; i <10;i ++) {
            if(i>4){
                topic="weather3";
                msg = "40 degrees in Alaska";
            }
            logMessage("Publishing message "+i+ " for topic : "+ topic);
            System.out.println("publishing "+k+++" "+ msg);
            publish(new Message(msg), topic);
        }
    }



    private void publish(Message message, String topic) throws Exception {
        ((PublisherPublicationPlugin)this.getPlugin(ALASKA_PUB_PLUGIN_URI)).publish(message,topic);
    }
    public void createTopic(String topic) throws Exception {
        ((PublisherManagementPlugin)this.getPlugin(ALASKA_MAN_PLUGIN_URI)).createTopic(topic);
    }

}

