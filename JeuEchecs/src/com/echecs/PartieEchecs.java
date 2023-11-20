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

        // Positions initiales et finales valides
        if(estLigneValide(initiale) && estLigneValide(finale) && estColonneValide(initiale) && estColonneValide(finale)) {
            // Si il y a une piece a la position initiale
            if(estUnePiece(initiale)) {

                char couleurPieceInitiale = echiquier[(int)initiale.getColonne() - 97][(int)initiale.getLigne()].getCouleur();

                // la couleur de la piece est bien la couleur du joueur a qui son tour
                if(Character.compare(couleurPieceInitiale, tour) == 0) {
                    char couleurPieceFinale = ' ';
                    if (echiquier[(int)finale.getColonne() - 97][(int)finale.getLigne()] != null) {
                        couleurPieceFinale = echiquier[(int)finale.getColonne() - 97][(int)finale.getLigne()].getCouleur();
                    }
                    // si la piece a la position finale n'est pas la meme couleur que la piece a la position initiale
                    if(Character.compare(couleurPieceInitiale, couleurPieceFinale) != 0 || Character.compare(couleurPieceInitiale, ' ') == 0) {

                        // PROMOTION
                        if(echiquier[(int)initiale.getColonne() - 97][(int)initiale.getLigne()] instanceof Pion) {
                            if(((Pion.capture(initiale, finale, echiquier) || Pion.mouvementVertical(initiale, finale, echiquier)) && finale.getLigne() == 7 && couleurPieceInitiale == 'b')) {
                                echiquier[(int)initiale.getColonne()-97][initiale.getLigne()] = null;
                                echiquier[(int)finale.getColonne()-97][finale.getLigne()] = new Dame('b');
                                return true;
                            }
                            else if(((Pion.capture(initiale, finale, echiquier) || Pion.mouvementVertical(initiale, finale, echiquier)) && finale.getLigne() == 0 && couleurPieceInitiale == 'n')) {
                                echiquier[(int)initiale.getColonne()-97][initiale.getLigne()] = null;
                                echiquier[(int)finale.getColonne()-97][finale.getLigne()] = new Dame('n');
                                return true;
                            }
                        }

                        // ROQUE
                        if(echiquier[(int)initiale.getColonne()-97][(int)initiale.getLigne()] instanceof Roi)
                        {
                            if(echiquier[(int)initiale.getColonne()-97][(int)initiale.getLigne()].getCouleur() == 'b')   //BLANCS
                            {
                                if((int)finale.getColonne() - (int)initiale.getColonne() == 2) //Droite
                                {
                                    if(echiquier[(int)initiale.getColonne()-96][initiale.getLigne()] == null && echiquier[(int)initiale.getColonne()-95][initiale.getLigne()] == null &&
                                            !tour2BlancBouge && !roiBlancBouge)
                                    {


                                        Piece tmp = echiquier[7][0];        //Tour
                                        echiquier[7][0] = null;
                                        echiquier[5][0] = tmp;

                                        tmp = echiquier[4][0];              //Roi
                                        echiquier[4][0] = null;
                                        echiquier[6][0] = tmp;

                                        if(estEnEchec() == 'b') {
                                            Piece piece = echiquier[5][0];        //Tour
                                            echiquier[5][0] = null;
                                            echiquier[7][0] = piece;

                                            piece = echiquier[6][0];              //Roi
                                            echiquier[6][0] = null;
                                            echiquier[4][0] = piece;
                                            return false;
                                        }

                                        return true;
                                    }
                                }
                                else if((int)finale.getColonne() - (int)initiale.getColonne() == -2)                                                     //Gauche
                                {
                                    if(echiquier[(int)initiale.getColonne()-98][initiale.getLigne()] == null && echiquier[(int)initiale.getColonne()-99][initiale.getLigne()] == null &&
                                            echiquier[(int)initiale.getColonne()-100][initiale.getLigne()] == null && !tour1BlancBouge && !roiBlancBouge)
                                    {
                                        Piece tmp = echiquier[0][0];        //Tour
                                        echiquier[0][0] = null;
                                        echiquier[3][0] = tmp;

                                        tmp = echiquier[4][0];              //Roi
                                        echiquier[4][0] = null;
                                        echiquier[2][0] = tmp;

                                        Piece[][] board = echiquier;
                                        if(trouverSiEchec(board) == 'b') {
                                            Piece piece = echiquier[3][0];        //Tour
                                            echiquier[3][0] = null;
                                            echiquier[0][0] = piece;

                                            piece = echiquier[2][0];              //Roi
                                            echiquier[2][0] = null;
                                            echiquier[4][0] = piece;
                                            return false;
                                        }
                                        return true;
                                    }
                                }
                            }
                            else                                                        //NOIR
                            {
                                if((int)finale.getColonne() - (int)initiale.getColonne() == 2) //Droite
                                {
                                    if(echiquier[(int)initiale.getColonne()-96][initiale.getLigne()] == null && echiquier[(int)initiale.getColonne()-95][initiale.getLigne()] == null &&
                                            !tour2NoirBouge && !roiNoirBouge)
                                    {
                                        Piece tmp = echiquier[7][7];        //Tour
                                        echiquier[7][7] = null;
                                        echiquier[5][7] = tmp;

                                        tmp = echiquier[4][7];              //Roi
                                        echiquier[4][7] = null;
                                        echiquier[6][7] = tmp;

                                        Piece[][] board = echiquier;
                                        if(trouverSiEchec(board) == 'n') {
                                            Piece piece = echiquier[5][7];        //Tour
                                            echiquier[5][7] = null;
                                            echiquier[7][7] = piece;

                                            piece = echiquier[6][7];              //Roi
                                            echiquier[6][7] = null;
                                            echiquier[4][7] = piece;
                                            return false;
                                        }
                                        return true;
                                    }
                                }
                                else if((int)finale.getColonne() - (int)initiale.getColonne() == -2)                                                     //Gauche
                                {
                                    if(echiquier[(int)initiale.getColonne()-98][initiale.getLigne()] == null && echiquier[(int)initiale.getColonne()-99][initiale.getLigne()] == null &&
                                            echiquier[(int)initiale.getColonne()-100][initiale.getLigne()] == null && !tour1NoirBouge && !roiNoirBouge)
                                    {
                                        Piece tmp = echiquier[0][7];        //Tour
                                        echiquier[0][7] = null;
                                        echiquier[3][7] = tmp;

                                        tmp = echiquier[4][7];              //Roi
                                        echiquier[4][7] = null;
                                        echiquier[2][7] = tmp;

                                        Piece[][] board = echiquier;
                                        if(trouverSiEchec(board) == 'n') {
                                            Piece piece = echiquier[3][7];        //Tour
                                            echiquier[3][7] = null;
                                            echiquier[0][7] = piece;

                                            piece = echiquier[2][7];              //Roi
                                            echiquier[4][7] = null;
                                            echiquier[4][7] = piece;
                                            return false;
                                        }
                                        return true;
                                    }
                                }
                            }
                        }

                        if(echiquier[(int)initiale.getColonne()-97][(int)initiale.getLigne()].peutSeDeplacer(initiale, finale, echiquier)) {
                            if(echiquier[(int)initiale.getColonne()-97][(int)initiale.getLigne()] instanceof Roi)
                            {
                                if(echiquier[(int)initiale.getColonne()-97][(int)initiale.getLigne()].getCouleur() == 'b')
                                {
                                    if(!roiBlancBouge)
                                    {
                                        roiBlancBouge = true;
                                    }
                                }
                                else if(!roiNoirBouge)
                                {
                                    roiNoirBouge = true;
                                }
                            }

                            if(echiquier[(int)initiale.getColonne()-97][(int)initiale.getLigne()] instanceof Tour)
                            {
                                if(echiquier[(int)initiale.getColonne()-97][(int)initiale.getLigne()].getCouleur() == 'b')
                                {
                                    if (!tour1BlancBouge && (int) initiale.getColonne() - 97 == 0 && initiale.getLigne() == 0)
                                    {
                                        tour1BlancBouge = true;
                                    }
                                    else if(!tour2BlancBouge && (int) initiale.getColonne() - 97 == 7 && initiale.getLigne() == 0)
                                    {
                                        tour2BlancBouge = true;
                                    }
                                }
                                else
                                {
                                    if(!tour1NoirBouge && (int) initiale.getColonne() - 97 == 0 && initiale.getLigne() == 7)
                                    {
                                        tour1NoirBouge = true;
                                    }
                                    else if(!tour2NoirBouge && (int) initiale.getColonne() - 97 == 7 && initiale.getLigne() == 7)
                                    {
                                        tour2NoirBouge = true;
                                    }
                                }
                            }

                            Piece[][] board = echiquier;
                            Piece piece = board[(int) initiale.getColonne() - 97][(int) initiale.getLigne()];
                            board[(int) initiale.getColonne() - 97][(int) initiale.getLigne()] = null;
                            board[(int) finale.getColonne() - 97][(int) finale.getLigne()] = piece;

                            if (couleurPieceInitiale == estEnEchec() || couleurPieceInitiale == trouverSiEchec(board)) {
                                return false;
                            }
                            echiquier[(int)initiale.getColonne()-97][(int)initiale.getLigne()] = null;
                            echiquier[(int)finale.getColonne()-97][(int)finale.getLigne()] = piece;

                            changerTour();
                            
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean estLigneValide(Position ligne) {
        return ligne.getLigne() >= 0 && ligne.getLigne() < 9;
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
        return trouverSiEchec(echiquier);
    }
    private char trouverSiEchec(Piece[][] board) {
        char caractere = ' ';
        // Pour chacune des pièces de l'adversaire, si peutsedeplacer à la position du roi est vrai, le roi est en echec.
        Position positionRoiBlanc = null;
        Position positionRoiNoir = null;
        for(int i = 0; i < board.length; ++i) {
            for(int j = 0; j < board[i].length; ++j) {
                if (board[i][j] != null) {
                    if(board[i][j] instanceof Roi) {
                        if(Character.compare(board[i][j].getCouleur(),'b') == 0) {
                            positionRoiBlanc = new Position((char)(i + 97), (byte)j);
                        }
                        else {
                            positionRoiNoir = new Position((char)(i + 97), (byte)j);
                        }
                    }
                }
            }
        }
        for(int i = 0; i < board.length; ++i) {
            for (int j = 0; j < board[i].length; ++j) {
                if (board[i][j] != null) {
                    if (board[i][j].peutSeDeplacer(new Position((char)(i + 97), (byte)j), positionRoiBlanc, board) && board[i][j].getCouleur() == 'n') {
                        caractere = 'b';
                    }
                    else if (board[i][j].peutSeDeplacer(new Position((char)(i + 97), (byte)j), positionRoiNoir, board)  && board[i][j].getCouleur() == 'b') {
                        caractere = 'n';
                    }
                }
            }
        }

        return caractere;
    }
    public char echecEtMat() {
        char caractere = 'e';
        // Pour chacune des pièces de l'adversaire, si peutsedeplacer à la position du roi est vrai, le roi est en echec.
        Piece roiNoir = null;
        Piece roiBland = null;
        Position positionRoiBlanc = null;
        Position positionRoiNoir = null;
        boolean roiNoirEchecMat = true;
        boolean roiBlancEchecMat = true;

        if(estEnEchec() == 'n' || estEnEchec() == 'b') {
            for(int i = 0; i < echiquier.length; ++i) {
                for(int j = 0; j < echiquier[0].length; ++j) {
                    if (echiquier[i][j] != null) {
                        if(echiquier[i][j] instanceof Roi) {
                            if(Character.compare(echiquier[i][j].getCouleur(),'b') == 0) {
                                roiBland = echiquier[i][j];
                                positionRoiBlanc = new Position(((char)(i + 97)), (byte)j);
                            }
                            else {
                                roiNoir = echiquier[i][j];
                                positionRoiNoir = new Position(((char)(i + 97)), (byte)j);
                            }
                        }
                    }
                }
            }
            for(int i = 0; i < echiquier.length; ++i) {
                for(int j = 0; j < echiquier[0].length; ++j) {
                    if (echiquier[i][j] != null) {
                        if(roiBland.peutSeDeplacer(positionRoiBlanc, new Position((char)(i + 97), (byte)j), echiquier)) {
                            roiBlancEchecMat = false;
                        }
                        if(roiNoir.peutSeDeplacer(positionRoiNoir, new Position((char)(i + 97), (byte)j), echiquier)) {
                            roiNoirEchecMat = false;
                        }
                    }
                }
            }
            if(roiBlancEchecMat) {
                return 'b';
            }
            if(roiNoirEchecMat) {
                return 'n';
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