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

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.*;

import connectors.ReceptionConnector;

public class Broker extends AbstractComponent {

    class SubHandler{
        String subUri;
        BrokerReceptionOutboundPort port;
        String topic;
        MessageFilterI filter;
        public SubHandler(String subUri, BrokerReceptionOutboundPort port, String topic) {
            this.subUri = subUri;
            this.port = port;
            this.topic = topic;
        }
        public SubHandler(String subUri, BrokerReceptionOutboundPort port, String topic, MessageFilterI filter) {
            this.subUri = subUri;
            this.port = port;
            this.topic = topic;
            this.filter = filter;
        }
    }

    private class MsgEntry{
        public MsgEntry(MessageI message, String topic) {
            this.message = message;
            this.topic = topic;
        }

        MessageI message;
        String topic;
    }
	private static int i;

	private Map<String, ArrayDeque<MessageI>> topicMessageStorageMap;
    private Map<String, ArrayList<SubHandler>>topicSubHandlersMap;

    private final Lock lock = new ReentrantLock();
    private final ReentrantReadWriteLock lockSubscribers = new ReentrantReadWriteLock();
    Condition condEmpty = lock.newCondition();
    Condition condEmptySubs = lockSubscribers.writeLock().newCondition();

	protected String brokerPublicationInboundPortURI;
	protected String acceptionExecutorURI="handler1";
	protected String publishingExecutorURI="handler2";
	protected String subscriptionExecutorURI="handler3";

	protected Broker(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}

	protected Broker(String uri,
			String publicationInboundPortURI,
					 String managmentInboundPortURI) throws Exception
	{
		super(uri, 0, 2) ;

        topicMessageStorageMap=new HashMap<>();
        topicSubHandlersMap=new HashMap<>();

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
	    this.createNewExecutorService(acceptionExecutorURI,5,true);
	    this.createNewExecutorService(publishingExecutorURI,5,true);
	    this.createNewExecutorService(subscriptionExecutorURI,5,true);
        handleRequestAsync(acceptionExecutorURI,new AbstractComponent.AbstractService<Void>() {
            @Override
            public Void call() throws Exception {
                ((Broker)this.getServiceOwner()).acceptMessages();
                return null;
            }
        });

	}

    public void publish(MessageI m, String topic) throws Exception {
               storePublished(m,topic);
    }

    public void storePublished(MessageI m, String topic){
        lock.lock();
        try{
            if(topicMessageStorageMap.containsKey(topic)){
                topicMessageStorageMap.get(topic).addLast(m);
            }else{
                ArrayDeque<MessageI> queue = new ArrayDeque<>();
                queue.add(m);
                topicMessageStorageMap.put(topic,queue);
            }
            System.out.println("Stored "+m);
            condEmpty.signal();
        }finally {
            lock.unlock();
        }
    }

    public void deliver(MsgEntry msgEntry) throws Exception {

            System.out.println("Sending"+msgEntry.message);
            if(topicSubHandlersMap.containsKey(msgEntry.topic)){
                topicSubHandlersMap.get(msgEntry.topic).get(0).port.acceptMessage(msgEntry.message);
                Collections.shuffle(topicSubHandlersMap.get(msgEntry.topic));
            }

   }

    public void subscribe(String topic, String inboundPortURI) throws Exception {
        handleRequestAsync(subscriptionExecutorURI,new AbstractComponent.AbstractService<Void>() {
            @Override
            public Void call() throws Exception {
                ((Broker)this.getServiceOwner()).subscribeAux(topic,inboundPortURI);
                return null;
            }
        });
    }

    public void subscribeAux(String topic, String inboundPortURI) throws Exception {
        lockSubscribers.writeLock().lock();
        try{
            String outUri="outbound-reception-broker-uri"+i;
            i++;
            this.addRequiredInterface(ReceptionCI.class);
            BrokerReceptionOutboundPort brop =
                    new BrokerReceptionOutboundPort(outUri, this);
            brop.publishPort();
            this.doPortConnection(outUri,inboundPortURI,ReceptionConnector.class.getCanonicalName() );
            if(topicSubHandlersMap.containsKey(topic)){
                topicSubHandlersMap.get(topic).add(new SubHandler(outUri,brop,topic));
            }else {
                ArrayList<SubHandler> l = new ArrayList<>();
                l.add(new SubHandler(outUri,brop,topic));
                topicSubHandlersMap.put(topic,l);
            }
            logMessage(inboundPortURI+" has subscribed");
            System.out.println("Subed to"+topic);
        }finally {
            lockSubscribers.writeLock().unlock();
        }
    }



        public void acceptMessages() throws Exception {
        MsgEntry msgEntry=null;
        while(true){
            lock.lock();
            try{
                while(isEmptyMap()){
                    System.out.println("entering empty msg");
                    condEmpty.await();
                    System.out.println("ending empty msg");
                }
                msgEntry = popMessageMap();
                assert (msgEntry!=null);
            }finally {
                lock.unlock();
            }
            MsgEntry finalMsgEntry = msgEntry;
            handleRequestAsync(acceptionExecutorURI,new AbstractComponent.AbstractService<Void>() {
                @Override
                public Void call() throws Exception {
                    ((Broker)this.getServiceOwner()).deliver(finalMsgEntry);
                    return null;
                }
            });
        }
    }

    private boolean isEmptyMap() {
        for (Map.Entry<String, ArrayDeque<MessageI>>
                entryTopicQueue : topicMessageStorageMap.entrySet()) {
            if (!entryTopicQueue.getValue().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private MsgEntry popMessageMap(){
	    for (Map.Entry<String, ArrayDeque<MessageI>>
                entryTopicQueue :topicMessageStorageMap.entrySet()){
            if(!entryTopicQueue.getValue().isEmpty()){
                return new MsgEntry(entryTopicQueue.getValue().pop(),entryTopicQueue.getKey());
            }
        }
	    return null;
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
        //FIXME
		//topicSubsUriMap.put(topic,new HashSet<>());
	}

	public void createTopics(String[] topics) {
		for(String t : topics){
			createTopic(t);
		}
	}

	public void destroyTopic(String topic) {
		//topicSubsUriMap.remove(topic);
	}

	public boolean isTopic(String topic) {
		//return topicSubsUriMap.containsKey(topic);
        return true;
	}

	public String[] getTopics() {
	//	Set<String > tset= topicSubsUriMap.keySet();
		//String [] topics = new String [tset.size()];
		//return tset.toArray(topics);
        return null;
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
		//topicSubsUriMap.get(topic).add(inboutPortURI);
		//subUriFilterMap.put(inboutPortURI,filter);
		subscribe(topic, inboutPortURI);

	}

	public void unsubscribe(String topic, String inboundPortURI) {
		//topicSubsUriMap.get(topic).remove(inboundPortURI);

	}

	public void modifyFilter(String topic,
						MessageFilterI newFilter,
						String inboundPort ){

		//subUriFilterMap.remove(inboundPort);
		//subUriFilterMap.put(inboundPort,newFilter);

	}

}
