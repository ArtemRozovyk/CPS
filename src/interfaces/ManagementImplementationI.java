package interfaces;

public interface ManagementImplementationI {
	
	void createTopic(String topic) throws Exception;
	void createTopics(String[] topic);
	void destroyTopic(String topic);
	boolean isTopic(String topic);
	String[] getTopics();

}
