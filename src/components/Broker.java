package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.examples.cps.components.ValueConsumer;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.ports.PortI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import interfaces.ReceptionCI;
import message.Message;
import message.MessageFilterI;
import plugins.BrokerPublicationPlugin;
import plugins.BrokerReceptionClientPlugin;
import ports.BrokerManagementInboundPort;
import ports.BrokerPublicationInboundPort;
import ports.BrokerReceptionOutboundPort;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import connectors.ReceptionConnector;

public class Broker extends AbstractComponent {
	private static int i;

	//protected BrokerPublicationInboundPort bpip;
	public final static String	DYNAMIC_CONNECTION_PLUGIN_URI =
			"serverSidePLuginURI" ;
	
	public final static String	BROKER_CLIENT_RECEPTION_PLUGIN =
			"broker-recepetion-plugin-uri" ;
	
	public final static String	BROKER_PUBLICATION_PLUGIN =
			"broker-publication-plugin-uri" ;



	protected BrokerReceptionOutboundPort brop;

	private Map<String, Set<String >> topicSubsUriMap;
	private Map<String, Set<MessageI>> topicMessageStorageMap;
	private Map<String, MessageFilterI> subUriFilterMap;
	private Map<String,BrokerReceptionOutboundPort> subUriPortObjMap;

	public Broker(String reflectionInboundPortURI,
				  int nbThreads,
				  int nbSchedulableThreads,
				  Map<String, Set<String>> topicSubsUri,
				  Map<String, Set<MessageI>> topicMessageStorage,
				  Map<String, MessageFilterI> subUriFilter
				  ) {
		super(reflectionInboundPortURI, nbThreads, nbSchedulableThreads);

	}



	protected String brokerPublicationInboundPortURI;
	protected String brokerManagementInboundPortURI;


	protected Broker(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}

	protected Broker(String uri,
			String publicationInboundPortURI,
					 String managmentInboundPortURI) throws Exception
	{
		super(uri, 0, 1) ;
		subUriPortObjMap=new HashMap<>();
		topicSubsUriMap=new HashMap<>();
		subUriFilterMap=new HashMap<>();
		topicMessageStorageMap=new HashMap<>();

		assert	uri != null :
			new PreconditionException("uri can't be null!") ;
		assert	publicationInboundPortURI != null :
			new PreconditionException("inbound port can't be null!") ;

		this.brokerPublicationInboundPortURI = uri;

		//Publish the reception port (an outbound port is always local)
		/*this.brop = new BrokerReceptionOutboundPort(receptionOutboundPortURI, this);
		this.brop.localPublishPort();
		*/
		//Publish the publication inbound port
		PortI p = new BrokerPublicationInboundPort(publicationInboundPortURI, this);
		p.publishPort();

		PortI m = new BrokerManagementInboundPort(managmentInboundPortURI, this);
		m.publishPort();
		
		// Install the plugins
		/*BrokerReceptionClientPlugin pluginReception = new BrokerReceptionClientPlugin();
		pluginReception.setPluginURI(BROKER_CLIENT_RECEPTION_PLUGIN);
		this.installPlugin(pluginReception);
		
		BrokerPublicationPlugin pluginPublication = new BrokerPublicationPlugin();
		pluginPublication.setPluginURI(BROKER_PUBLICATION_PLUGIN);
		this.installPlugin(pluginPublication);*/

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}

		this.tracer.setTitle("broker") ;
		this.tracer.setRelativePosition(1, 1) ;

		Broker.checkInvariant(this) ;

