package com.echecs;

import com.echecs.pieces.*;
import com.echecs.util.EchecsUtil;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;

/**
 * Représente une partie de jeu d'échecs. Orcheste le déroulement d'une partie :
 * déplacement des pièces, vérification d'échec, d'échec et mat,...
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class PartieEchecs {
    /**
     * Grille du jeu d'échecs. La ligne 0 de la grille correspond à la ligne
     * 8 de l'échiquier. La colonne 0 de la grille correspond à la colonne a
     * de l'échiquier.
     */
    private Piece[][] echiquier;
    private String aliasJoueur1, aliasJoueur2;
    private char couleurJoueur1, couleurJoueur2;

    static private boolean tour1BlancBouge, tour2BlancBouge, roiBlancBouge, tour1NoirBouge, tour2NoirBouge, roiNoirBouge;



    /**
     * La couleur de celui à qui c'est le tour de jouer (n ou b).
     */
    private char tour = 'b'; //Les blancs commencent toujours
    /**
     * Crée un échiquier de jeu d'échecs avec les pièces dans leurs positions
     * initiales de début de partie.
     * Répartit au hasard les couleurs n et b entre les 2 joueurs.
     */
    @Override
    public String toString() {
        String str = "";
        for(int i = 0; i < echiquier.length; ++i) {
            for(int j = 0; j < echiquier[0].length; ++j) {
                str += echiquier[j][i] + " ";
            }
            str += "\n";
        }
        return str;
    }
    public PartieEchecs() {
        echiquier = new Piece[8][8];

        char couleurJoueur1 = ((int)(Math.random()*2)+1) == 1 ? 'b' : 'n';
        char couleurJoueur2 = Character.compare(couleurJoueur1, 'b') == 0 ? 'n' : 'b';
        this.couleurJoueur1 = couleurJoueur1;
        this.couleurJoueur2 = couleurJoueur2; 
                                         
        tour = 'b';
        //Placement des pièces :

        // Tours
        echiquier[0][0] = new Tour('b');
        echiquier[7][0] = new Tour('b');
        echiquier[0][7] = new Tour('n');
        echiquier[7][7] = new Tour('n');

        // Cavaliers
        echiquier[1][0] = new Cavalier('b');
        echiquier[6][0] = new Cavalier('b');
        echiquier[1][7] = new Cavalier('n');
        echiquier[6][7] = new Cavalier('n');

        // Fous
        echiquier[2][0] = new Fou('b');
        echiquier[5][0] = new Fou('b');
        echiquier[2][7] = new Fou('n');
        echiquier[5][7] = new Fou('n');

        // Reines
        echiquier[3][0] = new Dame('b');
        echiquier[3][7] = new Dame('n');

        // Roi
        echiquier[4][0] = new Roi('b');
        echiquier[4][7] = new Roi('n');

        // Pions
        placerPions(1);
        placerPions(6);
    }

    private void placerPions(int ligne) {
        int colonne = 0;
        for(int i = 0; i <= 7; ++i) {
            echiquier[colonne++][ligne] = new Pion(ligne == 1 ? 'b' : 'n');
        }
    }

    /**
     * Change la main du jeu (de n à b ou de b à n).
     */
    public void changerTour() {
        if (Character.compare(tour, 'b') == 0)
            tour = 'n';
        else
            tour = 'b';
    }
    /**
     * Tente de déplacer une pièce d'une position à une autre sur l'échiquier.
     * Le déplacement peut échouer pour plusieurs raisons, selon les règles du
     * jeu d'échecs. Par exemples :
     *  Une des positions n'existe pas;
     *  Il n'y a pas de pièce à la position initiale;
     *  La pièce de la position initiale ne peut pas faire le mouvement;
     *  Le déplacement met en échec le roi de la même couleur que la pièce.
     *
     * @param initiale Position la position initiale
     * @param finale Position la position finale
     *
     * @return boolean true, si le déplacement a été effectué avec succès, false sinon
     */
    public boolean deplace(Position initiale, Position finale) {

        // position initiales et finales sont valide
        // Si il y a une piece a la position
        // La couleur de la piece correspond au joueur qui joue
        // A la position finale il n'y a pas une piece de la meme couleur
        // Roque: Si le roi ou la tour a bougé on ne peut pas.

        System.out.println((int)initiale.getColonne());
        System.out.println(initiale.getLigne());
        System.out.println((int)finale.getColonne());
        System.out.println(finale.getLigne());

        if(estLigneValide(initiale) && estLigneValide(finale) && estColonneValide(initiale) && estColonneValide(finale)) {
            if(estUnePiece(initiale)) {
                char couleurPieceInitiale = echiquier[(int)initiale.getColonne() - 97][(int)initiale.getLigne()].getCouleur();

                System.out.println("ON EST RENDU ICI #1");
                System.out.println("Couleur piece initiale: " + couleurPieceInitiale);
                System.out.println("Tour: " + tour);

                if(Character.compare(couleurPieceInitiale, tour) == 0) {
                    System.out.println("ON EST RENDU ICI #1.5");
                    char couleurPieceFinale = ' ';
                    if (echiquier[(int)finale.getColonne() - 97][(int)finale.getLigne()] != null) {
                        System.out.println("ON EST RENDU ICI #2");
                        couleurPieceFinale = echiquier[(int)finale.getColonne() - 97][(int)finale.getLigne()].getCouleur();
                    }
                    System.out.println("ON EST RENDU ICI #3");
                    if(Character.compare(couleurPieceInitiale, couleurPieceFinale) != 0 || Character.compare(couleurPieceInitiale, ' ') == 0) {
                        System.out.println("ON EST RENDU ICI #4");
                        if(Character.compare(tour, 'b') == 0 && !roiBlancBouge) {
                            if((int)finale.getColonne() - (int)initiale.getColonne() == 2) {
                                // roque avec tour de droite
                                if(!tour2BlancBouge) {
                                    // ROQUE
                                    echiquier[1][6] = echiquier[1][7];
                                    echiquier[1][7] = echiquier[1][4];
                                    return true;
                                }
                            }
                            else if ((int)finale.getColonne() - (int)initiale.getColonne() == -2) {
                                // roque avec tour de gauche
                                if(!tour1BlancBouge) {
                                    // ROQUE
                                    echiquier[1][2] = echiquier[1][0];
                                    echiquier[1][0] = echiquier[1][4];
                                    return true;
                                }
                            }
                        }
                        else if (!roiNoirBouge){
                            if((int)finale.getColonne() - (int)initiale.getColonne() == 2) {
                                // roque avec tour de droite
                                if(!tour2NoirBouge) {
                                    // ROQUE
                                    echiquier[7][6] = echiquier[7][7];
                                    echiquier[7][7] = echiquier[7][4];
                                    return true;
                                }
                            }
                            else if ((int)finale.getColonne() - (int)initiale.getColonne() == -2) {
                                // roque avec tour de gauche
                                if(!tour1NoirBouge) {
                                    // ROQUE
                                    echiquier[7][2] = echiquier[7][0];
                                    echiquier[7][0] = echiquier[7][4];
                                    return true;
                                }
                            }
                        }

                        if(echiquier[(int)initiale.getColonne()-97][(int)initiale.getLigne()].peutSeDeplacer(initiale, finale, echiquier)) {
                            Piece piece = echiquier[(int)initiale.getColonne()-97][(int)initiale.getLigne()];
                            echiquier[(int)initiale.getColonne()-97][(int)initiale.getLigne()] = null;
                            echiquier[(int)finale.getColonne()-97][(int)finale.getLigne()] = piece;
                            return true;
                        }

                    }
                }
            }
        }
        return false;
    }

    private boolean estLigneValide(Position ligne) {
        return ligne.getLigne() > 0 && ligne.getLigne() < 9;
    }
    private boolean estColonneValide(Position colonne) {
        return (int)colonne.getColonne() > 96 && (int)colonne.getColonne() < 105;
    }
    private boolean estUnePiece(Position position) {
        return echiquier[(int)position.getColonne() - 97][position.getLigne()] != null;
    }
    /**
     * Vérifie si un roi est en échec et, si oui, retourne sa couleur sous forme
     * d'un caractère n ou b.
     * Si la couleur du roi en échec est la même que celle de la dernière pièce
     * déplacée, le dernier déplacement doit être annulé.
     * Les 2 rois peuvent être en échec en même temps. Dans ce cas, la méthode doit
     * retourner la couleur de la pièce qui a été déplacée en dernier car ce
     * déplacement doit être annulé.
     *
     * @return char Le caractère n, si le roi noir est en échec, le caractère b,
     * si le roi blanc est en échec, tout autre caractère, sinon.
     */
    public char estEnEchec() {
            
        char caractere = 'e';
        // Pour chacune des pièces de l'adversaire, si peutsedeplacer à la position du roi est vrai, le roi est en echec. 
        Position positionRoiBlanc = null;
        Position positionRoiNoir = null;
        for(int i = 0; i < echiquier.length; ++i) {
            for(int j = 0; j < echiquier[0].length; ++j) {
                if (echiquier[i][j] != null) {
                    if(echiquier[i][j] instanceof Roi) {
                        if(Character.compare(echiquier[i][j].getCouleur(),'b') == 0) {
                            positionRoiBlanc = new Position((char)(i + 97), (byte)j);
                        }
                        else {
                            positionRoiNoir = new Position((char)(i + 97), (byte)j);
                        }
                    }
                    if (echiquier[i][j].peutSeDeplacer(new Position((char)(i + 97), (byte)j), positionRoiBlanc, echiquier)) {
                        caractere = 'b';
                    }
                    else if (echiquier[i][j].peutSeDeplacer(new Position((char)(i + 97), (byte)j), positionRoiNoir, echiquier)) {
                        caractere = 'n';
                    }
                } 
            }
        }
        return caractere;
    }
    /**
     * Retourne la couleur n ou b du joueur qui a la main.
     *
     * @return char la couleur du joueur à qui c'est le tour de jouer.
     */
    public char getTour() {
        return tour;
    }
    /**
     * Retourne l'alias du premier joueur.
     * @return String alias du premier joueur.
     */
    public String getAliasJoueur1() {
        return aliasJoueur1;
    }
    /**
     * Modifie l'alias du premier joueur.
     * @param aliasJoueur1 String nouvel alias du premier joueur.
     */
    public void setAliasJoueur1(String aliasJoueur1) {
        this.aliasJoueur1 = aliasJoueur1;
    }
    /**
     * Retourne l'alias du deuxième joueur.
     * @return String alias du deuxième joueur.
     */
    public String getAliasJoueur2() {
        return aliasJoueur2;
    }
    /**
     * Modifie l'alias du deuxième joueur.
     * @param aliasJoueur2 String nouvel alias du deuxième joueur.
     */
    public void setAliasJoueur2(String aliasJoueur2) {
        this.aliasJoueur2 = aliasJoueur2;
    }
    /**
     * Retourne la couleur n ou b du premier joueur.
     * @return char couleur du premier joueur.
     */
    public char getCouleurJoueur1() {
        return couleurJoueur1;
    }
    /**
     * Retourne la couleur n ou b du deuxième joueur.
     * @return char couleur du deuxième joueur.
     */
    public char getCouleurJoueur2() {
        return couleurJoueur2;
    }
}