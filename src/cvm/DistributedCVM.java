package cvm;

import components.*;
import connectors.*;
import fr.sorbonne_u.components.AbstractComponent;

//Copyright Jacques Malenfant, Sorbonne Universite.
//
//Jacques.Malenfant@lip6.fr
//
//This software is a computer program whose purpose is to provide a
//basic component programming model to program with components
//distributed applications in the Java programming language.
//
//This software is governed by the CeCILL-C license under French law and
//abiding by the rules of distribution of free software.  You can use,
//modify and/ or redistribute the software under the terms of the
//CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
//URL "http://www.cecill.info".
//
//As a counterpart to the access to the source code and  rights to copy,
//modify and redistribute granted by the license, users are provided only
//with a limited warranty  and the software's author,  the holder of the
//economic rights,  and the successive licensors  have only  limited
//liability.
//
//In this respect, the user's attention is drawn to the risks associated
//with loading,  using,  modifying and/or developing or reproducing the
//software by the user in light of its specific status of free software,
//that may mean  that it is complicated to manipulate,  and  that  also
//therefore means  that it is reserved for developers  and  experienced
//professionals having in-depth computer knowledge. Users are therefore
//encouraged to load and test the software's suitability as regards their
//requirements in conditions enabling the security of their systems and/or
//data to be ensured and,  more generally, to use and operate it in the
//same conditions as regards security.
//
//The fact that you are presently reading this means that you have had
//knowledge of the CeCILL-C license and that you accept its terms.

import fr.sorbonne_u.components.cvm.*;
import fr.sorbonne_u.components.examples.ddeployment_cs.components.DynamicAssembler;

//-----------------------------------------------------------------------------
/**
 * The class <code>DistributedCVM</code> creates a component assembly for the
 * multiple-JVM execution of the dynamic deployment example.
 *
 * <p><strong>Description</strong></p>
 *
 * <p><strong>Invariant</strong></p>
 *
 * <pre>
 * invariant		true
 * </pre>
 *
 * <p>Created on : 2014-03-14</p>
 *
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class DistributedCVM
        extends AbstractDistributedCVM
{
    /* The following must be the same as the ones in the confi.xml file.	*/
    protected static String		DOG_JVM_URI = "jvm_dog" ;
    protected static String		CAT_JVM_URI = "jvm_cat" ;


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

    protected DynamicAssembler	da ;

    public DistributedCVM(
            String[] args,
            int xLayout,
            int yLayout
    ) throws Exception
    {
        super(args, xLayout, yLayout) ;
    }

    /**
     * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#initialise()
     */
    @Override
    public void			initialise() throws Exception
    {
        super.initialise() ;
        /*
        String[] jvmURIs = this.configurationParameters.getJvmURIs() ;
        boolean assemblerJVM_URI_OK = false ;
        boolean providerJVM_URI_OK = false ;
        boolean consumerJVM_URI_OK = false ;
        for (int i = 0 ; i < jvmURIs.length &&
                (!assemblerJVM_URI_OK ||
                        !providerJVM_URI_OK ||
                        !consumerJVM_URI_OK) ; i++) {
            if (jvmURIs[i].equals(ASSEMBLER_JVM_URI)) {
                assemblerJVM_URI_OK = true ;
            } else if (jvmURIs[i].equals(PROVIDER_JVM_URI)) {
                providerJVM_URI_OK = true ;
            }
            }
        }
        assert	assemblerJVM_URI_OK && providerJVM_URI_OK &&
                consumerJVM_URI_OK ;*/
    }

    /**
     *
     * <p><strong>Contract</strong></p>
     *
     * <pre>
     * pre	true				// no more preconditions.
     * post	true				// no more postconditions.
     * </pre>
     *
     * @see fr.sorbonne_u.components.cvm.AbstractDistributedCVM#instantiateAndPublish()
     */
    @Override
    public void			instantiateAndPublish() throws Exception
    {
        if (thisJVMURI.equals(CAT_JVM_URI)) {

            this.brokerURI = AbstractComponent.createComponent(
                    Broker.class.getCanonicalName(),
                    new Object[]{BROKER_COMPONENT_URI,
                            BROKER_PUBLICATION_INBOUND_PORT,
                            BROKER_MANAGEMENT_INBOUND_PORT,5,5});

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

        }else{
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
        }

        super.instantiateAndPublish();
    }

    public static void	main(String[] args)
    {
        try {
            DistributedCVM dda = new DistributedCVM(args, 2, 5) ;
            dda.startStandardLifeCycle(20000) ;
            Thread.sleep(25000L) ;
            System.exit(0) ;
        } catch (Exception e) {
            throw new RuntimeException(e) ;
        }
    }
}
//-----------------------------------------------------------------------------
