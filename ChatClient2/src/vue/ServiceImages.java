package vue;


import javax.swing.ImageIcon;
import java.io.File;

/**
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-10-01
 */
public class ServiceImages {
	public static ImageIcon getIconePourPiece(char piece) {
		char couleur;
		String nomFichier = null;
		if (piece>='a' && piece<='z')
			couleur = 'n';
		else {
			couleur = 'b';
			piece += 32; //conversion en minuscule
		}
		switch (piece) {
			case 'c':
				nomFichier = "cavalier-"+couleur+".png";
				break;
			case 'd':
				nomFichier = "dame-"+couleur+".png";
				break;
			case 'f':
				nomFichier = "fou-"+couleur+".png";
				break;
			case 'p':
				nomFichier = "pion-"+couleur+".png";
				break;
			case 'r':
				nomFichier = "roi-"+couleur+".png";
				break;
			case 't':
				nomFichier = "tour-"+couleur+".png";
				break;
		}
		System.out.println("Nom fichier de l'image : "+nomFichier);
		if (nomFichier==null)
			return null;
		File file = new File("imgs/" +nomFichier);
		if(file.exists()) {
			return new ImageIcon("imgs/" +nomFichier);
		}
		System.out.println("File doesnt exist");
		return null;
	}
	public static ImageIcon getImage(String nomFichier) {
		return new ImageIcon("imgs/" +nomFichier);
	}
}
