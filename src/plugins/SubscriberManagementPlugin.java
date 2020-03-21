package plugins;

import connectors.ManagementConnector;
import cvm.CVM;
import fr.sorbonne_u.components.AbstractPlugin;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import fr.sorbonne_u.components.ports.PortI;
import fr.sorbonne_u.components.reflection.connectors.ReflectionConnector;
import fr.sorbonne_u.components.reflection.interfaces.ReflectionI;
import fr.sorbonne_u.components.reflection.ports.ReflectionOutboundPort;
import interfaces.ManagementCI;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import ports.SubscriberManagementOutbondPort;
import ports.SubscriberReceptionInboundPort;
import ports.SubscriberReceptionInboundPortForPlugin;

public class SubscriberManagementPlugin
extends AbstractPlugin
{

	private static final long serialVersionUID = 1L;
	
	/** Inbound port to connect to the plugin **/
	protected SubscriberManagementOutbondPort smop;



	/**
	 * Used in components to install the plugin
	 */
	@Override
	public void installOn(ComponentI owner) throws Exception
	{
		super.installOn(owner);
		
		// We add the required interface and publish the inbound port
		this.addRequiredInterface(ManagementCI.class);
		this.smop = new SubscriberManagementOutbondPort(
											this.getPluginURI(), this.owner);
		this.smop.publishPort();
	}

	@Override
	public void initialise() throws Exception {
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
				this.smop.getPortURI(),
				urisManage[0],
				ManagementConnector.class.getCanonicalName()) ;

		super.initialise();
	}

	/**
	 * Disconnect the inbound port
	 */
	@Override
	public void finalise() throws Exception
	{
		this.owner.doPortDisconnection(this.smop.getPortURI());
	}
	
	/**
	 * Unpublish the inbound port, destroy the port and remove
	 * the required interface
	 */
	@Override
	public void uninstall() throws Exception
	{
		this.smop.unpublishPort();
		this.smop.destroyPort();
		this.removeOfferedInterface(ReceptionCI.class);
	}
	
	private ReceptionCI getOwner()
	{
		return (ReceptionCI)this.getOwner();
	}


	public void subscribe(String topic, String subscriberReceptionInboundPortURI) throws Exception {
		smop.subscribe(topic, subscriberReceptionInboundPortURI);
	}


    public void unsubscribe(String topic, String subscriberReceptionInboundPortURI) throws Exception {
	    smop.unsubscribe(topic,subscriberReceptionInboundPortURI);
    }
}
