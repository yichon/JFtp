package test.transfer.server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import test.transfer.common.*;

public class FileManager {
	private File src_dir;
	private File[] srcfiles;
	// 
	private Map<String, ArrayList<File>> filesToBeSent;
	
	private static FileManager instance = new FileManager();
	
	private FileManager(){
		filesToBeSent = new HashMap<>();
		src_dir = new File(Config.getInstance().src_dir);
		// if file directory doesn't exist, set current directory as file directory
		if((srcfiles = src_dir.listFiles()) == null){
			src_dir = new File(".");
			srcfiles = src_dir.listFiles();
		}
		//throw new FTException("File Directory doesn't exist.");
	}
	
	public static FileManager getInstance(){
//		if(instance == null){
//			synchronized(FileManager.class){
//				if(instance == null)
//					instance = new FileManager();
//			}
//		}
		
		return instance;
	}
	
	// check if a file exists in an file ArrayList list and return its index
	public int check(String filename, ArrayList<File> filelist) {
		if (filename != null && filelist != null && filelist.size() > 0) {
			for (int i = 0; i < filelist.size(); i++) {
				if (filename.equals(filelist.get(i).getName())) {
                     return i;
				}
			}
		}
		return -1;
	}
		
	// get source files
	public File[] getSrcFiles(){
		return srcfiles;
	}

	// print all source files
	public String printSrcFileList(){
		StringBuilder sb = new StringBuilder("");
		String s = null;
		for (File f : srcfiles) {
			s = f.getName() + " : " + (f.length()) + " Bytes\n";
			sb.append(s);
			System.out.print(s);
		}
		
		return sb.toString();
	}
	
	// reload source files
	public synchronized void reload() {
		srcfiles = src_dir.listFiles();
	}

	// check if a file exists in an file Array and return its index
	public int check(String filename, File[] filelist) {
		if (filename != null && filelist != null && filelist.length > 0) {
			for (int i = 0; i < filelist.length; i++) {
				if (filename.equals(filelist[i].getName())) {
                     return i;
				}
			}
		}
		return -1;
	}
	// 
	public int inSrcFiles(String filename){
		return check(filename, srcfiles);
	}

	// add files to the transfer file list
	public synchronized boolean addSentFile(String file, String host) {
		ArrayList<File> alist = null;
		int index = -1;
		
		if((index = FileManager.getInstance().inSrcFiles(file)) >= 0){ //System.out.println("File Index: " + index);
			if ((alist = filesToBeSent.get(host)) == null) {
				alist = new ArrayList<File>();
				filesToBeSent.put(host, alist);
			} 
			
			if ((check(file, alist)) < 0) {
				alist.add(FileManager.getInstance().getSrcFiles()[index]);
				return true;
			}
		}
		return false;
	}

	// get the sent files of some host & remove it from the sent list
	public synchronized ArrayList<File> getSentFile(String host) {
		ArrayList<File> list = null;
		list = filesToBeSent.get(host);
		filesToBeSent.remove(host);
		return list;
	}
	
	// convert the source file list to a JSON string
	public String toJSON(){
		String s = null;
		SimFile sf;
		SimFileList sfl = new SimFileList();
		ArrayList<SimFile> a = new ArrayList<SimFile>();
		for (File f : srcfiles) {
			sf = new SimFile();
			sf.name = f.getName();
			sf.length = f.length();
			a.add(sf);
		}
		SimFile[] ty = new SimFile[a.size()];
		sfl.list = a.toArray(ty);
		
		ObjectMapper om = new ObjectMapper();
		try{
			s = om.writeValueAsString(sfl);
			//s = om.writerWithDefaultPrettyPrinter().writeValueAsString(sfl);

		}catch(Exception e){
			e.printStackTrace();
		}
		
		return s;
	}

	// construct an SimFileList object from a JSON string
	public SimFileList parseJSON(String s){
		SimFileList sfl = null;
		ObjectMapper om = new ObjectMapper();
		try{
			sfl = om.readValue(s, SimFileList.class);
			for(SimFile sf : sfl.list){
				System.out.println(sf.name + " : " + sf.length);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return sfl;
	}
	
//	public static void main(String[] args) {
//		String s = null;
//		System.out.println(s = FileManager.getInstance().toJSON());
//		FileManager.getInstance().parseJSON(s);
//		FileManager.getInstance().printSrcFileList();
//	}

}
