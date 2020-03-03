package components;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.examples.cps.components.ValueConsumer;
import fr.sorbonne_u.components.examples.ddeployment_cs.components.DynamicURIConsumer;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

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

	//dans une entree lock stack...
	//ls

	protected BrokerReceptionOutboundPort brop;

	private Map<String, Set<String >> topicSubsUriMap;
	private Map<String, Set<MessageI>> topicMessageStorageMap1;
	//private Map<String, Set<MessageI>> topicMessageStorageMap2;
	private Integer numberOfMsgStored=0;
	private Map<String, MessageFilterI> subUriFilterMap;
	private Map<String,BrokerReceptionOutboundPort> subUriPortObjMap;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

	protected String brokerPublicationInboundPortURI;
	protected String messageAcceptionExecutorURI="handler1";

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
		topicMessageStorageMap1=new HashMap<>();
		//topicMessageStorageMap2=new HashMap<>();

		assert	uri != null :
			new PreconditionException("uri can't be null!") ;
		assert	publicationInboundPortURI != null :
			new PreconditionException("inbound port can't be null!") ;

		this.brokerPublicationInboundPortURI = uri;
		PortI p = new BrokerPublicationInboundPort(publicationInboundPortURI, this);
		p.publishPort();

		PortI m = new BrokerManagementInboundPort(managmentInboundPortURI, this);
		m.publishPort();

		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		this.tracer.setTitle("broker") ;
		this.tracer.setRelativePosition(1, 1) ;
		Broker.checkInvariant(this);
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
	public void execute() throws Exception{
			this.createNewExecutorService(messageAcceptionExecutorURI,2,true);

			handleRequestAsync(messageAcceptionExecutorURI,new AbstractComponent.AbstractService<Void>() {
				@Override
				public Void call() throws Exception {
					((Broker)this.getServiceOwner()).acceptMessages();
					return null;
				}
			});
	}



	public void acceptMessagesConcurrent() throws Exception{

	}
    public static int k=0;

    public void publish(MessageI m, String topic) throws Exception {
        lock.writeLock().lock();
        try {
            Set<MessageI> storedMsgs;
            if ((storedMsgs = topicMessageStorageMap1.get(topic)) != null) {
                storedMsgs.add(m);
            } else {
                Set<MessageI> s = new HashSet<>();
                s.add(m);
                topicMessageStorageMap1.put(topic, s);
            }
            condEmpty.notifyAll();
        }finally {
            lock.writeLock().unlock();
        }
    }

    public void subscribe(String topic, String inboundPortURI) throws Exception {
        lock.writeLock().lock();
        String outUri="outbound-reception-broker-uri"+i;
        i++;
        this.addRequiredInterface(ReceptionCI.class);
        BrokerReceptionOutboundPort brop =
                new BrokerReceptionOutboundPort(outUri, this);
        brop.publishPort();
        this.doPortConnection(outUri,inboundPortURI,ReceptionConnector.class.getCanonicalName() );

        try{
            subUriPortObjMap.put(inboundPortURI, brop);
            if(topicSubsUriMap.containsKey(topic)){
                topicSubsUriMap.get(topic).add(inboundPortURI);
            }else{
                Set <String> uriSet = new HashSet<>();
                uriSet.add(inboundPortURI);
                topicSubsUriMap.put(topic,uriSet);
            }

            logMessage(inboundPortURI+" has subscribed");
            System.out.println("Subscription for "+topic);
        }finally {
            lock.writeLock().unlock();
        }

    }
    Condition condEmpty = lock.writeLock().newCondition();


	public void acceptMessages() throws Exception {
            HashMap<String,Set<MessageI>> alreadySent;
            while(true){
                lock.writeLock().lock();
                try{
                    alreadySent= new HashMap<>();
                    String topic;

                    while(!hasValues(topicMessageStorageMap1)){
                        condEmpty.wait();
                    }

                    for(Map.Entry<String, Set<MessageI>> entry : topicMessageStorageMap1.entrySet()){
                        topic = entry.getKey();
                        //no one for this topic yet:
                        if(!alreadySent.containsKey(topic)) alreadySent.put(topic,new HashSet<>());
                        for(MessageI msg : entry.getValue()){
                            //Thread.sleep(50);
                            for(String uriSub : topicSubsUriMap.get(topic)){
                                MessageFilterI filter ;
                                if((filter=subUriFilterMap.get(uriSub))!=null){
                                    if(filter.filter(msg)){
                                        subUriPortObjMap.get(uriSub).acceptMessage(msg);
                                    }
                                }else{
                                    subUriPortObjMap.get(uriSub).acceptMessage(msg);
                                }
                                System.out.println("Sending "+k+++" "+msg);
                                //TODO remove msg even if thereis no one to receive.

                            }
                            alreadySent.get(topic).add(msg);
                        }
                    }
                    System.out.println("ha-ha");
                    if(hasValues(alreadySent)){
                        System.out.println(alreadySent.size()+" topics sent");
                        for(Map.Entry<String, Set<MessageI>> entry : alreadySent.entrySet()){
                            System.out.println(entry.getValue().size()+" messages sent for "+entry.getKey());
                            for(MessageI m : entry.getValue()){
                                topicMessageStorageMap1.get(entry.getKey()).remove(m);
                                System.out.println("removing "+jj+++m);
                            }
                        }
                    }

                } finally {
                    lock.writeLock().unlock();
                }
            }
	}

    public static int jj=0;

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
        //FIXME
		//topicSubsUriMap.put(topic,new HashSet<>());
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
    private boolean hasValues(Map <String,Set<MessageI>> map){
        for(Map.Entry<String, Set<MessageI>> entry : map.entrySet()){
            if(!entry.getValue().isEmpty()){
                return true;
            }
        }
        return false;
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
