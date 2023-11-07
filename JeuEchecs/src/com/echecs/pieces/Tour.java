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
        if(pos1.estSurLaMemeColonneQue(pos2))                  //Deplacement verticale (Sont sur la meme colonne)
        {
            for(int i = pos1.getLigne() + 1; i < pos2.getLigne(); i++)
            {
                if(echiquier[(int)pos1.getColonne()-65][i] != null)             //Passe a travers toute les lignes qui les separe, s'il y a une piece
                {                                                               //deplacement non permis
                    return false;
                }
            }
            return true;
        }

        if(pos1.estSurLaMemeLigneQue(pos2))                  //Deplacement horizontale (Sont sur la meme ligne
        {
            for(int i = ((int)pos1.getColonne()-65) + 1; i < (int)pos2.getColonne() - 65; i++)
            {
                if(echiquier[i][pos1.getLigne()] != null)                          //Passe a travers toute les colonnes qui les separe, s'il y a une piece
                {                                                                  //deplacement non permis
                    return false;
                }
            }
            return true;
        }

        return false;
    }
}