package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.ManagementCI;

public class PublisherManagementOutboundPort 
extends AbstractOutboundPort 
implements ManagementCI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PublisherManagementOutboundPort(Class<?> 
	implementedInterface, 
	ComponentI owner) throws Exception {
		super(implementedInterface, owner);
	}
 
}
