package components;

import connectors.ReceptionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.exceptions.ComponentShutdownException;
import fr.sorbonne_u.components.exceptions.PostconditionException;
import fr.sorbonne_u.components.exceptions.PreconditionException;
import fr.sorbonne_u.components.ports.PortI;
import interfaces.MessageI;
import interfaces.PublicationCI;
import interfaces.ReceptionCI;
import message.MessageFilterI;
import ports.BrokerManagementInboundPort;
import ports.BrokerPublicationInboundPort;
import ports.BrokerReceptionOutboundPort;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Broker extends AbstractComponent {

    public static int externCount = 0;
    public static int deliverycount = 0;
    public static int actualdeliverycount = 0;
    public static int popcount = 0;
    private static int i;
    private final Lock lock = new ReentrantLock();
    private final ReentrantReadWriteLock lockSubscribers = new ReentrantReadWriteLock();
    protected String brokerPublicationInboundPortURI;
    protected String acceptionExecutorURI = "handler1";
    protected String publishingExecutorURI = "handler2";
    protected String subscriptionExecutorURI = "handler3";
    Condition condEmpty = lock.newCondition();
    Condition condEmptySubs = lockSubscribers.writeLock().newCondition();

    //On runtime, the values will be given HashSet type
    //that makes no guarantees as to the iteration order of the set.
    private Map<String, Set<MessageI>> topicMessageStorageMap;
    private Map<String, Set<SubHandler>> topicSubHandlersMap;

    protected Broker(int nbThreads, int nbSchedulableThreads) {
        super(nbThreads, nbSchedulableThreads);
    }

    protected Broker(String uri,
                     String publicationInboundPortURI,
                     String managmentInboundPortURI) throws Exception {
        super(uri, 1, 1);

        topicMessageStorageMap = new HashMap<>();
        topicSubHandlersMap = new HashMap<>();

        assert uri != null :
                new PreconditionException("uri can't be null!");
        assert publicationInboundPortURI != null :
                new PreconditionException("inbound port can't be null!");

        this.brokerPublicationInboundPortURI = uri;
        PortI p = new BrokerPublicationInboundPort(publicationInboundPortURI, this);
        p.publishPort();

        PortI m = new BrokerManagementInboundPort(managmentInboundPortURI, this);
        m.publishPort();

        if (AbstractCVM.isDistributed) {
            this.executionLog.setDirectory(System.getProperty("user.dir"));
        } else {
            this.executionLog.setDirectory(System.getProperty("user.home"));
        }
        this.createNewExecutorService(acceptionExecutorURI, 5, false);
        this.createNewExecutorService(publishingExecutorURI, 5, false);
        this.createNewExecutorService(subscriptionExecutorURI, 1, false);
        this.tracer.setTitle("broker");
        this.tracer.setRelativePosition(0, 3);
        Broker.checkInvariant(this);

        assert this.brokerPublicationInboundPortURI.equals(uri) :
                new PostconditionException("The URI prefix has not "
                        + "been initialised!");
        assert this.isPortExisting(publicationInboundPortURI) :
                new PostconditionException("The component must have a "
                        + "port with URI " + publicationInboundPortURI);
        assert this.findPortFromURI(publicationInboundPortURI).
                getImplementedInterface().equals(PublicationCI.class) :
                new PostconditionException("The component must have a "
                        + "port with implemented interface URIProviderI");
        assert this.findPortFromURI(publicationInboundPortURI).isPublished() :
                new PostconditionException("The component must have a "
                        + "port published with URI " + publicationInboundPortURI);
    }

    @Override
    public void execute() throws Exception {

        handleRequestAsync(acceptionExecutorURI, new AbstractComponent.AbstractService<Void>() {
            @Override
            public Void call() throws Exception {
                ((Broker) this.getServiceOwner()).acceptMessages();
                return null;
            }
        });

    }

    public void publish(MessageI m, String topic) throws Exception {
        //System.out.println("Extern pub "+m+" in "+Thread.currentThread());
        externCount++;
        //FIXME 2
        this.handleRequestAsync(publishingExecutorURI, new AbstractComponent.AbstractService<Void>() {
            @Override
            public Void call() {
                ((Broker) this.getServiceOwner()).storePublished(m, topic);
                return null;
            }
        });

    }

    public void subscribe(String topic, String inboundPortURI) throws Exception {

        handleRequestAsync(subscriptionExecutorURI, new AbstractComponent.AbstractService<Void>() {
            @Override
            public Void call() throws Exception {
                ((Broker) this.getServiceOwner()).subscribeAux(topic, inboundPortURI);
                return null;
            }
        });
    }

    public void unsubscribe(String topic, String inboundPortURI) throws Exception {
        handleRequestAsync(subscriptionExecutorURI, new AbstractComponent.AbstractService<Void>() {
            @Override
            public Void call() throws Exception {
                ((Broker) this.getServiceOwner()).removeSubscriber(topic, inboundPortURI);
                return null;
            }
        });
    }

    public void acceptMessages() throws Exception {
        MsgEntry msgEntry;
        while (true) {
            //locking acces to message storage.
            lock.lock();
            try {
                while (isEmptyMap()) {
                    condEmpty.await();
                }
                msgEntry = popMessageMap();
                assert (msgEntry != null);
            } finally {
                lock.unlock();
            }
            MsgEntry finalMsgEntry = msgEntry;
            handleRequestAsync(acceptionExecutorURI, new AbstractComponent.AbstractService<Void>() {
                @Override
                public Void call() throws Exception {
                    ((Broker) this.getServiceOwner()).deliver(finalMsgEntry);
                    return null;
                }
            });
        }
    }

    public void deliver(MsgEntry msgEntry) throws Exception {
        deliverycount++;
        if (topicSubHandlersMap.containsKey(msgEntry.topic)) {
            //people trying to sub will be effectively added only
            //after we finished iterating over existing subs
            synchronized (topicSubHandlersMap.get(msgEntry.topic)) {
                for (SubHandler sh : topicSubHandlersMap.get(msgEntry.topic)) {
                    actualdeliverycount++;
                    if(sh.filter!=null){
                        if(sh.filter.filter(msgEntry.message)){
                            sh.port.acceptMessage(msgEntry.message);
                        }
                    }else {
                        sh.port.acceptMessage(msgEntry.message);
                    }

                    System.out.println("delivered " + msgEntry.topic);
                }
            }
        }
    }

    public void storePublished(MessageI m, String topic) {
        lock.lock();
        try {
            if (topicMessageStorageMap.containsKey(topic)) {
                topicMessageStorageMap.get(topic).add(m);
            } else {
                Set<MessageI> queue = Collections.synchronizedSet(new HashSet<>());
                queue.add(m);
                topicMessageStorageMap.put(topic, queue);
            }
            condEmpty.signal();
        } finally {
            lock.unlock();
        }
    }


    public void subscribeAux(String topic, String inboundPortURI) throws Exception {
        String outUri = "outbound-reception-broker-uri" + i;
        i++;
        this.addRequiredInterface(ReceptionCI.class);
        BrokerReceptionOutboundPort brop =
                new BrokerReceptionOutboundPort(outUri, this);
        brop.publishPort();
        this.doPortConnection(outUri, inboundPortURI, ReceptionConnector.class.getCanonicalName());
        logMessage(inboundPortURI + " has subscribed  to " + topic);
        System.out.println("Subed to " + topic + " in " + Thread.currentThread() + " map sz: " + sizeMessageMap());
        if (topicSubHandlersMap.containsKey(topic)) {
            synchronized (topicSubHandlersMap.get(topic)) {
                topicSubHandlersMap.get(topic).add(new SubHandler(inboundPortURI, brop, topic));
            }
        } else {
            Set<SubHandler> l = Collections.synchronizedSet(new HashSet<>());
            l.add(new SubHandler(inboundPortURI, brop, topic));
            topicSubHandlersMap.put(topic, l);
        }
    }


    private boolean isEmptyMap() {
        for (Map.Entry<String, Set<MessageI>>
                entryTopicQueue : topicMessageStorageMap.entrySet()) {
            if (!entryTopicQueue.getValue().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private int sizeMessageMap() {
        int sz = 0;
        for (Map.Entry<String, Set<MessageI>>
                entryTopicQueue : topicMessageStorageMap.entrySet()) {
            if (!entryTopicQueue.getValue().isEmpty()) {
                sz += entryTopicQueue.getValue().size();
            }
        }
        return sz;
    }

    private MsgEntry popMessageMap() {
        MessageI toRet;
        for (Map.Entry<String, Set<MessageI>>
                entryTopicQueue : topicMessageStorageMap.entrySet()) {
            if (!entryTopicQueue.getValue().isEmpty()) {
                popcount++;
                Iterator<MessageI> it = entryTopicQueue.getValue().iterator();
                if (it.hasNext()) {
                    toRet = it.next();
                    it.remove();
                } else {
                    throw new IllegalStateException("Iterator is empty," +
                            " probably has to do with your synchronization");
                }
                return new MsgEntry(toRet, entryTopicQueue.getKey());
            }
        }
        return null;
    }

    public void publish(MessageI m, String[] topics) throws Exception {
        for (String topic : topics) {
            publish(m, topic);
        }
    }

    public void publish(MessageI[] ms, String topic) throws Exception {
        for (MessageI m : ms) {
            publish(m, topic);
        }
    }

    public void publish(MessageI[] ms, String[] topics) throws Exception {
        for (MessageI m : ms) {
            for (String topic : topics) {
                publish(m, topic);
            }
        }
    }

    public void createTopic(String topic) {
        //FIXME
        //topicSubsUriMap.put(topic,new HashSet<>());
    }

    public void createTopics(String[] topics) {
        for (String t : topics) {
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


    public void subscribe(String[] topics, String inboutPortURI) throws Exception {
        for (String topic : topics) {
            subscribe(topic, inboutPortURI);
        }
    }

    public void subscribe(String topic, MessageFilterI filter, String inboutPortURI) throws Exception {
        //topicSubsUriMap.get(topic).add(inboutPortURI);
        //subUriFilterMap.put(inboutPortURI,filter);
        subscribe(topic, inboutPortURI);

    }

    public void removeSubscriber(String topic, String inboundPortURI) {
        logMessage(inboundPortURI + " WANTS TO UNSUB FROM " + topic);

        if (topicSubHandlersMap.containsKey(topic)) {
            synchronized (topicSubHandlersMap.get(topic)) {
                Iterator<SubHandler> it = topicSubHandlersMap.get(topic).iterator();
                while (it.hasNext()) {
                    SubHandler sh = it.next();
                    if (sh.subUri.equals(inboundPortURI)) {
                        logMessage(sh.subUri + " has unsubed from " + topic);
                        it.remove();
                        return;
                    }
                }
            }
        }
    }


    public void modifyFilter(String topic,
                             MessageFilterI newFilter,
                             String inboundPort) {
        //subUriFilterMap.remove(inboundPort);
        //subUriFilterMap.put(inboundPort,newFilter);

    }

    @Override
    public void shutdown() throws ComponentShutdownException {
        super.shutdown();

    }

    private static class SubHandler {
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

    private static class MsgEntry {
        MessageI message;
        String topic;

        public MsgEntry(MessageI message, String topic) {
            this.message = message;
            this.topic = topic;
        }
    }

}
