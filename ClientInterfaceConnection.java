import javax.swing.*;

public class ClientInterfaceConnection {
    private Client client;
    private ClientInterface clientInterface;

    public ClientInterfaceConnection(Client client, ClientInterface clientInterface) {
        this.client = client;
        this.clientInterface = clientInterface;
        client.setClientInterfaceConnection(this);
        this.client.start();
    }

    public void setUser(){
        SwingUtilities.invokeLater(()->clientInterface.getUserLabel()
                .setText(client.getUsername() + " connected"));
    }

    public void updateTextArea(String text){
        clientInterface.getChatArea().append(text);
    }

    public void sendMessage(String text){
        client.getInputThread().send(text);
    }

    public void quit(){
        client.quit(client.getInputThread());
        //when the window is getting closed the client will still work create a function that will interrupt user from working and close connection
    }




}
