package test;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.ServerSocket;


public class ServerTest {
	   ServerSocket server;
	   int serverPort = 8888;
	   
	   // Constructor to allocate a ServerSocket listening at the given port.
	   public ServerTest() {
	      try {
	         server = new ServerSocket(serverPort);
	         System.out.println("ServerSocket: " + server);
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }
	   
	   // Start listening.
	   private void listen() {
	      while (true) { // run until you terminate the program
	         try {
	            // Wait for connection. Block until a connection is made.
	            Socket socket = server.accept();
	            System.out.println("Socket: " + socket);
	            InputStream in = socket.getInputStream();
	            int byteRead;
	            // Block until the client closes the connection (i.e., read() returns -1)
	            while ((byteRead = in.read()) != -1) {
	               System.out.print((char)byteRead);
	            }
	            System.out.println("Conversation End!");
	         } catch (IOException e) {
	            e.printStackTrace();
	         }
	      }
	   }
	
	public static void main(String[] args) {
		ServerTest st = new ServerTest();
		st.listen();
	}

}
