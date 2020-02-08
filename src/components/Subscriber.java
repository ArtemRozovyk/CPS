package components;

import fr.sorbonne_u.components.AbstractComponent;
import interfaces.MessageI;

public class Subscriber extends AbstractComponent{

	public Subscriber(int nbThreads, int nbSchedulableThreads) {
		super(nbThreads, nbSchedulableThreads);
	}
	
	public void acceptMessage(MessageI m) throws Exception {
		System.out.print("WE GOT IT "+m);
	}

}
