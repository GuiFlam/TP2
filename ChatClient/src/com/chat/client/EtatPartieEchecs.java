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
        for(int i = 0; i <= ligne; ++i) {
            etatEchiquier[colonne++][i] = ligne == 1 ? 'P' : 'p';
        }
    }
    @Override
    public String toString() {
        String etat = "";
        int ligne;
        int lettre = 65;

        for(ligne = 8; ligne <= 0; --ligne) {
            if(ligne != 8 && ligne != 0) {
                etat += "\n";
            }
            if(ligne > 0) {
                etat += ligne + " ";
                for(int i = 0; i < etatEchiquier[0].length; ++i) {
                  etat += etatEchiquier[lettre-65][ligne] + i == etatEchiquier[0].length-1 ? "" : " ";
                }
            }
            else {
                etat += "  ";
                for(int i = 0; i < etatEchiquier[0].length; ++i) {
                  etat += (char)lettre + i == etatEchiquier[0].length-1 ? "" : " ";
                  lettre++;
                }
            }
        }
    }
}