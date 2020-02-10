package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.annotations.AddPlugin;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import fr.sorbonne_u.components.examples.basic_cs.components.URIConsumer;
import fr.sorbonne_u.components.plugins.dconnection.example.components.ServerSideExample;
import interfaces.MessageI;
import message.Message;
import ports.PublisherManagementOutboundPort;
import ports.PublisherPublicationOutboundPort;
@AddPlugin(pluginClass = ServerSideExample.ServerSidePlugin.class,
pluginURI = ServerSideExample.DYNAMIC_CONNECTION_PLUGIN_URI)
public class Publisher extends AbstractComponent{
	
	protected PublisherPublicationOutboundPort ppop;
	
	protected PublisherManagementOutboundPort pmop;
	
	//protected String publisherPublicationOutboundPortURI;

	protected Publisher(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
		// TODO Auto-generated constructor stub
	}

	protected Publisher(String uri,
			String publicationOutboundPortURI) throws Exception
	{
		super(uri, 0, 1) ;
		
		//Publish the reception port (an outbound port is always local)
		this.ppop = new PublisherPublicationOutboundPort(publicationOutboundPortURI, this);
		this.ppop.localPublishPort();
		
		this.pmop = new PublisherManagementOutboundPort(publicationOutboundPortURI, this);
		this.pmop.localPublishPort();
		
		if (AbstractCVM.isDistributed) {
			this.executionLog.setDirectory(System.getProperty("user.dir")) ;
		} else {
			this.executionLog.setDirectory(System.getProperty("user.home")) ;
		}
		
		this.tracer.setTitle("publisher") ;
		this.tracer.setRelativePosition(1, 0) ;
	}
	
	
	@Override
	public void execute() throws Exception{
		publish(new Message("Hello bro"), "nothing");
	}
	
	public void publish(MessageI m, String topic) throws Exception {
		logMessage("Publishing message "+m);
		for (int i =0; i <5;i ++) {
			this.scheduleTask(new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						((Publisher)this.getTaskOwner()).publish(m, topic);							
					} catch (Exception e) {
						throw new RuntimeException(e) ;
					}
				}
			}, 1000, TimeUnit.MILLISECONDS) ;
		}
		ppop.publish(m, topic);

	}

}
