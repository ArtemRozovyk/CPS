package message;
import java.io.Serializable;

import interfaces.MessageI;

public class Message implements MessageI {
	
	
	private static final long serialVersionUID = -2687757832261453813L;
	
	private String msg;
	
	private TimeStamp ts;
	
	private Properties prop;

	public Message(String msg) {
		super();
		this.msg = msg;
	}
	
	public Message(String msg, TimeStamp t) {
		super();
		this.msg = msg;
		this.ts = t;
		this.prop = new Properties();
	}

	
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

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public TimeStamp getTs() {
		return ts;
	}

	public void setTs(TimeStamp ts) {
		this.ts = ts;
	}
}
