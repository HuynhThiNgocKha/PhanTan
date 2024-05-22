package demo;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ServerUIApp extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton startServerButton;
	private JButton stopServerButton;
	private JButton sendBroadcastButton;
	private JTextArea logArea;
	private JTextField broadcastField;
	private ServerSocket serverSocket;
	private List<ClientHandler> clients = new CopyOnWriteArrayList<ServerUIApp.ClientHandler>();

	public ServerUIApp() {
		setTitle("Chat Server");
		logArea = new JTextArea(16, 50);
		logArea.setEditable(false);
		broadcastField = new JTextField(50);
		startServerButton = new JButton("Start Server");
		stopServerButton = new JButton("Stop Server");
		stopServerButton.setEnabled(false);
		sendBroadcastButton = new JButton("Send Broadcast");

		startServerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startServer();
			}
		});

		stopServerButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stopServer();
			}
		});

		sendBroadcastButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String message = broadcastField.getText();
				broadcastMessage(message);
				broadcastField.setText("");
			}
		});

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(logArea), BorderLayout.CENTER);
		panel.add(broadcastField, BorderLayout.SOUTH);

		getContentPane().add(panel, BorderLayout.CENTER);
		getContentPane().add(startServerButton, BorderLayout.NORTH);
		getContentPane().add(stopServerButton, BorderLayout.WEST);
		getContentPane().add(sendBroadcastButton, BorderLayout.EAST);
		pack();
	}

	private void startServer() {
		final int PORT = 9999;
		try {
			serverSocket = new ServerSocket(PORT, 50);
			logArea.append("Server is listening on port " + PORT + "\n");
			startServerButton.setEnabled(false);
			stopServerButton.setEnabled(true);
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (!serverSocket.isClosed()) {
							Socket clientSocket = serverSocket.accept();
							System.out.println(clientSocket);
							logArea.append("Listen from: " + clientSocket.getInetAddress().getHostAddress() + "\n");
							ClientHandler handler = new ClientHandler(clientSocket);
							clients.add(handler);
							new Thread(handler).start();
						}
					} catch (IOException e) {
						logArea.append("Server stopped.\n");
					}
				}
			}).start();

		} catch (IOException e) {
			logArea.append("Could not start the server.\n");
			e.printStackTrace();
		}
	}

	private void stopServer() {
		try {
			for (ClientHandler client : clients) {
				client.closeConnection();
			}
			serverSocket.close();
			startServerButton.setEnabled(true);
			stopServerButton.setEnabled(false);
			logArea.append("Server stopped.\n");
		} catch (IOException e) {
			logArea.append("Could not stop the server.\n");
			e.printStackTrace();
		}
	}

	private void broadcastMessage(String message) {
		for (ClientHandler client : clients) {
			client.sendMessage(message);
		}
		logArea.append("Broadcast: " + message + "\n");
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ServerUIApp server = new ServerUIApp();
				server.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				server.setVisible(true);
			}
		});
	}

	private class ClientHandler implements Runnable {
		private Socket socket;
		private ObjectInputStream in;
		private ObjectOutputStream out;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			try {
				out = new ObjectOutputStream(socket.getOutputStream());
				in = new ObjectInputStream(socket.getInputStream());

				while (true) {
					String message = in.readUTF();

					System.out.println("Received: " + message);
					logArea.append("Listen from: " + socket.getInetAddress().getHostAddress() + " - " + message + "\n");
					sendMessage("Server send message: " + message);
				}
			} catch (IOException e) {
//				e.printStackTrace();
			}
		}

		public void sendMessage(String message) {
			try {
				out.writeUTF(message);
				out.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void closeConnection() {
			try {
				if (out != null)
					out.close();
				if (in != null)
					in.close();
				if (socket != null)
					socket.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}
}
