package client;

import javax.swing.*;
import java.awt.*;

/**
 * Created by lemon on 11/28/2016.
 */
@SuppressWarnings({"WeakerAccess", "FieldCanBeLocal", "MagicConstant", "DefaultFileTemplate"})
public class Main extends JFrame{
    private JButton start,stop,send;
    private JTextField field;
    private JPanel buttonPanel;
    private JLabel statusBar;
    private TCPClient client;
    public JTextArea jTextArea;

    public Main() {
        super("Client Socket...");
        setVisible(true);
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jTextArea=new JTextArea(50,30);
        add(new JScrollPane(jTextArea),BorderLayout.CENTER);
        buttonPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        add(buttonPanel,BorderLayout.NORTH);
        start=new JButton("START");
        stop=new JButton("STOP");
        send=new JButton("SEND");
        field=new JTextField(35);
        buttonPanel.add(start);
        buttonPanel.add(stop);
        buttonPanel.add(field);
        buttonPanel.add(send);
        modifyActionButton(false);

        start.addActionListener(e -> {
            modifyActionButton(true);
            client=new TCPClient(Main.this, message -> jTextArea.append(message+"\n"));
            field.setText("");
            client.start();
        });

        stop.addActionListener(e -> {
            modifyActionButton(false);

            if(client!=null){
                client.stopConnection();
                client=null;
            }
        });
        send.addActionListener(e -> {
            if(client!=null){
                client.sendMessage(field.getText());
                setStatus("Message was sent...");
                field.setText("");
            }
        });

        statusBar=new JLabel("STATUS:");
        add(statusBar,BorderLayout.SOUTH);
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

    public void stopped() {
        send.setEnabled(false);
        stop.setEnabled(false);
        start.setEnabled(true);
    }
}
