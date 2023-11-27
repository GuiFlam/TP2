package controleur;

import com.chat.client.ClientChat;
import vue.PanneauChat;
import vue.PanneauChatPrive;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

/**
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-11-01
 */
public class EcouteurChatPrive extends EcouteurChatPublic {
    private String alias;
    public EcouteurChatPrive(String alias, ClientChat clientChat, PanneauChat panneauChat) {
        super(clientChat, panneauChat);
        this.alias = alias;
    }
    //à compléter (redéfinir la méthode actionPerformed())
    @Override
    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource() instanceof JButton) {
            String text = ((JButton) evt.getSource()).getText();
            if(text.equals("Accepter") || text.equals("Inviter échec")) {
                clientChat.envoyer("CHESS " + alias);
            }
            else if(text.equals("Refuser")) {
                clientChat.envoyer("DECLINE " + alias);
            }
        }
        else if(evt.getSource() instanceof JTextField) {
            JTextField source = (JTextField) evt.getSource();
            String text = source.getText();
            if(text.equals("QUIT")) {
                clientChat.envoyer("QUIT " + alias);
            }
            else if(text.equals("ABANDON")) {
                clientChat.envoyer("ABANDON");
            }
            else {
                clientChat.envoyer("PRV " + alias + " " + text);
            }
            source.setText("");
        }
    }

}