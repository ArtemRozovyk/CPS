package components;

import java.util.concurrent.TimeUnit;

import fr.sorbonne_u.components.AbstractComponent;
import fr.sorbonne_u.components.cvm.AbstractCVM;
import interfaces.MessageI;
import message.Message;
import ports.PublisherManagementOutboundPort;
import ports.PublisherPublicationOutboundPort;

public class Publisher extends AbstractComponent{
	
	protected PublisherPublicationOutboundPort ppop;
	protected PublisherManagementOutboundPort pmop;
	
	public final static String	PUBLISHER_CLIENT_PUBLICATION_PLUGIN =
			"publisher-publication-plugin-uri" ;
	
	protected Publisher(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}

	protected Publisher(String uri,
			String publicationOutboundPortURI,
						String managementOutboundPort) throws Exception
	{
		super(uri, 0, 1) ;
		
		//Publish the reception port (an outbound port is always local)
		this.ppop = new PublisherPublicationOutboundPort(publicationOutboundPortURI, this);
		this.ppop.localPublishPort();
		
		this.pmop = new PublisherManagementOutboundPort(managementOutboundPort, this);
		this.pmop.localPublishPort();
		
		// Install the plugin
		/*PublisherPublicationClientPlugin pluginPublication = new PublisherPublicationClientPlugin();
		pluginPublication.setPluginURI(PUBLISHER_CLIENT_PUBLICATION_PLUGIN);
		this.installPlugin(pluginPublication);*/
		
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
		String topic = "weather0";
		String msg = "110 degrees in Denver";
		for (int i =0; i <10;i ++) {
			if(i>4){
				topic="weather1";
				msg = "150 degrees in Acapulco";
			}
			logMessage("Publishing message "+i);
			publish(new Message(msg), topic);
		}
	}
	
	public void publish(MessageI m, String topic) throws Exception {
			this.scheduleTask(new AbstractComponent.AbstractTask() {
				@Override
				public void run() {
					try {
						((Publisher)
								this.getTaskOwner()).ppop.publish(m,topic);
					} catch (Exception e) {
						throw new RuntimeException(e) ;
					}
				}
			}, 1000, TimeUnit.MILLISECONDS) ;
	}
	
	public void publish(MessageI[] ms, String topic) throws Exception {
		this.scheduleTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {

					((Publisher)
							this.getTaskOwner()).ppop.publish(ms,topic);
				} catch (Exception e) {
					throw new RuntimeException(e) ;
				}
			}
		}, 1000, TimeUnit.MILLISECONDS) ;
	}
	
	public void publish(MessageI m, String[] topics) throws Exception {
		this.scheduleTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {
					((Publisher)
							this.getTaskOwner()).ppop.publish(m,topics);
				} catch (Exception e) {
					throw new RuntimeException(e) ;
				}
			}
		}, 1000, TimeUnit.MILLISECONDS) ;
	}
	
	public void publish(MessageI[] ms, String[] topics) throws Exception {

		this.scheduleTask(new AbstractComponent.AbstractTask() {
			@Override
			public void run() {
				try {

					((Publisher)
							this.getTaskOwner()).ppop.publish(ms,topics);
				} catch (Exception e) {
					throw new RuntimeException(e) ;
				}
			}
		}, 1000, TimeUnit.MILLISECONDS) ;
	}


}
