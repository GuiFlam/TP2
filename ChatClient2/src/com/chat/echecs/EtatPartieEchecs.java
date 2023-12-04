package com.chat.echecs;

import observer.Observable;
import observer.Observateur;
import vue.PanneauEchiquier;

import java.util.Observer;

/**
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-10-01
 */
public class EtatPartieEchecs extends Observable {
    private char[][] etatEchiquier = new char[8][8];
    public EtatPartieEchecs() {
        //Les pions :
        for (int j=0;j<8;j++) {
            etatEchiquier[1][j] = 'p';
            etatEchiquier[6][j] = 'P';
        }
        //Cases vide :
        for (int j=0;j<8;j++)
          for (int i=2;i<6;i++) {
            etatEchiquier[i][j] = ' ';
        }

        //Tours :
        etatEchiquier[0][0] = 't';
        etatEchiquier[0][7] = 't';
        etatEchiquier[7][0] = 'T';
        etatEchiquier[7][7] = 'T';

        //Cavaliers :
        etatEchiquier[0][1] = 'c';
        etatEchiquier[0][6] = 'c';
        etatEchiquier[7][1] = 'C';
        etatEchiquier[7][6] = 'C';

        //Fous :
        etatEchiquier[0][2] = 'f';
        etatEchiquier[0][5] = 'f';
        etatEchiquier[7][2] = 'F';
        etatEchiquier[7][5] = 'F';

        //Dames :
        etatEchiquier[0][3] = 'd';
        etatEchiquier[7][3] = 'D';

        //Rois :
        etatEchiquier[0][4] = 'r';
        etatEchiquier[7][4] = 'R';
    }
    public boolean move(String deplacement) {
        boolean res = false;
        //à compléter


        char[] tableau = deplacement.toCharArray();
        tableau[1] -= 48;
        tableau[tableau.length-1] -= 48;
        System.out.println(tableau[0] + "" + ((int)tableau[1]) + "-" + tableau[3] + "" + ((int)tableau[4]));

        if(estLettreValide(tableau[0]) && estChiffreValide((int)tableau[1]) && estLettreValide(tableau[tableau.length-2]) && estChiffreValide((int)tableau[tableau.length-1])) {
            System.out.print("ici\n");
            tableau[1] = (char)(8 - tableau[1]);
            tableau[tableau.length-1] = (char)(8 - tableau[4]);
            System.out.println((tableau[0]-'a') + "" + ((int)tableau[1]) + "-" + (tableau[tableau.length-2]-'a') + "" + ((int)tableau[4]));
            etatEchiquier[tableau[tableau.length-1]][(int)tableau[tableau.length-2]-'a'] = etatEchiquier[tableau[1]][(int)tableau[0]-'a'];
            etatEchiquier[tableau[1]][(int)tableau[0]-'a'] = ' ';
            if(etatEchiquier[tableau[tableau.length-1]][(int)tableau[tableau.length-2]-'a'] == 'p' && tableau[tableau.length-1] == 0) {
                etatEchiquier[tableau[tableau.length-1]][(int)tableau[tableau.length-2]-'a'] = 'd';
            }
            else if(etatEchiquier[tableau[tableau.length-1]][(int)tableau[tableau.length-2]-'a'] == 'P' && tableau[tableau.length-1] == 7) {
                etatEchiquier[tableau[tableau.length-1]][(int)tableau[tableau.length-2]-'a'] = 'D';
            }
            System.out.println("avant");
            this.notifierObservateurs();
            System.out.println("apres");
            res = true;

        }
        return res;
    }
    private boolean estLettreValide(char c) {
        return (int)c >= 97 && (int)c <= 104;
    }
    private boolean estChiffreValide(int c) {
        return c >= 0 && c <= 7;
    }
    @Override
    public String toString() {
        String s = "";
        for (byte i=0;i<8;i++) {
            s+=(byte)(8-i)+" ";
            for (int j=0;j<8;j++)
                s+=((etatEchiquier[i][j]==' ')?".":etatEchiquier[i][j])+" ";
            s+="\n";
        }
        s+="  ";
        for (char j='a';j<='h';j++)
            s+=j+" ";
        return s;
    }

    public char[][] getEtatEchiquier() {
        return etatEchiquier;
    }

    public void setEtatEchiquier(char[][] etatEchiquier) {
        this.etatEchiquier = etatEchiquier;
    }

    public void ajouterObservateur(PanneauEchiquier panneauEchiquier) {
        super.ajouterObservateur(panneauEchiquier);
    }
}
