package View;

import Controller.ControllerG;
import Model.Fractal;
import Model.Julia;
import Model.Sierpinski;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ViewFX {

    private final ControllerG control;
    private Stage mainStage;
    private final ImageView fractImg = new ImageView();
    private final Pane previewImage = new Pane();
    private ChoiceBox<String> colorChoice;
    private TextField fieldOrdre;
    private final ArrayList<TextField> textFields = new ArrayList<>();
    private final ArrayList<TextField> julMandOpt = new ArrayList<>();
    private final ArrayList<TextField> sierpOpt = new ArrayList<>();


    public ViewFX(ControllerG control){
        this.control = control;
    }

    /**
     * Verouille tous les champs disponibles
     */
    public void lockAll(){
        textFields.forEach(e -> {
            e.setFocusTraversable(false);
            e.setDisable(true);
        });
        colorChoice.setDisable(true);
    }

    /**
     * Déverouille tous les champs étant utilisés pour définir une Fractale de Julia ou de Mandelbrot
     */
    public void unlockJulMand(){
        julMandOpt.forEach(e -> {
            e.setBackground(Background.EMPTY);
            e.setStyle("-fx-text-box-border: #000000; -fx-focus-color: #000000;");
            e.setDisable(false);
        });
    }

    /**
     * Déverouille tous les champs étant utilisés pour définir une Fractale de Sierpinski
     */
    public void unlockSierp(){
        sierpOpt.forEach(e -> {
            e.setBackground(Background.EMPTY);
            e.setStyle("-fx-text-box-border: #000000; -fx-focus-color: #000000;");
            e.setDisable(false);
        });
    }

    /**
     * Création du menu de définition de la fractale
     */
    public void showSetMenu(){

        GridPane root = new GridPane();

        root.setPadding(new Insets(20));
        root.setHgap(25);
        root.setVgap(20);


        /* ******************************* *
         * Création des éléments du layout *
         * ******************************* */

        //Titre
        Label labelTitle = new Label("Let's create a fractal!");
        labelTitle.setFont(new Font(20));

        //Choix du Pas
        VBox pasBox = new VBox();
        pasBox.setAlignment(Pos.CENTER);
        pasBox.setSpacing(7);
        Label labelPas = new Label("Pas de discretisation");
        TextField fieldPas = new TextField();
        fieldPas.setPromptText("0.001");
        textFields.add(fieldPas);
        julMandOpt.add(fieldPas);

        //Choix du nombre d'itérations
        VBox iterBox = new VBox();
        iterBox.setAlignment(Pos.CENTER);
        iterBox.setSpacing(7);
        Label labelIter = new Label("Nombre d'iterations");
        TextField fieldIter = new TextField("500");
        fieldIter.setPrefWidth(170);
        textFields.add(fieldIter);
        julMandOpt.add(fieldIter);

        //Choix de la couleur
        VBox colorBox = new VBox();
        colorBox.setAlignment(Pos.CENTER);
        colorBox.setSpacing(7);
        Label labelColor = new Label("Couleur");
        this.colorChoice = new ChoiceBox<>(FXCollections.observableArrayList(
                "Noir et Blanc","Bleu","Rouge","Vert","Multicolore","Rose-Orange","Orange-Rose","Vert-Bleu","Vert-Rose"));
        colorChoice.setValue("Noir et Blanc");

        //Choix du nom du fichier
        VBox ficBox = new VBox();
        ficBox.setAlignment(Pos.CENTER);
        ficBox.setSpacing(7);
        Label labelFic = new Label("Nom du fichier");
        TextField fieldFic = new TextField("Fractale");
        textFields.add(fieldFic);
        julMandOpt.add(fieldFic);
        sierpOpt.add(fieldFic);

        //Choix du rectangle
        VBox rectBox = new VBox();
        rectBox.setAlignment(Pos.CENTER);
        rectBox.setSpacing(7);
        Label labelRect = new Label("Rectangle de visualisation");
        TextField fieldRect = new TextField();
        fieldRect.setPromptText("P1x;P2x;P1y;P2y");
        textFields.add(fieldRect);
        julMandOpt.add(fieldRect);

        //Choix de la constante
        VBox constantBox = new VBox();
        constantBox.setAlignment(Pos.CENTER);
        constantBox.setSpacing(7);

        Label labelConstant = new Label("Constante");
        Label labelIm = new Label("+ i");
        TextField fieldConstantR = new TextField();
        fieldConstantR.setPrefWidth(70);
        textFields.add(fieldConstantR);
        julMandOpt.add(fieldConstantR);

        TextField fieldConstantI = new TextField();
        fieldConstantI.setPrefWidth(70);
        textFields.add(fieldConstantI);
        julMandOpt.add(fieldConstantI);
        HBox constant = new HBox();

        VBox foncBox = new VBox();
        foncBox.setAlignment(Pos.CENTER);
        foncBox.setSpacing(7);
        Label labelFonc = new Label("Fonction");
        TextField fieldFonc = new TextField();
        textFields.add(fieldFonc);
        julMandOpt.add(fieldFonc);

        //Choix de l'ordre
        VBox orderBox = new VBox();
        orderBox.setSpacing(7);
        orderBox.setAlignment(Pos.CENTER);
        Label labelOrder = new Label("Ordre");
        fieldOrdre = new TextField();
        textFields.add(fieldOrdre);
        sierpOpt.add(fieldOrdre);

        //Choix du type de fractale
        VBox typeBox = new VBox();
        typeBox.setAlignment(Pos.CENTER);
        typeBox.setSpacing(7);
        Label labelType = new Label("Type de fractale");
        ChoiceBox<String> typechoice = new ChoiceBox<>(FXCollections.observableArrayList(
                "Julia", "Mandelbrot", "Sierpinski"));
        typechoice.setOnAction(e ->{
            File file ;
            Image image;
            ImageView iv;
            switch (typechoice.getValue()) {
                case "Julia" -> {
                    file = new File("Ressources/Julia.png");
                    lockAll();
                    unlockJulMand();
                    constantBox.setDisable(false);
                }
                case "Mandelbrot" -> {
                    file = new File("Ressources/Mandelbrot.png");
                    lockAll();
                    unlockJulMand();
                    constantBox.setDisable(true);
                }
                case "Sierpinski" -> {
                    file = new File("Ressources/Sierpinski.png");
                    lockAll();
                    unlockSierp();
                }
                default -> file = new File("Ressources/White.png");
            }
            colorChoice.setDisable(false);
            image = new Image(file.toURI().toString());
            iv = new ImageView(image);
            previewImage.getChildren().clear();
            previewImage.getChildren().add(iv);

        });

        lockAll();

        //Bouton de confirmation
        Button createButton = new Button("Creer votre fractale");
        createButton.setOnAction(e -> {
            ArrayList<String> opt = new ArrayList<>();
            if(typechoice.getValue() == null) {
                showAlertType();
                return;
            }
            opt.add(typechoice.getValue());
            opt.add(fieldFic.getText());
            opt.add(colorChoice.getValue());

            switch (typechoice.getValue()){
                case "Julia","Mandelbrot" -> {
                    opt.add(fieldConstantR.getText() + ";" + fieldConstantI.getText());
                    opt.add(fieldRect.getText());
                    opt.add(fieldPas.getText());
                    opt.add(fieldFonc.getText());
                    opt.add(fieldIter.getText());
                }
                case "Sierpinski" -> opt.add(fieldOrdre.getText());

            }
            showAlertOpt(control.fractalLaunch(opt));
        });

        //Bouton de retour
        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> mainMenu());


        /* **************************** *
         * Setup des éléments du layout *
         * **************************** */

        //Titre
        GridPane.setHalignment(labelTitle, HPos.CENTER);
        root.add(labelTitle, 0, 0, 3, 1);

        //Type
        typeBox.getChildren().addAll(labelType,typechoice);
        root.add(typeBox,1,1,2,1);

        //Pas
        pasBox.getChildren().addAll(labelPas,fieldPas);
        root.add(pasBox,1,2,1,1);
        fieldPas.getParent().requestFocus();

        //Itération
        iterBox.getChildren().addAll(labelIter,fieldIter);
        root.add(iterBox,2,2,1,1);

        //Couleur
        GridPane.setHalignment(colorBox, HPos.CENTER);
        colorBox.getChildren().addAll(labelColor,colorChoice);
        root.add(colorBox,1,3,1,1);

        //Nom du fichier
        ficBox.getChildren().addAll(labelFic,fieldFic);
        root.add(ficBox,2,3,1,1);

        //Constante
        GridPane.setHalignment(constantBox, HPos.CENTER);
        HBox.setMargin(labelIm, new Insets(0,10,0,10));
        constant.getChildren().addAll(fieldConstantR,labelIm,fieldConstantI);
        constantBox.getChildren().addAll(labelConstant,constant);
        root.add(constantBox,1,4,1,1);

        //Fonction
        foncBox.getChildren().addAll(labelFonc,fieldFonc);
        root.add(foncBox,2,4,1,1);

        //Ordre
        orderBox.getChildren().addAll(labelOrder,fieldOrdre);
        root.add(orderBox, 2,5,1,1);

        //Rectangle de visualisation
        rectBox.getChildren().addAll(labelRect,fieldRect);
        root.add(rectBox,1,5,1,1);
        fieldRect.getParent().requestFocus();

        //Bouton de confirmation
        GridPane.setHalignment(createButton, HPos.CENTER);
        root.add(createButton, 1, 6,2,1);

        //Bouton de retour
        GridPane.setHalignment(backButton, HPos.CENTER);
        root.add(backButton,1,7,2,1);

        previewImage.setMinSize(500,500);
        previewImage.setPrefSize(500,500);
        root.add(previewImage,0,1,1,6);
        Scene scene = new Scene(root,1000,600);
        mainStage.setScene(scene);
        mainStage.setAlwaysOnTop(false);
        mainStage.show();
    }

    /**
     * Creation du panneau de commande lors de l'affichage d'une fractale
     * @return La VBox correspondant au panneau de commande
     */
    public VBox createControlPanel(){
        previewImage.getChildren().clear();

        //Bouton de retour
        VBox main = new VBox();
        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> showSetMenu());

        //Bouton de sauvegarde
        Button saveButton = new Button("Sauvegarder cette fractale");
        saveButton.setOnAction(e -> control.saveImg());

        //Boutons de zoom et de dézoom
        Button zoomIn,zoomOut;
        zoomIn = new Button("+");
        zoomIn.setOnAction(e -> control.requestZoomIn());


        zoomOut = new Button("-");
        zoomOut.setMinSize(zoomIn.getWidth(), zoomIn.getHeight());
        zoomOut.setOnAction(e -> control.requestZoomOut());

        HBox zoomBox = new HBox();
        zoomBox.setAlignment(Pos.CENTER);
        zoomBox.setSpacing(5);
        zoomBox.getChildren().addAll(zoomIn,zoomOut);


        main.setAlignment(Pos.CENTER);
        main.setSpacing(10);
        GridPane.setHalignment(saveButton,HPos.CENTER);
        GridPane.setHalignment(backButton,HPos.CENTER);
        main.getChildren().addAll(zoomBox,saveButton,backButton);
        return main;
    }

    /**
     * Fonction qui réduit la taille de l'image si elle est trop grande pour l'écran de l'utilisateur
     * @param width Largeur de l'image originale
     * @param height Hauteur de l'image originale
     * @param image L'image originale
     * @return L'image originale redimensionée ou l'image originale
     */
    public WritableImage resizeIfNecessary(int width, int height, WritableImage image){
        WritableImage newImg = image;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenWidth = screenSize.getWidth();
        double screenHeight = screenSize.getHeight() - 50;
        double ratioW = screenWidth / width;
        double ratioH = screenHeight / height;
        if (ratioW < 1 || ratioH < 1){

            BufferedImage buffImage = SwingFXUtils.fromFXImage(newImg,null);
            java.awt.Image resultingImage;
            BufferedImage outputImage;
            if(ratioW < ratioH){
                resultingImage = buffImage.getScaledInstance((int) screenWidth, (int) (ratioW * height), java.awt.Image.SCALE_SMOOTH);
                outputImage = new BufferedImage((int) screenWidth, (int) (ratioW * height), BufferedImage.TYPE_INT_RGB);
            }else{
                resultingImage = buffImage.getScaledInstance((int) (ratioH * width), (int) screenHeight, java.awt.Image.SCALE_SMOOTH);
                outputImage = new BufferedImage((int) (ratioH * width), (int) screenHeight, BufferedImage.TYPE_INT_RGB);
            }
            outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
            newImg = SwingFXUtils.toFXImage(outputImage,null);
        }
        return newImg;
    }

    /**
     * Affiche la fractale de Sierpinski donnée en paramètre.
     * @param fractal La fractale de Sierpinski à afficher
     */
    public void showFractalS(Fractal fractal){
        double[][] tab_ind = fractal.getTableau();
        int tabLength = ((Sierpinski)fractal).getTab().length;
        WritableImage image = new WritableImage(tabLength,tabLength);
        ImageView view = new ImageView();
        for (int i = 0;i<tabLength;i++){
            for (int j = 0; j< tabLength;j++){
                int c = fractal.coloration((int)tab_ind[i][j]);
                if (((Sierpinski)fractal).getTab()[i][j] == 0){
                    image.getPixelWriter().setArgb(i,j,c);
                } else {
                    image.getPixelWriter().setArgb(i,j, java.awt.Color.BLACK.getRGB());
                }
            }
        }
        fractImg.setImage(image);
        image = resizeIfNecessary(tabLength,tabLength, image);
        view.setImage(image);
        HBox pane = new HBox();
        pane.getChildren().addAll(view,createControlPanel());
        Scene scene = new Scene(pane, Math.min(tab_ind.length, image.getWidth()) + 175, Math.min(tab_ind[0].length, image.getHeight()));
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()){
                case Z -> control.requestMove("UP");
                case S -> control.requestMove("DOWN");
                case Q -> control.requestMove("LEFT");
                case D -> control.requestMove("RIGHT");
            }
        });
        mainStage.setAlwaysOnTop(true);
        mainStage.setScene(scene);
        mainStage.show();
    }

    /**
     * Affiche la fractale de Julia ou de Mandelbrot donnée en paramètre.
     * @param fractal La fractale à afficher
     */
    public void showFractalJM(Fractal fractal){
        double[][] tab_index = fractal.getTableau();
        WritableImage image = new WritableImage(tab_index[0].length, tab_index.length);
        ImageView view = new ImageView();
        for (int i = 0;i< tab_index.length;i++) {
            for (int j = 0; j < tab_index[0].length; j++) {
                int c = fractal.coloration((int) tab_index[i][j]);
                image.getPixelWriter().setArgb(j, i, c);
            }
        }
        fractImg.setImage(image);
        image = resizeIfNecessary(tab_index[0].length, tab_index.length, image);
        view.setImage(image);
        HBox pane = new HBox();
        HBox.setMargin(pane,new Insets(5));
        pane.getChildren().addAll(view,createControlPanel());
        Scene scene = new Scene(pane, Math.min(tab_index.length, image.getWidth()) + 175, Math.min(tab_index[0].length, image.getHeight()));
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()){
                case Z -> control.requestMove("UP");
                case S -> control.requestMove("DOWN");
                case Q -> control.requestMove("LEFT");
                case D -> control.requestMove("RIGHT");
                case PLUS -> control.requestZoomIn();
                case MINUS -> control.requestZoomOut();
            }
        });
        mainStage.setScene(scene);
        mainStage.setAlwaysOnTop(true);
        mainStage.show();
    }

    /**
     * Affiche le Menu principal
     */
    public void mainMenu(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(15);

        Label titleLabel = new Label("Fractales");
        titleLabel.setFont(new Font("Arial",50));
        GridPane.setHalignment(titleLabel, HPos.CENTER);
        grid.add(titleLabel,0,0,2,1);

        VBox createBox = new VBox();
        createBox.setAlignment(Pos.CENTER);
        createBox.setOnMouseClicked(e -> showSetMenu());
        ImageView createImg = new ImageView();
        createImg.setImage(new Image(new File("Ressources/create.png").toURI().toString()));
        Label createLabel = new Label("Creez une fractale !");
        createLabel.setFont(new Font(15));
        createBox.getChildren().addAll(createImg,createLabel);
        GridPane.setHalignment(createBox,HPos.CENTER);
        GridPane.setValignment(createBox, VPos.CENTER);

        VBox howToBox = new VBox();
        howToBox.setAlignment(Pos.CENTER);
        howToBox.setOnMouseClicked(e -> howToCreateScreen());
        ImageView howToImg = new ImageView();
        howToImg.setImage(new Image(new File("Ressources/howTo.png").toURI().toString()));
        Label howToLabel = new Label("Comment creer une fractale ?");
        howToLabel.setFont(new Font(15));
        howToBox.getChildren().addAll(howToImg,howToLabel);
        GridPane.setHalignment(howToBox,HPos.CENTER);
        GridPane.setValignment(howToBox, VPos.CENTER);

        VBox exempleBox = new VBox();
        exempleBox.setAlignment(Pos.CENTER);
        exempleBox.setOnMouseClicked(e -> galleryScreen());
        ImageView exempleImg = new ImageView();
        exempleImg.setImage(new Image(new File("Ressources/gallery.png").toURI().toString()));
        Label exempleLabel = new Label("Exemples de fractales !");
        exempleLabel.setFont(new Font(15));
        exempleBox.getChildren().addAll(exempleImg,exempleLabel);
        GridPane.setHalignment(exempleBox,HPos.CENTER);
        GridPane.setValignment(exempleBox, VPos.CENTER);

        VBox explainBox = new VBox();
        explainBox.setOnMouseClicked(e -> presentationScreen());
        explainBox.setAlignment(Pos.CENTER);
        ImageView explainImg = new ImageView();
        explainImg.setImage(new Image(new File("Ressources/explanations.png").toURI().toString()));
        Label explainLabel = new Label("Qu'est ce qu'une fractale ?");
        explainLabel.setFont(new Font(15));
        explainBox.getChildren().addAll(explainImg,explainLabel);
        GridPane.setHalignment(explainBox,HPos.CENTER);
        GridPane.setValignment(explainBox, VPos.CENTER);

        grid.add(createBox,1,1);
        grid.add(exempleBox,0,1);
        grid.add(howToBox,1,2);
        grid.add(explainBox,0,2);

        grid.setStyle("-fx-background-color: radial-gradient(center 50% 50% , radius 90% , #BEE8F2, #000000);");
        mainStage.setScene(new Scene(grid,800,500));
        mainStage.show();
    }

    /**
     * Affiche la gallerie de fractales intéressantes
     */
    public void galleryScreen(){
        VBox root = new VBox();
        ScrollPane gallery = new ScrollPane();

        HBox images = new HBox();
        HBox.setHgrow(gallery,Priority.ALWAYS);
        ImageView curr = new ImageView();

        /*
         * Récupère toutes les images contenues dans le dossier Gallery et les ajoute à l'HBox
         */
        try {
            List<File> files = Files.list(Paths.get("Ressources/Gallery"))
                    .map(Path::toFile).collect(Collectors.toList());

            for (File file : files){
                curr = new ImageView();
                curr.setImage(new Image(file.toURI().toString()));
                images.getChildren().add(curr);
            }
        } catch (IOException e) {
            System.out.println("Erreur de lecture");
        }

        if (curr.getImage() == null){
            System.out.println("Error");
            return;
        }

        gallery.setContent(images);

        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> mainMenu());
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);
        root.setStyle("-fx-background-color: radial-gradient(center 50% 50% , radius 90% , #BEE8F2, #000000);");

        root.getChildren().addAll(gallery,backButton);

        mainStage.setScene(new Scene(root,1000,500));
        mainStage.show();
    }

    /**
     * Affiche la présentation de fractales
     */
    public void presentationScreen(){
        GridPane grid = new GridPane();

        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> mainMenu());

        VBox juliaBox = new VBox();

        ImageView juliaImg = new ImageView(new Image(new File("Ressources/JuliaExpl.png").toURI().toString()));
        Text juliaText = new Text("""
                    L'ensemble de Julia est un ensemble defini a partir
                    du comportement d'une fonction. Ainsi pour une
                    fonction f, et une valeur donnee c, cela
                    correspond a l'ensemble des valeurs pour
                    lequelles la suite est bornee.
                """);
        juliaText.setFont(new Font(13));
        VBox.setMargin(juliaImg,new Insets(5));
        VBox.setMargin(juliaText,new Insets(5));
        juliaBox.getChildren().addAll(juliaImg,juliaText);
        VBox mandelBox = new VBox();

        ImageView mandelImg = new ImageView(new Image(new File("Ressources/MandelbrotExpl.png").toURI().toString()));
        Text mandelText = new Text("""
              L'ensemble de Mandelbrot est l'ensemble des point
              correspondant a un ensemble de Julia. Si un
              ensemble de Julia appartient a l'ensemble de
              Mandelbrot, cela signifie qu'il s'agit d'un
              ensemble connexe.
              """);
        mandelText.setFont(new Font(13));
        VBox.setMargin(mandelImg,new Insets(5));
        VBox.setMargin(mandelText,new Insets(5));
        mandelBox.getChildren().addAll(mandelImg,mandelText);
        VBox sierpBox = new VBox();

        ImageView sierpImg = new ImageView(new Image(new File("Ressources/SierpinskiExpl.png").toURI().toString()));
        Text sierpText = new Text("""
                     Concernant le Tapis de Sierpinski, il s'agit d'une
                     generalisation de l'ensemble de Cantor en 2D.
                     C'est une fractale obtenue en partant d'un carre,
                     ou, a chaque etape, on decoupe le carre initial
                     en 9 carres egaux grace a une grille de 3x3,
                     et ou on supprime le carre central.\s
                     On reitere ce processus sur les 8 autres carres
                     restant selon l'ordre voulu.
                """);
        sierpText.setFont(new Font(13));
        VBox.setMargin(sierpImg,new Insets(5));
        VBox.setMargin(sierpText,new Insets(5));
        sierpBox.getChildren().addAll(sierpImg,sierpText);

        grid.setHgap(30);

        grid.add(juliaBox,0,0);
        grid.add(mandelBox,1,0);
        grid.add(sierpBox,2,0);
        grid.add(backButton,1,1);

        grid.setAlignment(Pos.TOP_CENTER);
        grid.setStyle("-fx-background-color: radial-gradient(center 50% 50% , radius 90% , #BEE8F2, #000000);");
        GridPane.setHalignment(backButton,HPos.CENTER);

        mainStage.setScene(new Scene(grid,1000,600));
        mainStage.show();
    }

    /**
     * Affiche l'écran d'information sur les paramètres à rentrer
     * pour afficher une fractale.
     */
    public void howToCreateScreen(){
        GridPane grid = new GridPane();

        Text howToText = new Text("""
                Pour creer votre fractale :
                Choisissez le type de la fractale, si voulez :
                
                   -Une fractale de Julia ou Mandelbrot, alors veulliez rentrer :
                       -La partie reelle et imaginaire de la constante de la fonction
                       -La fonction pour la fractale :
                       Chaque monome est sous forme : "x(degre du monome)"   x²+5 -> x2+5
                       -Le nombre d'iterations de la fonction
                       -Le pas de discretisation qui doit etre strictement positif et
                        inferieur a la difference entre l'abscisse ou l'ordonnee des 2 points du rectangle
                       -Les bornes du rectangle a afficher sous la forme "(Abscisse point 1);(Abscisse point 2);(Ordonnee point 1);(Ordonnee point 2)"
                       
                   -Une fractale du tapis de Sierpinski, alors veuillez rentrer :
                        -L'ordre du Tapis de Sierpinski
                       
                   -Le nom du fichier de l'image
                   -La couleur de l'image désiree
                """);

        Button backButton = new Button("Retour");
        backButton.setOnAction(e -> mainMenu());
        grid.add(howToText,0,0);

        GridPane.setHalignment(backButton,HPos.CENTER);
        grid.add(backButton,0,1);
        grid.setStyle("-fx-background-color: radial-gradient(center 50% 50% , radius 90% , #BEE8F2, #000000);");

        mainStage.setScene(new Scene(grid,700,350));
        mainStage.show();
    }

    /**
     * Affiche une boite de dialogue si des paramètres n'ont pas été rentrés sous la bonne forme
     * @param errOpt La liste de champs ayant une erreur
     */
    public void showAlertOpt(ArrayList<String> errOpt){
        if (errOpt.isEmpty()){
            return;
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Champs non valides");
        alert.setHeaderText("Les champs suivants sont invalides :");
        alert.setContentText("");
        errOpt.forEach(e -> {
            switch (e) {
                case "cst" -> alert.setContentText(alert.getContentText() + "Constante\n");
                case "r" -> alert.setContentText(alert.getContentText() + "Rectangle de visualisation\n");
                case "p" -> alert.setContentText(alert.getContentText() + "Pas de discretisation\n");
                case "fo" -> alert.setContentText(alert.getContentText() + "Fonction\n");
                case "it" -> alert.setContentText(alert.getContentText() + "Nombre d'iterations\n");
                case "o" -> alert.setContentText(alert.getContentText() + "Ordre\n");
                case "rp" -> alert.setContentText(alert.getContentText() + "Pas et Rectangle incompatibles\n");
            }
        });
        alert.showAndWait();
    }

    /**
     * Affiche une alerte si l'utilisateur essaye de créer une fractale sans choisir le type
     */
    public void showAlertType(){
        Alert typeAlert = new Alert(Alert.AlertType.ERROR);
        typeAlert.setTitle("Fractal type is null");
        typeAlert.setHeaderText("Choose a fractal type!");
        typeAlert.showAndWait();
    }

    public void setMainStage(Stage mainStage) {
        this.mainStage = mainStage;
    }

    public ImageView getFractImg() {
        return fractImg;
    }
}


