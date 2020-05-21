package util.replication.selectors;

import util.replication.interfaces.*;
import fr.sorbonne_u.components.ports.*;

// -----------------------------------------------------------------------------

/**
 * The class <code>IdentitySelector</code> implements a selector that
 * returns all of the proposed ports.
 *
 * <p><strong>Description</strong></p>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-02-28</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			WholeSelector
implements	SelectorI
{
	/**
	 * @see SelectorI#select(OutboundPortI[])
	 */
	@Override
	public synchronized OutboundPortI[]	select(OutboundPortI[] ports)
	{
		assert	ports != null && ports.length > 0 ;
		return ports ;
	}
}
// -----------------------------------------------------------------------------
