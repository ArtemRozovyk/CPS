package components;

import fr.sorbonne_u.components.AbstractComponent;
import message.Message;
import plugins.PublisherPlugin;

public class PublisherAlaska extends AbstractComponent {
    protected final static String	ALASKA_PLUGIN_URI = "publisher-alaska-plugin-uri" ;

    protected PublisherAlaska(int nbThreads, int nbSchedulableThreads) {
        super(nbThreads, nbSchedulableThreads);
    }

    protected PublisherAlaska(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);
        PublisherPlugin pluginPublication = new PublisherPlugin();
        pluginPublication.setPluginURI(ALASKA_PLUGIN_URI);
        this.installPlugin(pluginPublication);
        this.tracer.setTitle("publisher-alaska") ;
        this.tracer.setRelativePosition(2, 1) ;
    }

    @Override
    public void execute() throws Exception{
        String topic = "weather0";
        String msg = "120 degrees in Florida";
        for (int i =0; i <10;i ++) {
            if(i>4){
                topic="weather1";
                msg = "40 degrees in Alaska";
            }
            logMessage("Publishing message "+i);
            publish(new Message(msg), topic);
        }
    }



    private void publish(Message message, String topic) throws Exception {
        ((PublisherPlugin)this.getPlugin(ALASKA_PLUGIN_URI)).publish(message,topic);
    }

}

