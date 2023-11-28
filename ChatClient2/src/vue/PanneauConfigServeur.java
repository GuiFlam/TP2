package vue;

import jdk.nashorn.internal.runtime.NumberToString;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-11-01
 */
public class PanneauConfigServeur extends JPanel {
    private JTextField txtAdrServeur, txtNumPort;

    public PanneauConfigServeur(String adr, int port) {
        setLayout(new GridLayout(2,1));

        JLabel ipLabel = new JLabel("Adresse IP:");
        JLabel portLabel = new JLabel("Port:");

        txtAdrServeur = new JTextField();
        txtAdrServeur.setText(adr);
        txtNumPort = new JTextField();
        txtNumPort.setText(Integer.toString(port));

        add(ipLabel);
        add(txtAdrServeur);
        add(portLabel);
        add(txtNumPort);
    }
    public String getAdresseServeur() {
        return txtAdrServeur.getText();
    }
    public String getPortServeur() {
        return txtNumPort.getText();
    }
}
