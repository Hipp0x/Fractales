package Application;

import View.ViewFX;
import Controller.ControllerG;

import javafx.application.Application;
import javafx.stage.Stage;


public class UIApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Fractals");
        stage.toFront();

        ControllerG control = new ControllerG();
        ViewFX view = new ViewFX(control);
        view.setMainStage(stage);
        control.setView(view);

        view.mainMenu();
    }

    public void launchGraph(){
        launch();
    }

    public static void main(String[] args){
        launch();
    }
}
