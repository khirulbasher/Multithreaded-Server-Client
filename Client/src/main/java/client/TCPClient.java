package client;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by lemon on 11/28/2016.
 */
@SuppressWarnings({"DefaultFileTemplate", "WeakerAccess"})
public class TCPClient extends Thread{

    private final Main main;
    private Listener listener;
    private boolean isRunning;
    private PrintWriter outputStream;
    private BufferedReader inputStream;
    private Socket socket;
    public static final int TCP_PORT=2323;


    public TCPClient(Main main, Listener listener) {
        this.main = main;
        this.listener = listener;
    }

    public void sendMessage(String message) {
        if(message!=null){
            outputStream.println(message);
            outputStream.flush();
        }
    }

    public void stopConnection() {
        isRunning=false;
        main.setStatus("Disconnected...");
        try {
            if(outputStream!=null){
                outputStream.flush();
                outputStream.close();
            }
            if(inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listener=null;
        socket=null;
        inputStream=null;
        outputStream=null;
    }

    @Override
    public void run() {
        super.run();
        startSocket();
    }

    private void startSocket() {
        isRunning=true;

        try {
            main.setStatus("Try to Connect...");
            socket=new Socket(InetAddress.getLocalHost(), TCP_PORT);
            outputStream=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            outputStream.flush();
            inputStream=new BufferedReader(new InputStreamReader(socket.getInputStream()));
            main.setStatus("Connected...");

            while (isRunning){
                String message;

                try {
                    message=inputStream.readLine();
                    if(message!=null)
                        listener.onListen(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            stopConnection();
            main.stopped();
            main.setStatus("Disconnected... ");
        }
    }

}
