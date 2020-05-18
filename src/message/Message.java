package message;

import interfaces.MessageI;

import java.io.Serializable;

/**
 * That class represents the messages to be published and received
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant		true
 * </pre>
 */
public class Message implements MessageI, Serializable {

    private static final long serialVersionUID = -2687757832261453813L;

    private Serializable content;

    private TimeStamp ts;

    private String URI;

    private Properties prop;


    /**
     * Creation of a message
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	content != null
     * pre	ts != null
     * pre	URI != null
     * pre  prop != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @param content the payload of the message
     * @param ts      the timestamp of the message
     * @param URI     the uri of the message
     * @param prop    the properties of the message
     */
    public Message(Serializable content, TimeStamp ts, String URI, Properties prop) {
        this.content = content;
        this.ts = ts;
        this.URI = URI;
        this.prop = prop;
    }

    /**
     * Creation of a message
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	msg != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @param msg the content of the message
     */
    public Message(String msg) {
        this.URI = msg;
    }

    /**
     * @see interfaces.MessageI#toString()
     */
    @Override
    public String toString() {
        return "Message: " + URI + "  ";
    }


    /**
     * @see interfaces.MessageI#getURI()
     */
    @Override
    public String getURI() {
        return this.URI;
    }


    /**
     * @see interfaces.MessageI#getTimeStamp()
     */
    @Override
    public TimeStamp getTimeStamp() {
        return this.ts;
    }


    /**
     * @see interfaces.MessageI#getProperties()
     */
    @Override
    public Properties getProperties() {
        return this.prop;
    }


    /**
     * @see interfaces.MessageI#getPayload()
     */
    @Override
    public Serializable getPayload() {
        return this.content;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
