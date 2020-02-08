package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.ManagementCI;

public class BrokerManagementInboundPort extends AbstractInboundPort implements ManagementCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BrokerManagementInboundPort(Class<?> implementedInterface, ComponentI owner) throws Exception {
		super(implementedInterface, owner);
		// TODO Auto-generated constructor stub
	}

}
