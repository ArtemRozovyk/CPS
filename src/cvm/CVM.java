package cvm;

import components.*;
import connectors.ManagementConnector;
import connectors.PublicationConnector;
import connectors.ReceptionConnector;
import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;

/**
 * The class <code>CVM</code> implements the single JVM assembly
 */
public class CVM extends AbstractCVM {



    public CVM(boolean isDistributed) throws Exception {
        super(isDistributed);
    }

    protected String subscriberCuba;
    protected String brokerURI;
    protected String publisherURI;
    protected String publisherAlaksaURI;
    protected String publisherFranceURI;
    protected String publisherUKURI;
    protected String subscriberURI2;
    protected String subscriberAlaska;
    public static final String PUBLISHER_COMPONENT_URI = "my-URI-publisher";
    public static final String PUBLISHER_ALASKA_COMPONENT_URI = "my-URI-publisher-alaska";
    public static final String PUBLISHER_FRANCE_COMPONENT_URI = "my-URI-publisher-france";
    public static final String PUBLISHER_UK_COMPONENT_URI = "my-URI-publisher-uk";
    public static final String BROKER_COMPONENT_URI = "my-URI-broker";
    public static final String SUBSCRIBER2_COMPONENT_URI = "my-URI-subscriber2";
    public static final String BROKER_PUBLICATION_INBOUND_PORT = "i-broker-publication";
    public static final String BROKER_MANAGEMENT_INBOUND_PORT = "i-broker-management";
    public static final String PUBLISHER_MANAGEMENT_INBOUND_PORT = "i-publisher-management";
    public static final String PUBLISHER_PUBLICATION_OUTBOUND_PORT = "o-publisher-publication";
    public static final String SUBSCRIBER2_MANAGEMENT_OUTBOUND_PORT = "o-subscriber2-management";
    public static final String SUBSCRIBER_ALASKA_COMPONENT_URI = "alaska-sub-URI-publisher";
    public static final String SUBSCRIBER_CUBA_COMPONENT_URI = "cuba-sub-URI-publisher";

    public CVM() throws Exception {
        super();
    }



    /**
     * Creates the components, publishes theirs ports
     * and links them together
     */
    @Override
    public void deploy() throws Exception {
        assert !this.deploymentDone();

        //Create the Broker component
        this.brokerURI = AbstractComponent.createComponent(
                Broker.class.getCanonicalName(),
                new Object[]{BROKER_COMPONENT_URI,
                        BROKER_PUBLICATION_INBOUND_PORT,
                        BROKER_MANAGEMENT_INBOUND_PORT});

        assert this.isDeployedComponent(this.brokerURI);
        this.toggleTracing(this.brokerURI);
        this.toggleLogging(this.brokerURI);

        // Create the Publisher component
        this.publisherURI = AbstractComponent.createComponent(
                Publisher.class.getCanonicalName(),
                new Object[]{PUBLISHER_COMPONENT_URI,
                        PUBLISHER_PUBLICATION_OUTBOUND_PORT,
                        PUBLISHER_MANAGEMENT_INBOUND_PORT});

        assert this.isDeployedComponent(this.publisherURI);
        this.toggleTracing(this.publisherURI);
        this.toggleLogging(this.publisherURI);


        //Create the Subscriber1 Component
        this.subscriberURI2 = AbstractComponent.createComponent(
                Subscriber.class.getCanonicalName(),
                new Object[]{SUBSCRIBER2_COMPONENT_URI,
                        SUBSCRIBER2_MANAGEMENT_OUTBOUND_PORT
                        , BROKER_MANAGEMENT_INBOUND_PORT
                });

        assert this.isDeployedComponent(this.subscriberURI2);
        this.toggleTracing(this.subscriberURI2);
        this.toggleLogging(this.subscriberURI2);


        this.doPortConnection(
                this.publisherURI,
                PUBLISHER_PUBLICATION_OUTBOUND_PORT,
                BROKER_PUBLICATION_INBOUND_PORT,
                PublicationConnector.class.getCanonicalName());

        this.publisherAlaksaURI = AbstractComponent.createComponent(
                PublisherAlaska.class.getCanonicalName(),
                new Object[]{PUBLISHER_ALASKA_COMPONENT_URI});

        assert this.isDeployedComponent(this.publisherAlaksaURI);
        this.toggleTracing(this.publisherAlaksaURI);
        this.toggleLogging(this.publisherAlaksaURI);


        //plugin subscriberAlaksa
        this.subscriberAlaska = AbstractComponent.createComponent(
                SubscriberAlaska.class.getCanonicalName(),
                new Object[]{SUBSCRIBER_ALASKA_COMPONENT_URI});

        assert this.isDeployedComponent(this.subscriberAlaska);
        this.toggleTracing(this.subscriberAlaska);
        this.toggleLogging(this.subscriberAlaska);

        //subscriber cuba
        this.subscriberCuba = AbstractComponent.createComponent(
                SubscriberCuba.class.getCanonicalName(),
                new Object[]{SUBSCRIBER_CUBA_COMPONENT_URI});

        assert this.isDeployedComponent(this.subscriberCuba);
        this.toggleTracing(this.subscriberCuba);
        this.toggleLogging(this.subscriberCuba);

        // Publisher France
        this.publisherFranceURI = AbstractComponent.createComponent(
                PublisherFrance.class.getCanonicalName(),
                new Object[]{PUBLISHER_FRANCE_COMPONENT_URI});

        assert this.isDeployedComponent(this.publisherFranceURI);
        this.toggleTracing(this.publisherFranceURI);
        this.toggleLogging(this.publisherFranceURI);

        // Publisher UK
        this.publisherUKURI = AbstractComponent.createComponent(
                PublisherUK.class.getCanonicalName(),
                new Object[]{PUBLISHER_UK_COMPONENT_URI});

        assert this.isDeployedComponent(this.publisherUKURI);
        this.toggleTracing(this.publisherUKURI);
        this.toggleLogging(this.publisherUKURI);

        super.deploy();
        assert this.deploymentDone();
    }

    /**
     * Disconnect the components
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true				// no more preconditions.
     * post	true				// no more postconditions.
     * </pre>
     *
     * @see fr.sorbonne_u.components.cvm.AbstractCVM#shutdown()
     */

    public static void main(String[] args) {
        try {
            //while(true){
            CVM c = new CVM();
            c.startStandardLifeCycle(14555L);
            Thread.sleep(20000L);
            System.out.println(Broker.deliverycount + " del count ");
            System.out.println(Broker.popcount + " pop count ");
            System.out.println(Broker.actualdeliverycount + " actual count ");
            System.out.println(Broker.externCount + " exte count ");
            System.exit(0);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
