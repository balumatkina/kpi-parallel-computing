import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class AdditionalClient {
    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;

    public AdditionalClient(String address, int port) {
        Scanner scanner = new Scanner(System.in);
        int arrayLength = 0, nThreads = 0;
        try {
            socket = new Socket(address, port);
            System.out.println("The client is connected\n" + socket +"\n");

            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException u) {
            u.printStackTrace();
        }

        try {
            serverRead(dis);

            while (true) {
                System.out.println("Enter command number (1-5): ");
                String command = scanner.nextLine();

                dos.writeUTF(command);

                switch (command) {
                    case "1" -> {
                        serverRead(dis);

                        while (!(arrayLength > 0)) {
                            System.out.println("Print array length:");
                            arrayLength = scanner.nextInt();
                        }

                        while (!(nThreads > 0)) {
                            System.out.println("Print threads amount:");
                            nThreads = scanner.nextInt();
                        }
                        scanner.nextLine();

                        dos.writeUTF(arrayLength + " " + nThreads);

                        serverRead(dis);
                    }
                    case "2" -> {
                        List<Double> array = generateRandDoubleArrList(arrayLength);

                        serverRead(dis);

                        for (double num : array) {
                            dos.writeDouble(num);
                        }

                        serverRead(dis);
                    }
                    case "3" ->
                            serverRead(dis);
                    case "4" -> {
                        serverRead(dis);
                        serverRead(dis);
                    }
                    case "5" -> {
                        serverRead(dis);
                        serverRead(dis);

                        try {
                            if (dis != null) {
                                dis.close();
                            }
                            if (dos != null) {
                                dos.close();
                            }
                            if (socket != null) {
                                socket.close();
                            }
                            if (scanner != null) {
                                scanner.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return;
                    }
                    default -> System.out.println("Invalid operation number. Please try again.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        Client client = new Client("localhost", 6060);
        try {
            new AdditionalClient("localhost", 6060);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Double> generateRandDoubleArrList (int arrLength) {
        Random random = new Random();
        List<Double> array = new ArrayList<>();

        for (int i = 0; i < arrLength; i++) {
            array.add(random.nextDouble(100));
        }
        return array;
    }

    private static void serverRead(DataInputStream dis) throws IOException {
        String serverResponse = dis.readUTF();
        System.out.println("SERVER: " + serverResponse);
    }

}
