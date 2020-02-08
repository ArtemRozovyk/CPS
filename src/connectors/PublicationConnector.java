package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.MessageI;
import interfaces.PublicationCI;

public class PublicationConnector 
extends AbstractConnector 
implements PublicationCI{

	@Override
	public void publish(MessageI m, String topic) throws Exception {
		((PublicationCI)this.offering).publish(m, topic);
	}

	@Override
	public void publish(MessageI m, String[] topics) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void publish(MessageI[] ms, String topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void publish(MessageI[] ms, String[] topics) {
		// TODO Auto-generated method stub
		
	}

}
