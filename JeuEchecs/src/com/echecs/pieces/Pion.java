package com.echecs.pieces;

import com.echecs.Position;

public class Pion extends Piece
{
    public Pion(char couleur) {
        super(couleur);
    }
    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {
        if(couleur == 'b' && pos1.getLigne() == 1 && pos2.getLigne() - pos1.getLigne() <= 2) {        //Move depart Blancs
            return true;
        }
        else if(couleur == 'n' && pos1.getLigne() == 6 && pos1.getLigne() - pos2.getLigne() <= 2) {       //Move depart Noirs
            return true;
        }
        else if(mouvementVertical(pos1, pos2, echiquier)) {
            return true;
        }
        else if(capture(pos1, pos2, echiquier)) {     //Capturer
            return true;
        }
        else if(couleur == 'b' && Math.abs(pos2.getLigne() - pos1.getLigne()) == 1 && pos2.getLigne() == 7     //Promotion reine Blancs
                && pos1.getColonne() == pos2.getColonne() && echiquier[(int)pos2.getColonne()-97][pos2.getLigne()] == null){

           // echiquier[(int)pos2.getColonne()-97][pos2.getLigne()] = new Dame('b');
            return true;
        }
        else if(couleur == 'n' && Math.abs(pos2.getLigne() - pos1.getLigne()) == 1 && pos2.getLigne() == 0     //Promotion reine Noirs
                && pos1.getColonne() == pos2.getColonne() && echiquier[(int)pos2.getColonne()-97][pos2.getLigne()] == null){

          //  echiquier[(int)pos2.getColonne()-97][pos2.getLigne()] = new Dame('n');
            return true;
        }
        return false;
    }
    public static boolean  capture(Position pos1, Position pos2, Piece[][] echiquier) {
    return Math.abs((int)pos2.getColonne() - (int)pos1.getColonne()) == 1  && Math.abs(pos2.getLigne() - pos1.getLigne()) == 1
            && echiquier[(int)pos2.getColonne()-97][pos2.getLigne()] != null;
    }
    public static boolean mouvementVertical(Position pos1, Position pos2, Piece[][] echiquier) {
        return Math.abs(pos2.getLigne() - pos1.getLigne()) == 1 && pos1.getColonne() == pos2.getColonne()      //Mouvement vertical standard
                && echiquier[(int)pos2.getColonne()-97][pos2.getLigne()] == null;
    }
}
