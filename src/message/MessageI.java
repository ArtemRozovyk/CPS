package message;

import java.io.Serializable;

public interface MessageI extends Serializable {
   boolean getInitialised();
   long getTime();
   String getTimestamper();
   void setTimestamper(String timestamper);

}
