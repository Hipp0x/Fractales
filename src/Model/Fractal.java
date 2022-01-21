package Model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Fractal {

    /*
     * ***************************************************** *
     *                Fonctions abstraites
     * ***************************************************** *
     */

    /**
     * @return le tableau de données de la fractale
     */
    public abstract double[][] getTableau();

    /**
     * @return le nom du fichier voulu pour la fractale
     */
    public abstract String getFichier();

    /**
     * Determine la coloration de notre point
     * @param val : valeur d'une case de notre tableau de données
     * @return la couleur de point dans notre fractale en fonction de la coloration
     */
    public abstract int coloration(double val);

    /**
     * Initialise et complète le tableau de données
     * @return le tableau de données complété
     */
    public abstract double[][] createRect() ;

    /**
     * Creer l'image de notre fractale
     * @param data : notre tableau de données
     * @return l'image de la fractale en fonction du tableau de données
     */
    public abstract BufferedImage createImg(double[][] data);

    /**
     * Ecrit un fichier descriptif de la fractale
     * @throws IOException
     * pas de retour
     */
    public abstract void writeFileTxt() throws IOException;

    /*
     * ***************************************************** *
     *                  Fonctions communes
     * ***************************************************** *
     */

    /**
     * Creer une fractale
     * @throws IOException
     * pas de retour
     */
    public void launchFractale() throws IOException {
        BufferedImage img = createImg(this.getTableau());

        File file = new File(this.getFichier()+".png");

        ImageIO.write(img, "PNG", file);
        writeFileTxt();
    }

}
