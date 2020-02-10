package message;
import java.io.Serializable;

import interfaces.MessageI;

public class Message implements MessageI {
	
	
	private static final long serialVersionUID = -2687757832261453813L;
	
	private Serializable content;
	private TimeStamp ts;
	private String URI;

	public Message(Serializable content, TimeStamp ts, String URI, Properties prop) {
		this.content = content;
		this.ts = ts;
		this.URI = URI;
		this.prop = prop;
	}

	private Properties prop;

	public Message(String msg) {
		super();
	}
	


	
	@Override
	public String toString() {
		return "Message{"+ts.timestamper;
	}

	@Override
	public String getURI() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TimeStamp getTimeStamp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Properties getProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Serializable getPayload() {
		// TODO Auto-generated method stub
		return null;
	}



	public TimeStamp getTs() {
		return ts;
	}

	public void setTs(TimeStamp ts) {
		this.ts = ts;
	}
}
