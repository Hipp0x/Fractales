package Model;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ForkJoinPool;

import static java.lang.System.exit;

public class Julia extends Fractal{

    private final double[][] rect;
    private final double[][] data;
    private final double pas;
    private final int iter;
    private final int color;
    private final String fic;
    private final Fonction function;
    private final int radius;

    Julia(BuilderFractal builderFractal) {
        this.rect = builderFractal.rect;
        this.pas = builderFractal.pas;
        this.iter = builderFractal.iter;
        this.color = builderFractal.color;
        this.fic = builderFractal.fic;
        this.function = builderFractal.function;
        this.radius = builderFractal.radius;
        this.data = createRect();
    }


    /*
     * ***************************************************** *
     *                     Fonctions
     * ***************************************************** *
     */

    @Override
    public double[][] getTableau() {
        return this.data;
    }

    @Override
    public String getFichier() {
        return fic;
    }

    @Override
    public double[][] createRect() {
        try {
            double w = (this.rect[0][1] - this.rect[0][0]) / this.pas;
            double h = (this.rect[1][1] - this.rect[1][0]) / this.pas;
            double[][] d = new double[(int) h][(int) w];
            int total = (d.length * d[0].length);
            ActionJulia work = new ActionJulia(0, total, d, pas, this.rect[0][0], this.rect[1][0], function, iter, total / 3, radius);
            ForkJoinPool pool = new ForkJoinPool();
            pool.invoke(work);
            return d;
        } catch (Error e){
            System.out.println("Pas assez d'espace memoire.\nVeuillez reduire le pas ou le rectangle.");
            exit(0);
        }
        return null;
    }

    @Override
    public BufferedImage createImg(double[][] data){
        BufferedImage img = new BufferedImage(data[0].length, data.length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0;i<data.length;i++){
            for (int j = 0; j< data[0].length;j++){
                img.setRGB(j,i,coloration(data[i][j]));
            }
        }
        return img;
    }

    @Override
    public int coloration(double val) {
        if (val == this.iter) {
            return new Color(0,0,0).getRGB();
        }
        switch (this.color) {
            case 0 -> {
                int r = (int) ((255 * val) / this.iter);
                int g = (int) ((255 * val) / this.iter);
                int b = (int) ((255 * val) / this.iter);
                return new Color(r,g,b).getRGB();
            }
            case 1 -> {
                //20 = 0.055
                //345 = 0.9583
                float res = (float) (0.9583 + (0.0967 * (val / this.iter)));
                return Color.HSBtoRGB(res, (float)val/iter, (float)val/iter);
            }
            case 2 -> {
                //180 = 0.5
                //270 = 0.75
                float res = (float) (0.5 + (0.25 * (val / this.iter)));
                return Color.HSBtoRGB(res, (float)val/iter, (float)val/iter);
            }
            case 3 -> {
                //75 = 0.208
                //160 = 0.44
                float res = (float) (0.208 + ( 0.232 * (val / this.iter)));
                return Color.HSBtoRGB( res, (float) (0.2 + (0.8*val/iter)), (float) (0.1 + (0.9 *val/iter)));
            }
            case 4 -> {
                float res = (float) (val / this.iter);
                return Color.HSBtoRGB(res, (float) (0.7 + 0.3*val/iter), (float) (0.8 +  0.2*val/iter));
            }
            case 5 -> { //rose-orange FF00FF
                //y = ax + b
                // a = orange - rose / iter
                // b = jaune
                double a = (Color.decode("#FF00FF").getRGB() - Color.decode("#F0CB75").getRGB()) / (double)iter;
                double b = Color.decode("#F0CB75").getRGB();
                return (int) ( (a * val) + b);
            }
            case 6 -> { //orange-rose
                double a = (Color.decode("#F0CB75").getRGB() - Color.decode("#FF00FF").getRGB()) / (double)iter;
                double b = Color.decode("#FF00FF").getRGB();
                return (int) ( (a * val) + b);
            }
            case 7 -> { //vert-bleu
                double a = (Color.decode("#79BFF0").getRGB() - Color.decode("#00ffb7").getRGB()) / (double)iter;
                double b = Color.decode("#00ffb7").getRGB();
                return (int) ( (a * val) + b);
            }
            case 8 -> { //vert-rose
                double a = (Color.decode("#EB83F4").getRGB() - Color.decode("#87F075").getRGB()) / (double)iter;
                double b = Color.decode("#87F075").getRGB();
                return (int) ( (a * val) + b);
            }
            case 9 -> {
                float res = (float) (val / this.iter);
                return Color.HSBtoRGB(res, (float)val/iter, (float)val/iter); //nous
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
        writer.println("Type : Julia\n");
        writer.println("Rect : [ ["+this.rect[0][0]+", "+this.rect[0][1]+"], ["+this.rect[1][0]+", "+this.rect[1][1]+"] ]");
        writer.println("Pas : "+this.pas);
        writer.println("Radius : "+this.radius);
        writer.println("Fonction : "+ this.function);
        writer.println("Constante : "+this.function.getC());
        writer.println("Iterations : "+ this.iter);
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
}
