package server;


import javax.swing.*;
import java.awt.*;

/**
 * Created by lemon on 11/28/2016.
 */
@SuppressWarnings({"FieldCanBeLocal", "WeakerAccess", "MagicConstant", "DefaultFileTemplate"})
public class Main extends JFrame {
    private JTextField field;
    private JButton start,stop,send;
    public JTextArea jTextArea;
    private JPanel buttonPanel;
    private JLabel statusBar;
    private Server server;

    public Main() {
        super("Server...");
        setVisible(true);
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jTextArea = new JTextArea(50,30);
        add(new JScrollPane(jTextArea), BorderLayout.CENTER);
        buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        add(buttonPanel, BorderLayout.NORTH);
        start = new JButton("START");
        stop = new JButton("STOP");
        send = new JButton("SEND");
        buttonPanel.add(start);
        buttonPanel.add(stop);
        field=new JTextField(35);
        buttonPanel.add(field);
        buttonPanel.add(send);

        modifyActionButton(false);

        start.addActionListener(e -> {
            server=new Server(Main.this);
            server.start();
            modifyActionButton(true);
        });

        stop.addActionListener(e -> {
            modifyActionButton(false);
            if(server!=null){
                server.stopAll();
                server=null;
            }

        });
        send.addActionListener(e -> {
            String text=field.getText();
            field.setText("");
            server.send(text);
            setStatus("Message was sent...");
        });

        statusBar = new JLabel("STATUS:");
        add(statusBar, BorderLayout.SOUTH);
    }

    private void modifyActionButton(boolean stat) {
        send.setEnabled(stat);
        stop.setEnabled(stat);
        start.setEnabled(!stat);
    }


    public static void main(String[] args){
        new Main();
    }

    public void setStatus(String status){
        statusBar.setText("STATUS:"+status);
    }

}
