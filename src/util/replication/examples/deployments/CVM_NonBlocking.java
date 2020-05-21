package util.replication.examples.deployments;

// Copyright Jacques Malenfant, Sorbonne Universite.
// Jacques.Malenfant@lip6.fr
//
// This software is a computer program whose purpose is to provide a
// basic component programming model to program with components
// distributed applications in the Java programming language.
//
// This software is governed by the CeCILL-C license under French law and
// abiding by the rules of distribution of free software.  You can use,
// modify and/ or redistribute the software under the terms of the
// CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
// URL "http://www.cecill.info".
//
// As a counterpart to the access to the source code and  rights to copy,
// modify and redistribute granted by the license, users are provided only
// with a limited warranty  and the software's author,  the holder of the
// economic rights,  and the successive licensors  have only  limited
// liability. 
//
// In this respect, the user's attention is drawn to the risks associated
// with loading,  using,  modifying and/or developing or reproducing the
// software by the user in light of its specific status of free software,
// that may mean  that it is complicated to manipulate,  and  that  also
// therefore means  that it is reserved for developers  and  experienced
// professionals having in-depth computer knowledge. Users are therefore
// encouraged to load and test the software's suitability as regards their
// requirements in conditions enabling the security of their systems and/or 
// data to be ensured and,  more generally, to use and operate it in the 
// same conditions as regards security. 
//
// The fact that you are presently reading this means that you have had
// knowledge of the CeCILL-C license and that you accept its terms.

import components.*;
import connectors.*;
import util.replication.combinators.*;
import util.replication.components.*;
import util.replication.components.ReplicationManagerNonBlocking.*;
import util.replication.connectors.*;
import util.replication.examples.components.*;
import util.replication.interfaces.*;
import util.replication.ports.*;
import util.replication.selectors.*;
import fr.sorbonne_u.components.*;
import fr.sorbonne_u.components.cvm.*;
import fr.sorbonne_u.components.ports.*;

// -----------------------------------------------------------------------------

/**
 * The class <code>DispatcherCVM</code> implements an example of replication
 * to dispatch calls among servers.
 *
 * <p><strong>Description</strong></p>
 * 
 * <table>
 * <caption>Useful combinations</caption>
 * <tr><td>SINGLE_ROUND_ROBIN</td><td>SINGLE</td><td>LONE</td></tr>
 * <tr><td>SINGLE_RANDOM</td>     <td>SINGLE</td><td>LONE</td></tr>
 * <tr><td>MANY_SUBSET</td>       <td>ANY</td>   <td>LONE</td></tr>
 * <tr><td>MANY_SUBSET</td>       <td>FIRST</td> <td>LONE</td></tr>
 * <tr><td>MANY_SUBSET</td>       <td>ALL</td>   <td>FIXED</td></tr>
 * <tr><td>MANY_SUBSET</td>       <td>ALL</td>   <td>MAJORITY_VOTE</td></tr>
 * <tr><td>MANY_SUBSET</td>       <td>ALL</td>   <td>RANDOM</td></tr>
 * <tr><td>MANY_ALL</td>          <td>ANY</td>   <td>LONE</td></tr>
 * <tr><td>MANY_ALL</td>          <td>FIRST</td> <td>LONE</td></tr>
 * <tr><td>MANY_ALL</td>          <td>ALL</td>   <td>FIXED</td></tr>
 * <tr><td>MANY_ALL</td>          <td>ALL</td>   <td>MAJORITY_VOTE</td></tr>
 * <tr><td>MANY_ALL</td>          <td>ALL</td>    <td>RANDOM</td></tr>
 * </table>
 * 
 * <p><strong>Invariant</strong></p>
 * 
 * <pre>
 * invariant		true
 * </pre>
 * 
 * <p>Created on : 2020-02-28</p>
 * 
 * @author	<a href="mailto:Jacques.Malenfant@lip6.fr">Jacques Malenfant</a>
 */
