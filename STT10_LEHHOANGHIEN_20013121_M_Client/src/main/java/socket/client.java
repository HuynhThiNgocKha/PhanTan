package socket;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {

  private static final int SERVER_PORT = 6789;
  private static final String SERVER_HOST = "localhost";

  public static void main(String args[]) throws Exception {
    Scanner scanner = new Scanner(System.in);
    int choice;

    while (true) {
      displayMenu();
      choice = scanner.nextInt();

      switch (choice) {
        case 1:
          sendMessageToServer(scanner);
          break;
        case 2:
          System.out.println("Exiting...");
          System.exit(0);
        default:
          System.out.println("Invalid choice. Please try again.");
      }
    }
  }

  private static void sendMessageToServer(Scanner scanner) throws Exception {
    Socket clientSocket = new Socket(SERVER_HOST, SERVER_PORT);

    DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
    BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

    System.out.println("Enter your message: ");
    String message = scanner.nextLine();

    outToServer.writeBytes(message + '\n');

    String response = inFromServer.readLine();
    System.out.println("FROM SERVER: " + response);

    clientSocket.close();
  }

  private static void displayMenu() {
    System.out.println("\n** Client Menu **");
    System.out.println("1. Send Message");
    System.out.println("2. Exit");
    System.out.print("Enter your choice: ");
  }
}
