import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.*;

public class ClientHandler implements Runnable {
    private Socket clSocket;
    private ExecutorService executor;
    private DataOutputStream dos;
    private DataInputStream dis;

    public ClientHandler(Socket clSocket) throws IOException {
        this.clSocket = clSocket;
        dis = new DataInputStream(clSocket.getInputStream());
        dos = new DataOutputStream(clSocket.getOutputStream());
    }

        @Override
        public void run () {
        try {
            CompletableFuture<Double[]> future = new CompletableFuture<>();
            int length = 0, nThreads = 0;
            boolean isConnected = true;
            String clientCommand;
            ArrayList<Double> array = new ArrayList<>();

            String serverCommands = """
                    This server provides calculations of mode 
                    and median of a given vector of numbers.
                    Options:
                    1. Setting configurations (vector length, number of threads);
                    2. Sending a vector to the server;
                    3. Starting processing;
                    4. Getting results of counted mode and median;
                    5. Disconnecting from the server.
                    """;
            dos.writeUTF(serverCommands);

            while (isConnected) {
                clientCommand = dis.readUTF();
                switch (clientCommand) {
                    case "1" -> {
                        serverResponse(dos, "Option 1. Getting configurations.");

                        String[] configs = dis.readUTF().split(" ");
                        length = Integer.parseInt(configs[0]);
                        nThreads = Integer.parseInt(configs[1]);

                        serverResponse(dos, "Received data:\nArray length: "
                                + length + "\nThreads amount: " + nThreads + "\n");
                    }
                    case "2" -> {
                        array.clear();
                        serverResponse(dos, "Option 2. Getting array.");

                        for (int i = 0; i < length; i++) {
                            array.add(dis.readDouble());
                        }

                        serverResponse(dos, "Array successfully received.\n");
                    }
                    case "3" -> {
                        serverResponse(dos, "Option 3. Starting processing.\n");

                        // Future starting
                        int finalNumbThreads = nThreads;
                        future = CompletableFuture.supplyAsync(() ->
                                processVector(array, finalNumbThreads));
                    }
                    case "4" -> {
                        serverResponse(dos, "Option 4. Getting results.");

                        if (future != null && future.isDone()) {
                            Double[] result = future.getNow(null);
                            serverResponse(dos, "Result:\nMode: " + result[0] +
                                    "\nMedian: " + result[1] + "\n");
                            future = null;
                        } else {
                            serverResponse(dos, "At the moment results are not ready.\n");
                        }
                    }
                    case "5" -> {
                        serverResponse(dos, "Option 5. Breaking connection.");

                        isConnected = false;
                        if (executor != null) {
                            executor.shutdown();
                        }

                        serverResponse(dos, "Connection stopped.\n");
                    }
                    default -> System.err.println("Unknown operation.");
                }
            }

            try {
                if (dis != null) {
                    dis.close();
                }
                if (dos != null) {
                    dos.close();
                }
                if (clSocket != null) {
                    clSocket.close();
                }
                System.out.println("Client closed: " + clSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Double[] processVector(ArrayList<Double> array, int nThreads) {
        Double mode = 0.0, counter = 0.0, finalMedian = 0.0;
        int length = array.size();

        executor = Executors.newFixedThreadPool(nThreads);

        ArrayList<Double> medianArray = new ArrayList<>();
        // Task list for different threads
        List<Calculation> tasks = new ArrayList<>();
        // For Future
        CompletionService<Double[]> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0; i < nThreads; i++) {
            int startIndex = length / nThreads * i;
            int endIndex = (i == (nThreads - 1)) ? length : (length / nThreads * (i + 1));

            endIndex = Math.min(endIndex, length);

            tasks.add(new Calculation(array, startIndex, endIndex));
        }

        try {
            // Submitting tasks for processing
            for (int i = 0; i < nThreads; i++) {
                completionService.submit(tasks.get(i));
            }

            mode = array.get(0);
            finalMedian = array.get(0);
            for (int i = 0; i < nThreads; i++) {
                Future<Double[]> completedFuture = completionService.take();
                Double[] res = completedFuture.get();

                if (res[2] > counter) {
                    mode = res[1];
                    counter = res[2];
                }

                medianArray.add(res[0]);
                Collections.sort(medianArray);
                int middle = medianArray.size() / 2;
                if (medianArray.size() % 2 == 0) {
                    finalMedian = (medianArray.get(middle - 1) + medianArray.get(middle)) / 2.0;
                } else {
                    finalMedian = medianArray.get(middle);
                }

            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return new Double[]{mode, finalMedian};
    }

    private static void serverResponse(DataOutputStream dos, String message) throws IOException {
        dos.writeUTF(message);
        System.out.println(message);
    }
}