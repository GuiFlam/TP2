package com.echecs.pieces;

import com.echecs.Position;

public class Pion extends Piece
{
    public Pion(char couleur) {
        super(couleur);
    }
    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier) {
        if(Character.compare(couleur, 'b') == 0 && pos1.getLigne() == 1 && pos2.getLigne() - pos1.getLigne() <= 2) {
            return true;
        }
        else if(Character.compare(couleur, 'n') == 0 && pos1.getLigne() == 6 && pos1.getLigne() - pos2.getLigne() <= 2) {
            return true;
        }
        else if(Character.compare(couleur, 'b') == 0 && pos2.getLigne() - pos1.getLigne() == 1    && Character.compare(pos1.getColonne(), pos2.getColonne()) == 0) {
            return true;
        }
        else if(Character.compare(couleur, 'n') == 0 && pos1.getLigne() - pos2.getLigne() == 1    && Character.compare(pos1.getColonne(), pos2.getColonne()) == 0) {
            return true;
        }
        else if(Character.compare(couleur, 'b') == 0    && 
        Math.abs((int)pos1.getColonne() - (int)pos2.getColonne()) == 1  && 
        pos2.getLigne() - pos1.getLigne() == 1) {
            // et que case pas nulle
            return true;
        }
        else if(Character.compare(couleur, 'n') == 0    && 
                Math.abs((int)pos1.getColonne() - (int)pos2.getColonne()) == 1  && 
                pos1.getLigne() - pos2.getLigne() == 1) {
            // et que case pas nulle
            return true;
        }
        return false;
    }
}
