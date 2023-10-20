package com.chat.serveur;

import com.chat.commun.evenement.Evenement;
import com.chat.commun.evenement.GestionnaireEvenement;
import com.chat.commun.net.Connexion;

import java.util.ArrayList;
import java.util.List;

/**
 * Cette classe représente un gestionnaire d'événement d'un serveur. Lorsqu'un serveur reçoit un texte d'un client,
 * il crée un événement à partir du texte reçu et alerte ce gestionnaire qui réagit en gérant l'événement.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class GestionnaireEvenementServeur implements GestionnaireEvenement {
    private Serveur serveur;

    private List<Invitation> invitations = new ArrayList<>();
    private List<SalonPrive> salonsPrives = new ArrayList<>();

    /**
     * Construit un gestionnaire d'événements pour un serveur.
     *
     * @param serveur Serveur Le serveur pour lequel ce gestionnaire gère des événements
     */
    public GestionnaireEvenementServeur(Serveur serveur) {
        this.serveur = serveur;
    }

    /**
     * Méthode de gestion d'événements. Cette méthode contiendra le code qui gère les réponses obtenues d'un client.
     *
     * @param evenement L'événement à gérer.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();
        Connexion cnx;
        String msg, typeEvenement, aliasExpediteur;
        ServeurChat serveur = (ServeurChat) this.serveur;

        if (source instanceof Connexion) {
            cnx = (Connexion) source;
            System.out.println("SERVEUR-Recu : " + evenement.getType() + " " + evenement.getArgument());
            typeEvenement = evenement.getType();
            aliasExpediteur = cnx.getAlias();
            String argument = evenement.getArgument();

            switch (typeEvenement) {
                case "EXIT": //Ferme la connexion avec le client qui a envoyé "EXIT":
                    cnx.envoyer("END");
                    serveur.enlever(cnx);
                    cnx.close();
                    break;
                case "LIST": //Envoie la liste des alias des personnes connectées :
                    cnx.envoyer("LIST " + serveur.list());
                    break;

                //Ajoutez ici d’autres case pour gérer d’autres commandes.
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
                            connexion.envoyer(aliasExpediteur + " à refusé votre invitation de chat privé.");
                        }
                    }

                    break;


                case "INV":
                    String liste = "Utilisateurs qui vous ont invités: \n";
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
                        for(int i = 0; i < this.invitations.size(); ++i) {
                            if((this.invitations.get(i).getAliasHote().equals(aliasExpediteur) && this.invitations.get(i).getAliasInvite().equals(arguments[0])) || this.invitations.get(i).getAliasHote().equals(arguments[0]) && this.invitations.get(i).getAliasInvite().equals(aliasExpediteur)) {
                                for(int j = 0; j < serveur.connectes.size(); ++j) {
                                    Connexion connexion = serveur.connectes.elementAt(j);
                                    if(connexion.getAlias().equals(arguments[0])) {
                                        String message = "";
                                        for(int k = 2; k < arguments.length; ++k) {
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
                connexion.envoyer(aliasExpediteur + " vous invite dans un salon privé. Pour accepter, écrivez: JOIN " + aliasExpediteur + " ou DECLINE pour refuser ");
            }
        }
    }

}