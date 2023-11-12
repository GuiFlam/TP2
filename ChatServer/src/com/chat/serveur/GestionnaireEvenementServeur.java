package com.chat.serveur;

import com.chat.client.Client;
import com.chat.client.ClientChat;
import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;
import com.echecs.PartieEchecs;
import com.echecs.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe repr�sente un gestionnaire d'�v�nement d'un serveur. Lorsqu'un serveur re�oit un texte d'un client,
 * il cr�e un �v�nement � partir du texte re�u et alerte ce gestionnaire qui r�agit en g�rant l'�v�nement.
 *
 * @author Abdelmoum�ne Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;

    private List<Invitation> invitations = new ArrayList<>();
    private List<SalonPrive> salonsPrives = new ArrayList<>();

    /**
     * Construit un gestionnaire d'�v�nements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire g�re des �v�nements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * M�thode de gestion d'�v�nements. Cette m�thode contiendra le code qui g�re les r�ponses obtenues d'un client.
     *
     * @param evenement L'�v�nement � g�rer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        Connexion cnx;
        String msg, typeEvenement, aliasExpediteur;
        ServeurChat serveur = (ServeurChat) this.serveur;

        if (source  instanceof Connexion) {
            cnx = (Connexion) source;
            System.out.println("SERVEUR-Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            aliasExpediteur = cnx.getAlias();
            String argument = evenement.getArgument();

            switch (typeEvenement) {
                case "EXIT": //Ferme la connexion avec le client qui a envoy� "EXIT":
                    cnx.envoyer("END");
                    serveur.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des alias des personnes connect�es :
                    cnx.envoyer("LIST " + serveur.list());
                    break;

                //Ajoutez ici d�autres case pour g�rer d�autres commandes.
                case "MSG":
                    serveur.envoyerATousSauf(argument, aliasExpediteur);
                    serveur.ajouterHistorique(aliasExpediteur + ">>" + argument);
                    break;


                case "HIST":
                    cnx.envoyer(serveur.historique());
                    break;

                case "JOIN":
                    if(invitationDejaEnvoye(argument, aliasExpediteur)) {
                        salonsPrives.add(new SalonPrive(argument, aliasExpediteur));
                    }
                    else {
                        boolean existe = false;
                        for(int i = 0; i < this.invitations.size(); ++i) {
                            if(this.invitations.get(i).getAliasHote().equals(aliasExpediteur) && this.invitations.get(i).getAliasInvite().equals(argument)) {
                                existe = true;
                            }
                        }
                        if(!existe) {
                            this.invitations.add(new Invitation(aliasExpediteur, argument));
                            envoyerInvitation(aliasExpediteur, argument);
                        }

                    }
                    break;


                case "DECLINE":
                    for(int i = 0; i < this.invitations.size(); ++i) {
                        if(this.invitations.get(i).getAliasHote().equals(argument) && this.invitations.get(i).getAliasInvite().equals(aliasExpediteur)) {
                            this.invitations.remove(i);
                        }
                    }
                    for(int i = 0; i < serveur.connectes.size(); ++i) {
                        Connexion connexion = serveur.connectes.elementAt(i);
                        if(connexion.getAlias().equals(argument)) {
                            connexion.envoyer(aliasExpediteur + " à refusé votre invitation");
                        }
                    }

                    break;


                case "INV":
                    String liste = "Utilisateurs qui vous ont invit�s: \n";
                    for(int i = 0; i < this.invitations.size(); ++i) {
                        if(this.invitations.get(i).getAliasInvite().equals(aliasExpediteur)) {
                            liste += (this.invitations.get(i).getAliasHote() + "\n");
                        }
                    }
                    cnx.envoyer(liste);
                    break;


                case "PRV":
                    String[] arguments = argument.split(" ");

                    if(!arguments[0].equals(aliasExpediteur)) {
                        for(int i = 0; i < this.salonsPrives.size(); ++i) {
                            if((this.salonsPrives.get(i).getAliasHote().equals(aliasExpediteur) && this.salonsPrives.get(i).getAliasInvite().equals(arguments[0])) || this.salonsPrives.get(i).getAliasHote().equals(arguments[0]) && this.salonsPrives.get(i).getAliasInvite().equals(aliasExpediteur)) {
                                for(int j = 0; j < serveur.connectes.size(); ++j) {
                                    Connexion connexion = serveur.connectes.elementAt(j);
                                    if(connexion.getAlias().equals(arguments[0])) {
                                        String message = "";
                                        for(int k = 1; k < arguments.length; ++k) {
                                            message += arguments[k] + " ";
                                        }
                                        connexion.envoyer(aliasExpediteur + ": " + message);
                                    }
                                }
                            }
                        }
                    }

                    break;


                case "QUIT":
                    for(int i = 0; i < this.salonsPrives.size(); ++i) {
                        if((this.invitations.get(i).getAliasHote().equals(aliasExpediteur) && this.invitations.get(i).getAliasInvite().equals(argument))) {
                            this.salonsPrives.get(i).setAliasHote("");
                        }
                        else {
                            this.salonsPrives.get(i).setAliasInvite("");
                        }
                        if(this.salonsPrives.get(i).getAliasHote().equals("") && this.salonsPrives.get(i).equals("")) {
                            this.salonsPrives.remove(i);
                        }
                    }


                case "CHESS":
                    // inviter argument à jouer une partie d'échec.
                    // sert aussi à alias1 à accepter une invitation de alias 2

                        for(int i = 0; i < this.salonsPrives.size(); ++i) {
                            if(this.salonsPrives.get(i).getAliasHote().equals(argument) || this.salonsPrives.get(i).getAliasHote().equals(aliasExpediteur)) {
                                this.salonsPrives.get(i).setPartieEchecs(new PartieEchecs());

                                this.salonsPrives.get(i).getPartieEchecs().setAliasJoueur1(argument);
                                this.salonsPrives.get(i).getPartieEchecs().setAliasJoueur2(aliasExpediteur);

                                for(int j = 0; j < serveur.connectes.size(); ++j) {
                                     Connexion connexion = serveur.connectes.elementAt(j);
                                     
                                     if(connexion.getAlias().equals(aliasExpediteur)) {

                                        connexion.envoyer("CHESSOK " + this.salonsPrives.get(i).getPartieEchecs().getCouleurJoueur2());
                                     }
                                     if(connexion.getAlias().equals(argument)) {

                                        connexion.envoyer("CHESSOK " + this.salonsPrives.get(i).getPartieEchecs().getCouleurJoueur1());
                                     }
                                 }

                            }

                        
                    }
                        boolean existe = false;
                        for(int i = 0; i < this.invitations.size(); ++i) {
                            if(this.invitations.get(i).getAliasHote().equals(aliasExpediteur) && this.invitations.get(i).getAliasInvite().equals(argument)) {
                                existe = true;
                            }
                        }
                        if(!existe) {
                            this.invitations.add(new Invitation(aliasExpediteur, argument));
                            envoyerInvitation(aliasExpediteur, argument);
                        }


                    break;

                case "MOVE":
                    char[] message = argument.toCharArray();
                    Position posInitiale = null;
                    Position posFinale = null;
                    if(message.length == 4) {
                        posInitiale = new Position(message[0], (byte)((int)(message[1])-49));
                        posFinale = new Position(message[2], (byte)((int)(message[3])-49));
                    }
                    else if(message.length == 5) {
                        posInitiale = new Position(message[0], (byte)((int)(message[1])-49));
                        posFinale = new Position(message[3], (byte)((int)(message[4])-49));
                    }

                    for(int i = 0; i < this.salonsPrives.size(); i++) {
                        if(this.salonsPrives.get(i).getAliasHote().equals(aliasExpediteur) || this.salonsPrives.get(i).getAliasInvite().equals(aliasExpediteur)) {
                            //System.out.println(this.salonsPrives.get(i).getPartieEchecs());

                            if(this.salonsPrives.get(i).getPartieEchecs().deplace(posInitiale, posFinale)) {
                                this.salonsPrives.get(i).getPartieEchecs().changerTour();
                                for(int j = 0; j < serveur.connectes.size(); ++j) {
                                    Connexion connexion = serveur.connectes.elementAt(j);
                                    if(connexion.getAlias().equals(this.salonsPrives.get(i).getAliasHote()) || connexion.getAlias().equals(this.salonsPrives.get(i).getAliasInvite())) {

                                        char tour = this.salonsPrives.get(i).getPartieEchecs().getTour();
                                        String aliasJoueurActuel = tour == this.salonsPrives.get(i).getPartieEchecs().getCouleurJoueur1() ? this.salonsPrives.get(i).getPartieEchecs().getAliasJoueur1() : this.salonsPrives.get(i).getPartieEchecs().getAliasJoueur2();
                                        connexion.envoyer("MOVE " + argument + " " + aliasJoueurActuel + " " + tour);
                                        if(this.salonsPrives.get(i).getPartieEchecs().estEnEchec() == 'b' || this.salonsPrives.get(i).getPartieEchecs().estEnEchec() == 'n') {
                                            String aliasJoueurEchec = "";
                                            if(this.salonsPrives.get(i).getPartieEchecs().getCouleurJoueur1() == 'b') {
                                                aliasJoueurEchec = this.salonsPrives.get(i).getPartieEchecs().getAliasJoueur1();
                                            }
                                            else {
                                                aliasJoueurEchec = this.salonsPrives.get(i).getPartieEchecs().getAliasJoueur2();
                                            }
                                            connexion.envoyer("ECHEC " + aliasJoueurEchec);
                                            if(this.salonsPrives.get(i).getPartieEchecs().echecEtMat() == this.salonsPrives.get(i).getPartieEchecs().estEnEchec()) {
                                                connexion.envoyer("MAT " + (aliasJoueurEchec.equals(this.salonsPrives.get(i).getPartieEchecs().getAliasJoueur1()) ? this.salonsPrives.get(i).getPartieEchecs().getAliasJoueur2() : this.salonsPrives.get(i).getPartieEchecs().getAliasJoueur1()));
                                            }
                                        }
                                    }
                                }
                            }
                            else {
                                for(int j = 0; j < serveur.connectes.size(); ++j) {
                                    Connexion connexion = serveur.connectes.elementAt(j);

                                    if(connexion.getAlias().equals(this.salonsPrives.get(i).getAliasHote()) || connexion.getAlias().equals(this.salonsPrives.get(i).getAliasInvite())) {

                                        connexion.envoyer("INVALID");
                                    }
                                }
                            }
                        }
                    }



                    break;

                case "ABANDON":




                    break;
                default: //Renvoyer le texte recu convertit en majuscules :
                    msg = (evenement.getType() + " " + evenement.getArgument()).toUpperCase();
                    cnx.envoyer(msg);
            }
        }
    }

    private boolean invitationDejaEnvoye(String aliasHote, String aliasInvite) {
        if(this.invitations.size() > 0) {
            for(int i = 0; i < this.invitations.size(); ++i) {
                if(this.invitations.get(i).getAliasHote().equals(aliasHote) && this.invitations.get(i).getAliasInvite().equals(aliasInvite)) {
                    this.invitations.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    private void envoyerInvitation(String aliasExpediteur, String aliasInvite) {
        for(int i = 0; i < serveur.connectes.size(); ++i) {
            Connexion connexion = serveur.connectes.elementAt(i);
            if(connexion.getAlias().equals(aliasInvite)) {
                connexion.envoyer(aliasExpediteur + " vous invite dans un salon priv�. Pour accepter, �crivez: JOIN " + aliasExpediteur + " ou DECLINE pour refuser ");
            }
        }
    }

}