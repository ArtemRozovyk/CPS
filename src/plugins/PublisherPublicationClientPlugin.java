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
import ports.PublisherPublicationOutboundPort;

public class PublisherPublicationClientPlugin 
extends AbstractPlugin
{

	private static final long serialVersionUID = 1L;
	
	/** Outbound port required to connect to the Broker component **/
	protected PublisherPublicationOutboundPort ppop;
	
	
	/**
	 * Used in components to install the plugin
	 */
	@Override
	public void installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		
		// We add the required interface and publish the outbound port
		this.addRequiredInterface(PublicationCI.class);
		this.ppop = new PublisherPublicationOutboundPort(this.owner);
		this.ppop.publishPort();
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
	public void initialise() throws Exception
	{
		// We use the reflection approach to get the URI of the inbound port
		// of the hash map component.
		this.addRequiredInterface(ReflectionI.class);
		ReflectionOutboundPort rop = new ReflectionOutboundPort(this.owner);
		rop.publishPort();
		
		this.owner.doPortConnection(
				rop.getPortURI(), 
				CVM.BROKER_PUBLICATION_INBOUND_PORT,
				ReflectionConnector.class.getCanonicalName());
		
		String[] uris = rop.findPortURIsFromInterface(PublicationCI.class) ;
		assert	uris != null && uris.length == 1 ;
		
		this.owner.doPortDisconnection(rop.getPortURI()) ;
		rop.unpublishPort() ;
		rop.destroyPort() ;
		this.removeRequiredInterface(ReflectionI.class) ;
		
		// connect the outbound port.
		this.owner.doPortConnection(
				this.ppop.getPortURI(),
				uris[0],
				PublicationConnector.class.getCanonicalName()) ;

		super.initialise();
	}
	
	/**
	 * Disconnect the ountbound port
	 */
	@Override
	public void finalise() throws Exception
	{
		this.owner.doPortDisconnection(this.ppop.getPortURI());
	}
	
	/**
	 * Unpublish the outbound port, destroy the port and remove
	 * the required interface
	 */
	@Override
	public void uninstall() throws Exception
	{
		this.ppop.unpublishPort();
		this.ppop.destroyPort();
		this.removeRequiredInterface(PublicationCI.class);
	}
	
	public void publish(MessageI m, String topic) throws Exception {
		this.ppop.publish(m, topic);
	}

	public void publish(MessageI m, String[] topics) throws Exception {
		this.ppop.publish(m, topics);
	}

	public void publish(MessageI[] ms, String topic) throws Exception {
		this.ppop.publish(ms, topic);
	}

	public void publish(MessageI[] ms, String[] topics) throws Exception {
		this.ppop.publish(ms, topics);
	}
	
}
