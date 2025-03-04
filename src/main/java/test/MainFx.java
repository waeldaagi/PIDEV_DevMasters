package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFx extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MainFx.fxml"));
            Parent root = loader.load();
            
            // Créer la scène
            Scene scene = new Scene(root);
            
            // Configurer la fenêtre principale
            primaryStage.setTitle("Gestion des Projets et Équipes");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}