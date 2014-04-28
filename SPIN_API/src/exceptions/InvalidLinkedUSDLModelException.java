package exceptions;

public class InvalidLinkedUSDLModelException extends Exception{
	
	private ErrorEnum errorCode;
	private String[] errorArguments;
	
	public InvalidLinkedUSDLModelException(ErrorEnum error){
		super(error.getMessage());
		errorCode = error;
	}
	
	public InvalidLinkedUSDLModelException(ErrorEnum error, String... args){
		super(error.getMessage(args));
		errorCode = error;
		errorArguments = args;
	}

	public InvalidLinkedUSDLModelException(ErrorEnum error, Throwable cause, String... args){
		super(error.getMessage(args), cause);
		errorCode = error;
		errorArguments = args;
	}	

}