public class			CVM_NonBlocking
extends AbstractCVM
{
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

	protected final SelectorType	currentSelector = SelectorType.MANY_ALL ;
	protected final int				fixedIndex = 0 ;
	protected final CallMode		currentCallMode = CallMode.ALL ;
	protected final CombinatorType	currentCombinator = CombinatorType.MAJORITY_VOTE ;

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

	public				CVM_NonBlocking() throws Exception
	{
		assert		this.currentSelector == SelectorType.SINGLE_ROUND_ROBIN &&
					this.currentCallMode == CallMode.SINGLE &&
					this.currentCombinator == CombinatorType.LONE
				||	this.currentSelector == SelectorType.SINGLE_RANDOM &&
					this.currentCallMode == CallMode.SINGLE &&
					this.currentCombinator == CombinatorType.LONE
				||	this.currentSelector == SelectorType.MANY_SUBSET &&
					this.currentCallMode == CallMode.ANY &&
					this.currentCombinator == CombinatorType.LONE
				||	this.currentSelector == SelectorType.MANY_SUBSET &&
					this.currentCallMode == CallMode.FIRST &&
					this.currentCombinator == CombinatorType.LONE
				||	this.currentSelector == SelectorType.MANY_SUBSET &&
					this.currentCallMode == CallMode.ALL &&
					this.currentCombinator == CombinatorType.FIXED
				||	this.currentSelector == SelectorType.MANY_SUBSET &&
					this.currentCallMode == CallMode.ALL &&
					this.currentCombinator == CombinatorType.MAJORITY_VOTE
				||	this.currentSelector == SelectorType.MANY_SUBSET &&
					this.currentCallMode == CallMode.ALL &&
					this.currentCombinator == CombinatorType.RANDOM
				||	this.currentSelector == SelectorType.MANY_ALL &&
					this.currentCallMode == CallMode.ANY &&
					this.currentCombinator == CombinatorType.LONE
				||	this.currentSelector == SelectorType.MANY_ALL &&
					this.currentCallMode == CallMode.FIRST &&
					this.currentCombinator == CombinatorType.LONE
				||	this.currentSelector == SelectorType.MANY_ALL &&
					this.currentCallMode == CallMode.ALL &&
					this.currentCombinator == CombinatorType.FIXED
				||	this.currentSelector == SelectorType.MANY_ALL &&
					this.currentCallMode == CallMode.ALL &&
					this.currentCombinator == CombinatorType.MAJORITY_VOTE
				||	this.currentSelector == SelectorType.MANY_ALL &&
					this.currentCallMode == CallMode.ALL &&
					this.currentCombinator == CombinatorType.RANDOM ;
	}



	private void deployProject() throws Exception {



	}
	/**
	 * @see AbstractCVM#deploy()
	 */
	@Override
	public void			deploy() throws Exception
	{



		for (int i = 0 ; i < SERVER_BROKER_INBOUND_PORT_URIS.length ; i++) {
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
		}

		this.subscriberTestUri = AbstractComponent.createComponent(
				SubscriberTest.class.getCanonicalName(),
				new Object[]{SUBSCRIBER_CUBA_COMPONENT_URI,BROKER_COMPONENT_URIs[0]});

		assert this.isDeployedComponent(this.subscriberTestUri);
		this.toggleTracing(this.subscriberTestUri);
		this.toggleLogging(this.subscriberTestUri);

		// Publisher France
		this.publisherTestURI = AbstractComponent.createComponent(
				PublisherTest.class.getCanonicalName(),
				new Object[]{PUBLISHER_FRANCE_COMPONENT_URI,BROKER_COMPONENT_URIs[1]});
		assert this.isDeployedComponent(this.publisherTestURI);
		this.toggleTracing(this.publisherTestURI);
		this.toggleLogging(this.publisherTestURI);
		this.createReplicationManager() ;
		super.deploy() ;
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
			CVM_NonBlocking cvm = new CVM_NonBlocking() ;
			cvm.startStandardLifeCycle(100000L) ;
			Thread.sleep(100000L) ;
			System.exit(0) ;
		} catch (Exception e) {
			throw new RuntimeException(e) ;
		}
	}
}
// -----------------------------------------------------------------------------
