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

    /*
    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier)
    {
        if(pos1.estSurLaMemeColonneQue(pos2))                  //Deplacement verticale (Sont sur la meme colonne)
        {
            for(int i = pos1.getLigne() < pos2.getLigne() ? pos1.getLigne() + 1 : pos1.getLigne() - 1;
                    pos1.getLigne() < pos2.getLigne() ? i < pos2.getLigne() : i > pos2.getLigne();
                    i = pos1.getLigne() < pos2.getLigne() ? i + 1 : i - 1)
            {
                if(echiquier[(int)pos1.getColonne()-65][i] != null)             //Passe a travers toute les lignes qui les separe, s'il y a une piece
                {                                                               //deplacement non permis
                    return false;
                }
            }
            return true;
        }

        if(pos1.estSurLaMemeLigneQue(pos2))                  //Deplacement horizontale (Sont sur la meme ligne)
        {
            for(int i = pos1.getColonne() < pos2.getColonne() ? ((int)pos1.getColonne()-65) + 1 : ((int)pos1.getColonne()-65) - 1;
                pos1.getColonne() < pos2.getColonne() ? i < (int)pos2.getColonne()-65 : i > (int)pos2.getColonne()-65;
                i = pos1.getColonne() < pos2.getColonne() ? i + 1 : i - 1)
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
     */
}