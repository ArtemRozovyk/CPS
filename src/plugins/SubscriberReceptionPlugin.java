package plugins;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import interfaces.ManagementCI;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import ports.SubscriberReceptionInboundPortForPlugin;

/**
 * The plugin SubscriberReceptionPlugin is used to implement the
 * reception services for a subscriber
 * 
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 */
public class SubscriberReceptionPlugin
extends AbstractPlugin
implements ReceptionCI
{

	private static final long serialVersionUID = 1L;
	
	/** Inbound port to connect to the plugin **/
	protected SubscriberReceptionInboundPortForPlugin sripfp;
    protected String sripURI ;

    /**
     * Plugin creation
     * 
     * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	subscriberReceptionInboundPortURI != null	
	 * post	true				// no more postconditions.
	 * </pre>
	 * 
     * @param subscriberReceptionInboundPortURI			le port entrant du subscriber
     */
    public SubscriberReceptionPlugin(String subscriberReceptionInboundPortURI) {
        this.sripURI=subscriberReceptionInboundPortURI;
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
	 * 
	 */
    @Override
    public void initialise() throws Exception {
        this.sripfp = new SubscriberReceptionInboundPortForPlugin(
                sripURI,this.getPluginURI(), this.owner);
        this.sripfp.publishPort();
    }

    /**
	 * Used in components to install the plugin
	 */
	@Override
	public void installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		// We add the required interface and publish the inbound port
		this.addOfferedInterface(ReceptionCI.class);

	}
	
	/**
	 * Returns the owner of the plugin
	 */
	private ReceptionCI getOwner()
	{
		return (ReceptionCI)this.owner;
	}

	/**
	 * @see interfaces.ReceptionCI#acceptMessage(MessageI)
	 */
	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.getOwner().acceptMessage(m);
	}

	/**
	 * @see interfaces.ReceptionCI#acceptMessage(MessageI[])
	 */
	@Override
	public void acceptMessage(MessageI[] ms) throws Exception {
		this.getOwner().acceptMessage(ms);
	}

	/**
	 * Unpublish the outbound port, destroy the port and remove
	 * the required interface
	 */
    @Override
    public void uninstall() throws Exception
    {
        this.sripfp.unpublishPort();
        this.sripfp.destroyPort();
        this.removeOfferedInterface(ReceptionCI.class);
    }
}
