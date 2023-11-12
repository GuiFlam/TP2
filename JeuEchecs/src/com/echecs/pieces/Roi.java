package com.echecs.pieces;
import com.echecs.Position;
public class Roi extends Piece
{
    public Roi(char couleur) {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier)
    {
        //Deplacement en X et Y
        int depX = (int)pos2.getColonne() - (int)pos1.getColonne();
        int depY = pos2.getLigne() - pos1.getLigne();

        if(pos1.estVoisineDe(pos2))
        {
            return true;
        }

        return false;
    }
}