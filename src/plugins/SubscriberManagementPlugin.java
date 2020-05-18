package plugins;

import connectors.ManagementConnector;
import cvm.CVM;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import interfaces.ManagementCI;
import interfaces.MessageFilterI;
import interfaces.ReceptionCI;
import ports.SubscriberManagementOutbondPort;

/**
 * The plugin SubscriberManagementPlugin is used to implement the
 * management services for a subscriber
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant		true
 * </pre>
 */
public class SubscriberManagementPlugin
        extends AbstractPlugin {

    private static final long serialVersionUID = 1L;

    /**
     * Inbound port to connect to the plugin
     **/
    protected SubscriberManagementOutbondPort smop;


    /**
     * Used in components to install the plugin
     */
    @Override
    public void installOn(ComponentI owner) throws Exception {
        super.installOn(owner);

        // We add the required interface and publish the inbound port
        this.addRequiredInterface(ManagementCI.class);
        this.smop = new SubscriberManagementOutbondPort(
                this.getPluginURI(), this.owner);
        this.smop.publishPort();
    }

    /**
     * We assume that the plug-in on the server component has already been
     * installed and initialised.
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true				// no more preconditions.
     * post	true				// no more postconditions.
     * </pre>
     */
    @Override
    public void initialise() throws Exception {
        this.addRequiredInterface(ReflectionI.class);
        ReflectionOutboundPort rop = new ReflectionOutboundPort(this.owner);
        rop.publishPort();

        this.owner.doPortConnection(
                rop.getPortURI(),
                CVM.BROKER_COMPONENT_URI,
                ReflectionConnector.class.getCanonicalName());


        String[] urisManage = rop.findPortURIsFromInterface(ManagementCI.class);
        assert urisManage != null && urisManage.length == 1;

        this.owner.doPortDisconnection(rop.getPortURI());
        rop.unpublishPort();
        rop.destroyPort();
        this.removeRequiredInterface(ReflectionI.class);

        // connect the outbound port.

        this.owner.doPortConnection(
                this.smop.getPortURI(),
                urisManage[0],
                ManagementConnector.class.getCanonicalName());

        super.initialise();
    }

    /**
     * Disconnect the inbound port
     */
    @Override
    public void finalise() throws Exception {
        this.owner.doPortDisconnection(this.smop.getPortURI());
    }

    /**
     * Unpublish the inbound port, destroy the port and remove
     * the required interface
     */
    @Override
    public void uninstall() throws Exception {
        this.smop.unpublishPort();
        this.smop.destroyPort();
        this.removeRequiredInterface(ManagementCI.class);
    }

    /**
     * Returns the owner of the plugin
     */
    private ReceptionCI getOwner() {
        return (ReceptionCI) this.getOwner();
    }


    /**
     * @see interfaces.ManagementCI#subscribe(String, String)
     */
    public void subscribe(String topic, String subscriberReceptionInboundPortURI) throws Exception {
        smop.subscribe(topic, subscriberReceptionInboundPortURI);
    }

    /**
     * @see interfaces.ManagementCI#subscribe(String[], String)
     */
    public void subscribe(String[] topic, String subscriberReceptionInboundPortURI) throws Exception {
        for (String t : topic) {
            smop.subscribe(t, subscriberReceptionInboundPortURI);
        }
    }

    /**
     * @see interfaces.ManagementCI#subscribe(String, MessageFilterI, String)
     */
    public void subscribe(String topic, MessageFilterI filter, String subscriberReceptionInboundPortURI) throws Exception {
        smop.subscribe(topic, filter, subscriberReceptionInboundPortURI);
    }

    /**
     * @see interfaces.ManagementCI#modifyFilter(String, MessageFilterI, String)
     */
    public void modifyFilter(String topic, MessageFilterI newFilter, String inboundPortUri) throws Exception {
        smop.modifyFilter(topic, newFilter, inboundPortUri);
    }

    /**
     * @see interfaces.ManagementCI#unsubscribe(String, String)
     */
    public void unsubscribe(String topic, String subscriberReceptionInboundPortURI) throws Exception {
        smop.unsubscribe(topic, subscriberReceptionInboundPortURI);
    }

    /**
     * @see interfaces.ManagementCI#createTopic(String)
     */
    public void createTopic(String topic) throws Exception {
        smop.createTopic(topic);
    }


    /**
     * @see interfaces.ManagementCI#createTopics(String[])
     */
    public void createTopic(String[] topic) throws Exception {
        for (String t : topic) {
            smop.createTopic(t);
        }
    }

    /**
     * @see interfaces.ManagementCI#destroyTopic(String)
     */
    public void destroyTopic(String topic) throws Exception {
        smop.destroyTopic(topic);
    }

    /**
     * @see interfaces.ManagementCI#isTopic(String)
     */
    public boolean isTopic(String topic) throws Exception {
        return smop.isTopic(topic);
    }

    /**
     * @see interfaces.ManagementCI#getTopics()
     */
    public String[] getTopics() throws Exception {
        return smop.getTopics();
    }

    /**
     * @see interfaces.ManagementCI#getPublicatinPortURI()
     */
    public String getPublicatinPortURI() throws Exception {
        return smop.getPublicatinPortURI();
    }
}
