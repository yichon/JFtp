package test.transfer.server;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStreamWriter;
import java.io.BufferedInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;


public class Config {
	public String src_dir = ".";
	public int syn_port  = 20021;
	public int trans_port  = 20020;
	
	private static Config instance = new Config();
	
	private Config(){
		try{
			load();
		}catch(FTException e){
			e.printMsg();
		}
	}
	
	public static Config getInstance(){
//		if(instance == null){
//			synchronized(Config.class){
//				if(instance == null)
//					instance = new Config();
//			}
//		}		
		return instance;
	}
		
	private static class ConfigData{
		public String src_dir;
		public int syn_port;
		public int trans_port;
		
	}
	
	private ConfigData ecapData(){
		ConfigData d = new ConfigData();
		d.src_dir = src_dir;
		d.syn_port = syn_port;
		d.trans_port = trans_port;

		return d;
	}
	
	public void printData(){
		System.out.println("src_dir: " + src_dir);
		System.out.println("syn_port: " + syn_port);
		System.out.println("trans_port: " + trans_port);		
	}
	
	public void load(){
		ObjectMapper om = new ObjectMapper();
		try(FileInputStream fis = new FileInputStream("./config/config.json");
				BufferedInputStream bis = new BufferedInputStream(fis)){

			ConfigData d = om.readValue(bis, ConfigData.class);
			
			src_dir = d.src_dir;
			syn_port = d.syn_port;
			trans_port = d.trans_port;

			System.out.println(om.writeValueAsString(d));
			System.out.println("¡ü¡ü¡ü Config Information Loaded ¡ü¡ü¡ü");
			
		}catch(Exception e){
			throw new FTException("Loading config information failed.", e);
			//e.printStackTrace();
		}
	}
	
	public void save(){
		ConfigData d = ecapData();
		ObjectMapper om = new ObjectMapper();
		try(FileOutputStream fos = new FileOutputStream("./config/config.json");
				OutputStreamWriter osr = new OutputStreamWriter(fos)){
			String s = om.writerWithDefaultPrettyPrinter().writeValueAsString(d);
			System.out.println(s);
			osr.write(s);
		}catch(Exception e){
			e.printStackTrace();
		}

	}
		
	public static void main(String[] args){		
		Config.getInstance().printData();
		//save();
	}
}
