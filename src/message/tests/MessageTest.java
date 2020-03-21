package message.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import interfaces.MessageI;
import message.Message;
import message.MessageFilterI;
import message.Properties;
import message.TimeStamp;
class MessageTest {
	
	private MessageI msg1;
	private MessageI msg2;
	
	@BeforeEach
	public void init() throws InterruptedException {
		this.msg1 = new Message("message test integer", new TimeStamp(System.currentTimeMillis(),"mg"), "mg-test-0", new Properties());
		this.msg1.getProperties().putProp("USA", new Integer(10));
		
		Thread.sleep(1);
		
		this.msg2 = new Message("message test message properties", new TimeStamp(System.currentTimeMillis(),"marlou"), "mg-test-1", new Properties());
		this.msg2.getProperties().putProp("USA", new Integer(20));
	}

	@Test
	void testInteger() {
		MessageFilterI mf = (MessageI m) -> {
			if(m.getProperties().getIntProp("USA") < 15) 
				return true;
			
			return false;
		};
		boolean canSendMsg1 = mf.filter(msg1);
		boolean canSendMsg2 = mf.filter(msg2);
		
		assertEquals(true, canSendMsg1);
		assertEquals(false, canSendMsg2);
	}
	
	@Test
	void testMessageURI() {
		assertEquals(true, !msg1.getURI().equals(msg2.getURI()));
	}
	
	@Test
	void testTimestamp() {
		assertEquals(msg1.getTimeStamp().getTimestamper(), "mg");
		assertEquals(msg2.getTimeStamp().getTimestamper(), "marlou");
		assertEquals(true, msg1.getTimeStamp().getTime() < msg2.getTimeStamp().getTime());
	}
	
	@Test
	void testContent() {
		assertEquals(msg1.getPayload().toString(), "message test integer");
		assertEquals(msg2.getPayload().toString(), "message test message properties");
	}

}
