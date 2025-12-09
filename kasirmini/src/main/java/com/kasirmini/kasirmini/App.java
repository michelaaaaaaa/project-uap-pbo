package com.kasirmini.kasirmini;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;
    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        Parent root = loadFXML("login");
        // BUAT SCENE SEDIKIT LEBIH BESAR
        scene = new Scene(root, 800, 600);

        stage.setScene(scene);
        stage.setTitle("Kasir Mini");

        // optional: batasi ukuran minimum agar tampilan tidak diperkecil sampai kepotong
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        Parent root = loadFXML(fxml);
        scene.setRoot(root);

        // SESUAIKAN UKURAN STAGE DENGAN SCENE BARU
        if (primaryStage != null) {
            primaryStage.sizeToScene();
        }
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
