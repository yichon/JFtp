package test;
import java.net.URL;
import java.net.URLConnection;
import java.io.InputStream;

public class UrlTest {
	private URL url = null;
	private URLConnection uc = null;
	public void setUrl(URL url) {
		this.url = url;
	}
	
	public UrlTest() {
		try{
			url = new URL("http://wapmail.10086.cn/login3.htm");
			uc = url.openConnection();
			
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void startTest(){
		try(InputStream is = uc.getInputStream()){
			int data;
			while((data = is.read()) != -1){
			    System.out.print((char) data);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		try{
			UrlTest my = new UrlTest();
			my.startTest();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

}
