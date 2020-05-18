package ports;

import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.ports.AbstractOutboundPort;
import interfaces.MessageI;
import interfaces.ReceptionCI;

/**
 * The class BrokerReceptionOutboundPort implements the outbound port
 * of a broker that requires the reception services from the ReceptionCI
 * interface
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant		true
 * </pre>
 */
public class BrokerReceptionOutboundPort
        extends AbstractOutboundPort
        implements ReceptionCI {

    private static final long serialVersionUID = 1L;

    /**
     * Create the port with the given owner
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	owner != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @param owner owner of the port
     * @throws Exception
     */
    public BrokerReceptionOutboundPort(ComponentI owner) throws Exception {
        super(ReceptionCI.class, owner);
    }

    /**
     * Create the port with the given URI and the given owner
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	uri != null and owner != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @param uri   uri of the port
     * @param owner owner of the port
     * @throws Exception
     */
    public BrokerReceptionOutboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ReceptionCI.class, owner);
    }


    /**
     * @see interfaces.ReceptionCI#acceptMessage(MessageI)
     */
    @Override
    public void acceptMessage(MessageI m) throws Exception {
        ((ReceptionCI) this.connector).acceptMessage(m);
    }

    /**
     * @see interfaces.ReceptionCI#acceptMessage(MessageI[])
     */
    @Override
    public void acceptMessage(MessageI[] ms) throws Exception {
        ((ReceptionCI) this.connector).acceptMessage(ms);

    }

}
