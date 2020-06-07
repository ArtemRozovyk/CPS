package components;

import connectors.ReceptionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.examples.pingpong.components.*;
import fr.sorbonne_u.components.exceptions.*;
import interfaces.*;
import message.*;
import org.junit.jupiter.api.extension.*;
import ports.BrokerManagementInboundPort;
import ports.BrokerPublicationInboundPort;
import ports.BrokerReceptionOutboundPort;
import util.replication.connectors.*;
import util.replication.interfaces.*;
import util.replication.ports.*;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Broker component. It is used to broadcast messages.
 * It can receive messages and publish them.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant		true
 * </pre>
 */
public class Broker extends AbstractComponent implements ReplicaI<String> {

    public static int externCount = 0;
    public static int deliverycount = 0;
    public static int actualdeliverycount = 0;
    public static int popcount = 0;
    private static int i;
    private int brokerId;
    private final Lock lock = new ReentrantLock();
    protected String brokerPublicationInboundPortURI;
    protected String acceptionExecutorURI = "handler1";
    protected String publishingExecutorURI = "handler2";
    protected String subscriptionExecutorURI = "handler3";
    Condition condEmpty = lock.newCondition();

    //On runtime, the values will be given HashSet type
    //that makes no guarantees as to the iteration order of the set.
    private Map<String, Set<MessageI>> topicMessageStorageMap;
    private Map<String, Set<SubHandler>> topicSubHandlersMap;
    protected BrokerPublicationInboundPort bpip;
    protected BrokerManagementInboundPort bmip;
    //TODO replica2
    protected ReplicableOutboundPort<String> rop ;
    protected String							replicableInboundPortURI ;
    protected ReplicableInboundPort<String>	rip ;


    /**
     * Broker creation
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	nbThreads > 0
     * post	true			// no postcondition.
     * </pre>
     *
     * @param nbThreads            number of threads used by the component
     * @param nbSchedulableThreads number of schedulable threads
     */
    protected Broker(int nbThreads, int nbSchedulableThreads) {
        super(nbThreads, nbSchedulableThreads);
    }

