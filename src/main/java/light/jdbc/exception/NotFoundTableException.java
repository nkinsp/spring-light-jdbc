package light.jdbc.exception;

public class NotFoundTableException extends RuntimeException{
	
	private static final long serialVersionUID = -4835244772356021707L;

	public NotFoundTableException(String message) {
		super(message);
	}
	
	

}
