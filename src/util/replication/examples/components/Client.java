package util.replication.examples.components;

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

import util.replication.connectors.*;
import util.replication.interfaces.*;
import util.replication.ports.*;
import fr.sorbonne_u.components.*;
import fr.sorbonne_u.components.annotations.*;
import fr.sorbonne_u.components.exceptions.*;

import java.util.*;

// -----------------------------------------------------------------------------

/**
 * The class <code>Client</code> implements a client of a replication service.
 *
 * <p><strong>Description</strong></p>
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
// -----------------------------------------------------------------------------
@RequiredInterfaces(required = {ReplicableCI.class})
// -----------------------------------------------------------------------------
public class			Client
extends AbstractComponent
{
	/** number of calls to be made in the tests scenario.					*/
	public static int							NUMBER_OF_CALLS = 100 ;
	/** outbound port used to call the service.								*/
	protected ReplicableOutboundPort<String> 	rop ;
	/** URI of the inbound port proposed by the replication service.		*/
	protected String							replicableInboundPortURI ;
	/** a number used to distinguish the client in traces.					*/
	protected int								base ;

	/**
	 * create a client component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	replicableInboundPortURI != null
	 * pre	{@code base % 1000 == 0}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param replicableInboundPortURI	URI of the inbound port proposed by the replication service.
	 * @param base						a number used to distinguish the client in traces.
	 * @throws Exception				<i>to do</i>.
	 */
	protected			Client(String replicableInboundPortURI, int base)
	throws Exception
	{
		super(1, 0) ;
		this.initialise(replicableInboundPortURI, base) ;
	}

	/**
	 * create a client component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	replicableInboundPortURI != null
	 * pre	{@code base % 1000 == 0}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param reflectionInboundPortURI	URI of the reflection inbound port of this component.
	 * @param replicableInboundPortURI	URI of the inbound port proposed by the replication service.
	 * @param base						a number used to distinguish the client in traces.
	 * @throws Exception				<i>to do</i>.
	 */
	protected			Client(
		String reflectionInboundPortURI,
		String replicableInboundPortURI,
		int base
		) throws Exception
	{
		super(reflectionInboundPortURI, 1, 0) ;
		this.initialise(replicableInboundPortURI, base) ;
	}

	/**
	 * initialise the component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	replicableInboundPortURI != null
	 * pre	{@code base % 1000 == 0}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param replicableInboundPortURI	URI of the inbound port proposed by the replication service.
	 * @param base						a number used to distinguish the client in traces.
	 * @throws Exception				<i>to do</i>.
	 */
	protected void		initialise(String replicableInboundPortURI, int base)
	throws Exception
	{
		assert	base % 1000 == 0 ;

		this.base = base ;
		this.replicableInboundPortURI = replicableInboundPortURI ;
		this.rop = new ReplicableOutboundPort<String>(this) ;
		this.rop.publishPort() ;
		try {
			this.tracer.setTitle("Client") ;
			this.tracer.setRelativePosition(0, base/1000 - 1) ;
			this.toggleTracing() ;
		} catch(Exception e) {
			System.out.println("Exception levée = " + e);
		}
	}

	/**
	 * @see AbstractComponent#start()
	 */
	@Override
	public void			start() throws ComponentStartException
	{
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
	 * @see AbstractComponent#execute()
	 */
	@Override
	public void			execute() throws Exception
	{
		// Create a random number generator to generate some random delays
		// between requests to have a better test scenario compared to real
		// executions.
		Random rg = new Random(System.currentTimeMillis()) ;

		// This accumulator allows us to compute the average service time of
		// requests as seen from the client (including call/return time,
		// which could be a fair part of the service time for distributed
		// executions.
		long cumulativeDuration = 0L ;
		for (int i = 0 ; i < NUMBER_OF_CALLS ; i++) {
			try {
				// Waiting time between requests
				Thread.sleep(rg.nextInt(50)) ;
				// Start the timer.
				long startTime = System.currentTimeMillis() ;
				// Call
				String result = this.rop.call(i + this.base) ;
				// Compute the duration and add it to the accumulator
				long duration = System.currentTimeMillis() - startTime ;
				cumulativeDuration += duration ;
				// To see the result and the duration
				this.traceMessage(result + " " + duration + "\n") ;
			} catch(RuntimeException e) {
				this.traceMessage("exception thrown\n") ;
			}
		}
		// Compute and show the mean service time.
		this.traceMessage(
				"mean request processing time = " +
						((double)cumulativeDuration/(double)NUMBER_OF_CALLS)
																	+ "\n") ;
	}

	/**
	 * @see AbstractComponent#finalise()
	 */
	@Override
	public void			finalise() throws Exception
	{
		this.doPortDisconnection(this.rop.getPortURI()) ;
		super.finalise();
	}

	/**
	 * @see AbstractComponent#shutdown()
	 */
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		try {
			this.rop.unpublishPort() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown() ;
	}
}
// -----------------------------------------------------------------------------
