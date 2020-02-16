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
	String msg;
	public Message(String msg) {
		//super();
		this.msg=msg;
	}

	@Override
	public String toString() {
		return "Message: "+msg+"  ";
	}

	@Override
	public String getURI() {
		return this.URI;
	}

	@Override
	public TimeStamp getTimeStamp() {
		return this.ts;
	}

	@Override
	public Properties getProperties() {
		return this.prop;
	}

	@Override
	public Serializable getPayload() {
		return this.content;
	}

}
