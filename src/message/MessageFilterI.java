package message;

import interfaces.MessageI;

/**
 * The interface MessageFilterI defines the interface 
 * required to filter messages
 */
public interface MessageFilterI {

   boolean  filter (MessageI m);
}
