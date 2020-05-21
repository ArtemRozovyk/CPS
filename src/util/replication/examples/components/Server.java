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

import util.replication.interfaces.*;
import util.replication.ports.*;
import fr.sorbonne_u.components.*;
import fr.sorbonne_u.components.annotations.*;
import fr.sorbonne_u.components.exceptions.*;

// -----------------------------------------------------------------------------

/**
 * The class <code>Server</code> implements a server component that
 * returns a customized result for any given parameter.
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
@OfferedInterfaces(offered = {ReplicableCI.class})
// -----------------------------------------------------------------------------
public class			Server
extends AbstractComponent
implements	ReplicaI<String>
{
	/**	a relative position for the trace window of the component.			*/
	protected int							position ;
	/** URI of the reflection inbound port of this component.				*/
	protected String						refl_ipURI = "generated-" ;
	/** URI of the inbound port used to offer the service.					*/
	protected ReplicableInboundPort<String>	rip ;

	/**
	 * create a server.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code inboundPortURI != null && position >= 0}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param inboundPortURI	URI of the inbound port used to offer the service.
	 * @param position			a relative position for the trace window of the component.
	 * @throws Exception		<i>to do</i>.
	 */
	protected			Server(String inboundPortURI, int position)
	throws Exception
	{
		super(1, 0) ;
		assert	inboundPortURI != null && position >= 0 ;
		this.initialise(inboundPortURI, position) ;
	}

	/**
	 * create a server.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code inboundPortURI != null && position >= 0}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param reflectionInboundPortURI	URI of the reflection inbound port of this component.
	 * @param inboundPortURI			URI of the inbound port used to offer the service.
	 * @param position					a relative position for the trace window of the component.
	 * @throws Exception				<i>to do</i>.
	 */
	protected			Server(
		String reflectionInboundPortURI,
		String inboundPortURI,
		int position
		) throws Exception
	{
		super(reflectionInboundPortURI, 1, 0) ;
		this.refl_ipURI = reflectionInboundPortURI ;
		this.initialise(inboundPortURI, position) ;
	}

	/**
	 * initialise the component.
	 * 
	 * <p><strong>Contract</strong></p>
	 * 
	 * <pre>
	 * pre	{@code inboundPortURI != null && position >= 0}
	 * post	true			// no postcondition.
	 * </pre>
	 *
	 * @param inboundPortURI			URI of the inbound port used to offer the service.
	 * @param position					a relative position for the trace window of the component.
	 * @throws Exception				<i>to do</i>.
	 */
	protected void		initialise(String inboundPortURI, int position)
	throws Exception
	{
		assert	position >= 0 ;
		this.createReplicableInboundPort(inboundPortURI) ;
		this.tracer.setTitle(refl_ipURI) ;
		this.tracer.setRelativePosition(2, position) ;
		this.toggleTracing() ;
	}

	protected void		createReplicableInboundPort(String inboundPortURI)
	throws Exception
	{
		this.rip = new ReplicableInboundPort<String>(inboundPortURI, this) ;
		this.rip.publishPort() ;
		
	}

	/**
	 * @see util.replication.interfaces.ReplicaI#call(Object[])
	 */
	@Override
	public String		call(Object... parameters) throws Exception
	{
		String ret = this.refl_ipURI + parameters[0] ;
		this.traceMessage(ret + "\n") ;
		Thread.sleep(5L) ;
		return ret ;
	}

	/**
	 * @see AbstractComponent#shutdown()
	 */
	@Override
	public void			shutdown() throws ComponentShutdownException
	{
		try {
			this.rip.unpublishPort() ;
		} catch (Exception e) {
			throw new ComponentShutdownException(e) ;
		}
		super.shutdown();
	}
}
// -----------------------------------------------------------------------------
