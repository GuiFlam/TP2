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
        //Deplacement en X et Y
        int depX = (int)pos2.getColonne() - (int)pos1.getColonne();
        int depY = pos2.getLigne() - pos1.getLigne();

        // Verifie si deplacement est diagonale
        if(!pos1.estSurLaMemeDiagonaleQue(pos2))
        {
            return false;
        }

        int incrementX = depX > 0 ? 1 : -1;
        int incrementY = depY > 0 ? 1 : -1;

        int colonne = (int)pos1.getColonne()-97 + incrementX;
        int ligne = pos1.getLigne() + incrementY;

        while(colonne != (int)pos2.getColonne()-97)
        {
            if(echiquier[colonne][ligne] != null)
            {
                return false;
            }
            colonne += incrementX;
            ligne += incrementY;
        }
        return true;
    }
}