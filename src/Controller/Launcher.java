package Controller;

import Application.UIApp;
import Model.*;

import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.LinkedList;

public class Launcher {

    static Options opt = CommandApache.createOptionDemarrage();

    /*
     * ***************************************************** *
     * Verifie le format des arguments de la ligne de commande
     * ***************************************************** *
     */

    /**
     * Verifie le format d'entree du rectangle
     * @param rect : le rectangle voulu
     * @return un tableau de double correspondant au rectangle
     */
    public static double[] correctFormatRect(String rect){
        String[] tab = rect.split(";");
        if (tab.length != 4){
            errorParsing();
        } else {
            try {
                return new double[]{Double.parseDouble(tab[0]), Double.parseDouble(tab[1]), Double.parseDouble(tab[2]), Double.parseDouble(tab[3])};
            } catch (Exception e){
                errorParsing();
            }
        }
        return null;
    }

    /**
     * Verifie le format d'entree du pas
     * @param pas : pas voulu
     * @return un double correspondant au pas
     */
    public static double correctFormatPas(String pas){
        try {
            return Double.parseDouble(pas);
        } catch (Exception e){
            errorParsing();
        }
        return -1;
    }

    /**
     * Verifie le format d'entree du pas
     * @param it : le nombre d'iteration voulu
     * @return l'entier correspondant au nombre d'iteration
     */
    public static int correctFormatIte(String it){
        try {
            return Integer.parseInt(it);
        } catch (Exception e){
            errorParsing();
        }
        return -1;
    }

    /**
     * Verifie le format d'entree de la constante de la fonction
     * @param constante : la constante voulue
     * @return un tableau de double correspondant a notre constante
     */
    public static double[] correctFormatCst(String constante){

        String[] tab = constante.split(";");
        if (tab.length != 2){
            errorParsing();
        }else{
            try {
                return new double[]{Double.parseDouble(tab[0]), Double.parseDouble(tab[1])};
            } catch (Exception e){
                errorParsing();
            }
        }
        return null;

    }

    /**
     * Calcule le nombre de x present dans un string
     * @param s : la chaine de caracteres
     * @return le nombre de x comptes
     */
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

