package plugins;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import interfaces.ManagementCI;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import ports.SubscriberReceptionInboundPortForPlugin;

public class SubscriberReceptionPlugin
extends AbstractPlugin
implements ReceptionCI
{

	private static final long serialVersionUID = 1L;
	
	/** Inbound port to connect to the plugin **/
	protected SubscriberReceptionInboundPortForPlugin sripfp;
    protected String sripURI ;

    public SubscriberReceptionPlugin(String subscriberReceptionInboundPortURI) {
        this.sripURI=subscriberReceptionInboundPortURI;
    }

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
	
	
	private ReceptionCI getOwner()
	{
		return (ReceptionCI)this.owner;
	}

	@Override
	public void acceptMessage(MessageI m) throws Exception {
		this.getOwner().acceptMessage(m);
	}


	@Override
	public void acceptMessage(MessageI[] ms) throws Exception {
		this.getOwner().acceptMessage(ms);
	}

    @Override
    public void uninstall() throws Exception
    {
        this.sripfp.unpublishPort();
        this.sripfp.destroyPort();
        this.removeOfferedInterface(ReceptionCI.class);
    }
}
