package app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import entities.Student;

public class Client {
	private static final Scanner SC = new Scanner(System.in);

	public static void main(String[] args) throws UnknownHostException, IOException {
		
		// Create a client socket and connect to the server
		try (Socket socket = new Socket("192.168.175.193", 9999)) {
			ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
			ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

			try {
				
				System.out.println("Get student by id");
				out.writeUTF("getStudentById&1");
				out.flush();
				
				Student student = (Student)in.readObject();
				
				System.out.println("Received from server: " + student);
				
				System.out.println("Get student by id");
				out.writeUTF("getStudentById&2");
				out.flush();
				
				Student student2 = (Student)in.readObject();
				System.out.println("Received from server 2: " + student2);
			} catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				out.close();
				in.close();
			}
		}
	}

}
