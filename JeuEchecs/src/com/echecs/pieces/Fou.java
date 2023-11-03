package com.echecs.pieces;
import com.echecs.Position;
public class Fou extends Piece
{
    public Fou(char couleur)
    {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier)
    {
        if(pos1.estSurLaMemeDiagonaleQue(pos2))
        {
            for(int i = pos1.getLigne(); i <= pos2.getLigne(); i++)
            {
                for(int j = (int)pos1.getColonne()-65; j <= (int)pos2.getColonne()-65; j++)
                {

                }
            }
        }
        return false;
    }
}
