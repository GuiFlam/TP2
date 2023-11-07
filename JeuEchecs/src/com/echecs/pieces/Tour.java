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
        Piece pieceInitiale = echiquier[(int)pos1.getColonne()-65][pos1.getLigne()];            //Verifie s'il y a une piece de meme couleur a pos1 et pos2
        Piece pieceFinale = echiquier[(int)pos2.getColonne()-65][pos2.getLigne()];
        if(Character.compare(pieceInitiale.getCouleur(), pieceFinale.getCouleur()) == 0)
        {
            return false;
        }

        if((int)pos1.getColonne()-65 == (int)pos2.getColonne()-65)                  //Deplacement verticale (Sont sur la meme colonne)
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

        if(pos1.getLigne() == pos2.getLigne())                  //Deplacement horizontale (Sont sur la meme ligne
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