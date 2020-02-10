package message;

public class TimeStamp {

	protected long time;

	protected String timestamper;

	public boolean isInitialised() {
		Long a = new Long(this.time);
		return !a.equals(null);
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
