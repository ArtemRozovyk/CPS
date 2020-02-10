package connectors;

import fr.sorbonne_u.components.connectors.AbstractConnector;
import interfaces.ManagementCI;
import interfaces.MessageFilterI;

public class ManagementConnector 
extends AbstractConnector 
implements ManagementCI  {

	@Override
	public void createTopic(String topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createTopics(String[] topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroyTopic(String topic) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isTopic(String topic) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String[] getTopics() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void subscribe(String topic, String inboundPortURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(String[] topics, String inboutPortURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void subscribe(String topic, MessageFilterI filter, String inboutPortURI) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsubscribe(String topic, String inboundPortURI) {
		// TODO Auto-generated method stub
		
	}
	
}
