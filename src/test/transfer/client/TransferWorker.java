package test.transfer.client;

import java.net.Socket;
import java.io.FileOutputStream;
import java.io.InputStream;

import test.transfer.common.*;


public class TransferWorker implements Runnable {
	private FileManager fm = null;
	private String trans_addr = "0.0.0.0";
	private int trans_port = 20020;
	private String dst_dir = ".";
	private String dst_file = "target";
	
	public TransferWorker(){
		fm = FileManager.getInstance();
		
		trans_port = Config.getInstance().trans_port;
		trans_addr = Config.getInstance().server_addr;
		dst_dir = Config.getInstance().dst_dir;
	}
	
	@Override
	public void run() {
		System.out.println("\nTransferWorker Start Running...");
		if (fm.revfiles != null && fm.revfiles.list != null){
			try(Socket cs = new Socket(trans_addr, trans_port);
					InputStream is = cs.getInputStream();){
				System.out.println("Server " + trans_addr + ":" + trans_port + " connected.");
				byte[] buffer = new byte[1024], ibuf;
				int len = 0, off = 0;
				long filesize, acc;
				
				for(SimFile sf : fm.revfiles.list){
					if(sf != null){
						try(FileOutputStream fo = new FileOutputStream(dst_dir + sf.name);){
							filesize = sf.length;
							acc = 0;
							
							while(filesize > 0){
								
								if(filesize >= 1024){
									ibuf = buffer;
								}else{
									ibuf = new byte[(int) filesize];
								}
								len = is.read(ibuf);
								fo.write(ibuf, 0, len);
								filesize = filesize - len;

							}
							
//							if(off != 0 && off < len){
//								fo.write(buffer, off, len);
//								acc = acc + (len - off);
//							}
//							while((len = is.read(buffer)) > 0){
//								if(acc + len > filesize){
//									
//								}
//								fo.write(buffer, 0, len);
//							}

														
							System.out.println(sf.name + " Received.");
						}catch(Exception e){
							System.out.println("File Transfer Failed.");
							e.printStackTrace();
						}
					}

				}		

			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
//		try(Socket cs = new Socket(trans_addr, trans_port);
//				FileOutputStream fo = new FileOutputStream(dst_dir + dst_file);
//				InputStream is = cs.getInputStream();){
//			
//			byte[] buffer = new byte[1024];
//			int length;
//			while((length = is.read(buffer)) > 0){
//				fo.write(buffer, 0, length);
//			}
//			
//			System.out.println("File Trade Success.");
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
	    System.out.println("\nTransferWorker Closed.");
	}

//	public static void main(String[] args) {
//		new Thread(new TransferWorker("target")).start();
//
//	}

}
