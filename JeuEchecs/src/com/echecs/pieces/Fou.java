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
        int depX = pos2.getColonne() - pos1.getColonne();
        int depY = pos2.getLigne() - pos1.getLigne();

        // Verifie si deplacement est diagonale
        if(!pos1.estSurLaMemeDiagonaleQue(pos2))
        {
            return false;
        }

        int incrementX = depX > 0 ? 1 : -1;
        int incrementY = depY > 0 ? 1 : -1;

        int colonne = (int)pos1.getColonne()-65 + incrementX;
        int ligne = pos1.getLigne() + incrementY;

        while(colonne != (int)pos2.getColonne()-65)
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

    /*
    @Override
    public boolean peutSeDeplacer(Position pos1, Position pos2, Piece[][] echiquier)
    {
        Position posTemp;

        if(pos1.getColonne() < pos2.getColonne() && pos1.getLigne() < pos2.getLigne())      //Cas que pos1 < pos2
        {
            for(int colonne = 0; colonne < echiquier.length; colonne++)
            {
                for(int ligne = 0; ligne < echiquier[0].length; ligne++)
                {
                    posTemp = new Position((char)(colonne+65),(byte)ligne);

                    if(     posTemp.estSurLaMemeDiagonaleQue(pos1) && posTemp.estSurLaMemeDiagonaleQue(pos2) &&          //Meme diagonale
                            posTemp.getLigne() > pos1.getLigne() && posTemp.getColonne() > pos1.getColonne() &&          //posTemp > pos1
                            posTemp.getLigne() < pos2.getLigne() && posTemp.getColonne() < pos2.getColonne() &&          ////posTemp < pos2
                            echiquier[colonne][ligne] != null)
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        */
}