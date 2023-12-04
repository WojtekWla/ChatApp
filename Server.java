import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private ArrayList<ConnectionManager> connections;

    public Server(){
        connections = new ArrayList<>();
    }


    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(1234);
            pool = Executors.newCachedThreadPool();
            while(true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connected user");
                ConnectionManager connectionManager = new ConnectionManager(socket);
                connections.add(connectionManager);
                pool.execute(connectionManager);
                System.out.println("Out");
            }



        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void broadcast(String message){
        for(ConnectionManager cm : connections){
            cm.sendMessage(message);
        }
    }

    public void userQuit(ConnectionManager connectionManager)
    {
        connectionManager.setRunning(false);
        broadcast(connectionManager.getUser_name() + ": left the chat");
        connections.remove(connectionManager);
        System.out.println(connections.size());
    }


    class ConnectionManager extends Thread{
        private String user_name;
        private Socket socket;
        private BufferedReader bufferedReader;
        private BufferedWriter bufferedWriter;

        private boolean running;

        public ConnectionManager(Socket socket) {
            this.socket = socket;
            running = true;
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public String getUser_name() {
            return user_name;
        }

        @Override
        public void run() {

            try {
                bufferedWriter.write("Hello, input your username");
                bufferedWriter.newLine();
                bufferedWriter.flush();
                String message = bufferedReader.readLine(); // Read the user's input
                this.user_name = message;
                System.out.println(message);
                broadcast(user_name + ": joined the session");


                while (running) {
                    String line = bufferedReader.readLine();
                    if(line.equalsIgnoreCase("/quit"))
                    {
                        userQuit(this);
                    }else
                        broadcast(user_name + ": " + line);
                }
            }catch(Exception e){

            }


        }
        public void sendMessage(String message)
        {
            try {
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
//        public void quit(){
//
//            try {
//                bufferedReader.close();
//                bufferedWriter.close();
//                socket.close();
//            }catch(Exception e){
//
//            }
//            userQuit(this);
//        }



    }

    public static void main(String[] args) {
        Thread thread = new Thread(new Server());
        thread.start();
    }


}
