package transit;

public class StopExistsException extends Exception {

	public StopExistsException() {
	}

	public StopExistsException(String detailMessage) {
		super(detailMessage);
	}

	public StopExistsException(Throwable throwable) {
		super(throwable);
	}

	public StopExistsException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

}
