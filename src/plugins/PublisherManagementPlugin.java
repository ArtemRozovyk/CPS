package plugins;

import connectors.ManagementConnector;
import connectors.PublicationConnector;
import cvm.CVM;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import interfaces.ManagementCI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import ports.PublisherManagementOutboundPort;
import ports.PublisherPublicationOutboundPort;

public class PublisherManagementPlugin
extends AbstractPlugin
{

	private static final long serialVersionUID = 1L;
	
	/** Outbound port required to connect to the Broker component **/
	protected PublisherManagementOutboundPort pmop;

	
	/**
	 * Used in components to install the plugin
	 */
	@Override
	public void installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		
		// We add the required interface and publish the outbound port
		//management
		this.addRequiredInterface(ManagementCI.class);
		this.pmop = new PublisherManagementOutboundPort(this.owner);
		this.pmop.publishPort();
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
				CVM.BROKER_COMPONENT_URI,
				ReflectionConnector.class.getCanonicalName());
		


		String[] urisManage = rop.findPortURIsFromInterface(ManagementCI.class) ;
		assert	urisManage != null && urisManage.length == 1 ;
		
		this.owner.doPortDisconnection(rop.getPortURI()) ;
		rop.unpublishPort() ;
		rop.destroyPort() ;
		this.removeRequiredInterface(ReflectionI.class) ;
		
		// connect the outbound port.

		this.owner.doPortConnection(
				this.pmop.getPortURI(),
				urisManage[0],
				ManagementConnector.class.getCanonicalName()) ;

		super.initialise();
	}
	
	/**
	 * Disconnect the ountbound port
	 */
	@Override
	public void finalise() throws Exception
	{
		this.owner.doPortDisconnection(this.pmop.getPortURI());
	}
	
	/**
	 * Unpublish the outbound port, destroy the port and remove
	 * the required interface
	 */
	@Override
	public void uninstall() throws Exception
	{
		this.pmop.unpublishPort();
		this.pmop.destroyPort();
		this.removeRequiredInterface(ManagementCI.class);
	}
	
	public void createTopic(String topic) throws Exception {
		this.pmop.createTopic(topic);
	}
	//...TODO Management

	
}
