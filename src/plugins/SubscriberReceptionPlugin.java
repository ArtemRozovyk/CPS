package plugins;

import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import ports.SubscriberReceptionInboundPortForPlugin;

public class SubscriberReceptionPlugin
extends AbstractPlugin
implements ReceptionCI
{

	private static final long serialVersionUID = 1L;
	
	/** Inbound port to connect to the plugin **/
	protected String sripURI ;
	protected String pluginURI ;
	protected SubscriberReceptionInboundPortForPlugin sripfp;

	public SubscriberReceptionPlugin(String subscriberReceptionInboundPortURI,
									 String subAlaskaReceptPluginUri) {

		this.sripURI=subscriberReceptionInboundPortURI;
		this.pluginURI=subAlaskaReceptPluginUri;
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
		this.sripfp = new SubscriberReceptionInboundPortForPlugin(
				sripURI,pluginURI, this.owner);
		this.sripfp.publishPort();
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





}
