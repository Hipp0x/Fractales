package Controller;
import Model.*;
import View.ViewFX;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

public class ControllerG {
    private ViewFX view;
    private ArrayList<String> fractaleOpt;

    public ControllerG(){}

    /**
    *  Transforme un string correspond a une couleur en sa valeur numérique attribuée
    */
    public int colorFromField(String str){
        switch (str){
            case "Rouge" -> {
                return 1;
            }
            case "Bleu" -> {
                return 2;
            }
            case "Vert" -> {
                return 3;
            }
            case "Multicolore" -> {
                return 4;
            }
            case "Rose-Orange" -> {
                return 5;
            }
            case "Orange-Rose" -> {
                return 6;
            }
            case "Vert-Bleu" -> {
                return 7;
            }
            case "Vert-Rose" -> {
                return 8;
            }
            default -> {
                return 0;
            }
        }
    }

    /*
     ************************************************************
     *  Versions graphique de fonctions définies dans Launcher  *
     ************************************************************
     */

    public static double[] validCst(String constante){
        String[] tab = constante.split(";");
        if (tab.length != 2){
            return null;
        }else{
            try {
                return new double[]{Double.parseDouble(tab[0]), Double.parseDouble(tab[1])};
            } catch (Exception e){
                return null;
            }
        }
    }

    public static double[] validRect(String rect){
        double[] r;
        String[] tab = rect.split(";");
        if (tab.length != 4){
            return null;
        } else {
            try {
                r = new double[]{Double.parseDouble(tab[0]), Double.parseDouble(tab[1]), Double.parseDouble(tab[2]), Double.parseDouble(tab[3])};
            } catch (Exception e){
                return null;
            }
        }

        if (r[0] >= r[1] || r[2] >= r[3]){
            return null;
        }
        return r;
    }

    public static int nbX(String s){
        int c = 0;
        for (int i = 0; i<s.length();i++){
            if (s.charAt(i) == 'x'){
                c++;
            }
        }
        return c;
    }

    public static boolean containsC(String[] last){
        for (String s : last) {
            if (containsC(s)) return true;
        }
        return false;
    }