    /**
     * Broker creation
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	uri != null && publicationInboundPortURI != null && managmentInboundPortURI != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @param uri                       uri of the component
     * @param publicationInboundPortURI uri of publication inbound port
     * @param managmentInboundPortURI   uri of the management inbound port
     * @throws Exception
     */
    protected Broker(int id,
                     String uri,
                     String publicationInboundPortURI,
                     String managmentInboundPortURI,
                     String replicableInboundPortURI,
                     String inboundPortURI,
                     int nbAccepingThreads,
                     int nbPublishingThreads) throws Exception {
        super(uri, 1, 1);

        //TODO replica1



        brokerId=id;
        topicMessageStorageMap = new HashMap<>();
        topicSubHandlersMap = new HashMap<>();
        addOfferedInterface(ManagementCI.class);
        addOfferedInterface(PublicationCI.class);
        addRequiredInterface(ReceptionCI.class);
        addRequiredInterface(ReplicableCI.class);
        addOfferedInterface(ReplicableCI.class);
        if(replicableInboundPortURI!=null){

            this.replicableInboundPortURI = replicableInboundPortURI ;
            this.rop = new ReplicableOutboundPort<String>(this) ;
            this.rop.publishPort() ;

            this.rip = new ReplicableInboundPort<String>(inboundPortURI, this) ;
            this.rip.publishPort() ;
        }
        assert uri != null :
                new PreconditionException("uri can't be null!");
        assert publicationInboundPortURI != null :
                new PreconditionException("inbound port can't be null!");

        this.brokerPublicationInboundPortURI = uri;
        bpip = new BrokerPublicationInboundPort(publicationInboundPortURI, this);
        bpip.publishPort();
        bmip = new BrokerManagementInboundPort(managmentInboundPortURI, this);
        bmip.publishPort();

        if (AbstractCVM.isDistributed) {
            this.executionLog.setDirectory(System.getProperty("user.dir"));
        } else {
            this.executionLog.setDirectory(System.getProperty("user.home"));
        }
        this.createNewExecutorService(acceptionExecutorURI, nbAccepingThreads, false);
        this.createNewExecutorService(publishingExecutorURI, nbPublishingThreads, false);
        this.createNewExecutorService(subscriptionExecutorURI, 1, false); // 1 is for concurrency matters
        this.tracer.setTitle("broker");
        System.out.println(uri);
        this.tracer.setRelativePosition((Integer.parseInt(uri.substring(uri.length() - 1)) % 2), 3);
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

    /**
     * Action executed by the component
     */
    @Override
    public void execute() throws Exception {
        this.runTask(acceptionExecutorURI,
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            ((Broker) this.getTaskOwner()).acceptMessages();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void start() throws ComponentStartException {
        super.start() ;
        try {
            this.doPortConnection(
                    this.rop.getPortURI(),
                    this.replicableInboundPortURI,
                    ReplicableConnector.class.getCanonicalName()) ;
        } catch (Exception e) {
            throw new ComponentStartException(e) ;
        }

    }

    /**
     * @see interfaces.PublicationCI#publish(MessageI, String)
     */
    public void publish(MessageI m, String topic) throws Exception {
        //System.out.println("Extern pub "+m+" in "+Thread.currentThread());
        externCount++;
        //FIXME 2
        //TODO Vous pourriez éviter ce passage pas une tâche en vous inspirant des derniers exemples données avec le cours 8.
        //

        //TODO is Task needed here?
        this.runTask(acceptionExecutorURI,
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            ((Broker) this.getTaskOwner()).storePublished(m, topic);
                            try {
                                String result = ((Broker) this.getTaskOwner()).rop.call(brokerId,m,topic) ;//TODO Change it
                                logMessage("Transmitted messages to other brokers, got answer : ["+result+"]");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * @see interfaces.ManagementCI#subscribe(String, String)
     */
    public void subscribe(String topic, String inboundPortURI) throws Exception {
        this.runTask(acceptionExecutorURI,
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            ((Broker) this.getTaskOwner()).subscribeAux(topic, null, inboundPortURI);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * @see interfaces.ManagementCI#subscribe(String, MessageFilterI, String)
     */
    public void subscribe(String topic, MessageFilterI filter, String inboutPortURI) throws Exception {
        this.runTask(acceptionExecutorURI,
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            ((Broker) this.getTaskOwner()).subscribeAux(topic, filter, inboutPortURI);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * @see interfaces.ManagementCI#subscribe(String[], String)
     */
    public void subscribe(String[] topics, String inboutPortURI) throws Exception {
        for (String topic : topics) {
            subscribe(topic, inboutPortURI);
        }
    }

    /**
     * @see interfaces.ManagementCI#unsubscribe(String, String)
     */
    public void unsubscribe(String topic, String inboundPortURI) throws Exception {

        this.runTask(acceptionExecutorURI,
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            ((Broker) this.getTaskOwner()).removeSubscriber(topic, inboundPortURI);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    /**
     * Iterate infinitely over map to deliver messages to subs.
     */
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
            this.runTask(acceptionExecutorURI,
                    new AbstractComponent.AbstractTask() {
                        @Override
                        public void run() {
                            try {
                                ((Broker) this.getTaskOwner()).deliver(finalMsgEntry);
                            } catch (Exception e) {
                                e.printStackTrace() ;
                            }
                        }
                    }) ;

        }
    }

    /**
     * Delivers a message to the subscribers of the
     * message's topic
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	msgEntry != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @param msgEntry the message to send
     * @throws Exception
     */
    public void deliver(MsgEntry msgEntry) throws Exception {
        deliverycount++;
        if (topicSubHandlersMap.containsKey(msgEntry.topic)) {
            //people trying to sub will be effectively added only
            //after we finished iterating over existing subs
            synchronized (topicSubHandlersMap.get(msgEntry.topic)) {
                for (SubHandler sh : topicSubHandlersMap.get(msgEntry.topic)) {
                    //System.out.println("delivering" + msgEntry.topic + msgEntry.message + " sz :" + sizeMessageMap());
                    actualdeliverycount++;
                    if (sh.filter != null) {
                        if (sh.filter.filter(msgEntry.message)) {
                            sh.port.acceptMessage(msgEntry.message);
                        }
                    } else {
                        sh.port.acceptMessage(msgEntry.message);
                    }

                    // System.out.println("delivered " + msgEntry.topic);
                }
            }
        }
    }

    /**
     * Store the publishe messages in the topic-message Map
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	m != null && topic != null
     * post	topicMessageStorageMap.isEmpty() != false
     * </pre>
     *
     * @param m     the message to be stored
     * @param topic the topic of the message
     */
    public void storePublished(MessageI m, String topic) {
        lock.lock();
            logMessage("Storing "+m+" for "+topic);



        try {
            if (topicMessageStorageMap.containsKey(topic)) {
                topicMessageStorageMap.get(topic).add(m);
            } else {
                Set<MessageI> queue = Collections.synchronizedSet(new HashSet<>());
                queue.add(m);
                topicMessageStorageMap.put(topic, queue);
            }
            //System.out.println("Stored" + m + "TT" + topic + "TT");

            condEmpty.signal();
        } finally {
            lock.unlock();
        }
    }


    /**
     * Auxiliary method used to manage subscription
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	topic != null && inboundPortURI != null
     * post
     * </pre>
     *
     * @param topic          the topic the subscriber wants to subscribe to
     * @param filter         the filter used in the subscription
     * @param inboundPortURI the inbound port of the subscriber
     * @throws Exception
     */
    private void subscribeAux(String topic, MessageFilterI filter, String inboundPortURI) throws Exception {
        String outUri = "outbound-reception-broker-uri" + i;
        i++;
        if(outUri.equals("outbound-reception-broker-uri3")){
            System.out.println("here");
        }
        BrokerReceptionOutboundPort brop =
                new BrokerReceptionOutboundPort(outUri, this);
        brop.publishPort();

        this.doPortConnection(outUri, inboundPortURI, ReceptionConnector.class.getCanonicalName());
        logMessage(inboundPortURI + " has subscribed  to " + topic+"with"+outUri);
        //System.out.println("Subed to " + topic + " in " + Thread.currentThread() + " map sz: " + sizeMessageMap());
        if (topicSubHandlersMap.containsKey(topic)) {
            synchronized (topicSubHandlersMap.get(topic)) {
                if (filter != null) {
                    topicSubHandlersMap.get(topic).add(new SubHandler(inboundPortURI, brop, topic, filter));
                } else {
                    topicSubHandlersMap.get(topic).add(new SubHandler(inboundPortURI, brop, topic));
                }
            }
        } else {
            Set<SubHandler> l = Collections.synchronizedSet(new HashSet<>());
            if (filter != null) {
                l.add(new SubHandler(inboundPortURI, brop, topic, filter));
            } else {
                l.add(new SubHandler(inboundPortURI, brop, topic));
            }
            topicSubHandlersMap.put(topic, l);
        }
    }


    /**
     * Check if the topic-message map is empty
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	topicMessageStorageMap != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @return true if the map if empty, false otherwise
     */
    private boolean isEmptyMap() {
        for (Map.Entry<String, Set<MessageI>>
                entryTopicQueue : topicMessageStorageMap.entrySet()) {
            if (!entryTopicQueue.getValue().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the size of the topic-message Map
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	topicMessageStorageMap != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @return the number of messages in the map
     */
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

    /**
     * Pop(remove and return) message from the map
     */
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

    /**
     * @see interfaces.PublicationCI#publish(MessageI, String[])
     */
    public void publish(MessageI m, String[] topics) throws Exception {
        this.runTask(acceptionExecutorURI,
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            for (String topic : topics) {
                                ((Broker) this.getTaskOwner()).publish(m, topic);
                            }
                        } catch (Exception e) {
                            e.printStackTrace() ;
                        }
                    }
                }) ;



    }

    /**
     * @see interfaces.PublicationCI#publish(MessageI[], String)
     */
    public void publish(MessageI[] ms, String topic) throws Exception {

        this.runTask(acceptionExecutorURI,
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            for (MessageI m : ms) {
                                ((Broker) this.getTaskOwner()).publish(m, topic);
                            }
                        } catch (Exception e) {
                            e.printStackTrace() ;
                        }
                    }
                }) ;


    }

    /**
     * @see interfaces.PublicationCI#publish(MessageI[], String[])
     */
    public void publish(MessageI[] ms, String[] topics) throws Exception {

        this.runTask(acceptionExecutorURI,
                new AbstractComponent.AbstractTask() {
                    @Override
                    public void run() {
                        try {
                            for (MessageI m : ms) {
                                for (String topic : topics) {
                                    ((Broker) this.getTaskOwner()).publish(m, topic);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace() ;
                        }
                    }
                }) ;


    }

    /**
     * @see interfaces.ManagementCI#createTopic(String)
     */
    public void createTopic(String topic) {
        lock.lock();
        try {
            if (topicMessageStorageMap.containsKey(topic)) return;

            topicMessageStorageMap.put(topic, new HashSet<>());
        } finally {
            lock.unlock();
        }
    }

    /**
     * @see interfaces.ManagementCI#createTopics(String[])
     */
    public void createTopics(String[] topics) {
        for (String t : topics) {
            createTopic(t);
        }
    }

    /**
     * @see interfaces.ManagementCI#destroyTopic(String)
     */
    public void destroyTopic(String topic) throws Exception {
        lock.lock();
        try {
            for (MessageI m : topicMessageStorageMap.get(topic)) {

                this.runTask(acceptionExecutorURI,
                        new AbstractComponent.AbstractTask() {
                            @Override
                            public void run() {
                                try {
                                    ((Broker) this.getTaskOwner()).deliver(new MsgEntry(m, topic));
                                } catch (Exception e) {
                                    e.printStackTrace() ;
                                }
                            }
                        }) ;

            }
            if(topicMessageStorageMap.containsKey(topic)){
                topicMessageStorageMap.get(topic).clear();
                topicMessageStorageMap.remove(topic);
            }
            if(topicSubHandlersMap.containsKey(topic)){
                for(SubHandler sh : topicSubHandlersMap.get(topic)){
                    sh.port.unpublishPort();
                    logMessage("Topic "+topic+" has been destroyed, destroying port "+sh.subUri);
                    sh.port.destroyPort();
                }
                topicSubHandlersMap.get(topic).clear();
                topicSubHandlersMap.remove(topic);
            }

        } finally {
            lock.unlock();
        }
    }

    /**
     * @see interfaces.ManagementCI#isTopic(String)
     */
    public boolean isTopic(String topic) {
        return topicMessageStorageMap.containsKey(topic) || topicSubHandlersMap.containsKey(topic);
    }

    /**
     * @see interfaces.ManagementCI#getTopics()
     */
    public String[] getTopics() {
        Set<String> tset = topicMessageStorageMap.keySet();
        String[] topics = new String[tset.size()];
        topics = tset.toArray(topics);
        return topics;
    }

    /**
     * @see interfaces.ManagementCI#getPublicatinPortURI()
     */
    public String getPublicationPortURI() {
        return brokerPublicationInboundPortURI;
    }

    /**
     * Remove a subscriber during an unsubscription
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	topic != null && inboundPortURI != null
     * post	true			// no postcondition.
     * </pre>
     *
     * @param topic          the topic you want to unsubscribe fro
     * @param inboundPortURI the inbound port of the ubscriber
     * @throws Exception
     */
    private void removeSubscriber(String topic, String inboundPortURI) throws Exception {
        logMessage(inboundPortURI + " wants to unsub from " + topic);

        if (topicSubHandlersMap.containsKey(topic)) {
            synchronized (topicSubHandlersMap.get(topic)) {
                Iterator<SubHandler> it = topicSubHandlersMap.get(topic).iterator();
                while (it.hasNext()) {
                    SubHandler sh = it.next();
                    if (sh.subUri.equals(inboundPortURI)) {
                        logMessage(sh.subUri + " has unsubed from " + topic);
                        sh.port.unpublishPort();
                        sh.port.destroyPort();

                        it.remove();
                        return;
                    }
                }
            }
        }
    }


    /**
     * @see interfaces.ManagementCI#modifyFilter(String, MessageFilterI, String)
     */
    public void modifyFilter(String topic,
                             MessageFilterI newFilter,
                             String inboundPort) {
        synchronized (topicSubHandlersMap.get(topic)) {
            topicSubHandlersMap.get(topic).forEach((SubHandler sh) -> {
                if (sh.subUri.equals(inboundPort)){
                    //System.out.println("Modifying filter for "+inboundPort+" with topic "+topic+"old "+sh.filter+" new "+newFilter);
                    sh.filter = newFilter;
                }

            });
        }
    }

    @Override
    public void finalise() throws Exception {
        try {
            System.out.println("Destroying "+topicSubHandlersMap.values().size()+" ports");
            for (Set<SubHandler> shs : topicSubHandlersMap.values()) {
                for (SubHandler sh : shs) {
                    System.out.println("Destroying " + sh.port.getPortURI());
                    sh.port.unpublishPort();
                    sh.port.destroyPort();
                }
            }
            bpip.unpublishPort();
            bpip.destroyPort();
            bmip.unpublishPort();
            bmip.destroyPort();
            this.doPortDisconnection(this.rop.getPortURI()) ;

        } catch (Exception e) {
            throw new ComponentShutdownException(e);
        }
        super.finalise();
    }

    /**
     * Shutdown of the component, unpublish and destroy the ports
     */
    @Override
    public void shutdown() throws ComponentShutdownException {
        try {
            this.rop.unpublishPort() ;
            this.rip.unpublishPort() ;
        } catch (Exception e) {
            throw new ComponentShutdownException(e) ;
        }
        super.shutdown();
    }

    @Override
    public String call(Object... parameters) throws Exception {

            //TODO proper selector
            //logMessage("Getting message "+parameters[0]+parameters[1]);
            if(parameters[0] instanceof MessageI && parameters[1] instanceof String){
                storePublished((MessageI)parameters[0],(String)parameters[1]);
            }else{
                throw new ParameterResolutionException("Bad call, not instance of MessageI or String");
            }



        return "I stored your message,  "+parameters[0];
    }

    /**
     * Subclass SubHandler used to store subscribers
     */
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

    /**
     * Subclass MsgEntry used to store messages
     */
    private static class MsgEntry {
        MessageI message;
        String topic;

        public MsgEntry(MessageI message, String topic) {
            this.message = message;
            this.topic = topic;
        }
    }

}
