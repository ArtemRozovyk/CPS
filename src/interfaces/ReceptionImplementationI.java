package interfaces;

public interface ReceptionImplementationI {
	
	void  acceptMessage(MessageI m) throws Exception;
	void  acceptMessage(MessageI[] ms) throws Exception;
}
