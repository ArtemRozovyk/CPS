package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.MessageI;
import interfaces.ReceptionCI;

/**
 * The class ReceptionConnector defines the connector for
 * the ReceptionCI interface
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant	true
 * </pre>
 */
public class ReceptionConnector
        extends AbstractConnector
        implements ReceptionCI {

    /**
     * @see interfaces.ReceptionCI#acceptMessage(MessageI)
     */
    @Override
    public void acceptMessage(MessageI m) throws Exception {
        ((ReceptionCI) this.offering).acceptMessage(m);

    }


    /**
     * @see interfaces.ReceptionCI#acceptMessage(MessageI[])
     */
    @Override
    public void acceptMessage(MessageI[] ms) {
        // TODO Auto-generated method stub
        System.out.println("lol");
    }

}
