package com.chat.client;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;
import com.echecs.Position;
import com.echecs.pieces.Dame;
import com.echecs.pieces.Piece;
import com.echecs.pieces.Pion;
import com.echecs.pieces.Roi;

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

                    System.out.println();
                    System.out.println("---------------------------------");
                    System.out.println(clientChat.getEtatPartieEchecs());
                    System.out.println("---------------------------------");
                    System.out.println("Entrez MOVE xxyy / xx-yy / xx yy pour faire un deplacement:");
                    break;

                case "MOVE":
                    arg = evenement.getArgument();
                    String[] arguments = arg.split(" ");

                    Position posInitiale;
                    Position posFinale;

                    if(arguments[0].length() == 2 && arguments[1].length() == 2) {
                        posInitiale = new Position(arguments[0].toCharArray()[0], (byte)((int)(arguments[0].toCharArray()[1])-49));
                        posFinale = new Position(arguments[1].toCharArray()[0], (byte)((int)(arguments[1].toCharArray()[1])-49));
                    }
                    else {
                        if(arguments[0].length() == 4) {
                            posInitiale = new Position(arguments[0].toCharArray()[0], (byte)((int)(arguments[0].toCharArray()[1])-49));
                            posFinale = new Position(arguments[0].toCharArray()[2], (byte)((int)(arguments[0].toCharArray()[3])-49));
                        }
                        else {
                            posInitiale = new Position(arguments[0].toCharArray()[0], (byte)((int)(arguments[0].toCharArray()[1])-49));
                            posFinale = new Position(arguments[0].toCharArray()[3], (byte)((int)(arguments[0].toCharArray()[4])-49));
                        }
                    }

                    char piece = ' ';

                    EtatPartieEchecs etatPartieEchecs = clientChat.getEtatPartieEchecs();

                    // PROMOTION
                    if(etatPartieEchecs.getEtatEchiquier()[(int)posInitiale.getColonne() - 97][(int)posInitiale.getLigne()] == 'P') {
                        if((posFinale.getLigne() == 7)) {
                            etatPartieEchecs.getEtatEchiquier()[(int) posInitiale.getColonne() - 97][posInitiale.getLigne()] = ' ';
                            etatPartieEchecs.getEtatEchiquier()[(int) posFinale.getColonne() - 97][posFinale.getLigne()] = 'D';
                            clientChat.setEtatPartieEchecs(etatPartieEchecs);
                            ecrireEtatPartie(arguments, clientChat);
                            break;
                        }
                    }
                    else if(etatPartieEchecs.getEtatEchiquier()[(int)posInitiale.getColonne() - 97][(int)posInitiale.getLigne()] == 'p') {
                        if((posFinale.getLigne() == 0)) {
                            etatPartieEchecs.getEtatEchiquier()[(int) posInitiale.getColonne() - 97][posInitiale.getLigne()] = ' ';
                            etatPartieEchecs.getEtatEchiquier()[(int) posFinale.getColonne() - 97][posFinale.getLigne()] = 'd';
                            clientChat.setEtatPartieEchecs(etatPartieEchecs);
                            ecrireEtatPartie(arguments, clientChat);
                            break;
                        }
                    }

                    // ROQUE
                    if(etatPartieEchecs.getEtatEchiquier()[(int)posInitiale.getColonne()-97][(int)posInitiale.getLigne()] == 'R' || etatPartieEchecs.getEtatEchiquier()[(int)posInitiale.getColonne()-97][(int)posInitiale.getLigne()] == 'r' )
                    {
                            if((int)posFinale.getColonne() - (int)posInitiale.getColonne() == 2) //Droite
                            {
                                char tmp = etatPartieEchecs.getEtatEchiquier()[7][posFinale.getLigne()];        //Tour
                                char tmp2 = etatPartieEchecs.getEtatEchiquier()[4][posFinale.getLigne()];
                                etatPartieEchecs.getEtatEchiquier()[7][posFinale.getLigne()] = ' ';
                                etatPartieEchecs.getEtatEchiquier()[5][posFinale.getLigne()] = tmp;
                                         //Roi
                                System.out.println("Valeur de la piece temp: " + tmp);
                                etatPartieEchecs.getEtatEchiquier()[4][posFinale.getLigne()] = ' ';
                                etatPartieEchecs.getEtatEchiquier()[6][posFinale.getLigne()] = tmp2;

                            }
                            else if((int)posFinale.getColonne() - (int)posInitiale.getColonne() == -2)                                                     //Gauche
                            {
                                char tmp = etatPartieEchecs.getEtatEchiquier()[0][posFinale.getLigne()];        //Tour
                                etatPartieEchecs.getEtatEchiquier()[0][posFinale.getLigne()] = ' ';
                                etatPartieEchecs.getEtatEchiquier()[3][posFinale.getLigne()] = tmp;

                                tmp = etatPartieEchecs.getEtatEchiquier()[4][posFinale.getLigne()];              //Roi
                                etatPartieEchecs.getEtatEchiquier()[4][posFinale.getLigne()] = ' ';
                                etatPartieEchecs.getEtatEchiquier()[2][posFinale.getLigne()] = tmp;
                            }
                        clientChat.setEtatPartieEchecs(etatPartieEchecs);
                        ecrireEtatPartie(arguments, clientChat);

                        break;
                    }

                    int iFinal = 0;
                    int jFinal = 0;
                    for(int i = 0; i < etatPartieEchecs.getEtatEchiquier().length; ++i) {
                        for(int j = 0; j < etatPartieEchecs.getEtatEchiquier()[0].length; ++j) {
                            if (i == posInitiale.getLigne() && j == (int)posInitiale.getColonne() - 97) {
                                piece = etatPartieEchecs.getEtatEchiquier()[j][i];
                                etatPartieEchecs.getEtatEchiquier()[j][i] = ' ';
                            }
                            if(i == posFinale.getLigne() && j == (int)posFinale.getColonne()-97) {
                                iFinal = i;
                                jFinal = j;
                            }
                        }
                    }
                    etatPartieEchecs.getEtatEchiquier()[jFinal][iFinal] = piece;

                    clientChat.setEtatPartieEchecs(etatPartieEchecs);
                    ecrireEtatPartie(arguments, clientChat);

                    break;

                case "MAT":
                    clientChat.setEtatPartieEchecs(null);
                    break;

                case "ABANDON":
                    clientChat.setEtatPartieEchecs(null);
                    String[] message = evenement.getArgument().split(" ");
                    System.out.println(message[0] + " à abandonné la partie d'échecs");
                    break;

                default: //Afficher le texte recu :
                    System.out.println("\t\t\t."+evenement.getType()+" "+evenement.getArgument());
            }
        }
    }
    private void ecrireEtatPartie(String[] arguments, ClientChat clientChat) {
        System.out.println();
        System.out.println("C'est au tour de: " + arguments[1] + " (" + (arguments[2].equals("b") ? "Blancs" : "Noirs") + ")");
        System.out.println("---------------------------------");
        System.out.println(clientChat.getEtatPartieEchecs());
        System.out.println("---------------------------------");
        System.out.println("Entrez MOVE xxyy / xx-yy / xx yy pour faire un deplacement:");
        System.out.println();
        System.out.println(arguments[3].equals("e") ? "" : arguments[3] + " est en echec!!!");
        if(!arguments[4].equals("e")) {
            System.out.println("PARTIE TERMINEE");
            System.out.println(arguments[4] + " a gagner!!!");
        }
    }
}
