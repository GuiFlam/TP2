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
public class PanneauChat extends JPanel {
    protected JTextArea zoneChat;
    protected JTextField champDeSaisie;

    public PanneauChat() {
        setLayout(new BorderLayout());

        zoneChat = new JTextArea();
        zoneChat.setEditable(false);

        champDeSaisie = new JTextField();

        add(champDeSaisie, BorderLayout.SOUTH);
        add(zoneChat);

        JScrollPane scrollPane = new JScrollPane(zoneChat);

        JViewport viewport = scrollPane.getViewport();

        SwingUtilities.invokeLater(() -> {
            int x = (viewport.getWidth() - zoneChat.getPreferredSize().width) / 2;
            int y = (viewport.getHeight() - zoneChat.getPreferredSize().height) / 2;
            viewport.setViewPosition(new Point(x, y));
        });

        add(scrollPane, BorderLayout.CENTER);
    }

    public void ajouter(String msg) {
        zoneChat.append("\n"+msg);
    }
    public void setEcouteur(ActionListener ecouteur) {
        //Enregistrer l'écouteur auprès du champ de saisie
        champDeSaisie.addActionListener(ecouteur);
    }

    public void vider() {
        this.zoneChat.setText("");
    }
}