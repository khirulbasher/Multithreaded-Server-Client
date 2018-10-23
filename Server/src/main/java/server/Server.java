package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by lemon on 11/28/2016.
 */
@SuppressWarnings({"WeakerAccess", "InfiniteLoopStatement", "DefaultFileTemplate"})
public class Server extends Thread implements Callback{
    private Main main;
    private ServerSocket serverSocket;
    private ClientControl clientControl;
    private boolean isRunning=false;
    public static final int TCP_PORT=2323;

    public Server(Main main) {
        this.main = main;
    }

    @Override
    public void run() {
        super.run();
        isRunning=true;
        try {
            serverSocket=new ServerSocket(TCP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        while(isRunning){
            main.setStatus("Wait for connection...");
            try {
                Socket socket=serverSocket.accept();
                PrintWriter printWriter=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
                printWriter.flush();
                BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));

                clientControl=new ClientControl(this,socket,printWriter,reader);
                clientControl.start();
                main.setStatus("Connected...");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stopAll();
    }

    public void send(String message) {
        clientControl.send(message);
    }

    @Override
    public void readMessage(String message) {
        main.jTextArea.append(message+"\n");
    }

    @Override
    public void interrupt() {
        stopAll();
        super.interrupt();
    }

    public void stopAll() {
        main.setStatus("Disconnected...");
        isRunning=false;
        if (clientControl!=null) {
            clientControl.closeConnection();
        }
        try {
            if(serverSocket!=null)
                serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        serverSocket=null;
    }
}
