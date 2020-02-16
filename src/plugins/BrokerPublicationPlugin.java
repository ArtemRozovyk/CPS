package plugins;

import connectors.PublicationConnector;
import cvm.CVM;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import interfaces.MessageI;
import interfaces.PublicationCI;
import interfaces.PublicationsImplementationI;
import ports.BrokerPublicationInboundPortForPlugin;
import ports.PublisherPublicationOutboundPort;

public class BrokerPublicationPlugin 
extends AbstractPlugin
implements PublicationsImplementationI
{

	private static final long serialVersionUID = 1L;
	
	/** Inbound port to connect to the plugin **/
	protected BrokerPublicationInboundPortForPlugin bpipfp;
	
	
	/**
	 * Used in components to install the plugin
	 */
	@Override
	public void installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		
		// We add the required interface and publish the outbound port
		this.addOfferedInterface(PublicationCI.class);
		this.bpipfp = new BrokerPublicationInboundPortForPlugin(
											this.getPluginURI(), this.owner);
		this.bpipfp.publishPort();
	}
	
	
	/**
	 * Disconnect the inbound port
	 */
	@Override
	public void finalise() throws Exception
	{
		this.owner.doPortDisconnection(this.bpipfp.getPortURI());
	}
	
	/**
	 * Unpublish the inbound port, destroy the port and remove
	 * the required interface
	 */
	@Override
	public void uninstall() throws Exception
	{
		this.bpipfp.unpublishPort();
		this.bpipfp.destroyPort();
		this.removeOfferedInterface(PublicationCI.class);
	}
	
	private PublicationsImplementationI getOwner()
	{
		return (PublicationsImplementationI)this.getOwner();
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
