package app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dao.EntityManagerFactoryUtil;
import dao.StudentDAO;
import entities.Student;
import jakarta.persistence.EntityManager;

public class Server {

	public static void main(String[] args) throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(9999)) {
			// Create a thread pool to handle multiple clients
			ExecutorService executorService = Executors.newCachedThreadPool();

			// Start accepting client connections
			System.out.println("Server listening on port 9999...");

			while (true) {
				// Accept a client connection
				Socket clientSocket = serverSocket.accept();
				System.out.println("Client connected: " + clientSocket.getInetAddress().getHostAddress());

				// Create a new thread for the client and submit it to the thread pool
				ClientHandler clientHandler = new ClientHandler(clientSocket);
				executorService.execute(clientHandler);
			}
		}
	}
	
	private static class ClientHandler implements Runnable {
		private Socket clientSocket;
		private ObjectInputStream in;
		private ObjectOutputStream out;
		private EntityManagerFactoryUtil mangerFactoryUtil;
		private EntityManager entityManager;

		public ClientHandler(Socket clientSocket) {
			super();
			this.clientSocket = clientSocket;
			this.mangerFactoryUtil = new EntityManagerFactoryUtil();
			this.entityManager = mangerFactoryUtil.getEnManager();
		}

		@Override
		public void run() {
			try {

				// Create input and output streams for data transmission
				in = new ObjectInputStream(clientSocket.getInputStream());
				out = new ObjectOutputStream(clientSocket.getOutputStream());

				while (true) {
		
					String clientData = in.readUTF();
					System.out.println("Recive from client: " + clientData);
					String[] items = clientData.split("&", 2);

					if (items.length == 0) {
						System.out.println("Missing request from Client");
						return;
					}

					switch (items[0]) {
						case "getStudentById":
							StudentDAO studentDAO = new StudentDAO(entityManager);
							Student student = studentDAO.findById(Long.parseLong(items[1]));
							System.out.println(Long.parseLong(items[1]));
							System.out.println(student);
							out.writeObject(student);
							out.flush();
							break;
						case "getList":
							
							break;
						default:
							break;
					}

				}

			} catch (IOException e) {
//				e.printStackTrace();
			} 
			finally {
				try {
					in.close();
					out.close();
					
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				mangerFactoryUtil.closeEnManager();
				mangerFactoryUtil.closeEnManagerFactory();
			}
		}
	}
}


