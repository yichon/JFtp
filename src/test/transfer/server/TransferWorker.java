package test.transfer.server;

import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.File;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class TransferWorker implements Runnable {
	private Socket cs = null;
	private String clientIP = null;
	private String src_dir = ".";
	private String src_file = "source";
	
	public TransferWorker(Socket s){
		cs = s;
		if(cs != null)
			clientIP = ((InetSocketAddress) cs.getRemoteSocketAddress()).getAddress().getHostAddress();
		
		src_dir = Config.getInstance().src_dir;
		
	}

	@Override
	public void run() {
		ArrayList<File> sa = FileManager.getInstance().getSentFile(clientIP);
		
		System.out.println("Remote Address: " + clientIP);

		
		if(sa != null && sa.size() > 0){
			try(OutputStream os = cs.getOutputStream()){
				byte[] buffer = new byte[1024];
				int length;

				for(File f : sa){
					if(f != null){
						try(InputStream is = new FileInputStream(src_dir + "/" + f.getName())){
							while ((length = is.read(buffer)) > 0) {
								os.write(buffer, 0, length);
							}
							os.flush();
							System.out.println(f.getName() + " Sent.");
						}catch(Exception e){
							System.out.println(f.getName() + " loading failed: [Message] :" + e.getMessage());
						}
					}
				}
			}catch (Exception e) {
				throw new FTException("File transfer failed", e);
			}finally {
				// close the client Socket started by ServerSocket
				if (cs != null) {
					try {
						cs.close();
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				//System.out.println("Transfer Server Closed.");
			}
		}
		
	}

}
