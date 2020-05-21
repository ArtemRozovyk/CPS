package cvm;

import components.*;
import connectors.*;
import fr.sorbonne_u.components.*;
import fr.sorbonne_u.components.cvm.*;
import fr.sorbonne_u.components.examples.ddeployment_cs.components.*;
import fr.sorbonne_u.components.ports.*;
import util.replication.combinators.*;
import util.replication.components.*;
import util.replication.connectors.*;
import util.replication.examples.deployments.*;
import util.replication.interfaces.*;
import util.replication.ports.*;
import util.replication.selectors.*;

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
public class DistributedCVM_final
        extends AbstractDistributedCVM
{
    /* The following must be the same as the ones in the confi.xml file.	*/

    protected static String		BROKER1_JVM_URI = "jvm_b1" ;
    protected static String		BROKER2_JVM_URI = "jvm_b2" ;
    protected static String		REPLICATION_JVM_URI = "jvm_repl" ;






    // NEW CVM DISTRIBUTED ATTRIBUTES
    protected String subscriberTestUri;
    protected String brokerURI;
    protected String publisherTestURI;


    public static final String BROKER_PUBLICATION_INBOUND_PORT = "i-broker-publication";
    public static final String BROKER_MANAGEMENT_INBOUND_PORT = "i-broker-management";
    public static final String SUBSCRIBER_CUBA_COMPONENT_URI = "cuba-sub-URI-publisher";
    public static final String PUBLISHER_FRANCE_COMPONENT_URI = "my-URI-publisher-france";



    public static final String[]		SERVER_BROKER_INBOUND_PORT_URIS =
            new String[]{
                    "broker-service-1",
                    "broker-service-2"
            } ;
    public static final String[]		BROKER_COMPONENT_URIs =
            new String[]{
                    "broker-cp-1",
                    "broker-cp-2"
            } ;
    public static final String[]		SERVER_INBOUND_PORT_URIS =
            new String[]{
                    "server-service-1",
                    "server-service-2",
                    "server-service-3"
            } ;


    public static final String			MANAGER_INBOUND_PORT_URI = "manager" ;
    public static final int				NUMBER_OF_CLIENTS = 10 ;

    public enum SelectorType {
        SINGLE_ROUND_ROBIN,
        SINGLE_RANDOM,
        MANY_SUBSET,
        MANY_ALL
    }

    public enum CombinatorType {
        LONE,
        FIXED,
        MAJORITY_VOTE,
        RANDOM
    }

    protected final CVM_NonBlocking.SelectorType currentSelector = CVM_NonBlocking.SelectorType.MANY_ALL ;
    protected final int				fixedIndex = 0 ;
    protected final ReplicationManagerNonBlocking.CallMode currentCallMode = ReplicationManagerNonBlocking.CallMode.ALL ;
    protected final CVM_NonBlocking.CombinatorType currentCombinator = CVM_NonBlocking.CombinatorType.MAJORITY_VOTE ;

    public static final PortFactoryI PC =
            new PortFactoryI() {
                @Override
                public InboundPortI createInboundPort(ComponentI c)
                        throws Exception
                {
                    return new ReplicableInboundPortNonBlocking<String>(c) ;
                }

                @Override
                public InboundPortI createInboundPort(String uri, ComponentI c)
                        throws Exception
                {
                    return new ReplicableInboundPortNonBlocking<String>(uri, c) ;
                }

                @Override
                public OutboundPortI createOutboundPort(ComponentI c)
                        throws Exception
                {
                    return new ReplicableOutboundPort<String>(c) ;
                }

                @Override
                public OutboundPortI createOutboundPort(String uri, ComponentI c)
                        throws Exception
                {
                    return new ReplicableOutboundPort<String>(uri, c) ;
                }

                @Override
                public String getConnectorClassName() {
                    return ReplicableConnector.class.getCanonicalName() ;
                }
            } ;




    protected DynamicAssembler	da ;

    public DistributedCVM_final(
            String[] args,
            int xLayout,
            int yLayout
    ) throws Exception
    {
        super(args, xLayout, yLayout) ;
    }

    /**
     * @see AbstractDistributedCVM#initialise()
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
     * @see AbstractDistributedCVM#instantiateAndPublish()
     */
    @Override
    public void			instantiateAndPublish() throws Exception
    {
        if (thisJVMURI.equals(BROKER1_JVM_URI)) {
            int i = 0;
            this.brokerURI = AbstractComponent.createComponent(
                    Broker.class.getCanonicalName(),
                    new Object[]{BROKER_COMPONENT_URIs[i],
                            BROKER_PUBLICATION_INBOUND_PORT+i,//will be found by reflexion
                            BROKER_MANAGEMENT_INBOUND_PORT+i,
                            MANAGER_INBOUND_PORT_URI,
                            SERVER_BROKER_INBOUND_PORT_URIS[i]
                            ,5
                            ,5});

            assert this.isDeployedComponent(BROKER_COMPONENT_URIs[i]);
            this.toggleTracing(BROKER_COMPONENT_URIs[i]);
            this.toggleLogging(BROKER_COMPONENT_URIs[i]);

            this.subscriberTestUri = AbstractComponent.createComponent(
                    SubscriberTest.class.getCanonicalName(),
                    new Object[]{SUBSCRIBER_CUBA_COMPONENT_URI,BROKER_COMPONENT_URIs[0]});

            assert this.isDeployedComponent(this.subscriberTestUri);
            this.toggleTracing(this.subscriberTestUri);
            this.toggleLogging(this.subscriberTestUri);

        }
        if (thisJVMURI.equals(BROKER2_JVM_URI)) {
            int i =1;
            this.brokerURI = AbstractComponent.createComponent(
                    Broker.class.getCanonicalName(),
                    new Object[]{BROKER_COMPONENT_URIs[i],
                            BROKER_PUBLICATION_INBOUND_PORT+i,//will be found by reflexion
                            BROKER_MANAGEMENT_INBOUND_PORT+i,
                            MANAGER_INBOUND_PORT_URI,
                            SERVER_BROKER_INBOUND_PORT_URIS[i]
                            ,5
                            ,5});

            assert this.isDeployedComponent(BROKER_COMPONENT_URIs[i]);
            this.toggleTracing(BROKER_COMPONENT_URIs[i]);
            this.toggleLogging(BROKER_COMPONENT_URIs[i]);
            this.publisherTestURI = AbstractComponent.createComponent(
                    PublisherTest.class.getCanonicalName(),
                    new Object[]{PUBLISHER_FRANCE_COMPONENT_URI,BROKER_COMPONENT_URIs[1]});
            assert this.isDeployedComponent(this.publisherTestURI);
            this.toggleTracing(this.publisherTestURI);
            this.toggleLogging(this.publisherTestURI);

        }

        if (thisJVMURI.equals(REPLICATION_JVM_URI)) {
            createReplicationManager();

        }


            super.instantiateAndPublish();
    }

    protected void		createReplicationManager() throws Exception
    {

        AbstractComponent.createComponent(
                ReplicationManagerNonBlocking.class.getCanonicalName(),
                new Object[]{
                        SERVER_BROKER_INBOUND_PORT_URIS.length,
                        MANAGER_INBOUND_PORT_URI,
                        new WholeSelector()
                        ,
                        this.currentCallMode,

                        new RandomCombinator<String>(),
                        PC,
                        SERVER_BROKER_INBOUND_PORT_URIS
                }) ;

    }
    public static void	main(String[] args)
    {
        try {
            DistributedCVM_final dda = new DistributedCVM_final(args, 2, 5) ;
            dda.startStandardLifeCycle(20000) ;
            Thread.sleep(25000L) ;
            System.exit(0) ;
        } catch (Exception e) {
            throw new RuntimeException(e) ;
        }
    }
}
//-----------------------------------------------------------------------------
