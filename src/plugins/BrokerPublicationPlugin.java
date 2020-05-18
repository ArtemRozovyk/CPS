package plugins;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import ports.BrokerPublicationInboundPortForPlugin;

public class BrokerPublicationPlugin
        extends AbstractPlugin
        implements PublicationCI {

    private static final long serialVersionUID = 1L;

    /**
     * Inbound port to connect to the plugin
     **/
    protected BrokerPublicationInboundPortForPlugin bpipfp;


    /**
     * Used in components to install the plugin
     */
    @Override
    public void installOn(ComponentI owner) throws Exception {
        super.installOn(owner);

        // We add the required interface and publish the inbound port
        this.addOfferedInterface(PublicationCI.class);
        this.bpipfp = new BrokerPublicationInboundPortForPlugin(
                this.getPluginURI(), this.owner);
        this.bpipfp.publishPort();
    }


    /**
     * Disconnect the inbound port
     */
    @Override
    public void finalise() throws Exception {
        this.owner.doPortDisconnection(this.bpipfp.getPortURI());
    }

    /**
     * Unpublish the inbound port, destroy the port and remove
     * the required interface
     */
    @Override
    public void uninstall() throws Exception {
        this.bpipfp.unpublishPort();
        this.bpipfp.destroyPort();
        this.removeOfferedInterface(PublicationCI.class);
    }

    private PublicationCI getOwner() {
        return (PublicationCI) this.getOwner();
    }


    @Override
    public void publish(MessageI m, String topic) throws Exception {
        this.getOwner().publish(m, topic);
    }


    @Override
    public void publish(MessageI m, String[] topics) throws Exception {
        this.getOwner().publish(m, topics);
    }


    @Override
    public void publish(MessageI[] ms, String topic) throws Exception {
        this.getOwner().publish(ms, topic);
    }


    @Override
    public void publish(MessageI[] ms, String[] topics) throws Exception {
        this.getOwner().publish(ms, topics);
    }
}
