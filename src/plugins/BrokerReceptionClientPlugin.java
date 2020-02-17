package plugins;

import connectors.ReceptionConnector;
import cvm.CVM;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import interfaces.PublicationCI;
import interfaces.ReceptionCI;
import ports.BrokerReceptionOutboundPort;

public class BrokerReceptionClientPlugin 
extends AbstractPlugin
{

	private static final long serialVersionUID = 1L;
	
	/** Outbound port required to connect to the Subscriber component **/
	protected BrokerReceptionOutboundPort brop;
	
	
	/**
	 * Used in components to install the plugin
	 */
	@Override
	public void installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		
		// We add the required interface and publish the outbound port
		this.addRequiredInterface(PublicationCI.class);
		this.brop = new BrokerReceptionOutboundPort(this.owner);
		this.brop.publishPort();
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
		this.addRequiredInterface(ReflectionI.class);
		ReflectionOutboundPort rop = new ReflectionOutboundPort(this.owner);
		rop.publishPort();
		/*
		this.owner.doPortConnection(
				rop.getPortURI(), 
				CVM.SUBSCRIBER_RECEPTION_INBOUND_PORT,
				ReflectionConnector.class.getCanonicalName());
	    */
		String[] uris = rop.findPortURIsFromInterface(ReceptionCI.class) ;
		assert	uris != null && uris.length == 1 ;
		
		this.owner.doPortDisconnection(rop.getPortURI()) ;
		rop.unpublishPort() ;
		rop.destroyPort() ;
		this.removeRequiredInterface(ReflectionI.class) ;
		
		// connect the outbound port.
		this.owner.doPortConnection(
				this.brop.getPortURI(),
				uris[0],
				ReceptionConnector.class.getCanonicalName()) ;

		super.initialise();
	}
	
	/**
	 * Disconnect the ountbound port
	 */
	@Override
	public void finalise() throws Exception
	{
		this.owner.doPortDisconnection(this.brop.getPortURI());
	}
	
	/**
	 * Unpublish the outbound port, destroy the port and remove
	 * the required interface
	 */
	@Override
	public void uninstall() throws Exception
	{
		this.brop.unpublishPort();
		this.brop.destroyPort();
		this.removeRequiredInterface(ReceptionCI.class);
	}
	
}
