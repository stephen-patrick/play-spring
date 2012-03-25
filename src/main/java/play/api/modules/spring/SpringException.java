package play.api.modules.spring;


/**
 * TODO extend PlayException
 * 
 * @author stephen
 *
 */
public class SpringException extends RuntimeException {
	
	
	public SpringException(String message) {
		this(message, null);
	}
	
	public SpringException(String message,  Throwable cause) {
		super(message, cause);
	}
	
    
}