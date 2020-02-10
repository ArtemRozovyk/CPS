package interfaces;

import java.io.Serializable;

import message.Properties;
import message.TimeStamp;

public interface MessageI extends Serializable{
    String toString();
    String getURI();
    TimeStamp getTimeStamp();
    Properties getProperties();
    Serializable getPayload();
}
