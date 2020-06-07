package util.replication.selectors;

import com.sun.deploy.util.*;
import fr.sorbonne_u.components.ports.*;
import util.replication.interfaces.*;

import java.util.*;

public class ExclusiveSelector implements SelectorI {

    private int callerId;
    private Map<Integer,OutboundPortI> knownPorts;

    public void setKnownPorts(Map<OutboundPortI,Integer> p){
        knownPorts= new HashMap<>();
        for(Map.Entry<OutboundPortI,Integer> e : p.entrySet()){
            knownPorts.put(e.getValue(),e.getKey());
        }
    }

    public void setCallerId(int id){
        this.callerId=id;
    }

    @Override
    public OutboundPortI[] select(OutboundPortI[] ports) {
        return Arrays.stream(ports).
                filter(outboundPortI ->
                        !outboundPortI.equals(knownPorts.get(callerId))).
                toArray(OutboundPortI[]::new);
    }
}
