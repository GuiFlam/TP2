package com.echecs.pieces;
import com.echecs.Position;
public class Tour extends Piece
{
    public Tour(char couleur)
    {
        super(couleur);
    }

    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier)
    {
        if(pos1.getColonne() > pos2.getColonne())
        {
            if(pos1.estSurLaMemeColonneQue(pos2))       //Meme colonne
            {
                for(int i = pos1.getLigne() + 1; i <= pos2.getLigne(); i++)
                {
                    if(echiquier[(int)pos1.getColonne()-65][i] != null)
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        if(pos1.estSurLaMemeColonneQue(pos2))       //Meme colonne
        {
            for(int i = pos1.getLigne() + 1; i <= pos2.getLigne(); i++)
            {
                if(echiquier[(int)pos1.getColonne()-65][i] != null)
                {
                    return false;
                }
            }
            return true;
        }

        if(pos1.estSurLaMemeLigneQue(pos2))     //Meme ligne
        {
            for(int i = ((int)pos1.getColonne()-65) + 1; i <= pos2.getColonne(); i++)
            {
                if(echiquier[i][pos1.getLigne()] != null)
                {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}