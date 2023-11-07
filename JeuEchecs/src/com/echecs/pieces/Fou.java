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
        Piece pieceInitiale = echiquier[(int)pos1.getColonne()-65][pos1.getLigne()];            //Verifie s'il y a une piece de meme couleur a pos1 et pos2
        Piece pieceFinale = echiquier[(int)pos2.getColonne()-65][pos2.getLigne()];
        if(Character.compare(pieceInitiale.getCouleur(), pieceFinale.getCouleur()) == 0)
        {
            return false;
        }



        return false;
    }
}
