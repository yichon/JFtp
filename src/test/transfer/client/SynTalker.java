package test.transfer.client;

import java.lang.StringBuilder;
import java.net.Socket;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.ArrayList;

import test.transfer.common.*;

import com.fasterxml.jackson.databind.ObjectMapper;

public class SynTalker implements Runnable {
	private FileManager fm = null;
	private int syn_port = 22021;
	private String syn_addr = "0.0.0.0";

	public SynTalker() {
		fm = FileManager.getInstance();
		syn_port = Config.getInstance().syn_port;
		syn_addr = Config.getInstance().server_addr;
	}

	public String processResponse(String s) {
		if ("ok".equals(s) && fm.revfiles != null) {
			new Thread(new TransferWorker()).start();
			return s;
		}
		if ("Error".equals(s))
			return s;

		String r = "Process-Response-Error";

		ObjectMapper om = new ObjectMapper();
		try {
			fm.allfiles = om.readValue(s, SimFileList.class);
			if (fm.allfiles != null && fm.allfiles.list != null) {
				r = "";
				for (SimFile sf : fm.allfiles.list) {
					r = r + sf.name + " : " + sf.length + " bytes\n";
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return r;
	}

	public void processRequest(String input) {
		ArrayList<SimFile> sfal = new ArrayList<>();
		SimFile sf = null;
		if (input != null) {
			String[] sa = input.split(" ");
			if (sa.length > 1) {
				for (int i = 1; i < sa.length; i++) {
					if ((sf = fm.getSimFile(sa[i])) == null)
						throw new RuntimeException(
								"File doesn't exist in the server.");
					sfal.add(sf);
				}
				//System.out.println("num: " + sfal.size());
				if (sfal.size() > 0)
					fm.addRevFiles(sfal.toArray(new SimFile[sfal.size()]));
			}
		}

	}

	@Override
	public void run() {
		System.out.println("SynClient Start Running...");

		Scanner sin = new Scanner(System.in);
		String ostr = null, istr = null;
		char[] ibuffer = new char[1024];
		int len;
		StringBuilder sb;
		boolean isReady = false;

		try (Socket cs = new Socket(syn_addr, syn_port);
				OutputStreamWriter osw = new OutputStreamWriter(
						cs.getOutputStream());
				InputStreamReader isr = new InputStreamReader(
						cs.getInputStream());) {

			while (!isReady) {
				System.out.print("Input: ");
				ostr = sin.nextLine();
				processRequest(ostr);
				osw.write(ostr + '`');
				osw.flush();

				System.out.print("Request has been sent, waiting for Response...\n");

				sb = new StringBuilder();
				while ((len = isr.read(ibuffer)) != -1) {
					if (ibuffer[len - 1] != '`')
						sb.append(ibuffer, 0, len);
					else {
						sb.append(ibuffer, 0, len - 1);
						break;
					}
				}
				System.out.println("Response: \n"
						+ processResponse(sb.toString()));

				// int ch;
				// StringBuilder sb = new StringBuilder();
				// while((ch = isr.read()) != -1){
				// sb.append((char) ch);
				// }
				//
				// if(sb.toString().equals("ok")){
				// new Thread(new TransferWorker(str)).start();
				// isReady = true;
				// }

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.out.println("SynClient Closed");
		}

	}

	public static void main(String[] args) {

		new Thread(new SynTalker()).start();

	}

}
