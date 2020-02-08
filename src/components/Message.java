package components;
import interfaces.MessageI;

public class Message implements MessageI {
	public Message(String msg) {
		super();
		this.msg = msg;
	}

	String msg;
}
