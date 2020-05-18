package message;

public class TimeStamp {

    protected long time = 0;

    protected String timestamper;

    public TimeStamp(long t, String author) {
        this.time = t;
        this.timestamper = author;
    }

    public boolean isInitialised() {
        return time != 0 && timestamper != null;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long t) {
        this.time = t;
    }

    public String getTimestamper() {
        return timestamper;
    }

    public void setTimestamper(String timestamper) {
        this.timestamper = timestamper;
    }

}
