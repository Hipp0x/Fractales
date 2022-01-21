package Model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ForkJoinPool;

import static java.lang.System.exit;

public class Sierpinski extends Fractal{

    private final int ordre;
    private double[][] tab;
    private final int color;
    private final String fic;

    public Sierpinski(BuilderFractal builderFractal){
        this.color = builderFractal.color;
        this.fic = builderFractal.fic;
        this.ordre = builderFractal.ordre;
        this.tab = init();
        this.tab = createRect();
    }

    /**
     * Initialise le tableau de données tab
     * pas de retour
     */
    public double[][] init(){
        try {
            double[][] tab = new double[(int) Math.pow(3, this.ordre)][(int) Math.pow(3, this.ordre)];
            for (int i = 0; i < tab.length; i++) {
                for (int j = 0; j < tab[0].length; j++) {
                    tab[i][j] = 0;
                }
            }
            return tab;
        } catch (Error e){
            System.out.println("Pas assez d'espace memoire.\nVeuillez reduire l'ordre.");
            exit(0);
        }
        return null;
    }

    /**
     * Construit le tableau de données tab
     * @param compt : compteur pour savoir à quel ordre on se situe
     * @param i1 : 1ere position i dans le tableau
     * @param i2 : derniere position i dans le tableau
     * @param j1 : 1ere position j dans le tableau
     * @param j2 : derniere position j dans le tableau
     * @return le tableau de données tab remplis
     */
    public double[][] construct(int compt, int i1, int i2, int j1, int j2){
        try {
            ActionSierp work = new ActionSierp(ordre, tab, compt, i1, i2, j1, j2);
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(work);
            return tab;
        } catch (Error e){
            System.out.println("Pas assez d'espace memoire.\nVeuillez reduire l'ordre.");
            exit(0);
        }
        return null;
    }

    /*
     * ***************************************************** *
     *                     Fonctions
     * ***************************************************** *
     */

    @Override
    public double[][] getTableau() {
        return this.tab.clone();
    }

    @Override
    public String getFichier() {
        return fic;
    }


    @Override
    public double[][] createRect(){
        return construct(0, 0, this.tab.length, 0 , this.tab.length);
    }

    @Override
    public BufferedImage createImg(double[][] data){
        BufferedImage img = new BufferedImage(this.tab.length, this.tab.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0;i<this.tab.length;i++){
            for (int j = 0; j< this.tab[0].length;j++){
                int c = this.coloration((int) data[i][j]);
                if (this.tab[i][j] == 0){
                    img.setRGB(i,j,c);
                } else {
                    img.setRGB(i,j,Color.BLACK.getRGB());
                }
            }
        }
        return img;
    }

    @Override
    public int coloration(double val) {
        switch (this.color) {
            case 0 -> {
                return Color.WHITE.getRGB();
            }
            case 1 -> {
                return Color.RED.getRGB();
            }
            case 2 -> {
                return Color.BLUE.getRGB();
            }
            case 3 -> {
                return Color.GREEN.getRGB();
            }
            default -> {
                return Color.BLACK.getRGB();
            }
        }
    }

    @Override
    public void writeFileTxt() throws IOException {
        File file = new File(this.fic+".txt");
        PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
        writer.println("Descriptif de la Fractale :\n");
        writer.println("Type : Sierpinski\n");
        writer.println("Ordre : "+this.ordre);
        if (this.color == 0) {
            writer.println("Coloration : Noire et Blanche");
        } else if (this.color == 1) {
            writer.println("Coloration : Rouge");
        } else if (this.color == 2) {
            writer.println("Coloration : Bleue");
        } else if (this.color == 3) {
            writer.println("Coloration : Verte");
        }
        writer.close();
    }

    public double[][] getTab() {
        return tab;
    }
}