    /**
     * Verifie le format d'entree de la fonction
     * @param fonction : la fonction voulue
     * @return une liste de tableau de double correspondant aux coefficient de la fonction
     */
    public static LinkedList<double[]> correctFormatFct(String fonction){
        try {
            String[] aftPlus = fonction.split("\\+");
            if (!containsC(aftPlus)) { //verifie que la fonction contiens une constante
                errorParsing();
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
                                            errorParsing();
                                        }
                                    }
                                } else {
                                    if (tab[1].length() == 1) {
                                        liste.add(new double[]{1, 0, 1, Double.parseDouble(tab[0]), 1});
                                    } else {
                                        if (tab[1].length() == 2) {
                                            liste.add(new double[]{1, 0, 1, Double.parseDouble(tab[0]), Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                                        } else {
                                            errorParsing();
                                        }
                                    }
                                }
                            }
                            else {
                                errorParsing();
                            }
                        }
                    }
                    else if (current.startsWith("sinh(")) {
                        if (current.charAt(5) == 'c') { //sinh(c)
                            liste.add(new double[]{1, 0, 4, 0, 0});
                        } else {
                            String content = current.substring(5, closingParenthePos(current));
                            String[] tab = content.split("/");
                            if (tab.length != 2) errorParsing();
                            else {
                                if (tab[0].equals("x")) { // cos(x/c) ou cos(x/c2) ou 2 = puissance
                                    if (tab[1].length() == 1) {
                                        liste.add(new double[]{1, 0, 4, -1, 1});
                                    } else {
                                        if (tab[1].length() == 2) {
                                            liste.add(new double[]{1, 0, 4, -1, Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                                        } else {
                                            errorParsing();
                                        }
                                    }
                                } else {
                                    if (tab[1].length() == 1) {
                                        liste.add(new double[]{1, 0, 4, Double.parseDouble(tab[0]), 1});
                                    } else {
                                        if (tab[1].length() == 2) {
                                            liste.add(new double[]{1, 0, 4, Double.parseDouble(tab[0]), Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                                        } else {
                                            errorParsing();
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
                            if (tab.length != 2) errorParsing();
                            else {
                                if (tab[0].equals("x")) { // cos(x/c) ou cos(x/c2) ou 2 = puissance
                                    if (tab[1].length() == 1) {
                                        liste.add(new double[]{1, 0, 2, -1, 1});
                                    } else {
                                        if (tab[1].length() == 2) {
                                            liste.add(new double[]{1, 0, 2, -1, Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                                        } else {
                                            errorParsing();
                                        }
                                    }
                                } else {
                                    if (tab[1].length() == 1) {
                                        liste.add(new double[]{1, 0, 2, Double.parseDouble(tab[0]), 1});
                                    } else {
                                        if (tab[1].length() == 2) {
                                            liste.add(new double[]{1, 0, 2, Double.parseDouble(tab[0]), Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                                        } else {
                                            errorParsing();
                                        }
                                    }
                                }
                            }
                        }
                    }
                    else {
                        String[] tab = current.split("/");
                        if (tab.length != 2) errorParsing();
                        if (tab[1].length() == 1){
                            liste.add(new double[]{1, 0, 3, Double.parseDouble(tab[0]), 1});
                        }
                        else if (tab[1].length() == 2) {
                            liste.add(new double[]{1, 0, 3, Double.parseDouble(tab[0]), Double.parseDouble(String.valueOf(tab[1].charAt(1)))});
                        } else {
                            errorParsing();
                        }
                    }
                }
                else if (current.charAt(0) == 'x') {
                    if (nbX(current) != 1) {
                        errorParsing();
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
                                errorParsing();
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
                        errorParsing();
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
                        errorParsing();
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
                            errorParsing();
                        }
                    }
                }
            }
            return liste;
        }catch(NumberFormatException e) {
            System.out.println("It is not numerical string");
            errorParsing();
        }
        return null;
    }

    /**
     * Verifie le format d'entree de la coloration
     * @param c : coloration voulue
     * @return l'entier correspondant a la coloration
     */
    public static int correctFormatColor(String c){
        if (c.equals("0") || c.equals("1") || c.equals("2") || c.equals("3") || c.equals("4") ||
                c.equals("5") || c.equals("6") ||  c.equals("7") || c.equals("8") || c.equals("9")){
            return Integer.parseInt(c);
        } else {
            errorParsing();
        }
        return -1;
    }

    /**
     * Verifie le format d'entree du type de fractale
     * @param c : type de fractale voulu
     * @return le type souhaite
     */
    public static String correctFormatType(String c){
        if (c.equals("J") || c.equals("M") || c.equals("S")){
            return c;
        } else {
            errorParsing();
        }
        return "";
    }

    /**
     * Verifie le format d'entree de l'ordre
     * @param c : ordre voulu
     * @return l'entier correspondant a l'ordre
     */
    public static int correctFormatOrdre(String c){
        try {
            int o = (int)Double.parseDouble(c);
            if (o < 0){
                errorParsing();
            } else {
                return o;
            }
        }catch(NumberFormatException e) {
            System.out.println("It is not numerical string");
            errorParsing();
        }
        return -1;
    }

    /**
     * Verifie le format d'entree du radius
     * @param r : radius souhaite
     * @return l'entier correspondant au radius
     */
    public static int correctFormatRadius(String r){
        try {
            int o = (int)Double.parseDouble(r);
            if (o < 0){
                errorParsing();
            } else {
                return o;
            }
        }catch(NumberFormatException e) {
            System.out.println("It is not numerical string");
            errorParsing();
        }
        return -1;
    }

    /*
     * ***************************************************** *
     *              Lancement du Programme
     * ***************************************************** *
     */

    /**
     * Fonction qui intervient lors d'une erreur de Parsing :
     * Mauvais parametres, arguments non valides....
     */
    public static void errorParsing(){
        System.out.println("Error parsing command-line arguments!");
        System.out.println("Please, follow the instructions below:");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("FractaleCLI", opt, true);
        System.exit(0);
    }

    /**
     * Fonction principale de lancement du programme
     * @param args : la ligne de commande
     */
    public static void main(String[] args) throws IOException {

        //parser la ligne de commande
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(opt, args);
            //graphique
            if (cmd.hasOption("G")){
                if (cmd.hasOption("C") || cmd.hasOption("r") || cmd.hasOption("cst") || cmd.hasOption("p") || cmd.hasOption("h") || cmd.hasOption("f") ){
                    errorParsing();
                } else {
                    //lancer le mode graphique
                    UIApp ui = new UIApp();
                    ui.launchGraph();
                }
            }
            //console
            else if (cmd.hasOption( "C")){
                if (cmd.hasOption("G") || cmd.hasOption("h")){
                    errorParsing();
                }
                else if (cmd.hasOption("t")){
                    String type = correctFormatType(cmd.getOptionValue("t"));
                    //Julia ou Mandelbrot
                    if (type.equals("J") || type.equals("M")){
                        if (cmd.hasOption("o")) {
                            errorParsing();
                        }
                        else if (type.equals("J") && !cmd.hasOption("cst")){
                            errorParsing();
                        }
                        else if (cmd.hasOption("r") && cmd.hasOption("p")){
                            double[] r = correctFormatRect(cmd.getOptionValue("r"));
                            double p = correctFormatPas(cmd.getOptionValue("p"));

                            if (r[0] >= r[1] || r[2] >= r[3]){
                                errorParsing();
                            } else if (r[0] + p >= r[1] || r[2] + p >= r[3]){
                                errorParsing();
                            } else if (p == 0.0) {
                                errorParsing();
                            } else if (p < 0){
                                errorParsing();
                            }

                            BuilderFractal fractale = new BuilderFractal();
                            fractale = fractale.rect(r).type(type).pas(p);

                            Fonction.BuilderFonction fonction = new Fonction.BuilderFonction();
                            if (cmd.hasOption("fo")) {
                                LinkedList<double[]> fo = correctFormatFct(cmd.getOptionValue("fo"));
                                fonction = fonction.coef(fo);
                            }
                            if (cmd.hasOption("cst")){
                                double[] cst = correctFormatCst(cmd.getOptionValue("cst"));
                                fonction.cons(new Complex.Builder(cst[0],cst[1]).build());
                            }
                            fractale = fractale.fonction(fonction.build());

                            if (cmd.hasOption("col")){
                                int color = correctFormatColor(cmd.getOptionValue("col"));
                                fractale = fractale.coloration(color);
                            }

                            if (cmd.hasOption("it")){
                                int ite = correctFormatIte(cmd.getOptionValue("it"));
                                fractale = fractale.iter(ite);
                            }

                            if (cmd.hasOption("fi")){
                                String fic = cmd.getOptionValue("fi");
                                fractale = fractale.fichier(fic);
                            }

                            if (cmd.hasOption("ra")){
                                int rad = correctFormatRadius(cmd.getOptionValue("ra"));
                                fractale = fractale.radius(rad);
                            }

                            Fractal fractal = fractale.build();
                            fractal.launchFractale();

                        }
                        else {
                            errorParsing();
                        }
                    }
                    //Sierpinski
                    else if (type.equals("S")){
                        if (cmd.hasOption("o") && cmd.getArgs().length < 1){
                            int ordre = correctFormatOrdre(cmd.getOptionValue("o"));
                            BuilderFractal fractale = new BuilderFractal();
                            fractale = fractale.type(type).ordre(ordre);

                            if (cmd.hasOption("fi")){
                                String fic = cmd.getOptionValue("fi");
                                fractale = fractale.fichier(fic);
                            }

                            if (cmd.hasOption("col")){
                                int color = correctFormatColor(cmd.getOptionValue("col"));
                                fractale = fractale.coloration(color);
                            }

                            Fractal fractal = fractale.build();
                            fractal.launchFractale();

                        }
                        else {
                            errorParsing();
                        }
                    }
                    else {
                        errorParsing();
                    }
                } else {
                    errorParsing();
                }
            }
            //help
            else if (cmd.hasOption("h") && cmd.getArgs().length < 1){
                final HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("FractaleCLI", opt, true);
                System.exit(0);
            } else {
                errorParsing();
            }
        } catch (ParseException e) {
            errorParsing();
        }
    }

}
