package test.transfer.client;


import test.transfer.common.*;

public class FileManager {
	
	public SimFileList allfiles = null;
	public SimFileList revfiles = null;

	
	private static FileManager instance = new FileManager();
	
	private FileManager(){}
	
	public static FileManager getInstance(){
		return instance;
	}
	
	public SimFile getSimFile(String name){
		if(allfiles != null){
			for(SimFile sf : allfiles.list){
				if(sf.name.equals(name))
					return sf;
			}
		}
		return null;
	}
	
	public void addRevFiles(SimFile[] sfa){
		if(revfiles == null){
			revfiles = new SimFileList();
		}
		revfiles.list = sfa;
	}
	
}
