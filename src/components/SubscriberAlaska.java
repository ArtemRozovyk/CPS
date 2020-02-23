package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import plugins.SubscriberManagementPlugin;
import plugins.SubscriberReceptionPlugin;
import ports.SubscriberManagementOutbondPort;

public class SubscriberAlaska extends AbstractComponent implements ReceptionCI {
    protected final static String	SUB_ALASKA_RECEPT_PLUGIN_URI = "sub-alaska-recept-uri" ;
    protected final static String	SUB_ALASKA_MANAGE_PLUGIN_URI = "sub-alaska-manage-uri" ;

    static int i = 0;
    static final Object iGuard = new Object();
    private String subscriberReceptionInboundPortURI= "subscriber-reception-inbound-port-uri-3";


    protected SubscriberAlaska(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);

        SubscriberManagementPlugin smp = new SubscriberManagementPlugin();
        smp.setPluginURI(SUB_ALASKA_MANAGE_PLUGIN_URI);
        this.installPlugin(smp);

        //unstailling the plgin that will create port and publish it
        SubscriberReceptionPlugin subscriberPlugin
                = new SubscriberReceptionPlugin(subscriberReceptionInboundPortURI,SUB_ALASKA_RECEPT_PLUGIN_URI);
        subscriberPlugin.setPluginURI(SUB_ALASKA_RECEPT_PLUGIN_URI);
        this.installPlugin(subscriberPlugin);

        this.tracer.setTitle("sub-alaska") ;
        this.tracer.setRelativePosition(0, 2) ;


    }



    @Override
    public void			execute() throws Exception {
        super.execute();

        /*
        SubscriberReceptionPlugin subscriberPlugin = new SubscriberReceptionPlugin();
        subscriberPlugin.setPluginURI(SUB_ALASKA_RECEPT_PLUGIN_URI);
        this.installPlugin(subscriberPlugin);
        */

        //test scenario

        subscribe("weather3");


    }


    public void acceptMessage(MessageI m) throws Exception {
        logMessage("Getting message "+m);
    }

    @Override
    public void acceptMessage(MessageI[] ms) throws Exception {

    }

    public void subscribe(String topic ) throws Exception {


        logMessage("Subscribing to weather3 ");

        //sending the port over the Broker
        ((SubscriberManagementPlugin)this.getPlugin(
                SUB_ALASKA_MANAGE_PLUGIN_URI)).subscribe(topic,subscriberReceptionInboundPortURI);





        assert	this.subscriberReceptionInboundPortURI.equals("subscriber-reception-inbound-port-uri-3") :
                new PostconditionException("The URI prefix has not "
                        + "been initialised!") ;
        assert	this.isPortExisting(subscriberReceptionInboundPortURI) :
                new PostconditionException("The component must have a "
                        + "port with URI " + subscriberReceptionInboundPortURI) ;
        assert	this.findPortFromURI(subscriberReceptionInboundPortURI).isPublished() :
                new PostconditionException("The component must have a "
                        + "port published with URI " + subscriberReceptionInboundPortURI) ;

    }



}
