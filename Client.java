import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Thread{
    private Socket socket;
    private BufferedReader  bufferedReader;
    private BufferedWriter bufferedWriter;
    private ClientInterfaceConnection clientInterfaceConnection;
    private InputThread inputThread;
    private boolean running;

    private String username;

    public Client(){
         running = true;
     }

    public void setClientInterfaceConnection(ClientInterfaceConnection clientInterfaceConnection) {
        this.clientInterfaceConnection = clientInterfaceConnection;
    }

    public InputThread getInputThread() {
        return inputThread;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public void run() {



        try {
            socket = new Socket("localhost", 1234);
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            inputThread = new InputThread(bufferedWriter);
            Thread thread = new Thread(inputThread);
            thread.start();


            while(running){
//                System.out.println("Reading");
                String message = bufferedReader.readLine();
//                System.out.println("Entering updateTextArea");
                SwingUtilities.invokeLater(()->clientInterfaceConnection.updateTextArea(message + "\n"));
                System.out.println(message);
            }


        }catch(Exception e){


        }
    }

    void quit(InputThread inputThread){
        try {
            bufferedWriter.write("/quit");
            bufferedWriter.newLine();
            bufferedWriter.flush();
            System.out.println("Closed");
        }catch(Exception e){
            System.out.println("Wrong");
        }
        inputThread.setRunning(false);
        running = false;
    }

    class InputThread implements Runnable
    {
        private BufferedWriter writeToServer;
        private boolean running;
        private boolean first;

        public InputThread(BufferedWriter printWriter){
            this.writeToServer = printWriter;
            running = true;
            first = true;
        }

        public void setRunning(boolean running) {
            this.running = running;
        }

        public void send(String text){
            try {
                writeToServer.write(text);
                writeToServer.newLine();
                writeToServer.flush();
                if(first) {
                    username = text;
                    first = false;
                    SwingUtilities.invokeLater(()->clientInterfaceConnection.setUser());
                }
            }catch (Exception e){

            }
        }

        @Override
        public void run() {
            try {
                Scanner scanner = new Scanner(System.in);
                while(running){
//                    System.out.println("INput");
//                    String message = scanner.nextLine();
//                    writeToServer.write(message);
//                    writeToServer.newLine();
//                    writeToServer.flush();
//
//                    if(first) {
//                        username = message;
//                        first = false;
//                        SwingUtilities.invokeLater(()->clientInterfaceConnection.setUser());
//                    }
//
//
//                    if(message.equals("/quit"))
//                        quit(this);

                }


            }catch(Exception e){

            }
        }
    }




//    public static void main(String[] args) {
//        Thread thread = new Thread(new Client());
//        thread.start();
//    }


}
