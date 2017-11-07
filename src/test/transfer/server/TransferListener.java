package test.transfer.server;

import java.net.ServerSocket;
import java.net.Socket;

public class TransferListener implements Runnable {
	private int trans_port = Config.getInstance().trans_port;
	
	@Override
	public void run() {
		
		System.out.println("TransferListener Start Running...");
		Socket cs = null;

		try (ServerSocket ss = new ServerSocket(trans_port);) {
			while (FTServer.running) {
				cs = ss.accept();
				new Thread(new TransferWorker(cs)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}


	}

}
