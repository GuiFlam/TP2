package controleur;

import com.chat.client.ClientChat;
import com.chat.commun.net.Connexion;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EcouteurJeuEchecs implements ActionListener {

    private int iInitial = -1;
    private int jInitial = -1;
    private int iFinal = -1;
    private int jFinal = -1;
    private ClientChat clientChat;

    public EcouteurJeuEchecs(ClientChat clientChat) {
        this.clientChat = clientChat;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //à compléter
        if(e.getSource() instanceof JButton) {
            char[] deplacement = ((JButton)e.getSource()).getActionCommand().toCharArray();
            if(iInitial == -1 && jInitial == -1) {
                iInitial = (int)deplacement[0]-97;
                jInitial = (int)deplacement[1]-48;
            }
            else {
                iFinal = (int)deplacement[0]-97;
                jFinal = (int)deplacement[1]-48;
                clientChat.envoyer("MOVE " + (char)(iInitial+97) + "" + jInitial + "-" + (char)(iFinal+97) + "" + jFinal);

                iInitial = -1;
                jInitial = -1;
                iFinal = -1;
                jFinal = -1;
            }
        }
    }
}