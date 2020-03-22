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
        this.msg1 = new Message("message test integer", new TimeStamp(System.currentTimeMillis(), "mg"), "mg-test-0", new Properties());

        Thread.sleep(1);

        this.msg2 = new Message("message test message properties", new TimeStamp(System.currentTimeMillis(), "marlou"), "mg-test-1", new Properties());

    }

    @Test
    void testFilter() {
        msg1.getProperties().putProp("Quantity", 10);
        msg1.getProperties().putProp("Direction", 'n');
        msg1.getProperties().putProp("Label", "Bingo");


        msg2.getProperties().putProp("Curved", true);
        msg2.getProperties().putProp("PostCode", (byte) 127);
        msg2.getProperties().putProp("Cost", 1.3);
        msg2.getProperties().putProp("Distribution", 1.3f);
        msg2.getProperties().putProp("Capital", 420L);
        msg2.getProperties().putProp("Building", (short) 32767);


        MessageFilterI mf1 = m -> {
            Character c = m.getProperties().getCharProp("Direction");
            Integer i = m.getProperties().getIntProp("Quantity");
            String s = m.getProperties().getStringProp("Label");
            return (s != null && i != null && c != null) &&
                    c == 'n' && i < 15 && s.equals("Bingo");
        };

        MessageFilterI mf2 = m -> {
            Long l = m.getProperties().getLongProp("Capital");
            return l != null && l > 400L;
        };


        MessageFilterI mf3 = m -> {
            Boolean bl = m.getProperties().getBooleanProp("Curved");
            Byte b = m.getProperties().getByteProp("PostCode");
            Double d = m.getProperties().getDoubleProp("Cost");
            Float f = m.getProperties().getFloatProp("Distribution");
            Short s = m.getProperties().getShortProp("Building");
            return bl != null && b != null && d != null && f != null && s != null
                    && bl && (b == 127) && (d < 1.4) && (f < 1.5f) & (s == 32767);
        };


        assertTrue(mf1.filter(msg1));
        assertFalse(mf1.filter(msg2));

        assertTrue(mf2.filter(msg2));
        assertFalse(mf2.filter(msg1));

        assertTrue(mf3.filter(msg2));
        assertFalse(mf3.filter(msg1));


    }

    @Test
    void testMessageURI() {
        assertNotEquals(msg1.getURI(), msg2.getURI());
    }

    @Test
    void testTimestamp() {
        assertEquals(msg1.getTimeStamp().getTimestamper(), "mg");
        assertEquals(msg2.getTimeStamp().getTimestamper(), "marlou");
        assertTrue(msg1.getTimeStamp().getTime() < msg2.getTimeStamp().getTime());
    }

    @Test
    void testContent() {
        assertEquals(msg1.getPayload().toString(), "message test integer");
        assertEquals(msg2.getPayload().toString(), "message test message properties");
    }

    @Test
    void testProperties0() {
        msg1.getProperties().putProp("prop1", 42L); //long
        assertNull(msg1.getProperties().getIntProp("prop1"));
    }

    @Test
    void testProperties1() {

        msg1.getProperties().putProp("prop1", true); //boolean
        msg1.getProperties().putProp("prop1", (byte) 42); //byte
        msg1.getProperties().putProp("prop1", '4'); //char
        msg1.getProperties().putProp("prop1", 42.0); //dobule
        msg1.getProperties().putProp("prop1", 42.0f); //float
        msg1.getProperties().putProp("prop1", 42); //int
        msg1.getProperties().putProp("prop1", 42L); //long
        msg1.getProperties().putProp("prop1", (short) 42); //short
        msg1.getProperties().putProp("prop1", "42"); //String

        assertNotNull(msg1.getProperties().getBooleanProp("prop1"));
        assertNotNull(msg1.getProperties().getByteProp("prop1"));
        assertNotNull(msg1.getProperties().getCharProp("prop1"));
        assertNotNull(msg1.getProperties().getDoubleProp("prop1"));
        assertNotNull(msg1.getProperties().getFloatProp("prop1"));
        assertNotNull(msg1.getProperties().getIntProp("prop1"));
        assertNotNull(msg1.getProperties().getLongProp("prop1"));
        assertNotNull(msg1.getProperties().getShortProp("prop1"));
        assertNotNull(msg1.getProperties().getStringProp("prop1"));
    }

    @Test
    void testProperties2() {
        msg1.getProperties().putProp("prop1", 42);
        msg1.getProperties().putProp("prop2", 42);
        assertEquals(msg1.getProperties().getIntProp("prop1"),
                msg1.getProperties().getIntProp("prop2"));
    }


}
