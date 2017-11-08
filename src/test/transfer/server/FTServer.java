package test.transfer.server;
// Server
public class FTServer implements Runnable {
	public static volatile boolean running = true;
	public Config config;
	public FileManager filemanager;
	
	public SynListener sl;
	
	public FTServer(){
		config = Config.getInstance();
		filemanager = FileManager.getInstance();
		
		sl = new SynListener();
	}
	
	@Override
	public void run() {
		new Thread(sl).start();

	}

	public static void main(String[] args) {
		new Thread(new FTServer()).start();
		System.out.println("File Transfer Server Started.");
	}

}
