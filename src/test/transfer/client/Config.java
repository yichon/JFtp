package test.transfer.client;

public class Config {
	public String server_addr = "192.168.1.36";
	public int syn_port = 22021;
	public int trans_port = 22020;
	public String dst_dir = "/home/birdonwire/Others/TradeFile/";

	
	private static Config instance = new Config();
	
	private Config(){}
	
	public static Config getInstance(){
		return instance;
	}
	
}
