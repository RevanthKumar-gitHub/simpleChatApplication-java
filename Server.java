import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Server extends KeyAdapter {
    private ServerSocket con;
    private Socket serverSocket;
    private BufferedReader in;
    private PrintWriter out;
    private JTextField t = new JTextField();
    private JLabel header = new JLabel("SERVER END");
    private JTextArea msg = new JTextArea();
    private Font font;
    private JFrame f = new JFrame();

    Server() {
        try {

            //connection 
            con = new ServerSocket(1234);
            System.out.println("System is waiting for client to connect");
            serverSocket = con.accept();
            System.out.println("connected!");

            in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));

            out = new PrintWriter(serverSocket.getOutputStream());

            createWindow();
            handleEvents();
            startReading();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleEvents() {
        t.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    String msgContent = t.getText();
                    if (msgContent.equals("exit")) {
                        t.setText("");
                        msg.append("Server :" + msgContent + "\n");
                        out.println(msgContent);
                        out.flush();
                        t.setEditable(false);
                    } else {
                        t.setText("");
                        msg.append("Server :" + msgContent + "\n");
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

        JScrollPane j = new JScrollPane(msg);

        header.setFont(font);
        font = new Font("Serif", Font.BOLD, 15);
        msg.setFont(font);
        t.setFont(font);

        f.setLayout(new BorderLayout());

        f.add(t, BorderLayout.SOUTH);
        f.add(header, BorderLayout.NORTH);
        f.add(j, BorderLayout.CENTER);

        f.setTitle("CHAT APPLICATION[SERVER-END]");
        f.setVisible(true);
        f.setSize(400, 600);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        f.setLocationRelativeTo(null);
    }

    public void startReading() {
        Runnable r1 = () -> {
            try {
                while (true) {
                    String clientMsg = in.readLine();
                    if (clientMsg.equals("exit")) {
                        System.out.println("connection closed");
                        t.setEditable(false);
                        serverSocket.close();
                        System.exit(0);
                        break;
                    } else {
                        msg.append("Client : " + clientMsg + "\n");
                    }
                }
            } catch (Exception e) {
                System.out.println("connection closed");
                JOptionPane.showMessageDialog(f, "Connection lost");
                System.exit(0);
            }
        };
        new Thread(r1).start();
    }

    public static void main(String[] args) {
        System.out.println("System is going to start the connection......");
        new Server();
    }
}