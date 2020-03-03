package components;

import fr.sorbonne_u.components.AbstractComponent;
import message.Message;
import plugins.PublisherManagementPlugin;
import plugins.PublisherPublicationPlugin;

public class PublisherFrance extends AbstractComponent {
    protected final static String	FRANCE_PUB_PLUGIN_URI = "publisher-france-pub-plugin-uri" ;
    protected final static String	FRANCE_MAN_PLUGIN_URI = "publisher-france-man-plugin-uri" ;

    protected PublisherFrance(int nbThreads, int nbSchedulableThreads) {
        super(nbThreads, nbSchedulableThreads);
    }

    protected PublisherFrance(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);
        PublisherPublicationPlugin pluginPublication = new PublisherPublicationPlugin();
        pluginPublication.setPluginURI(FRANCE_PUB_PLUGIN_URI);
        this.installPlugin(pluginPublication);

        PublisherManagementPlugin managementPlugin = new PublisherManagementPlugin();
        managementPlugin.setPluginURI(FRANCE_MAN_PLUGIN_URI);
        this.installPlugin(managementPlugin);

        this.tracer.setTitle("publisher-france") ;
        this.tracer.setRelativePosition(2, 1) ;
    }

    @Override
    public void execute() throws Exception{

        String topic;
        String msg;
        
        /*createTopic("France");
        createTopic("IDF");
        createTopic("Paris");*/
        
        // 15 msg France, 15 msg IDF, 20 msg Paris
        for (int i = 0; i < 50;i ++) {
        	if (i < 15){
        		topic = "France";
        		msg = "13 degrees in average in France";
        	}
        	else if(i >= 15 && i < 30){
            	topic = "IDF";
        		msg = "10 degrees in average in IDF";
            }
            else {
            	topic = "Paris";
        		msg = "6 degrees in Paris";
            }
            logMessage("Publishing message "+i+ " for topic : "+ topic);
            publish(new Message(msg), topic);
        }
    }



    private void publish(Message message, String topic) throws Exception {
        ((PublisherPublicationPlugin)this.getPlugin(FRANCE_PUB_PLUGIN_URI)).publish(message,topic);
    }
    public void createTopic(String topic) throws Exception {
        ((PublisherManagementPlugin)this.getPlugin(FRANCE_MAN_PLUGIN_URI)).createTopic(topic);
    }

}

