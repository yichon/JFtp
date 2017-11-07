package test.transfer.server;

import java.lang.StringBuilder;
import java.net.Socket;
import java.net.InetSocketAddress;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SynWorker implements Runnable {

	private Socket skt = null;
	private String clientIP = null;

	public SynWorker(Socket s) {
		skt = s;
		if(skt != null)
			clientIP = ((InetSocketAddress) skt.getRemoteSocketAddress()).getAddress().getHostAddress();

	}

	public String processRequest(String s){
		String r = "Error";
		if(s != null){
			if("ls".equals(s))
				r = FileManager.getInstance().toJSON();
			else{
				String[] sa = s.split(" ");
				if(sa != null && sa.length > 1 && "cp".equals(sa[0])){
					for(int i = 1; i < sa.length; i++){
						FileManager.getInstance().addSentFile(sa[i], clientIP);
						System.out.println(clientIP + " : <" + sa[i] + "> added");
					}
					r = "ok";
				}
			}
		}
		return r;
	}
	
	@Override
	public void run() {
		if (skt != null) {
			String clientIP = ((InetSocketAddress) skt.getRemoteSocketAddress()).getAddress().getHostAddress();
			System.out.println("SynWorker Start Running...connect to " + clientIP);
			try (InputStreamReader isr = new InputStreamReader(skt.getInputStream());
					OutputStreamWriter osw = new OutputStreamWriter(skt.getOutputStream())) {
				//
				int data;
				String s = null;
				StringBuilder sb = new StringBuilder();// System.out.println("Hello");
				while ((data = isr.read()) != -1) {
					// System.out.print((char) data);
					if ((char) data != '`')
						sb.append((char) data);
					else{
						System.out.print("Request -> `");
						System.out.println((s = sb.toString()) + "`");
						s = this.processRequest(s); //System.out.println("Hello: " + s);
						osw.write(s + '`');
                        osw.flush();
						sb = new StringBuilder();
					}
				}
				

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (skt != null) {
					try {
						skt.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println("SynWorker Closed.");
			}
		} else {
			System.out.println("Unknown Socket Error.");
		}

	}

}
