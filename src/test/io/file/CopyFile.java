/**
 * Run program like this:
 *  java test.io.file.CopyFile F:\copy\a\RException.java F:\copy\b\RException.java
 * **/

package test.io.file;

import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class CopyFile {
	
	// Two File object arguments needed when calling `copy` method 
	public boolean copy(File src, File dst) {
		// try-with-resource requires Java 7 
		try(InputStream is = new FileInputStream(src);
				OutputStream os = new FileOutputStream(dst)) {
			byte[] buffer = new byte[1024];
			int length;

			while ((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}

		} catch (IOException e) {
			throw new FileCopyException("File Copy Failed", e);
		} catch(Exception e){
			System.out.println("File copy failed for unknown reason");
			e.printStackTrace();
			return false;
		}
		
		System.out.println("File Copy Success.");
		return true;
	}

	public static void main(String[] args) {
		//Test Code: Check Arguments
//		if (args.length > 0) {
//			System.out.println("args: " + args.length);
//			for (int i = 0; i < args.length; i++) {
//				System.out.println("arg" + (i + 1) + ": " + args[i]);
//			}
//		}
		
		try{
			// Check the number of arguments input
			if(args.length != 2)
				throw new FileCopyException("Please input two arguments.");
			
			// double back slashes(Escape) aren't necessary
//			args[0] = args[0].replace("\\", "\\\\");
//			System.out.println("Source: " + args[0]);
//			args[1] = args[1].replace("\\", "\\\\");
//			System.out.println("Destination: " + args[1]);			

			File src = new File(args[0]);
			File dst = new File(args[1]);
			// Check if source file exists
            if(!src.exists())
            	throw new FileCopyException("Source file not found.");
            // Check if destination file is directory
            if(dst.isDirectory())
            	throw new FileCopyException("Destination isn't a file.");
			
            CopyFile cf = new CopyFile();
			cf.copy(src, dst);
            
		}catch(FileCopyException e){
			// print error message
			System.out.print("Error: ");
			e.printMsg();
			//e.printStackTrace();
		}catch(Exception e){
			System.out.println("Failed for some unknown reasons");
			e.printStackTrace();
		}
		
	}

}