    public static boolean containsC(String last){
        for (int j = 0; j < last.length(); j++){
            if(last.charAt(j) == 'c'){
                if (j == last.length() - 1) {
                    return true;
                }else {
                    if (last.charAt(j+1) != 'o'){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static int closingParenthePos(String mon){
        for (int i = 0; i < mon.length(); i++){
            if(mon.charAt(i) == ')'){
                return i;
            }
        }
        return -1;
    }

    public static LinkedList<double[]> validFct(String fonction){
        try {
            String[] aftPlus = fonction.split("\\+");
            if (!containsC(aftPlus)) { //verifie que la fonction contiens une constante
                return null;
            }
            LinkedList<double[]> liste = new LinkedList<>();
            for (int i = 0; i < aftPlus.length; i++) {
                String current = aftPlus[i];
                double[] data = new double[5];
                if (containsC(current)) {
                    if (current.equals("c")) { //c
                        liste.add(new double[]{1, 0, 0, 0, 0});
                    }
                    else if (current.startsWith("cos(")) {
                        if (current.charAt(4) == 'c') { //cos(c)
                            liste.add(new double[]{1, 0, 1, 0, 0});
                        } else { //cos(../c) ou cos(x/c)
                            String content = current.substring(4, closingParenthePos(current));
                            String[] tab = content.split("/");
                            if (tab.length == 2) {
                                if (tab[0].equals("x")) { // cos(x/c) ou cos(x/c2) ou 2 = puissance
                                    if (tab[1].length() == 1) {
                                        liste.add(new double[]{1, 0, 1, -1, 1});
                                    } else {
                                        if (tab[1].length() == 2) {
                                            liste.add(new double[]{1, 0, 1, -1, Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                                        } else {
                                            return null;
                                        }
                                    }
                                } else {
                                    if (tab[1].length() == 1) {
                                        liste.add(new double[]{1, 0, 1, Double.parseDouble(tab[0]), 1});
                                    } else {
                                        if (tab[1].length() == 2) {
                                            liste.add(new double[]{1, 0, 1, Double.parseDouble(tab[0]), Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                                        } else {
                                            return null;
                                        }
                                    }
                                }
                            }
                            else {
                                return null;
                            }
                        }
                    }
                    else if (current.startsWith("sinh(")) {
                        if (current.charAt(5) == 'c') { //sinh(c)
                            liste.add(new double[]{1, 0, 4, 0, 0});
                        } else {
                            String content = current.substring(5, closingParenthePos(current));
                            String[] tab = content.split("/");
                            if (tab.length != 2) return null;
                            else {
                                if (tab[0].equals("x")) { // cos(x/c) ou cos(x/c2) ou 2 = puissance
                                    if (tab[1].length() == 1) {
                                        liste.add(new double[]{1, 0, 4, -1, 1});
                                    } else {
                                        if (tab[1].length() == 2) {
                                            liste.add(new double[]{1, 0, 4, -1, Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                                        } else {
                                            return null;
                                        }
                                    }
                                } else {
                                    if (tab[1].length() == 1) {
                                        liste.add(new double[]{1, 0, 4, Double.parseDouble(tab[0]), 1});
                                    } else {
                                        if (tab[1].length() == 2) {
                                            liste.add(new double[]{1, 0, 4, Double.parseDouble(tab[0]), Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                                        } else {
                                            return null;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else if (current.startsWith("sin(")) {
                        if (current.charAt(4) == 'c') { //sin(c)
                            liste.add(new double[]{1, 0, 2, 0, 0});
                        } else {
                            String content = current.substring(4, closingParenthePos(current));
                            String[] tab = content.split("/");
                            if (tab.length != 2) return null;
                            else {
                                if (tab[0].equals("x")) { // cos(x/c) ou cos(x/c2) ou 2 = puissance
                                    if (tab[1].length() == 1) {
                                        liste.add(new double[]{1, 0, 2, -1, 1});
                                    } else {
                                        if (tab[1].length() == 2) {
                                            liste.add(new double[]{1, 0, 2, -1, Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                                        } else {
                                            return null;
                                        }
                                    }
                                } else {
                                    if (tab[1].length() == 1) {
                                        liste.add(new double[]{1, 0, 2, Double.parseDouble(tab[0]), 1});
                                    } else {
                                        if (tab[1].length() == 2) {
                                            liste.add(new double[]{1, 0, 2, Double.parseDouble(tab[0]), Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                                        } else {
                                            return null;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        String[] tab = current.split("/");
                        if (tab.length != 2) return null;
                        if (tab[1].length() == 1){
                            liste.add(new double[]{1, 0, 3, Double.parseDouble(tab[0]), 1});
                        }
                        else if (tab[1].length() == 2) {
                            liste.add(new double[]{1, 0, 3, Double.parseDouble(tab[0]), Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                        } else {
                            return null;
                        }
                    }
                }
                else if (current.charAt(0) == 'x') {
                    if (nbX(current) != 1) {
                        return null;
                    } else {
                        data[0] = 1;
                        if (current.length() == 1) {
                            data[1] = 1;
                            liste.add(i, data);
                        } else {
                            String[] res = current.split("x");
                            try {
                                data[1] = Double.parseDouble(res[1]);
                                liste.add(i, data);
                            } catch (Exception e) {
                                return null;
                            }
                        }
                    }
                }
                else if (current.startsWith("cos(") || current.startsWith("sin(") || current.startsWith("sinh(")) {
                    String content;
                    if (current.startsWith("sinh(")){
                        content = current.substring(5, closingParenthePos(current));
                    } else {
                        content = current.substring(4, closingParenthePos(current));
                    }
                    if (nbX(current) != 1) {
                        return null;
                    } else {
                        data[0] = 1;
                        data[2] = 0;
                        data[3] = 0;
                        data[4] = 0;
                        if (current.startsWith("cos(")) {
                            data[2] = 1;
                        }else if (current.startsWith("sinh(")) {
                            data[2] = 4;
                        }else if (current.startsWith("sin(")) {
                            data[2] = 2;
                        }
                        if (content.charAt(0) == 'x') {
                            data[1] = 1;
                            liste.add(i, data);
                        } else {
                            String[] res = content.split("x");
                            data[0] = Double.parseDouble(res[0]);
                            if (res.length == 1) {
                                data[1] = 1;
                            } else {
                                data[1] = Double.parseDouble(res[1]);
                            }
                            liste.add(i, data);
                        }
                    }
                }
                else {
                    if (nbX(current) != 1) {
                        return null;
                    } else {
                        String[] res = current.split("x");
                        try {
                            data[0] = Double.parseDouble(res[0]);
                            if (res.length == 1) {
                                data[1] = 1;
                            } else {
                                data[1] = Double.parseDouble(res[1]);
                            }
                            liste.add(i, data);
                        } catch (Exception e) {
                            return null;
                        }
                    }
                }
            }
            return liste;
        }catch(NumberFormatException e) {
            System.out.println("It is not numerical string");
            return null;
        }
    }

    public static double validPas(String pas){
        double p;
        try {
            p = Double.parseDouble(pas);
        } catch (Exception e){
            return 0;
        }

        if (p == 0.0) {
            return 0;
        } else if (p < 0){
            return 0;
        }
        return p;
    }

    public int validOrder(String ordre){
        try {
            int o = (int)Double.parseDouble(ordre);
            if (o >= 0){
                return o;
            } else {
                return -1;
            }
        }catch(NumberFormatException e) {
            return -1;
        }
    }

    public static int validIte(String it){
        try {
            int i = Integer.parseInt(it);
            if (i < 1){
                return -1;
            }else{
                return i;
            }
        } catch (Exception e){
            return -1;
        }
    }

    public String fileName(String nomFic){
        if (nomFic.isEmpty()){
            return "Fractale";
        }
        for (int i = 0; i < nomFic.length(); i++){
            if (nomFic.charAt(i) != ' '){
                return nomFic;
            }
        }
        return "Fractale";
    }

    public ArrayList<String> fractalLaunch(ArrayList<String> opt){
        BuilderFractal fract = new BuilderFractal();
        ArrayList<String> err = new ArrayList<>();
        Fractal fractale;
        switch (opt.get(0)){
            case "Julia","Mandelbrot" -> {
                if(opt.get(0).equals("Julia")) {
                    fract = fract.type("J").fichier(fileName(opt.get(1))).coloration(colorFromField(opt.get(2)));
                }else{
                    fract = fract.type("M").fichier(fileName(opt.get(1))).coloration(colorFromField(opt.get(2)));
                }
                double[] cst = validCst(opt.get(3));
                double[] rect = validRect(opt.get(4));
                double pas = validPas(opt.get(5));

                if (pas == 0){
                    err.add("p");
                }else{
                    fract = fract.pas(pas);
                }

                if (rect == null){
                    err.add("r");
                }else{
                    fract = fract.rect(rect);
                    if (rect[0] + pas >= rect[1] || rect[2] + pas >= rect[3]) {
                        err.add("rp");
                    }
                }

                if(opt.get(0).equals("Julia")) {
                    if (cst == null) {
                        err.add("cst");
                        err.add("fo");
                    } else {
                        LinkedList<double[]> fo = validFct(opt.get(6));
                        if (fo == null) {
                            err.add("fo");
                        } else {
                            Fonction.BuilderFonction fonction = new Fonction.BuilderFonction();
                            fonction.coef(fo);
                            fonction.cons(new Complex.Builder(cst[0], cst[1]).build());
                            fract = fract.fonction(fonction.build());
                        }
                    }
                }

                if (opt.get(0).equals("Mandelbrot")){
                    LinkedList<double[]> fo = validFct(opt.get(6));
                    if (fo == null) {
                        err.add("fo");
                    } else {
                        Fonction.BuilderFonction fonction = new Fonction.BuilderFonction();
                        fonction.coef(fo);
                        fract = fract.fonction(fonction.build());
                    }
                }

                int ite = validIte(opt.get(7));
                if (ite == -1){
                    err.add("it");
                }else{
                    fract = fract.iter(ite);
                }
                if(err.isEmpty()) {
                    fractaleOpt = opt;
                    fractale = fract.build();
                    view.showFractalJM(fractale);
                }
            }
            case "Sierpinski" -> {
                fract = fract.type("S").fichier(fileName(opt.get(1))).coloration(colorFromField(opt.get(2)));
                int order = validOrder(opt.get(3));
                if (order == -1){
                    err.add("o");
                }else{
                    fract = fract.ordre(order);
                }

                if(err.isEmpty()) {
                    fractaleOpt = opt;
                    fractale = fract.build();
                    view.showFractalS(fractale);
                }
            }
        }
        return err;
    }

    public String rectArrayToString(double[] rect){
        StringBuilder rectangle = new StringBuilder();
        for (int i = 0; i<3; i++){
            rectangle.append(rect[i]).append(";");
        }
        rectangle.append(rect[3]);
        return rectangle.toString();
    }

    /*
    * *********************************** *
    * Fonctions de Zoom et de Déplacement *
    * *********************************** *
    */

    /**
     *  Effectue un zoom de 20% sur le centre de la fractale actuelle.
     *  Réduit la taille du rectangle de 20% et réduit le pas de 20%
     */
    public void requestZoomIn(){
        BuilderFractal zoomFract;
        ArrayList<String> opt = fractaleOpt;
        if (opt.get(0).equals("Julia") || opt.get(0).equals("Mandelbrot")){
            zoomFract = sameFract(opt);

            double[] rect = validRect(opt.get(4));
            double diffW10Perc = (rect[1] - rect[0]) * 0.1;
            double diffH10Perc = (rect[3] - rect[2]) * 0.1;
            rect[0] = rect[0] + diffW10Perc;
            rect[1] = rect[1] - diffW10Perc;
            rect[2] = rect[2] + diffH10Perc;
            rect[3] = rect[3] - diffH10Perc;
            String rectangle = rectArrayToString(rect);
            fractaleOpt.set(4,rectangle);
            double pas = 0.8 * validPas(opt.get(5));
            fractaleOpt.set(5, Double.toString(pas));
            zoomFract = zoomFract.rect(rect).pas(pas);
            view.showFractalJM(zoomFract.build());
        }
    }

    /**
     *  Effectue un dézoom de 20%
     *  Augmente la taille du rectangle de 20% et augmente le pas de 20%
     */
    public void requestZoomOut(){
        BuilderFractal zoomFract;
        ArrayList<String> opt = fractaleOpt;
        if (opt.get(0).equals("Julia") || opt.get(0).equals("Mandelbrot") ){
            zoomFract = sameFract(opt);
            double[] rect = validRect(opt.get(4));
            double diffW10Perc = (rect[1] - rect[0]) * 0.1;
            double diffH10Perc = (rect[3] - rect[2]) * 0.1;
            rect[0] = rect[0] - diffW10Perc;
            rect[1] = rect[1] + diffW10Perc;
            rect[2] = rect[2] - diffH10Perc;
            rect[3] = rect[3] + diffH10Perc;
            String rectangle = rectArrayToString(rect);
            fractaleOpt.set(4,rectangle);
            double pas = 1.2 * validPas(opt.get(5));
            fractaleOpt.set(5, Double.toString(pas));
            zoomFract = zoomFract.rect(rect).pas(pas);
            view.showFractalJM(zoomFract.build());
        }
    }

    /**
     * Effectue un mouvement du plan de la fractale vers l'un des plans points cardinaux
     * @param direction: String indiquant la direction du déplacement
     * */
    public void requestMove(String direction){
        BuilderFractal zoomFract;
        ArrayList<String> opt = fractaleOpt;
        if (opt.get(0).equals("Julia") || opt.get(0).equals("Mandelbrot")){
            zoomFract = sameFract(opt);
            fractaleOpt.set(5, Double.toString(validPas(opt.get(5))));

            double[] rect = validRect(opt.get(4));
            switch (direction){
                case "UP" -> {
                    double diffH10Perc = (rect[3] - rect[2]) * 0.1;
                    rect[2] = rect[2] - diffH10Perc;
                    rect[3] = rect[3] - diffH10Perc;
                }
                case "DOWN" -> {
                    double diffH10Perc = (rect[3] - rect[2]) * 0.1;
                    rect[2] = rect[2] + diffH10Perc;
                    rect[3] = rect[3] + diffH10Perc;
                }
                case "LEFT" -> {
                    double diffH10Perc = (rect[1] - rect[0]) * 0.1;
                    rect[0] = rect[0] - diffH10Perc;
                    rect[1] = rect[1] - diffH10Perc;
                }
                case "RIGHT" -> {
                    double diffH10Perc = (rect[1] - rect[0]) * 0.1;
                    rect[0] = rect[0] + diffH10Perc;
                    rect[1] = rect[1] + diffH10Perc;
                }

            }
            String rectangle = rectArrayToString(rect);
            fractaleOpt.set(4,rectangle);

            zoomFract = zoomFract.rect(rect).pas(validPas(opt.get(5)));
            view.showFractalJM(zoomFract.build());
        }
    }

    /**
     * Fonction remplissant la plupart des éléments d'une fractale
     * @param opt : La liste d'options rentrée par l'utilisateur
     * @return Un objet BuilderFractal partiellement défini
     **/
    public BuilderFractal sameFract(ArrayList<String> opt){
        BuilderFractal fract = new BuilderFractal();
        fract = fract.type(Character.toString(opt.get(0).charAt(0))).fichier(fileName(opt.get(1))).coloration(colorFromField(opt.get(2))).pas(validPas(opt.get(5))).iter(validIte(opt.get(7)));
        double[] cst = validCst(opt.get(3));
        Fonction.BuilderFonction fonc = new Fonction.BuilderFonction().coef(validFct(opt.get(6)));
        if (opt.get(0).charAt(0) == 'J') {
            fonc = fonc.cons(new Complex.Builder(cst[0], cst[1]).build());
        }
        fract = fract.fonction(fonc.build());
        return fract;
    }

    /*
    * *************************************** *
    *            Fonctions autres             *
    * *************************************** *
    */

    /**
     * Sauvegarde la fractale actuellement affichée dans un fichier
     */
    public void saveImg(){
        File outputFile = new File(fileName(fractaleOpt.get(1)) + ".png");
        BufferedImage bImage = SwingFXUtils.fromFXImage(view.getFractImg().getImage(), null);
        try {
            ImageIO.write(bImage, "png", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Setter de la vue
    public void setView(ViewFX view) {
        this.view = view;
    }
}
