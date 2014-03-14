package exceptions;

public class InvalidLinkedUSDLModelException extends Exception{
	
	public InvalidLinkedUSDLModelException(){
		
	}
	
	public InvalidLinkedUSDLModelException(String message){
		super(message);
	}

	public InvalidLinkedUSDLModelException(String message, Throwable cause){
		super(message, cause);
	}	

}
