import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientInterface extends JFrame {

    JLabel userLabel;
    TextArea chatArea;
    JTextField inputField;
    JButton sendButton;
    JButton quitButton;

    ClientInterfaceConnection clientInterfaceConnection;

    public JLabel getUserLabel() {
        return userLabel;
    }

    public TextArea getChatArea() {
        return chatArea;
    }

    public JTextField getInputField() {
        return inputField;
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JButton getQuitButton() {
        return quitButton;
    }

    public ClientInterface(){
        JPanel jPanel = new JPanel(new BorderLayout());

        userLabel = new JLabel("Username connected");
        chatArea = new TextArea(30,50);
        chatArea.setEditable(false);
        chatArea.setFocusable(false);

        JScrollPane jScrollPane = new JScrollPane(chatArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        inputField = new JTextField(50);
        sendButton = new JButton("Send");
        quitButton = new JButton("Exit");



        JPanel topPanel = new JPanel(new FlowLayout());
        topPanel.add(userLabel);
        topPanel.add(quitButton);

        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.add(inputField);
        bottomPanel.add(sendButton);

        jPanel.add(topPanel, BorderLayout.NORTH);
        jPanel.add(jScrollPane, BorderLayout.CENTER);
        jPanel.add(bottomPanel, BorderLayout.SOUTH);


        //starting Client
        clientInterfaceConnection = new ClientInterfaceConnection(new Client(), this);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendText();
            }
        });

        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER)
                    sendButton.doClick();
            }
        });

        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clientInterfaceConnection.quit();
                //TODO close the window
                System.exit(0);
            }
        });

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                clientInterfaceConnection.quit();
                System.exit(0);
            }
        });



        add(jPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);


    }

    public void sendText(){
        clientInterfaceConnection.sendMessage(inputField.getText());
        inputField.setText("");
    }

}
