package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import interfaces.ManagementCI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import interfaces.ReceptionCI;
import message.MessageFilterI;
import plugins.PublisherManagementPlugin;
import plugins.SubscriberManagementPlugin;
import plugins.SubscriberReceptionPlugin;
import ports.SubscriberManagementOutbondPort;

public class SubscriberAlaska extends AbstractComponent implements ReceptionCI {
    protected final static String SUB_ALASKA_RECEPT_PLUGIN_URI = "sub-alaska-recept-uri";
    protected final static String SUB_ALASKA_MANAGE_PLUGIN_URI = "sub-alaska-manage-uri";

    static int i = 0;
    static final Object iGuard = new Object();
    private String subscriberReceptionInboundPortURI = "subscriber-alaska-reception-inbound-port-uri-3";


    protected SubscriberAlaska(String reflectionInboundPortURI) throws Exception {
        super(reflectionInboundPortURI, 0, 1);
        this.tracer.setTitle("sub-alaska");
        this.tracer.setRelativePosition(0, 2);


    }


    @Override
    public void execute() throws Exception {
        super.execute();
        SubscriberManagementPlugin smp = new SubscriberManagementPlugin();
        smp.setPluginURI(SUB_ALASKA_MANAGE_PLUGIN_URI);
        this.installPlugin(smp);

        //unstailling the plgin that will create port and publish it
        SubscriberReceptionPlugin subscriberPlugin
                = new SubscriberReceptionPlugin(subscriberReceptionInboundPortURI);
        subscriberPlugin.setPluginURI(SUB_ALASKA_RECEPT_PLUGIN_URI);
        this.installPlugin(subscriberPlugin);


        /*
        SubscriberReceptionPlugin subscriberPlugin = new SubscriberReceptionPlugin();
        subscriberPlugin.setPluginURI(SUB_ALASKA_RECEPT_PLUGIN_URI);
        this.installPlugin(subscriberPlugin);
*/
        //test scenario
        MessageFilterI filter = m -> {
            Long lng = m.getProperties().getLongProp("Long");
            Integer in = m.getProperties().getIntProp("Integer");
            Character cr = m.getProperties().getCharProp("Character");
            Byte by = m.getProperties().getByteProp("Byte");
            Boolean bo = m.getProperties().getBooleanProp("Boolean");
            String str = m.getProperties().getStringProp("String");
            Short shrt = m.getProperties().getShortProp("Short");
            Double dbl = m.getProperties().getDoubleProp("Double");
            Float flt = m.getProperties().getFloatProp("Float");
            return (lng != null && in != null && cr != null &&
                    by != null && bo != null && str != null &&
                    shrt != null && dbl != null && flt != null &&
                    lng == 3L && in == 3 && cr == '3' && by == 3
                    && bo && str.equals("3") && shrt == 3
                    && dbl == 3.0 && flt == 3.0f)
                    && m.getPayload()!=null;
        };



        String [] topics ={"USA","Alaska","Anchorage","UK"};
        subscribe(topics); // 35 msg
        subscribe("Cambridge",filter); // 35 msg

        MessageFilterI filterModified = m -> {
            Long lng = m.getProperties().getLongProp("Long");
            Integer in = m.getProperties().getIntProp("Integer");
            Character cr = m.getProperties().getCharProp("Character");
            Byte by = m.getProperties().getByteProp("Byte");
            Boolean bo = m.getProperties().getBooleanProp("Boolean");
            String str = m.getProperties().getStringProp("String");
            Short shrt = m.getProperties().getShortProp("Short");
            Double dbl = m.getProperties().getDoubleProp("Double");
            Float flt = m.getProperties().getFloatProp("Float");
            return (lng != null && in != null && cr != null &&
                    by != null && bo != null && str != null &&
                    shrt != null && dbl != null && flt != null &&
                    lng == 5L && in == 5 && cr == '5' && by == 3
                    && bo && str.equals("3") && shrt == 3
                    && dbl == 3.0 && flt == 3.0f)
                    && m.getPayload()!=null;
        };
        Thread.sleep(5000);
        modifyFilter("Cambridge",filterModified);




    }


    public void acceptMessage(MessageI m) throws Exception {
        logMessage("Getting message " + m);
        if(m.getPayload()!=null){
            logMessage(" Pl : "+ m.getPayload());
        }
        if(m.getTimeStamp()!=null){
            logMessage(" Ts : "+ m.getTimeStamp().getTime()+":"+m.getTimeStamp().getTimestamper());
        }

    }

    @Override
    public void acceptMessage(MessageI[] ms) throws Exception {
        for (MessageI m : ms) {
            acceptMessage(m);
        }
    }

    public void subscribe(String topic) throws Exception {


        logMessage("Subscribing to " + topic);

        //sending the port over the Broker
        ((SubscriberManagementPlugin) this.getPlugin(
                SUB_ALASKA_MANAGE_PLUGIN_URI)).subscribe(topic, subscriberReceptionInboundPortURI);


        assert this.subscriberReceptionInboundPortURI.equals("subscriber-alaska-reception-inbound-port-uri-3") :
                new PostconditionException("The URI prefix has not "
                        + "been initialised!");
        assert this.isPortExisting(subscriberReceptionInboundPortURI) :
                new PostconditionException("The component must have a "
                        + "port with URI " + subscriberReceptionInboundPortURI);
        assert this.findPortFromURI(subscriberReceptionInboundPortURI).isPublished() :
                new PostconditionException("The component must have a "
                        + "port published with URI " + subscriberReceptionInboundPortURI);

    }

    public void subscribe(String[] topics) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_ALASKA_MANAGE_PLUGIN_URI)).subscribe(topics, subscriberReceptionInboundPortURI);
    }

    public void subscribe(String topic, MessageFilterI filter) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_ALASKA_MANAGE_PLUGIN_URI)).subscribe(topic, filter, subscriberReceptionInboundPortURI);
    }

    public void modifyFilter(String topic, MessageFilterI newFilter) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_ALASKA_MANAGE_PLUGIN_URI)).modifyFilter(topic, newFilter, subscriberReceptionInboundPortURI);
    }

    public void unsubscribe(String topic) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_ALASKA_MANAGE_PLUGIN_URI)).unsubscribe(topic, subscriberReceptionInboundPortURI);
    }

    public void createTopic(String topic) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_ALASKA_MANAGE_PLUGIN_URI)).createTopic(topic);
    }

    public void createTopic(String[] topics) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_ALASKA_MANAGE_PLUGIN_URI)).createTopic(topics);
    }

    public void destroy(String topic) throws Exception {
        ((SubscriberManagementPlugin) this.getPlugin(SUB_ALASKA_MANAGE_PLUGIN_URI)).destroyTopic(topic);
    }

    public boolean isTopic(String topic) throws Exception {
        return ((SubscriberManagementPlugin) this.getPlugin(SUB_ALASKA_MANAGE_PLUGIN_URI)).isTopic(topic);
    }

    public String[] getTopics() throws Exception {
        return ((SubscriberManagementPlugin) this.getPlugin(SUB_ALASKA_MANAGE_PLUGIN_URI)).getTopics();
    }
    
    public String getPublicatinPortURI() throws Exception {
        return ((PublisherManagementPlugin) this.getPlugin(SUB_ALASKA_MANAGE_PLUGIN_URI)).getPublicatinPortURI();
    }

}
