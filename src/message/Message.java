package message;
import java.io.Serializable;

import interfaces.MessageI;

public class Message implements MessageI {
	public Message(String msg) {
		super();
		this.msg = msg;
	}

	public String msg;

	@Override
	public String toString() {
		return "Message{" +
				"msg='" + msg + '\'' +
				'}';
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
}
