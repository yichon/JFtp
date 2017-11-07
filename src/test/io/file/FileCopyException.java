package test.io.file;

public class FileCopyException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7344992158590450356L;
	public String msg = null;

	public FileCopyException(String msg, Throwable cause) {
		super(cause);
		this.msg = msg;
	}
	public FileCopyException(String msg) {
		super(msg);
		this.msg = msg;
	}
	public void printMsg() {
		if (this.msg != null)
			System.out.println("" + msg);
	}

	
}
