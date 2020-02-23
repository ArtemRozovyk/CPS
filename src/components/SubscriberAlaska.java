package components;

import fr.sorbonne_u.components.AbstractComponent;
import interfaces.MessageI;
import plugins.SubscriberPlugin;

public class SubscriberAlaska extends AbstractComponent {
    protected final static String	SUB_ALASKA_PLUGIN_URI = "sub-alaska-uri" ;



    protected SubscriberAlaska(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);

    }



    @Override
    public void			execute() throws Exception {
        super.execute();
        SubscriberPlugin subscriberPlugin = new SubscriberPlugin();
        subscriberPlugin.setPluginURI(SUB_ALASKA_PLUGIN_URI);
        this.installPlugin(subscriberPlugin);
        this.tracer.setTitle("sub-alaska") ;
        this.tracer.setRelativePosition(0, 3) ;



        //test scenario

    }


    public void acceptMessage(MessageI m) throws Exception {
        logMessage("Getting message "+m);
    }




}
