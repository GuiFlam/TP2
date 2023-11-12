package com.chat.client;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;
import com.echecs.Position;

/**
 * Cette classe représente un gestionnaire d'événement d'un client. Lorsqu'un client reçoit un texte d'un serveur,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementClient implements GestionnaireEvenement {
    private Client client;

    /**
     * Construit un gestionnaire d'événements pour un client.
     *
     * @param client Client Le client pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementClient(Client client) {
        this.client = client;
    }
    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un serveur.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        Connexion cnx;
        String typeEvenement, arg;
        String[] membres;
        ClientChat clientChat = (ClientChat)client;

        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            typeEvenement = evenement.getType();
            switch (typeEvenement) {
                case "END" : //Le serveur demande de fermer la connexion
                    client.deconnecter(); //On ferme la connexion
                    break;
                case "LIST" : //Le serveur a renvoyé la liste des connectés
                    arg = evenement.getArgument();
                    membres = arg.split(":");
                    System.out.println("\t\t"+membres.length+" personnes dans le salon :");
                    for (String s:membres)
                        System.out.println("\t\t\t- "+s);
                    break;

                case "CHESSOK":
                    System.out.print("Une nouvelle partie est creer, vous etes ");
                    System.out.println(Character.compare(evenement.getArgument().toCharArray()[0], 'b') == 0 ? "les BLANCS\n" : "les NOIRS\n");
                    clientChat.nouvellePartie();
                    System.out.println(clientChat.getEtatPartieEchecs());
                    break;

                case "MOVE":
                    arg = evenement.getArgument();
                    String[] arguments = arg.split(" ");
                    char[] positions = arguments[0].toCharArray();
                    char piece = ' ';
                    Position posInitiale = null;
                    Position posFinale = null;

                    if(positions.length == 4) {
                        posInitiale = new Position(positions[0], (byte)((int)(positions[1])-49));
                        posFinale = new Position(positions[2], (byte)((int)(positions[3])-49));
                    }
                    else if(positions.length == 5) {
                        posInitiale = new Position(positions[0], (byte)((int)(positions[1])-49));
                        posFinale = new Position(positions[3], (byte)((int)(positions[4])-49));
                    }

                    EtatPartieEchecs etatPartieEchecs = clientChat.getEtatPartieEchecs();

                    for(int i = 0; i < etatPartieEchecs.getEtatEchiquier().length; ++i) {
                        for(int j = 0; j < etatPartieEchecs.getEtatEchiquier()[0].length; ++j) {
                            if (i == posInitiale.getLigne() && j == (int)posInitiale.getColonne() - 97) {
                                piece = etatPartieEchecs.getEtatEchiquier()[j][i];
                                etatPartieEchecs.getEtatEchiquier()[j][i] = ' ';
                            }
                        }
                    }
                    for(int i = 0; i < etatPartieEchecs.getEtatEchiquier().length; ++i) {
                        for(int j = 0; j < etatPartieEchecs.getEtatEchiquier()[0].length; ++j) {
                            if (i == posFinale.getLigne() && j == (int)posFinale.getColonne() - 97) {
                                etatPartieEchecs.getEtatEchiquier()[j][i] = piece;
                            }
                        }
                    }

                    clientChat.setEtatPartieEchecs(etatPartieEchecs);
                    System.out.println();
                    System.out.println("C'est au tour de: " + arguments[1] + " (" + (arguments[2].equals("b") ? "Blancs" : "Noirs") + ")");
                    System.out.println("---------------------------------");
                    System.out.println(clientChat.getEtatPartieEchecs());
                    System.out.println("---------------------------------");
                    System.out.println("Entrez MOVE xxyy / xx-yy / xx yy pour faire un deplacement:");

                    break;

                case "MAT":
                    clientChat.setEtatPartieEchecs(null);
                    break;
                case "ABANDON":
                    clientChat.setEtatPartieEchecs(null);
                    String[] message = evenement.getArgument().split(" ");
                    System.out.println(message[1] + " à abandonné la partie d'échecs");
                    break;

                default: //Afficher le texte recu :
                    System.out.println("\t\t\t."+evenement.getType()+" "+evenement.getArgument());
            }
        }
    }
}
