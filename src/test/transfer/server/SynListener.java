package test.transfer.server;

import java.net.ServerSocket;
import java.net.Socket;

public class SynListener implements Runnable {
	private int syn_port = Config.getInstance().syn_port;
	private TransferListener tfl = null;

	public SynListener() {
		
	}

	@Override
	public void run() {
		System.out.println("SynListener Start Running...");
		// start the Transfer Listener
		tfl = new TransferListener();
		new Thread(tfl).start();
		
		Socket cs = null;
		try (ServerSocket ss = new ServerSocket(syn_port);) {
			while (FTServer.running) {
				cs = ss.accept();
				if (cs != null)
					new Thread(new SynWorker(cs)).start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}

	}

	public static void main(String[] args) {
		// new Thread(new SynListener()).start();
//		System.out.println(Config.src_dir);
//		System.out.println(Config.syn_port);
//		System.out.println(Config.trans_port);
	}

}
