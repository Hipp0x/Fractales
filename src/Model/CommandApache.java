package Model;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class CommandApache {

    public CommandApache(){}

    public static Options createOptionDemarrage(){
        Options options = new Options();

        //Graphique
        options.addOption(Option.builder("G")
                .longOpt("graph")
                .hasArg(false)
                .desc("Mode Graphique\n")
                .build());

        //Console
        options.addOption(Option.builder("C")
                .longOpt("cons")
                .hasArg(false)
                .desc("Mode Console\n")
                .build());

        //rectangle
        options.addOption(Option.builder("r")
                .longOpt("rect")
                .hasArg(true)
                .desc("Borne du rectangle, " +
                        "\nforme x1;x2;y1;y2\n")
                .argName("rectangle")
                .required(false)
                .build());

        //radius
        options.addOption(Option.builder("ra")
                .longOpt("rad")
                .hasArg(true)
                .desc("Radius pour le calcul de l'indice de divergence")
                .argName("radius")
                .required(false)
                .build());

        //pas
        options.addOption(Option.builder("p")
                .longOpt("pas")
                .hasArg(true)
                .desc("Pas de discretisation\n")
                .argName("pas")
                .required(false)
                .build());

        //constante
        options.addOption(Option.builder("cst")
                .longOpt("const")
                .hasArg(true)
                .desc("Constante de la fonction," +
                        "\nforme r;i " +
                        "\nou r et i sont les valeurs respectives de la partie reelle/img\n")
                .argName("constante")
                .required(false)
                .build());

        //fonction
        options.addOption(Option.builder("fo")
                .longOpt("fonct")
                .hasArg(true)
                .desc("Fonction pour la fractale, " +
                        "\nforme 3x2+x+c, " +
                        "\navec c obligatoire\n")
                .argName("fonction")
                .required(false)
                .build());

        //nombre d'iteration
        options.addOption(Option.builder("it")
                .longOpt("iter")
                .hasArg(true)
                .desc("Nombre d'iterations de la fonction\n")
                .argName("iterations")
                .required(false)
                .build());

        //coloration
        options.addOption(Option.builder("col")
                .longOpt("color")
                .hasArg(true)
                .desc("Choix de la coloration : " +
                        "\n0(Noir/Blanc)," +
                        "\n1(Rouge)," +
                        "\n2(Bleu)," +
                        "\n3(Vert)," +
                        "\n4(Multicolor)," +
                        "\n5(Rose/Orange)," +
                        "\n6(Orange/Rose)," +
                        "\n7(Vert/Bleu)," +
                        "\n8(Vert/Rose)\n")
                .argName("coloration")
                .required(false)
                .build());

        //fichier
        options.addOption(Option.builder("fi")
                .longOpt("fic")
                .hasArg(true)
                .desc("Nom du fichier de l'image\n")
                .argName("fichier")
                .required(false)
                .build());

        //type de la fractale
        options.addOption(Option.builder("t")
                .longOpt("type")
                .hasArg(true)
                .desc("Type de Fractale : " +
                        "\nJ, M ou S\n")
                .argName("type")
                .required(false)
                .build());

        //ordre (Sierpinski)
        options.addOption(Option.builder("o")
                .longOpt("ord")
                .hasArg(true)
                .desc("Ordre du Tapis de Sierpinski\n")
                .argName("ordre")
                .required(false)
                .build());

        //help
        options.addOption(Option.builder("h")
                .longOpt("help")
                .desc("Aide pour les commandes/arguments\n")
                .build());

        return options;
    }
}
