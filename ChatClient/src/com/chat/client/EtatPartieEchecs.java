package com.chat.client;

import com.echecs.pieces.Cavalier;
import com.echecs.pieces.Fou;
import com.echecs.pieces.Pion;
import com.echecs.pieces.Tour;

public class EtatPartieEchecs {
    private char[][] etatEchiquier = new char[8][8];

    public void setEtatEchiquier(char[][] etatEchiquier) {
        this.etatEchiquier = etatEchiquier;
    }

    public char[][] getEtatEchiquier() {
        return this.etatEchiquier;
    }

    public EtatPartieEchecs() {
        for(int i = 0; i < etatEchiquier.length; ++i) {
            for(int j = 0; j < etatEchiquier[0].length; ++j) {
                etatEchiquier[i][j] = ' ';
            }
        }

        // Tours
        etatEchiquier[0][0] = 'T';
        etatEchiquier[7][0] = 'T';
        etatEchiquier[0][7] = 't';
        etatEchiquier[7][7] = 't';

        // Cavaliers
        etatEchiquier[1][0] = 'C';
        etatEchiquier[6][0] = 'C';
        etatEchiquier[1][7] = 'c';
        etatEchiquier[6][7] = 'c';

        // Fous
        etatEchiquier[2][0] = 'F';
        etatEchiquier[5][0] = 'F';
        etatEchiquier[2][7] = 'f';
        etatEchiquier[5][7] = 'f';

        // Reines
        etatEchiquier[3][0] = 'D';
        etatEchiquier[3][7] = 'd';

        // Roi
        etatEchiquier[4][0] = 'R';
        etatEchiquier[4][7] = 'r';

        // Pions
        placerPions(1);
        placerPions(6);
    }
    private void placerPions(int ligne) {
        int colonne = 0;
        for(int i = 0; i <= 7; ++i) {
            etatEchiquier[colonne++][ligne] = ligne == 1 ? 'P' : 'p';
        }
    }
    @Override
    public String toString() {
        String etat = "";
        int ligne;
        int colonne = 65;

        for(ligne = 7; ligne >= 0; --ligne) {
            etat += ligne + 1;
            etat += " ";
            for(colonne = 0; colonne <= 7; ++colonne) {
                etat += etatEchiquier[colonne][ligne] == ' ' ? '.' : etatEchiquier[colonne][ligne];
                etat += " ";
            }
            etat += "\n";
            if(ligne == 0) {
                etat += "  ";
                for(colonne = 0; colonne <= 7; ++colonne) {
                    etat += Character.toLowerCase((char)(colonne+65));
                    etat += " ";
                }
            }
        }
        return etat;
    }
}