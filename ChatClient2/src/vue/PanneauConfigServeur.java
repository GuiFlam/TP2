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

        JTextField ipTextField = new JTextField();
        ipTextField.setText(adr);
        JTextField portTextField = new JTextField();
        portTextField.setText(Integer.toString(port));

        add(ipLabel);
        add(ipTextField);
        add(portLabel);
        add(portTextField);
    }
    public String getAdresseServeur() {
        return txtAdrServeur.getText();
    }
    public String getPortServeur() {
        return txtNumPort.getText();
    }
}
