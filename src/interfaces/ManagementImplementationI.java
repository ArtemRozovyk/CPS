package interfaces;

public interface ManagementImplementationI {
	
	void createTopic(String topic);
	void createTopics(String[] topic);
	void destroyTopic(String topic);
	boolean isTopic(String topic);
	String[] getTopics();

}
