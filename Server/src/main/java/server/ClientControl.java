package server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by lemon on 12/17/2016.
 */
@SuppressWarnings({"DefaultFileTemplate", "WeakerAccess"})
public class ClientControl extends Thread{
    private Callback callBack;
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader bufferedReader;
    private boolean isRunning=false;

    public ClientControl(Callback callback, Socket socket, PrintWriter printWriter, BufferedReader bufferedReader) {
        this.socket = socket;
        this.printWriter = printWriter;
        this.bufferedReader = bufferedReader;
        this.callBack = callback;
    }

    @Override
    public void run() {
        super.run();
        isRunning=true;

        try {
            while (isRunning){
                String message;
                try {
                    message=bufferedReader.readLine();
                    if(message!=null)
                        callBack.readMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }


    public void send(String message) {
        if(message==null) return;
        printWriter.println(message);
        printWriter.flush();
    }

    public void closeConnection() {
        isRunning=false;
        if(printWriter!=null)
            printWriter.flush();
        if(printWriter!=null)
            printWriter.close();
        try {
            if(bufferedReader!=null)
                bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if(socket!=null)
                socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        printWriter=null;
        bufferedReader=null;
        socket=null;
    }
}
