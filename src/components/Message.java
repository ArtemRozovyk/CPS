package components;
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
}
