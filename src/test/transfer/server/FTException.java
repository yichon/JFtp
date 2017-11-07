package test.transfer.server;

public class FTException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8170627698575884676L;
	public String msg = null;

	public FTException(String msg, Throwable cause) {
		super(cause);
		this.msg = msg;
	}
	public FTException(String msg) {
		super(msg);
		this.msg = msg;
	}
	public void printMsg() {
		if (this.msg != null)
			System.out.println("" + msg);
    }
	
}