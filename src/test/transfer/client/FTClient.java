package test.transfer.client;

public class FTClient implements Runnable {
	public String server_addr = "192.168.1.36";
	@Override
	public void run() {
		new Thread(new SynTalker()).start();

	}

	public static void main(String[] args) {
		new Thread(new FTClient()).start();
		
	}

}
