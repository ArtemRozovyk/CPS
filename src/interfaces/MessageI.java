package interfaces;

import java.io.Serializable;

import message.Properties;
import message.TimeStamp;

/**
 * The interface ManagementImplementationI defines the interface
 * required by a Java Object class that is meant to be published 
 * and received, like a message
 */
public interface MessageI extends Serializable{
	
	/**
	 * Returns the content of the message
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	this.msg != null
	 * post	ret != null
	 * </pre>
	 */
    String toString();
    
    /**
	 * Returns the URI of the message
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	this.URI != null
	 * post	ret != null
	 * </pre>
	 */
    String getURI();
    
    /**
	 * Returns the timestamp of the message
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	this.ts != null
	 * post	ret != null
	 * </pre>
	 */
    TimeStamp getTimeStamp();
    
    /**
	 * Returns the properties of the message
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	this.prop != null
	 * post	ret != null
	 * </pre>
	 */
    Properties getProperties();
    
    /**
	 * Returns the payload of the message
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	this.content != null
	 * post	ret != null
	 * </pre>
	 */
    Serializable getPayload();
}
