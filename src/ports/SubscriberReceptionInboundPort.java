package ports;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.ComponentI;
import fr.sorbonne_u.components.examples.pingpong.components.*;
import fr.sorbonne_u.components.ports.AbstractInboundPort;
import interfaces.MessageI;
import interfaces.ReceptionCI;
import plugins.*;

public class SubscriberReceptionInboundPort
        extends AbstractInboundPort
        implements ReceptionCI {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public SubscriberReceptionInboundPort(ComponentI owner) throws Exception {
        super(ReceptionCI.class, owner);
    }

    public SubscriberReceptionInboundPort(String uri, ComponentI owner) throws Exception {
        super(uri, ReceptionCI.class, owner);
    }

    @Override
    public void acceptMessage(MessageI m) throws Exception {
        this.owner.runTask(
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            ((ReceptionCI) this.getTaskProviderReference()).acceptMessage(m);
                        } catch (Exception e) {
                            e.printStackTrace() ;
                        }
                    }
                }) ;
    }

    @Override
    public void acceptMessage(MessageI[] ms) throws Exception {
        this.owner.runTask(
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            ((ReceptionCI) this.getTaskProviderReference()).acceptMessage(ms);
                        } catch (Exception e) {
                            e.printStackTrace() ;
                        }
                    }
                }) ;

    }

}