		assert	this.brokerPublicationInboundPortURI.equals(uri) :
					new PostconditionException("The URI prefix has not "
												+ "been initialised!") ;
		assert	this.isPortExisting(publicationInboundPortURI) :
					new PostconditionException("The component must have a "
							+ "port with URI " + publicationInboundPortURI) ;
		assert	this.findPortFromURI(publicationInboundPortURI).
					getImplementedInterface().equals(PublicationCI.class) :
					new PostconditionException("The component must have a "
							+ "port with implemented interface URIProviderI") ;
		assert	this.findPortFromURI(publicationInboundPortURI).isPublished() :
					new PostconditionException("The component must have a "
							+ "port published with URI " + publicationInboundPortURI) ;
	}

	@Override
	public void	start() throws ComponentStartException{

	}
	@Override
	public void execute() throws Exception{
		int i = 0;
			this.scheduleTask(
					new AbstractComponent.AbstractTask() {
						@Override
						public void run() {
							try {
								//there are messages, find recepients
								((Broker)this.getTaskOwner()).
										acceptMessages() ;
							} catch (Exception e) {
								throw new RuntimeException(e) ;
							}
						}
					},1000L, TimeUnit.MILLISECONDS) ; ;


	}

	public void acceptMessages() throws Exception {
		new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(Map.Entry<String, Set<MessageI>> entry
					: topicMessageStorageMap.entrySet()){
				String topic = entry.getKey();
				for(MessageI msg : entry.getValue()){
					//all the msgs for this topic
					//TODO include topic and filter
					try {
						for(String uriSub : topicSubsUriMap.get(topic)){
							//trouver le filte
							MessageFilterI filter ;
							if((filter=subUriFilterMap.get(uriSub))!=null){
								if(filter.filter(msg)){
									subUriPortObjMap.get(uriSub).acceptMessage(msg);
								}
							}else{
								subUriPortObjMap.get(uriSub).acceptMessage(msg);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}


		}
				 ).start();
	}


	public void publish(MessageI m, String topic) throws Exception {
		//voir le stockage et la publication
			Set<MessageI> storedMsgs;
			if((storedMsgs=topicMessageStorageMap.get(topic))!=null){
				storedMsgs.add(m);
			}else {
				Set<MessageI> s=new HashSet<>();
				s.add(m);
				topicMessageStorageMap.put(topic, s);
			}
	}

	public void publish(MessageI m, String[] topics) throws Exception {
		for(String topic : topics ){
			publish(m,topic);
		}
	}

	public void publish(MessageI[] ms, String topic) throws Exception {
			for (MessageI m :ms){
				publish(m,topic);
			}
	}

	public void publish(MessageI[] ms, String[] topics) throws Exception {
			for (MessageI m : ms ){
				for(String topic : topics ){
					publish(m,topic);
				}
			}
	}

	public void createTopic(String topic) {

		topicSubsUriMap.put(topic,new HashSet<>());
	}

	public void createTopics(String[] topics) {
		for(String t : topics){
			createTopic(t);
		}
	}

	public void destroyTopic(String topic) {
		topicSubsUriMap.remove(topic);
	}

	public boolean isTopic(String topic) {
		return topicSubsUriMap.containsKey(topic);
	}

	public String[] getTopics() {
		Set<String > tset= topicSubsUriMap.keySet();
		String [] topics = new String [tset.size()];
		return tset.toArray(topics);
	}

	public void subscribe(String topic, String inboundPortURI) throws Exception {
		String outUri="outbound-reception-broker-uri"+i;
		i++;
		this.addRequiredInterface(ReceptionCI.class);
		BrokerReceptionOutboundPort brop =
				new BrokerReceptionOutboundPort(outUri, this);
		brop.publishPort();
		this.doPortConnection(outUri,inboundPortURI,ReceptionConnector.class.getCanonicalName() );
		subUriPortObjMap.put(inboundPortURI, brop);

		Set <String> uriSet = new HashSet<>();
		uriSet.add(inboundPortURI);
		topicSubsUriMap.put(topic,uriSet);
		//create outbound port for each subscriber
		//topicSubsUriMap.get(topic).add(inboundPortURI);
		logMessage(inboundPortURI+" has subscribed");
		//brop.acceptMessage(new Message("you have been connected to "+outUri));
		try{
			brop.acceptMessage(new Message("You have been connected to "+outUri));
		}catch (Exception e){
			e.printStackTrace();
		}
	}

	public void subscribe(String[] topics, String inboutPortURI) throws Exception {
		for(String topic : topics ){
			subscribe(topic,inboutPortURI);
		}
	}

	public void subscribe(String topic, MessageFilterI filter, String inboutPortURI) throws Exception {
		topicSubsUriMap.get(topic).add(inboutPortURI);
		subUriFilterMap.put(inboutPortURI,filter);
		subscribe(topic, inboutPortURI);

	}

	public void unsubscribe(String topic, String inboundPortURI) {
		topicSubsUriMap.get(topic).remove(inboundPortURI);

	}

	public void modifyFilter(String topic,
						MessageFilterI newFilter,
						String inboundPort ){

		subUriFilterMap.remove(inboundPort);
		subUriFilterMap.put(inboundPort,newFilter);

	}

}
