package exceptions;

public class ReadModelException extends Exception{
	
	public ReadModelException(){
		
	}
	
	public ReadModelException(String message){
		super(message);
	}

	public ReadModelException(String message, Throwable cause){
		super(message, cause);
	}	

}
