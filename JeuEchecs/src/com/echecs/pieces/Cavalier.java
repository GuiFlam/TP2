package com.echecs.pieces;
import com.echecs.Position;
public class Cavalier extends Piece
{
    public Cavalier(char couleur)
    {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier)
    {
        //Deplacement en X et Y
        int depX = Math.abs((int)pos2.getColonne() - (int)pos1.getColonne());
        int depY = Math.abs(pos2.getLigne() - pos1.getLigne());

        System.out.print("X : " + depX);
        System.out.print(("Y : " + depY));

        return depX == 1 && depY == 2 || depX == 2 && depY == 1;
    }
}