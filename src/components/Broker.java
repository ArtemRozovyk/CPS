package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.AddPlugin;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.examples.basic_cs.components.URIProvider;
import fr.sorbonne_u.components.examples.basic_cs.interfaces.URIProviderI;
import fr.sorbonne_u.components.exceptions.ComponentStartException;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.plugins.dconnection.example.components.ServerSideExample;
import fr.sorbonne_u.components.ports.PortI;
import interfaces.MessageFilterI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import message.Message;
import ports.BrokerPublicationInboundPort;
import ports.BrokerReceptionOutboundPort;

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
	
	public Broker(int nbThreads, int nbSchedulableThreads, BrokerReceptionOutboundPort brop,
			Map<String, Set<String>> topicSubsUriMap, Map<String, Set<MessageI>> topicMessageStorageMap,
			Map<String, MessageFilterI> subUriFilterMap, Map<String, BrokerReceptionOutboundPort> subUriPortObjMapMap,
			String brokerReceptionOutboundPortURI, String brokerPublicationInboundPortURI) {
		super(nbThreads, nbSchedulableThreads);
		this.brop = brop;
		this.topicSubsUriMap = topicSubsUriMap;
		this.topicMessageStorageMap = topicMessageStorageMap;
		this.subUriFilterMap = subUriFilterMap;
		this.subUriPortObjMap = subUriPortObjMapMap;
		this.brokerReceptionOutboundPortURI = brokerReceptionOutboundPortURI;
		this.brokerPublicationInboundPortURI = brokerPublicationInboundPortURI;
	}



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



	protected String brokerReceptionOutboundPortURI;
	
	protected String brokerPublicationInboundPortURI;

	protected Broker(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}
	
	protected Broker(String uri,
			String receptionOutboundPortURI,
			String publicationInboundPortURI) throws Exception
	{
		super(uri, 0, 1) ;
		
		assert	uri != null :
			new PreconditionException("uri can't be null!") ;
		assert	publicationInboundPortURI != null :
			new PreconditionException("inbound port can't be null!") ;
		
		this.brokerPublicationInboundPortURI = uri;
		
		//Publish the reception port (an outbound port is always local)
		this.brop = new BrokerReceptionOutboundPort(receptionOutboundPortURI, this);
		this.brop.localPublishPort();
		
		//Publish the publication inbound port
		PortI p = new BrokerPublicationInboundPort(publicationInboundPortURI, this);
		p.publishPort();
		
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
		this.scheduleTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
											
				} catch (Exception e) {
					throw new RuntimeException(e) ;
				}
			}
		}, 1000, TimeUnit.MILLISECONDS) ;
	}
	
	public void publish(MessageI m, String topic) throws Exception {
		Set<MessageI> storedMsgs;
		if((storedMsgs=topicMessageStorageMap.get(topic))!=null){
			storedMsgs.add(m);
		}else {
			//topic doesn't exist
			throw new Exception("Topic doesnt exist");
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
		BrokerReceptionOutboundPort brop = 
				new BrokerReceptionOutboundPort(outUri, this);
		brop.localPublishPort();
		this.doPortConnection(outUri, inboundPortURI,ReceptionConnector.class.getCanonicalName() );
		subUriPortObjMap.put(inboundPortURI, brop);
		
		//create outbound port for each subscriber 
		topicSubsUriMap.get(topic).add(inboundPortURI);
	}

	public void subscribe(String[] topics, String inboutPortURI) throws Exception {
		for(String topic : topics ){
			subscribe(topic,inboutPortURI);
		}
	}

	public void subscribe(String topic, MessageFilterI filter, String inboutPortURI) {
		topicSubsUriMap.get(topic).add(inboutPortURI);
		subUriFilterMap.put(inboutPortURI,filter);

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
