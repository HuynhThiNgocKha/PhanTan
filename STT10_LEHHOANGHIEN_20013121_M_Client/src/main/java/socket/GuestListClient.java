package socket;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import entities.Guest;

public class GuestListClient {

    public static void main(String[] args) {
        InetAddress serverAddress;
        int port = 6789;

        try {
            serverAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            System.err.println("Error: Could not find server. Please check the hostname or IP address.");
            e.printStackTrace();
            return;
        }

        try (Socket connectionSocket = new Socket(serverAddress, port);
             DataOutputStream outToServer = new DataOutputStream(connectionSocket.getOutputStream());
             ObjectInputStream inFromServer = new ObjectInputStream(connectionSocket.getInputStream())) {

            // Send request for guest list
            outToServer.writeBytes("GET_GUEST_LIST\n");

            // Receive guest list from server
            List<Guest> guestList = (List<Guest>) inFromServer.readObject();
            System.out.println("Guest List:");
            for (Guest guest : guestList) {
                System.out.println(guest);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error: Could not connect to server. Please check the network connection or server status.");
            e.printStackTrace();
        }
    }
}
