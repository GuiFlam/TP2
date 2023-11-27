package controleur;

import com.chat.client.ClientChat;
import vue.PanneauInvitations;
import vue.PanneauPrincipal;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EcouteurEvenementAction implements ActionListener {
    ClientChat clientChat;
    PanneauInvitations panneauInvitations;
    public EcouteurEvenementAction(ClientChat clientChat, PanneauInvitations panneauInvitations) {
        this.clientChat = clientChat;
        this.panneauInvitations = panneauInvitations;
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if(evt.getSource() instanceof JButton) {
            String text = ((JButton) evt.getSource()).getText();

            List<String> invitationsRecues = panneauInvitations.getElementsSelectionnes();

            String commande = "";
            for(int i = 0; i <invitationsRecues.size(); ++i) {
                if(text.equals("+")) {
                    commande = "JOIN ";
                }
                else if(text.equals("x")) {
                    commande = "DECLINE ";
                }
                commande += invitationsRecues.get(i);
                clientChat.envoyer(commande);
            }
            for(int i = 0; i <invitationsRecues.size(); ++i) {
                panneauInvitations.retirerInvitationRecue(invitationsRecues.get(i));
            }
        }
    }
}
