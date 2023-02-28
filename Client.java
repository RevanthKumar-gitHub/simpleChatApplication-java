import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Client extends KeyAdapter{
    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;
    private JFrame f = new JFrame();
    private JTextField t = new JTextField();
    private JLabel header = new JLabel("CLIENT END");
    private JTextArea msg = new JTextArea();
    private Font font;

    Client() {
        try {
            clientSocket = new Socket("localhost", 1234);
            System.out.println("connected!");

            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            out = new PrintWriter(clientSocket.getOutputStream());

            createWindow();
            handleEvents();
            startReading();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void handleEvents() {
        t.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == 10)
                {
                    String msgContent = t.getText();
                    if(msgContent.equals("exit"))
                    {
                        t.setText("");
                        msg.append("Me :" + msgContent+"\n");
                        out.println(msgContent);
                        out.flush();
                        t.setEditable(false);
                    }
                    else
                    {
                        t.setText("");
                        msg.append("Me :" + msgContent+"\n");
                        out.println(msgContent);
                        out.flush();
                    }
                }
            }
            
        });
    }

    public void createWindow() {
        font = new Font("Sans-serif", Font.PLAIN, 20);

        header.setHorizontalAlignment(SwingConstants.CENTER);

        header.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        msg.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        t.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        msg.setBackground(Color.lightGray);
        msg.setEditable(false);

        header.setFont(font);
        font = new Font("Serif", Font.BOLD, 15);
        msg.setFont(font);
        t.setFont(font);

        f.setLayout(new BorderLayout());

        JScrollPane  j =new JScrollPane(msg);

        f.add(t, BorderLayout.SOUTH);
        f.add(header, BorderLayout.NORTH);
        f.add(j, BorderLayout.CENTER);

        f.setTitle("CHAT APPLICATION[CLIENT-END]");
        f.setVisible(true);
        f.setSize(400, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
    }

    public void startReading() {
        Runnable r4 = () -> {
            try {

                while (true) {
                    String serverMsg = in.readLine();
                    if (serverMsg.equals("exit")) {
                        System.out.println("connection closed");
                        t.setEditable(false);
                        clientSocket.close();
                        System.exit(0);
                        break;
                    }
                    else
                    {
                        msg.append("Server: " + serverMsg+"\n");
                    }
                }
            } catch (Exception e) {
                System.out.println("connection closed");
                JOptionPane.showMessageDialog(f,"Connection lost");
                System.exit(0);
            }
        };
        new Thread(r4).start();
    }

    public static void main(String[] args) {
        new Client();
    }


}