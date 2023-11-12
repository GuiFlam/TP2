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
        //Deplacement en X et Y
        int depX = (int)pos2.getColonne() - (int)pos1.getColonne();
        int depY = pos2.getLigne() - pos1.getLigne();

        //Deplacement vertical
        if(pos1.estSurLaMemeColonneQue(pos2))
        {
            int incrementY = depY > 0 ? 1 : -1;
            int ligne = pos1.getLigne() + incrementY;

            while(ligne != pos2.getLigne())
            {
                if(echiquier[(int)pos1.getColonne()-97][ligne] != null)
                {
                    return false;
                }
                ligne += incrementY;
            }
            return true;
        }

        //Deplacement horizontale
        if(pos1.estSurLaMemeLigneQue(pos2))
        {
            int incrementX = depX > 0 ? 1 : -1;
            int colonne = (int)pos1.getColonne()-97 + incrementX;

            while(colonne != (int)pos2.getColonne()-97)
            {
                if(echiquier[colonne][pos1.getLigne()] != null)
                {
                    return false;
                }
                colonne += incrementX;
            }
            return true;
        }

        return false;
    }
}