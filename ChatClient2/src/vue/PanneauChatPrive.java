package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-11-01
 */
public class PanneauChatPrive extends PanneauChat {
    private JButton bAccepterOuInviter, bRefuser;
    private FenetreEchecs fenetreEchecs;
    public PanneauChatPrive() {
        bAccepterOuInviter = new JButton("Inviter échec");
        bRefuser = new JButton("Refuser");
        bRefuser.setVisible(false);
        bAccepterOuInviter.setActionCommand("ACCEPTER");
        bRefuser.setActionCommand("REFUSER");

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.add(bAccepterOuInviter, BorderLayout.EAST);
        panel.add(bRefuser, BorderLayout.WEST);

        add(panel ,BorderLayout.NORTH);



    }
    @Override
    public void setEcouteur(ActionListener ecouteur) {
        super.setEcouteur(ecouteur);
        bAccepterOuInviter.addActionListener(ecouteur);
        bRefuser.addActionListener(ecouteur);
    }
    public void invitationEchecRecue() {
        //à compléter
        bAccepterOuInviter.setText("Accepter");
        bRefuser.setVisible(true);
    }
    public void invitationEchecAnnulee() {
        //à compléter
        bAccepterOuInviter.setText("Inviter échec");
        bRefuser.setVisible(false);
    }

    public void setFenetreEchecs(FenetreEchecs fenetreEchecs) {
        if (this.fenetreEchecs!=null){
            this.fenetreEchecs.setVisible(false);
            this.fenetreEchecs.dispose();
        }
        this.fenetreEchecs = fenetreEchecs;
    }
    public FenetreEchecs getFenetreEchecs() {
        return this.fenetreEchecs;
    }
}